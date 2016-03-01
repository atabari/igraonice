package controllers;

import models.Apartment;
import models.Reservation;
import play.data.DynamicForm;
import play.data.Form;
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

    public Result saveReservation(Integer apartmentId){
        DynamicForm form = Form.form().bindFromRequest();
        String date = form.field("checkIndate").value();
        String timeFrom = form.field("timeFrom").value();
        String timeTo = form.field("timeTo").value();
        String name = form.field("name").value();
        String phone = form.field("phone").value();

        Reservation.saveReservation(apartmentId, name, null, phone, date, timeFrom, timeTo,null);
        return redirect(routes.Paketi.listOfPackages(apartmentId));
    }
}
