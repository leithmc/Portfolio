// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Class that manages the redrawing of scene objects over time
class Animator
{
public:
	Animator(string SceneFile);
	Animator() = default;
	void createWindow();
	void draw();
	void nextStep();
	void keyboard(unsigned char key, int x, int y);
	bool inProgress;
	//	Scene scene; I still have no idea why I can't declare this variable here but have to declare it in the cpp
	~Animator();

protected:
	int counter;
};

