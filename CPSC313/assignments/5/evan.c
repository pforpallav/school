#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include <stdlib.h>
#include <stdio.h>


int sectorSize(char one, char two) {
	char size[1];
	size[0]=two;
	size[1]=one;
	int b = (size[0] << 8) | size[1];

	return b;
}

int sectorsPerCluster(char one) {
	int b = one;
	return b;
}

int clusterSize(int sectorSize, int sectorsPerCluster) {
	int b = sectorSize*sectorsPerCluster;
	return b;
}

int numberOfDirs(char one, char two) {
	char num[1];
	num[0]=two;
	num[1]=one;
	int b = (num[0] <<8) | num[1];
	return b;
}

int sectorsPerFAT(char one, char two) {
	char num[1];
	num[0]=two;
	num[1]=one;
	int b = (num[0] <<8) | num[1];
	return b;
}

int reservedSectors(char one, char two) {
	char num[1];
	num[0]=two;
	num[1]=one;
	int b = (num[0] <<8) | num[1];
	return b;
}
int hiddenSectors(char one, char two) {
	char num[1];
	num[0]=two;
	num[1]=one;
	int b = (num[0] <<8) | num[1];
	return b;
}

int sectorNumber(char one, char two) {

}

int main(int argsc, char* argv[]) {

	int fh;
	// char* buffer = (char*) malloc(5242422*sizeof(char));
	char buffer[256];
	int gotten;


	fh = open(argv[1],O_RDONLY);
	// printf ("File handle %d\n\n",fh);
	// printf("woeijfwo;ejf");

	if (read(fh,buffer,256*sizeof(char))) {
		int ss = sectorSize(buffer[11], buffer[12]);
		int spc = sectorsPerCluster(buffer[13]);
		int cs = clusterSize(ss, spc);
		int dirs = numberOfDirs(buffer[17], buffer[18]);
		int spfat = sectorsPerFAT(buffer[22], buffer[23]);
		int rs = reservedSectors(buffer[14],buffer[15]);
		int hs = hiddenSectors(buffer[28], buffer[29]);

		printf("Sector Size: %i\n", ss);
		printf("Sectors per cluster: %i\n", spc);
 		printf("Cluster Size: %i\n", cs);
 		printf("Number of Dirs in root: %i\n", dirs);
 		printf("Sectors Per FAT: %i\n", spfat);
 		printf("Reserved Sectors: %i\n", rs);
		printf("Hidden Sectors: %i\n", hs);
	}




	// printf("AWLIEJFWIEJFWI");
	// while (gotten = read(fh,buffer,256*sizeof(char))) {
	// 	// buffer[gotten] = 'P';
	// 	int i = 0;
	// 	while(i<=sizeof(buffer)) {
	// 		printf("%c", buffer[i]);
	// 		i++;
	// 	}
	// 	// printf("%c",*buffer);
	// }

	// int size = sizeof(buffer) / sizeof(buffer[1]);
	// printf("%i", size);




}
