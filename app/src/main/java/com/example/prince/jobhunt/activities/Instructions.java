package com.example.prince.jobhunt.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.prince.jobhunt.R;
import com.example.prince.jobhunt.engine.AuthManager;
import com.example.prince.jobhunt.engine.FirebaseAgent;
import com.example.prince.jobhunt.model.Notyfication;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Instructions extends AppCompatActivity {

	public static final int REQUEST_CODE = 123;
	private TimePickerDialog timePickerDialog;
	private DatePickerDialog datePickerDialog;
	@BindView(R.id.toolbar_home)
	Toolbar toolbar;
	@BindView(R.id.timePicker)
	View timePicker;
	@BindView(R.id.timePicker_end)
	View timePicker_end;
	@BindView(R.id.datePicker)
	View datePicker;
	@BindView(R.id.datePicker_end)
	View datePicker_end;
	@BindView(R.id.timeStart)
	TextView start_time;
	@BindView(R.id.timeEnd)
	TextView end_time;
	@BindView(R.id.dateStart)
	TextView start_date;
	@BindView(R.id.dateEnd)
	TextView end_date;
	@BindView(R.id.isNeg)
	Switch isNeg;
	@BindView(R.id.cvImg)ImageView cvImg;

	private FirebaseFirestore firestore;
	private AuthManager authManager;
	private FirebaseAgent agent;
	Boolean n = false;
	private Uri file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructions);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		setPickers();

		firestore = FirebaseFirestore.getInstance();
		authManager = new AuthManager(this);
		agent = new FirebaseAgent(this);


	}

	public void setPickers() {
		timePicker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Calendar calendar = Calendar.getInstance();
				int hour = calendar.get(Calendar.HOUR);
				int min = calendar.get(Calendar.MINUTE);

				timePickerDialog = new TimePickerDialog(Instructions.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int i, int i1) {
						String m = String.valueOf(i1);
						String h = String.valueOf(i);
						start_time.setText(h + " : " + m);
					}
				}, hour, min, false);
				timePickerDialog.show();
			}
		});

		timePicker_end.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Calendar calendar = Calendar.getInstance();
				int hour = calendar.get(Calendar.HOUR);
				int min = calendar.get(Calendar.MINUTE);

				timePickerDialog = new TimePickerDialog(Instructions.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int i, int i1) {
						String m = String.valueOf(i1);
						String h = String.valueOf(i);
						end_time.setText(h + " : " + m);
					}
				}, hour, min, false);
				timePickerDialog.show();
			}
		});

		datePicker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Calendar calendar = Calendar.getInstance();
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				int month = calendar.get(Calendar.MONTH);
				int year = calendar.get(Calendar.YEAR);

				datePickerDialog = new DatePickerDialog(Instructions.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
						String year = String.valueOf(i);
						String month = String.valueOf(i1 + 1);
						String day = String.valueOf(i2);

						start_date.setText(day + "/ " + month + "/ " + year);
					}
				}, year, month, day);

				datePickerDialog.show();
			}
		});

		datePicker_end.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Calendar calendar = Calendar.getInstance();
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				int month = calendar.get(Calendar.MONTH);
				int year = calendar.get(Calendar.YEAR);

				datePickerDialog = new DatePickerDialog(Instructions.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
						String year = String.valueOf(i);
						String month = String.valueOf(i1 + 1);
						String day = String.valueOf(i2);

						end_date.setText(day + "/ " + month + "/ " + year);

//						if (!start_date.getText().toString().equals("")){
//							if (checkDate(start_date.getText().toString(), end_date.getText().toString())){
//
//								Toast.makeText(Instructions.this, "ok", Toast.LENGTH_SHORT).show();
//							}else {
//								Toast.makeText(Instructions.this, "greater than date 2", Toast.LENGTH_SHORT).show();
//							}
//						}else {
//							return;
//						}

					}
				}, year, month, day);

				datePickerDialog.show();
			}
		});

		isNeg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b) {
					n = true;
					Toast.makeText(Instructions.this, "on", Toast.LENGTH_SHORT).show();
				} else {
					n = false;
					Toast.makeText(Instructions.this, "off", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void sendInstructions(View view) {
		String reciever = getIntent().getStringExtra("receiver");
		String job_id = getIntent().getStringExtra("job_id");

		Notyfication notification = new Notyfication();
		notification.setSender(authManager.getCurrentUID());
		notification.setReceiver(reciever);
		notification.setJob_id(job_id);
		notification.setMessage("ddd");
		notification.setCv_image(notification.getReceiver());
		notification.setEnd_date(end_date.getText().toString());
		notification.setEnd_time(end_time.getText().toString());
		notification.setNeg(n);
		notification.setPartTime(true);
		notification.setStart_date(start_date.getText().toString());
		notification.setStart_time(start_time.getText().toString());


		agent.sendNotification(notification, file, new FirebaseAgent.notifStatus() {
			@Override
			public void isNotifSent(Boolean status) {
				if (status){
					Toast.makeText(Instructions.this, "notification sent", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public boolean checkDate(String s_date, String e_date) {
		boolean isPast = false;
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date date1 = format.parse(s_date);
			Date date2 = format.parse(e_date);

			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date1);

			Calendar calendar2 = new GregorianCalendar();
			calendar.setTime(date2);

			if (calendar.after(calendar2)) {
				return false;
			} else {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void openChatRoom(View view) {
		startActivity(new Intent(Instructions.this, ChatRoom.class));
	}

	public void uploadPic(View view) {
		Intent i = new Intent();
		i.setType("image/*");
		i.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(i, "Select CV Image"), REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && data.getData() != null){
			file = data.getData();

			CropImage.activity(file).start(this);
		} else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			CropImage.ActivityResult result = CropImage.getActivityResult(data);
			if (resultCode == RESULT_OK){
				file = result.getUri();
				Bitmap bitmap;
				try {
					bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
					cvImg.setImageBitmap(bitmap);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
