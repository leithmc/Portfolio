// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

#include "stdafx.h"

void Tetrahedron::draw(int step)
{
	// Do the preliminary drawing steps
	Polyhedron::draw(step);
	// Create a unit tetrahedron at the origin, modified by the transformations
	glutWireTetrahedron();
	// Clear the matrix
	glPopMatrix();
}

Tetrahedron::~Tetrahedron()
{
}
