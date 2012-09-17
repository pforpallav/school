#include <stdio.h>

/* The heap we are dealing with. */
extern int heap[];

/* Two functions called by heapsort */
extern int  extract_max(int last);
extern void heapify_array(int last);

/*
 * Heapsort algorithm.
 */
void heapsort(int last)
{
    int i;

    if (last < 0) return;

    heapify_array(last);
    for (i = last; i >= 0; i--)
    {
	heap[i] = extract_max(i);
    }
}
