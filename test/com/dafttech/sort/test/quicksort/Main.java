package com.dafttech.sort.test.quicksort;

import com.dafttech.storage.array.sort.Sorter;

public class Main {

    public static void main(String[] args) {
        Integer[] test = { 9, 2, 56, 0, 2, 25, 345, 1, 5456, 2345, 723, 12, 63, 4 };
        for (Integer i : new Sorter<Integer>(test).mergeSort()) {
            System.out.print(i + " ");
        }
    }

}
