// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Abstract parent class to all polyhedral shapes.
// Provides base constructor, a utility function to color a shape, and a partial drawing function.
#include "stdafx.h"

void Polyhedron::draw(int step)
{
	// ** This is a partial function which should only be called by the 
	// draw(int) functions of its children before performing the drawing 
	// tasks specific to a given shape. The child function must then
	// call glPopMatrix to complete the process**

	// open the matrix
	glPushMatrix();

	//*****This doesn't work here...
	//glMatrixMode(GL_PROJECTION);
	//glLoadIdentity();
	//gluLookAt(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

	// iterate through the transformations in order, passing the current
	// step to each trnasformation
	for (auto transformation : transformations)
		transformation->transform(this, step);
	// Color the shape and set line width
	colorDrawing();
	glLineWidth(3);
	// Return to the specific draw(int) function of the calling shape
}

// Base constructor
Polyhedron::Polyhedron(Color color, vector<Transformation*> transformations)
{
	this->transformations = transformations;
	this->color = color;
}

void Polyhedron::colorDrawing() const
{
	glColor3d(color.red, color.green, color.blue);
}
