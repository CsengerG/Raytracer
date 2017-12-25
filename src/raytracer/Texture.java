package raytracer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Texture {
    private int width;
    private int height;
    private BufferedImage texture;

    public Texture(String textureImage){
        File file = new File(textureImage);
        BufferedImage texture = null;

        try {
            texture = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        width = texture.getWidth();
        height = texture.getHeight();
    }

    public Vector3D getColorAt(double x, double y){
        x = x - Math.floor(x);
        y = y - Math.floor(y);

        System.out.println(x + " " + y);

        return null;
    }
}
