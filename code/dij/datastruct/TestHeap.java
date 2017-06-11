package dij.datastruct;

import java.util.*;

/**
 * Created by Administrator on 2017/5/21.
 */
public class TestHeap {
    public static void main(String[] args) {//7, 26, 19, 9,
//        BinaryHeap<Comparable> binaryHeap = new BinaryHeap<>();
//        Random random = new Random();
//        int[] array = new int[1000];
//        for (int i = 0; i < array.length; i++) {
//            array[i] = random.nextInt(1000);
//            binaryHeap.insert(array[i]);
//            binaryHeap.showRaw();
//        }
//        System.out.println(Arrays.toString(array));
//        Arrays.sort(array);
//        System.out.println(Arrays.toString(array));
//        binaryHeap.show();
        BinaryHeap<Integer> heap = new BinaryHeap<>((o1, o2) -> o1.compareTo(o2));
        for (int i = 0; i < 10; i++) heap.insert(10 - i);
        System.out.println(Arrays.toString(heap.show()));
        heap.showRaw();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("make your choice>>>");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println(Arrays.toString(heap.show()));
                    break;
                case 2:
                    heap.showRaw();
                    break;
                case 3:
                    System.out.print("which index do you want to delete>>>");
                    heap.delete(scanner.nextInt());
                    break;
                case 4:
                    System.out.print("enter the new elements>>>");
                    heap.insert(scanner.nextInt());
                    break;
                case 5:
                    System.out.println("minValue is : " + heap.extractTheMostValue());
                    break;
                default:
                    break;
            }
        }

    }
}