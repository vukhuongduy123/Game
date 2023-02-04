package game.window.ulti;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Vertex {
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Position {
        private float x, y, z;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class UVCoordinate {
        private int x, y;
    }

    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class RGBA {
        private float red, green, blue, alpha;
    }

    private Position position;
    private RGBA rgba;

    private UVCoordinate uvCoordinate;
}
