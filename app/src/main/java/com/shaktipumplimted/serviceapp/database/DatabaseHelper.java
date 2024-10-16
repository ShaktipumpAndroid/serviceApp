package com.shaktipumplimted.serviceapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shaktipumplimted.serviceapp.Utils.common.model.ImageModel;
import com.shaktipumplimted.serviceapp.Utils.common.model.SpinnerDataModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintForward.model.CompForwardListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintStatusModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry.model.DsrDetailsModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.localconveyance.model.LocalConveyanceModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.AllAttendanceRecordModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.MarkAttendanceModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Shakti Service App";
    public static final int DATABASE_VERSION = 36;

    /*-------------------------------------------TABLE NAME---------------------------------------------------*/

    public static final String TABLE_COMPLAINT_STATUS_DATA = "tbl_complaint_status_data";
    public static final String TABLE_COMPLAINT_IMAGE_DATA = "tbl_complaint_image_data";
    public static final String TABLE_PENDING_REASON_IMAGE_DATA = "tbl_pending_reason_image_data";
    public static final String TABLE_SITE_SURVEY_IMAGE_DATA = "tbl_site_survey_image_data";
    public static final String TABLE_CHECK_OUT_IMAGE_DATA = "tbl_checkout_image_data";
    public static final String TABLE_LOCAL_CONVEYANCE_DATA = "tbl_local_conveyance_data";

    public static final String TABLE_MARK_ATTENDANCE_DATA = "tbl_mark_attendance_data";

    public static final String TABLE_ATTENDANCE_HISTORY_DATA = "tbl_attendance_history_data";
    public static final String TABLE_COMPLAINT_DATA = "tbl_complaint_data";
    public static final String TABLE_PENDING_REASON_DATA = "tbl_pending_reason_data";
    public static final String TABLE_COMPLAINT_CATEGORY = "tbl_complain_category";
    public static final String TABLE_COMPLAINT_DEFECT = "tbl_complain_defect";
    public static final String TABLE_COMPLAINT_RELATED = "tbl_complain_related_to";
    public static final String TABLE_COMPLAINT_CLOSURE = "tbl_complain_closer";
    public static final String TABLE_DSR_DROPWODN = "tbl_dsr_dropdown";
    public static final String TABLE_DSR_RECORD = "tbl_dsr_record";

    public static final String TABLE_COMPLAINT_FORWARD_PERSON_DATA = "tbl_complaint_forward_data";
    public static final String TABLE_CHECK_OUT_DROPDOWN = "tbl_check_out_dropdown";

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
    public static final String KEY_TRAVEL_MODE = "travel_mode";


    public static final String KEY_ATTENDANCE_DATE = "attendance_in_date";
    public static final String KEY_ATTENDANCE_TIME = "attendance_in_time";

    public static final String KEY_ATTENDANCE_STATUS = "attendance_out_time";
    public static final String KEY_ATTENDANCE_IN_IMG = "attendance_in_img";
    public static final String KEY_ATTENDANCE_IN_TIME = "attendance_in_time";
    public static final String KEY_ATTENDANCE_OUT_TIME = "attendance_out_time";
    public static final String KEY_ATTENDANCE_LAT = "attendance_latitude";
    public static final String KEY_ATTENDANCE_LNG = "attendance_longitude";

    public static final String KEY_COMPLAINT_STATUS_ID = "complaint_status_id";
    public static final String KEY_COMPLAINT_STATUS = "complaint_status";

    public static final String KEY_STATUS_SELECTED = "status_selected";
    public static final String KEY_DSR_ACTIVITY = "dsr_activity";
    public static final String KEY_DSR_PURPOSE = "dsr_purpose";
    public static final String KEY_DSR_OUTCOME = "dsr_outcome";
    public static final String KEY_DSR_DATE = "dsr_date";
    public static final String KEY_DSR_LAT = "dsr_latitude";
    public static final String KEY_DSR_LNG = "dsr_longitude";
    public static final String KEY_DSR_TIME = "dsr_time";

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

    public static final String KEY_CURRENT_LAT = "current_lat";

    public static final String KEY_CURRENT_LNG = "current_lng";

    public static final String KEY_CUST_PAY = "customer_pay";

    public static final String KEY_COMP_PAY = "company_pay";

    public static final String KEY_FOC_AMOUNT = "for_amount";

    public static final String KEY_RETURN_BY_COMP = "reture_by_company";

    public static final String KEY_PAY_TO_FREELANCER = "pay_to_freelancer";

    public static final String KEY_PUMP_SR_NO = "pump_sr_no";

    public static final String KEY_MOTOR_SR_NO = "motor_sr_no";

    public static final String KEY_CONTROLLER_SR_NO = "controller_sr_no";

    public static final String KEY_CMP_CATEGORY = "cmp_category";

    public static final String KEY_CMP_CLOSURE_REASON = "closure_reason";

    public static final String KEY_CMP_DEFECT_TYPE = "defect_type";

    public static final String KEY_CMP_RELATED_TO = "related_to";

    public static final String KEY_CMP_REMARK = "complaint_remark";
    public static final String KEY_CURRENT_DATE = "current_date";
    public static final String KEY_CURRENT_TIME = "current_time";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_WARRANTY = "warranty";
    public static final String KEY_SERIAL_NO = "serial_no";
    public static final String KEY_BENEFICIARY_NO = "beneficiary_no";
    public static final String KEY_DATA_SAVED_LOCALLY = "data_saved_locally";


    /*----------------------------------------Complaint forward key code------------------------------------------------------*/

    public static final String KEY_PERSON_CODE = "person_code";
    public static final String KEY_PERSON_NAME = "person_name";



    /*-----------------------------------------------------Create Forward Persons Tables---------------------------------------------*/
    private static final String CREATE_TABLE_COMPLAINT_FORWARD_PERSON_DATA = "CREATE TABLE "
            + TABLE_COMPLAINT_FORWARD_PERSON_DATA + "(" + KEY_PERSON_CODE + " TEXT,"
            + KEY_PERSON_NAME + " TEXT,"
            + KEY_STATUS_SELECTED + " TEXT)";




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

    private static final String CREATE_TABLE_PENDING_REASON_IMAGES = "CREATE TABLE "
            + TABLE_PENDING_REASON_IMAGE_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
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




    private static final String CREATE_TABLE_COMPLAINT_CATEGORY = "CREATE TABLE "
            + TABLE_COMPLAINT_CATEGORY + "(" + KEY_ID + " TEXT,"
            + KEY_NAME + " TEXT)";


    private static final String CREATE_TABLE_COMPLAINT_DEFECT = "CREATE TABLE "
            + TABLE_COMPLAINT_DEFECT + "(" + KEY_ID + " TEXT,"
            + KEY_NAME + " TEXT)";

    private static final String CREATE_TABLE_COMPLAINT_RELATED = "CREATE TABLE "
            + TABLE_COMPLAINT_RELATED + "(" + KEY_ID + " TEXT,"
            + KEY_NAME + " TEXT)";

    private static final String CREATE_TABLE_COMPLAINT_CLOSURE = "CREATE TABLE "
            + TABLE_COMPLAINT_CLOSURE + "(" + KEY_ID + " TEXT,"
            + KEY_NAME + " TEXT)";


    private static final String CREATE_TABLE_DSR_DROPWODN = "CREATE TABLE "
            + TABLE_DSR_DROPWODN + "(" + KEY_ID + " TEXT,"
            + KEY_NAME + " TEXT)";

    private static final String CREATE_TABLE_CHECK_OUT_DROPDOWN = "CREATE TABLE "
            + TABLE_CHECK_OUT_DROPDOWN + "(" + KEY_ID + " TEXT,"
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
            + KEY_END_TRAVEL_IMG + " TEXT,"
            + KEY_TRAVEL_MODE + " TEXT)";

    /*-----------------------------------------------------Create Mark Attendance Tables---------------------------------------------*/
    private static final String CREATE_TABLE_MARK_ATTENDANCE_DATA = "CREATE TABLE "
            + TABLE_MARK_ATTENDANCE_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_ATTENDANCE_DATE + " TEXT,"
            + KEY_ATTENDANCE_TIME + " TEXT,"
            + KEY_ATTENDANCE_STATUS + " TEXT,"
            + KEY_ATTENDANCE_IN_IMG + " TEXT,"
            + KEY_ATTENDANCE_LAT + " TEXT,"
            + KEY_ATTENDANCE_LNG + " TEXT,"
            + KEY_DATA_SAVED_LOCALLY + " TEXT)";

    private static final String CREATE_TABLE_ATTENDANCE_HISTORY_DATA = "CREATE TABLE "
            + TABLE_ATTENDANCE_HISTORY_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_ATTENDANCE_DATE + " TEXT,"
            + KEY_ATTENDANCE_IN_TIME + " TEXT,"
            + KEY_ATTENDANCE_OUT_TIME + " TEXT)";

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
            + KEY_CURRENT_STATUS + " TEXT,"
            + KEY_CURRENT_LAT + " TEXT,"
            + KEY_CURRENT_LNG + " TEXT,"
            + KEY_CUST_PAY + " TEXT,"
            + KEY_COMP_PAY + " TEXT,"
            + KEY_FOC_AMOUNT + " TEXT,"
            + KEY_RETURN_BY_COMP + " TEXT,"
            + KEY_PAY_TO_FREELANCER + " TEXT,"
            + KEY_PUMP_SR_NO + " TEXT,"
            + KEY_MOTOR_SR_NO + " TEXT,"
            + KEY_CONTROLLER_SR_NO + " TEXT,"
            + KEY_CMP_CATEGORY + " TEXT,"
            + KEY_CMP_CLOSURE_REASON + " TEXT,"
            + KEY_CMP_DEFECT_TYPE + " TEXT,"
            + KEY_CMP_RELATED_TO + " TEXT,"
            + KEY_CMP_REMARK + " TEXT,"
            + KEY_CURRENT_DATE + " TEXT,"
            + KEY_CURRENT_TIME + " TEXT,"
            + KEY_DISTANCE + " TEXT,"
            + KEY_WARRANTY + " TEXT,"
            + KEY_SERIAL_NO + " TEXT,"
            + KEY_BENEFICIARY_NO + " TEXT,"
            + KEY_DATA_SAVED_LOCALLY + " TEXT)";


    /*-----------------------------------------------------Create Dsr record data Table---------------------------------------------*/
    private static final String CREATE_TABLE_DSR_RECORD = "CREATE TABLE "
            + TABLE_DSR_RECORD + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_DSR_ACTIVITY + " TEXT,"
            + KEY_DSR_DATE + " TEXT,"
            + KEY_DSR_LAT + " TEXT,"
            + KEY_DSR_LNG + " TEXT,"
            + KEY_DSR_TIME + " TEXT,"
            + KEY_DSR_PURPOSE + " TEXT,"
            + KEY_DSR_OUTCOME + " TEXT,"
            + KEY_DATA_SAVED_LOCALLY + " TEXT)";




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
        db.execSQL(CREATE_TABLE_COMPLAINT_CATEGORY);
        db.execSQL(CREATE_TABLE_COMPLAINT_DEFECT);
        db.execSQL(CREATE_TABLE_COMPLAINT_RELATED);
        db.execSQL(CREATE_TABLE_COMPLAINT_CLOSURE);
        db.execSQL(CREATE_TABLE_DSR_DROPWODN);
        db.execSQL(CREATE_TABLE_DSR_RECORD);
        db.execSQL(CREATE_TABLE_COMPLAINT_FORWARD_PERSON_DATA);
        db.execSQL(CREATE_TABLE_PENDING_REASON_IMAGES);
        db.execSQL(CREATE_TABLE_CHECK_OUT_DROPDOWN);
        db.execSQL(CREATE_TABLE_ATTENDANCE_HISTORY_DATA);

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_FORWARD_PERSON_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_DEFECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_RELATED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT_CLOSURE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING_REASON_IMAGE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DSR_DROPWODN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DSR_RECORD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECK_OUT_DROPDOWN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_HISTORY_DATA);
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
        contentValues.put(KEY_TRAVEL_MODE, localConveyanceModel.getTravelMode());

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
        contentValues.put(KEY_TRAVEL_MODE, localConveyanceModel.getTravelMode());
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
                    localConveyanceModel.setTravelMode(mcursor.getString(13));

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
        contentValues.put(KEY_ATTENDANCE_LAT, markAttendanceModel.getLatitude());
        contentValues.put(KEY_ATTENDANCE_LNG, markAttendanceModel.getLongitude());
        contentValues.put(KEY_DATA_SAVED_LOCALLY, String.valueOf(markAttendanceModel.isDataSavedLocally()));

        database.insert(TABLE_MARK_ATTENDANCE_DATA, null, contentValues);
        database.close();
    }

    public void updateMarkAttendanceData(MarkAttendanceModel markAttendanceModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ATTENDANCE_DATE, markAttendanceModel.getAttendanceDate());
        contentValues.put(KEY_ATTENDANCE_TIME, markAttendanceModel.getAttendanceTime());
        contentValues.put(KEY_ATTENDANCE_STATUS, markAttendanceModel.getAttendanceStatus());
        contentValues.put(KEY_ATTENDANCE_IN_IMG, markAttendanceModel.getAttendanceImg());
        contentValues.put(KEY_ATTENDANCE_LAT, markAttendanceModel.getLatitude());
        contentValues.put(KEY_ATTENDANCE_LNG, markAttendanceModel.getLongitude());
        contentValues.put(KEY_DATA_SAVED_LOCALLY, String.valueOf(markAttendanceModel.isDataSavedLocally()));

        String where = KEY_ATTENDANCE_DATE + "='" + markAttendanceModel.getAttendanceDate() + "'";
        database.update(TABLE_MARK_ATTENDANCE_DATA, contentValues, where, null);

        database.close();
    }

    public ArrayList<MarkAttendanceModel> getAllMarkAttendanceData(boolean isAllData,String date,boolean isUnsyncData) {
        String selectQuery;
        ArrayList<MarkAttendanceModel> imageModelArrayList = new ArrayList<MarkAttendanceModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (doesTableExist(database, TABLE_MARK_ATTENDANCE_DATA)) {

            if(isAllData && isUnsyncData) {
                selectQuery = "SELECT * FROM " + TABLE_MARK_ATTENDANCE_DATA + " WHERE " + KEY_DATA_SAVED_LOCALLY + " == '" + String.valueOf(isUnsyncData) + "'";
            }else {
                selectQuery = "SELECT * FROM " + TABLE_MARK_ATTENDANCE_DATA + " WHERE " + KEY_ATTENDANCE_DATE + " == '" + date + "'";
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
                    markAttendanceModel.setLatitude(mcursor.getString(5));
                    markAttendanceModel.setLongitude(mcursor.getString(6));
                    markAttendanceModel.setDataSavedLocally(Boolean.parseBoolean(mcursor.getString(7)));

                    imageModelArrayList.add(markAttendanceModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return  imageModelArrayList;
    }

    public void insertAttendanceHistoryData(AllAttendanceRecordModel allAttendanceRecordModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ATTENDANCE_DATE, allAttendanceRecordModel.getAttendanceDate());
        contentValues.put(KEY_ATTENDANCE_IN_TIME, allAttendanceRecordModel.getAttendanceInTime());
        contentValues.put(KEY_ATTENDANCE_OUT_TIME, allAttendanceRecordModel.getAttendanceOutTime());
        database.insert(TABLE_ATTENDANCE_HISTORY_DATA, null, contentValues);
        database.close();
    }

    public void updateAttendanceHistoryData(AllAttendanceRecordModel allAttendanceRecordModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ATTENDANCE_DATE, allAttendanceRecordModel.getAttendanceDate());
        contentValues.put(KEY_ATTENDANCE_IN_TIME, allAttendanceRecordModel.getAttendanceInTime());
        contentValues.put(KEY_ATTENDANCE_OUT_TIME, allAttendanceRecordModel.getAttendanceOutTime());
        // update Row
        String where = KEY_ATTENDANCE_DATE + "='" + allAttendanceRecordModel.getAttendanceDate() + "'";
        db.update(TABLE_ATTENDANCE_HISTORY_DATA, contentValues, where, null);
        db.close();
    }

    public ArrayList<AllAttendanceRecordModel> getAllAttendanceHistoryData() {
        String selectQuery;
        ArrayList<AllAttendanceRecordModel> attendanceRecordArrayList = new ArrayList<AllAttendanceRecordModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (doesTableExist(database, TABLE_ATTENDANCE_HISTORY_DATA)) {

                selectQuery = "SELECT * FROM " + TABLE_ATTENDANCE_HISTORY_DATA;
            Cursor mcursor = database.rawQuery(selectQuery, null);

            attendanceRecordArrayList.clear();
            AllAttendanceRecordModel attendanceRecordModel;
            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();
                    attendanceRecordModel = new AllAttendanceRecordModel();
                    attendanceRecordModel.setUniqId(mcursor.getString(0));
                    attendanceRecordModel.setAttendanceDate(mcursor.getString(1));
                    attendanceRecordModel.setAttendanceInTime(mcursor.getString(2));
                    attendanceRecordModel.setAttendanceOutTime(mcursor.getString(3));

                    attendanceRecordArrayList.add(attendanceRecordModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return  attendanceRecordArrayList;
    }



    /*---------------------------------------------Complaint Status Data--------------------------------------------------*/
    public void insertComplaintStatusData(ComplaintStatusModel.Datum complaintStatusModel, boolean bool) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_COMPLAINT_STATUS_ID, complaintStatusModel.getValpos());
        contentValues.put(KEY_COMPLAINT_STATUS, complaintStatusModel.getDomvalueL());
        contentValues.put(KEY_STATUS_SELECTED, String.valueOf(bool));
        database.insert(TABLE_COMPLAINT_STATUS_DATA, null, contentValues);
        database.close();
    }

    /*@SuppressLint("Range")
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
*/



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
        contentValues.put(KEY_CURRENT_LAT, complaintListModel.getCurrentLat());
        contentValues.put(KEY_CURRENT_LNG, complaintListModel.getCurrentLng());
        contentValues.put(KEY_CUST_PAY, complaintListModel.getCustomerPay());
        contentValues.put(KEY_COMP_PAY, complaintListModel.getCompanyPay());
        contentValues.put(KEY_FOC_AMOUNT, complaintListModel.getFocAmount());
        contentValues.put(KEY_RETURN_BY_COMP, complaintListModel.getReturnByCompany());
        contentValues.put(KEY_PAY_TO_FREELANCER, complaintListModel.getPayToFreelancer());
        contentValues.put(KEY_PUMP_SR_NO, complaintListModel.getPumpSrNo());
        contentValues.put(KEY_MOTOR_SR_NO, complaintListModel.getMotorSrNo());
        contentValues.put(KEY_CONTROLLER_SR_NO, complaintListModel.getControllerSrNo());
        contentValues.put(KEY_CMP_CATEGORY, complaintListModel.getCategory());
        contentValues.put(KEY_CMP_CLOSURE_REASON, complaintListModel.getClosureReason());
        contentValues.put(KEY_CMP_DEFECT_TYPE, complaintListModel.getDefectType());
        contentValues.put(KEY_CMP_RELATED_TO, complaintListModel.getRelatedTo());
        contentValues.put(KEY_CMP_REMARK, complaintListModel.getRemark());
        contentValues.put(KEY_CURRENT_DATE, complaintListModel.getCurrentDate());
        contentValues.put(KEY_CURRENT_TIME, complaintListModel.getCurrentTime());
        contentValues.put(KEY_DISTANCE, complaintListModel.getDistance());
        contentValues.put(KEY_WARRANTY, complaintListModel.getWarrantyTxt());
        contentValues.put(KEY_SERIAL_NO, complaintListModel.getSernr());
        contentValues.put(KEY_BENEFICIARY_NO,complaintListModel.getBeneficiary());
        contentValues.put(KEY_DATA_SAVED_LOCALLY, String.valueOf(complaintListModel.isDataSavedLocally()));


        database.insert(TABLE_COMPLAINT_DATA, null, contentValues);
        database.close();
    }


    public void updateComplaintDetailsData(ComplaintListModel.Datum complaintListModel) {
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
        contentValues.put(KEY_CURRENT_LAT, complaintListModel.getCurrentLat());
        contentValues.put(KEY_CURRENT_LNG, complaintListModel.getCurrentLng());
        contentValues.put(KEY_CUST_PAY, complaintListModel.getCustomerPay());
        contentValues.put(KEY_COMP_PAY, complaintListModel.getCompanyPay());
        contentValues.put(KEY_FOC_AMOUNT, complaintListModel.getFocAmount());
        contentValues.put(KEY_RETURN_BY_COMP, complaintListModel.getReturnByCompany());
        contentValues.put(KEY_PAY_TO_FREELANCER, complaintListModel.getPayToFreelancer());
        contentValues.put(KEY_PUMP_SR_NO, complaintListModel.getPumpSrNo());
        contentValues.put(KEY_MOTOR_SR_NO, complaintListModel.getMotorSrNo());
        contentValues.put(KEY_CONTROLLER_SR_NO, complaintListModel.getControllerSrNo());
        contentValues.put(KEY_CMP_CATEGORY, complaintListModel.getCategory());
        contentValues.put(KEY_CMP_CLOSURE_REASON, complaintListModel.getClosureReason());
        contentValues.put(KEY_CMP_DEFECT_TYPE, complaintListModel.getDefectType());
        contentValues.put(KEY_CMP_RELATED_TO, complaintListModel.getRelatedTo());
        contentValues.put(KEY_CMP_REMARK, complaintListModel.getRemark());
        contentValues.put(KEY_CURRENT_DATE, complaintListModel.getCurrentDate());
        contentValues.put(KEY_CURRENT_TIME, complaintListModel.getCurrentTime());
        contentValues.put(KEY_DISTANCE, complaintListModel.getDistance());
        contentValues.put(KEY_WARRANTY, complaintListModel.getWarrantyTxt());
        contentValues.put(KEY_SERIAL_NO, complaintListModel.getSernr());
        contentValues.put(KEY_BENEFICIARY_NO,complaintListModel.getBeneficiary());
        contentValues.put(KEY_DATA_SAVED_LOCALLY, String.valueOf(complaintListModel.isDataSavedLocally()));

        String where = KEY_COMPLAINT_NUMBER + "='" + complaintListModel.getCmpno() + "'";
        database.update(TABLE_COMPLAINT_DATA, contentValues, where, null);
        database.close();
    }

    @SuppressLint("Range")
    public ArrayList<ComplaintListModel.Datum> getAllComplaintDetailData(String status,String isSavedLocally) {
        String selectQuery;
        ArrayList<ComplaintListModel.Datum> complaintModelList = new ArrayList<ComplaintListModel.Datum>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (doesTableExist(database, TABLE_COMPLAINT_DATA)) {

            if(!status.isEmpty()){
                selectQuery = "SELECT * FROM " + TABLE_COMPLAINT_DATA+  " WHERE " +  KEY_STATUS + " == '" + status  + "'" + " AND " + KEY_DATA_SAVED_LOCALLY + " == '" + isSavedLocally + "'";
            }else {
                selectQuery = "SELECT * FROM " + TABLE_COMPLAINT_DATA +  " WHERE " + KEY_DATA_SAVED_LOCALLY + " == '" + isSavedLocally + "'";

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
                    complaintModel.setCurrentLat(mcursor.getString(mcursor.getColumnIndex(KEY_CURRENT_LAT)));
                    complaintModel.setCurrentLng(mcursor.getString(mcursor.getColumnIndex(KEY_CURRENT_LNG)));
                    complaintModel.setCustomerPay(mcursor.getString(mcursor.getColumnIndex(KEY_CUST_PAY)));
                    complaintModel.setCompanyPay(mcursor.getString(mcursor.getColumnIndex(KEY_COMP_PAY)));
                    complaintModel.setFocAmount(mcursor.getString(mcursor.getColumnIndex(KEY_FOC_AMOUNT)));
                    complaintModel.setReturnByCompany(mcursor.getString(mcursor.getColumnIndex(KEY_RETURN_BY_COMP)));
                    complaintModel.setPayToFreelancer(mcursor.getString(mcursor.getColumnIndex(KEY_PAY_TO_FREELANCER)));
                    complaintModel.setPumpSrNo(mcursor.getString(mcursor.getColumnIndex(KEY_PUMP_SR_NO)));
                    complaintModel.setMotorSrNo(mcursor.getString(mcursor.getColumnIndex(KEY_MOTOR_SR_NO)));
                    complaintModel.setControllerSrNo(mcursor.getString(mcursor.getColumnIndex(KEY_CONTROLLER_SR_NO)));
                    complaintModel.setCategory(mcursor.getString(mcursor.getColumnIndex(KEY_CMP_CATEGORY)));
                    complaintModel.setClosureReason(mcursor.getString(mcursor.getColumnIndex(KEY_CMP_CLOSURE_REASON)));
                    complaintModel.setDefectType(mcursor.getString(mcursor.getColumnIndex(KEY_CMP_DEFECT_TYPE)));
                    complaintModel.setRelatedTo(mcursor.getString(mcursor.getColumnIndex(KEY_CMP_RELATED_TO)));
                    complaintModel.setRemark(mcursor.getString(mcursor.getColumnIndex(KEY_CMP_REMARK)));
                    complaintModel.setCurrentDate(mcursor.getString(mcursor.getColumnIndex(KEY_CURRENT_DATE)));
                    complaintModel.setCurrentTime(mcursor.getString(mcursor.getColumnIndex(KEY_CURRENT_TIME)));
                    complaintModel.setDistance(mcursor.getString(mcursor.getColumnIndex(KEY_DISTANCE)));
                    complaintModel.setWarrantyTxt(mcursor.getString(mcursor.getColumnIndex(KEY_WARRANTY)));
                    complaintModel.setSernr(mcursor.getString(mcursor.getColumnIndex(KEY_SERIAL_NO)));
                    complaintModel.setBeneficiary(mcursor.getString(mcursor.getColumnIndex(KEY_BENEFICIARY_NO)));
                    complaintModel.setDataSavedLocally(Boolean.parseBoolean(mcursor.getString(mcursor.getColumnIndex(KEY_DATA_SAVED_LOCALLY))));
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


    /*------------------------------------------------Complaint Forward Person Database----------------------------------------------*/

    public void insertComplaintForwardPersonData(CompForwardListModel.Response compForwardListModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PERSON_CODE, compForwardListModel.getPartnerCode());
        contentValues.put(KEY_PERSON_NAME, compForwardListModel.getPartnerName());
        contentValues.put(KEY_STATUS_SELECTED, compForwardListModel.getIsSelected());

        database.insert(TABLE_COMPLAINT_FORWARD_PERSON_DATA, null, contentValues);
        database.close();
    }

    public ArrayList<CompForwardListModel.Response> getComplaintForwardPersonList(String Status) {
        ArrayList<CompForwardListModel.Response> spinnerArrayList = new ArrayList<CompForwardListModel.Response>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (doesTableExist(database, TABLE_COMPLAINT_FORWARD_PERSON_DATA)) {
            String selectQuery = "SELECT  *  FROM " + TABLE_COMPLAINT_FORWARD_PERSON_DATA +  " WHERE " + KEY_STATUS_SELECTED + " == '" + Status + "'";
            Cursor mcursor = database.rawQuery(selectQuery, null);

            spinnerArrayList.clear();
            CompForwardListModel.Response compForwardListModel;
            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();
                    compForwardListModel = new CompForwardListModel.Response();
                    compForwardListModel.setPartnerCode(mcursor.getString(0));
                    compForwardListModel.setPartnerName(mcursor.getString(1));
                    compForwardListModel.setIsSelected(mcursor.getString(2));

                    spinnerArrayList.add(compForwardListModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return  spinnerArrayList;
    }





    /*---------------------------------------------DSR entry Data--------------------------------------------------*/


    public void insertDsrData(DsrDetailsModel dsrDetailsModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DSR_ACTIVITY, dsrDetailsModel.getDsrActivity());
        contentValues.put(KEY_DSR_PURPOSE, dsrDetailsModel.getDsrPurpose());
        contentValues.put(KEY_DSR_OUTCOME, dsrDetailsModel.getDsrOutcome());
        contentValues.put(KEY_DSR_DATE, dsrDetailsModel.getDate());
        contentValues.put(KEY_DSR_TIME, dsrDetailsModel.getTime());
        contentValues.put(KEY_DSR_LAT, dsrDetailsModel.getLat());
        contentValues.put(KEY_DSR_LNG, dsrDetailsModel.getLng());
        contentValues.put(KEY_DATA_SAVED_LOCALLY, String.valueOf(dsrDetailsModel.isDataSavedLocally()));

        database.insert(TABLE_DSR_RECORD, null, contentValues);
        database.close();
    }

    public void updateDsrData(DsrDetailsModel dsrDetailsModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DSR_ACTIVITY, dsrDetailsModel.getDsrActivity());
        contentValues.put(KEY_DSR_PURPOSE, dsrDetailsModel.getDsrPurpose());
        contentValues.put(KEY_DSR_OUTCOME, dsrDetailsModel.getDsrOutcome());
        contentValues.put(KEY_DSR_DATE, dsrDetailsModel.getDate());
        contentValues.put(KEY_DSR_TIME, dsrDetailsModel.getTime());
        contentValues.put(KEY_DSR_LAT, dsrDetailsModel.getLat());
        contentValues.put(KEY_DSR_LNG, dsrDetailsModel.getLng());
        contentValues.put(KEY_DATA_SAVED_LOCALLY, String.valueOf(dsrDetailsModel.isDataSavedLocally()));

        String where = KEY_DSR_DATE + "='" + dsrDetailsModel.getDate() + "'";
        database.update(TABLE_DSR_RECORD, contentValues, where, null);

        database.close();
    }

    public ArrayList<DsrDetailsModel> getAllDsrEntry(boolean isSavedLocally) {
        ArrayList<DsrDetailsModel> spinnerArrayList = new ArrayList<DsrDetailsModel>();
        SQLiteDatabase database = this.getWritableDatabase();
        if (doesTableExist(database, TABLE_DSR_RECORD)) {

            String selectQuery = "SELECT  *  FROM " + TABLE_DSR_RECORD +" WHERE " + KEY_DATA_SAVED_LOCALLY + " == '" + isSavedLocally + "'";
            Cursor mcursor = database.rawQuery(selectQuery, null);

            spinnerArrayList.clear();
            DsrDetailsModel dsrDetailsModel;
            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();
                    dsrDetailsModel = new DsrDetailsModel();
                    dsrDetailsModel.setUniqId(mcursor.getString(0));
                    dsrDetailsModel.setDsrActivity(mcursor.getString(1));
                    dsrDetailsModel.setDate(mcursor.getString(2));
                    dsrDetailsModel.setLat(mcursor.getString(3));
                    dsrDetailsModel.setLng(mcursor.getString(4));
                    dsrDetailsModel.setTime(mcursor.getString(5));
                    dsrDetailsModel.setDsrPurpose(mcursor.getString(6));
                    dsrDetailsModel.setDsrOutcome(mcursor.getString(7));
                    dsrDetailsModel.setDataSavedLocally(Boolean.parseBoolean(mcursor.getString(8)));

                    spinnerArrayList.add(dsrDetailsModel);
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

    public boolean isDataAvailable(String tablename ) {
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
