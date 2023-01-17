package game.ulti;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RGBA {
    private float red, green, blue, alpha;
    private boolean isFadeToBlack;
}
