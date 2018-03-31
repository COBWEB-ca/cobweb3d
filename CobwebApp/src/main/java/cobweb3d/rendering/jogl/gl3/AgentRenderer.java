package cobweb3d.rendering.jogl.gl3;

import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;

public class AgentRenderer {
    private static final float AGENT_SIZE = 1;

  /*  private void CreateCubeFace(int x, int y, int z, FaceDirection direction, Vector3i color) {

        Vector3f topLeftFront = new Vector3f(x, y + AGENT_SIZE, z);
        Vector3f bottomLeftFront = new Vector3f(x, y, z);

        Vector3f topRightFront = new Vector3f(x + AGENT_SIZE, y + AGENT_SIZE, z);
        Vector3f bottomRightFront = new Vector3f(x + AGENT_SIZE, y, z);

        Vector3f topLeftBack = new Vector3f(x, y + AGENT_SIZE, z + AGENT_SIZE);
        Vector3f bottomLeftBack = new Vector3f(x, y, z + AGENT_SIZE);

        Vector3f topRightBack = new Vector3f(x + AGENT_SIZE, y + AGENT_SIZE, z + AGENT_SIZE);
        Vector3f bottomRightBack = new Vector3f(x + AGENT_SIZE, y, z + AGENT_SIZE);

        switch (direction) {
            case NegativeX:
                vertices.Add(new ChunkVertex(topLeftBack, new Vector2(texCoordsSides.Z, texCoordsSides.Y), (shade)));
                vertices.Add(new ChunkVertex(bottomLeftBack, new Vector2(texCoordsSides.Z, texCoordsSides.W), (shade)));
                vertices.Add(new ChunkVertex(topLeftFront, new Vector2(texCoordsSides.X, texCoordsSides.Y), (shade)));
                vertices.Add(new ChunkVertex(bottomLeftFront, new Vector2(texCoordsSides.X, texCoordsSides.W), (shade)));
                break;

            case PositiveX:
                vertices.Add(new ChunkVertex(topRightFront, new Vector2(texCoordsSides.Z, texCoordsSides.Y), (shade)));
                vertices.Add(new ChunkVertex(bottomRightFront, new Vector2(texCoordsSides.Z, texCoordsSides.W), (shade)));
                vertices.Add(new ChunkVertex(topRightBack, new Vector2(texCoordsSides.X, texCoordsSides.Y), (shade)));
                vertices.Add(new ChunkVertex(bottomRightBack, new Vector2(texCoordsSides.X, texCoordsSides.W), (shade)));
                break;

            case NegativeY:
                vertices.Add(new ChunkVertex(bottomLeftFront, new Vector2(texCoordsBottom.Z, texCoordsBottom.Y), (shade)));
                vertices.Add(new ChunkVertex(bottomLeftBack, new Vector2(texCoordsBottom.Z, texCoordsBottom.W), (shade)));
                vertices.Add(new ChunkVertex(bottomRightFront, new Vector2(texCoordsBottom.X, texCoordsBottom.Y), (shade)));
                vertices.Add(new ChunkVertex(bottomRightBack, new Vector2(texCoordsBottom.X, texCoordsBottom.W), (shade)));
                break;

            case PositiveY:
                vertices.Add(new ChunkVertex(topLeftBack, new Vector2(texCoordsTop.Z, texCoordsTop.Y), (shade)));
                vertices.Add(new ChunkVertex(topLeftFront, new Vector2(texCoordsTop.Z, texCoordsTop.W), (shade)));
                vertices.Add(new ChunkVertex(topRightBack, new Vector2(texCoordsTop.X, texCoordsTop.Y), (shade)));
                vertices.Add(new ChunkVertex(topRightFront, new Vector2(texCoordsTop.X, texCoordsTop.W), (shade)));
                break;

            case NegativeZ:
                vertices.Add(new ChunkVertex(topLeftFront, new Vector2(texCoordsSides.Z, texCoordsSides.Y), (shade)));
                vertices.Add(new ChunkVertex(bottomLeftFront, new Vector2(texCoordsSides.Z, texCoordsSides.W), (shade)));
                vertices.Add(new ChunkVertex(topRightFront, new Vector2(texCoordsSides.X, texCoordsSides.Y), (shade)));
                vertices.Add(new ChunkVertex(bottomRightFront, new Vector2(texCoordsSides.X, texCoordsSides.W), (shade)));
                break;

            case PositiveZ:
                vertices.Add(new ChunkVertex(topRightBack, new Vector2(texCoordsSides.Z, texCoordsSides.Y), (shade)));
                vertices.Add(new ChunkVertex(bottomRightBack, new Vector2(texCoordsSides.Z, texCoordsSides.W), (shade)));
                vertices.Add(new ChunkVertex(topLeftBack, new Vector2(texCoordsSides.X, texCoordsSides.Y), (shade)));
                vertices.Add(new ChunkVertex(bottomLeftBack, new Vector2(texCoordsSides.X, texCoordsSides.W), (shade)));
                break;
        }

                indices.add((short)(vertices.size() - 4));
        indices.add((short)(vertices.size() - 3));
        indices.add((short)(vertices.size() - 2));

        indices.add((short)(vertices.size() - 3));
        indices.add((short)(vertices.size() - 1));
        indices.add((short)(vertices.size() - 2));
    } */

    // TODO
    private void CreateCube(int x, int y, int z, Vector3i color) {

        Vector3f topLeftFront = new Vector3f(x, y + AGENT_SIZE, z);
        Vector3f bottomLeftFront = new Vector3f(x, y, z);

        Vector3f topRightFront = new Vector3f(x + AGENT_SIZE, y + AGENT_SIZE, z);
        Vector3f bottomRightFront = new Vector3f(x + AGENT_SIZE, y, z);

        Vector3f topLeftBack = new Vector3f(x, y + AGENT_SIZE, z + AGENT_SIZE);
        Vector3f bottomLeftBack = new Vector3f(x, y, z + AGENT_SIZE);

        Vector3f topRightBack = new Vector3f(x + AGENT_SIZE, y + AGENT_SIZE, z + AGENT_SIZE);
        Vector3f bottomRightBack = new Vector3f(x + AGENT_SIZE, y, z + AGENT_SIZE);

        List<AgentVertex> vertices = new ArrayList<>();
        List<Short> indices = new ArrayList<>();

        vertices.add(new AgentVertex(topLeftBack, color));
        vertices.add(new AgentVertex(bottomLeftBack, color));
        vertices.add(new AgentVertex(topLeftFront, color));
        vertices.add(new AgentVertex(bottomLeftFront, color));

        indices.add((short) (vertices.size() - 4));
        indices.add((short) (vertices.size() - 3));
        indices.add((short) (vertices.size() - 2));

        indices.add((short) (vertices.size() - 3));
        indices.add((short) (vertices.size() - 1));
        indices.add((short) (vertices.size() - 2));

        vertices.add(new AgentVertex(topRightFront, color));
        vertices.add(new AgentVertex(bottomRightFront, color));
        vertices.add(new AgentVertex(topRightBack, color));
        vertices.add(new AgentVertex(bottomRightBack, color));

        indices.add((short) (vertices.size() - 4));
        indices.add((short) (vertices.size() - 3));
        indices.add((short) (vertices.size() - 2));

        indices.add((short) (vertices.size() - 3));
        indices.add((short) (vertices.size() - 1));
        indices.add((short) (vertices.size() - 2));

        vertices.add(new AgentVertex(bottomLeftFront, color));
        vertices.add(new AgentVertex(bottomLeftBack, color));
        vertices.add(new AgentVertex(bottomRightFront, color));
        vertices.add(new AgentVertex(bottomRightBack, color));

        indices.add((short) (vertices.size() - 4));
        indices.add((short) (vertices.size() - 3));
        indices.add((short) (vertices.size() - 2));

        indices.add((short) (vertices.size() - 3));
        indices.add((short) (vertices.size() - 1));
        indices.add((short) (vertices.size() - 2));

        vertices.add(new AgentVertex(topLeftBack, color));
        vertices.add(new AgentVertex(topLeftFront, color));
        vertices.add(new AgentVertex(topRightBack, color));
        vertices.add(new AgentVertex(topRightFront, color));

        indices.add((short) (vertices.size() - 4));
        indices.add((short) (vertices.size() - 3));
        indices.add((short) (vertices.size() - 2));

        indices.add((short) (vertices.size() - 3));
        indices.add((short) (vertices.size() - 1));
        indices.add((short) (vertices.size() - 2));

        vertices.add(new AgentVertex(topLeftFront, color));
        vertices.add(new AgentVertex(bottomLeftFront, color));
        vertices.add(new AgentVertex(topRightFront, color));
        vertices.add(new AgentVertex(bottomRightFront, color));

        indices.add((short) (vertices.size() - 4));
        indices.add((short) (vertices.size() - 3));
        indices.add((short) (vertices.size() - 2));

        indices.add((short) (vertices.size() - 3));
        indices.add((short) (vertices.size() - 1));
        indices.add((short) (vertices.size() - 2));

        vertices.add(new AgentVertex(topRightBack, color));
        vertices.add(new AgentVertex(bottomRightBack, color));
        vertices.add(new AgentVertex(topLeftBack, color));
        vertices.add(new AgentVertex(bottomLeftBack, color));

        indices.add((short) (vertices.size() - 4));
        indices.add((short) (vertices.size() - 3));
        indices.add((short) (vertices.size() - 2));

        indices.add((short) (vertices.size() - 3));
        indices.add((short) (vertices.size() - 1));
        indices.add((short) (vertices.size() - 2));
    }

    private class AgentVertex {
        public Vector3f location;
        public Vector3i color;

        public AgentVertex(Vector3f location, Vector3i color) {
            this.location = location;
            this.color = color;
        }
    }

    public enum FaceDirection {
        NegativeX,
        PositiveX,
        NegativeY,
        PositiveY,
        NegativeZ,
        PositiveZ
    }
}
