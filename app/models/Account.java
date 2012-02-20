package models;

import play.db.jpa.Model;
import securesocial.provider.ProviderType;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKey;
import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
public class Account extends Model
{
    @OneToOne(cascade = CascadeType.ALL)
    public User user;

    @ElementCollection(fetch= FetchType.EAGER)
    @MapKey(name = "provider")
    public Map<ProviderType, LinkedAccount> linkedAccounts = new HashMap<ProviderType, LinkedAccount>();

    private Account(Builder builder)
    {
        linkedAccounts = builder.linkedAccounts;
        user = builder.user;
    }

    public static Account findByUser(User user)
    {
        return find("byUser", user).first();
    }

    public static final class Builder
    {
        private Map<ProviderType, LinkedAccount> linkedAccounts;
        private User user;

        public Builder()
        {
        }

        public Builder linkedAccounts(Map<ProviderType, LinkedAccount> linkedAccounts)
        {
            this.linkedAccounts = linkedAccounts;
            return this;
        }

        public Builder user(User user)
        {
            this.user = user;
            return this;
        }

        public Account build()
        {
            return new Account(this);
        }
    }
}
