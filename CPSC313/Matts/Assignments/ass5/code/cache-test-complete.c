#include "cache.h"
#include <stdio.h>

#define CACHE_SIZE 128

#define MAXN 68
#define N1 64
#define N2 48

static cache_t *cache;
int a[MAXN * MAXN];

/*
 * Write data to the array, bypassing the "cache".
 */
void fillArray(int *a, int n)
{
    int i, j;

    for (i = 0; i < n; i++)
        for (j = 0; j < n; j ++)
            a[i * n + j] = (i + 1) * (j + 1);
}

int sumA(int *a, int n)
{
    int i, j;
    int sum = 0;

    for (i = 0; i < n; i++)
        for (j = 0; j < n; j ++)
	    sum += cache_read(cache, &a[i * n + j]);

    return sum;
}

int sumB(int *a, int n)
{
    int i, j;
    int sum = 0;

    for (j = 0; j < n; j ++)
        for (i = 0; i < n; i++)
	    sum += cache_read(cache, &a[i * n + j]);

    return sum;
}

int sumC(int *a, int n)
{
    int i, j;
    int sum = 0;

    for (j = 0; j < n; j += 2)
        for (i = 0; i < n; i += 2)
            sum += cache_read(cache, &a[i * n + j]) + cache_read(cache, &a[(i+1) * n + j]) +
	   	   cache_read(cache, &a[i * n + (j+1)])  + cache_read(cache, &a[(i+1) * n + (j+1)]);

    return sum;
}

void print_stats(int x)
{
    int mc = cache_miss_count(cache);
    int ac = cache_access_count(cache);

    printf("Sum = %d\n", x);
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
    int x;
    fillArray(a, N1);

    cache = cache_new(128, 16, 1, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Array: N1 x N1  Cache 128 blocks, 16 bytes/block, direct-mapped, sumA\n");
    x = sumA(a, N1);
    print_stats(x);

    cache = cache_new(128, 16, 1, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Array: N1 x N1  Cache 128 blocks, 16 bytes/block, direct-mapped, sumB\n");
    x = sumB(a, N1);
    print_stats(x);

    cache = cache_new(128, 16, 1, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Array: MAXN x MAXN  Cache 128 blocks, 16 bytes/block, direct-mapped, sumB\n");
    x = sumB(a, MAXN);
    print_stats(x);

    cache = cache_new(128, 16, 1, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Array: N1 x N1  Cache 128 blocks, 16 bytes/block, direct-mapped, sumC\n");
    x = sumC(a, N1);
    print_stats(x);

    cache = cache_new(128, 16, 1, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Array: N2 x N2  Cache 128 blocks, 16 bytes/block, direct-mapped, sumA\n");
    x = sumA(a, N2);
    print_stats(x);

    cache = cache_new(128, 16, 1, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Array: N2 x N2  Cache 128 blocks, 16 bytes/block, direct-mapped, sumB\n");
    x = sumB(a, N2);
    print_stats(x);

    cache = cache_new(128, 16, 1, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Array: N2 x N2  Cache 128 blocks, 16 bytes/block, direct-mapped, sumC\n");
    x = sumC(a, N2);
    print_stats(x);

    cache = cache_new(128, 16, 2, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Array: N1 x N1  Cache 128 blocks, 16 bytes/block, 2-way SA, sumA\n");
    x = sumA(a, N1);
    print_stats(x);

    cache = cache_new(128, 16, 2, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Array: N1 x N1  Cache 128 blocks, 16 bytes/block, 2-way SA, sumB\n");
    x = sumB(a, N1);
    print_stats(x);

    cache = cache_new(128, 16, 2, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Array: N1 x N1  Cache 128 blocks, 16 bytes/block, 2-way SA, sumC\n");
    x = sumC(a, N1);
    print_stats(x);

    cache = cache_new(128, 16, 2, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Array: N2 x N2  Cache 128 blocks, 16 bytes/block, 2-way SA, sumA\n");
    x = sumA(a, N2);
    print_stats(x);

    cache = cache_new(128, 16, 2, CACHE_REPLACEMENTPOLICY_LRU | CACHE_TRACEPOLICY);
    printf("Array: N2 x N2  Cache 128 blocks, 16 bytes/block, 2-way SA, sumB\n");
    x = sumB(a, N2);
    print_stats(x);

    cache = cache_new(128, 16, 2, CACHE_REPLACEMENTPOLICY_LRU);
    printf("Array: N2 x N2  Cache 128 blocks, 16 bytes/block, 2-way SA, sumC\n");
    x = sumC(a, N2);
    print_stats(x);

    return 0;
}
