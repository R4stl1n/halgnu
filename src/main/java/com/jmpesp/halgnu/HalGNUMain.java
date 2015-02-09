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
                        .setName("HalGNU")
                        .setLogin("HalGNU")
                        .setAutoNickChange(true)
                        .setServer("irc.freenode.net", 6697)
                        .addAutoJoinChannel("#HalGNUTest")
                        .setSocketFactory(SSLSocketFactory.getDefault())
                        .buildConfiguration();

                PircBotX myBot = new PircBotX(config);
                myBot.startBot();
            }
        }
    }
}
