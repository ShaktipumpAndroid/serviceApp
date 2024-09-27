package com.shaktipumplimted.serviceapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.shaktipumplimted.serviceapp.Utils.common.model.ImageModel;
import com.shaktipumplimted.serviceapp.Utils.common.model.SpinnerDataModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintStatusModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model.LocalConveyanceModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.MarkAttendanceModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Shakti Service App";
    public static final int DATABASE_VERSION = Build.VERSION.SDK_INT;

    /*-------------------------------------------TABLE NAME---------------------------------------------------*/

    public static final String TABLE_COMPLAINT_STATUS_DATA = "tbl_complaint_status_data";
    public static final String TABLE_COMPLAINT_IMAGE_DATA = "tbl_complaint_image_data";
    public static final String TABLE_SITE_SURVEY_IMAGE_DATA = "tbl_site_survey_image_data";
    public static final String TABLE_CHECK_OUT_IMAGE_DATA = "tbl_checkout_image_data";
    public static final String TABLE_LOCAL_CONVEYANCE_DATA = "tbl_local_conveyance_data";

    public static final String TABLE_MARK_ATTENDANCE_DATA = "tbl_mark_attendance_data";
    public static final String TABLE_COMPLAINT_DATA = "tbl_complaint_data";

    public static final String TABLE_PENDING_REASON_DATA = "tbl_pending_reason_data";

    /*------------------------------------------------KET IDS--------------------------------------------------------*/


    public static final String KEY_ID = "key_id";
    public static final String KEY_NAME = "key_name";
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


    public static final String KEY_ATTENDANCE_DATE = "attendance_in_date";
    public static final String KEY_ATTENDANCE_TIME = "attendance_in_time";

    public static final String KEY_ATTENDANCE_STATUS = "attendance_out_time";
    public static final String KEY_ATTENDANCE_IN_IMG = "attendance_in_img";


    public static final String KEY_COMPLAINT_STATUS_ID = "complaint_status_id";
    public static final String KEY_COMPLAINT_STATUS = "complaint_status";

    public static final String KEY_STATUS_SELECTED = "status_selected";

    /*------------------------------------- Complaint Data Columns*/
    public static final String KEY_COMPLAINT_NUMBER = "complaint_number";
    public static final String KEY_CUST_ADDRESS = "customer_address";
    public static final String KEY_CUST_MOB_NO = "customer_mobile_no";
    public static final String KEY_CUST_NAME = "customer_name";
    public static final String KEY_PERNR = "pernr";
    public static final String KEY_ENAME = "ename";
    public static final String KEY_STATUS = "status";
    public static final String KEY_MATERIAL_CODE = "material_code";
    public static final String KEY_MATERIAL_NAME = "material_name";
    public static final String KEY_BILL_NO = "bill_no";
    public static final String KEY_BILL_DATE = "bill_date";
    public static final String KEY_FRWD_TO = "forward_to";
    public static final String KEY_COMPLAINT_DATE = "complaint_date";
    public static final String KEY_CMP_ACTION = "cmp_action";
    public static final String KEY_CMP_PENDING_RE = "complaint_pending_reason";
    public static final String KEY_CMP_LAT = "complaint_latitude";
    public static final String KEY_CMP_LNG = "complaint_longitude";
    public static final String KEY_CURRENT_STATUS = "current_status";






    /*-----------------------------------------------------Create Complaint Status Tables---------------------------------------------*/
    private static final String CREATE_TABLE_COMPLAINT_STATUS_DATA = "CREATE TABLE "
            + TABLE_COMPLAINT_STATUS_DATA + "(" + KEY_COMPLAINT_STATUS_ID + " TEXT,"
            + KEY_STATUS_SELECTED + " TEXT,"
            + KEY_COMPLAINT_STATUS + " TEXT)";


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

    private static final String CREATE_TABLE_SITE_SURVEY_IMAGES = "CREATE TABLE "
            + TABLE_SITE_SURVEY_IMAGE_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_IMAGE_NAME + " TEXT,"
            + KEY_IMAGE_PATH + " TEXT,"
            + KEY_IMAGE_SELECTED + " BOOLEAN,"
            + KEY_IMAGE_BILL_NO + " TEXT,"
            + KEY_IMAGE_LATITUDE + " TEXT,"
            + KEY_IMAGE_LONGITUDE + " TEXT,"
            + KEY_IMAGE_POSITION + " TEXT)";
    private static final String CREATE_TABLE_CHECK_OUT_IMAGES = "CREATE TABLE "
            + TABLE_CHECK_OUT_IMAGE_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_IMAGE_NAME + " TEXT,"
            + KEY_IMAGE_PATH + " TEXT,"
            + KEY_IMAGE_SELECTED + " BOOLEAN,"
            + KEY_IMAGE_BILL_NO + " TEXT,"
            + KEY_IMAGE_LATITUDE + " TEXT,"
            + KEY_IMAGE_LONGITUDE + " TEXT,"
            + KEY_IMAGE_POSITION + " TEXT)";


    /*-----------------------------------------------------Create Spinners Tables---------------------------------------------*/
    private static final String CREATE_TABLE_PENDING_REASON_DATA = "CREATE TABLE "
            + TABLE_PENDING_REASON_DATA + "(" + KEY_ID + " TEXT,"
            + KEY_NAME + " TEXT)";

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

    /*-----------------------------------------------------Create Mark Attendance Tables---------------------------------------------*/
    private static final String CREATE_TABLE_MARK_ATTENDANCE_DATA = "CREATE TABLE "
            + TABLE_MARK_ATTENDANCE_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_ATTENDANCE_DATE + " TEXT,"
            + KEY_ATTENDANCE_TIME + " TEXT,"
            + KEY_ATTENDANCE_STATUS + " TEXT,"
            + KEY_ATTENDANCE_IN_IMG + " TEXT)";

    /*-----------------------------------------------------Create complaint data Table---------------------------------------------*/

    private static final String CREATE_TABLE_COMPLAINT_DATA = "CREATE TABLE "
            + TABLE_COMPLAINT_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_COMPLAINT_NUMBER + " TEXT,"
            + KEY_CUST_ADDRESS + " TEXT,"
            + KEY_CUST_MOB_NO + " TEXT,"
            + KEY_CUST_NAME + " TEXT,"
            + KEY_PERNR + " TEXT,"
            + KEY_ENAME + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_MATERIAL_CODE + " TEXT,"
            + KEY_MATERIAL_NAME + " TEXT,"
            + KEY_BILL_NO + " TEXT,"
            + KEY_BILL_DATE + " TEXT,"
            + KEY_FRWD_TO + " TEXT,"
            + KEY_COMPLAINT_DATE + " TEXT,"
            + KEY_CMP_ACTION + " TEXT,"
            + KEY_CMP_PENDING_RE + " TEXT,"
            + KEY_CMP_LAT + " TEXT,"
            + KEY_CMP_LNG + " TEXT,"
            + KEY_CURRENT_STATUS + " TEXT)";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COMPLAINT_IMAGES);
        db.execSQL(CREATE_TABLE_LOCAL_CONVEYANCE_DATA);
        db.execSQL(CREATE_TABLE_MARK_ATTENDANCE_DATA);
        db.execSQL(CREATE_TABLE_COMPLAINT_STATUS_DATA);
        db.execSQL(CREATE_TABLE_COMPLAINT_DATA);
        db.execSQL(CREATE_TABLE_SITE_SURVEY_IMAGES);
        db.execSQL(CREATE_TABLE_CHECK_OUT_IMAGES);
        db.execSQL(CREATE_TABLE_PENDING_REASON_DATA);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_IMAGE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAL_CONVEYANCE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARK_ATTENDANCE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_STATUS_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITE_SURVEY_IMAGE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECK_OUT_IMAGE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING_REASON_DATA);
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


    /*---------------------------------------------Mark Attendance Data--------------------------------------------------*/
    public void insertMarkAttendanceData(MarkAttendanceModel markAttendanceModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ATTENDANCE_DATE, markAttendanceModel.getAttendanceDate());
        contentValues.put(KEY_ATTENDANCE_TIME, markAttendanceModel.getAttendanceTime());
        contentValues.put(KEY_ATTENDANCE_STATUS, markAttendanceModel.getAttendanceStatus());
        contentValues.put(KEY_ATTENDANCE_IN_IMG, markAttendanceModel.getAttendanceImg());

        database.insert(TABLE_MARK_ATTENDANCE_DATA, null, contentValues);
        database.close();
    }

    public ArrayList<MarkAttendanceModel> getAllMarkAttendanceData(boolean isRetrieveLastData) {
        String selectQuery;
        ArrayList<MarkAttendanceModel> imageModelArrayList = new ArrayList<MarkAttendanceModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (doesTableExist(database, TABLE_MARK_ATTENDANCE_DATA)) {

            if(isRetrieveLastData) {
                selectQuery = "SELECT * FROM " + TABLE_MARK_ATTENDANCE_DATA;
            }else {
                selectQuery = "SELECT * FROM " + TABLE_MARK_ATTENDANCE_DATA + " WHERE " + KEY_ATTENDANCE_DATE + " != '" + "" + "'" + " AND " + KEY_ATTENDANCE_TIME + " != '" + "" + "'";
            }
            Cursor mcursor = database.rawQuery(selectQuery, null);

            imageModelArrayList.clear();
            MarkAttendanceModel markAttendanceModel;
            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();
                    markAttendanceModel = new MarkAttendanceModel();
                    markAttendanceModel.setUniqId(mcursor.getString(0));
                    markAttendanceModel.setAttendanceDate(mcursor.getString(1));
                    markAttendanceModel.setAttendanceTime(mcursor.getString(2));
                    markAttendanceModel.setAttendanceStatus(mcursor.getString(3));
                    markAttendanceModel.setAttendanceImg(mcursor.getString(4));

                    imageModelArrayList.add(markAttendanceModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return  imageModelArrayList;
    }

    /*---------------------------------------------Mark Attendance Data--------------------------------------------------*/
    public void insertComplaintStatusData(ComplaintStatusModel.Datum complaintStatusModel, boolean bool) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COMPLAINT_STATUS_ID, complaintStatusModel.getValpos());
        contentValues.put(KEY_COMPLAINT_STATUS, complaintStatusModel.getDomvalueL());
        contentValues.put(KEY_STATUS_SELECTED, String.valueOf(bool));
        database.insert(TABLE_COMPLAINT_STATUS_DATA, null, contentValues);
        database.close();
    }

    @SuppressLint("Range")
    public ArrayList<ComplaintStatusModel.Datum> getAllComplaintStatusData() {
        String selectQuery;
        ArrayList<ComplaintStatusModel.Datum> complaintStatusList = new ArrayList<ComplaintStatusModel.Datum>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (doesTableExist(database, TABLE_COMPLAINT_STATUS_DATA)) {

                selectQuery = "SELECT * FROM " + TABLE_COMPLAINT_STATUS_DATA;

            Cursor mcursor = database.rawQuery(selectQuery, null);

            complaintStatusList.clear();
            ComplaintStatusModel.Datum complaintStatusModel;
            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();
                    complaintStatusModel = new ComplaintStatusModel.Datum();
                    complaintStatusModel.setValpos(mcursor.getString(mcursor.getColumnIndex(KEY_COMPLAINT_STATUS_ID)));
                    complaintStatusModel.setSelected(Boolean.parseBoolean(mcursor.getString(mcursor.getColumnIndex(KEY_STATUS_SELECTED))));
                    complaintStatusModel.setDomvalueL(mcursor.getString(mcursor.getColumnIndex(KEY_COMPLAINT_STATUS)));

                    complaintStatusList.add(complaintStatusModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return  complaintStatusList;
    }




    /*---------------------------------------------Complaint Data--------------------------------------------------*/


    public void insertComplaintDetailsData(ComplaintListModel.Datum complaintListModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COMPLAINT_NUMBER, complaintListModel.getCmpno());
        contentValues.put(KEY_CUST_ADDRESS, complaintListModel.getCaddress());
        contentValues.put(KEY_CUST_MOB_NO, complaintListModel.getMblno());
        contentValues.put(KEY_CUST_NAME, complaintListModel.getCstname());
        contentValues.put(KEY_PERNR, complaintListModel.getPernr());
        contentValues.put(KEY_ENAME, complaintListModel.getEname());
        contentValues.put(KEY_STATUS, complaintListModel.getStatus());
        contentValues.put(KEY_MATERIAL_CODE, complaintListModel.getMatnr());
        contentValues.put(KEY_MATERIAL_NAME, complaintListModel.getMaktx());
        contentValues.put(KEY_BILL_NO, complaintListModel.getVbeln());
        contentValues.put(KEY_BILL_DATE, complaintListModel.getFkdat());
        contentValues.put(KEY_FRWD_TO, complaintListModel.getFwrdTo());
        contentValues.put(KEY_COMPLAINT_DATE, complaintListModel.getFdate());
        contentValues.put(KEY_CMP_ACTION, complaintListModel.getAction());
        contentValues.put(KEY_CMP_PENDING_RE, complaintListModel.getCmpPenRe());
        contentValues.put(KEY_CMP_LAT, complaintListModel.getLat());
        contentValues.put(KEY_CMP_LNG, complaintListModel.getLng());
        contentValues.put(KEY_CURRENT_STATUS, complaintListModel.getCurrentStatus());


        database.insert(TABLE_COMPLAINT_DATA, null, contentValues);
        database.close();
    }

    @SuppressLint("Range")
    public ArrayList<ComplaintListModel.Datum> getAllComplaintDetailData(String status) {
        String selectQuery;
        ArrayList<ComplaintListModel.Datum> complaintModelList = new ArrayList<ComplaintListModel.Datum>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (doesTableExist(database, TABLE_COMPLAINT_DATA)) {

            if(status.isEmpty()){
                selectQuery = "SELECT * FROM " + TABLE_COMPLAINT_DATA;
            }else{
                selectQuery = "SELECT * FROM " + TABLE_COMPLAINT_DATA +  " WHERE " + KEY_STATUS + " == '" + status + "'";
            }


            Cursor mcursor = database.rawQuery(selectQuery, null);

            complaintModelList.clear();
            ComplaintListModel.Datum complaintModel;
            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();
                    complaintModel = new ComplaintListModel.Datum();
                    complaintModel.setCmpno(mcursor.getString(mcursor.getColumnIndex(KEY_COMPLAINT_NUMBER)));
                    complaintModel.setCaddress(mcursor.getString(mcursor.getColumnIndex(KEY_CUST_ADDRESS)));
                    complaintModel.setMblno(mcursor.getString(mcursor.getColumnIndex(KEY_CUST_MOB_NO)));
                    complaintModel.setCstname(mcursor.getString(mcursor.getColumnIndex(KEY_CUST_NAME)));
                    complaintModel.setPernr(mcursor.getString(mcursor.getColumnIndex(KEY_PERNR)));
                    complaintModel.setEname(mcursor.getString(mcursor.getColumnIndex(KEY_ENAME)));
                    complaintModel.setStatus(mcursor.getString(mcursor.getColumnIndex(KEY_STATUS)));
                    complaintModel.setMatnr(mcursor.getString(mcursor.getColumnIndex(KEY_MATERIAL_CODE)));
                    complaintModel.setMaktx(mcursor.getString(mcursor.getColumnIndex(KEY_MATERIAL_NAME)));
                    complaintModel.setVbeln(mcursor.getString(mcursor.getColumnIndex(KEY_BILL_NO)));
                    complaintModel.setFkdat(mcursor.getString(mcursor.getColumnIndex(KEY_BILL_DATE)));
                    complaintModel.setFwrdTo(mcursor.getString(mcursor.getColumnIndex(KEY_FRWD_TO)));
                    complaintModel.setFdate(mcursor.getString(mcursor.getColumnIndex(KEY_COMPLAINT_DATE)));
                    complaintModel.setAction(mcursor.getString(mcursor.getColumnIndex(KEY_CMP_ACTION)));
                    complaintModel.setCmpPenRe(mcursor.getString(mcursor.getColumnIndex(KEY_CMP_PENDING_RE)));
                    complaintModel.setLat(mcursor.getString(mcursor.getColumnIndex(KEY_CMP_LAT)));
                    complaintModel.setLng(mcursor.getString(mcursor.getColumnIndex(KEY_CMP_LNG)));
                    complaintModel.setCurrentStatus(mcursor.getString(mcursor.getColumnIndex(KEY_CURRENT_STATUS)));


                    complaintModelList.add(complaintModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return  complaintModelList;
    }


    /*------------------------------------------------Spinners Database----------------------------------------------*/

    public void insertSpinnerData(SpinnerDataModel spinnerDataModel, String TableName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, spinnerDataModel.getId());
        contentValues.put(KEY_NAME, spinnerDataModel.getName());

        database.insert(TableName, null, contentValues);
        database.close();
    }

    public void updateSpinnerData(SpinnerDataModel spinnerDataModel,String TableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, spinnerDataModel.getId());
        contentValues.put(KEY_NAME, spinnerDataModel.getName());

        // update Row
        String where = KEY_ID + "='" + spinnerDataModel.getId() + "'";
        db.update(TableName, contentValues, where, null);
        db.close();
    }
    public ArrayList<SpinnerDataModel> getSpinnerData(String TableName) {
        ArrayList<SpinnerDataModel> spinnerArrayList = new ArrayList<SpinnerDataModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (doesTableExist(database, TableName)) {
            String selectQuery = "SELECT  *  FROM " + TableName ;
            Cursor mcursor = database.rawQuery(selectQuery, null);

            spinnerArrayList.clear();
            SpinnerDataModel spinnerDataModel;
            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();
                    spinnerDataModel = new SpinnerDataModel();
                    spinnerDataModel.setId(mcursor.getString(0));
                    spinnerDataModel.setName(mcursor.getString(1));

                    spinnerArrayList.add(spinnerDataModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return  spinnerArrayList;
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

    public boolean isDataAvailabe(String tablename ) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + tablename ;
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
