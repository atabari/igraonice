package controllers;

import models.Apartment;
import models.Item;
import play.mvc.Controller;

import java.io.File;
import java.util.List;

import models.Image;
import play.Logger;
import play.mvc.*;
import views.html.imagesUpload;

/**
 * Created by Ajla on 5.5.2016.
 */
public class Images extends Controller {

    /* ------------------- images upload render ------------------ */
    public Result imagesUploadRender(Integer apartmentId){
        return ok(imagesUpload.render(apartmentId));
    }

    /* ------------------- images upload  ------------------ */
//        @BodyParser.Of(value = BodyParser.MultipartFormData.class, maxLength = 1000 * 1024)
    public Result imagesUpload(Integer apartmentId) {
        Apartment apartment = Apartment.getApartmentById(apartmentId);

        Http.MultipartFormData body1 = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> fileParts = body1.getFiles();
        if (fileParts != null) {
            for (Http.MultipartFormData.FilePart filePart1 : fileParts) {
                File file = filePart1.getFile();
                Logger.debug("File: " + file.getName());
                Image image = Image.create(file, apartment.id);
                Logger.debug("Image: " + image.image_url);
                apartment.images.add(image);
            }
        }

        apartment.update();
        return redirect(routes.Images.listOfPicturesRender(apartmentId));
    }

    /* ------------------- list of images render ------------------ */
    public Result listOfPicturesRender(Integer apartmentId) {
        List<Image> images = Image.findApartmentImages(apartmentId);
        return ok(views.html.Apartments.listOfImages.render(images, apartmentId));
    }

    /* ------------------- delete image ------------------ */
    public Result deleteImage(String image_public_id) {
        Image image = Image.findImageById(image_public_id);

        if (image.apartment.id != null) {
            return redirect(routes.Images.listOfPicturesRender(image.apartment.id));
        }
        if (image.item.id != null) {
            return redirect(routes.Images.listOfPicturesRender(image.item.id));
        }
        if (image.cake.id != null) {
            return redirect(routes.Images.listOfPicturesRender(image.cake.id));
        }
        else {
            return redirect(routes.Images.listOfPicturesRender(image.pastry.id));
        }
    }

        /* ------------------- add item image ------------------ */

    public Result imagesItemUpload(Integer itemId) {
        Item item = Item.findItemById(itemId);

        Http.MultipartFormData body1 = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> fileParts = body1.getFiles();
        if (fileParts != null) {
            for (Http.MultipartFormData.FilePart filePart1 : fileParts) {
                File file = filePart1.getFile();
                Image image = Image.createItemImage(file, item.id);
                item.images.add(image);
            }
        }

        item.update();
        return redirect(routes.Items.listOfItemImages(itemId));
    }
}
