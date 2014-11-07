package imaging;

import javaxt.io.Image;

import java.util.Map;

public class ExifImageUtils {
    public static final int ORIENTATION = 0x0112;

    public static Image rotateFromOrientationData(Image input) {
        Map<Integer, Object> exif = input.getExifTags();

        if (exif.containsKey(ORIENTATION) && exif.get(ORIENTATION) != null) {

            input.rotate(); // Rotate based on EXIF data.

            exif.remove(ORIENTATION);

        }

        return input;

    }
}
