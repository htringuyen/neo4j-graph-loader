package org.snowj.synthea.ingest.parser;

public class IngestParsingException extends Exception {
    public IngestParsingException(String message) {
        super(message);
    }

    public IngestParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
