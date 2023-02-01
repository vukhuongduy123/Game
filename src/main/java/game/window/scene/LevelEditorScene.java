package game.window.scene;

import game.GameMain;
import game.window.render.Shader;
import game.window.ulti.Conversion;
import game.window.ulti.Pair;
import game.window.ulti.Position;
import game.window.ulti.RGBA;
import lombok.SneakyThrows;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {
    private Shader shader;
    private int vaoID;

    private final List<Pair<Position, RGBA>> vertexes = new ArrayList<>() {{
        add(new Pair<>(new Position(0.5f, -0.5f, 0.0f), new RGBA(1.0f, 0.0f, 0.0f, 1.0f)));
        add(new Pair<>(new Position(-0.5f, 0.5f, 0.0f), new RGBA(0.0f, 1.0f, 0.0f, 1.0f)));
        add(new Pair<>(new Position(0.5f, 0.5f, 0.0f), new RGBA(1.0f, 0.0f, 1.0f, 1.0f)));
        add(new Pair<>(new Position(-0.5f, -0.5f, 0.0f), new RGBA(1.0f, 1.0f, 0.0f, 1.0f)));
    }};

    // IMPORTANT: Must be in counter-clockwise order
    private final int[] elements = {
            /*
                    x        x
                    x        x
             */
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };

    @Override
    public void update(float dt) {
        shader.use();
        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elements.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        shader.detach();
    }

    @Override
    @SneakyThrows
    public void inti() {
        shader = new Shader("/assets/shaders/vertex.glsl", "/assets/shaders/fragment.glsl");
        shader.compile();
        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        int vertexArrayObjectID = glGenVertexArrays();
        glBindVertexArray(vertexArrayObjectID);
        vaoID = vertexArrayObjectID;
        System.out.println(vertexArrayObjectID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexes.size() * 7);
        vertexBuffer.put(Conversion.convertToFloatArray(vertexes)).flip();
        System.out.println(Arrays.toString(Conversion.convertToFloatArray(vertexes)));

        // Create VBO upload the vertex buffer
        int vertexBufferObjectID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObjectID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elements.length);
        elementBuffer.put(elements).flip();

        int elementBufferObjectID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferObjectID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = Float.BYTES;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes,
                              positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }
}
