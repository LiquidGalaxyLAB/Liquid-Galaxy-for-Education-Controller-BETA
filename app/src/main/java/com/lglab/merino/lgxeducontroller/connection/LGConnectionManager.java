package com.lglab.merino.lgxeducontroller.connection;

import android.database.Cursor;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.lglab.merino.lgxeducontroller.legacy.data.POIsProvider;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

public class LGConnectionManager {
    private static LGConnectionManager instance = null;
    public static LGConnectionManager getInstance() {
        if(instance == null)
            instance = new LGConnectionManager();
        return instance;
    }


    private String user;
    private String password;
    private String hostname;
    private int port;

    private Session session;

    public LGConnectionManager() {
        user = "lg";
        password = "lg";
        hostname = "10.160.67.56";
        port = 22;

        session = null;

        loadDataFromDB();
    }

    private void loadDataFromDB() {
        Cursor category_cursor = POIsProvider.getLGConnectionData();
        if (category_cursor.moveToNext()) {
            user = category_cursor.getString(category_cursor.getColumnIndexOrThrow("user"));
            password = category_cursor.getString(category_cursor.getColumnIndexOrThrow("password"));
            hostname = category_cursor.getString(category_cursor.getColumnIndexOrThrow("hostname"));
            port = category_cursor.getInt(category_cursor.getColumnIndexOrThrow("port"));
        }
    }

    private void saveDataToDB() {
        POIsProvider.updateLGConnectionData(user, password, hostname, port);
    }

    public void setData(String user, String password, String hostname, int port) {
        this.user = user;
        this.password = password;
        this.hostname = hostname;
        this.port = port;

        saveDataToDB();
    }

    public String sendCommandToLG(String command) {
        Session session = getSession();
        try {
            if (session != null && session.isConnected()) {
                ChannelExec channelssh = (ChannelExec) session.openChannel("exec");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                channelssh.setOutputStream(baos);

                channelssh.setCommand(command);
                channelssh.connect();
                channelssh.disconnect();

                return baos.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private Session getSession() {
        try {
            if (session == null || !session.isConnected()) {
                JSch jsch = new JSch();

                session = jsch.getSession(user, hostname, port);
                session.setPassword(password);

                Properties prop = new Properties();
                prop.put("StrictHostKeyChecking", "no");
                session.setConfig(prop);
                session.connect(Integer.MAX_VALUE);

                return session;
            }

            session.sendKeepAliveMsg();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }
}
