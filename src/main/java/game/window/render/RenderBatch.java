package game.window.render;

import game.window.GameWindow;
import game.window.component.SpriteRenderer;
import game.window.ulti.Conversion;
import game.window.ulti.Vertex;
import lombok.Getter;

import java.util.List;

import static game.window.ulti.Vertex.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    // Vertex
    // ======
    // Pos               Color
    // float, float,     float, float, float, float

    private final SpriteRenderer[] sprites;
    private int numSprites;
    @Getter
    private boolean hasRoom;
    private final Vertex[] vertices;

    private int vaoID, vboID;
    private final int maxBatchSize;
    private final Shader shader;

    public RenderBatch(int maxBatchSize) {
        shader = new Shader("/assets/shaders/vertex.glsl", "/assets/shaders/fragment.glsl");
        shader.compile();
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        // 4 vertices quads
        vertices = new Vertex[maxBatchSize * 4];

        // inti first vertex to use default
        vertices[0] = new Vertex(new Position(new float[]{0, 0}), new RGBA(0, 0, 0, 0));

        this.numSprites = 0;
        this.hasRoom = true;
    }

    public void start() {
        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Vertex.getNumElements(vertices[0]) * Float.BYTES,
                GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_2_SIZE, GL_FLOAT, false, VERTEX_2_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_2_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void addSprite(SpriteRenderer spr) {
        // Get index and add renderObject
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        // Add properties to local vertices array
        loadVertexProperties(index);
        if (numSprites >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }

    public void render() {
        // For now, we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Conversion.convertToFloatArray(List.of(vertices)));

        // Use shader
        shader.use();
        shader.uploadMatrix("uProjection",
                GameWindow.getInstance().getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMatrix("uView", GameWindow.getInstance().getCurrentScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = this.sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4;

        Vertex.RGBA color = sprite.getColor();

        // Add vertices with the appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }

            vertices[offset] = new Vertex(new Position(new float[]{
                    sprite.gameObject.getTransform()
                            .getPosition().x + (xAdd * sprite.gameObject.getTransform().getScale().x),
                    sprite.gameObject.getTransform()
                            .getPosition().y + (yAdd * sprite.gameObject.getTransform().getScale().y)}),
                    new RGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));

            offset ++;
        }
    }

    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

}
