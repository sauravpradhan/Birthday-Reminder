package com.saurav.BirthdayReminder;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SmsActivity extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_activity);

		final EditText phoneno = (EditText)findViewById(R.id.editTextPhoneNo);
		final EditText smsedit = (EditText)findViewById(R.id.editTextSMS);
		Button send = (Button)findViewById(R.id.send);
		Button cancel = (Button)findViewById(R.id.cancel);

		phoneno.setText(AlarmBroadcast.bphone.get(NotificationHandler.passing_pos));
		smsedit.setText("Many Many Happy Returns of the day! \nHAPPY BIRTHDAY!!");

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String uri= "smsto:"+ phoneno.getText().toString();
				//Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
				//intent.putExtra("sms_body",smsedit.getText().toString());
				//startActivity(intent);
				
			
				SmsManager smsManager = SmsManager.getDefault();
				
				Log.d("Saurav", phoneno.getText().toString());
				
				smsManager.sendTextMessage(uri, null , smsedit.getText().toString(), null, null);
				
				finish();
				
			}
		});
	}

}
