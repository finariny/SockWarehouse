package com.example.sockwarehouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Носки
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Socks {
    private Color color;
    private Size size;
    private int cottonPart;
}
