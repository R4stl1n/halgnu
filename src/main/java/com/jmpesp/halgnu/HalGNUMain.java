package com.jmpesp.halgnu;

import com.jmpesp.halgnu.util.ConfigManager;
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
                        .buildConfiguration();

                PircBotX myBot = new PircBotX(config);
                myBot.startBot();
            }
        }
    }
}
