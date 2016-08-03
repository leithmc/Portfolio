// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

#include "stdafx.h"

void Cube::draw(int step)
{
	// Do the preliminary drawing steps
	Polyhedron::draw(step);

	//*** Draw a unit cube ***
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	glBegin(GL_QUADS);

	// Top face
	glVertex3d(-0.5, 0.5, -0.5);
	glVertex3d(0.5, 0.5, -0.5);
	glVertex3d(0.5, 0.5, 0.5);
	glVertex3d(-0.5, 0.5, 0.5);

	// Bottom face
	glVertex3d(-0.5, -0.5, -0.5);
	glVertex3d(0.5, -0.5, -0.5);
	glVertex3d(0.5, -0.5, 0.5);
	glVertex3d(-0.5, -0.5, 0.5);

	// Front face
	glVertex3d(-0.5, 0.5, 0.5);
	glVertex3d(0.5, 0.5, 0.5);
	glVertex3d(0.5, -0.5, 0.5);
	glVertex3d(-0.5,- 0.5, 0.5);

	//back face
	glVertex3d(-0.5, 0.5, -0.5);
	glVertex3d(0.5, 0.5, -0.5);
	glVertex3d(0.5, -0.5, -0.5);
	glVertex3d(-0.5, -0.5, -0.5);

	// Left face
	glVertex3d(-0.5, 0.5, 0.5);
	glVertex3d(-0.5, 0.5, -0.5);
	glVertex3d(-0.5, -0.5, -0.5);
	glVertex3d(-0.5, -0.5, 0.5);

	// Right face
	glVertex3d(0.5, 0.5, 0.5);
	glVertex3d(0.5, 0.5, -0.5);
	glVertex3d(0.5, -0.5, -0.5);
	glVertex3d(0.5, -0.5, 0.5);

	// Complete the drawing and clear the matrix
	glEnd();
	glPopMatrix();
}

Cube::~Cube()
{
}
