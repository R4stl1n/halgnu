package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.managers.DatabaseManager;
import com.jmpesp.halgnu.models.ActivityModel;
import com.jmpesp.halgnu.models.MemberModel;
import com.jmpesp.halgnu.tasks.InactivityTask;
import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.util.PermissionHelper;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ActivityListener extends ListenerAdapter {

    private String m_lastSeenCommand = ".lastseen";
    private String m_toggleExemptCommand = ".exempt";

    private List<MemberModel.MemberStatus> m_lastSeenNeededPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));

    private List<MemberModel.MemberStatus> m_exemptNeededPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.ADMIN
            ));

    private Timer m_timer;

    public ActivityListener() {

        m_timer = new Timer();

        m_timer.scheduleAtFixedRate(new InactivityTask(), 2*60*1000, TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));


    }

    public static void sendHelpMsg(GenericMessageEvent event) {
        event.getBot().sendIRC().message(event.getUser().getNick(), ".lastseen <user> - Shows when a user last said something");
        event.getBot().sendIRC().message(event.getUser().getNick(), ".exempt <user> - Exempts a user from the inactivity check");

    }

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        if(!event.getMessage().startsWith(".")) {
            Date date = new Date(event.getTimestamp());
            // Make sure we ignore nickserv messages
            if(event.getUser().getNick().contains("NickServ") == false){
                // Hardcoded for freenode should probably change this to a config.
                if(event.getUser().getNick().contains(".freenode.net") == false) {
                    if (DatabaseManager.getInstance().getActivityByUsername(event.getUser().getNick().trim()) != null) {
                        DatabaseManager.getInstance().updateActivity(event.getUser().getNick().trim(), event.getMessage());
                    } else {
                        DatabaseManager.getInstance().createActivity(event.getUser().getNick(), event.getMessage());
                    }
                }
            }
        }

        if (event.getMessage().startsWith(m_lastSeenCommand)) {
            handleLastSeenCommand(event);
        }

        if (event.getMessage().startsWith(m_toggleExemptCommand)) {
            handleExemptCommand(event);
        }

    }

    public void handleLastSeenCommand(GenericMessageEvent event) {
        if (PermissionHelper.HasPermissionFromList(m_lastSeenNeededPermissions, event.getUser().getNick())) {
            if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                String check = CommandHelper.removeCommandFromString(event.getMessage()).trim();
                try {
                    ActivityModel activityModel = DatabaseManager.getInstance().getActivityByUsername(check);
                    if (activityModel != null) {
                        event.respond(activityModel.getLastActive()+" :<"
                                +activityModel.getUsername()+"> " + activityModel.getLastMessage());
                    } else {
                        event.respond("No record found.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    event.respond("No record found.");
                }

            } else {
                event.respond("Ex. "+ m_lastSeenCommand +" <user>");
            }
        } else {
            event.respond("Permission denied");
        }
    }
    public void handleExemptCommand(GenericMessageEvent event) {
        if (PermissionHelper.HasPermissionFromList(m_exemptNeededPermissions, event.getUser().getNick())) {
            if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                String toggleUser = CommandHelper.removeCommandFromString(event.getMessage()).trim();

                if (DatabaseManager.getInstance().toggleActivityExempt(toggleUser)) {
                    if(DatabaseManager.getInstance().getActivityExempt(toggleUser)) {
                        event.respond(toggleUser + " is now exempt from idle checks");
                    } else {
                        event.respond(toggleUser + " is no longer exempt from idle checks");
                    }
                } else {
                    event.respond("No record found.");
                }

            } else {
                event.respond("Ex. "+ m_toggleExemptCommand +" <user>");
            }
        } else {
            event.respond("Permission denied");
        }
    }
}