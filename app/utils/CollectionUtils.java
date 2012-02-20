package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class CollectionUtils
{
    public static <T> List<List<T>> split(List<T> items,
                                          int count)
    {
        List<List<T>> splitItems = new ArrayList<List<T>>();
        if (!items.isEmpty())
        {
            int index = 0;
            int size = items.size();
            while (index < size)
            {
                List<T> subList = new ArrayList<T>(items.subList(index,
                                                                 index + count < size ? index + count : size));
                splitItems.add(subList);
                index += subList.size();
            }
        }
        return splitItems;
    }
}
