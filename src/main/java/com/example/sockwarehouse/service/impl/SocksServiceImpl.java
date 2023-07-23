package com.example.sockwarehouse.service.impl;

import com.example.sockwarehouse.exception.SocksValidationException;
import com.example.sockwarehouse.model.Color;
import com.example.sockwarehouse.model.Size;
import com.example.sockwarehouse.model.Socks;
import com.example.sockwarehouse.model.SocksBatch;
import com.example.sockwarehouse.model.operation.OperationType;
import com.example.sockwarehouse.model.operation.SocksOperation;
import com.example.sockwarehouse.service.FileService;
import com.example.sockwarehouse.service.SocksService;
import com.example.sockwarehouse.service.ValidationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SocksServiceImpl implements SocksService {

    @Value("${path.to.socks.file}")
    private String socksFilePath;
    @Value("${name.of.socks.file}")
    private String socksFileName;
    @Value("${path.to.socks.operations.file}")
    private String socksOperationsFilePath;
    @Value("${name.of.socks.operations.file}")
    private String socksOperationsFileName;

    private static List<SocksOperation> socksOperations = new LinkedList<>();
    private static Map<Socks, Long> socksMap = new HashMap<>();

    private final ValidationService validationService;
    private final FileService fileService;

    private final ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        objectMapper.findAndRegisterModules();
        if (Files.exists(socksFileUrl())) {
            readFromSocksFile();
        }
        if (Files.exists(socksOperationsFileUrl())) {
            readFromSocksOperationsFile();
        }
    }

    @Override
    public SocksOperation accept(SocksBatch socksBatch) {
        if (!validationService.validate(socksBatch)) {
            throw new SocksValidationException(socksBatch);
        }

        Socks socks = socksBatch.getSocks();
        long quantity = socksBatch.getQuantity();

        if (socksMap.containsKey(socks)) {
            socksMap.replace(socks, socksMap.get(socks) + quantity);
        } else {
            socksMap.put(socks, quantity);
        }
        saveToSocksFile();
        return createSocksOperation(OperationType.ACCEPT, socksBatch);
    }

    @Override
    public SocksOperation issuance(SocksBatch socksBatch) {
        if (!validationService.validate(socksBatch)) {
            throw new SocksValidationException(socksBatch);
        }
        if (remove(socksBatch) != 0) {
            return createSocksOperation(OperationType.ISSUANCE, socksBatch);
        }
        return null;
    }

    @Override
    public long getQuantity(Color color, Size size, int cottonMin, int cottonMax) {
        if (!validationService.validate(color, size, cottonMin, cottonMax)) {
            throw new SocksValidationException(color, size, cottonMin, cottonMax);
        }
        long socksQuantity = 0L;
        for (Map.Entry<Socks, Long> entry : socksMap.entrySet()) {
            Socks socks = entry.getKey();
            if (socks.getColor() == color
                    && socks.getSize() == size
                    && socks.getCottonPart() >= cottonMin
                    && socks.getCottonPart() <= cottonMax) {
                socksQuantity += entry.getValue();
            }
        }
        return socksQuantity;
    }

    @Override
    public SocksOperation reject(SocksBatch socksBatch) {
        if (!validationService.validate(socksBatch)) {
            throw new SocksValidationException(socksBatch);
        }
        if (remove(socksBatch) != 0) {
            return createSocksOperation(OperationType.REJECT, socksBatch);
        }
        return null;
    }

    @Override
    public Path socksFileUrl() {
        return Path.of(socksFilePath, socksFileName);
    }

    @Override
    public Path socksOperationsFileUrl() {
        return Path.of(socksOperationsFilePath, socksOperationsFileName);
    }

    @Override
    public File getSocksFile() {
        return new File(socksFilePath + "/" + socksFileName);
    }

    @Override
    public File getSocksOperationsFile() {
        return new File(socksOperationsFilePath + "/" + socksOperationsFileName);
    }

    private long remove(SocksBatch socksBatch) {
        Socks socks = socksBatch.getSocks();
        long quantity = socksBatch.getQuantity();

        long currentQuantity = socksMap.getOrDefault(socks, 0L);
        if (socksMap.containsKey(socks)) {
            if (currentQuantity > quantity) {
                socksMap.replace(socks, currentQuantity - quantity);
                saveToSocksFile();
            } else {
                socksMap.remove(socks);
                saveToSocksFile();
            }
        }
        return currentQuantity;
    }

    private SocksOperation createSocksOperation(OperationType operationType, SocksBatch socksBatch) {
        SocksOperation socksOperation = new SocksOperation(operationType, socksBatch);
        socksOperations.add(socksOperation);
        saveToSocksOperationsFile();
        return socksOperation;
    }

    private void saveToSocksFile() {
        try {
            String json = objectMapper.writeValueAsString(socksMap);
            fileService.saveToFile(socksFileUrl(), json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void saveToSocksOperationsFile() {
        try {
            String json = objectMapper.writeValueAsString(socksOperations);
            fileService.saveToFile(socksOperationsFileUrl(), json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void readFromSocksFile() {
        try {
            String json = fileService.readFromFile(socksFileUrl());
            socksMap = objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void readFromSocksOperationsFile() {
        try {
            String json = fileService.readFromFile(socksOperationsFileUrl());
            socksOperations = objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
