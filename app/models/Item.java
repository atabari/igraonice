package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by User on 5/5/2016.
 */

@Entity
public class Item extends Model {

    @Id
    public Integer id;
    public String name;
    public Integer price;
    public String description;
    public String size;
    public Integer category;

    @ManyToOne
    public Store store;

    @OneToMany
    public List<Image> images;

    public Item() {}


    private static Model.Finder<String, Item> finder = new Model.Finder<>(Item.class);

            /* --------------- find item by id ---------------*/

    public static Item findItemById (Integer itemId) {
        return finder.where().eq("id", itemId).findUnique();
    }


    /* --------------- find all store items ---------------*/

    public static List<Item> findStoreItems(Integer storeId) {
        return finder.where().eq("store_id", storeId).findList();
    }

            /* --------------- create item  ---------------*/

    public static Item createItem(String name, Integer price, String description, String size, Store store, Integer category) {
        Item item = new Item();

        item.name = name;
        item.price = price;
        item.description = description;
        item.size = size;
        item.store = store;
        item.category = category;
        item.save();
        return  item;
    }


            /* --------------- update item ---------------*/

    public static Integer updateItem(String name, Integer price, String description, String size,Integer category, Integer itemId) {

        Item item = findItemById(itemId);
        item.name = name;
        item.price = price;
        item.description = description;
        item.size = size;
        item.category = category;
        item.update();
        return item.store.id;
    }

                /* --------------- delete item ---------------*/

    public static Integer deleteItem(Integer itemId) {
        Item item = findItemById(itemId);
        item.delete();
        return  item.store.id;
    }
    
}
