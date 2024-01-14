package pl.msocha.analyzer.exception;

import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException {

    private final String id;

    public InvalidInputException(String id, String message) {
        super(message);
        this.id = id;
    }
}
