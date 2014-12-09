package com.saurav.BirthdayReminder;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NotificationHandler extends ListActivity {
	public static int passing_pos;
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("Born Today");
		AlarmBroadcast arrayobj = new AlarmBroadcast();
		ArrayList<String> passedbday = null;
		passedbday = arrayobj.getList();

		super.onCreate(savedInstanceState);
		if(AlarmBroadcast.bname.equals(null))
		{
			AlarmBroadcast.bname.add("No Birthday Today!");
		}
		setListAdapter(new ArrayAdapter<String>(this, R.layout.listnotification,AlarmBroadcast.bname));
		//Toast.makeText(getApplicationContext(), "Toast from the Notification Handler", Toast.LENGTH_LONG).show();

	}

	protected void onListItemClick(ListView l, View v, final int position, long id)
	{
		passing_pos = position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder
		.setTitle("Birthday Reminder")
		.setIcon(R.drawable.birthday)
		.setMessage("Today is: "+AlarmBroadcast.bname.get(position)+"'s "+"birthday!\nHow would you like to wish?")
		.setCancelable(false)
		.setPositiveButton("Call", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int id) 
			{
				
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", AlarmBroadcast.bphone.get(position), null));
				startActivity(intent);
			}
		});
		builder.setNeutralButton("Sms",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {



				Intent i=new Intent(getApplicationContext(),SmsActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(i);


			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}
		});
		AlertDialog alert = builder.create();
		alert.show();}

	public int PassPosition()
	{
		return passing_pos;
	}
}
