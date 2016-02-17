package controllers;

import helpers.ConfigProvider;
import models.Email;
import models.Reservation;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.createapartment;
import views.html.createuser;

/**
 * Created by User on 1/8/2016.
 */
public class Emails extends Controller {
    public Result sendMail(Integer apartmentId) {

        //taking values from input fields
        DynamicForm form = Form.form().bindFromRequest();
        String name = form.field("name").value();
        String mail = form.field("mail").value();
        String phone = form.field("phone").value();
        String checkIndate = form.field("checkIndate").value();
        String checkOutdate = form.field("checkOutdate").value();
        String numOfPersons = form.field("numOfPersons").value();
        String comment = form.field("comment").value();

            Email.sendMailReservation(name, mail, phone, checkIndate, checkOutdate, numOfPersons, comment, apartmentId);
            /*If mail is sent flash appears and user is redirected to index page */
            flash("success", "Vasa poruka je poslana. Potrudit cemo se da odgovorimo u najkracem mogucem roku. Zahvaljujemo!");
            return redirect(routes.Apartments.apartment(apartmentId));
    }


}
