package controllers;

import controllers.securesocial.SecureSocial;
import models.Account;
import models.LinkedAccount;
import models.User;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.Util;
import play.mvc.With;
import securesocial.provider.ProviderType;
import securesocial.provider.SocialUser;
import utils.StringUtils;

import java.util.HashMap;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@With(SecureSocial.class)
public class RootController extends Controller
{
    public static User getUser()
    {
        String userName = session.get("userName");
        User user = null;
        if (!StringUtils.isEmpty(userName))
        {
            user = User.findByUserName(userName);
        }

        return user;
    }

    public static Account getAccount()
    {
        User user = getUser();
        return Account.findByUser(user);
    }

    @Util
    public static void setUser(String userName)
    {
        session.put("userName", userName);
    }

    public static void createAccount()
    {
        render();
    }

    public static void logout()
    {
        session.clear();
        SecureSocial.logout();
    }

    public static void doCreateAccount(@Required String userName)
    {
        if (validation.hasErrors())
        {
            params.flash();
            validation.keep();
            createAccount();
        }

        if (User.userExists(userName))
        {
            flash.error("That user name is already taken");
            params.flash();
            validation.keep();
            createAccount();
        }

        SocialUser socialUser = SecureSocial.getCurrentUser();
        User user = new User.Builder()
                .userName(userName)
                .displayName(socialUser.displayName)
                .avatarUrl(socialUser.avatarUrl)
                .build()
                .save();

        Account account = new Account.Builder()
                .user(user)
                .linkedAccounts(new HashMap<ProviderType, LinkedAccount>())
                .build();

        LinkedAccount linkedAccount = LinkedAccount.findByProviderAndUserId(socialUser.id.provider,
                                                                            socialUser.id.id);
        linkedAccount.user = user;
        linkedAccount.save();

        account.linkedAccounts.put(linkedAccount.provider,
                                   linkedAccount);
        account.save();

        setUser(userName);

        SociallySecure.index();
    }
}
