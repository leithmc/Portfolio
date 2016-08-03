#include <GL/glut.h>
#include <stdio.h>


/* Global variables */
char title[] = "CMSC405 Week8_1_Modified";
// Window display size
GLsizei winWidth = 640, winHeight = 480;


/* Initialize OpenGL Graphics */
void init() {
	// controls the specular color 
	GLfloat mat_specular[] = { 0.9, 0.5, 0.5, 1.0 };
	// Larger values result in more shininess
	// Also more spreadding of the specular color
	GLfloat mat_shininess[] = { 50.0 };
	// x,y and z position of the light
	// Changing these parameters results in applying the light 
	// at different locations on the object
	GLfloat light_position[] = { 0.3, 0.3, 1.0, 0.0 };


	// Apply the specular color to the front.
	glMaterialfv(GL_FRONT, GL_SPECULAR, mat_specular);
	// Apply the shininess to the front.
	glMaterialfv(GL_FRONT, GL_SHININESS, mat_shininess);
	// Apply the first light (Light0) at the assigned position
	glLightfv(GL_LIGHT0, GL_POSITION, light_position);

	GLfloat light_pos2[] = { 0.7, 0.7, 0.2, 0.0 };
	glLightfv(GL_LIGHT1, GL_DIFFUSE, light_pos2);



	// Enable lighting and Depth perception

	glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT0);
	glEnable(GL_LIGHT1);
	glEnable(GL_DEPTH_TEST);
	// Set the type of depth-test
	glDepthFunc(GL_LEQUAL);
	// Provide smooth shading
	glShadeModel(GL_SMOOTH);

}

// Display Function
void displayfcn() {
	// Clear color and depth buffers
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	// Model view matrix mode
	glMatrixMode(GL_MODELVIEW);

	// Reset the model-view matrix
	// This is done each time you need to clear the matrix operations
	// This is similar to glPopMatrix()
	glLoadIdentity();

	// Add a cone
	float colorCone[]{ 0.5f, 0.3f, 0.0f, 1.0f };
	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, colorCone);
	glLoadIdentity();
	// Translate to center
	glTranslatef(0.0f, 0.75f, -7.0f);
	glRotated(110.0, 1.0, 0.0, 0.0);
	glutSolidCone(1.0, 1.8, 12, 18);

	// Put some ice cream in it
	float colorCream[]{ 1.0f, 1.0f, 0.9f, 1.0f };
	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, colorCream);
	glLoadIdentity();
	// Translate to center
	glTranslatef(0.0f, 1.5f, -7.0f);
	glutSolidSphere(1.0, 20, 16);

	// Double buffering
	glutSwapBuffers();
}

// Windows redraw function
void winReshapeFcn(GLsizei width, GLsizei height) {
	// Compute aspect ratio of the new window
	if (height == 0)
		height = 1;
	GLfloat aspect = (GLfloat)width / (GLfloat)height;

	// Set the viewport 
	// Allows for resizing without major display issues
	glViewport(0, 0, width, height);

	// Set the aspect ratio of the clipping volume to match the viewport
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	// Enable perspective projection with fovy, aspect, zNear and zFar
	// This is the camera view and objects align with view frustrum	
	gluPerspective(45.0f, aspect, 0.1f, 100.0f);
}

/* Main function: GLUT runs as a console application starting at main() */
void main(int argc, char** argv) {
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE);
	// Set the window's initial width & height
	glutInitWindowSize(winWidth, winHeight);
	// Position the window's initial top-left corner
	glutInitWindowPosition(50, 50);
	// Create window with the given title
	glutCreateWindow(title);
	// Display Function
	glutDisplayFunc(displayfcn);
	// Reshape function
	glutReshapeFunc(winReshapeFcn);
	// Initialize
	init();
	// Process Loop
	glutMainLoop();
}