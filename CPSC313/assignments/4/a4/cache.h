/*
 * cache.h
 *
 * Definition of the structure used to represent a cache.
 */
#ifndef CACHE_H
#define CACHE_H

#include <stdlib.h>
#include <inttypes.h>

/*
 * Replacement policies.
 */
#define CACHE_REPLACEMENTPOLICY_MASK   1

#define CACHE_REPLACEMENTPOLICY_LRU    0
#define CACHE_REPLACEMENTPOLICY_RANDOM 1

/*
 * Write policies.
 */
#define CACHE_WRITEPOLICY_MASK   6

#define CACHE_WRITEPOLICY_WRITETHROUGH       0
#define CACHE_WRITEPOLICY_WRITEBACK          2

#define CACHE_WRITEPOLICY_WRITEALLOCATE      0
#define CACHE_WRITEPOLICY_WRITENOALLOCATE    4

/*
 * Other policies.
 */
#define CACHE_TRACE_MASK  8
#define CACHE_TRACEPOLICY 8

/*
 * Structure used to store a single cache line.
 */
typedef struct cache_line_s
{
    /* The valid bit. */
    int is_valid;

    /* The tag. */
    intptr_t tag;

    /* The data. */
    unsigned char *data;

} cache_line_t;

/*
 * Structure used to store a cache set: a cache set contains a pointer
 * to an array of pointers to cache lines.
 */
typedef struct cache_set_s
{
    cache_line_t **cache_lines;
} cache_set_t;

/*
 * Structure used to store a cache.
 */
typedef struct cache_s
{ 
    /* Number of sets in the cache. */
    unsigned int num_sets;

    /* Number of blocks in each set. */
    unsigned int associativity;

    /* Size of each block. */
    size_t block_size;

    /* Mask for block offset. */
    unsigned int block_offset_mask;

    /* Mask for cache index. */
    unsigned int cache_index_mask;

    /* Shift for cache index and tag. */
    unsigned int cache_index_shift;

    /* Shift for tag. */
    unsigned int tag_shift;

    /* Replacement and write policies. */
    unsigned int policies;

    /* Array of sets, each of which is an array of blocks, each of which is an array of bytes. */
    cache_set_t *sets;

    /* Statistics about cache usage. */
    unsigned int access_count, miss_count;
} cache_t;

/*
 * Create a new cache that contains a total of num_blocks blocks, each of which is block_size
 * bytes long, with the given associativity.
 */
cache_t *cache_new(size_t num_blocks, size_t block_size, unsigned int associativity, int policies);

/*
 * Read a single integer from the cache.
 */
int cache_read(cache_t *cache, int *address);

/*
 * Write a single integer to memory and/or the cache.
 */
void cache_write(cache_t *cache, int *address, int value);

/*
 * Return the number of cache misses since the cache was created.
 */
int cache_miss_count(cache_t *cache);

/*
 * Return the number of cache accesses since the cache was created.
 */
int cache_access_count(cache_t *cache);
#endif
