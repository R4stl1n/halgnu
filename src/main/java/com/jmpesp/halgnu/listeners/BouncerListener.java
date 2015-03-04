package com.jmpesp.halgnu.listeners;

import com.jmpesp.halgnu.managers.ConfigManager;
import com.jmpesp.halgnu.managers.DatabaseManager;
import com.jmpesp.halgnu.models.MemberModel;
import com.jmpesp.halgnu.util.AdminCmdHelper;
import com.jmpesp.halgnu.util.CommandHelper;
import com.jmpesp.halgnu.util.PermissionHelper;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BouncerListener extends ListenerAdapter {

    private String m_inviteCommand = ".invite";
    private String m_whoInvitedCommand = ".whoinvited";
    private String m_memberStatusCommand = ".memberstatus";
    private String m_statusOfMember = ".statusofmember";
    private String m_enforceCommand = ".enforce";
    private String m_changeStatusCommand = ".changestatus";
    private String m_removeMemberCommand = ".removemember";
    
    private List<MemberModel.MemberStatus> m_neededInvitePermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER
            ));

    private List<MemberModel.MemberStatus> m_neededWhoInvitedPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));

    private List<MemberModel.MemberStatus> m_neededMemberStatusPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));

    private List<MemberModel.MemberStatus> m_neededStatusOfMemberPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.OG,
                    MemberModel.MemberStatus.ADMIN,
                    MemberModel.MemberStatus.MEMBER,
                    MemberModel.MemberStatus.PROSPECT
            ));

    private List<MemberModel.MemberStatus> m_neededChangeStatusPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.ADMIN
            ));

    private List<MemberModel.MemberStatus> m_neededEnforcePermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.ADMIN
            ));

    private List<MemberModel.MemberStatus> m_neededRemoveMemberPermissions =
            new ArrayList<MemberModel.MemberStatus>(Arrays.asList(
                    MemberModel.MemberStatus.ADMIN
            ));
    
    private boolean m_enforce = false;

    public static void sendHelpMsg(GenericMessageEvent event) {
        event.getBot().sendIRC().message(event.getUser().getNick(), ".invite <user> - Used to invite user to room");
        event.getBot().sendIRC().message(event.getUser().getNick(), ".whoinvited <user> - Returns who invited the user");
        event.getBot().sendIRC().message(event.getUser().getNick(),".memberitatus - Returns your member status");
        event.getBot().sendIRC().message(event.getUser().getNick(), ".statusofmember <member> - Returns status of desired member");
        event.getBot().sendIRC().message(event.getUser().getNick(), ".enforce - Enables/Disables bouncer enforcement");
        event.getBot().sendIRC().message(event.getUser().getNick(), ".changestatus <user> <status> - Change users membership status");
        event.getBot().sendIRC().message(event.getUser().getNick(), ".removemember <user> - Removes user from the room");
    }
    
    @Override
    public void onJoin(final JoinEvent join) throws Exception {

        if(m_enforce) {
            if (!(join.getUser().getNick().trim().equals(ConfigManager.getInstance().getIrcNick()))) {

                MemberModel member = DatabaseManager.getInstance().getMemberByUsername(join.getUser().getNick().trim());

                if (member != null) {
                    join.respond("Authorized");
                } else {
                    AdminCmdHelper.kickUserFromRoom(join.getUser().getNick(), "Not-Authorized");
                }

                doEnforcmentCheck(join);
            }
        }
    }

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        // Handle enforce command
        if (event.getMessage().startsWith(m_enforceCommand)) {
            handleEnforceCommand(event);
        }

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
        
        // Handle changeStatus Command
        if (event.getMessage().startsWith(m_changeStatusCommand)) {
            handleChangeStatusCommand(event);
        }
        
        // Handle remove member
        if (event.getMessage().startsWith(m_removeMemberCommand)) {
            handleRemoveMemberCommand(event);
        }
    }
    
    private void handleRemoveMemberCommand(GenericMessageEvent event) {
        if(PermissionHelper.HasPermissionFromList(m_neededRemoveMemberPermissions, event.getUser().getNick())) {
            if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 1)) {
                try {
                    MemberModel member = DatabaseManager.getInstance().getMemberByUsername(CommandHelper.removeCommandFromString(event.getMessage()).trim());
                    if (member != null) {
                        DatabaseManager.getInstance().getMemberDao().delete(member);
                        event.respond("User removed from database");
                        AdminCmdHelper.kickUserFromRoom(CommandHelper.removeCommandFromString(event.getMessage()).trim(), "Membership_Revoked");
                        
                    } else {
                        event.respond("User does not exist");
                    }
                } catch (SQLException e) {
                    event.respond("User does not exist");
                }
            } else {
                event.respond("Ex: " + m_removeMemberCommand + " <user>");
            }
        } else {
            event.respond("Permission denied");
        }
    }
    
    private void handleChangeStatusCommand(GenericMessageEvent event) {
        if(PermissionHelper.HasPermissionFromList(m_neededChangeStatusPermissions, event.getUser().getNick())) {
            if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 2)) {
                String arguments = CommandHelper.removeCommandFromString(event.getMessage());
                String[] splitArguments = arguments.split(" ");
                
                if(splitArguments.length == 2 ) {
                    try {
                        MemberModel member = DatabaseManager.getInstance().getMemberByUsername(splitArguments[0]);
                        
                        if(member != null) {
                            String status = splitArguments[1];
                            boolean success = false;
                            
                            if(status.equals("og")) {
                                success = true;
                                member.setMemberStatus(MemberModel.MemberStatus.OG);
                                DatabaseManager.getInstance().getMemberDao().update(member);
                            } else if (status.equals("admin")) {
                                success = true;
                                member.setMemberStatus(MemberModel.MemberStatus.ADMIN);
                                DatabaseManager.getInstance().getMemberDao().update(member);
                            } else if (status.equals("member")) {
                                success = true;
                                member.setMemberStatus(MemberModel.MemberStatus.MEMBER);
                                DatabaseManager.getInstance().getMemberDao().update(member);
                            } else if (status.equals("prospect")) {
                                success = true;
                                member.setMemberStatus(MemberModel.MemberStatus.PROSPECT);
                                DatabaseManager.getInstance().getMemberDao().update(member);
                            } else {
                                event.respond("Unknown status detected");
                            }
                            
                            if(success) {
                                event.respond("Status update completed");
                            }
                            
                        } else {
                            event.respond("Member not found");
                        }
                        
                    } catch (SQLException e) {
                        e.printStackTrace();
                        event.respond("Issue with query");
                    }

                } else {
                    event.respond("Ex. <username> <status> | admin,og,member,prospect 2");
                }
            } else {
                event.respond("Ex. <username> <status> | admin,og,member,prospect 1");
            }
        }
        else {
            event.respond("Permission denied");
        }
    }

    private void handleEnforceCommand(GenericMessageEvent event) {
        int numKicked = 0;

        if(PermissionHelper.HasPermissionFromList(m_neededEnforcePermissions, event.getUser().getNick())) {
            if (CommandHelper.checkForAmountOfArgs(event.getMessage(), 0)) {

                m_enforce = !m_enforce;

                if(m_enforce) {
                    event.respond("Activating enforcement mode");

                    event.respond("Scanning room");
                    doEnforcmentCheck(event);
                    event.respond("Scan complete");

                } else {
                    event.respond("Deactivating enforcement mode");
                }
            }
        } else {
            event.respond("Permission denied");
        }
    }

    private void doEnforcmentCheck(GenericMessageEvent event) {
        int numKicked = 0;

        for (User user : event.getBot().getUserBot().getChannels().first().getUsers()) {
            if(!(user.getNick().trim().equals(ConfigManager.getInstance().getIrcNick()))) {
                try {
                    MemberModel member = DatabaseManager.getInstance().getMemberByUsername(user.getNick().trim());
                    if (member == null) {
                        AdminCmdHelper.kickUserFromRoom(user.getNick(), "Not-Authorized");
                        numKicked += 1;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if(numKicked == 1) {
            event.respond("Removed " + numKicked + " user");
        } else {
            event.respond("Removed " + numKicked + " users");
        }
    }

    private void doEnforcmentCheck(JoinEvent event) {
        int numKicked = 0;

        for (User user : event.getBot().getUserBot().getChannels().first().getUsers()) {
            if(!(user.getNick().trim().equals(ConfigManager.getInstance().getIrcNick()))) {
                try {
                    MemberModel member = DatabaseManager.getInstance().getMemberByUsername(user.getNick().trim());
                    if (member == null) {
                        AdminCmdHelper.kickUserFromRoom(user.getNick(), "Not-Authorized");
                        numKicked += 1;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleInviteCommand(GenericMessageEvent event) {
        if(PermissionHelper.HasPermissionFromList(m_neededInvitePermissions, event.getUser().getNick())) {
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

    private void handleWhoInvitedCommand(GenericMessageEvent event) {
        if(PermissionHelper.HasPermissionFromList(m_neededWhoInvitedPermissions, event.getUser().getNick())) {

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

    private void handleMemberStatusCommand(GenericMessageEvent event) {
        if(PermissionHelper.HasPermissionFromList(m_neededMemberStatusPermissions, event.getUser().getNick())) {

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

    private void handleStatusOfMemberCommand(GenericMessageEvent event) {
        // Handle statusOfMember Command
        if (event.getMessage().startsWith(m_statusOfMember)) {
            if(PermissionHelper.HasPermissionFromList(m_neededStatusOfMemberPermissions, event.getUser().getNick())) {

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
