package utils;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public interface Processor<T>
{
    T process(T t);
}
