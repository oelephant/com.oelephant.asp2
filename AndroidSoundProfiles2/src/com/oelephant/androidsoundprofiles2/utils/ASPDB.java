package com.oelephant.androidsoundprofiles2.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ASPDB extends SQLiteOpenHelper {
	
	public ASPDB(Context context) {
		
		super(context, ASP.DB_NAME, null, ASP.DB_VERSION);
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(ASP.Profiles.TBL_CREATE);
		db.execSQL(ASP.Schedules.TBL_CREATE);
		db.execSQL(ASP.Exceptions.TBL_CREATE);
		db.execSQL(ASP.Queue.TBL_CREATE);
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL(ASP.Profiles.TBL_DROP);
		db.execSQL(ASP.Schedules.TBL_DROP);
		db.execSQL(ASP.Exceptions.TBL_DROP);
		db.execSQL(ASP.Queue.TBL_DROP);
		onCreate(db);
		
	}
	
	public long createProfile(String name, int screen, int volRing, int volNotify, int volMedia, int volAlarm, int vibRing, int vibNotify, int isDefault) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ASP.Profiles.NAME, name);
		values.put(ASP.Profiles.SCREEN, screen);
		values.put(ASP.Profiles.VOL_RING, volRing);
		values.put(ASP.Profiles.VOL_NOTIFY, volNotify);
		values.put(ASP.Profiles.VOL_MEDIA, volMedia);
		values.put(ASP.Profiles.VOL_ALARM, volAlarm);
		values.put(ASP.Profiles.VIB_RING, vibRing);
		values.put(ASP.Profiles.VIB_NOTIFY, vibNotify);
		values.put(ASP.Profiles.IS_DEFAULT, isDefault);
		
		return db.insert(ASP.Profiles.TBL_NAME, null, values);
		
	}
	
	public long createSchedule(String name, int color, long pid, int sun, int mon, int tue, int wed, int thu, int fri, int sat, int start, int length) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.query(ASP.Schedules.TBL_NAME, null, null, null, null, null, null);
		
		Schedule newSchedule = new Schedule(name, color, pid, sun, mon, tue, wed, thu, fri, sat, start, length);
		
		if (c.getCount() > 0) {

			List<Schedule> schedules = new ArrayList<Schedule>();	
			c.moveToFirst();
			
			do {
				
				schedules.add(new Schedule(c));
				
			} while (c.moveToNext());
			
			for (Schedule schedule : schedules) {
				
				boolean isOverlap = newSchedule.overlaps(schedule);
				
				if (isOverlap) {
					
					return schedule.id;
					
				}
				
			}
			
		}
		
		ContentValues values = new ContentValues();
		values.put(ASP.Schedules.NAME, newSchedule.name);
		values.put(ASP.Schedules.COLOR, newSchedule.color);
		values.put(ASP.Schedules.PID, newSchedule.pid);
		values.put(ASP.Schedules.SUN, newSchedule.sun);
		values.put(ASP.Schedules.MON, newSchedule.mon);
		values.put(ASP.Schedules.TUE, newSchedule.tue);
		values.put(ASP.Schedules.WED, newSchedule.wed);
		values.put(ASP.Schedules.THU, newSchedule.thu);
		values.put(ASP.Schedules.FRI, newSchedule.fri);
		values.put(ASP.Schedules.SAT, newSchedule.sat);
		values.put(ASP.Schedules.START, newSchedule.start);
		values.put(ASP.Schedules.LENGTH, newSchedule.length);
		
		db.insert(ASP.Schedules.TBL_CREATE, null, values);
		
		return 0;
		
	}

	public long createException(String name, int color, long pid, long startUTC, long length) {
		
		long endUTC = startUTC + length;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.query(ASP.Exceptions.TBL_NAME, null, null, null, null, null, null);
		
		List<Exception> exceptions = new ArrayList<Exception>();
		
		if (c.getCount() > 0) {
			
			c.moveToFirst();
			
			do {
				
				exceptions.add(new Exception(c));
				
			} while (c.moveToNext());
			
		}
		
		for (Exception exception : exceptions) {
			
			if (startUTC >= exception.startUTC && startUTC < exception.endUTC) {
				
				return exception.id;
				
			}
			
			if (endUTC > exception.startUTC && endUTC <= exception.endUTC) {
				
				return exception.id;
				
			}
			
		}
		
		ContentValues values = new ContentValues();
		values.put(ASP.Exceptions.NAME, name);
		values.put(ASP.Exceptions.COLOR, color);
		values.put(ASP.Exceptions.PID, pid);
		values.put(ASP.Exceptions.START_UTC, startUTC);
		values.put(ASP.Exceptions.LENGTH, length);
		values.put(ASP.Exceptions.END_UTC, endUTC);
		
		db.insert(ASP.Exceptions.TBL_NAME, null, values);
		return 0;
		
	}
	
	public Profile getProfile(long id) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(ASP.Profiles.TBL_NAME, null, ASP.Profiles.ID + "=" + id, null, null, null, null);
		
		if (c.getCount() > 0) {
			
			c.moveToFirst();
			return new Profile(c);
			
		}
		
		return null;
		
	}
	
	public Profile getDefaultProfile() {
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(ASP.Profiles.TBL_NAME, null, ASP.Profiles.IS_DEFAULT + "=1", null, null, null, null);
		
		if (c.getCount() > 0) {
			
			c.moveToFirst();
			return new Profile(c);
			
		}
		
		return null;
		
	}
	
	public Schedule getSchedule(long id) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(ASP.Schedules.TBL_NAME, null, ASP.Schedules.ID + "=" + id, null, null, null, null);
		
		if (c.getCount() > 0) {
			
			c.moveToFirst();
			return new Schedule(c);
			
		}
		
		return null;
		
	}
	
	public Exception getException(long id) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(ASP.Exceptions.TBL_NAME, null, ASP.Exceptions.ID + "=" + id, null, null, null, null);
		
		if (c.getCount() > 0) {
			
			c.moveToFirst();
			return new Exception(c);
			
		}
		
		return null;
		
	}
	
	public boolean deleteProfile(long id) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.query(ASP.Profiles.TBL_NAME, null, ASP.Profiles.ID + "=" + id, null, null, null, null);
		
		boolean isDefault = false;
		
		if (c.getCount() > 0) {
			
			isDefault = true;
			
		}
		
		db.delete(ASP.Profiles.TBL_NAME, ASP.Profiles.ID + "=" + id, null);
		db.delete(ASP.Schedules.TBL_NAME, ASP.Schedules.PID + "=" + id, null);
		db.delete(ASP.Exceptions.TBL_NAME, ASP.Exceptions.PID + "=" + id, null);
		
		return isDefault;
		
	}
	
	public void deleteSchedule(long id) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(ASP.Schedules.TBL_NAME, ASP.Schedules.PID + "=" + id, null);
		
	}
	
	public void deleteException(long id) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(ASP.Exceptions.TBL_NAME, ASP.Exceptions.PID + "=" + id, null);
		
	}
	
	public void editProfile(long id, String name, int screen, int volRing, int volNotify, int volMedia, int volAlarm, int vibRing, int vibNotify) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ASP.Profiles.NAME, name);
		values.put(ASP.Profiles.SCREEN, screen);
		values.put(ASP.Profiles.VOL_RING, volRing);
		values.put(ASP.Profiles.VOL_NOTIFY, volNotify);
		values.put(ASP.Profiles.VOL_MEDIA, volMedia);
		values.put(ASP.Profiles.VOL_ALARM, volAlarm);
		values.put(ASP.Profiles.VIB_RING, vibRing);
		values.put(ASP.Profiles.VIB_NOTIFY, vibNotify);
		
		db.update(ASP.Profiles.TBL_NAME, values, ASP.Profiles.ID + "=" + id, null);
		
	}
	
	public long editSchedule(long id, String name, int color, long pid, int sun, int mon, int tue, int wed, int thu, int fri, int sat, int start, int length) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.query(ASP.Schedules.TBL_NAME, null, null, null, null, null, null);
		
		Schedule newSchedule = new Schedule(name, color, pid, sun, mon, tue, wed, thu, fri, sat, start, length);
		
		if (c.getCount() > 0) {

			List<Schedule> schedules = new ArrayList<Schedule>();	
			c.moveToFirst();
			
			do {
				
				schedules.add(new Schedule(c));
				
			} while (c.moveToNext());
			
			for (Schedule schedule : schedules) {

				if (schedule.id == id) {
					
					continue;
					
				}
				
				boolean isOverlap = newSchedule.overlaps(schedule);
				
				if (isOverlap) {
					
					return schedule.id;
					
				}
				
			}
			
		}
		
		ContentValues values = new ContentValues();
		values.put(ASP.Schedules.NAME, newSchedule.name);
		values.put(ASP.Schedules.COLOR, newSchedule.color);
		values.put(ASP.Schedules.PID, newSchedule.pid);
		values.put(ASP.Schedules.SUN, newSchedule.sun);
		values.put(ASP.Schedules.MON, newSchedule.mon);
		values.put(ASP.Schedules.TUE, newSchedule.tue);
		values.put(ASP.Schedules.WED, newSchedule.wed);
		values.put(ASP.Schedules.THU, newSchedule.thu);
		values.put(ASP.Schedules.FRI, newSchedule.fri);
		values.put(ASP.Schedules.SAT, newSchedule.sat);
		values.put(ASP.Schedules.START, newSchedule.start);
		values.put(ASP.Schedules.LENGTH, newSchedule.length);
		
		db.update(ASP.Schedules.TBL_CREATE, values, ASP.Schedules.ID + "=" + id, null);
		
		return 0;
		
	}

	public long editException(long id, String name, int color, long pid, long startUTC, long length) {
		
		long endUTC = startUTC + length;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.query(ASP.Exceptions.TBL_NAME, null, null, null, null, null, null);
		
		List<Exception> exceptions = new ArrayList<Exception>();
		
		if (c.getCount() > 0) {
			
			c.moveToFirst();
			
			do {
				
				exceptions.add(new Exception(c));
				
			} while (c.moveToNext());
			
		}
		
		for (Exception exception : exceptions) {
			
			if (exception.id == id) {
				
				continue;
				
			}
			
			if (startUTC >= exception.startUTC && startUTC < exception.endUTC) {
				
				return exception.id;
				
			}
			
			if (endUTC > exception.startUTC && endUTC <= exception.endUTC) {
				
				return exception.id;
				
			}
			
		}
		
		ContentValues values = new ContentValues();
		values.put(ASP.Exceptions.NAME, name);
		values.put(ASP.Exceptions.COLOR, color);
		values.put(ASP.Exceptions.PID, pid);
		values.put(ASP.Exceptions.START_UTC, startUTC);
		values.put(ASP.Exceptions.LENGTH, length);
		values.put(ASP.Exceptions.END_UTC, endUTC);
		
		db.update(ASP.Exceptions.TBL_NAME, values, ASP.Exceptions.ID + "=" + id, null);
		return 0;
		
	}
	
	public void createQueue(int depthInWeeks) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c1, c2;
		
		db.execSQL(ASP.Queue.TBL_DROP);
		db.execSQL(ASP.Queue.TBL_CREATE);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		String[] days = new String[] {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
		
		for (int i = 0; i < depthInWeeks; i++) {
			
			for (String day : days) {
				
				c1 = db.query(ASP.Schedules.TBL_NAME, null, day + " = 1", null, null, null, ASP.Schedules.START + " ASC");
				
				if (c1.getCount() > 0) {
					
					c1.moveToFirst();
					
					do {

						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);
						cal.add(Calendar.MILLISECOND, c1.getInt(c1.getColumnIndex("start")));
						
						long startUTC = cal.getTimeInMillis();
						long length = c1.getLong(c1.getColumnIndex(ASP.Schedules.LENGTH));
						long endUTC = startUTC + length;
						
						ContentValues values = new ContentValues();
						values.put(ASP.Queue.SID, c1.getLong(c1.getColumnIndex(ASP.Schedules.ID)));
						values.put(ASP.Queue.PID, c1.getLong(c1.getColumnIndex(ASP.Schedules.PID)));
						values.put(ASP.Queue.COLOR, c1.getInt(c1.getColumnIndex(ASP.Schedules.COLOR)));
						values.put(ASP.Queue.START_UTC, startUTC);
						values.put(ASP.Queue.LENGTH, length);
						values.put(ASP.Queue.END_UTC, endUTC);
						
						db.insert(ASP.Queue.TBL_NAME, null, values);
					
					} while (c1.moveToNext());
				
				}

				cal.add(Calendar.DAY_OF_YEAR, 1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
			}
		}
		
		c1 = db.query(ASP.Exceptions.TBL_NAME, null, null, null, null, null, null);
		
		if (c1.getCount() > 0) {
			
			c1.moveToFirst();
			
			do {
				
				long startUTC = c1.getLong(c1.getColumnIndex(ASP.Exceptions.START_UTC));
				long endUTC = c1.getLong(c1.getColumnIndex(ASP.Exceptions.END_UTC));
				
				c2 = db.query(ASP.Queue.TBL_NAME, null, 
						ASP.Queue.START_UTC + ">=" + startUTC + " AND " + ASP.Queue.START_UTC + "<" + endUTC, 
						null, null, null, null);
				
				if (c2.getCount() > 0) {
					
					c2.moveToFirst();
					
					do {
						
						long id = c2.getLong(c2.getColumnIndex(ASP.Queue.ID));
						long newStartUTC = endUTC;
						long oldEndUTC = c2.getLong(c2.getColumnIndex(ASP.Queue.END_UTC));
						long newLength = oldEndUTC - newStartUTC;
						
						ContentValues values = new ContentValues();
						values.put(ASP.Queue.START_UTC, newStartUTC);
						values.put(ASP.Queue.LENGTH, newLength);
						
						db.update(ASP.Queue.TBL_NAME, values, ASP.Queue.ID + "=" + id, null);
						
					} while (c2.moveToNext());
					
				}
				
				c2 = db.query(ASP.Queue.TBL_NAME, null, 
						ASP.Queue.END_UTC + ">=" + startUTC + " AND " + ASP.Queue.END_UTC + "<=" + endUTC, 
						null, null, null, null);
				
				if (c2.getCount() > 0) {
					
					c2.moveToFirst();
					
					do {
						
						long id = c2.getLong(c2.getColumnIndex(ASP.Queue.ID));
						long newEndUTC = startUTC;
						long oldStartUTC = c2.getLong(c2.getColumnIndex(ASP.Queue.START_UTC));
						long newLength = newEndUTC - oldStartUTC;
						
						ContentValues values = new ContentValues();
						values.put(ASP.Queue.END_UTC, newEndUTC);
						values.put(ASP.Queue.LENGTH, newLength);
						
						db.update(ASP.Queue.TBL_NAME, values, ASP.Queue.ID + "=" + id, null);
						
					} while (c2.moveToNext());
				
				}
				
				ContentValues values = new ContentValues();
				values.put(ASP.Queue.EID, c1.getLong(c1.getColumnIndex(ASP.Exceptions.ID)));
				values.put(ASP.Queue.PID, c1.getLong(c1.getColumnIndex(ASP.Exceptions.PID)));
				values.put(ASP.Queue.COLOR, c1.getInt(c1.getColumnIndex(ASP.Exceptions.COLOR)));
				values.put(ASP.Queue.START_UTC, startUTC);
				values.put(ASP.Queue.LENGTH, endUTC - startUTC);
				values.put(ASP.Queue.END_UTC, endUTC);
				
				db.insert(ASP.Queue.TBL_NAME, null, values);
				
			} while (c1.moveToNext());
			
		}
		
	}
	
	public ArrayList<QueueItem> getQueue() {
		
		ArrayList<QueueItem> queue = new ArrayList<QueueItem>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(ASP.Queue.TBL_NAME, null, null, null, null, null, ASP.Queue.START_UTC + " ASC");
		
		if (c.getCount() > 0) {
			
			c.moveToFirst();
			
			do {
				
				queue.add(new QueueItem(c));
				
			} while (c.moveToNext());
			
		}
		
		return queue;
		
	}

	public class Profile {
		
		private long id;
		private String name;
		private int screen, volRing, volNotify, volMedia, volAlarm, vibRing, vibNotify, isDefault;
		
		public Profile(Cursor c) {
			
			id = c.getLong(c.getColumnIndex(ASP.Profiles.ID));
			name = c.getString(c.getColumnIndex(ASP.Profiles.NAME));
			screen = c.getInt(c.getColumnIndex(ASP.Profiles.SCREEN));
			volRing = c.getInt(c.getColumnIndex(ASP.Profiles.VOL_RING));
			volNotify= c.getInt(c.getColumnIndex(ASP.Profiles.VOL_NOTIFY));
			volMedia = c.getInt(c.getColumnIndex(ASP.Profiles.VOL_MEDIA));
			volAlarm = c.getInt(c.getColumnIndex(ASP.Profiles.VOL_ALARM));
			vibRing = c.getInt(c.getColumnIndex(ASP.Profiles.VIB_RING));
			vibRing = c.getInt(c.getColumnIndex(ASP.Profiles.VIB_NOTIFY));
			isDefault = c.getInt(c.getColumnIndex(ASP.Profiles.IS_DEFAULT));
			
		}
		
		public Profile(String name, int screen, int volRing, int volNotify, int volMedia, int volAlarm, int vibRing, int vibNotify, int isDefault) {
			
			this(0, name, screen, volRing, volNotify, volMedia, volAlarm, vibRing, vibNotify, isDefault);
			
		}
		
		public Profile(long id, String name, int screen, int volRing, int volNotify, int volMedia, int volAlarm, int vibRing, int vibNotify, int isDefault) {
			
			this.id = id;
			this.name = name;
			this.screen = screen;
			this.volRing = volRing;
			this.volNotify = volNotify;
			this.volMedia = volMedia;
			this.volAlarm = volAlarm;
			this.vibRing = vibRing;
			this.vibNotify = vibNotify;
			this.isDefault = isDefault;
			
		}
		
		public long getID() {
			
			return id;
			
		}
		
		public String getName() {
			
			return name;
		
		}
		
		public int getScreen() {
			
			return screen;
			
		}
		
		public int getVol(String which) {
			
			if (which == ASP.Profiles.VOL_RING) {
				
				return volRing;
				
			} else if (which == ASP.Profiles.VOL_NOTIFY) {
				
				return volNotify;
				
			} else if (which == ASP.Profiles.VOL_MEDIA) {
				
				return volMedia;
				
			} else if (which == ASP.Profiles.VOL_ALARM) {
				
				return volAlarm;
				
			}
			
			return 0;
		
		}
		
		public int getVib(String which) {
			
			if (which == ASP.Profiles.VIB_RING) {
				
				return vibRing;
			
			} else if (which == ASP.Profiles.VIB_NOTIFY) {
				
				return vibNotify;
				
			}
			
			return 0;
			
		}
		
		public int isDefault() {
			
			return isDefault;
			
		}
		
	}
	
	public class Schedule {
		
		private long id, pid, start, length;
		private String name;
		private int color, sun, mon, tue, wed, thu, fri, sat;
		
		public Schedule(Cursor c) {
			
			id = c.getLong(c.getColumnIndex(ASP.Schedules.ID));
			name = c.getString(c.getColumnIndex(ASP.Schedules.NAME));
			color = c.getInt(c.getColumnIndex(ASP.Schedules.COLOR));
			pid = c.getLong(c.getColumnIndex(ASP.Schedules.PID));
			sun = c.getInt(c.getColumnIndex(ASP.Schedules.SUN));
			mon = c.getInt(c.getColumnIndex(ASP.Schedules.MON));
			tue = c.getInt(c.getColumnIndex(ASP.Schedules.TUE));
			wed = c.getInt(c.getColumnIndex(ASP.Schedules.WED));
			thu = c.getInt(c.getColumnIndex(ASP.Schedules.THU));
			fri = c.getInt(c.getColumnIndex(ASP.Schedules.FRI));
			sat = c.getInt(c.getColumnIndex(ASP.Schedules.SAT));
			start = c.getLong(c.getColumnIndex(ASP.Schedules.START));
			length = c.getLong(c.getColumnIndex(ASP.Schedules.LENGTH));
			
		}
		
		public Schedule(String name, int color, long pid, int sun, int mon, int tue, int wed, int thu, int fri, int sat, long start, long length) {
			
			this(0, name, color, pid, sun, mon, tue, wed, thu, fri, sat, start, length);
			
		}
		
		public Schedule(long id, String name, int color, long pid, int sun, int mon, int tue, int wed, int thu, int fri, int sat, long start, long length) {

			this.id = id;
			this.name = name;
			this.color = color;
			this.pid = pid;
			this.sun = sun;
			this.mon = mon;
			this.tue = tue;
			this.wed = wed;
			this.thu = thu;
			this.fri = fri;
			this.sat = sat;
			this.start = start;
			this.length = length;
		
		}

		public long getID() {
			
			return id;
			
		}
		
		public String getName() {
			
			return name;
			
		}
		
		public int getColor() {
			
			return color;
			
		}
		
		public long getPID() {
			
			return pid;
			
		}
		
		public int getSun() {
			
			return sun;
			
		}
		
		public int getMon() {
			
			return mon;
			
		}
		
		public int getTue() {
			
			return tue;
		
		}
		
		public int getWed() {
			
			return wed;
			
		}
		
		public int getThu() {
			
			return thu;
			
		}
		
		public int getFri() {
			
			return fri;
		
		}
		
		public int getSat() {
			
			return sat;
			
		}
		
		public long getStart() {
			
			return start;
			
		}
		
		public long getLength() {
			
			return length;
			
		}
		
		public boolean overlaps(Schedule schedule) {

			ArrayList<long[]> thisTestVector = getTestVector();
			ArrayList<long[]> scheduleTestVector = getTestVector();
			
			for (int i = 0; i < thisTestVector.size(); i++) {
				
				for (int j = 0; j < scheduleTestVector.size(); j++) {
					
					if (thisTestVector.get(i)[0] >= scheduleTestVector.get(j)[0] &&
							thisTestVector.get(i)[0] < scheduleTestVector.get(j)[1]) {
						
						return true;
						
					}

					if (thisTestVector.get(i)[1] > scheduleTestVector.get(j)[0] &&
							thisTestVector.get(i)[1] <= scheduleTestVector.get(j)[1]) {
						
						return true;
						
					}
					
				}
				
			}
			
			return false;
			
		}
		
		public ArrayList<long[]> getTestVector() {
			
			int[] days = new int[] {sun, mon, tue, wed, thu, fri, sat};
			ArrayList<long[]> list = new ArrayList<long[]>();
			
			for (int day = 0; day < days.length; day++) {
				
				if (days[day] == 1) {
					
					long startUTC = 24 * 60 * 60 * 1000 * day + start;
					long endUTC = startUTC + length;
					list.add(new long[] {startUTC, endUTC});
					
				}
				
			}
			
			return list;
			
		}
		
	}
	
	public class Exception {
		
		private long id, pid, startUTC, length, endUTC;
		private String name;
		private int color;;
		
		public Exception(Cursor c) {
			
			id = c.getLong(c.getColumnIndex(ASP.Exceptions.ID));
			name = c.getString(c.getColumnIndex(ASP.Exceptions.NAME));
			color = c.getInt(c.getColumnIndex(ASP.Exceptions.COLOR));
			pid = c.getLong(c.getColumnIndex(ASP.Exceptions.PID));
			startUTC = c.getLong(c.getColumnIndex(ASP.Exceptions.START_UTC));
			length = c.getLong(c.getColumnIndex(ASP.Exceptions.LENGTH));
			endUTC = c.getLong(c.getColumnIndex(ASP.Exceptions.END_UTC));
			
		}
		
		public Exception(String name, int color, long pid, long startUTC, long length) {
			
			this(0, name, color, pid, startUTC, length, (startUTC + length));
			
		}
		
		public Exception(String name, int color, long pid, long startUTC, long length, long endUTC) {
			
			this(0, name, color, pid, startUTC, length, endUTC);
			
		}

		public Exception(long id, String name, int color, long pid, long startUTC, long length) {
			

			this(id, name, color, pid, startUTC, length, (startUTC + length));
			
		}

		public Exception(long id, String name, int color, long pid, long startUTC, long length, long endUTC) {
			
			this.id = id;
			this.name = name;
			this.color = color;
			this.pid = pid;
			this.startUTC = startUTC;
			this.length = length;
			this.endUTC = endUTC;
			
		}
		
		public long getID() {
			
			return id;
			
		}
		
		public String getName() {
			
			return name;
			
		}
		
		public int getColor() {
			
			return color;
			
		}
		
		public long getPID() {
			
			return pid;
			
		}
		
		public long getStartUTC() {
			
			return startUTC;
			
		}
		
		public long getLength() {
			
			return length;
			
		}
		
		public long getEndUTC() {
			
			return endUTC;
			
		}
		
	}
	
	public class QueueItem {
		
		private long id, sid, eid, pid, startUTC, length, endUTC;
		private int color;
		
		public QueueItem(Cursor c) {
		
			id = c.getLong(c.getColumnIndex(ASP.Queue.ID));
			color = c.getInt(c.getColumnIndex(ASP.Queue.COLOR));
			sid = c.getInt(c.getColumnIndex(ASP.Queue.SID));
			eid = c.getInt(c.getColumnIndex(ASP.Queue.EID));
			pid = c.getInt(c.getColumnIndex(ASP.Queue.PID));
			startUTC = c.getInt(c.getColumnIndex(ASP.Queue.START_UTC));
			length = c.getInt(c.getColumnIndex(ASP.Queue.LENGTH));
			endUTC = c.getInt(c.getColumnIndex(ASP.Queue.END_UTC));
		
		}
		
		public QueueItem(int color, long sid, long eid, long pid, long startUTC, long length, long endUTC) {
			
			this(0, color, sid, eid, pid, startUTC, length, endUTC);
			
		}
		
		public QueueItem(long id, int color, long sid, long eid, long pid, long startUTC, long length, long endUTC) {
			
			this.id = id;
			this.color = color;
			this.sid = sid;
			this.eid = eid;
			this.pid = pid;
			this.startUTC = startUTC;
			this.length = length;
			this.endUTC = endUTC;
			
		}
		
		public long getID() {
			
			return id;
			
		}
		
		public int getColor() {
			
			return color;
			
		}
		
		public long getSID() {
			
			return sid;
			
		}
		
		public long getEID() {
			
			return eid;
			
		}
		
		public long getPID() {
			
			return pid;
			
		}
		
		public long getStartUTC() {
			
			return startUTC;
			
		}
		
		public long getLength() {
			
			return length;
			
		}
		
		public long getEndUTC() {
			
			return endUTC;
			
		}
		
	}
	
}
