package dij.datastruct;

/**
 * Created by Administrator on 2017/5/21.
 */
public interface Heap<T> {

    T peek();

    int size();

    boolean isEmpty();

    T extractTheMostValue();

    void insert(T z);

    T delete(T z);

    T delete(int index);

    boolean contain(T z);

    void clear();
}
