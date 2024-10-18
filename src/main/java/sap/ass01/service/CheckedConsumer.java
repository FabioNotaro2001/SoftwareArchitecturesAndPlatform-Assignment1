package sap.ass01.service;

/**
 * A functional interface that represents a consumer which can throw exceptions.
 * 
 * This interface is useful in situations where a lambda expression needs to perform 
 * an operation on an input while also allowing for the possibility of throwing 
 * checked exceptions.
 * 
 * @param <T> the type of the input to the operation
 */
@FunctionalInterface
public interface CheckedConsumer<T> {
    
    /**
     * Performs this operation on the given argument.
     * 
     * @param t the input argument
     * @throws Exception if an exception occurs during operation
     */
    void accept(T t) throws Exception;
}
