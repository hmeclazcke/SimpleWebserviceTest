package com.hmeclazcke.simplewebservicetest.controller;

import com.hmeclazcke.simplewebservicetest.dto.TransferRequest;
import com.hmeclazcke.simplewebservicetest.dto.TransferResponse;
import com.hmeclazcke.simplewebservicetest.service.TransferService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// CONTROLLER:
// Esta capa recibe llamadas HTTP.
// Ejemplo:
// POST /api/cuentas
// GET  /api/cuentas
// POST /api/transferencias
@RestController
@RequestMapping("/api")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public TransferResponse transfer(@RequestBody TransferRequest request) {
        return transferService.transfer(
                request.originId(),
                request.destinationId(),
                request.amount()
        );
    }

    @GetMapping("/transfer")
    public List<TransferResponse> listTransfers() {
        return transferService.listTransfers();
    }

}
