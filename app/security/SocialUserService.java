package security;

import models.LinkedAccount;
import securesocial.provider.SocialUser;
import securesocial.provider.UserId;
import securesocial.provider.UserService;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class SocialUserService implements UserService.Service
{
    /** {@inheritDoc} */
    public SocialUser find(UserId id)
    {
        SocialUser socialUser = null;
        LinkedAccount linkedAccount = LinkedAccount.findByProviderAndUserId(id.provider,
                                                                            id.id);
        if (linkedAccount != null)
        {
            socialUser = new LinkedAccountToSocialUser().transform(linkedAccount);
        }
        return socialUser;
    }

    /** {@inheritDoc} */
    public void save(SocialUser user)
    {
        LinkedAccount linkedAccount = LinkedAccount.findByProviderAndUserId(user.id.provider,
                                                                            user.id.id);
        if (linkedAccount == null)
        {
            linkedAccount = new SocialUserToLinkedAccount().transform(user);
        }
        linkedAccount.save();
    }

    /** {@inheritDoc} */
    public boolean activate(String uuid)
    {
        // no-op
        return false;
    }

    /** {@inheritDoc} */
    public String createActivation(SocialUser user)
    {
        // no-op
        return null;
    }

    /** {@inheritDoc} */
    public void deletePendingActivations()
    {
        // no-op
    }
}
