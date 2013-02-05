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

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
/// Global State Variables ///////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////

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
    glEnable( GL_COLOR_MATERIAL );
    
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
    switch( key ){
        case 27:
            quit = true;
            break;
        default:
            break;
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
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluPerspective( 70.0f, float(glutGet(GLUT_WINDOW_WIDTH))/float(glutGet(GLUT_WINDOW_HEIGHT)), 0.1f, 2000.0f );
    
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glTranslatef( 0.0f, 0.0f, -5.0f );

    glBegin(GL_LINES);
    glColor3f( 1.0f, 0.0f, 0.0f );
    glVertex3f( 1.0f, 0.0f, 0.0f );
    glVertex3f( 0.0f, 0.0f, 0.0f );
    glColor3f( 0.0f, 1.0f, 0.0f );
    glVertex3f( 0.0f, 1.0f, 0.0f );
    glVertex3f( 0.0f, 0.0f, 0.0f );
    glColor3f( 0.0f, 0.0f, 1.0f );
    glVertex3f( 0.0f, 0.0f, 1.0f );
    glVertex3f( 0.0f, 0.0f, 0.0f );
    glEnd();

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



//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
/// Program Entry Point //////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
int main( int argc, char **argv ){
    // initialize glut
    glutInit( &argc, argv );
    
    // use double-buffered RGB+Alpha framebuffers with a depth buffer.
    glutInitDisplayMode( GLUT_RGBA | GLUT_DEPTH | GLUT_DOUBLE );
    
    // initialize the mothership window
    glutInitWindowSize( disp_width, disp_height );
    glutInitWindowPosition( 0, 100 );
    mother_window = glutCreateWindow( "Mother Ship" );
    glutKeyboardFunc( keyboard_callback );
    glutDisplayFunc( display_callback );
    glutReshapeFunc( resize_callback );
    
    // initialize the scout ship window
    glutInitWindowSize( disp_width, disp_height );
    glutInitWindowPosition( disp_width+50, 100 );
    scout_window = glutCreateWindow( "Scout Ship" );
    glutKeyboardFunc( keyboard_callback );
    glutDisplayFunc( display_callback );
    glutReshapeFunc( resize_callback );
    
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