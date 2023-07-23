package com.example.sockwarehouse.service;

import com.example.sockwarehouse.model.Color;
import com.example.sockwarehouse.model.Size;
import com.example.sockwarehouse.model.SocksBatch;

/**
 * Сервис валидации
 */
public interface ValidationService {

    /**
     * Валидирует партию носков
     *
     * @param socksBatch объект {@link SocksBatch}
     * @return <code>true</code> если партия корректная, <code>false</code> если партия не корректная
     */
    boolean validate(SocksBatch socksBatch);

    /**
     * Валидирует параметры носков
     *
     * @param color     цвет носков
     * @param size      размер носков
     * @param cottonMin минимальное значение хлопка
     * @param cottonMax максимальное значение хлопка
     * @return <code>true</code> если параметры корректные, <code>false</code> если параметры не корректные
     */
    boolean validate(Color color, Size size, int cottonMin, int cottonMax);
}
