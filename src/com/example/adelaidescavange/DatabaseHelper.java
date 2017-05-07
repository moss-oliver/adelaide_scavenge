package com.example.adelaidescavange;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper {

	static public String getTitle(SQLiteDatabase db, boolean isFound, int notFoundTitleID, int foundTitleId){
		Cursor cursor2;
		if(isFound){
			 cursor2 = db.rawQuery("SELECT title FROM Title WHERE titleID = "+ foundTitleId, null);
		}
		else{
			 cursor2 = db.rawQuery("SELECT title FROM Title WHERE titleID = "+ notFoundTitleID, null);
		}
		cursor2.moveToNext();
		
		if(cursor2.getCount() < 1)
		{
			return "Unknown";
		}
		String str = cursor2.getString(cursor2.getColumnIndex("title"));
		cursor2.close();
		return MainActivity.unescape( str );
	}
	
	static public String getDescription(SQLiteDatabase db, boolean isFound, int notFoundTitleID, int foundTitleId){
		Cursor cursor2;
		if(isFound){
			 cursor2 = db.rawQuery("SELECT description FROM Title WHERE titleID = "+ foundTitleId, null);
		}
		else{
			 cursor2 = db.rawQuery("SELECT description FROM Title WHERE titleID = "+ notFoundTitleID, null);
		}
		cursor2.moveToNext();

		if(cursor2.getCount() < 1)
		{
			return "Unknown Location.";
		}
		
		String str = cursor2.getString(cursor2.getColumnIndex("description"));
		cursor2.close();
		return MainActivity.unescape( str );
	}
	
	static public String getCourseName(SQLiteDatabase db, int courseId){
		Cursor cursor2;
		cursor2 = db.rawQuery("SELECT courseName FROM Course WHERE courseID = "+ courseId, null);
		
		cursor2.moveToNext();
		
		if(cursor2.getCount() < 1)
		{
			return "Nameless Course";
		}
		String str = cursor2.getString(cursor2.getColumnIndex("courseName"));
		cursor2.close();
		return str;
	}
	
	static public String getFractionCourseCompleted(SQLiteDatabase db, int courseId){
		Cursor cursor2;
		int totalDingsInCourse = 0;
		int totalDingsFound = 0;
		cursor2 = db.rawQuery("SELECT Ding.dingId as dID, Ding.found as fnd FROM Ding INNER JOIN CourseLocation ON Ding.dingId = CourseLocation.DingId WHERE (((CourseLocation.courseId)="+courseId+"));",null);

//SELECT Count(dingID) FROM DingID LEFT JOIN CourseLocation ON   WHERE courseID = "+ courseId, null);
		
		//cursor2.moveToNext();
		
		if(cursor2.getCount() < 1)
		{
			return "?/??";
		}
		while(cursor2.moveToNext())
		{
			if(cursor2.getString(cursor2.getColumnIndex("fnd")).equals("TRUE")){
				totalDingsFound = totalDingsFound +1;
			}
			totalDingsInCourse = totalDingsInCourse+1;
		}
		
		String str = "" + totalDingsFound + "/" + totalDingsInCourse;
		cursor2.close();
		return str;
	}
}
