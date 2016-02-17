package controllers;

import models.Apartment;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.index;
import views.html.upload;

import java.io.File;
import java.util.List;

/**
 * Created by ajla on 13-Jan-16.
 */
public class ImageUpload extends Controller {

    public Result uploadRender(Integer apartmentId) {
        return ok(upload.render(apartmentId));
    }

    public Result upload(Integer apartmentId) {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("picture");

        Apartment apartment = Apartment.getApartmentById(apartmentId);
        String folder = apartment.name + apartment.id;

        Logger.debug(folder);

        if (picture != null) {
            String fileName = picture.getFilename();
            File file = picture.getFile();

            File theDir = new File("E:\\StanNaDan\\public\\apartmentPhotos\\" + folder);

            // if the directory does not exist, create it

            boolean result = false;
            if (!theDir.exists()) {

                try {
                    theDir.mkdir();
                    result = true;
                }
                catch(SecurityException se){
                    //handle it
                }
            } else {
                result = true;
            }

            file.renameTo(new File(theDir, fileName));


            List<Apartment> apartments = Apartment.apartmentsForHomepage();
            Logger.debug("uspjesno");
            return ok(index.render(apartments));

        } else {
            flash("error", "Missing file");
            List<Apartment> apartments = Apartment.apartmentsForHomepage();
            Logger.debug("neuspjesno!!!!!!");
            return ok(index.render(apartments));
        }
    }
}
