package com.example.androidsoundprofiles2.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ASPDB extends SQLiteOpenHelper {
	
	public ASPDB(Context context) {
		
		super(context, ASP.Database.DB_NAME, null, ASP.Database.DB_VERSION);
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(ASP.Database.Profile.TBL_CREATE);
		db.execSQL(ASP.Database.Schedule.TBL_CREATE);
		db.execSQL(ASP.Database.Exception.TBL_CREATE);
		db.execSQL(ASP.Database.QueueItem.TBL_CREATE);
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL(ASP.Database.Profile.TBL_DROP);
		db.execSQL(ASP.Database.Schedule.TBL_DROP);
		db.execSQL(ASP.Database.Exception.TBL_DROP);
		db.execSQL(ASP.Database.QueueItem.TBL_DROP);
		onCreate(db);
		
	}
}
