package com.saurav.BirthdayReminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class AlarmBroadcast extends BroadcastReceiver{
	//public Context mycontext;
	public static ArrayList<String> bday = new ArrayList<String>();

	public static ArrayList<String> bname = new ArrayList<String>();
	public static ArrayList<String> bdate = new ArrayList<String>();
	public static ArrayList<String> bphone = new ArrayList<String>();

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
		{
			/*{	

			//Toast.makeText(mycontext, "Hey Boot Completed Received", Toast.LENGTH_LONG).show();
			Log.d("Saurav", "bOOT bOOT bOOT ");

		}
		else
		{*/
			//mycontext= context;
			Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show();
			Log.d("Saurav", "Alarm Triggered");
			try {
				strtalarmservice();
				//fetchbirthday();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(intent.getAction().equalsIgnoreCase("BirthdayReminder"))
		{
			try {
				fetchbirthday();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//}


	public void fetchbirthday() throws ParseException
	{
		ContentResolver cr = MainActivity.mycontext.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));		       
				ContentResolver bd = MainActivity.mycontext.getContentResolver();

				//Querying for birthday using contact_id
				Cursor bdc = bd.query(android.provider.ContactsContract.Data.CONTENT_URI,
						new String[] { Event.DATA }, 
						android.provider.ContactsContract.Data.CONTACT_ID+" = "+id+" AND "+Data.MIMETYPE+" = '"+Event.CONTENT_ITEM_TYPE+"' AND "+Event.TYPE+" = "+Event.TYPE_BIRTHDAY, 
						null, 
						android.provider.ContactsContract.Data.DISPLAY_NAME);
				String number = null;

				//code to fetch phone number from contact id
				Cursor phones = cr.query(Phone.CONTENT_URI, null,
						Phone.CONTACT_ID + " = " + id, null, null);
				while (phones.moveToNext()) {
					number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
				}

				if (bdc.getCount() > 0) {
					while (bdc.moveToNext()) {
						String birthday = bdc.getString(0);
						int count =0;

						Calendar c = Calendar.getInstance();
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						String formattedDate = df.format(c.getTime());

						//while(count != bdc.getCount())
						//if(name.equals("Saurav Pradhan"))
						{ 
							//need to change code for comparing only date, now it compares the whole date
							if(formattedDate.equals(birthday))
							{
								//converting string to date
								bday.add(name+" "+birthday+" "+number);
								bname.add(name);
								bdate.add(birthday);
								bphone.add(number);
								Toast.makeText(MainActivity.mycontext,"Name:"+name+" DOB:"+birthday+" Phone:"+number+"Today's Date is:"+formattedDate, Toast.LENGTH_LONG).show();
								count++;
							}
							else 
							{
								//Toast.makeText(mycontext, "No birthdays found today so closing the app!", Toast.LENGTH_LONG).show();
								bday.add("No Birthdays Today");
							}


						}
					}
				}
			}
			cur.close();

			//code to generate notification

			Intent notificationIntent = new Intent(MainActivity.mycontext, NotificationHandler.class);
			PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.mycontext,
					0, notificationIntent,
					0);

			NotificationManager nm = (NotificationManager) MainActivity.mycontext
					.getSystemService(Context.NOTIFICATION_SERVICE);

			Resources res = MainActivity.mycontext.getResources();
			NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.mycontext);
			builder.setContentIntent(contentIntent)
			.setSmallIcon(R.drawable.notification)
			//.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.notification))
			.setTicker("Birthday Reminder Alert!")
			.setWhen(System.currentTimeMillis())
			.setAutoCancel(true)
			.setContentTitle("You have birthday notifications")
			.setContentText("Touch on the reminder to know more details!");
			Notification n = builder.getNotification();

			n.defaults |= Notification.DEFAULT_ALL;
			nm.notify(0, n);
		}
	}

	public void strtalarmservice()
	{
		AlarmManager alarmMgr;
		PendingIntent alarmIntent;




		alarmMgr = (AlarmManager)MainActivity.mycontext.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(MainActivity.mycontext, AlarmBroadcast.class);
		intent.setAction("BirthdayReminder");
		alarmIntent = PendingIntent.getBroadcast(MainActivity.mycontext, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT
				| PendingIntent.FLAG_ONE_SHOT);




		//code for triggering alarm 
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.MINUTE, 00 );
		calendar.set(Calendar.SECOND, 00);
		Log.d("Saurav","Alarm triggering time is:"+Calendar.HOUR_OF_DAY+":"+Calendar.MINUTE);
	}

	public ArrayList<String> getList() {
		return bday;
	}
}
