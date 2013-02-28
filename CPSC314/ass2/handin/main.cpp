#if defined(__APPLE_CC__)
#include<OpenGL/gl.h>
#include<OpenGL/glu.h>
#include<GLUT/glut.h>
#elif defined(WIN32)
#include<windows.h>
#include<GL/gl.h>
#include<GL/glu.h>
#include<GL/glut.h>
#else
#include<GL/gl.h>
#include<GL/glu.h>
#include<GL/glut.h>
#include<stdint.h>
#endif

#include<iostream>
#include<stdlib.h>
#include "Solar.h"
#include "Ship.h"
#define Rotation 5

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
/// Global State Variables ///////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
bool SpinOn = 0, SpinBreak = 1;
enum camera {absolute, relative, geosync};
int startTime, prevTime; 
GLenum polygonMode = GL_FILL;
Solar solar;
char ship = 's';
Ship sShip;
Ship mShip;


Ship* currentShip = &sShip;
camera cam = relative;

void shipCamera(void)
{
	glRotatef(currentShip->getXRot(),1.0,0.0,0.0);
	glRotatef(currentShip->getYRot(),0.0,1.0,0.0);
	glTranslated(-currentShip->getXPos(),currentShip->getYPos(),-currentShip->getZPos());
	glutPostRedisplay();
}
// time increment between calls to idle() in ms,
// currently set to 30 FPS
float dt = 1000.0f*1.0f/30.0f;

// flag to indicate that we should clean up and exit
bool quit = false;

// window handles for mother ship and scout ship
int mother_window, scout_window;

// display width and height
int disp_width=512, disp_height=512;


//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
/// Initialization/Setup and Teardown ////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

// set up opengl state, allocate objects, etc.  This gets called
// ONCE PER WINDOW, so don't allocate your objects twice!
void init(){
	/////////////////////////////////////////////////////////////
	/// TODO: Put your initialization code here! ////////////////
	/////////////////////////////////////////////////////////////
	
	glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );
	
	glViewport( 0, 0, glutGet(GLUT_WINDOW_WIDTH), glutGet(GLUT_WINDOW_HEIGHT) );
	glEnable( GL_DEPTH_TEST );
	glEnable( GL_NORMALIZE );

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
	//glEnable( GL_COLOR_MATERIAL );
	
}

// free any allocated objects and return
void cleanup(){
	/////////////////////////////////////////////////////////////
	/// TODO: Put your teardown code here! //////////////////////
	/////////////////////////////////////////////////////////////

}



//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
/// Callback Stubs ///////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

// window resize callback
void resize_callback( int width, int height ){    
	/////////////////////////////////////////////////////////////
	/// TODO: Put your resize code here! ////////////////////////
	/////////////////////////////////////////////////////////////
}

// keyboard callback
void keyboard_callback( unsigned char key, int x, int y ){
	float xrotrad,yrotrad;
	
	switch(key) {
	case 'l':
		cam=absolute;
		break;
	case 'r':
		cam=relative;
		break;
	case 'g':
		solar.geo=!solar.geo;
		//cam=geosync;
		break;
	}


//// ABSOLUTE
	if (cam==absolute) {
		switch( key ){
		case 'x':
			solar.eyex++;
			break;
		case 'X':
			solar.eyex--;
			break;
		case 'y':
			solar.eyey++;
			break;
		case 'Y':
			solar.eyey--;
			break;
		case 'z':
			solar.eyez++;
			break;
		case 'Z':
			solar.eyez--;
			break;
		case 'a':
			solar.centerx++;
			break;
		case 'A':
			solar.centerx--;
			break;
		case 'b':
			solar.centery++;
			break;
		case 'B':
			solar.centery--;
			break;
		case 'c':
			solar.centerz++;
			break;
		case 'C':
			solar.centerz--;
			break;
		case 'd':
			solar.upx++;
			break;
		case 'D':
			solar.upx--;
			break;
		case 'e':
			solar.upy++;
			break;
		case 'E':
			solar.upy--;
			break;
		case 'f':
			solar.upz++;
			break;
		case 'F':
			solar.upz--;
			break;	
		case 27:
			quit = true;
			break;
		default:
			break;
	}
	}

//// RELATIVE
	if (cam==relative) {
		switch( key ){
		case 'a':
			currentShip->setZRot(currentShip->getZRot() + Rotation);
			if(currentShip->getZRot() > 360)
				currentShip->setZRot(currentShip->getZRot() - 360);
			std::cout<<"zrot:"<<currentShip->getZRot()<<std::endl;
			break;
		case 'd':
			currentShip->setZRot(currentShip->getZRot() - Rotation);
			if(currentShip->getZRot() < -360)
				currentShip->setZRot(currentShip->getZRot() + 360);
			std::cout<<"zrot:"<<currentShip->getZRot()<<std::endl;
			break;
		case 'z':
			currentShip->setXRot(currentShip->getXRot() + Rotation);
			if(currentShip->getXRot() > 360) 
				currentShip->setXRot(currentShip->getXRot() - 360); 
			std::cout<<"xrot:"<<currentShip->getXRot()<<std::endl;
			break;
		case 'c':
			currentShip->setXRot(currentShip->getXRot() - Rotation);
			if(currentShip->getXRot() < -360) 
				currentShip->setXRot(currentShip->getXRot() + 360);
			std::cout<<"xrot:"<<currentShip->getXRot()<<std::endl;
			break;
		case 'q':
			currentShip->setYRot(currentShip->getYRot() - Rotation);
			if(currentShip->getYRot()<-360) 
				currentShip->setYRot(currentShip->getYRot() + 360);
			std::cout<<"yrot:"<<currentShip->getYRot()<<std::endl;
			break;
		case 'e':
			currentShip->setYRot(currentShip->getYRot() + Rotation);
			if(currentShip->getYRot()>360) currentShip->setYRot(currentShip->getYRot() - 360);
			std::cout<<"yrot:"<<currentShip->getYRot()<<std::endl;
			break;
		case 'w':
			yrotrad = (currentShip->getYRot()/180 * 3.141592654f);
			xrotrad = (currentShip->getXRot()/180 * 3.141592654f);
			currentShip->setXPos(currentShip->getXPos() + float(sin(yrotrad)));
			currentShip->setZPos(currentShip->getZPos() - float(cos(yrotrad)));
			currentShip->setYPos(currentShip->getYPos() + float(sin(xrotrad)));
			std::cout<<yrotrad<<":"<<xrotrad<<std::endl;
			break;
		case 's':
			yrotrad = (currentShip->getYRot()/180 * 3.141592654f);
			xrotrad = (currentShip->getXRot()/180 * 3.141592654f);
			currentShip->setXPos(currentShip->getXPos() - float(sin(yrotrad)));
			currentShip->setZPos(currentShip->getZPos() + float(cos(yrotrad)));
			currentShip->setYPos(currentShip->getYPos() - float(sin(xrotrad)));
			std::cout<<yrotrad<<":"<<xrotrad<<std::endl;
			break;
		case 'p':
			SpinOn =! SpinOn;
			std::cout<<"SpinOn:"<<SpinOn<<std::endl;
			break;
		case '=':
			solar.incVelocity();
			std::cout<<"Velocity:"<<solar.getVelocity()<<std::endl;
			break;
		case '-':
			solar.decVelocity();
			std::cout<<"Velocity:"<<solar.getVelocity()<<std::endl;
			break;
		case '>':
			ship = 'm';
			currentShip = &mShip;
			break;
		case '<':
			ship = 's';
			currentShip = &sShip;
			
			break;
		case 27:
			quit = true;
			break;
		default:
			break;
	}
	}
	
	


	/////////////////////////////////////////////////////////////
	/// TODO: Put your keyboard code here! //////////////////////
	/////////////////////////////////////////////////////////////

}

// display callback
void display_callback( void ){
	int current_window;

	// retrieve the currently active window
	current_window = glutGetWindow();

	// clear the color and depth buffers
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


	/////////////////////////////////////////////////////////////
	/// TODO: Put your rendering code here! /////////////////////
	/////////////////////////////////////////////////////////////
	
	switch(cam) {
	case absolute:
		
		break;
	case relative:
		if (current_window==1) mShip.camera();
		else if (current_window==2) sShip.camera();
		break;
	case geosync:

		break;
	}

	solar.draw();
	mShip.draw();
	sShip.draw();
	glPolygonMode(GL_FRONT_AND_BACK, polygonMode);

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective( 70.0f, float(glutGet(GLUT_WINDOW_WIDTH))/float(glutGet(GLUT_WINDOW_HEIGHT)), 0.1f, 2000.0f );

	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();


	// swap the front and back buffers to display the scene
	glutSetWindow( current_window );
	glutSwapBuffers();
}

// not exactly a callback, but sets a timer to call itself
// in an endless loop to update the program
void idle( int value ){

	// if the user wants to quit the program, then exit the
	// function without resetting the timer or triggering
	// a display update
	if( quit ){
		// cleanup any allocated memory
		cleanup();

		// perform hard exit of the program, since glutMainLoop()
		// will never return
		exit(0);
	}

	/////////////////////////////////////////////////////////////
	/// TODO: Put your idle code here! //////////////////////////
	/////////////////////////////////////////////////////////////
	int currTime = glutGet(GLUT_ELAPSED_TIME);
	int timeSincePrevFrame = currTime - prevTime;
	int elapsedTime = currTime - startTime;

	if(SpinOn == 1) {
		solar.spin();
	}

	glutPostRedisplay();
	prevTime = currTime;

	// set the currently active window to the mothership and
	// request a redisplay
	glutSetWindow( mother_window );
	glutPostRedisplay();

	// now set the currently active window to the scout ship
	// and redisplay it as well
	glutSetWindow( scout_window );
	glutPostRedisplay();

	// set a timer to call this function again after the
	// required number of milliseconds
	glutTimerFunc( dt, idle, 0 );
}
void WireFrameMode() {
	if (polygonMode == GL_FILL ) {
		polygonMode = GL_LINE;
	}
	else {
		polygonMode = GL_FILL;
	}
}

void mouseMenu(int value)
{
	if(value == 1)
		WireFrameMode();

	if(value == 2)
		exit(0);
}

void menu(void)
{
	printf("--------OPTIONS--------\n");
	printf("Press 1 to start/pause\n");
	printf("Press 2 to increase speed\n");
	printf("Press 3 to decrease speed\n");
	printf("Right click for more\n\n");
	printf("-----VIEW CONTROLS-----\n");
	printf("W A S D to change angle\n");
	printf("+ and - to dictate zoom\n\n");
	printf("-----------------------\n");
	printf("Press Esc to Exit\n");
	printf("-----------------------\n");
}
void initMenu() {
	glutCreateMenu(mouseMenu);
	glutAddMenuEntry("Wireframe", 1);
	glutAddMenuEntry("Exit", 2);
	glutAttachMenu(GLUT_RIGHT_BUTTON);
}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
/// Program Entry Point //////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
int main( int argc, char **argv ){
	// initialize glut
	glutInit( &argc, argv );
	sShip.toggleScout();
	// use double-buffered RGB+Alpha framebuffers with a depth buffer.
	glutInitDisplayMode( GLUT_RGBA | GLUT_DEPTH | GLUT_DOUBLE );

	// initialize the mothership window
	glutInitWindowSize( disp_width, disp_height );
	glutInitWindowPosition( 0, 100 );
	mother_window = glutCreateWindow( "Mother Ship" );
	glutKeyboardFunc( keyboard_callback );
	glutDisplayFunc( display_callback );
	glutReshapeFunc( resize_callback );
	initMenu();

	// initialize the scout ship window
	glutInitWindowSize( disp_width, disp_height );
	glutInitWindowPosition( disp_width+50, 100 );
	scout_window = glutCreateWindow( "Scout Ship" );
	glutKeyboardFunc( keyboard_callback );
	glutDisplayFunc( display_callback );
	glutReshapeFunc( resize_callback );
	initMenu();

	glutSetWindow( mother_window );
	init();
	glutSetWindow( scout_window );
	init();

	// start the idle on a fixed timer callback
	idle( 0 );

	// start the blug main loop
	glutMainLoop();

	return 0;
}

