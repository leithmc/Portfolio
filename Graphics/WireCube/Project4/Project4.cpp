// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Project3.cpp : Defines the entry point for the console application.

#include "stdafx.h"

// Global variable that controls the animation sequence
static Animator animator;

// Callback function that is called each time the window is drawn
void draw(void) { animator.draw(); }

// Callback function that starts the timer, starts the next step in the 
// animation, and then continues to call itself.
void timerFunction(GLint value)
{
	if (animator.inProgress) animator.nextStep();
	glutTimerFunc(10, timerFunction, 1);
}

// Callback function that is called each time the window is resized
void resizeWindow(GLint newWidth, GLint newHeight)
{
	// Match viewport to window size
	glViewport(0, 0, newWidth, newHeight); 
	// Set matrix mode
	glMatrixMode(GL_PROJECTION | GL_MODELVIEW); 
	// Load identitiy
	glLoadIdentity(); 
	// Clear artifacts from previous paint
	glClear(GL_COLOR_BUFFER_BIT);
}

// Callback function called when keys are pressed.
void kbd(unsigned char key, int x, int y) {	animator.keyboard(key, x, y); }

// The main function of the whole program, which requires the name of the scene definition file as a command line argument
// If no command line argument is supplied the user is prompted to enter the file name
// It passes the name of the secene file to the animator, which calls the parser to parse the scene definition file and add the graphic objects to the scene,
// and then it creates the drawing window and registers the callback functions for drawing the window and resizing it, starting the timer, and handling keyboard input.
int main(GLint argc, char** argv)
{
	string sceneFile;
	if (argc > 1)
		sceneFile = argv[1];
	else
	{
		cout << "Enter scene file name: ";
		cin >> sceneFile;
	}
	animator = Animator(sceneFile);

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);  // Puts the program in double buffer mode so it can animate
	animator.createWindow();                      // Creates the window
	glutReshapeFunc(resizeWindow);                // Assigns the callback for when the window is resized
	glutDisplayFunc(draw);                        // Assigns the callback for when the display is to be updated
	glutTimerFunc(100, timerFunction, 1);         // Assigns the timer function that runs the animation
	glutKeyboardFunc(kbd);                        // Assigns the keyboard listener
	glutMainLoop();                               // Starts a thread for all of the listeners and callbacks
	return 0;
}
