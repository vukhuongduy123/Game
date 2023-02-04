package game.window.ulti;

import java.util.List;

public class Conversion {
    public static float[] convertToFloatArray(List<Vertex> vertices) {
        int numOfElements = 9;
        float[] floats = new float[vertices.size() * numOfElements];
        for (int i = 0; i < vertices.size(); i++) {
            floats[i * numOfElements] = vertices.get(i).getPosition().getX();;
            floats[i * numOfElements + 1] = vertices.get(i).getPosition().getY();
            floats[i * numOfElements + 2] = vertices.get(i).getPosition().getZ();
            floats[i * numOfElements + 3] = vertices.get(i).getRgba().getRed();
            floats[i * numOfElements + 4]  = vertices.get(i).getRgba().getGreen();
            floats[i * numOfElements + 5]  = vertices.get(i).getRgba().getBlue();
            floats[i * numOfElements + 6] = vertices.get(i).getRgba().getAlpha();
            floats[i * numOfElements + 7] = vertices.get(i).getUvCoordinate().getX();
            floats[i * numOfElements + 8] = vertices.get(i).getUvCoordinate().getY();
        }

        return floats;
    }
}
