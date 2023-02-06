package game.window.ulti;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joml.Vector2f;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transform {
    private Vector2f position;
    private Vector2f scale;
}
