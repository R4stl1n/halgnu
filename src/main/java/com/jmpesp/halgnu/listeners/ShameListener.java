package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.managers.ConfigManager;
import com.jmpesp.halgnu.models.MemberModel;
import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.util.PermissionHelper;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ShameListener extends ListenerAdapter {

    private String m_command = ".shame";

    private List<MemberModel.MemberStatus> m_neededPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));

    private String[] m_shameSayings = new String[]{"You should be ashamed of your self and your family, %XXXX%",
            "You're an awful human being and deserve aids, %XXXX%",
            "Jesus Christ, %XXXX%. I didn't know anyone could be that dumb.",
            "Go kill yourself, %XXXX%",
            "You are a sad, sorry little man and you have my pity, %XXXX%",
            "You look like something I'd draw with my left hand, %XXXX%"};

    public static void sendHelpMsg(GenericMessageEvent event) {
        event.getBot().sendIRC().message(event.getUser().getNick(), ".shame <user> - Shames a user");
    }

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {
        if (event.getMessage().startsWith(m_command)) {
            if (PermissionHelper.HasPermissionFromList(m_neededPermissions, event.getUser().getNick())) {

                if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                    int saying = randInt(0,m_shameSayings.length - 1);
                    String target = CommandHelper.removeCommandFromString(event.getMessage());
                    event.getBot().sendIRC().message(ConfigManager.getInstance().getIrcChannel(),
                            m_shameSayings[saying].replace("%XXXX%", target));

                } else {
                    event.respond("Ex. "+m_command+" <user>");
                }
            } else {
                event.respond("Permission Denied");
            }
        }
    }

    private static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}