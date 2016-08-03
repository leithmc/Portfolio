// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Abstract class that defines a transformation
class Transformation
{
public:
	virtual void transform(Polyhedron* shape, int step) = 0;
protected:
	GLdouble transX, transY, transZ;
	GLint first, last;
	Transformation(GLdouble transX, GLdouble transY, GLdouble transZ, GLint first, GLint last);
};