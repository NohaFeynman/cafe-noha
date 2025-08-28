package com.cafe.model;

import java.math.BigDecimal;

public class OrderSession {
    private String nombreCliente;
    private Integer numeroMesa;

    // Resumen por categoría
    private Integer cafeCantidad;
    private String cafeTamano;
    private Integer cafeAdicionales;
    private BigDecimal cafeSubtotal;

    private Integer teCantidad;
    private String teTamano;
    private Integer teAdicionales;
    private BigDecimal teSubtotal;

    private Integer postreCantidad;
    private String postreTamano;
    private Integer postreAdicionales;
    private BigDecimal postreSubtotal;

    // Total
    private BigDecimal total;

    // Getters/Setters
    // ...
}
