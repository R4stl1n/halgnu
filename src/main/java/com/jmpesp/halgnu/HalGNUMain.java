package com.jmpesp.halgnu;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

/**
 * Created by r4stl1n on 2/8/2015.
 */
public class HalGNUMain {

    public static void main(String[] args) throws Exception {
        Configuration<PircBotX> config = new Configuration.Builder()
                .setName("HalGNU")
                .setLogin("HalGNU")
                .setAutoNickChange(true)
                .setServer("irc.freenode.net", 6667)
                .addAutoJoinChannel("#HalGNUTest")
                .buildConfiguration();

        PircBotX myBot = new PircBotX(config);
        myBot.startBot();
    }
}
