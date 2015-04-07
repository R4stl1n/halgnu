package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.models.MemberModel;
import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.util.PermissionHelper;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpListener extends ListenerAdapter {

    private String m_command = ".help";

    private List<MemberModel.MemberStatus> m_neededPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        if (event.getMessage().startsWith(m_command)) {
            if(PermissionHelper.HasPermissionFromList(m_neededPermissions, event.getUser().getNick())) {
                
                if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                    String help = CommandHelper.removeCommandFromString(event.getMessage()).trim();
                    
                    if(help.equals("bouncer")) {
                        BouncerListener.sendHelpMsg(event);
                    } else if (help.equals("google")) {
                        GoogleSearchListener.sendHelpMsg(event);
                    } else if (help.equals("helloworld")) {
                        HelloWorldListener.sendHelpMsg(event);
                    } else if (help.equals("time")) {
                        TimeListener.sendHelpMsg(event);
                    } else if (help.equals("twitter")) {
                        TwitterListener.sendHelpMsg(event);
                    } else if (help.equals("twitterhandle")) {
                        TwitterHandleListener.sendHelpMsg(event);
                    } else if (help.equals("version")) {
                        VersionListener.sendHelpMsg(event);
                    } else if (help.equals("website")) {
                        WebsiteHeaderListener.sendHelpMsg(event);
                    } else if (help.equals("admin")) {
                        AdminCmdListener.sendHelpMsg(event);
                    } else if (help.equals("shame")) {
                        ShameListener.sendHelpMsg(event);
                    } else if (help.equals("activity")) {
                        ActivityListener.sendHelpMsg(event);
                    } else if (help.equals("tell")) {
                        TellListener.sendHelpMsg(event);
                    } else {
                        event.respond("Help module not found");
                    }
                } else {
                    event.respond("Loaded modules are: activity, admin, bouncer, helloworld, help, shame, tell, time, twitter, twitterhandle, version, website");
                    event.respond("Ex. .help <module>");
                }
                
            }else {
                event.respond("Permission denied");
            }
        }
    }
    
}
