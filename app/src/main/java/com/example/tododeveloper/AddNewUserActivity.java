package com.example.tododeveloper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tododeveloper.db.AppDatabase;
import com.example.tododeveloper.db.User;
import com.example.tododeveloper.notification.AlarmReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;


public class AddNewUserActivity extends AppCompatActivity {
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TextView eventTime;
    private ImageView eventImgbtn;
    private LinearLayout Event_Back_Img;
    public Boolean check =false;
    public static AddNewUserActivity Instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event1);
        createNotificationChannel();
        Instance =this;
        final EditText firstNameInput =  findViewById(R.id.Event_Title1);
        final EditText lastNameInput =  findViewById(R.id.Event_Description1);

        Event_Back_Img =findViewById(R.id.event_background);
        eventImgbtn =findViewById(R.id.event_image);
        eventImgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //for image
                intent.setType("image/*");
                //for video mp4 && for all type of data */*
                startActivityForResult(intent,33);
            }
        });
        eventTime =findViewById(R.id.event_Time);
//        set_Alarm =findViewById(R.id.set_Alarm_btn);
        FloatingActionButton saveButton =  findViewById(R.id.save_Button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewUser(firstNameInput.getText().toString(), lastNameInput.getText().toString(),eventTime.getText().toString());
                if (check == true){
                    setAlarm();
                }
            }
        });
        SwitchCompat switchCompatbtn =findViewById(R.id.toDoHasDateSwitchCompat);
        switchCompatbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true){
                    showTimePicker();
                    eventTime.setVisibility(View.VISIBLE);
                    check =true;
                }else {
                    cancelAlarm();
                    check =false;
                    eventTime.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void saveNewUser(String firstName, String lastName,String eventtime) {
        AppDatabase db  = AppDatabase.getDbInstance(this.getApplicationContext());

        User user = new User();
        user.firstName = firstName;
        user.lastName = lastName;
//        user.eventtime = eventtime;
        db.userDao().insertUser(user);

        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData() != null){
            Uri sFile = data.getData();
            eventImgbtn.setImageURI(sFile);
            Drawable myDrawable =eventImgbtn.getDrawable();
            Event_Back_Img.setBackground(myDrawable);
            eventImgbtn.setImageResource(R.drawable.image1);
        }
    }

    public static AddNewUserActivity getInstance(){
        return Instance;
    }
    public void cancelAlarm() {

        Intent intent = new Intent(this,AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);

        if (alarmManager == null){

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        }

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {
        alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent =new Intent(this,AlarmReceiver.class);
        pendingIntent =PendingIntent.getBroadcast(this,0,intent,0);

        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        Toast.makeText(this,"Alarm set Sucessfully",Toast.LENGTH_SHORT).show();

    }


    private void showTimePicker() {

        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        picker.show(getSupportFragmentManager(),"foxandroid");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (picker.getHour() > 12){

                    eventTime.setText(
                            String.format("%02d",(picker.getHour()-12))+" : "+String.format("%02d",picker.getMinute())+" PM"
                    );

                }else {

                    eventTime.setText(picker.getHour()+" : " + picker.getMinute() + " AM");

                }



                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                calendar.set(Calendar.MINUTE,picker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);

            }
        });


    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name= "SchedulerReminderChannel";
            String description ="Channnel for Alarm Manager";
            int important= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("scheduler",name,important);
            channel.setDescription(description);

            NotificationManager notificationManager =getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }


    }

}