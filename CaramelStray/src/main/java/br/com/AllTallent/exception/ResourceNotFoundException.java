package br.com.AllTallent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Esta é uma exceção customizada que usaremos em toda a aplicação
 * sempre que um recurso (como um funcionário, área, etc.) não for encontrado no banco.
 * A anotação @ResponseStatus(HttpStatus.NOT_FOUND) instrui o Spring a automaticamente
 * retornar um status HTTP 404 (Not Found) quando este erro for lançado por um controller.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}