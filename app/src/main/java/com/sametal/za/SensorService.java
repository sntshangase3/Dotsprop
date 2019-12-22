package com.sametal.za;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by fabio on 30/01/2016.
 */
public class SensorService extends Service {
    public int counter=0;
    // MainActivity activity =   MainActivity.instance;

    Connection con;
    String un,pass,db,ip;

    MainActivity activity = MainActivity.instance;

    String firstname = "",position = "",email = "";

    Calendar date;
    String userid;

    public SensorService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public SensorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("com.za.za.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job


        try{

            final NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            final Intent notificationIntent = new Intent(SensorService.this, SplashFragment.class);
            final Notification.Builder builder = new Notification.Builder(SensorService.this);



            String notificationbody = "";//
            // this code will be executed after 2 seconds  2000

            try {

                ip = "atlasms.sam.cpt";
                db = "Dotsprop";
                un = "PaymentsQuoting";
                pass = "PaymentsQuoting";
                ConnectionClass cn = new ConnectionClass();
                con = cn.connectionclass(un, pass, db, ip);

                userid = activity.edthidenuserid.getText().toString();
                String query1 = "select * from [sastaff] where [password]='" + userid+"'";
                PreparedStatement ps = con.prepareStatement(query1);
                ResultSet rs = ps.executeQuery();
                rs.next();
                firstname = rs.getString("staff").trim();
                position = rs.getString("position").trim();
                email = rs.getString("email").trim();
                Log.d("ReminderService In", firstname+" "+position+" "+email);
                ip = "winsqls01.cpt.wa.co.za";
                db = "SqaloITSolutionsTest";
                un = "sqaloits";
                pass = "422q5mfQzU";
                con = cn.connectionclass(un, pass, db, ip);
                String query;
                if (position.equals("Administrator")) {
                    query = "select  * from [Collection] where [collectionstatus]='New'";
                } else {
                    query = "select  * from [Collection] where [custno]=" + userid;
                }
                //Co-user login
                Log.d("ReminderService In", query);
                PreparedStatement ps1 = con.prepareStatement(query);
                ResultSet rs1 = ps1.executeQuery();
                int total = 0;
                while (rs1.next()) {
                    total = total + 1;
                }

                if(total!=0){
                    Notification myNotication;
                    int id = 0;
                    PendingIntent pendingIntent = PendingIntent.getActivity(SensorService.this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                    Log.d("ReminderService In", "Time");
                    builder.setAutoCancel(false);

                    builder.setTicker("Item about to expire");
                    builder.setSmallIcon(R.drawable.logos);
                    builder.setContentIntent(pendingIntent);
                    builder.setContentTitle(getResources().getString(R.string.notify_new_task_title));
                    builder.setContentText(notificationbody);
                    myNotication = builder.setStyle(new Notification.BigTextStyle().bigText(notificationbody)).build();
                   // myNotication.defaults |= Notification.DEFAULT_VIBRATE;
                   // myNotication.defaults |= Notification.DEFAULT_SOUND;


                    mgr.notify(id, myNotication);
                                       /* try {

                                            Mail m = new Mail("Info@sqaloitsolutions.co.za", "Mgazi@251085");
                                            //String to = "jabun@ngobeniholdings.co.za";
                                            // String to = "sntshangase3@gmail.com";
                                            // m.setFrom("Info@ngobeniholdings.co.za");
                                            // String to = "SibusisoN@sqaloitsolutions.co.za";
                                            String to = email;
                                            String from = "dev@sametal.co.za";
                                            String subject = "New Collection";
                                            String message = "Dear " + firstname + "\nYou have item/s about to expire:\n" + notificationbody + "\nLogin on your app and check." + "\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                                            String[] toArr = {to};
                                            m.setTo(toArr);
                                            m.setFrom(from);
                                            m.setSubject(subject);
                                            m.setBody(message);

                                            m.send();


                                        } catch (Exception e) {


                                        }*/
                }

            } catch (Exception ex) {
                Log.d("ReminderService In", ex.getMessage() + "######");
            }
           // initializeTimerTask();
        } catch (Exception ex) {

            Log.d("ReminderService In", ex.getMessage().toString());
    }
        //mylistselected the timer, to wake up every 1 second
      // timer.mylistselected(timerTask, 1000, 1000); //
        //mylistselected the timer, to wake up every 5 hours
       //####### timer.mylistselected(timerTask, 18000, 18000); #####//

    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.d("ReminderService In",  "###### STTTTTTT");
                Log.i("in timer", "in timer ++++  " + (counter++));



/*
                                          //  Log.d("ReminderService In", "Co-user");
                                            Date bestbefore = null;
                                            Date today = new Date();
                                            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                                            String todaydate = date_format.format(today);
                                            try {
                                                Date date = new Date(date_format.parse(rs.getString("bestbefore").toString()).getTime());
                                                Date date2 = new Date(date_format.parse(todaydate).getTime());
                                                bestbefore = date;
                                                today = date2;
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            long daysleft = getDateDiff(today, bestbefore, TimeUnit.DAYS);

                                            if (daysleft == Integer.parseInt(rs.getString("preferbestbeforedays").toString())) {
                                                Log.d("ReminderService In", String.valueOf(daysleft) + " " + rs.getString("preferbestbeforedays").toString());
                                                notificationbody = notificationbody + rs.getString("quantity").toString() + " " + rs.getString("name").toString() + "\n";



                                                //##Update isNotified/Read
                                                String commands = "update [UserProduct] set [isread]='Yes' where [id]='" + rs.getString("id").toString() + "'";
                                                PreparedStatement preStmt = con.prepareStatement(commands);
                                                preStmt.executeUpdate();
*/








            }


        };
    }
    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffinMill = date2.getTime() - date1.getTime();
        return timeUnit.DAYS.convert(diffinMill, TimeUnit.MILLISECONDS);
    }
public  void ShowPassCodeNotification(){

}
}