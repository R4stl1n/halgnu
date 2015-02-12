package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.models.MemberModel;
import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.util.PermissionHelper;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BouncerListener extends ListenerAdapter {

    private String m_command = ".invite";
    private String m_command2 = ".invitereg";
    private String m_command3 = ".whoinvited";

    private List<MemberModel.MemberStatus> neededPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));


    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        // Handle Invite Command
        if (event.getMessage().startsWith(m_command)) {
            if(PermissionHelper.HasPermissionFromList(neededPermissions, event.getUser().getNick())) {

                if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                    event.respond("HalGNU V1.0 - Daisy");
                } else {
                    event.respond("Ex: " + m_command + " <usernamehere>");
                }
            } else {
                event.respond("Permission denied");
            }
        }

        // Handle InviteReg Command
        if (event.getMessage().startsWith(m_command2)) {

            if(PermissionHelper.HasPermissionFromList(neededPermissions, event.getUser().getNick())) {

                if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                    event.respond("HalGNU V1.0 - Daisy");
                } else {
                    event.respond("Ex: " + m_command2 + " <usernamehere>");
                }
            } else {
                event.respond("Permission denied");
            }
        }

        // Handle WhoIvited Command
        if (event.getMessage().startsWith(m_command3)) {
            if(PermissionHelper.HasPermissionFromList(neededPermissions, event.getUser().getNick())) {

                if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                    event.respond("HalGNU V1.0 - Daisy");
                } else {
                    event.respond("Ex: " + m_command3 + " <usernamehere>");
                }
            } else {
                event.respond("Permission denied");
            }
        }
    }
}
