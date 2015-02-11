package com.jmpesp.halgnu.tasks;

import com.jmpesp.halgnu.managers.ConfigManager;
import com.jmpesp.halgnu.managers.IRCConnectionManager;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.TimerTask;

public class TwitterTask extends TimerTask{

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
    }
}
