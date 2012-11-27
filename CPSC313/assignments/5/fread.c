#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include <stdlib.h>
#include <stdio.h>


int main() {
    FILE *f = fopen("fat_volume.dat", "r");

    if (f == NULL) {
         printf("I couldn't open fat_volume.dat for writing.\n");
         exit(0);
    }

    int c;
    int count= 0 ;
    while((c = fgetc(f)) != EOF) {
        printf("%02x", c);
        count++;

        if (count % 2 == 0){
            printf(" ");
        }

        if (count % 16 == 0){
            printf("\n");
        }

    }

    fclose(f);

    return 0;
}
