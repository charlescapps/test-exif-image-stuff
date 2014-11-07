package controllers;

import com.google.common.net.UrlEscapers;
import imaging.ExifImageUtils;
import javaxt.io.Image;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

                Image image = new Image(pictureFile);

                ExifImageUtils.rotateFromOrientationData(image);

                File folder = pictureFile.getParentFile();
                File renamedFile = new File(folder, fileName);

                image.saveAs(renamedFile);

               // final String absolutePath = pictureFile.getAbsolutePath();
               // final String escapedPath = UrlEscapers.urlPathSegmentEscaper().escape(absolutePath);

               // return ok(views.html.main.render(escapedPath));
                return ok(views.html.main.render(renamedFile.getAbsolutePath()));
            }

            return ok("asdf");
        } catch (Exception e) {
            logger.error("Error uploading", e);
            return internalServerError(e.getMessage());
        }
    }


    public static Result getImage(final String pathToFile) throws IOException {
        File file = new File(pathToFile);
        return ok(Files.readAllBytes(file.toPath())).as("image/jpg");
    }


}
