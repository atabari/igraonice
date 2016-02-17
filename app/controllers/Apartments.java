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
            flash("success", "Uspješno ste kreirali apartman.");
            return redirect(routes.Apartments.apartment(apart.id));
        } else {
            flash("error", "Desila se greška, apartman nije kreiran.");
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
            flash("success", "Uspješno ste ažurirali podatke o apartmanu.");
            return ok(apartment.render(apart, currentUser,apartments,reservations));
        } else {
            flash("error", "Desila se greška, podaci o apartmanu nisu ažurirani.");
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

    /* --------------- apartments with location Banja Luka ---------------*/

    public Result banjalukaApartments(){
        List<Apartment> apartments = Apartment.apartmentsBanjaLuka();
        return ok(searchApartments.render(apartments));
    }

    /* --------------- apartments with location Mostar ---------------*/

    public Result mostarApartments(){
        List<Apartment> apartments = Apartment.apartmentsMostar();
        return ok(searchApartments.render(apartments));
    }

    /* --------------- apartments with location Zenica ---------------*/

    public Result zenicaApartments(){
        List<Apartment> apartments = Apartment.apartmentsZenica();
        return ok(searchApartments.render(apartments));
    }

    /* --------------- apartments with location Tuzla ---------------*/

    public Result tuzlaApartments(){
        List<Apartment> apartments = Apartment.apartmentsTuzla();
        return ok(searchApartments.render(apartments));
    }

    /* --------------- apartments in mountain Bjelasnica ---------------*/

    public Result bjelasnicaApartments(){
        List<Apartment> apartments = Apartment.apartmentsBjelasnica();
        return ok(searchApartments.render(apartments));
    }

    /* --------------- apartments in mountain Jahorina ---------------*/

    public Result jahorinaApartments(){
        List<Apartment> apartments = Apartment.apartmentsJahorina();
        return ok(searchApartments.render(apartments));
    }

    /* --------------- apartments in mountain Vlasic ---------------*/

    public Result vlasicApartments(){
        List<Apartment> apartments = Apartment.apartmentsVlasic();
        return ok(searchApartments.render(apartments));
    }

    /* --------------- apartments in mountain Igman ---------------*/

    public Result igmanApartments(){
        List<Apartment> apartments = Apartment.apartmentsIgman();
        return ok(searchApartments.render(apartments));
    }

    /* --------------- apartments in mountain Trebevic ---------------*/

    public Result trebevicApartments(){
        List<Apartment> apartments = Apartment.apartmentsTrebevic();
        return ok(searchApartments.render(apartments));
    }


        /* --------------- delete apartment ---------------*/

    public Result deleteApartment(Integer apartmentId){
        Apartment.deleteApartment(apartmentId);
        List<Apartment> apartments = Apartment.getAllApartments();
        return status(200, adminpage.render(apartments));
    }
            /* --------------- admin page render ---------------*/
//    @Security.Authenticated(Authenticator.AdminFilter.class)
//    public Result adminPageRender(){
//        List<Apartment> apartments = Apartment.getAllApartments();
//        return ok(adminpage.render(apartments));
//    }


     /* --------------- show apartment on homepage ---------------*/

    public Result showOnHomepage(Integer apartmentId){
        Apartment.isVisible(apartmentId);
        return redirect(routes.Login.apartmentsList());
    }
}
