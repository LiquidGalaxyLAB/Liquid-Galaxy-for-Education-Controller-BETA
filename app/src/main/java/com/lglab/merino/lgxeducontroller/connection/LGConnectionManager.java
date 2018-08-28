package com.lglab.merino.lgxeducontroller.connection;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.activities.NavigateActivity;
import com.lglab.merino.lgxeducontroller.legacy.data.POIsProvider;
import com.lglab.merino.lgxeducontroller.utils.LGCommand;

import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import pl.droidsonroids.gif.GifImageView;

public class LGConnectionManager implements Runnable {
    private static LGConnectionManager instance = null;

    private String user;
    private String password;
    private String hostname;
    private int port;

    private Session session;

    private BlockingQueue<LGCommand> queue;

    private int itemsToDequeue;
    private LGCommand lgCommandToReSend;

    private NavigateActivity activity;

    public LGConnectionManager() {
        user = "lg";
        password = "lg";
        hostname = "10.160.67.56";
        port = 22;

        session = null;
        queue = new LinkedBlockingDeque<>();
        itemsToDequeue = 0;
        lgCommandToReSend = null;

        //loadDataFromDB();
    }

    public static LGConnectionManager getInstance() {
        if (instance == null) {
            instance = new LGConnectionManager();
            new Thread(instance).start();
        }
        return instance;
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

    private Session getSession() {
        if (session == null || !session.isConnected()) {
            JSch jsch = new JSch();
            try {
                session = jsch.getSession(user, hostname, port);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            session.setPassword(password);

            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);

            try {
                session.connect(Integer.MAX_VALUE);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return session;
        }


        try {
            session.sendKeepAliveMsg();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return session;
    }

    private boolean sendLGCommand(LGCommand lgCommand) {
        lgCommandToReSend = lgCommand;

        Session session = getSession();
        if(session == null || !session.isConnected()) {
            return false;
        }

        ChannelExec channelSsh;
        try {
            channelSsh = (ChannelExec) session.openChannel("exec");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelSsh.setOutputStream(baos);

        channelSsh.setCommand(lgCommand.getCommand());

        try {
            channelSsh.connect();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        channelSsh.disconnect();
        //baos.toString();
        return true;
    }

    public void setData(String user, String password, String hostname, int port) {
        this.user = user;
        this.password = password;
        this.hostname = hostname;
        this.port = port;

        Log.d("HEY", password);
        //saveDataToDB();
    }

    public void setNavigateActivity(NavigateActivity activity) {
        this.activity = activity;
    }

    private synchronized void setGifWifiVisibility(boolean visibility) {
        try {
            if(activity != null) {
                int newVisibility = visibility ? View.VISIBLE : View.INVISIBLE;

                activity.runOnUiThread(() -> {
                    if(activity.wifiGif.getVisibility() != newVisibility)
                        activity.wifiGif.setVisibility(newVisibility);
                });
            }

        }
        catch(Exception e) {

        }
    }

    public void addCommandToLG(LGCommand lgCommand) {
        try {
            queue.offer(lgCommand);
            setGifWifiVisibility(true);
        } catch (Exception e) {

        }
    }

    @Override
    public void run() {
        try {
            while (true) {

                LGCommand lgCommand = lgCommandToReSend;
                if(lgCommand == null) {
                    lgCommand = queue.take();

                    if(itemsToDequeue > 0) {
                        itemsToDequeue--;
                        if(lgCommand.getPriorityType() == LGCommand.CRITICAL_MESSAGE) {
                            lgCommandToReSend = lgCommand;
                        }
                        continue;
                    }
                }

                long timeBefore = System.currentTimeMillis();

                if(!sendLGCommand(lgCommand)) {
                    //Command not sent
                    itemsToDequeue = queue.size();
                }
                else if(System.currentTimeMillis() - timeBefore >= 2000) {
                    //Command sent but took more than 2 seconds
                    lgCommandToReSend = null;
                    itemsToDequeue = queue.size();
                }
                else {
                    //Command sent in less than 2 seconds
                    lgCommandToReSend = null;
                }

                if(lgCommandToReSend == null && queue.size() == 0) {
                    setGifWifiVisibility(false);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }
}
