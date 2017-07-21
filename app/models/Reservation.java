package models;

import com.avaje.ebean.Model;
import com.sun.org.apache.regexp.internal.RE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.*;


/**
 * Created by ajla on 26-Dec-15.
 */
@Entity
public class Reservation extends Model {
    @Id
    public Integer id;

    public String dateFrom;

    public String timeFrom;
    public String timeTo;

    public String visitorName;
    public String visitorLastname;
    public String visitorEmail;
    public String capacity;
    public String phone;
    @Column(columnDefinition = "TEXT")
    public String comment;
    public Integer cost;
    @ManyToOne
    public Apartment apartment;
    public Boolean confirmed;
    @ManyToOne
    public Paket paket;
    public Boolean duplicated;


    public Reservation() {
    }
    public Reservation(Apartment apartment, String dateFrom,String timeFrom,String timeTo, String visitorName,
                       String visitorLastname, String visitorEmail,String capacity, String phone, String comment,
                       Integer cost, Boolean confirmed, Paket paket, Boolean duplicated) {
        this.apartment = apartment;
        this.dateFrom = dateFrom;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.visitorName = visitorName;
        this.visitorLastname = visitorLastname;
        this.visitorEmail = visitorEmail;
        this.capacity = capacity;
        this.phone = phone;
        this.comment = comment;
        this.cost = cost;
        this.confirmed = confirmed;
        this.paket = paket;
        this.duplicated = duplicated;
    }


    public static void saveReservation(Integer apartmentId, String name, String email, String phone, String checkInDate,
                                       String timeFrom, String timeTo, String comment, Integer paketId, Boolean duplicated) {

        Apartment apartment = Apartment.getApartmentById(apartmentId);

        Reservation reservation = new Reservation();
        reservation.apartment = apartment;
        reservation.comment = comment;
        reservation.phone = phone;
        reservation.visitorEmail = email;
        reservation.visitorName = name;

        if(reservation.visitorName.contains(" ")) {
            reservation.visitorName = name.split(" ")[0];
            reservation.visitorLastname = name.split(" ")[1];
        } else {
            reservation.visitorName = name;
            reservation.visitorLastname = " ";
        }
        reservation.dateFrom = checkInDate;
        reservation.timeFrom = timeFrom;
        reservation.timeTo = timeTo;
        reservation.confirmed = false;

        Paket paket = Paket.getPackageById(paketId);
        reservation.paket = paket;
        reservation.duplicated = duplicated;

        reservation.save();
    }

    public static Set<String> reservationTimes(Integer apartmentId, String dateToCheck) {
        Model.Finder<String, Reservation> finder = new Model.Finder<>(Reservation.class);
        List<Reservation> reservations = finder.where().eq("apartment_id", apartmentId).findList();
        Set<String> times = new HashSet<>();

        for (int i = 0; i < reservations.size(); i++) {
            if(reservations.get(i).confirmed && dateToCheck.equals(reservations.get(i).dateFrom)) {
                times.add(reservations.get(i).timeFrom + "-" + reservations.get(i).timeTo);
            }
        }
        return times;
    }

    public static HashMap<String, Set<String>> getReservationsByApartmentId(Integer apartmentId, String dateToCheck) {
        Model.Finder<String, Reservation> finder = new Model.Finder<>(Reservation.class);
        List <Reservation> confirmedReservations = new ArrayList<>();
        List<Reservation> reservations = finder.where().eq("apartment_id", apartmentId).findList();
        for(int e = 0; e < reservations.size(); e++) {
            if(reservations.get(e).confirmed == true) {
                confirmedReservations.add(reservations.get(e));
            }
        }
        HashMap<String, Set<String>> hashMap = new HashMap<>();

        for (int i=0; i < confirmedReservations.size(); i ++) {
            hashMap.put(confirmedReservations.get(i).dateFrom, reservationTimes(apartmentId, dateToCheck));

        }
        return hashMap;
    }

    public static List<Reservation> getAllPackageReservations(Integer paketid) {
        Model.Finder<String, Reservation> finder = new Model.Finder<>(Reservation.class);
        return finder.where().eq("paket_id", paketid).findList();
    }

    public static Set<String> getReservations(String datum) {

        String dateToCheck = datum.split("-")[0];
        Integer apartmentId = Integer.parseInt(datum.split("-")[1]) ;

        HashMap<String, Set<String>> hashMap = getReservationsByApartmentId(apartmentId, dateToCheck);

        return hashMap.get(dateToCheck);

    }

    public static List<Reservation> getApartmentReservations(Integer apartmentId) {
        Model.Finder<String, Reservation> finder = new Model.Finder<>(Reservation.class);
        return finder.where().eq("apartment_id", apartmentId).eq("duplicated", false).findList();
    }

    public static Reservation getReservationById(Integer reservationId) {
        Model.Finder<String, Reservation> finder = new Model.Finder<>(Reservation.class);
        return finder.where().eq("id", reservationId).findUnique();
    }

      /* --------------- confirm reservation ---------------*/

    public static void confirmReservation(Integer reservationId) {
        Reservation reservation = getReservationById(reservationId);

        if(reservation.confirmed) {
            reservation.confirmed = false;
            manageDuplicatedReservations(reservation, false);
        } else if (reservation.confirmed == false) {
            reservation.confirmed = true;
            manageDuplicatedReservations(reservation, true);
        }
        reservation.update();
    }

    /*
    Iterates through identical reservations and marks them as "duplicated", if confirming reservation,
    or unmarks them if reservation is cancelling.
     */
    private static void manageDuplicatedReservations(Reservation reservation, Boolean confirming) {
        String reservationDate = reservation.dateFrom;
        String reservationTime = reservation.timeFrom;
        Model.Finder<String, Reservation> finder = new Model.Finder<>(Reservation.class);

        List<Reservation> reservationsForProvidedDateAndTime = finder.where().eq("date_from",
                reservationDate).eq("time_from", reservationTime).findList();

        for (Reservation res : reservationsForProvidedDateAndTime) {
            if (!res.equals(reservation)) {
                res.duplicated = (confirming) ? true : false ;
                res.update();
            }
        }
    }

}
