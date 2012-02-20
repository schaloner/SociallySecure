package models;

import play.db.jpa.Model;
import securesocial.provider.AuthenticationMethod;
import securesocial.provider.ProviderType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;

/**
 * This persists SocialUser information.
 *
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
public class LinkedAccount extends Model
{
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    public User user;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    public LinkedAccountSecurity security;

    public String userId;

    @Enumerated(EnumType.STRING)
    public ProviderType provider;

    public String displayName;

    public String email;

    public String avatarUrl;

    public Date lastAccess;

    @Enumerated(EnumType.STRING)
    public AuthenticationMethod authMethod;

    public String token;

    public String secret;

    public String accessToken;

    public String password;

    public boolean isEmailVerified;

    public LinkedAccount()
    {
        // no-op
    }

    private LinkedAccount(Builder builder)
    {
        user = builder.user;
        security = builder.security;
        accessToken = builder.accessToken;
        userId = builder.userId;
        provider = builder.provider;
        displayName = builder.displayName;
        email = builder.email;
        avatarUrl = builder.avatarUrl;
        lastAccess = builder.lastAccess;
        authMethod = builder.authMethod;
        token = builder.token;
        secret = builder.secret;
        password = builder.password;
        isEmailVerified = builder.isEmailVerified;
    }

    public static LinkedAccount findByProviderAndUserId(ProviderType provider,
                                                        String userId)
    {
        return find("byProviderAndUserId",
                    provider,
                    userId).first();
    }

    public static final class Builder
    {
        private User user;
        private LinkedAccountSecurity security;
        private String accessToken;
        private String userId;
        private ProviderType provider;
        private String displayName;
        private String email;
        private String avatarUrl;
        private Date lastAccess;
        private AuthenticationMethod authMethod;
        private String token;
        private String secret;
        private String password;
        private boolean isEmailVerified;

        public Builder()
        {
        }

        public Builder accessToken(String accessToken)
        {
            this.accessToken = accessToken;
            return this;
        }

        public Builder user(User user)
        {
            this.user = user;
            return this;
        }

        public Builder security(LinkedAccountSecurity security)
        {
            this.security = security;
            return this;
        }

        public Builder userId(String userId)
        {
            this.userId = userId;
            return this;
        }

        public Builder provider(ProviderType provider)
        {
            this.provider = provider;
            return this;
        }

        public Builder displayName(String displayName)
        {
            this.displayName = displayName;
            return this;
        }

        public Builder email(String email)
        {
            this.email = email;
            return this;
        }

        public Builder avatarUrl(String avatarUrl)
        {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder lastAccess(Date lastAccess)
        {
            this.lastAccess = lastAccess;
            return this;
        }

        public Builder authMethod(AuthenticationMethod authMethod)
        {
            this.authMethod = authMethod;
            return this;
        }

        public Builder token(String token)
        {
            this.token = token;
            return this;
        }

        public Builder secret(String secret)
        {
            this.secret = secret;
            return this;
        }

        public Builder password(String password)
        {
            this.password = password;
            return this;
        }

        public Builder isEmailVerified(boolean isEmailVerified)
        {
            this.isEmailVerified = isEmailVerified;
            return this;
        }

        public LinkedAccount build()
        {
            return new LinkedAccount(this);
        }
    }
}