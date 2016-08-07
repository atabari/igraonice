package controllers;

import helpers.Authenticator;
import helpers.Cookies;
import helpers.Session;
import helpers.UserAccessLevel;
import models.Apartment;
import models.AppUser;
import models.Pastry;
import models.Store;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;
import views.html.store.storePanel;

import java.util.List;

/**
 * Created by User on 1/5/2016.
 */
public class Login extends Controller {
        /* --------------- login page render ---------------*/

    public Result loginIndex(){
        return ok(login.render());
    }
    /* ---------------  admin page list of apartments render ---------------*/
    @Security.Authenticated(Authenticator.AdminFilter.class)
    public Result apartmentsList() {
        List<Apartment> apartments = Apartment.getAllApartments();
        return ok(adminpage.render(apartments));
    }

    /* ---------------  admin page list of stores render ---------------*/
    @Security.Authenticated(Authenticator.AdminFilter.class)
    public Result storeList() {
        List<Store> stores = Store.getAllStores();
        return ok(adminStores.render(stores));
    }

    /* ---------------  admin page list of pastries render ---------------*/
    @Security.Authenticated(Authenticator.AdminFilter.class)
    public Result pastryList() {
        List<Pastry> pastries = Pastry.getAllPastries();
        return ok(adminPastries.render(pastries));
    }
    @Security.Authenticated(Authenticator.AdminFilter.class)
    public Result showAdminPanel(String email){
        AppUser user = AppUser.findUserByEmail(email);

        return ok(adminpanel.render(user));
    }

    /* --------------- admin panel ---------------*/
    //@Security.Authenticated(Authenticator.AdminFilter.class)
    public Result renderAdminPanel(){
        DynamicForm form = Form.form().bindFromRequest();

        String email = form.field("email").value();
        String password = form.field("password").value();

        AppUser user = AppUser.authenticate(email, password);

        if (user == null) {
            flash("login-error", "Incorrect email or password! Try again.");
        }else if(user.userAccessLevel == UserAccessLevel.ADMIN){
            Cookies.setUserCookies(user);
            Session.setUserSessionData(user);
            return ok(adminpanel.render(user));
        }else if(user.userAccessLevel == UserAccessLevel.IGRAONICA
                || user.userAccessLevel == UserAccessLevel.POKLONI
                || user.userAccessLevel == UserAccessLevel.TORTE
                || user.userAccessLevel == UserAccessLevel.ANIMATORI){
            Cookies.setUserCookies(user);
            Session.setUserSessionData(user);
            return ok(userpanel.render(user));
        }
        flash("login-error", "Incorrect email or password! Please, try again.");
        return redirect(routes.Login.loginIndex());
    }

    /* --------------- admin page update password ---------------*/

    @Security.Authenticated(Authenticator.AdminFilter.class)
    public Result updateUser(String email){
        AppUser user = AppUser.findUserByEmail(email);
        DynamicForm form = Form.form().bindFromRequest();
        String password = form.field("password").value();

        boolean isUpdated = AppUser.updateUser(user, password);

        if(isUpdated){
            flash("success", "Vas password je azuriran");
            return redirect(routes.Login.showAdminPanel(user.email));
        }
        flash("error-search", "Neuspjelo azuriranje.");
        return redirect(routes.Login.renderAdminPanel());
        }

    }

