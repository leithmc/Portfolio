// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Class definition for scaling class
class Scaling: public Transformation
{
public:
	Scaling(GLdouble x, GLdouble y, GLdouble z, GLint first, GLint last) 
		: Transformation(x, y, z, first, last) {};	// Uses base constructor
	void transform(Polyhedron* Polyhedron, int step);
};