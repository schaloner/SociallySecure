package security;

import models.LinkedAccount;
import securesocial.provider.OAuth1Provider;
import securesocial.provider.SocialUser;
import securesocial.provider.UserId;
import utils.Transformer;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class LinkedAccountToSocialUser implements Transformer<LinkedAccount, SocialUser>
{
    public SocialUser transform(LinkedAccount linkedAccount)
    {
        SocialUser socialUser = new SocialUser();
        socialUser.id = new UserId();
        socialUser.id.id = linkedAccount.userId;
        socialUser.id.provider = linkedAccount.provider;
        socialUser.authMethod = linkedAccount.authMethod;
        socialUser.accessToken = linkedAccount.accessToken;
        socialUser.avatarUrl = linkedAccount.avatarUrl;
        socialUser.displayName = linkedAccount.displayName;
        socialUser.email = linkedAccount.email;
        socialUser.isEmailVerified = linkedAccount.isEmailVerified;
        socialUser.lastAccess = linkedAccount.lastAccess;
        socialUser.password = linkedAccount.password;
        socialUser.secret = linkedAccount.secret;
        socialUser.token = linkedAccount.token;
        socialUser.serviceInfo = OAuth1Provider.createServiceInfo(linkedAccount.provider.name());

        return socialUser;
    }
}
