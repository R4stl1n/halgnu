package com.jmpesp.halgnu.managers;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.jmpesp.halgnu.models.MemberModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager {
    private final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    private static DatabaseManager m_instance;
    private static ConnectionSource m_connectionSource;
    
    private static Dao<MemberModel, String> m_memberDao;

    protected DatabaseManager() {
        String databaseUrl = "jdbc:sqlite:halgnu.db";

        try {
            m_connectionSource = new JdbcConnectionSource(databaseUrl);
            
            // Create daos
            m_memberDao = DaoManager.createDao(m_connectionSource, MemberModel.class);
            
            // Check if tables need to be created
            CheckDatabaseExists();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
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
    
    public MemberModel getMemberByUsername(String username) throws SQLException {
        MemberModel memberModel;

        memberModel = m_memberDao.queryForId(username);

        return memberModel;
    }
    
    private void CheckDatabaseExists() throws SQLException {
        File configFile = new File("halgnu.db");

        if(!configFile.exists()) {
            logger.info("Database not found creating new one");
            TableUtils.createTable(m_connectionSource, MemberModel.class);

            logger.info("Added owner to database");

            // Create owner in table
            createMember(ConfigManager.getInstance().getIrcOwner(),
                    ConfigManager.getInstance().getIrcNick(), MemberModel.MemberStatus.OG);
        }

        logger.info("Database found");
    }
    
    public static DatabaseManager getInstance() {
        if(m_instance == null) {
            m_instance = new DatabaseManager();
        }
        return m_instance;
    }
}
