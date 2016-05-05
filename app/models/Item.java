package models;

import com.avaje.ebean.Model;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by User on 5/5/2016.
 */
public class Item extends Model {

    @Id
    public Integer id;
    public String name;
    public Integer price;
    public String description;

    @ManyToOne
    public Store store;

    public Item() {}
    
}
