#pragma once
#include <GL/glut.h>
#include "Solar.h"
class Ship
{

private:
	float xpos, ypos, zpos, xrot, yrot, zrot;

public:
	Ship(void);
	~Ship(void);
	bool scout;
	void camera();
	void setXPos(float x);
	float getXPos();
	void setYPos(float y);
	float getYPos();
	void setZPos(float z);
	float getZPos();
	void setYRot(float angle);
	float getYRot();
	void setXRot(float angle);
	float getXRot();
	void draw();
	void setZRot(float angle);
	float getZRot();
	void toggleScout();
};

