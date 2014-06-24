package com.dafttech.sort;

import java.util.Arrays;

public class Sorter<Type extends Comparable<Type>> {
    private Type[] array;
    private boolean sorting = false;

    public Sorter(Type[] array) {
        this.array = array;
    }

    public Type[] quickSort() {
        if (sorting) return null;
        sorting = true;
        quickSort(0, array.length - 1);
        sorting = false;
        return array;
    }

    private void quickSort(int startIndex, int endIndex) {
        if (endIndex - startIndex < 1) return;
        int pivotIndex = startIndex, readIndex = endIndex, swap;
        Type pivot = array[pivotIndex], read;
        while (readIndex != pivotIndex) {
            read = array[readIndex];
            if ((pivotIndex - readIndex) * read.compareTo(pivot) > 0) {
                array[readIndex] = pivot;
                array[pivotIndex] = read;
                swap = pivotIndex;
                pivotIndex = readIndex;
                readIndex = swap;
            }
            if (pivotIndex < readIndex)
                readIndex--;
            else
                readIndex++;
        }
        quickSort(startIndex, pivotIndex - 1);
        quickSort(pivotIndex + 1, endIndex);
    }

    public Type[] mergeSort() {
        if (sorting) return null;
        sorting = true;
        mergeSort(0, array.length - 1);
        sorting = false;
        return array;
    }

    private void mergeSort(int startIndex, int endIndex) {
        if (startIndex == endIndex) return;
        int midIndex = (startIndex + endIndex) / 2;
        mergeSort(startIndex, midIndex);
        mergeSort(midIndex + 1, endIndex);
        Type[] tmpArray = Arrays.copyOfRange(array, startIndex, endIndex);

    }
}
