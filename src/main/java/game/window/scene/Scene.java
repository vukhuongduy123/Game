package game.window.scene;

import game.window.view.Camera;

public abstract class Scene {
    protected Camera camera;
    public abstract void update(float dt);
    public abstract void inti();
}
