package com.example.androidsoundprofiles2.utils;

import android.database.Cursor;

public class ASP {
	
	public class Database {
		
		public static final String DB_NAME = "asp_db";
		public static final int DB_VERSION = 1;
	
		public class Profile {
			
			// database values
			public static final String TBL_NAME = "profiles";
			public static final String TBL_CREATE = "CREATE TABLE profiles ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, "
					+ "vol_ring INTEGER NOT NULL, vol_notify INTEGER NOT NULL, "
					+ "vol_media INTEGER NOT NULL, vol_alarm INTEGER NOT NULL, "
					+ "vib_ring INTEGER NOT NULL, vib_notify INTEGER NOT NULL, " 
					+ "is_default INTEGER NOT NULL);";
			public static final String TBL_DROP = "DROP TABLE IF EXISTS profiles;";
			
			// table values
			public static final String ID = "_id";
			public static final String NAME = "name";
			public static final String VOL_RING = "vol_ring";
			public static final String VOL_NOTIFY = "vol_notify";
			public static final String VOL_MEDIA = "vol_media";
			public static final String VOL_ALARM = "vol_alarm";
			public static final String VIB_RING = "vib_ring";
			public static final String VIB_NOTIFY = "vib_notify";
			public static final String IS_DEFAULT = "is_default";
			
			// object values
			private long id;
			private String name;
			private int volRing, volNotify, volMedia, volAlarm, vibRing, vibNotify, isDefault;
			
			public Profile(Cursor c) {
				
				id = c.getLong(c.getColumnIndex(ID));
				name = c.getString(c.getColumnIndex(NAME));
				volRing = c.getInt(c.getColumnIndex(VOL_RING));
				volNotify= c.getInt(c.getColumnIndex(VOL_NOTIFY));
				volMedia = c.getInt(c.getColumnIndex(VOL_MEDIA));
				volAlarm = c.getInt(c.getColumnIndex(VOL_ALARM));
				vibRing = c.getInt(c.getColumnIndex(VIB_RING));
				vibRing = c.getInt(c.getColumnIndex(VIB_NOTIFY));
				isDefault = c.getInt(c.getColumnIndex(IS_DEFAULT));
				
			}
			
			public Profile(String name, int volRing, int volNotify, int volMedia, int volAlarm, int vibRing, int vibNotify, int isDefault) {
				
				this(0, name, volRing, volNotify, volMedia, volAlarm, vibRing, vibNotify, isDefault);
				
			}
			
			public Profile(long id, String name, int volRing, int volNotify, int volMedia, int volAlarm, int vibRing, int vibNotify, int isDefault) {
				
				this.id = id;
				this.name = name;
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
			
			public int getVol(String which) {
				
				if (which == VOL_RING) {
					
					return volRing;
					
				} else if (which == VOL_NOTIFY) {
					
					return volNotify;
					
				} else if (which == VOL_MEDIA) {
					
					return volMedia;
					
				} else if (which == VOL_ALARM) {
					
					return volAlarm;
					
				}
				
				return 0;
			
			}
			
			public int getVib(String which) {
				
				if (which == VIB_RING) {
					
					return vibRing;
				
				} else if (which == VIB_NOTIFY) {
					
					return vibNotify;
					
				}
				
				return 0;
				
			}
			
			public int isDefault() {
				
				return isDefault;
				
			}
			
		}
		
		public class Schedule {
			
			// database values
			public static final String TBL_NAME = "schedules";
			public static final String TBL_CREATE = "CREATE TABLE schedules ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, "
					+ "color INTEGER NOT NULL, pid INTEGER NOT NULL, "
					+ "sun INTEGER NOT NULL, mon INTEGER NOT NULL, "
					+ "tue INTEGER NOT NULL, wed INTEGER NOT NULL, "
					+ "thu INTEGER NOT NULL, fri INTEGER NOT NULL, "
					+ "sat INTEGER NOT NULL, start INTEGER NOT NULL, "
					+ "length INTEGER NOT NULL);";
			public static final String TBL_DROP = "DROP TABLE IF EXISTS schedules;";
			
			// table values
			public static final String ID = "_id";
			public static final String NAME = "name";
			public static final String COLOR = "color";
			public static final String PID = "pid";
			public static final String SUN = "sun";
			public static final String MON = "mon";
			public static final String TUE = "tue";
			public static final String WED = "wed";
			public static final String THU = "thu";
			public static final String FRI = "fri";
			public static final String SAT = "sat";
			public static final String START = "start";
			public static final String LENGTH = "length";
			
			// object values
			private long id, pid, start, length;
			private String name;
			private int color, sun, mon, tue, wed, thu, fri, sat;
			
			public Schedule(Cursor c) {
				
				id = c.getLong(c.getColumnIndex(ID));
				name = c.getString(c.getColumnIndex(NAME));
				color = c.getInt(c.getColumnIndex(COLOR));
				pid = c.getLong(c.getColumnIndex(PID));
				sun = c.getInt(c.getColumnIndex(SUN));
				mon = c.getInt(c.getColumnIndex(MON));
				tue = c.getInt(c.getColumnIndex(TUE));
				wed = c.getInt(c.getColumnIndex(WED));
				thu = c.getInt(c.getColumnIndex(THU));
				fri = c.getInt(c.getColumnIndex(FRI));
				sat = c.getInt(c.getColumnIndex(SAT));
				start = c.getLong(c.getColumnIndex(START));
				length = c.getLong(c.getColumnIndex(LENGTH));
				
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
			
		}
		
		public class Exception {
			
			// database values
			public static final String TBL_NAME = "exceptions";
			public static final String TBL_CREATE = "CREATE TABLE exceptions ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, "
					+ "color INTEGER NOT NULL, pid INTEGER NOT NULL, "
					+ "start_utc INTEGER NOT NULL, length INTEGER NOT NULL, "
					+ "end_utc INTEGER NOT NULL);";
			public static final String TBL_DROP = "DROP TABLE IF EXISTS exceptions;";
			
			// table values
			public static final String ID = "_id";
			public static final String NAME = "name";
			public static final String COLOR = "color";
			public static final String PID = "pid";
			public static final String START_UTC = "start_utc";
			public static final String LENGTH = "length";
			public static final String END_UTC = "end_utc";
			
			// object values
			private long id, pid, startUTC, length, endUTC;
			private String name;
			private int color;;
			
			public Exception(Cursor c) {
				
				id = c.getLong(c.getColumnIndex(ID));
				name = c.getString(c.getColumnIndex(NAME));
				color = c.getInt(c.getColumnIndex(COLOR));
				pid = c.getLong(c.getColumnIndex(PID));
				startUTC = c.getLong(c.getColumnIndex(START_UTC));
				length = c.getLong(c.getColumnIndex(LENGTH));
				endUTC = c.getLong(c.getColumnIndex(END_UTC));
				
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
			
			// database values
			public static final String TBL_NAME = "queue";
			public static final String TBL_CREATE = "CREATE TABLE queue ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, color INTEGER NOT NULL, "
				+ "sid INTEGER, eid INTEGER, pid INTEGER NOT NULL, "
				+ "start_utc INTEGER NOT NULL, length INTEGER NOT NULL, "
				+ "end_utc INTEGER NOT NULL);";
			public static final String TBL_DROP = "DROP TABLE IF EXISTS queue";
			
			// table values
			public static final String ID = "_id";
			public static final String COLOR = "color";
			public static final String SID = "sid";
			public static final String EID = "eid";
			public static final String PID = "pid";
			public static final String START_UTC = "start_utc";
			public static final String LENGTH = "length";
			public static final String END_UTC = "end_utc";
			
			// class values
			private long id, sid, eid, pid, startUTC, length, endUTC;
			private int color;
			
			public QueueItem(Cursor c) {
			
				id = c.getLong(c.getColumnIndex(ID));
				color = c.getInt(c.getColumnIndex(COLOR));
				sid = c.getInt(c.getColumnIndex(SID));
				eid = c.getInt(c.getColumnIndex(EID));
				pid = c.getInt(c.getColumnIndex(PID));
				startUTC = c.getInt(c.getColumnIndex(START_UTC));
				length = c.getInt(c.getColumnIndex(LENGTH));
				endUTC = c.getInt(c.getColumnIndex(END_UTC));
			
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

}