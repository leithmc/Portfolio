// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Defines the movement of the client shape through world space over the course of the supplied steps
#include "stdafx.h"

void Translation::transform(Polyhedron * Polyhedron, int step)
{
	// The shape is translated in each direction by the difference between the current
	// step and the step at which the translation starts. If the current step is
	// greater than last, use last instead.
	if (step < first) return;
	GLint current = (step <= last) ? 1 + step - first : 1 + last - first;
	glTranslated(transX * current, transY * current, transZ * current);
}
