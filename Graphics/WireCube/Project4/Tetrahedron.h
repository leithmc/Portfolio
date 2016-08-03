// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

#pragma once
// Class that defines a tetrahedron
class Tetrahedron :
	public Polyhedron
{
public:
	Tetrahedron(Color color, vector<Transformation*> transformations) : Polyhedron(color, transformations) {}; // Uses base constructor
	void draw(int step);
	~Tetrahedron();
};

