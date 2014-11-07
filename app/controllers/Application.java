package controllers;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;

public class Application extends Controller {
    private static final Logger.ALogger logger = Logger.of(Application.class);

    /**
     */
    public static Result index() throws Exception {
       return ok(views.html.index.render("Welcome!"));
    }

    public static Result upload() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("picture");
        if (picture != null) {
            String fileName = picture.getFilename();
            logger.info("Uploading file name: {}", fileName);

            File pictureFile = picture.getFile();
            pictureFile.getTotalSpace();
            logger.info("Total space: {}", pictureFile);

            pictureFile.

        }

        return ok("asdf");
    }


}
