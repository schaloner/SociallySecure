package models;

import models.deadbolt.Role;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
public class SociallySecureRole extends Model implements Role
{
    @Column(nullable = false)
    public String roleName;

    public SociallySecureRole(String roleName)
    {
        this.roleName = roleName;
    }

    public String getRoleName()
    {
        return roleName;
    }
}
