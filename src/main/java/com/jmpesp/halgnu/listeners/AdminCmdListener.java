package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.models.MemberModel;
import com.jmpesp.halgnu.util.AdminCmdHelper;
import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.util.PermissionHelper;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminCmdListener extends ListenerAdapter {

    private String m_kickCommand = ".kick";
    private String m_topicCommand = ".topic";

    private List<MemberModel.MemberStatus> neededKickPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN
            ));

    private List<MemberModel.MemberStatus> neededTopicPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));
    
    public static void sendHelpMsg(GenericMessageEvent event) {
        event.getBot().sendIRC().message(event.getUser().getNick(), ".kick <user> - Kick user from channel");
        event.getBot().sendIRC().message(event.getUser().getNick(), ".topic <topic> - Change room topic");
    }

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        if (event.getMessage().startsWith(m_kickCommand)) {
            handleKickCommand(event);
        }
        
        if (event.getMessage().startsWith(m_topicCommand)) {
            handleTopicCommand(event);
        }
    }
    
    private void handleKickCommand(GenericMessageEvent event) {
        if(PermissionHelper.HasPermissionFromList(neededKickPermissions, event.getUser().getNick())) {
            if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                AdminCmdHelper.kickUserFromRoom(CommandHelper.removeCommandFromString(event.getMessage()), "Admin Request");
            } else {
                event.respond("Ex: " + m_kickCommand + " <user>");
            }
        } else {
            event.respond("Permission Denied");
        }
    }
    
    private void handleTopicCommand(GenericMessageEvent event) {
        if(PermissionHelper.HasPermissionFromList(neededTopicPermissions, event.getUser().getNick())) {
            if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 0)) {
                AdminCmdHelper.changeTopic(CommandHelper.removeCommandFromString(event.getMessage()));
            } else {
                event.respond("Ex: " + m_topicCommand + " <topic>");
            }
        } else {
            event.respond("Permission Denied");
        }
    }
}
