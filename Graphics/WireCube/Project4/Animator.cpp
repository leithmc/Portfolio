// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

#include "stdafx.h"

// A constructor that is passed the name of the scene file that parses the scene and 
// initially creates the window.
Scene scene;
Animator::Animator(string sceneFile)
{
	try
	{
		Parser parser(sceneFile);
		scene = parser.parseScene();
	}
	catch (SyntaxError error)
	{
		cout << error.what() << endl;
		system("pause");
	}
	inProgress = false;
	counter = 0;
}

// Passes the call to scene
void Animator::createWindow() {	scene.createWindow(); }

// Draws the scene. Because double buffering should now be used, 
// it should swap the buffers after performing the drawing. It should be called by the 
// draw callback function in project3.cpp.
void Animator::draw()
{
	glClear(GL_COLOR_BUFFER_BIT);	// Remove artifacts from previous frame
	glMatrixMode(GL_PROJECTION | GL_MODELVIEW);	// Set matrix mode
	glLoadIdentity();				// Load identity
	glOrtho(-2.0, 2.0, -2.0, 2.0, -2.0, 2.0);	// Create orthographic projection
	double d = sqrt(1 / 3.14);		
	gluLookAt(d / 2, d / 2, d, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);	// Set viewing angle (Tricky!)
	scene.draw(counter);			// Now that everything is set up, draw the current frame
	glFlush();						// Make the finished buffer available sooner for better performance (optional)
	glutSwapBuffers();				// Swap the buffers so one can process while the other draws
}

// Advances to the next step of the animation. If an 
// animation is active, it should advance to the animation step and redraw the scene. 
// It should be called by the timer callback function in project3.cpp.
void Animator::nextStep()
{
	draw();									// Draw a new frame
	glutPostRedisplay();					// Inform the system that a new frame has been drawn and is ready to paint to screen
	if (counter < INT32_MAX) counter++;		// Advance the counter (within reason)
}

// Controls starting, pausing, and resetting the animation.
// Called by the keyboard callback function in project4.cpp.
void Animator::keyboard(unsigned char key, int x, int y)	
{
	switch (key) {  // s=start, p=pause, r=reset
	case 's': // Starts the animation
		inProgress = true;
		break;
	case 'p': // Pauses the animation
		inProgress = false;
		break;
	case 'r': // Resets the animation
		counter = 0;
		inProgress = true;
		break;
	default:  // Other keys are irrelevant
		break;
	}
}



Animator::~Animator()
{
}
