package com.dafttech.sort;

public class Sorter<Type extends Comparable<Type>> {
    private Type[] array;

    public Sorter(Type[] array) {
        this.array = array;
    }

    public Type[] quickSort() {
        quickSort(0, array.length - 1);
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
}
