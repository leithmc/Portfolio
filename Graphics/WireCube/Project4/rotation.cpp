// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Defines a rotation of the client shape over the course of the supplied steps
#include "stdafx.h"

// Constructor calls base constructor and defines rotation angle
Rotation::Rotation(GLdouble angle, GLdouble x, GLdouble y, GLdouble z, GLint first, GLint last)
	: Transformation(x, y, z, first, last)
{
	this->angle = angle;
}

void Rotation::transform(Polyhedron * Polyhedron, int step) 
{
	// The angle of rotation is rotated by the difference between the current
	// step and the step at which the rotation starts. If the current step is
	// greater than last, use last instead.
	if (step < first) return;
	GLint current = (step <= last) ? 1 + step - first : 1 + last - first;
	glRotated(angle * current, transX, transY, transZ);
}
