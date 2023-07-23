package com.example.sockwarehouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Партия носков
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocksBatch {
    private long quantity;
    private Socks socks;
}
