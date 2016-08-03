// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Defines resizing of the client shape over the course of the supplied steps
#include "stdafx.h"

void Scaling::transform(Polyhedron * Polyhedron, int step)
{
	// The shape is scaled by its current dimensions to the power of the 
	// difference between the current step and the step at which the scaling 
	// starts. If the current step is greater than last, use last instead.
	if (step < first) return;
	GLint current = (step <= last) ? 1 + step - first : 1 + last - first;
	glScaled(pow(transX, current), pow(transY, current), pow(transZ, current));
}
