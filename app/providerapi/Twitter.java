package providerapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.LinkedAccount;
import models.twitter.Tweet;
import play.libs.WS;
import securesocial.provider.OAuth1Provider;
import securesocial.provider.ProviderType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class Twitter
{
    private LinkedAccount linkedAccount;

    public Twitter(LinkedAccount linkedAccount)
    {
        this.linkedAccount = linkedAccount;
    }

    public List<Tweet> getTweets()
    {
        List<Tweet> tweets = new ArrayList<Tweet>();

        WS.WSRequest oauth = WS.url("http://api.twitter.com/1/statuses/home_timeline.json")
                               .oauth(OAuth1Provider.createServiceInfo(OAuth1Provider.getPropertiesKey(ProviderType.twitter)),
                                      linkedAccount.token,
                                      linkedAccount.secret);
        WS.HttpResponse httpResponse = oauth.get();
        JsonElement json = httpResponse.getJson();
        if (json.isJsonArray())
        {
            for (JsonElement jsonElement : json.getAsJsonArray())
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonObject user = jsonObject.getAsJsonObject("user");
                Tweet tweet = new Tweet.Builder()
                        .text(jsonObject.get("text").getAsString())
                        .userName(user.get("name").getAsString())
                        .avatarUrl(user.get("profile_image_url").getAsString())
                        .build();
                tweets.add(tweet);
            }

        }

        return tweets;
    }

    public List<Integer> getFriendIds()
    {
        List<Integer> friends = new ArrayList<Integer>();
//        JsonObject jsonResult = WS.url("https://api.twitter.com/1/followers/ids.json?cursor=-1&user_id=?",
//                                       linkedAccount.token)
//                                  .get()
//                                  .getJson()
//                                  .getAsJsonObject();
//
//        if (jsonResult != null)
//        {
//            JsonArray jsonArray = jsonResult.getAsJsonArray("ids");
//            if (jsonArray != null)
//            {
//                for (Iterator<JsonElement> it = jsonArray.iterator(); it.hasNext();)
//                {
//                    friends.add(it.next().getAsInt());
//                }
//            }
//        }

        return friends;
    }
}
