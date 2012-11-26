#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include <stdlib.h>
#include <stdio.h>



int main() {

	int fh;
	char* buffer;
	int gotten;
	int size = sizeof(char)+1;

	fh = open("fat_volume.dat",O_RDONLY);
	// printf ("File handle %d\n",fh);

	while (gotten = read(fh,buffer,sizeof(char))) {
		buffer[gotten] = '\0';
		printf("%c\n",*buffer);
	}



}
