package controllers;

import helpers.Authenticator;
import helpers.Cookies;
import helpers.UserAccessLevel;
import models.Apartment;
import models.AppUser;
import models.Reservation;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.util.List;


/**
 * Created by User on 12/29/2015.
 */
public class Apartments extends Controller {


    // Apartment details
    public Result apartment(Integer apartmentId) {
        Apartment apart = Apartment.getApartmentById(apartmentId);
        AppUser currentUser = UserAccessLevel.getCurrentUser(ctx());
        List<Apartment> apartments = Apartment.apartmentsToRecommend(apartmentId);
        List<String> reservations = Reservation.getReservationsByApartmentId(apartmentId);

        return ok(apartment.render(apart, currentUser, apartments, reservations));
    }

    // Create apartment
    @Security.Authenticated(Authenticator.AdminUserFilter.class)
    public Result renderApartment(Integer userId) {
        return ok(createapartment.render(userId));
    }
    @Security.Authenticated(Authenticator.AdminUserFilter.class)
    public Result createApartment(Integer userId) {
        Apartment apart = Apartment.createApartment(userId);
        if (apart != null) {
            flash("success", "Uspješno ste kreirali objekat.");
            return redirect(routes.Apartments.apartment(apart.id));
        } else {
            flash("error", "Desila se greška, objekat nije kreiran.");
            return status(404, createapartment.render(userId));
        }
    }

    // Update apartment
    @Security.Authenticated(Authenticator.AdminUserFilter.class)
    public Result renderUpdateApartment(Integer apartmentId) {
        Apartment apart = Apartment.getApartmentById(apartmentId);

        return ok(updateapartment.render(apart));
    }

    @Security.Authenticated(Authenticator.AdminUserFilter.class)
    public Result updateApartment(Integer apartmentId) {
        Apartment apart = Apartment.updateApartment(apartmentId);
        List<Apartment> apartments = Apartment.apartmentsToRecommend(apartmentId);
        List<String> reservations = Reservation.getReservationsByApartmentId(apartmentId);

        AppUser currentUser = UserAccessLevel.getCurrentUser(ctx());
        if (apart != null) {
            flash("success", "Uspješno ste ažurirali podatke o objektu.");
            return ok(apartment.render(apart, currentUser,apartments,reservations));
        } else {
            flash("error", "Desila se greška, podaci o objektu nisu ažurirani.");
            return ok(createapartment.render(apart.userId));
        }
    }
    public Result cookies(Integer apartmentId){
        Cookies.setCookies(apartmentId);
        return redirect(routes.Apartments.apartment(apartmentId));
    }

    public Result favouriteApartments(){
//        String apartment = ctx.session().get(" ");
        return ok(favourite.render());
    }
     /* --------------- apartments with neighbourhood centar ---------------*/

    public Result centarApartments(){
        List<Apartment> apartments = Apartment.apartmentsCentar();
        return ok(searchApartments.render(apartments));
    }
        /* --------------- apartments with neighbourhood novo sarajevo ---------------*/

    public Result nsarajevoApartments(){
        List<Apartment> apartments = Apartment.apartmentsNSarajevo();
        return ok(searchApartments.render(apartments));
    }
        /* --------------- apartments with neighbourhood novi grad ---------------*/

    public Result ngradApartments(){
        List<Apartment> apartments = Apartment.apartmentsNGrad();
        return ok(searchApartments.render(apartments));
    }
        /* --------------- apartments with neighbourhood stari grad ---------------*/

    public Result sgradApartments(){
        List<Apartment> apartments = Apartment.apartmentsSGrad();
        return ok(searchApartments.render(apartments));
    }
        /* --------------- apartments with neighbourhood ilizda ---------------*/

    public Result ilidzaApartments(){
        List<Apartment> apartments = Apartment.apartmentsIlidza();
        return ok(searchApartments.render(apartments));
    }

     /* --------------- apartments with location Sarajevo ---------------*/

    public Result sarajevoApartments(){
        List<Apartment> apartments = Apartment.apartmentsSarajevo();
        return ok(searchApartments.render(apartments));
    }


        /* --------------- delete apartment ---------------*/

    public Result deleteApartment(Integer apartmentId){
        Apartment.deleteApartment(apartmentId);
        List<Apartment> apartments = Apartment.getAllApartments();
        return status(200, adminpage.render(apartments));
    }



     /* --------------- show apartment on homepage ---------------*/

    public Result showOnHomepage(Integer apartmentId){
        Apartment.isVisible(apartmentId);
        return redirect(routes.Login.apartmentsList());
    }
}
