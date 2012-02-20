package security;

import models.LinkedAccount;
import models.LinkedAccountSecurity;
import securesocial.provider.SocialUser;
import utils.Transformer;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class SocialUserToLinkedAccount implements Transformer<SocialUser, LinkedAccount>
{
    public LinkedAccount transform(SocialUser socialUser)
    {
        return new LinkedAccountMerger().merge(new LinkedAccount.Builder()
                                                       .security(new LinkedAccountSecurity())
                                                       .build(),
                                               socialUser);
    }
}
