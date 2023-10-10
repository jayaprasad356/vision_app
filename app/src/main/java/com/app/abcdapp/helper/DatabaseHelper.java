package com.app.abcdapp.helper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.abcdapp.model.GenerateCodes;
import com.app.abcdapp.model.GenerateEmails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "fortune.db";
    public static final String TABLE_CODES = "tblcodes";
    public static final String TABLE_MAILS = "tblmails";
    public static final String TABLE_URLS= "tblurls";
    public static final String KEY_ID = "pid";
    final String ID = "id";
    final String UID = "id";
    final String URL = "url";
    final String SEEN = "seen";
    final String STUDENT_NAME = "student_name";
    final String ID_NUMBER = "id_number";
    final String ECITY = "ecity";
    final String PIN_CODE = "pin_code";
    final String INSTITUTION = "institution";

    final String CODESTABLEINFO = TABLE_CODES + "(" + ID + " INTEGER ," + STUDENT_NAME + " TEXT ," + ID_NUMBER + " TEXT ," + ECITY + " TEXT ," + PIN_CODE + " TEXT)";
    final String INSTABLEINFO = TABLE_MAILS + "(" + ID + " INTEGER ," + STUDENT_NAME + " TEXT ," + ID_NUMBER + " TEXT ," + ECITY + " TEXT ," + INSTITUTION + " TEXT)";
    final String URLSTABLEINFO = TABLE_URLS + "(" + ID + " INTEGER ," + URL + " TEXT ," + SEEN + " INTEGER)";

    public DatabaseHelper(Activity activity) {
        super(activity, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CODESTABLEINFO);
        db.execSQL("CREATE TABLE " + INSTABLEINFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        replaceDataToNewTable(db, TABLE_CODES, CODESTABLEINFO);
        replaceDataToNewTable(db, TABLE_URLS, URLSTABLEINFO);
        replaceDataToNewTable(db, TABLE_MAILS, INSTABLEINFO);
        onCreate(db);
    }


    void replaceDataToNewTable(SQLiteDatabase db, String tableName, String tableString) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableString);

        List<String> columns = getColumns(db, tableName);
        db.execSQL("ALTER TABLE " + tableName + " RENAME TO temp_" + tableName);
        db.execSQL("CREATE TABLE " + tableString);

        columns.retainAll(getColumns(db, tableName));
        String cols = join(columns);
        db.execSQL(String.format("INSERT INTO %s (%s) SELECT %s from temp_%s",
                tableName, cols, cols, tableName));
        db.execSQL("DROP TABLE temp_" + tableName);
    }

    List<String> getColumns(SQLiteDatabase db, String tableName) {
        List<String> ar = null;
        try (Cursor c = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 1", null)) {
            if (c != null) {
                ar = new ArrayList<>(Arrays.asList(c.getColumnNames()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ar;
    }

    public void beginTransaction (SQLiteDatabase db){
        db.beginTransaction();
        try {
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    String join(List<String> list) {
        StringBuilder buf = new StringBuilder();
        int num = list.size();
        for (int i = 0; i < num; i++) {
            if (i != 0)
                buf.append(",");
            buf.append(list.get(i));
        }
        return buf.toString();
    }
    public int getCodesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CODES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public void deleteUrls() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_URLS);
        db.close();
    }
    public ArrayList<GenerateCodes> getLimitCodes() {
        final ArrayList<GenerateCodes> generateCodes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CODES + " ORDER BY RANDOM() LIMIT 1", null);
        if (cursor.moveToFirst()) {
            do {
                GenerateCodes generateCodes1 = new GenerateCodes(cursor.getString(cursor.getColumnIndexOrThrow(ID)),cursor.getString(cursor.getColumnIndexOrThrow(STUDENT_NAME))
                        ,cursor.getString(cursor.getColumnIndexOrThrow(ID_NUMBER)),cursor.getString(cursor.getColumnIndexOrThrow(ECITY)),cursor.getString(cursor.getColumnIndexOrThrow(PIN_CODE)));
                //@SuppressLint("Range") String count = cursor.getString(cursor.getColumnIndex(QTY));
                generateCodes.add(generateCodes1);
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return generateCodes;
    }

    public ArrayList<GenerateEmails> getLimitEmails() {
        final ArrayList<GenerateEmails> generateCodes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MAILS + " ORDER BY RANDOM() LIMIT 1", null);
        if (cursor.moveToFirst()) {
            do {
                GenerateEmails generateCodes1 = new GenerateEmails(cursor.getString(cursor.getColumnIndexOrThrow(ID)),cursor.getString(cursor.getColumnIndexOrThrow(STUDENT_NAME))
                        ,cursor.getString(cursor.getColumnIndexOrThrow(ID_NUMBER)),cursor.getString(cursor.getColumnIndexOrThrow(ECITY)),cursor.getString(cursor.getColumnIndexOrThrow(INSTITUTION)));
                //@SuppressLint("Range") String count = cursor.getString(cursor.getColumnIndex(QTY));
                generateCodes.add(generateCodes1);
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return generateCodes;
    }

    public ArrayList<GenerateCodes> getMissingCodes(int number) {
        final ArrayList<GenerateCodes> generateCodes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CODES + " ORDER BY RANDOM()  LIMIT "+number, null);
        if (cursor.moveToFirst()) {
            do {
                GenerateCodes generateCodes1 = new GenerateCodes(cursor.getString(cursor.getColumnIndexOrThrow(ID)),cursor.getString(cursor.getColumnIndexOrThrow(STUDENT_NAME))
                        ,cursor.getString(cursor.getColumnIndexOrThrow(ID_NUMBER)),cursor.getString(cursor.getColumnIndexOrThrow(ECITY)),cursor.getString(cursor.getColumnIndexOrThrow(PIN_CODE)));
                //@SuppressLint("Range") String count = cursor.getString(cursor.getColumnIndex(QTY));
                generateCodes.add(generateCodes1);
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return generateCodes;
    }
    public void AddtoUrl(String uid, String url, String seen) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(UID, uid);
            values.put(URL, url);
            values.put(SEEN, seen);
            db.insert(TABLE_URLS, null, values);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UpdateUrl(String uid, String url, String seen) {
    }



    public void deleteDb (Activity activity){
        activity.deleteDatabase(DATABASE_NAME);

    }




}