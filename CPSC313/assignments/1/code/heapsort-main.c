#include <stdio.h>

extern int heapsort(int last);

/*
 * Global array and its length.
 */
int heap_size = 14;
int heap[] = { 3, 14, 5, 2, 22, 16, 11, 12, 9, 13, 15, 1, 4, 18 };

/*
 * Swap the two elements at the indicated positions.
 */
void swap(int index1, int index2)
{
    int tmp = heap[index1];
    heap[index1] = heap[index2];
    heap[index2] = tmp;
}
    
/*
 * Helper for heapify_node: checks to see if child's key is larger than the
 * parent's.
 */
int check_child(int child, int highest, int last)
{
    return child <= last && heap[highest] < heap[child];
}

/*
 * Heapify a single node of a heap.
 */
void heapify_node(int index, int last)
{
    /* Index of the node with the maximum key. */
    int highest = index;

    /* Index of the initial's node left child. */
    int left_child;

    /* Index of the initial's node right child. */
    int right_child;

    /* Whether or not we need to swap the node's key with its child's. */
    int need_to_swap;

    do
    {
	/*
	 * Check to see if left child's key is larger than the node's.
	 */
	left_child = 2 * index + 1;
	if (check_child(left_child, highest, last))
        {
	    highest = left_child;
	}

	/*
	 * Check to see if right child's key is larger than the node's.
	 */
	right_child = 2 * index + 2;
	if (check_child(right_child, highest, last))
        {
	    highest = right_child;
	}

	/*
	 * Swap keys if necessary.
	 */
        need_to_swap = index != highest;

	if (need_to_swap)
        {
	    swap(index, highest);
	    index = highest;
	}

    } while (need_to_swap);
}

/*
 * Turn an array into a heap.
 */
void heapify_array(int last)
{
    int i;

    for (i = (last - 1)/2; i >= 0; i--)
    {
	heapify_node(i, last);
    }
}

/*
 * Extract the smallest element of a heap.
 */
int extract_max(int last)
{
    int max =  heap[0];

    heap[0] = heap[last];
    heapify_node(0, last - 1);

    return max;
}

/*
 * Dump the contents of the array to a terminal.
 */
void print()
{
    int i;

    for (i = 0; i < heap_size; i++)
    {
	printf("%d\n", heap[i]);
    }
    putchar('\n');
}

/*
 * Main function: call heapsort on array a, and then print out its contents.
 */
int main(int argc, char *argv[])
{
    heapsort(heap_size - 1);
    print();
    return 0;
}
