package game.window.scene;

import game.window.component.GameObject;
import game.window.render.Renderer;
import game.window.view.Camera;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    @Getter
    @Setter
    protected Camera camera;

    protected Renderer renderer = new Renderer();
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public abstract void update(float dt);

    public abstract void inti();

    public void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);
        if (isRunning) {
            gameObject.start();
            this.renderer.add(gameObject);
        }
    }

    public void start() {
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            this.renderer.add(gameObject);

        }
        isRunning = true;
    }
}
