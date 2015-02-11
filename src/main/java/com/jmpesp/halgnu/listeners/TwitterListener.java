package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.util.ConfigManager;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by r4stl1n on 2/11/15.
 */
public class TwitterListener extends ListenerAdapter {

    private String m_command = ".tweet";

    private ConfigurationBuilder m_configBuilder;
    private TwitterFactory m_twitterFactory;
    private Twitter m_twitter;

    public TwitterListener() {
        m_configBuilder = new ConfigurationBuilder();
        m_configBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(ConfigManager.getInstance().getTwitterConsumerKey())
                .setOAuthConsumerSecret(ConfigManager.getInstance().getTwitterComsumerSecret())
                .setOAuthAccessToken(ConfigManager.getInstance().getTwitterAccessToken())
                .setOAuthAccessTokenSecret(ConfigManager.getInstance().getTwitterAccessSecret());
        m_twitterFactory = new TwitterFactory(m_configBuilder.build());
        m_twitter = m_twitterFactory.getInstance();
    }

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        if (event.getMessage().startsWith(m_command)) {
            if(CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                if(tweetMessage(CommandHelper.removeCommandFromString(event.getMessage())))
                {
                    event.respond("Tweeted");
                }
                else {
                    event.respond("Error occurred when sending tweet.");
                }
            } else {
                event.respond("Ex: "+m_command+"");
            }
        }
    }

    private boolean tweetMessage(String message) {
        try {
            m_twitter.updateStatus(message);
            return true;
        } catch (TwitterException e) {
            e.printStackTrace();
            return false;
        }
    }
}
