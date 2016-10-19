**Mergesort**

```
<T> void mergeSort(T[] a, Comparator<T> c) {
    if (a.length <= 1) return;
    T[] a0 = Arrays.copyOfRange(a, 0, a.length/2);
    T[] a1 = Arrays.copyOfRange(a, a.length/2, a.length);
    mergeSort(a0, c);
    mergeSort(a1, c);
    merge(a0, a1, a, c);
}
```

merge:
```
<T> void merge(T[] a0, T[] a1, T[] a, Comparator<T> c) {
    int i0 = 0, i1 = 0;
    for (int i = 0; i < a.length; i++) {
        if (i0 == a0.length)
            a[i] = a1[i1++];
        else if (i1 == a1.length)
            a[i] = a0[i0++];
        else if (compare(a0[i0], a1[i1]) < 0)
            a[i] = a0[i0++];
        else
            a[i] = a1[i1++];
    }
}
```

**Quicksort**

Choosing a Good Pivot

Surprisingly, or perhaps not, choosing a good pivot is crucial to the success of QuickSort. If the pivot is chosen poorly, for example, consistently the smallest or largest element in an array, or the first or last element in a nearly-sorted/reverse-sorted array, the complexity is O(n2n2). This is because the algorithm recurses on an array of length n - 1, then n - 2...and so on, until it finally reaches 1. There are n - 1 levels, and each level, O(nn) work is done, resulting in that complexity.
