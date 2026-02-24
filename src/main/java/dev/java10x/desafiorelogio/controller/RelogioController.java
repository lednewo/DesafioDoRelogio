package dev.java10x.desafiorelogio.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.java10x.desafiorelogio.domain.dto.AtualizarRelogioRequest;
import dev.java10x.desafiorelogio.domain.dto.CriarRelogioRequest;
import dev.java10x.desafiorelogio.domain.dto.RelogioResponse;
import dev.java10x.desafiorelogio.domain.dto.ResultDefaultResponse;
import dev.java10x.desafiorelogio.domain.entity.PaginationEntity;
import dev.java10x.desafiorelogio.service.RelogioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/V1/api/relogios")
@RequiredArgsConstructor
public class RelogioController {

    private final RelogioService relogioService;

    @GetMapping("/getAllPaginated")
    public PaginationEntity<RelogioResponse> listar(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String tipoMovimento,
            @RequestParam(required = false) String tipoVidro,
            @RequestParam(required = false) String materialCaixa,
            @RequestParam(required = false) String ordenacao,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer resistenciaAguaMin,
            @RequestParam(required = false) Integer resistenciaAguaMax) {
        return relogioService.listar(termo, marca, tipoMovimento, tipoVidro, materialCaixa, ordenacao, size, page,
                resistenciaAguaMin, resistenciaAguaMax);
    }

    @GetMapping("/details")
    public ResponseEntity<RelogioResponse> buscarPorUuid(@RequestParam(name = "id", required = true) String uuid) {
        RelogioResponse relogioResponse = relogioService.buscarPorUuid(java.util.UUID.fromString(uuid));
        return ResponseEntity.ok(relogioResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<ResultDefaultResponse> criarRelogio(@Valid @RequestBody CriarRelogioRequest req) {
        relogioService.createRelogio(req);
        return ResponseEntity.ok(new ResultDefaultResponse("Relógio criado com sucesso", "201"));
    }

    @PutMapping("update")
    public ResponseEntity<ResultDefaultResponse> atualizarRelogio(@RequestParam(name = "id", required = true) UUID uuid,
            @Valid @RequestBody AtualizarRelogioRequest entity) {
        relogioService.atualizar(uuid, entity);
        return ResponseEntity.ok(new ResultDefaultResponse("Relógio atualizado com sucesso", "200"));
    }

}
