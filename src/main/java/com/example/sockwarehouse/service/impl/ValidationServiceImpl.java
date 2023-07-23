package com.example.sockwarehouse.service.impl;

import com.example.sockwarehouse.model.Color;
import com.example.sockwarehouse.model.Size;
import com.example.sockwarehouse.model.SocksBatch;
import com.example.sockwarehouse.service.ValidationService;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public boolean validate(SocksBatch socksBatch) {
        return socksBatch != null
                && socksBatch.getSocks() != null
                && socksBatch.getSocks().getColor() != null
                && socksBatch.getSocks().getSize() != null
                && checkCottonPart(socksBatch.getSocks().getCottonPart(), socksBatch.getSocks().getCottonPart())
                && socksBatch.getQuantity() > 0;
    }

    @Override
    public boolean validate(Color color, Size size, int cottonMin, int cottonMax) {
        return color != null
                && size != null
                && checkCottonPart(cottonMin, cottonMax);
    }

    private boolean checkCottonPart(int cottonMin, int cottonMax) {
        return cottonMin >= 0
                && cottonMin <= 100
                && cottonMax >= 0
                && cottonMax <= 100;
    }
}
