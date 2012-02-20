package utils;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public interface Merger<A, B>
{
    A merge(A a,
            B b);
}
