package br.com.AllTallent.dto;

import br.com.AllTallent.model.FuncionarioCertificado;

public record CertificadoDTO(Integer codigo, String nome) {
    public CertificadoDTO(FuncionarioCertificado certificado) {
        this(certificado.getCodigo(), certificado.getCertificado());
    }
}