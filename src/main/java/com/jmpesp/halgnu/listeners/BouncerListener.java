package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.managers.DatabaseManager;
import com.jmpesp.halgnu.models.MemberModel;
import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.util.PermissionHelper;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BouncerListener extends ListenerAdapter {

    private String m_inviteCommand = ".invite";
    private String m_whoInvitedCommand = ".whoInvited";
    private String m_memberStatusCommand = ".memberStatus";
    private String m_statusOfMember = ".statusOfMember";

    private List<MemberModel.MemberStatus> neededInvitePermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER
            ));

    private List<MemberModel.MemberStatus> neededWhoInvitedPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));

    private List<MemberModel.MemberStatus> neededMemberStatusPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));

    private List<MemberModel.MemberStatus> neededStatusOfMemberPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));
    
    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        // Handle Invite Command
        if (event.getMessage().startsWith(m_inviteCommand)) {
            handleInviteCommand(event);
        }

        // Handle WhoIvited Command
        if (event.getMessage().startsWith(m_whoInvitedCommand)) {
            handleWhoInvitedCommand(event);
        }

        // Handle MemberStatus Command
        if (event.getMessage().startsWith(m_memberStatusCommand)) {
            handleMemberStatusCommand(event);
        }

        // Handle statusOfMember Command
        if (event.getMessage().startsWith(m_statusOfMember)) {
            handleStatusOfMemberCommand(event);
        }
    }
    

    public void handleInviteCommand(GenericMessageEvent event) {
        if(PermissionHelper.HasPermissionFromList(neededInvitePermissions, event.getUser().getNick())) {
            if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                if(DatabaseManager.getInstance().createMember(CommandHelper.removeCommandFromString(event.getMessage()).trim()
                        ,event.getUser().getNick())) {
                    event.respond("User added to registry");
                } else {
                    event.respond("User already in registry");
                }
            } else {
                event.respond("Ex: " + m_inviteCommand + " <usernamehere>");
            }
        } else {
            event.respond("Permission denied");
        }
    }
    
    public void handleWhoInvitedCommand(GenericMessageEvent event) {
        if(PermissionHelper.HasPermissionFromList(neededWhoInvitedPermissions, event.getUser().getNick())) {

            if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                MemberModel member = null;
                try {
                    member = DatabaseManager.getInstance()
                            .getMemberByUsername(CommandHelper.removeCommandFromString(event.getMessage()).trim());

                    if(member != null) {
                        event.respond(member.getInvitedBy() + " invited " + member.getUserName() + " on " + member.getDateInvited());
                    } else {
                        event.respond("User not in registry");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    event.respond("User not in registry");
                }
            } else {
                event.respond("Ex: " + m_whoInvitedCommand + " <usernamehere>");
            }
        } else {
            event.respond("Permission denied");
        }
    }
    
    public void handleMemberStatusCommand(GenericMessageEvent event) {
        if(PermissionHelper.HasPermissionFromList(neededMemberStatusPermissions, event.getUser().getNick())) {

            if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 0)) {
                MemberModel member = null;
                try {
                    member = DatabaseManager.getInstance()
                            .getMemberByUsername(event.getUser().getNick().trim());

                    if(member != null) {
                        if(member.getMemberStatus().equals(MemberModel.MemberStatus.OG)) {
                            event.respond("Your member status is: OG");
                        }

                        if(member.getMemberStatus().equals(MemberModel.MemberStatus.ADMIN)) {
                            event.respond("Your member status is: ADMIN");
                        }

                        if(member.getMemberStatus().equals(MemberModel.MemberStatus.MEMBER)) {
                            event.respond("Your member status is: MEMBER");
                        }

                        if(member.getMemberStatus().equals(MemberModel.MemberStatus.PROSPECT)) {
                            event.respond("Your member status is: PROSPECT");
                        }
                    } else {
                        event.respond("User not in registry");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    event.respond("User not in registry");
                }
            } else {
                event.respond("Ex: " + m_whoInvitedCommand + " <usernamehere>");
            }
        } else {
            event.respond("Permission denied");
        }
    }
    
    public void handleStatusOfMemberCommand(GenericMessageEvent event) {
        // Handle statusOfMember Command
        if (event.getMessage().startsWith(m_statusOfMember)) {
            if(PermissionHelper.HasPermissionFromList(neededStatusOfMemberPermissions, event.getUser().getNick())) {

                if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                    MemberModel member = null;
                    try {
                        member = DatabaseManager.getInstance()
                                .getMemberByUsername(CommandHelper.removeCommandFromString(event.getMessage()).trim());

                        if(member != null) {
                            if(member.getMemberStatus().equals(MemberModel.MemberStatus.OG)) {
                                event.respond(member.getUserName()+"'s member status is: OG");
                            }

                            if(member.getMemberStatus().equals(MemberModel.MemberStatus.ADMIN)) {
                                event.respond(member.getUserName()+"'s member status is: ADMIN");
                            }

                            if(member.getMemberStatus().equals(MemberModel.MemberStatus.MEMBER)) {
                                event.respond(member.getUserName()+"'s member status is: MEMBER");
                            }

                            if(member.getMemberStatus().equals(MemberModel.MemberStatus.PROSPECT)) {
                                event.respond(member.getUserName()+"'s member status is: PROSPECT");
                            }
                        } else {
                            event.respond("User not in registry");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        event.respond("User not in registry");
                    }
                } else {
                    event.respond("Ex: " + m_whoInvitedCommand + " <usernamehere>");
                }
            } else {
                event.respond("Permission denied");
            }
        }
    }
}
