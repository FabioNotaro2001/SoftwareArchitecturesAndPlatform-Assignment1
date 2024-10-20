package sap.ass01.layered.service;

import java.util.function.Consumer;

/**
 * Utility class that provides methods for working with lambda expressions.
 * Specifically, it wraps a consumer that may throw a checked exception,
 * allowing it to be used in contexts where unchecked exceptions are expected.
 */
public class LambdaUtil {

    /**
     * Creates a wrapper for a consumer that throws an exception, 
     * in order to handle said exception.
     * 
     * @param consumer the original consumer that may throw a checked exception
     * @return a Consumer<T> that wraps the original consumer, handling any exceptions
     */
    public static <T> Consumer<T> wrap(CheckedConsumer<T> consumer) {
        // Return a new Consumer that wraps the provided CheckedConsumer.
        return t -> {
            try {
                // Attempt to accept the input using the checked consumer.
                consumer.accept(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
