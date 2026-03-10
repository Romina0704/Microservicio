package com.microservicio.microserviciopedidos.controller;

import com.microservicio.microserviciopedidos.entidad.Proveedor;
import com.microservicio.microserviciopedidos.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public String listarProveedores(Model model) {
        model.addAttribute("proveedores", proveedorService.listarProveedores());
        return "proveedores";
    }

    @PostMapping("/agregar")
    public String agregarProveedor(Proveedor proveedor) {
        proveedorService.guardarProveedor(proveedor);
        return "redirect:/proveedores";
    }

    @PostMapping("/editar")
    public String editarProveedor(Proveedor proveedor) {
        proveedorService.guardarProveedor(proveedor);
        return "redirect:/proveedores";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
        return "redirect:/proveedores";
    }

    @GetMapping("/listar-activos")
    @ResponseBody
    public List<Proveedor> listarActivos() {
        return proveedorService.listarProveedores()
                .stream()
                .filter(Proveedor::isActivo)
                .collect(java.util.stream.Collectors.toList());
    }
}