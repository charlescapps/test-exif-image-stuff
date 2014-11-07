package controllers;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class Application extends Controller {
    private static final Logger.ALogger logger = Logger.of(Application.class);

    /**
     */
    public static Result index() throws Exception {
       return ok(views.html.index.render("Welcome!"));
    }

    public static Result upload() {
        try {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart picture = body.getFile("picture");
            if (picture != null) {
                String fileName = picture.getFilename();
                logger.info("Uploading file name: {}", fileName);

                File pictureFile = picture.getFile();
                pictureFile.getTotalSpace();
                logger.info("Total space: {}", pictureFile);

                File folder = pictureFile.getParentFile();
                File newFile = new File(folder, fileName);
                pictureFile.renameTo(newFile);

                final IImageMetadata metadata = Imaging.getMetadata(newFile);


                JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

                List<? extends IImageMetadata.IImageMetadataItem> items = jpegMetadata.getExif().getItems();
                for (IImageMetadata.IImageMetadataItem item: items) {
                    logger.info(String.valueOf(item));

                    if (item instanceof TiffImageMetadata.Item && "Orientation".equals(((TiffImageMetadata.Item) item).getKeyword())) {
                        logger.info("Found Orientation tag: {}", item);
                    }
                }


               // TagInfo tagInfo = new TagInfo("Orientation", ExifTagConstants.)
              //  TiffField tiffField = jpegMetadata.findEXIFValueWithExactMatch(ExifTagConstants.EXIF_TAG_PREVIEW_IMAGE_START_IFD0);

              //  logger.info(String.valueOf(tiffField));

            }

            return ok("asdf");
        } catch (Exception e) {
            logger.error("Error uploading", e);
            return internalServerError(e.getMessage());
        }
    }


}
