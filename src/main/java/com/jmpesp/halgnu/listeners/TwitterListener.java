package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.managers.IRCConnectionManager;
import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.managers.ConfigManager;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by r4stl1n on 2/11/15.
 */
public class TwitterListener extends ListenerAdapter {

    private String m_command = ".tweet";

    private ConfigurationBuilder m_configBuilder;
    private TwitterFactory m_twitterFactory;
    private Twitter m_twitter;

    private Timer m_timer;

    public TwitterListener() {
        m_timer = new Timer();
        m_configBuilder = new ConfigurationBuilder();
        m_configBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(ConfigManager.getInstance().getTwitterConsumerKey())
                .setOAuthConsumerSecret(ConfigManager.getInstance().getTwitterComsumerSecret())
                .setOAuthAccessToken(ConfigManager.getInstance().getTwitterAccessToken())
                .setOAuthAccessTokenSecret(ConfigManager.getInstance().getTwitterAccessSecret());
        m_twitterFactory = new TwitterFactory(m_configBuilder.build());
        m_twitter = m_twitterFactory.getInstance();

        m_timer.scheduleAtFixedRate(new TimerTask() {

            private String pastTweet = "";

            @Override
            public void run() {
                ConfigurationBuilder m_configBuilder;
                TwitterFactory m_twitterFactory;
                Twitter m_twitter;

                m_configBuilder = new ConfigurationBuilder();
                m_configBuilder.setDebugEnabled(true)
                        .setOAuthConsumerKey(ConfigManager.getInstance().getTwitterConsumerKey())
                        .setOAuthConsumerSecret(ConfigManager.getInstance().getTwitterComsumerSecret())
                        .setOAuthAccessToken(ConfigManager.getInstance().getTwitterAccessToken())
                        .setOAuthAccessTokenSecret(ConfigManager.getInstance().getTwitterAccessSecret());
                m_twitterFactory = new TwitterFactory(m_configBuilder.build());
                m_twitter = m_twitterFactory.getInstance();

                try {
                    User user = m_twitter.verifyCredentials();
                    List<Status> statuses = m_twitter.getHomeTimeline();

                    if(!(statuses.get(0).getText().equals(pastTweet))) {

                        if (statuses.get(0).getText().contains(user.getScreenName())) {

                            String completeMessage = "@" + statuses.get(0).getUser().getScreenName() + ": "
                                    + statuses.get(0).getText();
                            IRCConnectionManager.getInstance().getBotConnection().sendIRC()
                                    .message(ConfigManager.getInstance().getIrcChannel(), completeMessage);

                            pastTweet = statuses.get(0).getText();
                        }
                    }
                } catch (TwitterException e) {
                    e.printStackTrace();
                }

                // Your database code here
            }
        }, 1*60*1000, 1*60*1000);
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
