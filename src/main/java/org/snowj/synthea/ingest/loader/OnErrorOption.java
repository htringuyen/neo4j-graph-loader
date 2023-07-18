package org.snowj.synthea.ingest.loader;

public enum OnErrorOption {
    TERMINATE_PROGRAM,
    BREAK_CURRENT_LOADING,
    IGNORE_LOADING_ERROR;

    public static OnErrorOption fromString(String value) {
        if (value == null) {
            return null;
        }

        return switch (value.toLowerCase()) {
            case "terminate_program" -> TERMINATE_PROGRAM;
            case "break_current_loading" -> BREAK_CURRENT_LOADING;
            case "ignore_loading_error" -> IGNORE_LOADING_ERROR;
            default -> null;
        };
    }
}
