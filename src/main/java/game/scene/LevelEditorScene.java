package game.scene;

import game.GameWindow;
import game.key.KeyListener;
import game.ulti.RGBA;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean isChangingScene;
    private float timeToChangeScene = 2.0f;

    @Override
    public void update(float dt) {
        if (!isChangingScene && KeyListener.getInstance().getKeyPressed()[KeyEvent.VK_SPACE]) {
            isChangingScene = true;
        }

        if (isChangingScene && timeToChangeScene >= 0.f) {
            timeToChangeScene -= dt;
            RGBA color = GameWindow.getInstance().getColor();
            color.setRed(color.getRed() - dt * 5.0f);
            color.setBlue(color.getBlue() - dt * 5.0f);
            color.setGreen(color.getGreen() - dt * 5.0f);
            GameWindow.getInstance().setColor(color);
        } else if (isChangingScene) {
            GameWindow.getInstance().changeScene(1);
        }

    }
}
