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

public class DatabaseManager {

    private static DatabaseManager m_instance;
    private static ConnectionSource m_connectionSource;
    
    private static Dao<MemberModel, String> m_memberDao;

    protected DatabaseManager() {
        // this uses h2 by default but change to match your database
        String databaseUrl = "jdbc:sqlite:halgnu.db";
        // create a connection source to our database
        try {
            m_connectionSource = new JdbcConnectionSource(databaseUrl);
            
            // Create dao
            m_memberDao = DaoManager.createDao(m_connectionSource, MemberModel.class);
            
            // Check if tables need to be created
            CheckDatabaseExists();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public void createMember(String username, String invitedBy) {
        Date date = new Date();
        MemberModel memberModel = new MemberModel();
        
        memberModel.setUserName(username);
        memberModel.setInvitedBy(invitedBy);
        memberModel.setMemberStatus(MemberModel.MemberStatus.PROSPECT);
        memberModel.setDateInvited(date.toString());

        try {
            m_memberDao.create(memberModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createMember(String username, String invitedBy, MemberModel.MemberStatus memberStatus) {
        Date date = new Date();
        MemberModel memberModel = new MemberModel();
        
        memberModel.setUserName(username);
        memberModel.setInvitedBy(invitedBy);
        memberModel.setMemberStatus(memberStatus);
        memberModel.setDateInvited(date.toString());

        try {
            m_memberDao.create(memberModel);
        } catch (SQLException e) {
            e.printStackTrace();
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
            TableUtils.createTable(m_connectionSource, MemberModel.class);
        }
    }
    
    public static DatabaseManager getInstance() {
        if(m_instance == null) {
            m_instance = new DatabaseManager();
        }
        return m_instance;
    }
}
