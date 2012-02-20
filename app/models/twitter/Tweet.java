package models.twitter;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class Tweet
{
    public String text;

    public String userName;

    public String avatarUrl;

    private Tweet(Builder builder)
    {
        avatarUrl = builder.avatarUrl;
        text = builder.text;
        userName = builder.userName;
    }


    public static final class Builder
    {
        private String avatarUrl;
        private String text;
        private String userName;

        public Builder()
        {
        }

        public Builder avatarUrl(String avatarUrl)
        {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder text(String text)
        {
            this.text = text;
            return this;
        }

        public Builder userName(String userName)
        {
            this.userName = userName;
            return this;
        }

        public Tweet build()
        {
            return new Tweet(this);
        }
    }
}
