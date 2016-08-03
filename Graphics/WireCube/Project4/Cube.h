// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

#pragma once
// Class that defines a cube
class Cube :
	public Polyhedron
{
public:
	Cube(Color color, vector<Transformation*> transformations) : Polyhedron(color, transformations) {}; // Uses base constructor
	void draw(int step);
	~Cube();
};

