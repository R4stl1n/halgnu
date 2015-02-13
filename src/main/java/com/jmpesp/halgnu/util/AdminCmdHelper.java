package com.jmpesp.halgnu.util;

import com.jmpesp.halgnu.managers.ConfigManager;
import com.jmpesp.halgnu.managers.IRCConnectionManager;

public class AdminCmdHelper {

    public static void changeTopic(String topic) {
        String command = "topic "+ConfigManager.getInstance().getIrcChannel()+" ";

        if(!topic.isEmpty()) {
            IRCConnectionManager.getInstance().getBotConnection().sendRaw().rawLine(command + topic);
        }
    }

    public static void kickUserFromRoom(String nick, String reason) {
        String command = "kick "+ConfigManager.getInstance().getIrcChannel()+" ";
        if(!nick.isEmpty()) {
            IRCConnectionManager.getInstance().getBotConnection().sendRaw().rawLine(command+nick+" "+reason);
        }
    }

    public static void banUserFromRoom(String nick) {
        String command = "kickban "+ConfigManager.getInstance().getIrcChannel()+" ";
        if(!nick.isEmpty()) {
            IRCConnectionManager.getInstance().getBotConnection().sendIRC()
                    .message(ConfigManager.getInstance().getIrcChannel(), command+nick);
        }
    }
}
