package sap.ass01.service;

import java.util.function.Consumer;

public class LambdaUtil {
    /**
     * Creates a wrapper for a consumer that throws an exception, in order to handle said exception.
     * @param consumer the original consumer
     * @return the wrapper for the consumer
     */
    public static <T> Consumer<T> wrap(CheckedConsumer<T> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}