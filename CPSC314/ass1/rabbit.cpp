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
	rabbit::rear = ((rabbit::bodyCurl) ? false : true);
}
void rabbit::toggleJump() {
	rabbit::jump = ((rabbit::jump) ? false : true);
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
	glRotatef(-85,0,0,1);
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
	((side==left) ? z=0.3 : z=-0.3);
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

void rabbit::draw() {
	glPushMatrix();
glColor3f(1,1,1);
  ///////////////////////
  // Body
  ///////////////////////
	glPushMatrix();
		////STARTING POINT -> LOWER BODY
		glTranslatef(-0.8,0.8,0);
		glPushMatrix();
			glPushMatrix();
				rabbit::drawLowerBody();
			glPopMatrix();
			glTranslatef(0.8,0.4,0);
			glPushMatrix();
				rabbit::drawTorso();
			glPopMatrix();
			glTranslatef(0.8,0.5,0);
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
				rabbit::drawEar(left);
			glPopMatrix();
			//Right
			glPushMatrix();
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
		// BackLegs
		//	Left leg
		glPushMatrix();
			rabbit::drawLeg(left);
		glPopMatrix();
		glPushMatrix();
			rabbit::drawLeg(right);
		glPopMatrix();
		//tail
		glPushMatrix();
			rabbit::drawTail();
		glPopMatrix();


	glPopMatrix();
  //Middle
glPopMatrix();
//glutPostRedisplay();

}