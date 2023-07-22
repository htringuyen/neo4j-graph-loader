package org.snowj.synthea.ingest.loader;

public class CypherExecutingException extends Exception {
    public CypherExecutingException(String message) {
        super(message);
    }

    public CypherExecutingException(String message, Throwable cause) {
        super(message, cause);
    }
}
