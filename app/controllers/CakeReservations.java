package controllers;

import models.CakeReservation;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by User on 6/21/2016.
 */
public class CakeReservations extends Controller {

    public Result pastryReservationsRender(Integer pastryId) {
        List<CakeReservation> reservations = CakeReservation.findCakeReservationByPastryId(pastryId);
        return ok(views.html.pastry.pastryReservations.render(reservations));
    }

    public Result deleteCakeReservation(Integer reservationId) {
        Integer pastryId = CakeReservation.deleteCakeReservationByReservationId(reservationId);
        return redirect(routes.CakeReservations.pastryReservationsRender(pastryId));
    }
}
