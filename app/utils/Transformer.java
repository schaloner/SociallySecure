package utils;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public interface Transformer<I, O>
{
    O transform(I i);
}
