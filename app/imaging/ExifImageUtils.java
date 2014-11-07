package imaging;

import javaxt.io.Image;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputField;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.util.IoUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;

public class ExifImageUtils {
    public static final int ORIENTATION = 0x0112;

    public static File rotateFromOrientationData(File input, File output) throws Exception {

        final IImageMetadata metadata = Imaging.getMetadata(input);

        if (!(metadata instanceof JpegImageMetadata)) {
            return input;
        }

        JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

        final TiffImageMetadata exif = jpegMetadata.getExif();

        if (exif == null) {
            return input;
        }

        // Later we'll modify this.
        TiffOutputSet outputSet = exif.getOutputSet();
        TiffOutputDirectory exifDirectory = outputSet.getOrCreateRootDirectory();

        TiffOutputField orientationField = exifDirectory.findField(ORIENTATION);


        if (orientationField == null) {
            return input;
        }

        // Rotate image
        Image image = new Image(input);
        image.rotate();
        image.saveAs(output);

        // Add exif tags back
        exifDirectory.removeField(ORIENTATION);

        // Write updated exif
        byte[] bytes = Files.readAllBytes(output.toPath());


        try (FileOutputStream fos = new FileOutputStream(output)) {
            new ExifRewriter().updateExifMetadataLossy(bytes, fos, outputSet);
        }

        return output;

    }
}
