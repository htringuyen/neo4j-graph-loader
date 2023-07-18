package org.snowj.synthea.ingest.parser;

public class InvalidConfigException extends Exception {
    private final String message;
    private final String code;
    public InvalidConfigException(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }


}
