package cobweb3d.rendering.jogl.gl3;

import cobweb3d.core.environment.BaseEnvironment;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import org.joml.Vector3i;

import java.util.ArrayList;


public class GridRenderer {
    public boolean drawInnerGrid = false;
    private int gridWidth = 0;
    private int gridHeight = 0;
    private int gridDepth = 0;
    private Vector3i[] mOuterLineVertexPositions;
    private Vector3i[] mInnerLineVertexPositions;

    public GridRenderer(BaseEnvironment environment) {
        generateGeometry(environment);
    }

    private void generateGeometry(BaseEnvironment environment) {
        gridWidth = environment.environmentParams.width;
        gridHeight = environment.environmentParams.height;
        gridDepth = environment.environmentParams.depth;
        /* mOuterLineVertexPositions = new Vector3Int[
                    (width * depth * 2) + // Y-axis line vertices
                    (height * depth * 2) + // X-axis line vertices
                    (width * height * 2) // Z-axis line vertices
                ]; */
        ArrayList<Vector3i> mInnerVertexList = new ArrayList<>(
                (gridWidth * gridDepth * 2) + // Y-axis line vertices
                        (gridHeight * gridDepth * 2) + // X-axis line vertices
                        (gridWidth * gridHeight * 2) // Z-axis line vertices
        );
        ArrayList<Vector3i> mOuterVertexList = new ArrayList<>();


        for (int x = 0; x < gridWidth + 1; x++) {
            for (int z = 0; z < gridDepth + 1; z++) {
                if (x == 0 || x == gridWidth || z == 0 || z == gridDepth) {
                    mOuterVertexList.add(new Vector3i(x, 0, z));
                    mOuterVertexList.add(new Vector3i(x, gridHeight, z));
                } else {
                    mInnerVertexList.add(new Vector3i(x, 0, z));
                    mInnerVertexList.add(new Vector3i(x, gridHeight, z));
                }
            }
        }
        for (int y = 0; y < gridHeight + 1; y++) {
            for (int z = 0; z < gridDepth + 1; z++) {
                if (y == 0 || y == gridHeight || z == 0 || z == gridDepth) {
                    mOuterVertexList.add(new Vector3i(0, y, z));
                    mOuterVertexList.add(new Vector3i(gridWidth, y, z));
                } else {
                    mInnerVertexList.add(new Vector3i(0, y, z));
                    mInnerVertexList.add(new Vector3i(gridWidth, y, z));
                }
            }
        }

        for (int x = 0; x < gridWidth + 1; x++) {
            for (int y = 0; y < gridHeight + 1; y++) {
                if (y == 0 || y == gridHeight || x == 0 || x == gridWidth) {
                    mOuterVertexList.add(new Vector3i(x, y, 0));
                    mOuterVertexList.add(new Vector3i(x, y, gridDepth));
                } else {
                    mInnerVertexList.add(new Vector3i(x, y, 0));
                    mInnerVertexList.add(new Vector3i(x, y, gridDepth));
                }
            }
        }

        mOuterVertexList.toArray(mOuterLineVertexPositions = new Vector3i[mOuterVertexList.size()]);
        mInnerVertexList.toArray(mInnerLineVertexPositions = new Vector3i[mInnerVertexList.size()]);
    }

    public void draw(BaseEnvironment environment, GL2 gl) {
        if (environment.environmentParams.width != gridWidth
                || environment.environmentParams.height != gridHeight
                || environment.environmentParams.depth != gridDepth)
            generateGeometry(environment);

        gl.glBegin(GL.GL_LINES);
        gl.glColor3b((byte) 101, (byte) 101, (byte) 101);
        for (Vector3i position : mOuterLineVertexPositions) {
            gl.glVertex3f(position.x, position.y, position.z);
        }
        if (drawInnerGrid) {
            for (Vector3i position : mInnerLineVertexPositions) {
                gl.glVertex3f(position.x, position.y, position.z);
            }
        }
        /* for (int x = 0; x < environment.width + 1; x++) {
            for (int z = 0; z < environment.depth + 1; z++) {
                gl.glVertex3f(x,0, z);
                gl.glVertex3f(x, environment.height + 1, z);
            }

        }
        for (int y = 0; y < environment.height + 1; y++) {
            for (int z = 0; z < environment.depth + 1; z++) {
                gl.glVertex3f(0, y, z);
                gl.glVertex3f(environment.width + 1, y, z);
            }

        }

        for (int x = 0; x < environment.width + 1; x++) {
            for (int y = 0; y < environment.height + 1; y++) {
                gl.glVertex3f(x, y, 0);
                gl.glVertex3f(x, y, environment.depth + 1);
            }

        } */
        gl.glEnd();
    }

    /*public GridRenderer(Simulation simulation, GL2 gl) {
        gl.glBegin(GL.GL_LINES);
     //   float[] verticies = new float[1];
        gl.glColor3b((byte)101, (byte)101, (byte)101);


        for (int x = 0; x < simulation.environment.width + 1; x++) {
            for (int z = 0; z < simulation.environment.depth + 1; z++) {
                gl.glVertex3f(x,0, z);
                gl.glVertex3f(x, simulation.environment.height + 1, z);
            }

        }
        for (int y = 0; y < simulation.environment.height + 1; y++) {
            for (int z = 0; z < simulation.environment.depth + 1; z++) {
                gl.glVertex3f(0, y, z);
                gl.glVertex3f(simulation.environment.width + 1, y, z);
            }

        }

        for (int x = 0; x < simulation.environment.width + 1; x++) {
            for (int y = 0; y < simulation.environment.height + 1; y++) {
                gl.glVertex3f(x, y, 0);
                gl.glVertex3f(x, y, simulation.environment.depth + 1);
            }

        }
       gl.glEnd();
    }*/
}
