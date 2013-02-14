CCFLAGS = -g -pg

default: P2
all: P2

P2: main.cpp
	g++ ${CCFLAGS_MAKE_TEXTURES} main.cpp Ship.h Solar.h -o P2 \
		-lGL -lGLU -lglut

clean:
	touch P2
	rm -rf P2 *.o
