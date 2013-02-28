#pragma once
#include <GL/glut.h>
#include <math.h>
#include "Ship.h"

class Solar
{

private:
	int position; 
	float velocity;
	GLfloat shine[1], mooCol[3], moo2Col[3], merCol[3], venCol[3], earCol[3], marCol[3], jupCol[3], satCol[3], uraCol[3], nepCol[3], pluCol[3];
	float DEG2RAD;
	int Mer, Ven, Ear, Moo, Moo2, Mar, Jup, Sat, Ura, Nep, Plu;
	
public:
	Solar(void);
	virtual ~Solar(void);
	void incVelocity();
	void decVelocity();
	void drawCircle(float rad);
	void draw();
	void spin();
	float getVelocity();
	GLdouble eyex, eyey, eyez, centerx, centery, centerz, upx, upy, upz;
	bool geo;

};

