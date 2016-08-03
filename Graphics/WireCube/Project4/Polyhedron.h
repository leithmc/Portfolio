// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

#pragma once
// Abstract class that defines a polyhedron
class Polyhedron
{
public:
	virtual void draw(int step) = 0;
	~Polyhedron() {};

protected:
	Polyhedron(Color color, vector<Transformation*> transformations);
	void colorDrawing() const;
	Color color;
	vector<Transformation*> transformations;
};

