package com.example.sockwarehouse.exception;

import com.example.sockwarehouse.model.Color;
import com.example.sockwarehouse.model.Size;
import com.example.sockwarehouse.model.SocksBatch;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка валидации носков
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SocksValidationException extends RuntimeException {

    public SocksValidationException(SocksBatch socksBatch) {
        super("Ошибка валидации: " + socksBatch);
    }

    public SocksValidationException(Color color, Size size, int cottonMin, int cottonMax) {
        super("Ошибка валидации: color=" + color + ", size=" + size + ", cottonMin=" + cottonMin + ", cottonMax=" + cottonMax);
    }
}
