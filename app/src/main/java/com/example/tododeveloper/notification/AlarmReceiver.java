package com.example.tododeveloper.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tododeveloper.AddNewUserActivity;
import com.example.tododeveloper.MainActivity;
import com.example.tododeveloper.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri alarmSound = RingtoneManager. getDefaultUri (RingtoneManager.TYPE_NOTIFICATION);
        Drawable myDrawable = context.getDrawable(R.drawable.reminder);
        Bitmap Image      = ((BitmapDrawable) myDrawable).getBitmap();

        Intent i=new Intent(context,MainActivity.class);

        //Click on Notification
        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent1.putExtra("message", text);

        //Notification Builder
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");


        //here we set all the properties for the notification
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);
        contentView.setTextViewText(R.id.message, "text");
        contentView.setTextViewText(R.id.date, "date");
        mBuilder.setSmallIcon(R.drawable.alaram);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);

        //we have to create notification channel after api level 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Notification notification = mBuilder.build();
        notificationManager.notify(1, notification);



//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent= PendingIntent.getActivity(context,0,i,0);
//        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"scheduler")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("Scheduler Alarm Manager")
//                .setContentText(" Time Over")
//                .setLargeIcon(Image)
//                .setSound(alarmSound)
//                .setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent);
//
//
//        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
//        notificationManagerCompat.notify(123, builder.build());


//        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
//        vibrator.vibrate(4000);


    }
}
