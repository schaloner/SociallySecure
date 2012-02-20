package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.securesocial.SecureSocial;
import models.Account;
import models.Friend;
import models.LinkedAccount;
import models.LinkedAccountSecurity;
import models.User;
import models.twitter.Tweet;
import play.Logger;
import play.mvc.Before;
import play.mvc.With;
import providerapi.Twitter;
import securesocial.provider.IdentityProvider;
import securesocial.provider.ProviderRegistry;
import securesocial.provider.ProviderType;
import securesocial.provider.SocialUser;
import security.SocialUserToLinkedAccount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@With({SecureSocial.class,
       Deadbolt.class})
public class SociallySecure extends RootController
{
    @Before(priority = Integer.MAX_VALUE)
    static void globals()
    {
        if (!request.isAjax())
        {
            SocialUser socialUser = SecureSocial.getCurrentUser();

            LinkedAccount linkedAccount = LinkedAccount.findByProviderAndUserId(socialUser.id.provider,
                                                                                socialUser.id.id);
            if (session.get("userName") == null)
            {
                if (linkedAccount != null
                    && linkedAccount.user != null)
                {
                    setUser(linkedAccount.user.userName);
                }
                else
                {
                    createAccount();
                }
            }
            else
            {
                User user = getUser();
                if (user == null)
                {
                    createAccount();
                }
            }

            if (linkedAccount != null)
            {
                if (linkedAccount.user != null)
                {
                    renderArgs.put("displayName",
                                   linkedAccount.user.displayName);
                    renderArgs.put("avatarUrl",
                                   linkedAccount.user.avatarUrl);
                }
                else
                {
                    Account account = getAccount();
                    if (!account.linkedAccounts.containsKey(socialUser.id.provider))
                    {
                        linkedAccount.user = getUser();
                        linkedAccount.save();

                        account.linkedAccounts.put(linkedAccount.provider,
                                                   linkedAccount);
                        account.save();
                    }

                }
            }
        }
    }

    public static void index()
    {
        Account account = getAccount();
        List<Tweet> tweets = null;
        if (account.linkedAccounts.containsKey(ProviderType.twitter))
        {
            tweets = new Twitter(account.linkedAccounts.get(ProviderType.twitter)).getTweets();
        }
        render(tweets);
    }

    public static void friends()
    {
        User user = getUser();
        List<Friend> friends = user.friends;
        render(friends);
    }

    public static void findFriends(String searchTerm)
    {
        List<User> potentialFriends = new ArrayList<User>(User.find("byDisplayNameIlike",
                                                                    '%' + searchTerm + '%')
                                                              .<User>fetch());
        User user = getUser();
        potentialFriends.remove(user);
        for (Friend friend : user.friends)
        {
            potentialFriends.remove(friend.user);
        }
        render(potentialFriends);
    }

    public static void addAsFriend(Long id)
    {
        User currentUser = getUser();
        Friend friend = new Friend.Builder()
                .user(User.<User>findById(id))
                .build();
        currentUser.friends.add(friend);
        currentUser.save();

        friends();
    }

    public static void viewFriend(Long friendId)
    {
        Friend friend = Friend.findById(friendId);
        Account friendAccount = Account.findByUser(friend.user);
        List<Tweet> tweets = null;
        if (friendAccount.linkedAccounts.containsKey(ProviderType.twitter))
        {
            tweets = new Twitter(friendAccount.linkedAccounts.get(ProviderType.twitter)).getTweets();
        }

        render(friend,
               tweets);
    }

    public static void viewUser(Long userId)
    {
        User user = User.findById(userId);
        Account friendAccount = Account.findByUser(user);
        List<Tweet> tweets = null;
        if (friendAccount.linkedAccounts.containsKey(ProviderType.twitter))
        {
            tweets = new Twitter(friendAccount.linkedAccounts.get(ProviderType.twitter)).getTweets();
        }

        render(user,
               tweets);
    }

    public static void linkedAccounts()
    {
        Account account = getAccount();
        Map<ProviderType, LinkedAccount> linkedAccounts = account.linkedAccounts;
        LinkedAccount twitter = null;
        if (linkedAccounts.containsKey(ProviderType.twitter))
        {
            twitter = linkedAccounts.get(ProviderType.twitter);
        }

        Collection<IdentityProvider> identityProviders = ProviderRegistry.all();
        List<ProviderType> providerTypes = new ArrayList<ProviderType>();
        for (IdentityProvider identityProvider : identityProviders)
        {
            if (!linkedAccounts.containsKey(identityProvider.type))
            {
                providerTypes.add(identityProvider.type);
            }
        }

        render(linkedAccounts,
               providerTypes,
               twitter);
    }

    public static void newLinkedAccount(ProviderType provider)
    {
        User user = getUser();

        if (provider != null)
        {
            IdentityProvider identityProvider = ProviderRegistry.get(provider);
            SocialUser socialUser = identityProvider.authenticate();

            LinkedAccount linkedAccount = new SocialUserToLinkedAccount().transform(socialUser);
            linkedAccount.user = user;
            linkedAccount.security = new LinkedAccountSecurity();
            linkedAccount.save();

            Account account = getAccount();
            account.linkedAccounts.put(linkedAccount.provider,
                                       linkedAccount);
            account.save();
        }
        else
        {
            Logger.error("Provider is null!");
        }

        linkedAccounts();
    }

    public static void updateTweetVisibility(Boolean tweetsPubliclyVisible)
    {
        Account account = getAccount();
        Map<ProviderType, LinkedAccount> linkedAccounts = account.linkedAccounts;
        if (linkedAccounts.containsKey(ProviderType.twitter))
        {
            LinkedAccount twitter = linkedAccounts.get(ProviderType.twitter);
            twitter.security.publiclyVisible = tweetsPubliclyVisible;
            twitter.security.save();
        }

        linkedAccounts();
    }
}