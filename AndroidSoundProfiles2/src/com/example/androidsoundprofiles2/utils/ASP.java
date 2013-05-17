package com.example.androidsoundprofiles2.utils;


public class ASP {
		
	public static final String DB_NAME = "asp_db";
	public static final int DB_VERSION = 1;

	public class Profiles {
		
		public static final String TBL_NAME = "profiles";
		public static final String TBL_CREATE = "CREATE TABLE profiles ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, "
				+ "vol_ring INTEGER NOT NULL, vol_notify INTEGER NOT NULL, "
				+ "vol_media INTEGER NOT NULL, vol_alarm INTEGER NOT NULL, "
				+ "vib_ring INTEGER NOT NULL, vib_notify INTEGER NOT NULL, " 
				+ "is_default INTEGER NOT NULL);";
		public static final String TBL_DROP = "DROP TABLE IF EXISTS profiles;";
		
		public static final String ID = "_id";
		public static final String NAME = "name";
		public static final String VOL_RING = "vol_ring";
		public static final String VOL_NOTIFY = "vol_notify";
		public static final String VOL_MEDIA = "vol_media";
		public static final String VOL_ALARM = "vol_alarm";
		public static final String VIB_RING = "vib_ring";
		public static final String VIB_NOTIFY = "vib_notify";
		public static final String IS_DEFAULT = "is_default";
		
	}
	
	public class Schedules {
		
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
		
	}
	
	public class Exceptions {
		
		public static final String TBL_NAME = "exceptions";
		public static final String TBL_CREATE = "CREATE TABLE exceptions ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, "
				+ "color INTEGER NOT NULL, pid INTEGER NOT NULL, "
				+ "start_utc INTEGER NOT NULL, length INTEGER NOT NULL, "
				+ "end_utc INTEGER NOT NULL);";
		public static final String TBL_DROP = "DROP TABLE IF EXISTS exceptions;";
		
		public static final String ID = "_id";
		public static final String NAME = "name";
		public static final String COLOR = "color";
		public static final String PID = "pid";
		public static final String START_UTC = "start_utc";
		public static final String LENGTH = "length";
		public static final String END_UTC = "end_utc";
		
	}
	
	public class Queue {
		
		public static final String TBL_NAME = "queue";
		public static final String TBL_CREATE = "CREATE TABLE queue ("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, color INTEGER NOT NULL, "
			+ "sid INTEGER, eid INTEGER, pid INTEGER NOT NULL, "
			+ "start_utc INTEGER NOT NULL, length INTEGER NOT NULL, "
			+ "end_utc INTEGER NOT NULL);";
		public static final String TBL_DROP = "DROP TABLE IF EXISTS queue";
		
		public static final String ID = "_id";
		public static final String COLOR = "color";
		public static final String SID = "sid";
		public static final String EID = "eid";
		public static final String PID = "pid";
		public static final String START_UTC = "start_utc";
		public static final String LENGTH = "length";
		public static final String END_UTC = "end_utc";
		
	}

}