package com.shaktipumplimted.serviceapp.main.bootomTabs.unsync;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.Utils.common.model.CommonRespModel;
import com.shaktipumplimted.serviceapp.database.DatabaseHelper;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry.DSREntryActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.dsrEntry.model.DsrDetailsModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.markAttendance.model.MarkAttendanceModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.unsync.adapter.AttendanceAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.unsync.adapter.DsrAdapter;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;
import com.shaktipumplimted.serviceapp.webService.retofit.APIClient;
import com.shaktipumplimted.serviceapp.webService.retofit.APIInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnsyncListFragment extends Fragment implements DsrAdapter.ItemClickListener {

    RecyclerView dsrListView, attendanceListView;
    View view;
    DsrAdapter dsrAdapter;
    AttendanceAdapter attendanceAdapter;
    DatabaseHelper databaseHelper;
    List<DsrDetailsModel> dsrDetailsModelList;
    List<MarkAttendanceModel> attendanceModelList;
    APIInterface apiInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void inIt(View view) {
        apiInterface = APIClient.getRetrofit(getActivity()).create(APIInterface.class);
        dsrListView = view.findViewById(R.id.DsrListView);
        attendanceListView = view.findViewById(R.id.attendanceListView);
        databaseHelper = new DatabaseHelper(getActivity());
        retrieveValue();
    }

    private void retrieveValue() {
        dsrDetailsModelList = new ArrayList<>();
        if (databaseHelper.isDataAvailable(databaseHelper.TABLE_DSR_RECORD)) {
            dsrDetailsModelList = databaseHelper.getAllDsrEntry();
            if (dsrDetailsModelList.size() > 0) {
                setDsrAdapter();
            }
        }
        attendanceModelList = new ArrayList<>();
        if (databaseHelper.isDataAvailable(databaseHelper.TABLE_MARK_ATTENDANCE_DATA)) {
            attendanceModelList = databaseHelper.getAllMarkAttendanceData(true,"");
            if (attendanceModelList.size() > 0) {
                setAttendanceAdapter();
            }
        }

    }

    private void setAttendanceAdapter() {

    }

    private void setDsrAdapter() {
        dsrAdapter = new DsrAdapter(getActivity(), dsrDetailsModelList);
        dsrListView.setHasFixedSize(true);
        dsrListView.setAdapter(dsrAdapter);
        dsrAdapter.ItemClick(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_unsync_list, container, false);

        inIt(view);
        return view;
    }

    @Override
    public void SetOnItemClickListener(DsrDetailsModel dsrModel, int position) throws JSONException {
        if (Utility.isInternetOn(getActivity())) {
            Utility.showProgressDialogue(getActivity());
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("budat", dsrModel.getDate());
            jsonObject.put("help_name", dsrModel.getDsrActivity());
            jsonObject.put("dsr_agenda", dsrModel.getDsrPurpose());
            jsonObject.put("dsr_comment", dsrModel.getDsrOutcome());
            jsonObject.put("time", dsrModel.getTime());
            jsonObject.put("latitude", dsrModel.getLat());
            jsonObject.put("longitude", dsrModel.getLng());

            jsonArray.put(jsonObject);


            Call<CommonRespModel> call3 = apiInterface.saveDsrEntry(Utility.getSharedPreferences(getActivity(), Constant.accessToken), jsonArray.toString());
            call3.enqueue(new Callback<CommonRespModel>() {
                @Override
                public void onResponse(@NonNull Call<CommonRespModel> call, @NonNull Response<CommonRespModel> response) {
                    Utility.hideProgressDialogue();
                    if (response.isSuccessful()) {
                        CommonRespModel commonRespModel = response.body();
                        if (commonRespModel.getStatus().equals(Constant.TRUE)) {
                            Utility.ShowToast(commonRespModel.getMessage(), getActivity());
                            databaseHelper.deleteSpecificItem(DatabaseHelper.TABLE_DSR_RECORD,DatabaseHelper.KEY_DSR_DATE,dsrModel.getDate());
                            dsrAdapter.notifyDataSetChanged();
                        } else if (commonRespModel.getStatus().equals(Constant.FALSE)) {
                            Utility.hideProgressDialogue();
                            Utility.ShowToast(getResources().getString(R.string.something_went_wrong), getActivity());
                        } else if (commonRespModel.getStatus().equals(Constant.FAILED)) {
                            Utility.logout(getActivity());
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<CommonRespModel> call, @NonNull Throwable t) {
                    call.cancel();
                    Utility.hideProgressDialogue();
                    Log.e("Error====>", t.getMessage().toString().trim());
                }
            });
        } else {
            Utility.ShowToast(getActivity().getResources().getString(R.string.checkInternetConnection), getActivity());
        }
    }
}