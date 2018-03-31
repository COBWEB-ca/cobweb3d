package cobweb3d.rendering.jogl.gl3;

import cobweb3d.BuildConfig;
import cobweb3d.SimulationRunnerBase;
import cobweb3d.impl.Simulation;
import cobweb3d.rendering.SimulationRenderer;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import org.joml.Vector3f;

import java.awt.*;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

public class GLSimulationRenderer implements SimulationRenderer, GLEventListener {

    private Simulation simulation;
    private FPSAnimator fpsAnimator;
    private GLCanvas glCanvas;
    private GLU glu;  // for the GL Utility
    static boolean once = false;

    private float fovY = 45.0f;
    private float fovX = 45.0f;
    private float fovYr = 45.0f;
    private float fovXr = 45.0f;

    GridRenderer mGridRenderer;

    public GLSimulationRenderer(SimulationRunnerBase simulationRunner) {
        initializeOpenGL();
        bindSimulation(simulationRunner.getSimulation());
        simulationRunner.addUIComponent(this);
    }

    @Override
    public void refreshSimulation() {
        // TODO: Stub.
    }

    public void bindSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.mGridRenderer = new GridRenderer(simulation.environment);
    }

    @Override
    public Component getBackbuffer() {
        return glCanvas;
    }

    private static GL tryDebugGL(GL gl) {
        if (BuildConfig.DEBUG) {
            try {
                // Debug ..
                gl = gl.getContext().setGL(GLPipelineFactory.create("com.jogamp.opengl.Debug", null, gl, null));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                // Trace ..
                //  gl = gl.getContext().setGL( GLPipelineFactory.create("com.jogamp.opengl.Trace", null, gl, new Object[] { System.err } ) );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return gl;
    }

    @Override
    public boolean isReadyToUpdate() {
        return glCanvas != null && fpsAnimator != null;
    }

    @Override
    public void onStopped() {
        fpsAnimator.stop();
    }

    @Override
    public void update(boolean synchronous) {
        // if (fpsAnimator.isPaused()) fpsAnimator.start();
    }

    @Override
    public void onStarted() {
        //    fpsAnimator.start();
    }

    private void initializeOpenGL() {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glProfile);
        glCanvas = new GLCanvas(glcapabilities);
        glCanvas.addGLEventListener(this);

        fpsAnimator = new FPSAnimator(glCanvas, 61);
        //   fpsAnimator.start();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        fpsAnimator.stop();
    }

    int w = 100;
    int h = 100;

    public Camera camera;

    @Override
    public void init(GLAutoDrawable drawable) {
        glu = new GLU();
        GL2 gl;
        if (!once) {
            once = true;
            gl = tryDebugGL(drawable.getGL()).getGL2();
        } else {
            gl = drawable.getGL().getGL2();
        }
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
    }

    /**
     * Called back by the animator to perform rendering.
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glClear(GL_COLOR_BUFFER_BIT);

        GL2 gl = drawable.getGL().getGL2();  // Get the OpenGL 2 graphics context
        gl.glClearColor(0.93f, 0.93f, 0.93f, 1f);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear color and depth buffers
        gl.glLoadIdentity();  // Reset the impl-view matrix

        Vector3f gridCenter = new Vector3f(((float) simulation.environment.environmentParams.width) / 2f, ((float) simulation.environment.environmentParams.height) / 2f,
                ((float) simulation.environment.environmentParams.depth) / 2f);
        float gridBoundingRadius = new Vector3f(simulation.environment.environmentParams.width, simulation.environment.environmentParams.height,
                simulation.environment.environmentParams.depth).length() / 2;
        float optimalDistanceY = ((float) simulation.environment.environmentParams.height / 2) / (float) Math.tan(fovYr / 2);
        float optimalDistanceX = ((float) simulation.environment.environmentParams.width / 2) / (float) Math.tan(fovXr / 2);

        //      camera.target = new Vector3f(gridCenter.x, gridCenter.y, gridCenter.z);
        //    camera.distanceFromTarget = Math.max(optimalDistanceY, optimalDistanceX);
//        camera.position =

        glu.gluLookAt(gridCenter.x, gridCenter.y, -Math.max(optimalDistanceY, optimalDistanceX),
                gridCenter.x, gridCenter.y, gridCenter.z,
                0, 1, 0);

        // ----- Your OpenGL rendering code here (Render a white triangle for testing) -----
        mGridRenderer.draw(simulation.environment, gl);
    }

    /**
     * Call-back handler for window re-size event. Also called when the drawable is
     * first set to visible.
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        /*GL2 gl2 = drawable.getGL().getGL2();
        gl2.glMatrixMode(GL_PROJECTION);
        gl2.glLoadIdentity();

        // coordinate system origin at lower left with width and height same as the window
        GLU glu = new GLU();
        glu.gluOrtho2D(0.0f, width, 0.0f, height);

        gl2.glMatrixMode(GL_MODELVIEW);
        gl2.glLoadIdentity();

        gl2.glViewport(0, 0, width, height); */

        if (height == 0) height = 1;   // prevent divide by zero
        float aspect = ((float) width) / ((float) height);


        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        // Set the view port (display area) to cover the entire window.
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // Choose projection matrix.
        gl.glLoadIdentity(); // Reset projection matrix.
        glu.gluPerspective(fovY, aspect, 0.1, 1000.0); // FoVy, aspect, zNear, zFar
        fovYr = (float) Math.toRadians(fovY);
        fovXr = 2 * (float) Math.atan(Math.tan(fovYr / 2) * aspect);//fovYr * aspect; //2 * (float) Math.atan(Math.tan(fovYr / 2) * aspect);
        fovX = (float) Math.toDegrees(fovXr);
        //  fovX = (float) (float) Math.toDegrees(Math.atan(Math.tan(Math.toRadians(fovY) ) * aspect));//fovY * aspect;//(float) Math.toDegrees(Math.atan(Math.tan(fovY) * aspect));

        // Enable the impl-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }
}
