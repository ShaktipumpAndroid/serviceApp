package com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.ComplaintDetailsActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.adpater.ComplaintListAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.adpater.ComplaintStatusAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintStatusModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

import java.util.ArrayList;
import java.util.List;

public class ComplaintListFragment extends Fragment implements ComplaintStatusAdapter.ItemClickListener,ComplaintListAdapter.ItemClickListener {

    View view;
    Context mContext;
    RecyclerView compStatusList, compList;
    List<ComplaintStatusModel> complaintStatusArrayList;
    List<ComplaintListModel> complaintArrayList;

    ComplaintStatusAdapter complaintStatusAdapter;
    ComplaintListAdapter complaintListAdapter;

    RelativeLayout searchRelative;
    SearchView searchUser;
    TextView noDataFound;
    SwipeRefreshLayout pullToRefresh;
    int selectedPosition = 0, prevPosition = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_complaint_list, container, false);

        Init(view);



        return view;
    }

    private void getcomplaintList() {
        complaintArrayList = new ArrayList<>();

        complaintArrayList.add(new ComplaintListModel("GB0124","balkrishanan","9963894729",
                "thoppust thiruvallur thiruvallur tamil nadu india","28.08.2024","REPLY"));
        complaintArrayList.add(new ComplaintListModel("GC6639","eldos thundathil","7994641221",
                "paravur paravur ernakulam kerala india","29.08.2024","REPLY"));
        complaintArrayList.add(new ComplaintListModel("GC8260","shanker nayak ji","9448124708",
                "bijapur bijapur bijapur karnataka india","29.08.2024","CLOSURE"));
        complaintArrayList.add(new ComplaintListModel("GE6620","sahdev nikam","9223317893",
                "dyaderi bijapur bijapur karnataka india","05.09.2024","REPLY"));
        complaintArrayList.add(new ComplaintListModel("GI6961","virendar","9963894729",
                "vilage /yadgir yadgir yadgir karnataka india","09.08.2024","REPLY"));

        complaintListAdapter = new ComplaintListAdapter(mContext, complaintArrayList,noDataFound);
        compList.setHasFixedSize(true);
        complaintListAdapter.ItemClick(ComplaintListFragment.this);
        compList.setAdapter(complaintListAdapter);
    }

    private void getStatusList() {
        complaintStatusArrayList = new ArrayList<>();

        complaintStatusArrayList.add(new ComplaintStatusModel("01", "New", true));
        complaintStatusArrayList.add(new ComplaintStatusModel("02", "Replay", false));
        complaintStatusArrayList.add(new ComplaintStatusModel("03", "Awaiting For Approval", false));
        complaintStatusArrayList.add(new ComplaintStatusModel("04", "Approved Complaint", false));
        complaintStatusArrayList.add(new ComplaintStatusModel("05", "Pending For Closure", false));
        complaintStatusArrayList.add(new ComplaintStatusModel("06", "Closure", false));

        complaintStatusAdapter = new ComplaintStatusAdapter(mContext, complaintStatusArrayList);
        compStatusList.setHasFixedSize(true);
        complaintStatusAdapter.ItemClick(ComplaintListFragment.this);
        compStatusList.setAdapter(complaintStatusAdapter);
    }

    private void Init(View view) {
        mContext = getActivity();
        compStatusList = view.findViewById(R.id.compStatusList);
        compList = view.findViewById(R.id.compList);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        noDataFound = view.findViewById(R.id.noDataFound);
        searchUser = view.findViewById(R.id.searchUser);
        searchRelative = view.findViewById(R.id.searchRelative);

        searchViewMethod();
        listner();
        getLists();

    }

    private void listner() {
        pullToRefresh.setOnRefreshListener(() -> {
                getLists();
        });
    }

    private void getLists() {
        if(Utility.isInternetOn(mContext)){
            getStatusList();
            getcomplaintList();
        }else {
            Utility.ShowToast(getResources().getString(R.string.checkInternetConnection),mContext);
        }
    }

    private void searchViewMethod() {
        searchRelative.setOnClickListener(v -> {
            searchUser.setFocusableInTouchMode(true);
            searchUser.requestFocus();
            searchUser.onActionViewExpanded();

        });

        ImageView searchIcon = searchUser.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.baseline_search_24));
        searchIcon.setColorFilter(getResources().getColor(R.color.colorPrimary));

        ImageView searchClose = searchUser.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchClose.setColorFilter(getResources().getColor(R.color.colorPrimary));


        EditText searchEditText = searchUser.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._14sdp));

        searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (complaintListAdapter != null) {
                    if(!query.isEmpty()) {
                        complaintListAdapter.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (complaintListAdapter != null) {
                    if(!newText.isEmpty()) {
                        complaintListAdapter.getFilter().filter(newText);
                    }
                }
                return false;
            }
        });

        searchClose.setOnClickListener(v -> {

            searchUser.onActionViewCollapsed();
            if (complaintListAdapter != null) {
                complaintListAdapter.getFilter().filter("");
            }
        });

    }

    @Override
    public void SetOnItemClickListener(ComplaintStatusModel response, int position) {


        selectedPosition = position;

        ComplaintStatusModel complaintStatusModel = new ComplaintStatusModel();
        complaintStatusModel.setId(response.getId());
        complaintStatusModel.setStatus(response.getStatus());
        complaintStatusModel.setSelected(!response.isSelected());
        complaintStatusArrayList.set(position, complaintStatusModel);
        complaintStatusAdapter.notifyItemChanged(position);

        if (prevPosition != position) {
            if (prevPosition != -1) {
                ComplaintStatusModel complaintStatusModel1 = new ComplaintStatusModel();
                complaintStatusModel1.setId(complaintStatusArrayList.get(prevPosition).getId());
                complaintStatusModel1.setStatus(complaintStatusArrayList.get(prevPosition).getStatus());
                complaintStatusModel1.setSelected(false);

                complaintStatusArrayList.set(prevPosition, complaintStatusModel1);
                complaintStatusAdapter.notifyItemChanged(prevPosition);
            }

            prevPosition = position;

        }

    }

    @Override
    public void SetOnItemClickListener(ComplaintListModel response, int position) {
        Intent intent = new Intent(mContext, ComplaintDetailsActivity.class);
        intent.putExtra(Constant.complaintData,response);
        startActivity(intent);
    }
}