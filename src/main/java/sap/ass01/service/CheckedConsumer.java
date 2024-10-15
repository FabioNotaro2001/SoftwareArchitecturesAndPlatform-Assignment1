package sap.ass01.service;

/**
 * Special interface for lambdas that throw exceptions.
 */
@FunctionalInterface
public interface CheckedConsumer<T> {
    void accept(T t) throws Exception;
}
