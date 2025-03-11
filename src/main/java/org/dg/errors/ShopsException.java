package org.dg.errors;

import org.dg.utils.SerializeDeserialiseUtils;

import io.quarkus.logging.Log;
import lombok.Getter;

@Getter
public class ShopsException extends RuntimeException {

    private final ShopsErrors error;

    public ShopsException(ShopsErrors error) {
        super(error.getDescription());
        this.error = error;
        logError(this);
    }

    public ShopsException(ShopsErrors error, String detail) {
        super(detail);
        this.error = error;
        logError(this);
    }

    public ShopsException(ShopsErrors error, String technicalError, Throwable stack) {
        super(technicalError, stack);
        this.error = error;
        logError(this);
    }

    private static void logError(ShopsException e) {
        Log.error("Erreur " + e.error.getStatus().getStatusCode() + " " + e.error.getStatus().getReasonPhrase() + " : "
                + e.error.getDescription());
        if (e.getCause() != null) {
            Log.error(e.getCause().getMessage());
        }
        Log.error(e.getMessage());
    }

    @Override
    public String toString() {
        return SerializeDeserialiseUtils.serializeObjectToJSON(this);
    }
}
