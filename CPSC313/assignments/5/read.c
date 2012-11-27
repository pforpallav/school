#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include <stdlib.h>
#include <stdio.h>
int main()
{
  FILE *fp;
  long lSize;
  char *buffer;

  fp = fopen ( "fat_volume.dat" , "rb" );
  if( !fp ) perror("blah.txt"),exit(1);

  fseek( fp , 0L , SEEK_END);
  lSize = ftell( fp );
  rewind( fp );

  /* allocate memory for entire content */
  buffer = calloc( 1, lSize+1 );
  if( !buffer ) fclose(fp),fputs("memory alloc fails",stderr),exit(1);

  /* copy the file into the buffer */
  if( 1!=fread( buffer , lSize, 1 , fp) )
    fclose(fp),free(buffer),fputs("entire read fails",stderr),exit(1);

  /* do your work here, buffer is a string contains the whole text */

  int n = sizeof(buffer) / sizeof(buffer[0]);
  int i = 0;
  while (i <=0) {
    printf("%i", n);
    printf("%c", buffer[i]);
    i++;
  }
  // printf("%s", buffer);

  fclose(fp);
  free(buffer);
}