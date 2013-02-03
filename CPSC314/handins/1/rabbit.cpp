#include <stdio.h>
#include <stdlib.h>
#include <GL/glut.h>
#include "rabbit.h"
#include <iostream>
using namespace std;
rabbit::rabbit(void)
{
	rabbit::frame=true;
	rabbit::smoothAnimation=true;
}

rabbit::~rabbit(void)
{
}

GLfloat headAngle;
GLfloat leftArmAngle;
GLfloat rightArmAngle;
GLfloat leftLegAngle;
GLfloat rightLegAngle;
GLfloat rearAngle;
GLfloat lowerCurlAngle;
GLfloat upperCurlAngle;
GLfloat altitude;
GLfloat upperBody;
GLfloat lowerBody;
GLfloat leftEarAngle;
GLfloat rightEarAngle;
GLfloat leftEarOffsetY;
GLfloat leftEarOffsetZ;
GLfloat rightEarOffsetY;
GLfloat rightEarOffsetZ;

void rabbit::drawCube() {
	((rabbit::frame) ? glutSolidCube(1) : glutWireCube(1));
}
void rabbit::toggleSmoothAnimation() {
	rabbit::smoothAnimation = ((rabbit::smoothAnimation) ? false : true);
}
void rabbit::toggleWire() {
	rabbit::frame = ((rabbit::frame) ? false : true);
}
void rabbit::toggleHead() {
	rabbit::head = ((rabbit::head) ? false : true);
}
void rabbit::toggleLeftArm() {
	rabbit::leftArm = ((rabbit::leftArm) ? false : true);
}
void rabbit::toggleRightArm() {
	rabbit::rightArm = ((rabbit::rightArm) ? false : true);
}
void rabbit::toggleLeftLeg() {
	rabbit::leftLeg = ((rabbit::leftLeg) ? false : true);
}
void rabbit::toggleRightLeg() {
	rabbit::rightLeg = ((rabbit::rightLeg) ? false : true);
}
void rabbit::toggleLeftEar() {
	rabbit::leftEar = ((rabbit::leftEar) ? false : true);
}
void rabbit::toggleRightEar() {
	rabbit::rightEar = ((rabbit::rightEar) ? false : true);
}
void rabbit::toggleRear() {
	rabbit::rear = ((rabbit::rear) ? false : true);
}
void rabbit::toggleBodyCurl() {
	rabbit::bodyCurl = ((rabbit::bodyCurl) ? false : true);
}
void rabbit::toggleUpperCurl() {
	rabbit::upperBodyCurl == ((rabbit::upperBodyCurl) ? false :true);
}
void rabbit::toggleJump() {
	rabbit::jump = ((rabbit::jump) ? false : true);
}
void rabbit::toggleUpperBody() {
	rabbit::uBody = ((rabbit::uBody) ? false : true);
}
void rabbit::toggleLowerBody() {
	rabbit::lBody = ((rabbit::lBody) ? false : true);
}

void rabbit::drawLowerBody() {
	glRotatef(20,0,0,1);
	glScalef(0.8,0.9,1.35);
	drawCube();
}
void rabbit::drawTorso() {
	glRotatef(30,0,0,1);
	glScalef(2,1,1.2);
	drawCube();
}
void rabbit::drawUpperBody() {
	glTranslatef(-0.4,-0.4,0);
	glRotatef(40,0,0,1);
	glScalef(1.2,1,1);
	drawCube();
}
void rabbit::drawArm(rabbit::side side) {
	GLfloat z;
	((side==left) ? z=0.5 : z=-0.5);

	glTranslatef(0,-0.8,z);
	glRotatef(-90,0,0,1);
	glPushMatrix();
	glScalef(0.5,0.2,0.2);
	drawCube();
	glPopMatrix();
	glTranslatef(0.2,0.1,0);
	glRotatef(45,0,0,1);
	glScalef(0.3,0.2,0.1);
	drawCube();
}
void rabbit::drawNeck() {
	glRotatef(-20,0,0,1);
	glScalef(0.75,0.2,1);
	drawCube();
}
void rabbit::drawHead() {
	glScalef(0.9,0.9,1);
	drawCube();
}
void rabbit::drawEar(rabbit::side side) {
	GLfloat z;
	((side==left) ? z=0.4 : z=-0.4);
	glTranslatef(-0.3,0.7,z);
	glPushMatrix();
	glScalef(0.2,1,0.2);
	drawCube();
	glPopMatrix();
}
void rabbit::drawEye(rabbit::side side) {
	GLfloat z;
	((side==left) ? z=0.3 : z=-0.3);
	glTranslatef(0.5,0.2,z);
	glPushMatrix();
	glScalef(0.1,0.1,0.1);
	glColor4f(0,0.3,0.6,0.9);
	drawCube();
	glPopMatrix();
}
void rabbit::drawLeg(rabbit::side side) {
	GLfloat z;
	((side==left) ? z=0.4 : z=-0.4);
	glTranslatef(0.3,-0.3,z);
	glPushMatrix();
	glRotatef(15,0,0,1);
	glScalef(0.8,0.5,0.6);
	drawCube();
	glPopMatrix();
	glTranslatef(0.1,-0.2,0);
	glPushMatrix();
	glRotatef(60,0,0,1);
	glScalef(0.65,0.2,0.2);
	drawCube();
	glPopMatrix();
	glTranslatef(0,-0.25,0);
	glScalef(0.3,0.2,0.2);
	drawCube();
}
void rabbit::drawTail() {
	glTranslatef(-0.5,0,0);
	glScalef(0.3,0.3,0.3);
	drawCube();
}
void rabbit::moveHead() {
	GLfloat f;
	if (headAngle>=30) rabbit::dirHead=down;
	if (headAngle<=0) rabbit::dirHead=up;
	((rabbit::smoothAnimation) ? 
		((rabbit::dirHead==up) ? f=1 : f=-1):
		((rabbit::dirHead==up) ? f=30 : f=-30));

	headAngle += f;
	glRotatef(headAngle, 0 ,0, 1);
	if (headAngle>=30||headAngle<=0) rabbit::head=false;

}
void rabbit::moveArm(rabbit::side side) {
	GLfloat f;
	switch(side){
	case left:
		if(leftArmAngle>=30) rabbit::dirLeftArm=down;
		if(leftArmAngle<=0) rabbit::dirLeftArm=up;
		((rabbit::smoothAnimation) ?
			((rabbit::dirLeftArm==up) ? f=1 : f=-1):
			((rabbit::dirLeftArm==up) ? f=30 : f=-30));
		leftArmAngle += f;

		glRotatef(leftArmAngle, 0, 0, 1);
		if (leftArmAngle>=30||leftArmAngle<=0) rabbit::leftArm=false;
		break;
	case right:
		if(rightArmAngle>=30) rabbit::dirRightArm=down;
		if(rightArmAngle<=0) rabbit::dirRightArm=up;
		((rabbit::smoothAnimation) ?
			((rabbit::dirRightArm==up) ? f=1 : f=-1):
			((rabbit::dirRightArm==up) ? f=30 : f=-30));
		rightArmAngle += f;
		glRotatef(rightArmAngle, 0, 0, 1);
		if (rightArmAngle>=30||rightArmAngle<=0) rabbit::rightArm=false;
		break;
	}
}
void rabbit::moveLeg(rabbit::side side) {
	GLfloat f;
	switch(side) {
	case left:
		if(leftLegAngle>=30) rabbit::dirLeftLeg=down;
		if(leftLegAngle<=0) rabbit::dirLeftLeg=up;
		((rabbit::smoothAnimation) ?
			((rabbit::dirLeftLeg==up) ? f=1: f=-1):
			((rabbit::dirLeftLeg==up) ? f=30 : f=-30));
		leftLegAngle += f;
		glRotatef(leftLegAngle, 0, 0, 1);
		if (leftLegAngle>=30||leftLegAngle<=0) rabbit::leftLeg=false;
		break;
	case right:
		if(rightLegAngle>=30) rabbit::dirRightLeg=down;
		if(rightLegAngle<=0) rabbit::dirRightLeg=up;
		((rabbit::smoothAnimation) ?
			((rabbit::dirRightLeg==up) ? f=1: f=-1):
			((rabbit::dirRightLeg==up) ? f=30 : f=-30));
		rightLegAngle += f;
		glRotatef(rightLegAngle, 0, 0, 1);
		if (rightLegAngle>=30||rightLegAngle<=0) rabbit::rightLeg=false;
		break;
	}
}
void rabbit::moveRear() {
	GLfloat f;
	if (rearAngle>=35) rabbit::dirRear=down;
	if (rearAngle<=0) rabbit::dirRear=up;
	((rabbit::smoothAnimation) ?
		((rabbit::dirRear==up) ? f=1 : f=-1):
		((rabbit::dirRear==up) ? f=30: f=-30));
	rearAngle += f;
	glRotatef(rearAngle, 0, 0, 1);
	if (rearAngle>=35||rearAngle<=0) rabbit::rear=false;
}
void rabbit::moveCurl() {
	GLfloat f;
	if (lowerCurlAngle>=30) rabbit::dirBodyCurl=down;
	if (lowerCurlAngle<=0) rabbit::dirBodyCurl=up;
	((rabbit::smoothAnimation) ?
		((rabbit::dirBodyCurl==up) ? f=1 : f=-1):
		((rabbit::dirBodyCurl==up) ? f=30: f=-30));
	lowerCurlAngle += f;
	glRotatef(lowerCurlAngle, 0, 0, 1);
	if (lowerCurlAngle>=30||lowerCurlAngle<=0) rabbit::bodyCurl=false;
}
void rabbit::moveUpperCurl() {
	GLfloat f;
	if(upperCurlAngle>=30) rabbit::dirUpperBodyCurl=down;
	if(upperCurlAngle<=0) rabbit::dirUpperBodyCurl=up;
	((rabbit::smoothAnimation) ?
		((rabbit::dirBodyCurl==up) ? f=-1 : f=1):
		((rabbit::dirBodyCurl==up) ? f=-30: f=30));
	upperCurlAngle += f;
	glRotatef(upperCurlAngle, 0, 0, 1);
	if (upperCurlAngle>=30||upperCurlAngle<=0) rabbit::upperBodyCurl=false;
}
void rabbit::moveJump() {
	GLfloat f;
	if(altitude>=1) rabbit::dirJump=down;
	if(altitude<=0) rabbit::dirJump=up;
	((rabbit::smoothAnimation) ?
		((rabbit::dirJump==up) ? f=0.1 : f=-0.1):
		((rabbit::dirJump==up) ? f=1 : f=-1));
	altitude += f;
	glTranslatef(0,altitude,0);
	if (altitude>=1||altitude<=0) rabbit::jump=false;
}
void rabbit::openUpperBody() {
	GLfloat f;
	if(upperBody>=45) rabbit::dirUpperBody=down;
	if(upperBody<=0) rabbit::dirUpperBody=up;
	((rabbit::smoothAnimation) ?
		((rabbit::dirUpperBody==up) ? f=3 : f=-3):
		((rabbit::dirUpperBody==up) ? f=45 :f=-45));
	upperBody += f;
	glRotatef(upperBody,0,0,1);
	if (upperBody>=45||upperBody<=0.0) rabbit::uBody=false;
}
void rabbit::openLowerBody() {
	GLfloat f;
	if(lowerBody<=-100) rabbit::dirLowerBody=down;
	if(lowerBody>=0) rabbit::dirLowerBody=up;
	((rabbit::smoothAnimation) ?
		((rabbit::dirLowerBody==up) ? f=-10 : f=10):
		((rabbit::dirLowerBody==up) ? f=-100: f=100));
	lowerBody += f;
	glRotatef(lowerBody,0,0,1);
	if (lowerBody<=-100||lowerBody>=0) rabbit::lBody=false;
}
void rabbit::moveEar(rabbit::side side) {
	GLfloat f;
	switch (side) {
	case left:
		if(leftEarAngle>=120) rabbit::dirLeftEar=up;
		if(leftEarAngle<=0) rabbit::dirLeftEar=down;
		((rabbit::smoothAnimation) ?
			((rabbit::dirLeftEar==down) ? f=10 : f=-10):
			((rabbit::dirLeftEar==down) ? f=120 : f=-120));

		leftEarAngle += f;
		glTranslatef(0,0.4,0.5);
		glRotatef(leftEarAngle, 1, 0, 0);
		glTranslatef(0, -0.4,-0.5);
		if (leftEarAngle>=120||leftEarAngle<=0) rabbit::leftEar=false;
		break;
	case right:
		if(rightEarAngle<=-120) rabbit::dirRightEar=up;
		if(rightEarAngle>=0) rabbit::dirRightEar=down;
		((rabbit::smoothAnimation) ?
			((rabbit::dirRightEar==down) ? f=-10 : f=10):
			((rabbit::dirRightEar==down) ? f=-120 : f=120));
		rightEarAngle += f;
		glTranslatef(0,0.4,-0.5);
		glRotatef(rightEarAngle, 1, 0, 0);
		glTranslatef(0,-0.4,0.5);
		if (rightEarAngle<=-120||rightEarAngle>=0) rabbit::rightEar=false;
		break;
	}
}
void rabbit::draw() {
	glPushMatrix();
	glColor3f(1,1,1);
	((rabbit::jump) ? rabbit::moveJump() : glTranslatef(0,altitude,0));
	///////////////////////
	// Body
	///////////////////////
	glPushMatrix();
	////STARTING POINT -> LOWER BODY

	glTranslatef(-0.8,0.8,0);
	glPushMatrix();

	((rabbit::rear) ? rabbit::moveRear() : glRotatef(rearAngle, 0, 0, 1));
	glPushMatrix();
	((rabbit::bodyCurl) ? rabbit::moveCurl() : glRotatef(lowerCurlAngle, 0, 0, 1));
	((rabbit::lBody) ? rabbit::openLowerBody() : glRotatef(lowerBody,0,0,1));
	rabbit::drawLowerBody();
	glPopMatrix();
	glTranslatef(0.8,0.4,0);
	glPushMatrix();
	rabbit::drawTorso();
	glPopMatrix();
	glTranslatef(0.8,0.5,0);
	((rabbit::bodyCurl) ? rabbit::moveUpperCurl() : glRotatef(upperCurlAngle, 0, 0, 1));
	((rabbit::uBody) ? rabbit::openUpperBody() : glRotatef(upperBody,0,0,1));
	glPushMatrix();
	
	glPushMatrix();
	rabbit::drawUpperBody();
	glPopMatrix();
	//// Left Arm
	glPushMatrix();
	((rabbit::leftArm) ? rabbit::moveArm(left) : glRotatef(leftArmAngle, 0, 0, 1));
	rabbit::drawArm(left);
	glPopMatrix();
	//// Right Arm
	glPushMatrix();
	((rabbit::rightArm) ? rabbit::moveArm(right) : glRotatef(rightArmAngle, 0, 0, 1));
	rabbit::drawArm(right);				
	glPopMatrix();
	glPopMatrix();

	//// Neck Animation
	((rabbit::head)? rabbit::moveHead(): glRotatef(headAngle, 0, 0, 1));

	glTranslatef(0,0.2,0);
	glPushMatrix();
	rabbit::drawNeck();
	glPopMatrix();

	/////HEAD
	glTranslatef(0.3,0.3,0);
	glPushMatrix();
	rabbit::drawHead();
	glPopMatrix();

	///Ears
	//Left
	glPushMatrix();
	if (rabbit::leftEar) {
		rabbit::moveEar(left);
	} else {
		glTranslatef(0,0.4,0.5);
		glRotatef(leftEarAngle, 1, 0, 0);
		glTranslatef(0, -0.4,-0.5);
	}
	rabbit::drawEar(left);
	glPopMatrix();
	//Right
	glPushMatrix();
	if (rabbit::rightEar) {
		rabbit::moveEar(right);
	} else {
		glTranslatef(0,0.4,-0.5);
		glRotatef(rightEarAngle, 1, 0, 0);
		glTranslatef(0,-0.4,0.5);
	}
	rabbit::drawEar(right);
	glPopMatrix();

	//Eyes
	//Left
	glPushMatrix();
	rabbit::drawEye(left);
	glPopMatrix();
	// Right
	glPushMatrix();
	rabbit::drawEye(right);
	glPopMatrix();
	///////////////////////////////////////COLOR RESET//////////////////////
	glColor3f(1,1,1);
	glPopMatrix();
	// RETURN TO LOWER BODY
	//	Left leg
	((rabbit::bodyCurl) ? rabbit::moveCurl() : glRotatef(lowerCurlAngle, 0, 0, 1));
	((rabbit::lBody) ? rabbit::openLowerBody() : glRotatef(lowerBody,0,0,1));
	glPushMatrix();
	((rabbit::leftLeg) ? rabbit::moveLeg(left) : glRotated(leftLegAngle, 0, 0, 1));
	rabbit::drawLeg(left);
	glPopMatrix();
	glPushMatrix();
	((rabbit::rightLeg) ? rabbit::moveLeg(right) : glRotated(rightLegAngle, 0, 0, 1));
	rabbit::drawLeg(right);
	glPopMatrix();
	//tail
	glPushMatrix();
	rabbit::drawTail();
	glPopMatrix();


	glPopMatrix();
	//Middle
	glPopMatrix();
}