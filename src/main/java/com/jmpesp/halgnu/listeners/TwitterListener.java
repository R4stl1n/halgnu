package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.models.MemberModel;
import com.jmpesp.halgnu.tasks.TwitterTask;
import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.managers.ConfigManager;
import com.jmpesp.halgnu.util.PermissionHelper;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class TwitterListener extends ListenerAdapter {

    private String m_command = ".tweet";

    private List<MemberModel.MemberStatus> neededPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));

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

        m_timer.scheduleAtFixedRate(new TwitterTask(), 1*60*1000, 1*60*1000);
    }

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        if (event.getMessage().startsWith(m_command)) {
            if(PermissionHelper.HasPermissionFromList(neededPermissions, event.getUser().getNick())) {
                if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                    if (tweetMessage(CommandHelper.removeCommandFromString(event.getMessage()))) {
                        event.respond("Tweeted");
                    } else {
                        event.respond("Error occurred when sending tweet.");
                    }
                } else {
                    event.respond("Ex: " + m_command + "");
                }
            } else {
                event.respond("Permission denied");
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
