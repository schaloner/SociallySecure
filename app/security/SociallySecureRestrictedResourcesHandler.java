package security;

import controllers.RootController;
import controllers.deadbolt.RestrictedResourcesHandler;
import models.Account;
import models.LinkedAccountSecurity;
import models.User;
import models.deadbolt.AccessResult;
import securesocial.provider.ProviderType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class SociallySecureRestrictedResourcesHandler implements RestrictedResourcesHandler
{
    private final Map<String, RestrictedResourcesHandler> handlers = new HashMap<String, RestrictedResourcesHandler>();

    public SociallySecureRestrictedResourcesHandler()
    {
        handlers.put("userTweets",
                     new RestrictedResourcesHandler()
                     {
                         public AccessResult checkAccess(List<String> resourceNames,
                                                         Map<String, String> resourceParameters)
                         {

                             AccessResult accessResult = AccessResult.NOT_SPECIFIED;

                             String otherUserId = resourceParameters.get("otherUserId");
                             User otherUser = User.findById(Long.parseLong(otherUserId));
                             User currentUser = RootController.getUser();
                             if (otherUser.isFriend(currentUser))
                             {
                                 accessResult = AccessResult.ALLOWED;
                             }
                             else
                             {
                                 Account account = Account.findByUser(otherUser);
                                 if (account.linkedAccounts.containsKey(ProviderType.twitter))
                                 {
                                     LinkedAccountSecurity linkedAccountSecurity = account.linkedAccounts.get(ProviderType.twitter).security;
                                     accessResult = linkedAccountSecurity.publiclyVisible ? AccessResult.ALLOWED : AccessResult.DENIED;
                                 }
                             }
                             return accessResult;
                         }
                     });
    }

    public AccessResult checkAccess(List<String> resourceNames,
                                    Map<String, String> resourceParameters)
    {
        AccessResult accessResult = AccessResult.NOT_SPECIFIED;
        
        // keeping it simple here - restricted resources only have one name
        if (!resourceNames.isEmpty())
        {
            RestrictedResourcesHandler handler = handlers.get(resourceNames.get(0));
            if (handler != null)
            {
                accessResult = handler.checkAccess(resourceNames,
                                                   resourceParameters);
            }
        }

        return accessResult;
    }
}
