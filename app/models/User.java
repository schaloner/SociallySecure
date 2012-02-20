package models;

import models.deadbolt.Role;
import models.deadbolt.RoleHolder;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Iterator;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
public class User extends Model implements RoleHolder
{
    public String userName;

    public String displayName;

    public String avatarUrl;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Friend> friends;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "privilege_id", referencedColumnName = "id",nullable = false)
    @JoinTable(name="USER_ROLES")
    public List<SociallySecureRole> roles;

    private User(Builder builder)
    {
        avatarUrl = builder.avatarUrl;
        userName = builder.userName;
        displayName = builder.displayName;
        roles = builder.roles;
    }

    public List<? extends Role> getRoles()
    {
        return roles;
    }

    public static User findByUserName(String userName)
    {
        return find("byUserName", userName).first();
    }

    public static boolean userExists(String userName)
    {
        return count("byUserName", userName) > 0;
    }

    /**
     * Checks if otherUser is a friend of this user.
     *
     * @param otherUser the other user
     * @return true iff otherUser is a friend
     */
    public boolean isFriend(User otherUser)
    {
        boolean isFriend = false;

        for (Iterator<Friend> iterator = friends.iterator(); !isFriend && iterator.hasNext(); )
        {
            Friend friend = iterator.next();
            if (friend.user.equals(otherUser))
            {
                isFriend = true;
            }

        }

        return isFriend;
    }

    public static final class Builder
    {
        private String avatarUrl;
        private String userName;
        private String displayName;
        private List<SociallySecureRole> roles;

        public Builder()
        {
        }

        public Builder avatarUrl(String avatarUrl)
        {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder userName(String userName)
        {
            this.userName = userName;
            return this;
        }

        public Builder displayName(String displayName)
        {
            this.displayName = displayName;
            return this;
        }

        public Builder roles(List<SociallySecureRole> roles)
        {
            this.roles = roles;
            return this;
        }

        public User build()
        {
            return new User(this);
        }
    }
}
