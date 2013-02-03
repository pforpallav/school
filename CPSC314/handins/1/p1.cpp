#include <stdio.h>
#include <stdlib.h>

#include "rabbit.h"
#ifdef __APPLE__
#  include <GLUT/glut.h>
#else
#  include <GL/glut.h>
#endif



int dumpPPM(int frameNum);

void drawAxis();
void drawFloor();
void myScale(float x, float y, float z);

rabbit r;

unsigned char camera = 'r';



int iCount = 0;       // used for numbering the PPM image files
int Width = 400;      // window width (pixels)
int Height = 400;     // window height (pixels)
bool Dump=false;      // flag set to true when dumping animation frames

void timer(int val) {
	glutPostRedisplay();
	glutTimerFunc(10, timer, 0);
}
void keyboardCallback(unsigned char c, int x, int y) {
  switch (c) {

  case 'h':
	  r.toggleHead();
	  break;
  case 'l':
	  r.toggleLeftArm();
	  break;
  case 'm':
	  r.toggleRightArm();
	  break;
  case 'n':
	  r.toggleLeftLeg();
	  break;
  case 'o':
	  r.toggleRightLeg();
	  break;
  case 'e':
	  r.toggleRightEar();
	  break;
  case 'w':
	  r.toggleLeftEar();
	  break;
  case 't':
	  r.toggleRear();
	  break;
  case 'c' :
	  r.toggleBodyCurl();
	  r.toggleUpperCurl();
	  break;
  case 'j':
	  r.toggleJump();
	  r.toggleUpperBody();
	  r.toggleLowerBody();
	  break;
  case 'x':
	  r.toggleWire();
	  break;
  case 's':
	  r.toggleSmoothAnimation();
	  break;
  case 'q':
    exit (0);
    break;
  case 'p':
    camera = 'p';
    break;
  case 'f':
    camera = 'f';
    break;
  case 'b':
    camera = 'b';
    break;
  case 'a':
    camera = 'a';
    break;
  case 'u':
    camera = 'u';
    break;
  case 'r':
    camera = 'r';
    break;
  case 'i':
    dumpPPM(iCount);
    iCount++;
    break;
  case 'd':               // dump animation PPM frames
    iCount = 0;         // image file count
    Dump = !Dump;
    break;
  }
  
  //glutPostRedisplay();
}

void reshapeCallback(int w, int h)
{
   Width = w;
   Height = h;
   glViewport(0, 0, w, h);
}
void myScale(GLfloat x, GLfloat y, GLfloat z) {
	glPushMatrix();
	glScalef(x,y,z);
	glPopMatrix();
}
void displayCallback()
{
  // clear the color buffer
  glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

  // set up camera
  glMatrixMode( GL_PROJECTION );
  glLoadIdentity();
  gluPerspective( 45, 1.0, 0.1, 200.0 );

  glMatrixMode( GL_MODELVIEW );
  glLoadIdentity();
  glTranslatef( 0.0, 0.0, -10.0 );
  switch (camera) {
  case 'p':
    glRotatef( 180, 0.0, 1.0, 0.0 );
    break;
  case 'f':
    glRotatef( -90, 0.0, 1.0, 0.0 );
    break;
  case 'b':
    glRotatef( 90, 0.0, 1.0, 0.0 );
    break;
  case 'a':
    glRotatef( 90, 1.0, 0.0, 0.0 );
    break;
  case 'u':
    glRotatef( -90, 1.0, 0.0, 0.0 );
    break;
  case 'r':
    break;
  }

  drawAxis();
  
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // Draw your rabbit here
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

	r.draw();

 



  // draw after the opaque objects, since it is translucent
  drawFloor();

  // draw the buffer to the screen
  glutSwapBuffers();

  if (Dump) {               // save images to file
    dumpPPM(iCount);
    iCount++;
  }

  GLenum error = glGetError();
  if(error != GL_NO_ERROR)
    printf("ERROR: %s\n", gluErrorString(error));
}


//---------------------------------------------------------------

// Draw a set of coloured cones representing the current local coord system.
// X -> red, Y -> green, Z -> blue.
void drawAxis() {
  float axisHeight = 0.75; // TODO
  float axisBase = 0.075;
  glColor3f(0, 0, 1);
  glutSolidCone(axisBase, axisHeight, 8, 2);
  glPushMatrix();
  glRotatef(90, 0, 1, 0);
  glColor3f(1, 0, 0);
  glutSolidCone(axisBase, axisHeight, 8, 2);
  glPopMatrix();
  glPushMatrix();
  glRotatef(-90, 1, 0, 0);
  glColor3f(0, 1, 0);
  glutSolidCone(axisBase, axisHeight, 8, 2);
  glPopMatrix();
}

void drawFloor() {
  // Draw a floor. Since it is transparent, we need to do it AFTER all of
  // the opaque objects.
  for (int x = -5; x < 5; x++) {
    for (int y = -5; y < 5; y++) {
      glColor4f(1, 1, 1, (x + y) % 2 ? 0.75 : 0.5);
      glNormal3f(0, 1, 0);
      glBegin(GL_POLYGON);
      glVertex3f(x    , 0, y    );
      glVertex3f(x + 1, 0, y    );
      glVertex3f(x + 1, 0, y + 1);
      glVertex3f(x    , 0, y + 1);
      glEnd();
    }
  }
  // visible from initial side angle
  glBegin(GL_POLYGON);
  glVertex3f(-5,    0, 0);
  glVertex3f(-5, -.05, 0);
  glVertex3f( 5, -.05, 0);
  glVertex3f( 5,    0, 0);
  glEnd();
  
  // visible from front angle
  glBegin(GL_POLYGON);
  glVertex3f(0,    0, -5);
  glVertex3f(0, -.05, -5);
  glVertex3f(0, -.05, 5);
  glVertex3f(0,    0, 5);
  glEnd();
}

int dumpPPM(int frameNum)
{
  FILE *fp;
  const int maxVal=255;
  register int y;
  unsigned char *pixels;

  glReadBuffer( GL_FRONT );
  char fname[100];
  sprintf(fname,"./ppm/img%03d.ppm",frameNum);
  fp = fopen(fname,"wb");
  if (!fp) {
	printf("Unable to open file '%s'\n",fname);
	return 1;
  }
  printf("Saving image `%s`\n",fname);
  fprintf(fp, "P6 ");
  fprintf(fp, "%d %d ", Width, Height);
  fprintf(fp, "%d", maxVal);
  putc(13,fp);
  pixels = new unsigned char [3*Width];

  y = 0;
  glReadPixels(0,Height-1,Width,1,GL_RGB,GL_UNSIGNED_BYTE, (GLvoid *) pixels);

  for ( y = Height-1; y >=0; y-- ) {
	glReadPixels(0,y,Width,1,GL_RGB,GL_UNSIGNED_BYTE, (GLvoid *) pixels);
	for (int n=0; n<3*Width; n++) {
		putc(pixels[n],fp);
	}
  }
  fclose(fp);
  delete [] pixels;
  return 0;
}

//---------------------------------------------------------------

int main(int argc, char **argv)
{
  // create window and rendering context
  glutInit( &argc, argv );
  glutInitDisplayMode( GLUT_DEPTH | GLUT_RGB | GLUT_DOUBLE );
  glutInitWindowSize( Width, Height );
  glutCreateWindow( "Rabbit" );
  
  // register display callback
  glutDisplayFunc( displayCallback );
  glutKeyboardFunc( keyboardCallback );
  
  glViewport( 0, 0, Width, Height );
  glEnable( GL_DEPTH_TEST );
  glEnable( GL_NORMALIZE );
  
  // Turn on blending (for floor).
  glEnable(GL_BLEND);
  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  
  
  // lighting stuff
  GLfloat ambient[] = {0.0, 0.0, 0.0, 1.0};
  GLfloat diffuse[] = {0.9, 0.9, 0.9, 1.0};
  GLfloat specular[] = {0.4, 0.4, 0.4, 1.0};
  GLfloat position0[] = {1.0, 1.0, 1.0, 0.0};
  glLightfv( GL_LIGHT0, GL_POSITION, position0 );
  glLightfv( GL_LIGHT0, GL_AMBIENT, ambient );
  glLightfv( GL_LIGHT0, GL_DIFFUSE, diffuse );
  glLightfv( GL_LIGHT0, GL_SPECULAR, specular );
  GLfloat position1[] = {-1.0, -1.0, -1.0, 0.0};
  glLightfv( GL_LIGHT1, GL_POSITION, position1 );
  glLightfv( GL_LIGHT1, GL_AMBIENT, ambient );
  glLightfv( GL_LIGHT1, GL_DIFFUSE, diffuse );
  glLightfv( GL_LIGHT1, GL_SPECULAR, specular );
  
  glEnable( GL_LIGHTING );
  glEnable( GL_LIGHT0 );
  glEnable( GL_LIGHT1 );
  glEnable( GL_COLOR_MATERIAL );
  timer(0);
  // pass control over to GLUT
  glutMainLoop();

  return 0;       // never reached
}

