package game.window.component;

import game.window.ulti.Vertex;
import lombok.Getter;
import lombok.Setter;

public class SpriteRenderer extends Component {
    @Getter
    @Setter
    private Vertex.RGBA color;

    public SpriteRenderer(Vertex.RGBA color) {
        this.color = color;
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void start() {

    }
}
