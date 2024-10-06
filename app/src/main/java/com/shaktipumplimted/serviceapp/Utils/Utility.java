package com.shaktipumplimted.serviceapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shaktipumplimted.serviceapp.BuildConfig;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.common.model.SpinnerDataModel;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.login.LoginActivity;
import com.shaktipumplimted.serviceapp.otpReader.AppSignatureHashHelper.AppSignatureHashHelper;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Response;

public class Utility {

    private static final String PREFERENCE = "ServiceApp";
    public static CustomProgressDialog progressDialog;


    public static void ShowToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void showProgressDialogue(Activity context) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new CustomProgressDialog(context);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });


    }

    public static void hideProgressDialogue() {
        progressDialog.dismiss();

    }

    public static void setSharedPreference(Context context,String name,
                                           String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "");
    }

    public static void clearSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.clear();
        prefsEditor.apply();

    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static void buildAlertMessageNoGps(Context context) {

        new AlertDialog.Builder(context)
                .setTitle(R.string.Location_permission)  // GPS not found
                .setMessage(R.string.Location_permission_txt) // Want to enable?
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.cancel_text, null)
                .show();

    }

    public static void loadFragment(FragmentActivity activty, Fragment fragment, boolean backstack, String tagName) {
        // load fragment0.....

        FragmentTransaction transaction = activty.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (backstack) {
            transaction.addToBackStack(tagName);
        }
        transaction.commit();
    }

    public static Bitmap getBitmapFromBase64(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }


    public void removeFragments(FragmentActivity activty) {
        activty.getSupportFragmentManager().popBackStack("F", FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    public static void removeAllFragment(FragmentActivity activty, String tagName) {
        FragmentManager fm = activty.getSupportFragmentManager();

        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
           /* if (!fm.getBackStackEntryAt(i).getName().equalsIgnoreCase(tagName)) {
                fm.popBackStack();
            }*/
            fm.popBackStack();

        }
    }

    private static Bitmap rotateBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) 90);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public static File saveFile(Bitmap bitmap, String name, String folder) {

        String firstname = Utility.getUserFirstName(name);
        Log.e("fname=====>",firstname);
        File file = new File(getMediaFilePath(folder,firstname));
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getUserFirstName(String fullname){
        String firstName;
        String[] fullNameArray = fullname.split("\\s+");
        if(fullNameArray.length>1) {
            StringBuilder firstNameBuilder = new StringBuilder();
            for (int i = 0; i < fullNameArray.length - 1; i++) {
                firstNameBuilder.append(fullNameArray[i]);
                if(i != fullNameArray.length - 2){
                    firstNameBuilder.append(" ");
                }
            }
            firstName = firstNameBuilder.toString();
        } else {
            firstName = fullNameArray[0];
        }
        return firstName;
    }
    public static String getMediaFilePath(String folder, String name) {



        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Shakti Kusum App");

        File dir = new File(root.getAbsolutePath() + "/"+folder+"/" + name); //it is my root directory

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a media file name
        return dir.getPath() + File.separator + "IMG_"+ Calendar.getInstance().getTimeInMillis() +".jpg";
    }



    public static String differenceBetweenDays(String inputDate) {

        Date date1;
        Date date2;

        SimpleDateFormat curdt = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String CurrentDate = curdt.format(new Date());
        String FinalDate1 = ChangeDateFormat("dd.MM.yyyy", "MM/dd/yyyy", inputDate);
        String dayDifference = null;

        try {
            date1 = curdt.parse(CurrentDate);
            date2 = curdt.parse(FinalDate1);
            long difference = 0;
            if (date1 != null) {
                if (date2 != null) {
                    difference = Math.abs(date1.getTime() - date2.getTime());
                }
            }
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            dayDifference = Long.toString(differenceDates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayDifference;
    }

    public static String ChangeDateFormat(String inputFormat, String outputFormat, String inputDate) {
        Date parsed = null;
        String outputDate = "";
        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.ENGLISH);
        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (ParseException e) {

        }
        return outputDate;
    }

    public static String ChangeTimeFormat(String inputFormat, String outputFormat, String inputDate) {
        Date parsed = null;
        String outputDate = "";
        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.ENGLISH);
        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (ParseException e) {

        }
        return outputDate;
    }

    public static boolean isAlphaNumeric(String str)
    {
        // Regex to check string is alphanumeric or not.
        String regex = "^[0-9a-zA-Z]+$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }
    public static Bitmap saveImageWithTimeStamp( Context context,byte[] data,String value) {

        BitmapFactory.Options options = new BitmapFactory.Options();


        options.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        bmp = rotateBitmap(bmp);
        Canvas canvas = new Canvas(bmp);
        TextPaint mTextPaint = new TextPaint();
        int color = ContextCompat.getColor(context, R.color.black);
        mTextPaint.setColor(color);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        //  Log.e("displaytext===>", display.getText().toString());
        float scaledTextSize = 35 * context.getResources().getDisplayMetrics().scaledDensity;
        mTextPaint.setTextSize(scaledTextSize);
        StaticLayout mTextLayout = new StaticLayout(value.trim(), mTextPaint, canvas.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);

        canvas.save();
        canvas.translate(0f, bmp.getHeight() - mTextLayout.getHeight() - 0.0f);
        mTextLayout.draw(canvas);
        canvas.restore();


        return bmp;
    }

    public static String getCurrentDate() {
        SimpleDateFormat  simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return simpleDateFormat.format(new Date()).trim();
    }

    public static String getCurrentTime() {
        SimpleDateFormat  simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return simpleDateFormat.format(new Date()).trim();
    }
    public static String getFormattedDate(String inputFormat,String outputFormat,String date) {

        return ChangeDateFormat(inputFormat,outputFormat,date);
    }

    public static String getFormattedTime(String inputFormat,String outputFormat,String time) {

        return ChangeTimeFormat(inputFormat,outputFormat,time);
    }


    public static String getAddressFromLatLng(Context context, String latitude, String longitude) {

        String address = "";
        if(isInternetOn(context)){
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);

                if (!addresses.isEmpty()) {

                    address =  addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getAdminArea() + " " + addresses.get(0).getPostalCode() + "," + addresses.get(0).getCountryName();

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return address;
    }


    public static String parseError(Response<?> response){
        JSONArray jsonArray = null;
        String value ="";
        try {
            JSONObject jObjError = new JSONObject(response.errorBody().string());
            jsonArray = jObjError.getJSONArray("errors");
            value = jsonArray.getJSONObject(0).getString("message");
            return value ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean isTravelStart(Context context){
        boolean isTravelStart;
        if (getSharedPreferences(context, Constant.localConveyanceJourneyStart) != null &&
                !getSharedPreferences(context, Constant.localConveyanceJourneyStart).isEmpty()
                && getSharedPreferences(context, Constant.localConveyanceJourneyStart).equals("true")) {
            isTravelStart = true;
        }else {
            isTravelStart = false;
        }
        return isTravelStart;
    }

    public static void logout(Context context) {
        clearSharedPreferences(context);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.deleteData(DatabaseHelper.TABLE_COMPLAINT_IMAGE_DATA);
        databaseHelper.deleteData(DatabaseHelper.TABLE_LOCAL_CONVEYANCE_DATA);
        databaseHelper.deleteData(DatabaseHelper.TABLE_MARK_ATTENDANCE_DATA);
        databaseHelper.deleteData(DatabaseHelper.TABLE_COMPLAINT_STATUS_DATA);
        databaseHelper.deleteData(DatabaseHelper.TABLE_COMPLAINT_DATA);
        databaseHelper.deleteData(DatabaseHelper.TABLE_CHECK_OUT_IMAGE_DATA);
        databaseHelper.deleteData(DatabaseHelper.TABLE_SITE_SURVEY_IMAGE_DATA);
        databaseHelper.deleteData(DatabaseHelper.TABLE_PENDING_REASON_DATA);
        databaseHelper.deleteData(DatabaseHelper.TABLE_COMPLAINT_CATEGORY);
        databaseHelper.deleteData(DatabaseHelper.TABLE_COMPLAINT_DEFECT);
        databaseHelper.deleteData(DatabaseHelper.TABLE_COMPLAINT_RELATED);
        databaseHelper.deleteData(DatabaseHelper.TABLE_COMPLAINT_CLOSURE);
        databaseHelper.deleteData(DatabaseHelper.TABLE_PENDING_REASON_IMAGE_DATA);
        databaseHelper.deleteData(DatabaseHelper.TABLE_COMPLAINT_FORWARD_PERSON_DATA);

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public static boolean isOnRoleApp(){
        boolean isOnRoleApp = false;
        if(BuildConfig.IS_ONROLE){
            isOnRoleApp = true;
        }else {
            isOnRoleApp = false;
        }
        return isOnRoleApp;
    }

    public static boolean isFreelancerLogin(Context context){
        boolean isFreelancerLogin = false;
        if(Utility.getSharedPreferences(context,Constant.loginType).equals(Constant.freelancer)||
                Utility.getSharedPreferences(context,Constant.loginType).equals(Constant.serviceCenterTech)){
            isFreelancerLogin = true;
        }else if(Utility.getSharedPreferences(context,Constant.loginType).equals(Constant.employee)){
            isFreelancerLogin = false;
        }
        return true;
    }

    public static String getBase64FromPath(Context context,String Imagepath) {
        String imageString=" ";
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(Imagepath);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        }catch (Exception e){
            e.printStackTrace();
        }
        return imageString;

    }
    public static String getOtp(){
        Random random = new Random();
        return  String.format("%04d", random.nextInt(10000));
    }
    public static String getHashKey(Context context) {
        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(context);
        return appSignatureHashHelper.getAppSignatures().get(0);
    }

    public static int selectedPosition(List<SpinnerDataModel> array, String value){
        int position =0;
        for (int i=0; i<array.size(); i++){
            if(array.get(i).getName().equals(value)){
                position = i;
            }
        }


        return position;
    }
}
