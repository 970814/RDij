package dij.datastruct;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by L on 2017/3/1.
 */
public class BinaryHeap<E> implements Heap<E> {
    private E[] elements;
    private int capability;
    private final static int defaultSize = 8;
    private int length;
    Comparator<E> comparator;

    public BinaryHeap() {
        this(defaultSize, null);
    }

    public BinaryHeap(Object[] elements, Comparator<E> comparator) {
        this(elements, elements.length, comparator);
    }

    public BinaryHeap(int capability, Comparator<E> comparator) {
        this(new Object[capability], 0, comparator);
    }

    private BinaryHeap(Object[] elements, int length, Comparator<E> comparator) {
        this.elements = (E[]) elements;
        this.capability = elements.length;
        this.length = length;//有效元素
        this.comparator = comparator;
    }

    public BinaryHeap(Comparator<E> comparator) {
        this(defaultSize, comparator);
    }


    public void build() {
        build(length);
    }

    @Override
    public void insert(E z) {
        if (length == capability) grow();//如果元素个数达到容量个数,容量翻倍
        int x = length;
//        ((Comparable) elements[parent(x)]).compare(z)
        while (x > 0 && compare(elements[parent(x)], z) > 0) {
            elements[x] = elements[parent(x)];
            x = parent(x);
        }
        elements[x] = z;
        length++;
    }

    @Override
    public E delete(E z) {
        return delete(find(z));
    }

    public int find(E z) {
        for (int i = 0; i < length; i++) if (elements[i].equals(z)) return i;
        return -1;
    }

    @Override
    public E delete(int index) {
        Object z = peek(index);
        elements[index] = elements[length - 1];
        elements[--length] = null;//let gc
        maintain(index);
        if (length << 2 == capability) decrease();//如果元素个数为容量的四分之一,容量折半
        return (E) z;
    }

    public void decreaseValue(E z, E x) {
        delete(z);
        insert(x);
    }

    @SuppressWarnings("unchecked")
    public int compare(E o1, E o2) {
        if (comparator == null) return ((Comparable) o1).compareTo(o2);
        else return comparator.compare(o1, o2);
    }

    @SuppressWarnings("unchecked")
    public E extractTheMostValue() {
        return delete(0);
    }

    public E peek() {
        return peek(0);
    }

    public E peek(int index) {
        if (length <= index) throw new NoSuchElementException();
        return (E) elements[index];
    }

    private void decrease() {
        capability >>>= 1;
//        Comparable<Comparable>[] newData = new Comparable[capability];
        Object[] newData = new Object[capability];
        System.arraycopy(elements, 0, newData, 0, length);
        elements = (E[]) newData;
    }

    private void grow() {
        capability <<= 1;
//        Comparable<Comparable>[] newData = new Comparable[capability];
        Object[] newData = new Object[capability];
        System.arraycopy(elements, 0, newData, 0, length);
        elements = (E[]) newData;
    }

    public void build(int length) {
        for (int i = parent(length - 1); i >= 0; i--) maintain(i);
    }

    public void maintain(int index) {
        int l = left(index);
        int r = right(index);
        int theMost = index;
        //如果是最小堆，那么必须a>b返回大于0的值
        if (l < length && compare(elements[theMost], elements[l]) > 0) theMost = l;
        if (r < length && compare(elements[theMost], elements[r]) > 0) theMost = r;
        if (theMost == index) return;//说明该堆的性质成立
        exchange(index, theMost);
        maintain(theMost);
    }

    private void maintain0(int x) {
        int theMost = x;
        int pre;
        do {
            pre = theMost;
            int l = left(theMost);
            int r = right(theMost);
            if (l < length && compare(elements[theMost], elements[l]) > 0) theMost = l;
            if (r < length && compare(elements[theMost], elements[r]) > 0) theMost = r;
            if (theMost != pre) exchange(pre, theMost);
        } while (theMost != pre);
    }

    private void exchange(int u, int v) {
        Object tmp = elements[u];
        elements[u] = elements[v];
        elements[v] = (E) tmp;
    }

    private int parent(int x) {
        return x - 1 >>> 1;
    }

    private int left(int x) {//四则运算的优先级更高
        return (x << 1) + 1;
    }

    private int right(int x) {
        return left(x) + 1;
    }

    public int size() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
    }


    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    protected Object clone() {
        return new BinaryHeap(elements.clone(), length, comparator);
    }

    @SuppressWarnings("unchecked")
    public E[] show() {
        BinaryHeap clone = (BinaryHeap) clone();
        E[] sortedArray = (E[]) new Object[clone.size()];
        for (int i = 0; i < sortedArray.length; i++) sortedArray[i] = (E) clone.extractTheMostValue();
        return sortedArray;
    }

    public void showRaw() {
        for (int i = 0; i < size(); i++) System.out.print(elements[i] + ", ");
        System.out.println();
    }

    @Override
    public boolean contain(E z) {
        return find(z) > -1;
    }

    @Override
    public void clear() {
        for (int i = 0; i < length; i++) elements[i] = null;
        length = 0;
    }

    public void forEach(Consumer<E> action) {
        for (int i = 0; i < length; i++) {
            action.accept(elements[i]);
        }
    }
}
