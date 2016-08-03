// CMSC 405 Computer Graphics
// Project 4
// Leith McCombs
// May 3, 2016

// Header file to be included by all source files

#define _USE_MATH_DEFINES

// Contains all header files in the correct order

#include <cmath>
#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>
#include <map>

using namespace std;

#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <gl/glut.h>
#endif

#include "types.h"

class Transformation;
#include "syntax.h"
#include "polyhedron.h"
#include "Cube.h"
#include "Dodecahedron.h"
#include "Tetrahedron.h"
#include "transformation.h"
#include "rotation.h"
#include "scaling.h"
#include "translation.h"
#include "scene.h"
#include "token.h"
#include "lexer.h"
#include "parser.h"
#include "Animator.h"
