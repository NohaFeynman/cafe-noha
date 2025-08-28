package com.cafe.controller;

import com.cafe.model.OrderConfig;
import com.cafe.model.OrderSession;
import com.cafe.model.RatingForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@Controller
@RequestMapping("/order")
public class OrderController {

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("orderConfig", new OrderConfig()); // POJO para data binding
        return "order-form";
    }

    @PostMapping("/submit")
    public String procesarPedido(@ModelAttribute("orderConfig") OrderConfig cfg, Model model) {
        // Montos de ejemplo (ajústalos “a discreción del estudiante”)
        BigDecimal precioBaseCafePeq = new BigDecimal("6.0");
        BigDecimal precioBaseCafeMed = new BigDecimal("8.0");
        BigDecimal precioBaseCafeGra = new BigDecimal("10.0");
        BigDecimal extraCafe = new BigDecimal("1.0");

        BigDecimal precioBaseTePeq = new BigDecimal("5.0");
        BigDecimal precioBaseTeMed = new BigDecimal("6.5");
        BigDecimal precioBaseTeGra = new BigDecimal("8.0");
        BigDecimal extraTe = new BigDecimal("0.5");

        BigDecimal precioBasePostrePeq = new BigDecimal("7.0");
        BigDecimal precioBasePostreMed = new BigDecimal("9.0");
        BigDecimal precioBasePostreGra = new BigDecimal("11.0");
        BigDecimal extraPostre = new BigDecimal("1.5");

        OrderSession ses = new OrderSession();
        ses.setNombreCliente(cfg.getNombreCliente());
        ses.setNumeroMesa(cfg.getNumeroMesa());

        // Normaliza nulls a 0 para evitar NPE en binding inicial
        int cc = nullAsZero(cfg.getCafeCantidad());
        int ca = nullAsZero(cfg.getCafeAdicionales());
        int tc = nullAsZero(cfg.getTeCantidad());
        int ta = nullAsZero(cfg.getTeAdicionales());
        int pc = nullAsZero(cfg.getPostreCantidad());
        int pa = nullAsZero(cfg.getPostreAdicionales());

        // Café
        BigDecimal precioCafeBase = switch (safe(cfg.getCafeTamano())) {
            case "mediano" -> precioBaseCafeMed;
            case "grande"  -> precioBaseCafeGra;
            default        -> precioBaseCafePeq;
        };
        BigDecimal cafeSubtotal = precioCafeBase.multiply(BigDecimal.valueOf(cc))
                .add(extraCafe.multiply(BigDecimal.valueOf(ca)));
        ses.setCafeCantidad(cc);
        ses.setCafeTamano(safe(cfg.getCafeTamano()));
        ses.setCafeAdicionales(ca);
        ses.setCafeSubtotal(cafeSubtotal);

        // Té
        BigDecimal precioTeBase = switch (safe(cfg.getTeTamano())) {
            case "mediano" -> precioBaseTeMed;
            case "grande"  -> precioBaseTeGra;
            default        -> precioBaseTePeq;
        };
        BigDecimal teSubtotal = precioTeBase.multiply(BigDecimal.valueOf(tc))
                .add(extraTe.multiply(BigDecimal.valueOf(ta)));
        ses.setTeCantidad(tc);
        ses.setTeTamano(safe(cfg.getTeTamano()));
        ses.setTeAdicionales(ta);
        ses.setTeSubtotal(teSubtotal);

        // Postre
        BigDecimal precioPostreBase = switch (safe(cfg.getPostreTamano())) {
            case "mediano" -> precioBasePostreMed;
            case "grande"  -> precioBasePostreGra;
            default        -> precioBasePostrePeq;
        };
        BigDecimal postreSubtotal = precioPostreBase.multiply(BigDecimal.valueOf(pc))
                .add(extraPostre.multiply(BigDecimal.valueOf(pa)));
        ses.setPostreCantidad(pc);
        ses.setPostreTamano(safe(cfg.getPostreTamano()));
        ses.setPostreAdicionales(pa);
        ses.setPostreSubtotal(postreSubtotal);

        BigDecimal total = cafeSubtotal.add(teSubtotal).add(postreSubtotal);
        ses.setTotal(total);

        // Para la vista:
        model.addAttribute("resumen", ses);
        model.addAttribute("ratingForm", new RatingForm()); // formulario de rating
        return "order-summary";
    }

    @PostMapping("/rate")
    public String calificar(@ModelAttribute("ratingForm") RatingForm rating, Model model) {
        // No se persiste; solo confirmación
        model.addAttribute("mensaje",
                "¡Gracias por tu calificación de " + rating.getEstrellas() + "★!");
        model.addAttribute("comentario", rating.getComentario());
        return "order-summary"; // reusar plantilla mostrando el mensaje
    }

    private static int nullAsZero(Integer x) { return x == null ? 0 : x; }
    private static String safe(String s) { return s == null ? "pequeno" : s; }
}
