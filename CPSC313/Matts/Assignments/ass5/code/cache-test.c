#include "cache.h"
#include <stdio.h>

#define N 64

typedef int array_t[N][N];
static cache_t *cache;

array_t test_array;

/*
 * Write data to the array, bypassing the "cache".
 */
int fillArray(array_t a)
{
    int i, j;

    for (i = 0; i < N; i++)
        for (j = 0; j < N; j ++)
            a[i][j] = (i + 1) * (j + 1);
}

int sumA(array_t a)
{
    int i, j;
    int sum = 0;
    int x;

    for (i = 0; i < N; i++)
        for (j = 0; j < N; j ++)
	    sum += cache_read(cache, &a[i][j]);

    return sum;
}

int sumB(array_t a)
{
    int i, j;
    int sum = 0;
    int x;

    for (j = 0; j < N; j ++)
        for (i = 0; i < N; i++)
	    sum += cache_read(cache, &a[i][j]);

    return sum;
}

int sumC(array_t a)
{
    int i, j;
    int sum = 0;

    for (j = 0; j < N; j += 2)
        for (i = 0; i < N; i += 2)
            sum += cache_read(cache, &a[i][j]) + cache_read(cache, &a[i+1][j]) +
		cache_read(cache, &a[i][j+1])  + cache_read(cache, &a[i+1][j+1]);

    return sum;
}

int print_stats()
{
    int mc = cache_miss_count(cache);
    int ac = cache_access_count(cache);

    if (ac == 0)
    {
	printf("The cache wasn't used.\n");
    }
    else
    {
	printf("Miss rate = %8.4f\n", (double) mc/ac);
    }
}

int main()
{
    fillArray(test_array);

    cache = cache_new(256, 16, 1, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Sum = %d\n", sumA(test_array));
    print_stats();

    cache = cache_new(256, 16, 1, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Sum = %d\n", sumB(test_array));
    print_stats();

    cache = cache_new(256, 16, 1, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Sum = %d\n", sumC(test_array));
    print_stats();
}
