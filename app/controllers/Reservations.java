package controllers;

import models.Reservation;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by User on 2/25/2016.
 */
public class Reservations extends Controller {

    public Result listOfReservationTimes(String datum){
        List<String> times =  Reservation.getReservations(datum);
        return ok(String.valueOf(times));
    }
}
