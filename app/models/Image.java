package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import play.Logger;

/**
 * Created by Ajla on 5.5.2016.
 */
@Entity
public class Image extends Model {

    @Id
    public Integer id;
    public String public_id;
    public String image_url;
    public String secret_image_url;

    @ManyToOne
    @JsonBackReference
    public Apartment apartment;

    public static Cloudinary cloudinary;


    public static Finder<Integer, Image> finder = new Finder<Integer, Image>(Image.class);

    public static Image createImage(String public_id, String image_url, String secret_image_url, Apartment apartment) {
        Image image = new Image();
        image.public_id = public_id;
        image.image_url = image_url;
        image.secret_image_url = secret_image_url;
        image.apartment = apartment;
        image.save();
        return image;
    }

    public static Image create(String public_id, String image_url, String secret_image_url) {
        Image i = new Image();
        i.public_id = public_id;
        i.image_url = image_url;
        i.secret_image_url = secret_image_url;
        i.save();
        return i;
    }

    public static Image create(File image, Integer itemId) {
        Map result;

        try {
            result = cloudinary.uploader().upload(image, null);
            return create(result, itemId);

        } catch (IOException e) {
            Logger.debug("Failed to save image.", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static Image create(Map uploadResult, Integer apartmentId) {
        Image image = new Image();

        image.public_id = (String) uploadResult.get("public_id");
        Logger.debug(image.public_id);
        image.image_url = (String) uploadResult.get("url");
        Logger.debug(image.image_url);
        image.secret_image_url = (String) uploadResult.get("secure_url");
        Logger.debug(image.secret_image_url);
        if(apartmentId != null) {
            image.apartment = Apartment.getApartmentById(apartmentId);
        }
        image.save();
        return image;
    }

    /* ------------------- all images ------------------ */

    public static List<Image> all() {
        return finder.all();
    }

    /* ------------------- get image size ------------------ */

    public String getSize(int width, int height) {
        try {
            String url;

            url = cloudinary.url().format("jpg")
                    .transformation(new Transformation().width(width).height(height)).generate(public_id);

            return url;
        }catch (NullPointerException e){
            Logger.debug("Failed to receive image url.", e.getMessage());
            return "null";
        }
    }

    /* ------------------- delete image ------------------ */

    public static Integer deleteImage(Image image) {
        Integer apartmentId = image.apartment.id;
        try {
            cloudinary.uploader().destroy(image.public_id, null);
        } catch (IOException e) {
            Logger.debug("Failed to delete image.", e.getMessage());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        image.delete();
        return apartmentId;

    }

    /* ------------------- find images by item id ------------------ */

    public static List<Image> findImagesByItemId(Integer apartmentId){
        return finder.where().eq("apartment_id", apartmentId).findList();
    }

    /* ------------------- find images by news id ------------------ */

    public static List<Image> findImagesByNewsId(Integer newsId){
        return finder.where().eq("news_id", newsId).findList();
    }



    /* ------------------- find images by id ------------------ */

    public static Image findImageById(String public_id){
        return finder.where().eq("public_id", public_id).findUnique();
    }
}
