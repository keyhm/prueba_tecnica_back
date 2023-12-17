package com.alianza.springboot.prueba.tecnica.controllers;

import com.alianza.springboot.prueba.tecnica.models.entity.Cliente;
import com.alianza.springboot.prueba.tecnica.models.services.IClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteRestController.class);
    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public List<Cliente> index() {
        logger.info("Endpoint '/api/clientes' invocado");
        List<Cliente> clientes = clienteService.findAll();
        logger.info("Número de clientes recuperados: {}", clientes.size());
        return clientes;
    }

    @GetMapping("/clientes/{shared_key}")
    public ResponseEntity<?> buscarCliente(@PathVariable String shared_key) {
        logger.info("Endpoint '/api/clientes/{}' invocado", shared_key);
        Cliente cliente = clienteService.findBySharedKey(shared_key);

        if (cliente != null) {
            logger.info("Cliente encontrado: {}", cliente);
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } else {
            logger.warn("Cliente no encontrado para shared_key: {}", shared_key);
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> crearNuevoCliente(@RequestBody Cliente cliente) {
        try {
            logger.info("Endpoint '/api/create' invocado para crear un nuevo cliente");
            cliente.setData_added(new Date());
            Cliente nuevoCliente = this.clienteService.createNewClient(cliente);
            logger.info("Nuevo cliente creado: {}", nuevoCliente);
            return new ResponseEntity<>("Cliente creado con éxito. Shared Key: " + nuevoCliente.getShared_key(), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error al crear el cliente", e);
            return new ResponseEntity<>("Error al crear el cliente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
