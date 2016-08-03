// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Class definition for rotation class
class Rotation: public Transformation
{
public:
	Rotation(GLdouble angle, GLdouble x, GLdouble y, GLdouble z, GLint first, GLint last);
	void transform(Polyhedron* Polyhedron, int step) override;
private:
	GLdouble angle;
};