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

// Twitter Handle HalGNU extension by @tprime_
public class TwitterHandleListener extends ListenerAdapter {

    private String m_readTwitter = ".twitter";
    private String m_writeTwitter = ".settwitter";

    public static void sendHelpMsg(GenericMessageEvent event) {
        event.getBot().sendIRC().message(event.getUser().getNick(), ".twitter <user> - Retrieve a user's twitter handle");
        event.getBot().sendIRC().message(event.getUser().getNick(), ".settwitter <your handle> - Update your twitter handle");

    }

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        if (event.getMessage().startsWith(m_readTwitter)) {
            handleReadTwitterCommand(event);
        }

        if (event.getMessage().startsWith(m_writeTwitter)) {
            handleWriteTwitterCommand(event);
        }

    }

    public void handleReadTwitterCommand(GenericMessageEvent event) {
            if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                String check = CommandHelper.removeCommandFromString(event.getMessage()).trim();
                try {
                    MemberModel memberModel = DatabaseManager.getInstance().getMemberByUsername(check);
                    if (memberModel != null) {
                        event.respond(memberModel.getUserName()+"'s twitter handle is: "
                                +memberModel.getTwitterHandle()+" ");
                    } else {
                        event.respond("No record found.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    event.respond("No record found.");
                }
            } else {
                event.respond("Ex. "+ m_readTwitter +" <user>");
            }
    }
   public void handleWriteTwitterCommand(GenericMessageEvent event) {
            if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
            	String nick = event.getUser().getNick();
                String handle = CommandHelper.removeCommandFromString(event.getMessage()).trim();
                MemberModel memberModel = DatabaseManager.getInstance().setTwitterHandleByUsername(nick, handle);
                event.respond("Twitter handle set to: " + handle);
            } else {
                event.respond("Ex. "+ m_writeTwitter +" <your handle>");
            }
    }
}
