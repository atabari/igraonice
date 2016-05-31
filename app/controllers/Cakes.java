package controllers;

import models.Cake;
import models.Image;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import controllers.Images;

import java.io.File;
import java.util.List;

/**
 * Created by User on 5/24/2016.
 */
public class Cakes extends Controller {

    /* --------------- create cake render ---------------*/

    public Result createCakeRender(Integer storeId) {
        return ok(views.html.cake.createCake.render(storeId));
    }


    /* --------------- create cake  ---------------*/

    public Result createCake(Integer pastryId) {
        DynamicForm form = Form.form().bindFromRequest();
        String name = form.field("name").value();
        Integer price  = Integer.parseInt(form.field("price").value());
        String ingredients = form.field("ingredients").value();
        Integer numberOfPersons = Integer.parseInt(form.field("numberOfPersons").value());
        String description = form.field("description").value();

        Cake cake = Cake.createCake(name, ingredients, price, description, numberOfPersons, pastryId);

        Http.MultipartFormData body1 = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> fileParts = body1.getFiles();
        if (fileParts != null) {
            for (Http.MultipartFormData.FilePart filePart1 : fileParts) {
                File file = filePart1.getFile();
                Image image = Image.createCakeImage(file, cake.id);
                cake.images.add(image);
            }
        }
        cake.update();


        return redirect(routes.Cakes.listOfStoreCakes(pastryId));
    }


    /* --------------- list of store cakes  ---------------*/
    public Result listOfStoreCakes(Integer storeId) {
        List<Cake> cakes = Cake.findAllCakesByPastryId(storeId);
        return ok(views.html.cake.listOfStoreCakes.render(cakes, storeId));
    }


     /* --------------- update cake render ---------------*/

    public Result updateCakeRender(Integer cakeId) {
        Cake cake = Cake.findCakeById(cakeId);
        return ok(views.html.cake.updateCake.render(cake));
    }


    /* --------------- update cake  ---------------*/

    public Result updateCake(Integer cakeId) {
        DynamicForm form = Form.form().bindFromRequest();
        String name = form.field("name").value();
        Integer price  = Integer.parseInt(form.field("price").value());
        String ingredients = form.field("ingredients").value();
        Integer numberOfPersons = Integer.parseInt(form.field("numberOfPersons").value());
        String description = form.field("description").value();

        Integer pastryId = Cake.updateCake(name, ingredients,price,description,numberOfPersons,cakeId);
        return redirect(routes.Cakes.listOfStoreCakes(pastryId));
    }

     /* --------------- delete cake  ---------------*/

    public Result deleteCake(Integer cakeId) {
        List<Image> images = Image.findCakeImages(cakeId);
        for (int i = 0; i < images.size(); i++) {
            images.get(i).delete();
        }

        Cake cake = Cake.findCakeById(cakeId);

        Integer storeId = cake.pastry.id;
        cake.delete();

        return redirect(routes.Cakes.listOfStoreCakes(storeId));
    }

}
