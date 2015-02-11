package com.jmpesp.halgnu;

import com.jmpesp.halgnu.listeners.*;
import com.jmpesp.halgnu.util.ConfigManager;
import com.jmpesp.halgnu.util.DatabaseManager;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

import javax.net.ssl.SSLSocketFactory;

public class HalGNUMain {

    public static void main(String[] args) throws Exception {

        if(ConfigManager.getInstance().checkConfigExist()) {

            if(ConfigManager.getInstance().loadConfigurationFile()) {
                Configuration<PircBotX> config = new Configuration.Builder()
                        .setName(ConfigManager.getInstance().getIrcNick())
                        .setLogin(ConfigManager.getInstance().getIrcNick())
                        .setNickservPassword(ConfigManager.getInstance().getIrcPassword())
                        .setAutoNickChange(true)
                        .setServer(ConfigManager.getInstance().getIrcServer(), ConfigManager.getInstance().getIrcPort())
                        .addAutoJoinChannel(ConfigManager.getInstance().getIrcChannel())
                        .setSocketFactory(SSLSocketFactory.getDefault())
                        .addListener(new HelpListener())
                        .addListener(new TimeListener())
                        .addListener(new VersionListener())
                        .addListener(new HelloWorldListener())
                        .addListener(new GoogleSearchListener())
                        .addListener(new WebsiteHeaderListener())
                        .buildConfiguration();

                PircBotX myBot = new PircBotX(config);
                myBot.startBot();
            }
        }

        DatabaseManager.getInstance();
    }
}
