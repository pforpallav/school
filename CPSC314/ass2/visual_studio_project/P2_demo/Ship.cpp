#include "Ship.h"
#include <math.h>

Ship::Ship(void)
{
	scout = false;
}


Ship::~Ship(void)
{
}

void Ship::draw() {
	glPushMatrix();
	//glTranslated(xpos,-ypos-3,zpos-5);
	glTranslated(xpos,-ypos-0.2,zpos);
	glRotatef(-xrot,1.0,0.0,0.0);
	glRotatef(-yrot,0.0,1.0,0.0);
	glRotatef(-zrot,0,0,1);
	glPushMatrix();
	glRotated(180,1,0,0);
	glutSolidCone(0.1,0.4,10,10);
	glPopMatrix();
	glPopMatrix();
}
void Ship::camera() {
	glRotatef(xrot,1.0,0.0,0.0);
	glRotatef(yrot,0.0,1.0,0.0);
	glRotatef(zrot,0,0,1);
	glTranslated(-xpos,ypos,-zpos);
	glutPostRedisplay();
}

void Ship::setXPos(float x) {
	xpos = x;
}
float Ship::getXPos() {
	return xpos;
}
void Ship::setYPos(float y) {
	ypos=y;
}
float Ship::getYPos() {
	return ypos;
}
void Ship::setZPos(float z) {
	zpos = z;
}
float Ship::getZPos() {
	return zpos;
}
void Ship::setYRot(float angle) {
	yrot = angle;
}
float Ship::getYRot() {
	return yrot;
}
void Ship::setXRot(float angle) {
	xrot=angle;
}
float Ship::getXRot() {
	return xrot;
}
float Ship::getZRot() {
	return zrot;
}
void Ship::setZRot(float angle) {
	zrot = angle;
}
void Ship::toggleScout() {
	scout=!scout;
	if(scout) {
		xpos-=0;
		ypos-=3;
		zpos-=10;
	}
}