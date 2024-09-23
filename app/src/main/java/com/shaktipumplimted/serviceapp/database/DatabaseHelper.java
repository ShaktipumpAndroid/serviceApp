package com.shaktipumplimted.serviceapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.shaktipumplimted.serviceapp.Utils.common.model.ImageModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model.LocalConveyanceModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Shakti Service App";
    public static final int DATABASE_VERSION = Build.VERSION.SDK_INT;

    /*-------------------------------------------TABLE NAME---------------------------------------------------*/
    public static final String TABLE_COMPLAINT_IMAGE_DATA = "tbl_beneficiary_image_data";
    public static final String TABLE_LOCAL_CONVEYANCE_DATA = "tbl_local_conveyance_data";

    /*------------------------------------------------KET IDS--------------------------------------------------------*/


    public static final String KEY_ID = "key_id";
    public static final String KEY_IMAGE_NAME = "image_name";
    public static final String KEY_IMAGE_PATH = "image_path";
    public static final String KEY_IMAGE_SELECTED = "image_selected";
    public static final String KEY_IMAGE_BILL_NO = "image_billno";
    public static final String KEY_IMAGE_LATITUDE = "image_latitude";
    public static final String KEY_IMAGE_LONGITUDE = "image_longitude";
    public static final String KEY_IMAGE_POSITION = "image_position";

    public static final String KEY_START_LATITUDE = "start_latitude";

    public static final String KEY_START_LONGITUDE = "start_longitude";

    public static final String KEY_END_LATITUDE = "end_latitude";

    public static final String KEY_END_LONGITUDE = "end_longitude";

    public static final String KEY_START_ADDRESS = "start_address";

    public static final String KEY_END_ADDRESS = "end_address";

    public static final String KEY_START_DATE = "start_date";

    public static final String KEY_END_DATE = "end_date";

    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_END_TIME = "end_time";
    public static final String KEY_START_TRAVEL_IMG = "startTravelImg";
    public static final String KEY_END_TRAVEL_IMG = "endTravelImg";




    /*-----------------------------------------------------Create Image Tables---------------------------------------------*/
    private static final String CREATE_TABLE_COMPLAINT_IMAGES = "CREATE TABLE "
            + TABLE_COMPLAINT_IMAGE_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_IMAGE_NAME + " TEXT,"
            + KEY_IMAGE_PATH + " TEXT,"
            + KEY_IMAGE_SELECTED + " BOOLEAN,"
            + KEY_IMAGE_BILL_NO + " TEXT,"
            + KEY_IMAGE_LATITUDE + " TEXT,"
            + KEY_IMAGE_LONGITUDE + " TEXT,"
            + KEY_IMAGE_POSITION + " TEXT)";

    /*-----------------------------------------------------Local Conveyance Tables---------------------------------------------*/
    private static final String CREATE_TABLE_LOCAL_CONVEYANCE_DATA = "CREATE TABLE "
            + TABLE_LOCAL_CONVEYANCE_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_START_LATITUDE + " TEXT,"
            + KEY_START_LONGITUDE + " TEXT,"
            + KEY_END_LATITUDE + " TEXT,"
            + KEY_END_LONGITUDE + " TEXT,"
            + KEY_START_ADDRESS + " TEXT,"
            + KEY_END_ADDRESS + " TEXT,"
            + KEY_START_DATE + " TEXT,"
            + KEY_END_DATE + " TEXT,"
            + KEY_START_TIME + " TEXT,"
            + KEY_END_TIME + " TEXT,"
            + KEY_START_TRAVEL_IMG + " TEXT,"
            + KEY_END_TRAVEL_IMG + " TEXT)";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COMPLAINT_IMAGES);
        db.execSQL(CREATE_TABLE_LOCAL_CONVEYANCE_DATA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_IMAGE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAL_CONVEYANCE_DATA);
        onCreate(db);
    }



    /*------------------------------------------------ImageDatabase----------------------------------------------*/

    public void insertImagesData(ImageModel imageModel, boolean bool, String TableName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_IMAGE_NAME, imageModel.getName());
        contentValues.put(KEY_IMAGE_PATH, imageModel.getImagePath());
        contentValues.put(KEY_IMAGE_SELECTED, bool);
        contentValues.put(KEY_IMAGE_BILL_NO, imageModel.getBillNo());
        contentValues.put(KEY_IMAGE_LATITUDE, imageModel.getLatitude());
        contentValues.put(KEY_IMAGE_LONGITUDE, imageModel.getLongitude());
        contentValues.put(KEY_IMAGE_POSITION, imageModel.getPosition());
        database.insert(TableName, null, contentValues);
        database.close();
    }

    public void updateImagesData(ImageModel imageModel, boolean bool,String TableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_IMAGE_NAME, imageModel.getName());
        contentValues.put(KEY_IMAGE_PATH, imageModel.getImagePath());
        contentValues.put(KEY_IMAGE_SELECTED, bool);
        contentValues.put(KEY_IMAGE_BILL_NO, imageModel.getBillNo());
        contentValues.put(KEY_IMAGE_LATITUDE, imageModel.getLatitude());
        contentValues.put(KEY_IMAGE_LONGITUDE, imageModel.getLongitude());
        contentValues.put(KEY_IMAGE_POSITION, imageModel.getPosition());
          // update Row
        String where = KEY_IMAGE_NAME + "='" + imageModel.getName() + "'";
        db.update(TableName, contentValues, where, null);
        db.close();
    }
    public ArrayList<ImageModel> getAllImages(String TableName, String bill_no) {
        ArrayList<ImageModel> imageModelArrayList = new ArrayList<ImageModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (doesTableExist(database, TableName)) {
            String selectQuery = "SELECT  *  FROM " + TableName + " WHERE " + KEY_IMAGE_BILL_NO + " = '" + bill_no + "'";
            Cursor mcursor = database.rawQuery(selectQuery, null);

            imageModelArrayList.clear();
            ImageModel imageModel;
            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();
                    imageModel = new ImageModel();
                    imageModel.setID(mcursor.getString(0));
                    imageModel.setName(mcursor.getString(1));
                    imageModel.setImagePath(mcursor.getString(2));
                    imageModel.setImageSelected(Boolean.parseBoolean(mcursor.getString(3)));
                    imageModel.setBillNo(mcursor.getString(4));
                    imageModel.setLatitude(mcursor.getString(5));
                    imageModel.setLongitude(mcursor.getString(6));
                    imageModel.setPosition(mcursor.getInt(7));
                    imageModelArrayList.add(imageModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return  imageModelArrayList;
    }


    /*---------------------------------------------Local Conveyance Data--------------------------------------------------*/
    public void insertLocalConveyanceData(LocalConveyanceModel localConveyanceModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_START_LATITUDE, localConveyanceModel.getStartLatitude());
        contentValues.put(KEY_START_LONGITUDE, localConveyanceModel.getStartLongitude());
        contentValues.put(KEY_END_LATITUDE, localConveyanceModel.getEndLatitude());
        contentValues.put(KEY_END_LONGITUDE, localConveyanceModel.getEndLongitude());
        contentValues.put(KEY_START_ADDRESS, localConveyanceModel.getStartAddress());
        contentValues.put(KEY_END_ADDRESS, localConveyanceModel.getEndAddress());
        contentValues.put(KEY_START_DATE, localConveyanceModel.getStartDate());
        contentValues.put(KEY_END_DATE, localConveyanceModel.getEndDate());
        contentValues.put(KEY_START_TIME, localConveyanceModel.getStartTime());
        contentValues.put(KEY_END_TIME, localConveyanceModel.getEndTime());
        contentValues.put(KEY_START_TRAVEL_IMG, localConveyanceModel.getStartImgPath());
        contentValues.put(KEY_END_TRAVEL_IMG, localConveyanceModel.getEndImgPath());

        database.insert(TABLE_LOCAL_CONVEYANCE_DATA, null, contentValues);
        database.close();
    }

    public void updateLocalConveyanceData(LocalConveyanceModel localConveyanceModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.e("dbLat==>",localConveyanceModel.getEndLatitude());
        contentValues.put(KEY_START_LATITUDE, localConveyanceModel.getStartLatitude());
        contentValues.put(KEY_START_LONGITUDE, localConveyanceModel.getStartLongitude());
        contentValues.put(KEY_END_LATITUDE, localConveyanceModel.getEndLatitude());
        contentValues.put(KEY_END_LONGITUDE, localConveyanceModel.getEndLongitude());
        contentValues.put(KEY_START_ADDRESS, localConveyanceModel.getStartAddress());
        contentValues.put(KEY_END_ADDRESS, localConveyanceModel.getEndAddress());
        contentValues.put(KEY_START_DATE, localConveyanceModel.getStartDate());
        contentValues.put(KEY_END_DATE, localConveyanceModel.getEndDate());
        contentValues.put(KEY_START_TIME, localConveyanceModel.getStartTime());
        contentValues.put(KEY_END_TIME, localConveyanceModel.getEndTime());
        contentValues.put(KEY_START_TRAVEL_IMG, localConveyanceModel.getStartImgPath());
        contentValues.put(KEY_END_TRAVEL_IMG, localConveyanceModel.getEndImgPath());
        // update Row
     String   where = KEY_START_DATE + "='" + localConveyanceModel.getStartDate() + "'" + " AND " +
             KEY_START_TIME + "='" + localConveyanceModel.getStartTime() + "'" + " AND " +
             KEY_START_LATITUDE + "='" + localConveyanceModel.getStartLatitude() + "'" + " AND " +
             KEY_START_LONGITUDE + "='" + localConveyanceModel.getStartLongitude() + "'";
        db.update(TABLE_LOCAL_CONVEYANCE_DATA, contentValues, where, null);
        db.close();
    }


    public ArrayList<LocalConveyanceModel> getAllLocalConveyanceData(boolean isRetrieveLastData) {
        String selectQuery;
        ArrayList<LocalConveyanceModel> imageModelArrayList = new ArrayList<LocalConveyanceModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (doesTableExist(database, TABLE_LOCAL_CONVEYANCE_DATA)) {

            if(isRetrieveLastData) {
                 selectQuery = "SELECT * FROM " + TABLE_LOCAL_CONVEYANCE_DATA;
            }else {
                 selectQuery = "SELECT * FROM " + TABLE_LOCAL_CONVEYANCE_DATA + " WHERE " + KEY_END_DATE + " != '" + "" + "'" + " AND " + KEY_END_TIME + " != '" + "" + "'";
            }
            Cursor mcursor = database.rawQuery(selectQuery, null);

            imageModelArrayList.clear();
            LocalConveyanceModel localConveyanceModel;
            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();
                    localConveyanceModel = new LocalConveyanceModel();
                    localConveyanceModel.setUniqId(mcursor.getString(0));
                    localConveyanceModel.setStartLatitude(mcursor.getString(1));
                    localConveyanceModel.setStartLongitude(mcursor.getString(2));
                    localConveyanceModel.setEndLatitude(mcursor.getString(3));
                    localConveyanceModel.setEndLongitude(mcursor.getString(4));
                    localConveyanceModel.setStartAddress(mcursor.getString(5));
                    localConveyanceModel.setEndAddress(mcursor.getString(6));
                    localConveyanceModel.setStartDate(mcursor.getString(7));
                    localConveyanceModel.setEndDate(mcursor.getString(8));
                    localConveyanceModel.setStartTime(mcursor.getString(9));
                    localConveyanceModel.setEndTime(mcursor.getString(10));
                    localConveyanceModel.setStartImgPath(mcursor.getString(11));
                    localConveyanceModel.setEndImgPath(mcursor.getString(12));
                    imageModelArrayList.add(localConveyanceModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return  imageModelArrayList;
    }

  /*------------------------------------------Delete Database-----------------------------------------------------*/
    public void deleteData(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (doesTableExist(db, tableName)) {
            db.delete(tableName, null, null);
        }
    }

    public void deleteSpecificItem(String table, String Key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (doesTableExist(db, table)) {
            if (isRecordExist(table, Key, value)) {
                String where = Key + " = '" + value + "'";

                db.delete(table, where, null);
            }
        }
    }

    /*----------------------------------------------------Extra Methods--------------------------------------------------------*/
    public boolean isRecordExist(String tablename, String field, String fieldvalue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + tablename + " WHERE " + field + " = '" + fieldvalue + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public static boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }



}
