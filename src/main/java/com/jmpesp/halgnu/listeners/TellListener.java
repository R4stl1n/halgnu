package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.managers.ConfigManager;
import com.jmpesp.halgnu.models.MemberModel;
import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.util.PermissionHelper;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.*;

public class TellListener  extends ListenerAdapter {

    private String m_command = ".tell";

    private List<MemberModel.MemberStatus> m_neededPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));

    private Map<String, ArrayList<String>> m_tells = new HashMap<String, ArrayList<String>>();

    public static void sendHelpMsg(GenericMessageEvent event) {
        event.getBot().sendIRC().message(event.getUser().getNick(), ".tell <user> <msg> - Have "
                + ConfigManager.getInstance().getIrcNick()+" relay a message next time user is active");
    }

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {
        
        if(!event.getMessage().startsWith(m_command)) {
            if(m_tells.containsKey(event.getUser().getNick().trim())) {
                for(String data: m_tells.get(event.getUser().getNick())) {
                    System.out.println(data);

                    event.getBot().sendIRC().message(event.getUser().getNick(),data);
                }

                m_tells.get(event.getUser().getNick()).clear();
            }
        }

        if (event.getMessage().startsWith(m_command)) {
            if (PermissionHelper.HasPermissionFromList(m_neededPermissions, event.getUser().getNick())) {

                if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 2)) {
                    String fullMsg = CommandHelper.removeCommandFromString(event.getMessage());
                    String target = CommandHelper.removeCommandFromString(event.getMessage()).trim().split(" ")[0];
                    String data = CommandHelper.removeCommandFromString(fullMsg);

                    if(m_tells.containsKey(target)) {
                        m_tells.get(target).add("Message From: <" + event.getUser().getNick() + "> " + data);
                    } else {
                        m_tells.put(target, new ArrayList<String>());
                        m_tells.get(target).add("Message From: <" + event.getUser().getNick() + "> " + data);
                    }

                    event.respond("Noted");
                } else {
                    event.respond("Ex. "+m_command+" <user> <msg>");
                }
            } else {
                event.respond("Permission Denied");
            }
        }
    }
}
