package br.com.AllTallent.dto;

import br.com.AllTallent.model.FuncionarioCertificado;

public record CertificadoDTO(String nome) {
    public CertificadoDTO(FuncionarioCertificado certificado) {
        this(certificado.getCertificado());
    }
}