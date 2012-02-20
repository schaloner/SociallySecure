package controllers;

import controllers.deadbolt.DeadboltHandler;
import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import controllers.deadbolt.RestrictedResourcesHandler;
import controllers.securesocial.SecureSocial;
import models.deadbolt.RoleHolder;
import play.mvc.Controller;
import security.SociallySecureRestrictedResourcesHandler;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class SociallySecureDeadboltHandler extends RootController implements DeadboltHandler
{
    public void beforeRoleCheck()
    {
        // this is handled by SecureSocial#checkAccess
    }

    public RoleHolder getRoleHolder()
    {
        return getUser();
    }

    public void onAccessFailure(String controllerClassName)
    {
        System.out.println("SociallySecureDeadboltHandler.onAccessFailure");
    }

    public ExternalizedRestrictionsAccessor getExternalizedRestrictionsAccessor()
    {
        // does anyone actually use ExternalizedRestrictions?
        return null;
    }

    public RestrictedResourcesHandler getRestrictedResourcesHandler()
    {
        return new SociallySecureRestrictedResourcesHandler();
    }
}
