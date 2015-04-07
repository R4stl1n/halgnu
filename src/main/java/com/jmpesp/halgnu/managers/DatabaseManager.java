package com.jmpesp.halgnu.managers;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.jmpesp.halgnu.models.ActivityModel;
import com.jmpesp.halgnu.models.MemberModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;

public class DatabaseManager {
    private final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    private static DatabaseManager m_instance;
    private static ConnectionSource m_connectionSource;
    
    private static Dao<MemberModel, String> m_memberDao;
    private static Dao<ActivityModel, String> m_activityDao;

    protected DatabaseManager() {
        String databaseUrl = "jdbc:sqlite:halgnu.db";

        try {
            m_connectionSource = new JdbcConnectionSource(databaseUrl);
            
            // Create daos
            m_memberDao = DaoManager.createDao(m_connectionSource, MemberModel.class);
            m_activityDao = DaoManager.createDao(m_connectionSource, ActivityModel.class);

            TableUtils.createTableIfNotExists(m_connectionSource, MemberModel.class);
            TableUtils.createTableIfNotExists(m_connectionSource, ActivityModel.class);

            // Check if tables need to be created
            CheckIfAdminExists();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public Dao<MemberModel, String> getMemberDao() {
        return m_memberDao;
    }
    public Dao<ActivityModel, String> getActivityDao() { return m_activityDao; }

    public boolean createActivity(String username, String msg) {
        try {
            if(getActivityByUsername(username) == null) {
                Date date = new Date();
                ActivityModel activityModel = new ActivityModel();
                activityModel.setUsername(username);
                activityModel.setLastMessage(msg);
                activityModel.setLastActive(date.toString());

                try {
                    m_activityDao.create(activityModel);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public boolean getActivityExempt(String username) {
        try {
            ActivityModel activityModel = getActivityByUsername(username);
            if(activityModel != null) {
                return activityModel.getExempt();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public boolean toggleActivityExempt(String username) {
        try {
            ActivityModel activityModel = getActivityByUsername(username);
            if(activityModel != null) {

                activityModel.setExempt(!activityModel.getExempt());

                try {
                    m_activityDao.update(activityModel);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public boolean deleteActivity(String username) {
        try {
            ActivityModel activityModel = getActivityByUsername(username);
            if(activityModel != null) {

                try {
                    m_activityDao.delete(activityModel);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public boolean updateActivity(String username, String msg) {
        try {
            ActivityModel activityModel = getActivityByUsername(username);
            if(activityModel != null) {
                Date date = new Date();
                activityModel.setLastMessage(msg);
                activityModel.setLastActive(date.toString());

                try {
                    m_activityDao.update(activityModel);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public boolean createMember(String username, String invitedBy) {
        try {
            if(getMemberByUsername(username) == null) {
                Date date = new Date();
                MemberModel memberModel = new MemberModel();

                memberModel.setUserName(username);
                memberModel.setInvitedBy(invitedBy);
                memberModel.setMemberStatus(MemberModel.MemberStatus.PROSPECT);
                memberModel.setDateInvited(date.toString());

                try {
                    m_memberDao.create(memberModel);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        return false;
    }

    public boolean createMember(String username, String invitedBy, MemberModel.MemberStatus memberStatus) {
        try {
            if(getMemberByUsername(username) == null) {
                Date date = new Date();
                MemberModel memberModel = new MemberModel();

                memberModel.setUserName(username);
                memberModel.setInvitedBy(invitedBy);
                memberModel.setMemberStatus(memberStatus);
                memberModel.setDateInvited(date.toString());

                try {
                    m_memberDao.create(memberModel);
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeMemberByUsername(String username) throws SQLException {
        MemberModel memberModel = m_memberDao.queryForId(username);

        if(memberModel != null) {
            m_memberDao.delete(memberModel);

            return true;
        }

        return false;
    }

    public MemberModel setTwitterHandleByUsername(String username, String handle) {
        try {
            MemberModel memberModel = getMemberByUsername(username);
            if(memberModel != null) {

                memberModel.setTwitterHandle(handle);

                try {
                    m_memberDao.update(memberModel);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public MemberModel getMemberByUsername(String username) throws SQLException {
        MemberModel memberModel;

        memberModel = m_memberDao.queryForId(username);

        return memberModel;
    }

    public ActivityModel getActivityByUsername(String username) throws SQLException {
        ActivityModel activityModel;

        activityModel = m_activityDao.queryForId(username);

        return activityModel;
    }
    
    private void CheckIfAdminExists() throws SQLException {

        if(getMemberByUsername(ConfigManager.getInstance().getIrcOwner()) == null) {
            logger.info("Admin user not found");
            // Create owner in table
            createMember(ConfigManager.getInstance().getIrcOwner(),
                    ConfigManager.getInstance().getIrcNick(), MemberModel.MemberStatus.ADMIN);
            logger.info("Admin user added");

        }

        logger.info("Admin user found");
    }
    
    public static DatabaseManager getInstance() {
        if(m_instance == null) {
            m_instance = new DatabaseManager();
        }
        return m_instance;
    }
}
