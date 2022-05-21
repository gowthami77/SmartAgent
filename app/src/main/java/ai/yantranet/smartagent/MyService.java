package ai.yantranet.smartagent;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import ai.yantranet.smartagent.model.Data;
import ai.yantranet.smartagent.retrofithelper.BaseImages;
import ai.yantranet.smartagent.retrofithelper.Keys;
import ai.yantranet.smartagent.retrofithelper.LogoImages;
import retrofit2.Call;
import retrofit2.Callback;

public class MyService extends Service {
    private DatabaseHelper db;

    public boolean iscompleted = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        db = new DatabaseHelper(this);
        final Handler handler = new Handler();
        final int delay = 2000; // 2000 milliseconds == 2 second

        handler.postDelayed(new Runnable() {
            public void run() {
                fetchData();

                handler.postDelayed(this, delay);
            }
        }, delay);


        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    public void fetchData() {
        try {
            LogoImages service = BaseImages.getClient().create(LogoImages.class);
            Call<Data> call = service.fetch(Keys.readSecondKey());
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, retrofit2.Response<Data> response) {
                    Log.e("response", response.raw().toString());
                    if (!response.isSuccessful()) {
                        Log.e("API not success", response.toString() + response.message() + response.errorBody() + response.code());
                    }
                    Data data = response.body();
                    if (data == null) {
                        Log.e("Api error", "..." + response.code());

                    } else {
                        // Insert data into database
                        db.insertNote(data);

                        for (int i = 0; i < data.getDependencies().size(); i++) {
                            // File Downloading
                                long size = getFileSize(data.getDependencies().get(i).getName());
                                if (!iscompleted) {
                                    if (!checkFileExist(data.getDependencies().get(i).getName())) {

                                        Log.e("file downloading", "......image");
                                        Downback DB = new Downback(data.getDependencies().get(i).getCdnPath(), data.getDependencies().get(i).getName());
                                        DB.execute("");
                                    } else if (size != data.getDependencies().get(i).getSizeInBytes()) {
                                        Log.e("file downloading" + size, data.getDependencies().get(i).getSizeInBytes() + "......image else");

                                        Downback DB = new Downback(data.getDependencies().get(i).getCdnPath(), data.getDependencies().get(i).getName());
                                        DB.execute("");
                                    }

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    Log.e("failed", "error");
                }
            });
        } catch (Exception e) {
            String TAG = "tag";
            Log.e(TAG, e.getMessage());
        }
    }

    private long getFileSize(String name) {
        String rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "yantranet";
        File rootFile = new File(rootDir, name);

        if (rootFile.exists()) {

            return rootFile.length();
        } else {
            return 0;
        }
    }

    public boolean checkFileExist(String filename) {
        String rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + File.separator + "yantranet";
        File rootFile = new File(rootDir, filename);

        if (rootFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Download Files
    private class Downback extends AsyncTask<String, String, String> {
        String filepath;
        String filename;

        public Downback(String cdnPath, String name) {
            this.filepath = cdnPath;
            this.filename = name;
        }

        @Override
        protected void onPreExecute() {
            iscompleted = true;
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            final String vidurl = filepath;
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                java.net.URL url = new URL(vidurl);
                URLConnection conn = url.openConnection();
                int contentLength = conn.getContentLength();
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                input = connection.getInputStream();
                String rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + File.separator + "yantranet";
                File rootFile = new File(rootDir);
                if (!rootFile.exists()) {
                    rootFile.mkdir();
                }

                output = new FileOutputStream(new File(rootFile,
                        filename));

                byte data[] = new byte[contentLength];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    if (fileLength > 0)
                        output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("download completed", "..........");
            iscompleted = false;
            super.onPostExecute(s);
        }
    }


}
