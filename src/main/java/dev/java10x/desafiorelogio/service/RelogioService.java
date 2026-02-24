package dev.java10x.desafiorelogio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import dev.java10x.desafiorelogio.domain.dto.AtualizarRelogioRequest;
import dev.java10x.desafiorelogio.domain.dto.CriarRelogioRequest;
import dev.java10x.desafiorelogio.domain.dto.RelogioResponse;
import dev.java10x.desafiorelogio.domain.dto.ResultDefaultResponse;
import dev.java10x.desafiorelogio.domain.dto.mapper.RelogioMapper;
import dev.java10x.desafiorelogio.domain.entity.PaginationEntity;
import dev.java10x.desafiorelogio.domain.entity.RelogioEntity;
import dev.java10x.desafiorelogio.domain.enums.MaterialCaixaEnum;
import dev.java10x.desafiorelogio.domain.enums.TipoMovimentoEnum;
import dev.java10x.desafiorelogio.domain.enums.TipoVidroEnum;
import dev.java10x.desafiorelogio.domain.exception.RelogioNotFoundException;
import dev.java10x.desafiorelogio.repository.RelogioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RelogioService {

        private final RelogioRepository relogioRepository;
        private final RelogioMapper relogioMapper;

        public PaginationEntity<RelogioResponse> listar(
                        String termo,
                        String marca,
                        String tipoMovimento,
                        String tipoVidro,
                        String materialCaixa,
                        String ordenacao,
                        int size,
                        int page,
                        Integer resistenciaAguaMin,
                        Integer resistenciaAguaMax) {

                TipoMovimentoEnum tipoMovimentoEnum = TipoMovimentoEnum.fromApi(tipoMovimento);
                MaterialCaixaEnum materialCaixaEnum = MaterialCaixaEnum.fromApi(materialCaixa);
                TipoVidroEnum tipoVidroEnum = TipoVidroEnum.fromApi(tipoVidro);
                OrdenacaoRelogiosEnum ordenacaoRelogios = OrdenacaoRelogiosEnum.fromApi(ordenacao);

                Sort sort = switch (ordenacaoRelogios) {
                        case MAIS_RECENTES -> Sort.by(Sort.Direction.DESC, "criadoEm");
                        case PRECO_CRESC -> Sort.by(Sort.Direction.ASC, "precoEmCentavos");
                        case PRECO_DESC -> Sort.by(Sort.Direction.DESC, "precoEmCentavos");
                        case DIAMETRO_CRESC -> Sort.by(Sort.Direction.ASC, "diametroMm");
                        case RESISTENCIA_DESC -> Sort.by(Sort.Direction.DESC, "resistenciaAguaM");
                };

                Pageable pageable = PageRequest.of(page, size, sort);

                Specification<RelogioEntity> specs = Specification.where(RelogioSpecs.busca(termo))
                                .and(RelogioSpecs.tipoMovimento(tipoMovimentoEnum))
                                .and(RelogioSpecs.tipoVidroIagual(tipoVidroEnum))
                                .and(RelogioSpecs.materialCaixaIgual(materialCaixaEnum))
                                .and(RelogioSpecs.resistenciaAguaEntre(resistenciaAguaMin, resistenciaAguaMax));

                Page<RelogioEntity> relogiosPage = relogioRepository.findAll(
                                specs,
                                pageable);

                List<RelogioEntity> relogioEntities = relogiosPage.getContent();
                List<RelogioResponse> relogioResponseList = new ArrayList<>();
                for (RelogioEntity r : relogioEntities) {
                        RelogioResponse response = relogioMapper.tRelogioResponse(r);
                        if (response != null) {
                                relogioResponseList.add(response);
                        }
                }

                return new PaginationEntity<>(
                                page,
                                size,
                                relogiosPage.getTotalPages(),
                                relogioResponseList);
        }

        // TODO: criar exceptionHandler para tratar erros

        public RelogioResponse buscarPorUuid(UUID uuid) {
                RelogioEntity relogioEntity = relogioRepository.findById(uuid)
                                .orElseThrow(() -> new RelogioNotFoundException(uuid));
                return relogioMapper.tRelogioResponse(relogioEntity);
        }

        public ResultDefaultResponse createRelogio(CriarRelogioRequest req) {
                RelogioEntity relogioEntity = RelogioEntity.builder()
                                .marca(req.marca())
                                .modelo(req.modelo())
                                .referencia(req.referencia())
                                .tipoMovimento(TipoMovimentoEnum.fromApi(req.tipoMovimento()))
                                .materialCaixa(MaterialCaixaEnum.fromApi(req.materialCaixa()))
                                .tipoVidro(TipoVidroEnum.fromApi(req.tipoVidro()))
                                .resistenciaAguaM(req.resistenciaAguaM())
                                .diametroMm(req.diametroMm())
                                .lugToLugMm(req.lugtoLugMm())
                                .espessuraMm(req.espessuraMm())
                                .larguraLugMm(req.larguraMm())
                                .precoEmCentavos(req.precoEmCentavos())
                                .urlImagem(req.urlImagem())
                                .build();
                relogioRepository.save(relogioEntity);
                return new ResultDefaultResponse(
                                "Relógio criado com sucesso",
                                "201");
        }

        public ResultDefaultResponse atualizar(UUID uuid, AtualizarRelogioRequest req) {
                RelogioEntity relogioEntity = relogioRepository.findById(uuid)
                                .orElseThrow(() -> new RelogioNotFoundException(uuid));
                relogioEntity.setMarca(req.marca());
                relogioEntity.setModelo(req.modelo());
                relogioEntity.setReferencia(req.referencia());
                relogioEntity.setTipoMovimento(TipoMovimentoEnum.fromApi(req.tipoMovimento()));
                relogioEntity.setMaterialCaixa(MaterialCaixaEnum.fromApi(req.materialCaixa()));
                relogioEntity.setTipoVidro(TipoVidroEnum.fromApi(req.tipoVidro()));
                relogioEntity.setResistenciaAguaM(req.resistenciaAguaM());
                relogioEntity.setDiametroMm(req.diametroMm());
                relogioEntity.setLugToLugMm(req.lugtoLugMm());
                relogioEntity.setEspessuraMm(req.espessuraMm());
                relogioEntity.setLarguraLugMm(req.larguraMm());
                relogioEntity.setPrecoEmCentavos(req.precoEmCentavos());
                relogioEntity.setUrlImagem(req.urlImagem());

                relogioRepository.save(relogioEntity);
                return new ResultDefaultResponse(
                                "Relógio atualizado com sucesso",
                                "200");
        }

        public ResultDefaultResponse deletar(UUID uuid) {
                RelogioEntity relogioEntity = relogioRepository.findById(uuid)
                                .orElseThrow(() -> new RelogioNotFoundException(uuid));
                relogioRepository.delete(relogioEntity);
                return new ResultDefaultResponse(
                                "Relógio deletado com sucesso",
                                "200");
        }
}