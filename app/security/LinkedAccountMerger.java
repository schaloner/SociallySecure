package security;

import models.LinkedAccount;
import securesocial.provider.SocialUser;
import utils.Merger;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class LinkedAccountMerger implements Merger<LinkedAccount, SocialUser>
{
    public LinkedAccount merge(LinkedAccount linkedAccount,
                               SocialUser socialUser)
    {
        linkedAccount.userId = socialUser.id.id;
        linkedAccount.provider = socialUser.id.provider;
        linkedAccount.accessToken = socialUser.accessToken;
        linkedAccount.authMethod = socialUser.authMethod;
        linkedAccount.avatarUrl = socialUser.avatarUrl;
        linkedAccount.displayName = socialUser.displayName;
        linkedAccount.email = socialUser.email;
        linkedAccount.isEmailVerified = socialUser.isEmailVerified;
        linkedAccount.lastAccess = socialUser.lastAccess;
        linkedAccount.password = socialUser.password;
        linkedAccount.secret = socialUser.secret;
        linkedAccount.token = socialUser.token;

        return linkedAccount;
    }
}
