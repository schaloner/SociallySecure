package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
public class Friend extends Model
{
    @ManyToOne
    public User user;

    private Friend(Builder builder)
    {
        user = builder.user;
    }


    public static final class Builder
    {
        private User user;

        public Builder()
        {
        }

        public Builder user(User user)
        {
            this.user = user;
            return this;
        }

        public Friend build()
        {
            return new Friend(this);
        }
    }
}
