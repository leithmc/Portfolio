// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Class definition for the scene which contains a collection of polyhedron objects
class Scene
{
public:
	Scene() = default;
	Scene(string name, GLint height, GLint width);
	void createWindow();
	void draw(int step);
	void addPolyhedron(Polyhedron* shape) {shapes.push_back(shape);}
private:
	string name;
	GLint height, width;
	vector<Polyhedron*> shapes;
};
