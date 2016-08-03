// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Defines a transformation of a client shape over time
#include "stdafx.h"

// Base constructor
Transformation::Transformation(GLdouble x, GLdouble y, GLdouble z, GLint first, GLint last)
{
	// Define the x, y, and z values, named transX, transY, and transZ to avoid name conflicts 
	// with the coordinates of the shapes being operated on
	this->transX = x; this->transY = y; this->transZ = z;

	// Handle the default case (no steps specified) to run the transformation at step 1
	if (first == 0 && last == 0)
	{
		this->first = 0;
		this->last = 1;
	}
	// If steps are specified, set first and last accordingly
	else {
		this->first = first;
		this->last = last;
	}
}
