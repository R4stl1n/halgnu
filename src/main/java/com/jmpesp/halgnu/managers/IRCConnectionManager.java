package com.jmpesp.halgnu.managers;

import com.jmpesp.halgnu.listeners.*;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;

/**
 * Created by r4stl1n on 2/11/15.
 */
public class IRCConnectionManager {

    private static IRCConnectionManager m_instance;
    private Configuration<PircBotX> m_config;
    private PircBotX m_ircBot;

    protected IRCConnectionManager() {
        m_config = new Configuration.Builder()
                .setName(ConfigManager.getInstance().getIrcNick())
                .setLogin(ConfigManager.getInstance().getIrcNick())
                .setNickservPassword(ConfigManager.getInstance().getIrcPassword())
                .setAutoNickChange(true)
                .setServer(ConfigManager.getInstance().getIrcServer(), ConfigManager.getInstance().getIrcPort())
                .setSocketFactory(SSLSocketFactory.getDefault())
                .addListener(new HelpListener())
                .addListener(new TimeListener())
                .addListener(new BouncerListener())
                .addListener(new VersionListener())
                .addListener(new TwitterListener())
                .addListener(new AdminCmdListener())
                .addListener(new HelloWorldListener())
                .addListener(new GoogleSearchListener())
                .addListener(new WebsiteHeaderListener())
                .addAutoJoinChannel(ConfigManager.getInstance().getIrcChannel())
                .buildConfiguration();

    }

    public static IRCConnectionManager getInstance() {
        if(m_instance == null) {
            m_instance = new IRCConnectionManager();
        }
        return m_instance;
    }

    public PircBotX getBotConnection() {
        return m_ircBot;
    }

    public boolean startConnection() {
        m_ircBot = new PircBotX(m_config);

        try {
            m_ircBot.startBot();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (IrcException e) {
            e.printStackTrace();
            return false;
        }
    }
}
