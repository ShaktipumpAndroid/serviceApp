package com.shaktipumplimted.serviceapp.webService.uploadImages;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.webService.api.APIS;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.uploadImages.interfaces.ActionListenerCallback;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class UploadImageAPIS {

    ActionListenerCallback callBack;
    public  static Context mContext;
    JSONArray jsonArray;

    public UploadImageAPIS(Context context) {

        //initializing the callback object from the constructor
        this.mContext = context;
        this.callBack = null;

    }

    public void setActionListener(JSONArray jsonArray, String Value, ActionListenerCallback callBack) {

        this.callBack = callBack;
        this.jsonArray = jsonArray;
        switch (Value) {
            case Constant.addPendingImage:

                new syncData(jsonArray, APIS.BASEURL + APIS.ADD_PENDING_REASON + "?token="
                        + Utility.getSharedPreferences(mContext, Constant.accessToken), "inprocess_complaint", callBack).execute();
                break;
            case Constant.markAttendance:
                new syncData(jsonArray, APIS.BASEURL + APIS.MARK_ATTENDANCE + "?token="
                        + Utility.getSharedPreferences(mContext, Constant.accessToken), "data", callBack).execute();
                break;
            case Constant.localConveyance:
                new syncData(jsonArray, APIS.BASEURL + APIS.LOCAL_CONVEYANCE + "?token="
                        + Utility.getSharedPreferences(mContext, Constant.accessToken), "data", callBack).execute();
                break;
            case Constant.OffrollClosure:
                new syncData(jsonArray, APIS.BASEURL + APIS.FREELANCER_CLOSURE + "?token="
                        + Utility.getSharedPreferences(mContext, Constant.accessToken), "data", callBack).execute();
                break;
        }

    }


    private static class syncData extends AsyncTask<String, String, String> {
        JSONArray jsonArray;
        String url, arrayName;
        ActionListenerCallback callBack;

        public syncData(JSONArray jaInvcData, String url, String arrayName, ActionListenerCallback callBack) {
            this.jsonArray = jaInvcData;
            this.url = url;
            this.arrayName = arrayName;
            this.callBack = callBack;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            String obj2 = null;
            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair(arrayName, String.valueOf(jsonArray)));
            Log.e("DATA", "$$$$" + param1_invc);
            System.out.println("param1_invc_vihu==>>" + param1_invc);
            try {
                obj2 = executeHttpPost1(url, param1_invc);

            } catch (Exception e) {
                e.printStackTrace();

            }

            return obj2;

        }

        @Override
        protected void onPostExecute(String result) {

            try {

                JSONObject jsonObject = new JSONObject(result);

                    if (!result.isEmpty()) {
                        if(jsonObject.getString("status").equals(Constant.TRUE)) {
                            callBack.onActionSuccess(result);
                        }else  if(jsonObject.getString("status").equals(Constant.FALSE)) {
                            callBack.onActionFailure(result);
                        }else  if(jsonObject.getString("status").equals(Constant.FAILED)) {
                            callBack.onActionFailure(result);
                        }
                    } else {
                        callBack.onActionFailure(result);
                    }

            } catch (Exception e) {
                e.printStackTrace();
                callBack.onActionFailure(result);
            }
        }

    }

    public static String executeHttpPost1(String url, ArrayList<NameValuePair> postParameters) throws Exception {
        BufferedReader reader = null;
        String result = "";

        System.out.println("URL comes in jsonparser class is:  " + url);
        Log.e("url is....", url + "");
        Log.e("params  is....", postParameters + "");

        try {

            int TIMEOUT_MILLISEC = 150000; // = 12 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            InputStream is = httpResponse.getEntity().getContent();
            reader = new BufferedReader(new InputStreamReader(
                    is, StandardCharsets.ISO_8859_1), 8);

            StringBuilder sb = new StringBuilder();

            String line = null;


            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }
            is.close();


            result = sb.toString();

            System.out.println("URL comes in jsonparser class is:" + result);
            return result;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }

}
