// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Creates the window where objects are displayed and draws them
#include "stdafx.h"

// Constructor recieves the height, width, and name of the drawing window for the scene
Scene::Scene(string name, GLint height, GLint width)
{
	this->name = name;
	this->height = height;
	this->width = width;
}

// Creates the drawing window for the scene at position 100, 100 with the width and heights supplied to the constructor
void Scene::createWindow()
{
	glutInitWindowPosition(100, 100);
	glutInitWindowSize(width, height);
	glutCreateWindow(name.c_str());
   	glClearColor(1.0, 1.0, 1.0, 1.0);
}

// Traverses all the shapes defined in the scene and calls the draw function to draw them
void Scene::draw(int step)
{
	for (auto shape : shapes)
		shape->draw(step);
}