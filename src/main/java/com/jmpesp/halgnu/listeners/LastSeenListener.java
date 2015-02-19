package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.models.MemberModel;
import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.util.PermissionHelper;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.security.Timestamp;
import java.util.*;

public class LastSeenListener extends ListenerAdapter {

    private String m_command = ".lastseen";

    private List<MemberModel.MemberStatus> m_neededPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));

    private Map<String, String> m_lastSeen = new HashMap<String, String>();

    public static void sendHelpMsg(GenericMessageEvent event) {
        event.getBot().sendIRC().message(event.getUser().getNick(), ".lastseen <user> - Shows when a user last said something");
    }

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        if(!event.getMessage().startsWith(m_command)) {
            Date date = new Date(event.getTimestamp());

            if (m_lastSeen.containsKey(event.getUser().getNick())) {
                m_lastSeen.remove(event.getUser().getNick());
                m_lastSeen.put(event.getUser().getNick(), date.toString()
                        + ":<" + event.getUser().getNick()+"> " + event.getMessage());
            } else {
                m_lastSeen.put(event.getUser().getNick(), date.toString()
                        + ":<" + event.getUser().getNick()+"> " + event.getMessage());
            }
        }

        if (event.getMessage().startsWith(m_command)) {
            if (PermissionHelper.HasPermissionFromList(m_neededPermissions, event.getUser().getNick())) {
                if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                    String check = CommandHelper.removeCommandFromString(event.getMessage()).trim();

                    if (m_lastSeen.containsKey(check)) {
                        event.respond(m_lastSeen.get(check));
                    } else {
                        event.respond("No record found.");
                    }
                } else {
                    event.respond("Ex. "+m_command+" <user>");
                }
            } else {
                event.respond("Permission denied");
            }
        }
    }
}