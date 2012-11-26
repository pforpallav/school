#include <stdio.h>
int main()
{
  FILE *f;
  char buffer[11000];
  if (f = fopen("fat_volume.dat", "rt"))
  {
    fread(buffer, 1, 10000, f);
    buffer[100] = 0;
    fclose(f);
    printf("first 10 characters of the file:\n%x\n", buffer);
  }
  return 0;
}