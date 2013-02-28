#include "Solar.h"

Solar::Solar(void)
{
	geo = false;
	DEG2RAD = 3.14159/180;
	velocity = 1;
	Mer = 0.0, Ven = 0.0, Ear = 0.0, Moo = 0.0, Moo2 = 0.0, Mar = 0.0, Jup = 0.0, Sat = 0.0, Ura = 0.0, Nep = 0.0, Plu = 0.0;
	shine[0] = 10;
	mooCol[0]=2.3,	mooCol[1]=2.3,	mooCol[2]=2.3;
	moo2Col[0]=0.93,moo2Col[1]=0.93,moo2Col[2]=0.93;
	merCol[0]=2.05,	merCol[1]=1.90,	merCol[0]=1.12;
	venCol[0]=1.39,	venCol[1]=0.54,	venCol[2]=0.26;
	earCol[0]=0.0,	earCol[1]=0.3,	earCol[2]=0.6;
	marCol[0]=1.39,	marCol[1]=0.26,	marCol[2]=0.0;
	jupCol[0]=0.80,	jupCol[1]=0.53,	jupCol[2]=0.25;
	satCol[0]=0.6,	satCol[1]=0.5,	satCol[2]=0.09;
	uraCol[0]=0.40,	uraCol[1]=0.900,uraCol[2]=1.0;
	nepCol[0]=0.135,nepCol[1]=0.115,nepCol[2]=0.85;
	pluCol[0]=0.7,	pluCol[1]=0.8,	pluCol[2]=1.00;

	eyex=80.0,		eyey=10.0,		eyez= 10.0;
	centerx=0.0,	centery=0.0,	centerz=0.0;
	upx=0.0,		upy=1.0,		upz=0.0;
	
}


Solar::~Solar(void)
{
}
void Solar::incVelocity() {
	velocity++;
}
void Solar::decVelocity() {
	velocity--;
}
float Solar::getVelocity() {
	return velocity;
}
void Solar::spin() {
	Mer += 16.0 * velocity;
	if(Mer > 360.0)
		Mer = Mer - 360.0;

	Ven += 12.0 * velocity;
	if(Ven > 360.0)
		Ven = Ven - 360.0;

	Ear += 10.0 * velocity;
	if(Ear > 360.0)
		Ear = Ear - 360.0;

	Moo += 2.0 * velocity;
	if(Moo > 360.0)
		Moo = Moo - 360.0;

	Moo2 += 1.0 * velocity;
	if(Moo2 > 360.0)
		Moo2 = Moo2 - 360.0;

	Mar += 8.0 * velocity;
	if(Mar > 360.0)
		Mar = Mar - 360.0;

	Jup += 5.0 * velocity;
	if(Jup > 360.0)
		Jup = Jup - 360.0;

	Sat += 4.0 * velocity;
	if(Sat > 360.0)
		Sat = Sat - 360.0;

	Ura += 3.0 * velocity;
	if(Ura > 360.0)
		Ura = Ura - 360.0;

	Nep += 2.0 * velocity;
	if(Nep > 360.0)
		Nep = Nep - 360.0;

	Plu += 1.0 * velocity;
	if(Plu > 360.0)
		Plu = Plu - 360.0;
	

}
void Solar::drawCircle(float radius) {
	glBegin(GL_LINE_LOOP);
 
   for (int i=0; i<=360; i++)
   {
      float degInRad = i*DEG2RAD;
      glVertex2f(cos(degInRad)*radius,sin(degInRad)*radius);
   }
 
   glEnd();
}

void Solar::draw() {
GLfloat position[] = { 0.0, 0.0, 1.5, 1.0 };

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, shine);

	glPushMatrix();

		gluLookAt(eyex, eyey, eyez, centerx, centery, centerz, upx, upy, upz);

		glPushMatrix();

			glPushMatrix();
				glRotatef(90,1,0,0);
				drawCircle(4.9);
			glPopMatrix();
			glRotated((GLdouble) Mer, 0.0, 1.0, 0.0);

			

			glTranslated(0.0, 0.0, 4.9);

			glRotated((GLdouble) Mer, 0.0, 1.0, 0.0);

			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, merCol);

			glLightModelfv(GL_LIGHT_MODEL_AMBIENT, merCol);

			glutSolidSphere(0.049, 20, 20);
			

			glDisable(GL_DIFFUSE);
			if (geo) {

				glPushMatrix();
				glTranslated(0.1,0.0,0.1);
				glRotated((GLdouble) Mer, 0.0, 1.0, 0.0);
				glutSolidCone(0.1,0.4,10,10);
				glPopMatrix();
			}

		glPopMatrix();

		glPushMatrix();
			glPushMatrix();
				glRotatef(90,1,0,0);
				drawCircle(5.2);
			glPopMatrix();

			glRotated((GLdouble) Ven, 0.0, 1.0, 0.0);

			glTranslated(0.1, 0.0, 5.2);

			glRotated((GLdouble) Ven - 1, 0.0, 1.0, 0.0); //Retrograde (Counter) Rotation

			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, venCol);

			glLightModelfv(GL_LIGHT_MODEL_AMBIENT, venCol);

			glutSolidSphere(0.12, 20, 20);
			

			glDisable(GL_DIFFUSE);
			if (geo) {
				glPushMatrix();
				glTranslated(0.1,0.0,0.1);
				glRotated((GLdouble) Ven, 0.0, 1.0, 0.0);
				glutSolidCone(0.1,0.4,10,10);
				glPopMatrix();
			}

		glPopMatrix();

		glPushMatrix();
			glPushMatrix();
				glRotatef(90,1,0,0);
				drawCircle(5.6);
			glPopMatrix();

			glRotated((GLdouble) Ear, 0.0, 1.0, 0.0);

			glTranslated(0.1, 0.0, 5.6);

			glRotated((GLdouble) Ear, 0.0, 1.0, 0.0);

			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, earCol);

			glLightModelfv(GL_LIGHT_MODEL_AMBIENT, earCol);

			glutSolidSphere(0.13, 20, 20);

			glDisable(GL_DIFFUSE);

			glPushMatrix();	
				
				if (geo) {
					glPushMatrix();
					glPushMatrix();
					gluLookAt(0.1,0.0,0.1,0,0,0,0,1,0);
					
					glPopMatrix();
					glTranslated(0.1,0.0,0.1);
					glRotated((GLdouble) Ear, 0.0, 1.0, 0.0);
							


					glutSolidCone(0.1,0.4,10,10);
					glPopMatrix();
				}
				

				glRotated((GLdouble) Moo, 0.0, 1.0, 0.0);

				glTranslated(0.1, 0.0, 0.1);

				glRotated((GLdouble) Moo, 0.0, 1.0, 0.0);

				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, mooCol);

				glLightModelfv(GL_LIGHT_MODEL_AMBIENT, mooCol);

				glutSolidSphere(0.02, 20, 20);
				

				glDisable(GL_DIFFUSE);

			glPopMatrix();

		glPopMatrix();

		glPushMatrix();
			glPushMatrix();
				glRotatef(90,1,0,0);
				drawCircle(6.0);
			glPopMatrix();

			glRotated((GLdouble) Mar, 0.0, 1.0, 0.0);

			glTranslated(0.1, 0.0, 6.0);

			glRotated((GLdouble) Mar, 0.0, 1.0, 0.0);

			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, marCol);

			glLightModelfv(GL_LIGHT_MODEL_AMBIENT, marCol);

			glutSolidSphere(0.067, 20, 20);
			

			glDisable(GL_DIFFUSE);
			if (geo) {
				glPushMatrix();
				glTranslated(0.1,0.0,0.1);
				glRotated((GLdouble) Mar, 0.0, 1.0, 0.0);
				glutSolidCone(0.1,0.4,10,10);
				glPopMatrix();
			}
			glPushMatrix();

				glRotated((GLdouble) Moo, 0.0, 1.0, 0.0);

				glTranslated(0.0, 0.0, 0.1);

				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, mooCol);

				glLightModelfv(GL_LIGHT_MODEL_AMBIENT, mooCol);

				glRotated((GLdouble) Moo, 0.0, 1.0, 0.0);

				glutSolidSphere(0.015, 20, 10);

			glPopMatrix();

			glPushMatrix();

				glRotated((GLdouble) Moo2, 0.0, 1.0, 0.0);

				glTranslated(0.0, 0.0, 0.15);

				glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, moo2Col);

				glLightModelfv(GL_LIGHT_MODEL_AMBIENT, moo2Col);

				glRotated((GLdouble) Moo2, 0.0, 1.0, 0.0);

				glutSolidSphere(0.01, 20, 10);
			glPopMatrix();

		glPopMatrix();

		glPushMatrix();
			glPushMatrix();
				glRotatef(90,1,0,0);
				drawCircle(9.7);
			glPopMatrix();

			glRotated((GLdouble) Jup, 0.0, 1.0, 0.0);

			glTranslated(0.1, 0.0, 9.7);

			glRotated((GLdouble) Jup, 0.0, 1.0, 0.0);

			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, jupCol);

			glLightModelfv(GL_LIGHT_MODEL_AMBIENT, jupCol);

			glutSolidSphere(1.42, 20, 20);
			
			glDisable(GL_DIFFUSE);
			if (geo) {
				glPushMatrix();
				glTranslated(2,0.0,0.1);
				glRotated((GLdouble) Jup, 0.0, 1.0, 0.0);
				glutSolidCone(0.1,0.4,10,10);
				glPopMatrix();
			}

		glPopMatrix();

		glPushMatrix();
			glPushMatrix();
				glRotatef(90,1,0,0);
				drawCircle(14.0);
			glPopMatrix();

			glRotated((GLdouble) Sat, 0.0, 1.0, 0.0);

			glTranslated(0.1, 0.0, 14.0);

			glRotated((GLdouble) Sat, 0.0, 1.0, 0.0);

			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, satCol);

			glLightModelfv(GL_LIGHT_MODEL_AMBIENT, satCol);

			glPushMatrix();
			glRotatef(45,1,0,0);
				for (float i=1.7;i<=2.5;i+=0.05) {
					drawCircle(i);
					//std::cout<<i<<std::endl;
				}
			glPopMatrix();
			glutSolidSphere(1.20, 20, 20);
			

			glDisable(GL_DIFFUSE);
			if (geo) {
				glPushMatrix();
				glTranslated(2,0.0,0.1);
				glRotated((GLdouble) Sat, 0.0, 1.0, 0.0);
				glutSolidCone(0.1,0.4,10,10);
				glPopMatrix();
			}
		glPopMatrix();

		glPushMatrix();
			glPushMatrix();
				glRotatef(90,1,0,0);
				drawCircle(23.5);
			glPopMatrix();

			glRotated((GLdouble) Ura, 0.0, 1.0, 0.0);

			glTranslated(0.1, 0.0, 23.5);

			glRotated((GLdouble) Ura - 1, 1.0, 0.0, 0.0); //Retrograde Rotation

			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, uraCol);

			glLightModelfv(GL_LIGHT_MODEL_AMBIENT, uraCol);

			glutSolidSphere(0.51, 20, 20);
			

			glDisable(GL_DIFFUSE);
			if (geo) {
				glPushMatrix();
				glTranslated(1,0.0,0.1);
				glRotated((GLdouble) Ura, 0.0, 1.0, 0.0);
				glutSolidCone(0.1,0.4,10,10);
				glPopMatrix();
			}
		glPopMatrix();

		glPushMatrix();
			glPushMatrix();
				glRotatef(90,1,0,0);
				drawCircle(34.5);
			glPopMatrix();

			glRotated((GLdouble) Nep, 0.0, 1.0, 0.0);

			glTranslated(0.1, 0.0, 34.5);

			glRotated((GLdouble) Nep, 1.0, 0.0, 0.0); //Rotates top to bottom

			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, nepCol);

			glLightModelfv(GL_LIGHT_MODEL_AMBIENT, nepCol);

			glutSolidSphere(0.49, 20, 20);
			

			glDisable(GL_DIFFUSE);
			if (geo) {
				glPushMatrix();
				glTranslated(1,0.0,0.1);
				glRotated((GLdouble) Ura, 0.0, 1.0, 0.0);
				glutSolidCone(0.1,0.4,10,10);
				glPopMatrix();
			}
		glPopMatrix();

		glPushMatrix();
		
			glPushMatrix();
				glRotatef(90,1,0,0);
				drawCircle(44.0);
			glPopMatrix();

			glRotated((GLdouble) Plu, 0.0, 1.0, 0.0);

			glTranslated(0.1, 0.0, 44.0);

			glRotated((GLdouble) Plu, 0.0, 1.0, 0.0);

			glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, pluCol);

			glLightModelfv(GL_LIGHT_MODEL_AMBIENT, pluCol);

			glutSolidSphere(0.1, 20, 20);
			

			glDisable(GL_DIFFUSE);
			if (geo) {
				glPushMatrix();
				glTranslated(1,0.0,0.1);
				glRotated((GLdouble) Plu, 0.0, 1.0, 0.0);
				glutSolidCone(0.1,0.4,10,10);
				glPopMatrix();
			}
		glPopMatrix();

		glLightfv(GL_LIGHT0, GL_POSITION, position);

		GLfloat global_ambient[] = {0.5f, 0.5f, 0.5f, 1.0f}; //Emission

		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, global_ambient); //Emission

		glDisable(GL_LIGHTING);

		glColor3f(1.0, 1.0, 0.0);

		glRotated((GLdouble) Ear, 0.0, 1.0, 0.0);

		glutSolidSphere(4.00, 20, 20);

		glEnable(GL_LIGHTING);

		glPopMatrix();
}