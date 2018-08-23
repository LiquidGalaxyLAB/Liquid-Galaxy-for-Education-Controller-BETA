package com.lglab.merino.lgxeducontroller.connection;

import android.database.Cursor;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.lglab.merino.lgxeducontroller.legacy.data.POIsProvider;

import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class LGConnectionManager implements Runnable {
    private static LGConnectionManager instance = null;
    public static LGConnectionManager getInstance() {
        if(instance == null) {
            instance = new LGConnectionManager();
            new Thread(instance).start();
        }
        return instance;
    }


    private String user;
    private String password;
    private String hostname;
    private int port;

    private Session session;
    private BlockingQueue<String> queue;

    public LGConnectionManager() {
        user = "lg";
        password = "lg";
        hostname = "10.160.67.56";
        port = 22;

        session = null;
        queue = new LinkedBlockingDeque<>();

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

    private String sendCommandToLG(String command) {
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

    public void addCommandToLG(String message) {
        try {
            queue.offer(message);
        }
        catch(Exception e) {

        }
    }

    @Override
    public void run() {
        try {
            while(true){
                sendCommandToLG(queue.take());
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
