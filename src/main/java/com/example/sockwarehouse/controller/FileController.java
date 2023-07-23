package com.example.sockwarehouse.controller;

import com.example.sockwarehouse.service.FileService;
import com.example.sockwarehouse.service.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/socks/file")
@Tag(name = "Файлы", description = "Операции для работы с файлами носков")
@ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны")
public class FileController {

    private final SocksService socksService;
    private final FileService fileService;

    @GetMapping("/export")
    @Operation(summary = "Загрузка всех носков в виде json-файла")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен"),
            @ApiResponse(responseCode = "204", description = "Файл пуст")
    })
    public ResponseEntity<InputStreamResource> downloadSocksFile() throws FileNotFoundException {
        File file = socksService.getSocksFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Socks\"")
                    .body(resource);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export/operations")
    @Operation(summary = "Загрузка всех операций над носками в виде json-файла")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен"),
            @ApiResponse(responseCode = "204", description = "Файл пуст")
    })
    public ResponseEntity<InputStreamResource> downloadSocksOperationsFile() throws FileNotFoundException {
        File file = socksService.getSocksOperationsFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SocksOperations\"")
                    .body(resource);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Замена сохраненного на локальном диске json-файла с носками на новый")
    @ApiResponse(responseCode = "200", description = "Запрос выполнен")
    public ResponseEntity<Void> uploadSocksFile(@RequestBody MultipartFile multipartFile) {
        fileService.cleanFile(socksService.socksFileUrl());
        File file = socksService.getSocksFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            IOUtils.copy(multipartFile.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().build();
    }

    @PostMapping(value = "/import/operations", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Замена сохраненного на локальном диске json-файла с операциями над носками на новый")
    @ApiResponse(responseCode = "200", description = "Запрос выполнен")
    public ResponseEntity<Void> uploadSocksOperationsFile(@RequestBody MultipartFile multipartFile) {
        fileService.cleanFile(socksService.socksOperationsFileUrl());
        File file = socksService.getSocksOperationsFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            IOUtils.copy(multipartFile.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().build();
    }
}
