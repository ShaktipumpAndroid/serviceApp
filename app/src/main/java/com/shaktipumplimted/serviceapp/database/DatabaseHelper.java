package com.shaktipumplimted.serviceapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.shaktipumplimted.serviceapp.Utils.common.model.ImageModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Shakti Service App";
    public static final int DATABASE_VERSION = Build.VERSION.SDK_INT;

    /*-------------------------------------------TABLE NAME---------------------------------------------------*/
    public static final String TABLE_COMPLAINT_IMAGE_DATA = "tbl_beneficiary_image_data";

    /*------------------------------------------------KET IDS--------------------------------------------------------*/


    public static final String KEY_ID = "key_id";
    public static final String KEY_IMAGE_NAME = "image_name";
    public static final String KEY_IMAGE_PATH = "image_path";
    public static final String KEY_IMAGE_SELECTED = "image_selected";
    public static final String KEY_IMAGE_BILL_NO = "image_billno";
    public static final String KEY_IMAGE_LATITUDE = "image_latitude";
    public static final String KEY_IMAGE_LONGITUDE = "image_longitude";
    public static final String KEY_IMAGE_POSITION = "image_position";


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




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COMPLAINT_IMAGES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_IMAGE_DATA);
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
