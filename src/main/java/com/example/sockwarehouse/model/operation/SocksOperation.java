package com.example.sockwarehouse.model.operation;

import com.example.sockwarehouse.model.SocksBatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Операция над носками
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocksOperation {
    private OperationType operationType;
    private final LocalDateTime localDateTime = LocalDateTime.now();
    private SocksBatch socksBatch;
}
