package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by User on 5/23/2016.
 */
@Entity
public class ItemReservation extends Model {
    @Id
    public Integer id;

    public String date;
    public String customerName;
    public String customerPhone;
    public String customerEmail;
    @Column(columnDefinition = "TEXT")
    public String comment;

    public Integer cost;

    @ManyToOne
    public Store store;

    @ManyToOne
    public Item item;

    public ItemReservation(){}

    private static Model.Finder<String, ItemReservation> finder = new Model.Finder<>(ItemReservation.class);


    /* --------------- save item reservation ---------------*/

    public static void itemReservation(String date, String customerName, String customerPhone, String customerEmail, String comment, Integer cost, Integer itemId){
        ItemReservation reservation = new ItemReservation();
        Item item = Item.findItemById(itemId);
        Store store = item.store;

        reservation.date = date;
        reservation.customerName = customerName;
        reservation.customerPhone = customerPhone;
        reservation.customerEmail = customerEmail;
        reservation.comment = comment;
        reservation.cost = cost;
        reservation.item = item;
        reservation.store = store;

        reservation.save();
    }


    /* --------------- find item reservations by item id ---------------*/

    public static List<ItemReservation> findItemReservationsByStoreId(Integer storeId) {
        return finder.where().eq("store_id", storeId).findList();
    }


    /* --------------- find item reservations by item id ---------------*/

    public static ItemReservation findItemReservationById(Integer reservationId) {
        return finder.where().eq("id", reservationId).findUnique();
    }

    /* --------------- find item reservations by item id ---------------*/

    public static Integer deleteItemReservation(Integer reservationId) {
        ItemReservation reservation = findItemReservationById(reservationId);
        Integer storeId = reservation.store.id;
        reservation.delete();
        return storeId;
    }


}
