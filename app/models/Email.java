package models;

import com.avaje.ebean.Model;
import helpers.ConfigProvider;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Created by User on 2/1/2016.
 */
public class Email extends Model {
     /* ------------------- send mail to user when his account is created ------------------ */

    public static void sendMail(String mail, String password){


        SimpleEmail email = new SimpleEmail();
        email.setHostName(ConfigProvider.SMTP_HOST);
        email.setSmtpPort(Integer.parseInt(ConfigProvider.SMTP_PORT));
        try {
                /*Configuring mail*/
            email.setAuthentication(ConfigProvider.MAIL_FROM, ConfigProvider.MAIL_FROM_PASS);
            email.setFrom(ConfigProvider.MAIL_FROM);
            email.setStartTLSEnabled(true);
            email.addTo(mail);
            email.setSubject("Uspjesno kreiran profil");
            email.setMsg("Postovani, " + "\n" +
                    "Zelimo Vas obavijestiti da je Vas profil napravljen." + "\n" +
                    "Profilu mozete pristupiti uz sljedece podatke:" + "\n" +
                    "Username: " + mail + "\n" +
                    "Password: " + password + "\n" +

                    "Zelimo Vam puno uspjeha." + "\n" +
                    "Vas," + "\n" +
                    "StanNaDan");
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

         /* ------------------- send mail reservation ------------------ */

    public static void sendMailReservation(String name, String mail, String phone, String checkInDate, String checkOutDate, String numOfPersons, String comment, Integer apartmentId){


        /* sending an email*/
        SimpleEmail email = new SimpleEmail();
        email.setHostName(ConfigProvider.SMTP_HOST);
        email.setSmtpPort(Integer.parseInt(ConfigProvider.SMTP_PORT));
        try {
                /*Configuring mail*/
            email.setAuthentication(ConfigProvider.MAIL_FROM, ConfigProvider.MAIL_FROM_PASS);
            email.setFrom(ConfigProvider.MAIL_FROM);
            email.setStartTLSEnabled(true);
            email.addTo(mail);
            email.setSubject("Rezervacija");
            email.setMsg("Ime i prezime:  " + name + "\n" +
                    "Email:  " + mail + "\n" +
                    "Telefon:  " + phone + "\n" +
                    "Datum dolaska:  " + checkInDate + "\n" +
                    "Datum odlaska:  " + checkOutDate + "\n" +
                    "Broj osoba:  " + numOfPersons + "\n\n" +
                    "Komentar:  " + comment);

            Reservation.saveReservation(apartmentId, name, mail, phone, checkInDate, checkOutDate, numOfPersons, comment);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

}
