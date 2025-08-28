package com.cafe.model;

public class OrderConfig {
    // Datos del cliente
    private String nombreCliente;
    private Integer numeroMesa;

    // Café
    private Integer cafeCantidad;       // 0 si no desea
    private String cafeTamano;          // "pequeno" | "mediano" | "grande"
    private Integer cafeAdicionales;    // número de adicionales (leche veg., sin azúcar, toppings)

    // Té
    private Integer teCantidad;
    private String teTamano;
    private Integer teAdicionales;

    // Postre
    private Integer postreCantidad;
    private String postreTamano;        // si aplica tamaño, si no puedes ignorarlo en la vista
    private Integer postreAdicionales;

    // Getters y Setters (obligatorios para el binding)
    // ... generar todos los getters/setters
    // Sugerencia: usa atajo "Alt+Insert" (o similar en IntelliJ) para generarlos rápido.
}
