package game.window.scene;

import game.window.component.GameObject;
import game.window.component.SpriteRenderer;
import game.window.render.Texture;
import game.window.ulti.*;

import game.window.render.Shader;
import game.window.view.Camera;
import lombok.SneakyThrows;
import org.joml.Vector2f;

public class LevelEditorScene extends Scene {
    private Shader shader;
    private int vaoID;
    private Texture pacmanTexture;

    private GameObject gameObject;

/*    private final List<Vertex> vertices = new ArrayList<>() {{
        add(new Vertex(new Vertex.Position(new float[]{100f, 0f, 0.0f}),
                new Vertex.RGBA(1.0f, 0.0f, 0.0f, 1.0f),
                new Vertex.UVCoordinate(1, 1)));
        add(new Vertex(new Vertex.Position(new float[]{0f, 100f, 0.0f}),
                new Vertex.RGBA(0.0f, 1.0f, 0.0f, 1.0f),
                new Vertex.UVCoordinate(0, 0)));
        add(new Vertex(new Vertex.Position(new float[]{100f, 100f, 0.0f}),
                new Vertex.RGBA(1.0f, 0.0f, 1.0f, 1.0f),
                new Vertex.UVCoordinate(1, 0)));
        add(new Vertex(new Vertex.Position(new float[]{0f, 0f, 0.0f}),
                new Vertex.RGBA(1.0f, 1.0f, 0.0f, 1.0f),
                new Vertex.UVCoordinate(0, 1)));
    }};

    // IMPORTANT: Must be in counter-clockwise order
    private final int[] elements = {
            *//*
                    x        x
                    x        x
             *//*
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };*/

    @Override
    public void update(float dt) {
        System.out.println("FPS: " + (1.0f / dt));

/*        camera.getPosition().x -= dt * 50.0f;
        camera.getPosition().y -= dt * 20.0f;

        shader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        pacmanTexture.bind();

        shader.uploadMatrix("uProjection", camera.getProjectionMatrix());
        shader.uploadMatrix("uView", camera.getViewMatrix());
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
        shader.detach();*/

        for (GameObject go : gameObjects) {
            go.start();
        }
        this.renderer.render();
    }

    @Override
    @SneakyThrows
    public void inti() {
        this.camera = new Camera(new Vector2f(-250, 0));

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float) (600 - xOffset * 2);
        float totalHeight = (float) (300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;
        float padding = 3;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPos = xOffset + (x * sizeX) + (padding * x);
                float yPos = yOffset + (y * sizeY) + (padding * y);

                GameObject go = new GameObject("Obj" + x + "" + y,
                        new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vertex.RGBA(xPos / totalWidth,
                        yPos / totalHeight, 1, 1)));
                this.addGameObjectToScene(go);
            }
        }
    }
}
