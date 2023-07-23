package com.example.sockwarehouse.service;

import com.example.sockwarehouse.model.Color;
import com.example.sockwarehouse.model.Size;
import com.example.sockwarehouse.model.SocksBatch;
import com.example.sockwarehouse.model.operation.SocksOperation;

import java.io.File;
import java.nio.file.Path;

/**
 * Сервис для работы с носками
 */
public interface SocksService {

    /**
     * Регистрирует приход носков на склад
     *
     * @param socksBatch объект {@link SocksBatch}
     * @return объект {@link SocksOperation}
     */
    SocksOperation accept(SocksBatch socksBatch);

    /**
     * Регистрирует отпуск носков со склада
     *
     * @param socksBatch объект {@link SocksBatch}
     * @return объект {@link SocksOperation}
     */
    SocksOperation issuance(SocksBatch socksBatch);

    /**
     * Возвращает общее количество носков на складе, соответствующих переданным в параметрах критериям запроса
     *
     * @param color     цвет носков
     * @param size      размер носков
     * @param cottonMin минимальное значение хлопка
     * @param cottonMax максимальное значение хлопка
     * @return общее количество носков
     */
    long getQuantity(Color color, Size size, int cottonMin, int cottonMax);

    /**
     * Регистрирует списание испорченных (бракованных) носков
     *
     * @param socksBatch объект {@link SocksBatch}
     * @return объект {@link SocksOperation}
     */
    SocksOperation reject(SocksBatch socksBatch);

    Path socksFileUrl();

    Path socksOperationsFileUrl();

    File getSocksFile();

    File getSocksOperationsFile();
}
