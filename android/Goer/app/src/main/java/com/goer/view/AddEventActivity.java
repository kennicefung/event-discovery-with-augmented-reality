package com.goer.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import java.util.Calendar;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.goer.R;
import com.goer.controller.EventController;
import com.goer.controller.UserController;
import com.goer.helper.GlobalHelper;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.app.AlertDialog;

import android.support.v4.content.ContextCompat;


public class AddEventActivity extends AppCompatActivity{

    private int PLACE_PICKER_REQUEST = 1;
    private int IMAGE_PICKER_REQUEST = 2;

    private RelativeLayout f_location;
    private TextView display_location;
    private String lat, lng;

    private EditText start_date, start_time, end_date, end_time;
    private DatePickerDialog.OnDateSetListener startDateListener,endDateListener;
    private TimePickerDialog.OnTimeSetListener startTimeListener,endTimeListener;

    private LinearLayout ll_input_image;
    private ImageView input_image;
    private Bitmap bitmap;
    private Uri filePath;

    private int error_code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_event_toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Add New Event");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setupFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_submit:
                submit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupFields(){
        ll_input_image = (LinearLayout) findViewById(R.id.ll_input_image);
        input_image = (ImageView) findViewById(R.id.input_image);
        ll_input_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER_REQUEST);
            }
        });
        start_date = (EditText) findViewById(R.id.input_event_start_date);
        end_date = (EditText) findViewById(R.id.input_event_end_date);
        start_time = (EditText) findViewById(R.id.input_event_start_time);
        end_time = (EditText) findViewById(R.id.input_event_end_time);

        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                start_date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
            }
        };

        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                end_date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
            }
        };

        startTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                start_time.setText(hourOfDay + ":" + minute);
            }
        };

        endTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                end_time.setText(hourOfDay + ":" + minute);
            }
        };

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        startDateListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                dpd.show(getFragmentManager(), "Start Date");
            }
        });
        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        endDateListener,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                dpd.show(getFragmentManager(), "End Date");
            }
        });

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        startTimeListener,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                tpd.setAccentColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                tpd.show(getFragmentManager(), "Start Time");
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        endTimeListener,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                tpd.setAccentColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                tpd.show(getFragmentManager(), "End Time");
            }
        });

        f_location = (RelativeLayout) findViewById(R.id.input_event_location);
        display_location = (TextView) findViewById(R.id.display_event_location);
        f_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLocation();
            }
        });
    }

    public void selectLocation(){
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(AddEventActivity.this), PLACE_PICKER_REQUEST);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this,data);
                lat = place.getLatLng().latitude + "";
                lng = place.getLatLng().longitude + "";
                display_location.setTextSize(12);
                display_location.setText(place.getName()+ System.getProperty("line.separator") + place.getAddress());
            }
        }

        if (requestCode == IMAGE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    input_image.setImageBitmap(bitmap);
                    input_image.setLayoutParams(new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void submit(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            String new_id;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddEventActivity.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                return createEvent();
            }
        }

        try {
            UploadImage ui = new UploadImage();
            String new_id = ui.execute(bitmap).get();
            if(new_id != null){
                Intent i = new Intent(this, EventDetailActivity.class);
                i.putExtra("EventModel", EventController.getEventByEventID(new_id));
                startActivity(i);
                AddEventActivity.this.finish();
            }
            else
            {
                String err_msg = "";
                switch(error_code){
                    case 0:
                        err_msg = "Cannot upload the image due to server error.";
                        break;
                    case 1:
                        err_msg = "Please input valid start time and end time.";
                        break;
                    case 2:
                        err_msg = "Please select the event image and input all required fields.";
                        break;
                }
                AlertDialog.Builder dlgBuilder  = new AlertDialog.Builder(this);
                dlgBuilder.setMessage(err_msg);
                dlgBuilder.setTitle("Error");
                dlgBuilder.setPositiveButton("OK", null);
                dlgBuilder.setCancelable(true);
                final AlertDialog dlg = dlgBuilder.create();
                dlg.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dlg.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDarkGreen));
                    }
                });

                dlg.show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createEvent(){
        String title = ((EditText)findViewById(R.id.input_event_title)).getText().toString();
        String location = display_location.getText().toString();
        String des = ((EditText)findViewById(R.id.input_event_des)).getText().toString();
        String sd = start_date.getText().toString();
        String st = start_time.getText().toString();
        String ed = end_date.getText().toString();
        String et = end_time.getText().toString();
        String is_public = ((SwitchCompat)findViewById(R.id.input_is_public)).isChecked()? "1":"0";
        String maxNum = ((EditText)findViewById(R.id.input_max_num)).getText().toString();
        maxNum = maxNum.equals("")?"-1": maxNum;

        try {
            if (!title.equals("") && !lat.equals("") && !lng.equals("") && !des.equals("")
                    && !sd.equals("") && !st.equals("") && !ed.equals("") && !et.equals("")
                    && !is_public.equals("") && !maxNum.equals("")
                    && bitmap != null) {
                error_code = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:ss");
                Date date_s = sdf.parse(sd + " " + st);
                Date date_e = sdf.parse(ed + " " + et);
                if (date_e.after(date_s)) {
                    return EventController.createEvent(UserController.getUserModel(getApplicationContext()).getUserID(), title, location, lat, lng, des, sd + " " + st, ed + " " + et, GlobalHelper.getStringImage(bitmap), is_public, maxNum);
                } else {
                    error_code = 1;
                    return null;
                }
            }
            else
            {
                error_code = 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
