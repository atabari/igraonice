package controllers;

import models.AppUser;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by User on 1/11/2016.
 */
public class Informations extends Controller {
    /* --------------- cooperation get ---------------*/
    public Result cooperation(){
        return ok(views.html.navbarviews.cooperation.render());
    }


    /* --------------- rules get ---------------*/
    public Result rules(){
        return ok(views.html.navbarviews.kucnired.render());
    }
    /* --------------- reservations get ---------------*/
    public Result reservations(){
        return ok(views.html.navbarviews.reservations.render());
    }
    /* --------------- payment get ---------------*/
    public Result payment(){
        return ok(views.html.navbarviews.payment.render());
    }
    /* --------------- apartment rules get ---------------*/
    public Result apartmentRules(){
        return ok(views.html.navbarviews.apartmentrules.render());
    }

    /* --------------- arrivals and departure get ---------------*/
    public Result arrivalsAndDeparture(){
        return ok(views.html.navbarviews.arrivalanddeparture.render());

    }
}
