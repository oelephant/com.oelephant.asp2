package com.oelephant.androidsoundprofiles2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;

import com.oelephant.androidsoundprofiles2.utils.ASPDB;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ASPDB db = new ASPDB(this);
		db.createProfile("TEST", 0, 0, 0, 0, 0, 0, 0, 1);
		
		long start, length;
		
		start = (1000 * 60 * 60 * 10) + (1000 * 60 * 30);
		length = (1000 * 60 * 60 * 4);

		db.createSchedule("TEST", Color.BLUE, 1, 1, 1, 1, 1, 1, 1, 1, start, length);

		start = (1000 * 60 * 60 * 18);
		length = (1000 * 60 * 60 * 8);
		
		db.createSchedule("TEST", Color.GREEN, 1, 0, 1, 0, 1, 0, 1, 0, start, length);
		
		db.createQueue(4);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
