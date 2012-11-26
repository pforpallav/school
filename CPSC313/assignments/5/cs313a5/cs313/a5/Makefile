CC = gcc
CFLAGS = -g -O2 -Wall -Wstrict-prototypes 

SRC = fatinfo.c direntry.c directory.c blockio.c wordio.c fatfs.c fat12.c 
OBJ = $(SRC:.c=.o)

fat12info: $(OBJ)
	gcc -o fat12info $(OBJ)
depend: $(SRC)
	makedepend $(SRC)

clean:
	/bin/rm -f $(OBJ) fatinfo *~

# DO NOT DELETE

direntry.o: direntry.h
directory.o: directory.h
blockio.o: blockio.h
wordio.o: wordio.h
fatfs.o: fatfs.h
fat12.o: fat12.h
