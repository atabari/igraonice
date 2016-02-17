package models;

import com.avaje.ebean.Model;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by ajla on 05-Jan-16.
 */
@Entity
public class AppUser extends Model {
    public static Finder<String, AppUser> finder = new Finder<>(AppUser.class);

    @Id
    public Integer id;
    public String email;
    public String password;
    public Integer userAccessLevel;

    public AppUser(){

    }
    public AppUser(String email, String password, Integer userAccessLevel) {
        this.email = email;
        this.password = password;
        this.userAccessLevel = userAccessLevel;
    }

        /* ------------------- create user ------------------ */

    public static void createUser(){
        DynamicForm form = Form.form().bindFromRequest();
        String email = form.field("email").value();
        String password = form.field("password").value();

        try {
            AppUser user = new AppUser();
            user.email = email;
            user.password = password;
            user.hashPass();
            user.userAccessLevel = 2;
            user.save();
            Email.sendMail(email, password);
        }catch (IllegalArgumentException e) {
            Logger.info(e.getMessage());
        }

    }


        /* ------------------- finds user by email ------------------ */

    public static AppUser findUserByEmail(String email){
        AppUser user = finder.where().eq("email", email).findUnique();
        return user;
    }

        /* ------------------- authenticate user by email and password ------------------ */

    public static AppUser authenticate(String email, String password) {

        AppUser user = finder.where().eq("email", email).findUnique();

        if (user != null && BCrypt.checkpw(password, user.password)) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * Hashes user password.
     */
    public void hashPass() {
        this.password = BCrypt.hashpw(this.password, BCrypt.gensalt());
    }

    public static boolean updateUser(AppUser user, String password){
        if(user != null){
            try {
                user.password = password;
                user.hashPass();
                user.update();
                return true;
            }catch (PersistenceException e ){
                Logger.error("Failed to update user password" + e.getMessage());
                return false;
            }
        }
        return false;
    }
    /* ------------------- get all users ------------------ */
    public static List<AppUser> getAllAppUsers(){
        Model.Finder<String, AppUser> finder = new Model.Finder<>(AppUser.class);
        List<AppUser> users = finder.all();
        return users;
    }

    /* ------------------- delete user ------------------ */
    public static void deleteUser(Integer userId){
        AppUser user = finder.where().eq("id", userId).findUnique();
        if(user != null){
            user.delete();
        }
    }

}
