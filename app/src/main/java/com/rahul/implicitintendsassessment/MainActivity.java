package com.rahul.implicitintendsassessment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int PHOTO_REQUEST_CODE = 123;

    private EditText eventTitleEV;
    private CheckBox allDayEventCB;

    private TextView startDateBtn;
    private DatePickerDialog.OnDateSetListener startDateListner;

    private TextView endDateBtn;
    private DatePickerDialog.OnDateSetListener endDateListner;

    private TextView startTimeBtn;
    private TextView endTimeBtn;

    private EditText eventDescriptionEV;
    private EditText inviteesEmailEV;
    private Switch accessSwitch;

    private int startDate, startMonth, startYear, startHour, startMinute;
    private int endDate, endMonth, endYear, endHour, endMinute;

    private EditText recepientEV;
    private EditText mailSubjectEV;
    private EditText mailMessageEV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventTitleEV = findViewById(R.id.eventTitle);
        allDayEventCB = findViewById(R.id.allDayEventCB);
        eventDescriptionEV = findViewById(R.id.eventDescriptionEV);
        inviteesEmailEV = findViewById(R.id.inviteesEmailTV);

        startDateBtn = (TextView) findViewById(R.id.startDateSelectTV);
        accessSwitch = findViewById(R.id.accessSwitch);

        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        startDateListner,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        startDateListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = String.format("%02d/%02d/%02d", month, day, year);
                startDate = day;
                startMonth = month;
                startYear = year;
                startDateBtn.setText(date);
            }
        };


        endDateBtn = (TextView) findViewById(R.id.endDateSelectTV);

        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        endDateListner,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        endDateListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = String.format("%02d/%02d/%02d", month, day, year);
                endDate = day;
                endMonth = month;
                endYear = year;
                endDateBtn.setText(date);
            }
        };


        startTimeBtn = findViewById(R.id.startTimeSelectTV);
        endTimeBtn = findViewById(R.id.endTimeSelectTV);


    }

    public void popStartTimePicker(View view) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                startHour = selectedHour;
                startMinute = selectedMinute;
                startTimeBtn.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute));
            }
        };

        TimePickerDialog timePickerDialog  = new TimePickerDialog(this,
                                                TimePickerDialog.THEME_HOLO_DARK,
                                                onTimeSetListener,
                                                startHour,
                                                startMinute,
                                                true);
        timePickerDialog.show();
    }

    public void popEndTimePicker(View view) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                endHour = selectedHour;
                endMinute = selectedMinute;
                endTimeBtn.setText(String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute));
            }
        };

        TimePickerDialog timePickerDialog  = new TimePickerDialog(this,
                                                TimePickerDialog.THEME_HOLO_DARK,
                                                onTimeSetListener,
                                                endHour,
                                                endMinute,
                                                true);
        timePickerDialog.show();
    }

    public void addCalenderEvent(View view) {

        Calendar calendarStart = Calendar.getInstance();

        if (!startDateBtn.getText().toString().isEmpty()) {

            calendarStart.set(Calendar.YEAR, startYear);
            calendarStart.set(Calendar.MONTH, startMonth-1);
            calendarStart.set(Calendar.DAY_OF_MONTH, startDate);
        }

        if (!startTimeBtn.getText().toString().isEmpty()) {

            calendarStart.set(Calendar.HOUR_OF_DAY, startHour);
            calendarStart.set(Calendar.MINUTE, startMinute);
        }

        Date startDateTime = calendarStart.getTime();

        Calendar calendarEnd = Calendar.getInstance();

        if (!endDateBtn.getText().toString().isEmpty()) {

            calendarEnd.set(Calendar.YEAR, endYear);
            calendarEnd.set(Calendar.MONTH, endMonth-1);
            calendarEnd.set(Calendar.DAY_OF_MONTH, endDate);
        }

        if (!startTimeBtn.getText().toString().isEmpty()) {

            calendarEnd.set(Calendar.HOUR_OF_DAY, endHour);
            calendarEnd.set(Calendar.MINUTE, endMinute);
        }

        int accessLevel = CalendarContract.Events.ACCESS_DEFAULT;

        if(accessSwitch.isChecked()) {
            accessLevel = CalendarContract.Events.ACCESS_PRIVATE;
        }
        else {
            accessLevel = CalendarContract.Events.ACCESS_PUBLIC;
        }

        Date endDateTime = calendarEnd.getTime();

        Intent i = new Intent(Intent.ACTION_EDIT);
        i.setType("vnd.android.cursor.item/event");
        i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDateTime.getTime());
        i.putExtra(CalendarContract.Events.ALL_DAY, allDayEventCB.isChecked());
        i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDateTime.getTime());
        i.putExtra(CalendarContract.Events.TITLE, eventTitleEV.getText().toString());
        i.putExtra(CalendarContract.Events.DESCRIPTION, eventDescriptionEV.getText().toString());
        i.putExtra(Intent.EXTRA_EMAIL, inviteesEmailEV.getText().toString());
        i.putExtra(CalendarContract.Events.ACCESS_LEVEL, accessLevel);
        startActivity(i);
    }

    public void photoClicked(View view) {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoIntent, PHOTO_REQUEST_CODE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ImageView imageView = findViewById(R.id.imageView);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_CODE) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    public void sendEmailClicked(View view) {

        recepientEV = findViewById(R.id.recipientEV);
        mailSubjectEV = findViewById(R.id.subjectEV);
        mailMessageEV = findViewById(R.id.mailMessage);

        String [] arr = {"rahulbt20@gmail.com", "rt20@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, recepientEV.getText().toString().split(","));
        intent.putExtra(Intent.EXTRA_SUBJECT, mailSubjectEV.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, mailMessageEV.getText().toString());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}