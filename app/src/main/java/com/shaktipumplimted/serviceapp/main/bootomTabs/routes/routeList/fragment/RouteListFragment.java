package com.shaktipumplimted.serviceapp.main.bootomTabs.routes.routeList.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintDetails.ComplaintDetailsActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.adpater.ComplaintListAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.adpater.ComplaintStatusAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintListModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.model.ComplaintStatusModel;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customersList.CustomerListActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.routeList.adapter.RouteListAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.routeList.model.RouteListModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

import java.util.ArrayList;
import java.util.List;

public class RouteListFragment extends Fragment implements RouteListAdapter.ItemClickListener {


    View view;

    Context mContext;
    RecyclerView routeList;
    List<RouteListModel> routeArrayList;

    RouteListAdapter routeListAdapter;
    RelativeLayout searchRelative;
    SearchView searchUser;
    TextView noDataFound;
    SwipeRefreshLayout pullToRefresh;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_route_list, container, false);

        Init(view);
        return view;
    }

    private void Init(View view) {
        mContext = getActivity();
        routeList = view.findViewById(R.id.routeList);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        noDataFound = view.findViewById(R.id.noDataFound);
        searchUser = view.findViewById(R.id.searchUser);
        searchRelative = view.findViewById(R.id.searchRelative);

        searchViewMethod();
        listner();

        getList();
    }

    private void listner() {
        pullToRefresh.setOnRefreshListener(() -> {
                getList();
        });
    }

    private void getList() {
        if(Utility.isInternetOn(mContext)){
            getRouteList();
        }else {
            Utility.ShowToast(getResources().getString(R.string.checkInternetConnection),mContext);
        }
    }

    private void getRouteList() {
        routeArrayList = new ArrayList<>();
        routeArrayList.add(new RouteListModel("01","Indore Branch Office"));
        routeArrayList.add(new RouteListModel("01","Pithampur Branch Office"));
        routeArrayList.add(new RouteListModel("01","Dhar Office"));
        routeArrayList.add(new RouteListModel("01","Indore Branch HO Office"));

        if(routeArrayList.size()>0){
            routeListAdapter = new RouteListAdapter(mContext,routeArrayList,noDataFound);
            routeList.setHasFixedSize(true);
            routeList.setAdapter(routeListAdapter);
            routeListAdapter.ItemClick(this);

            routeList.setVisibility(View.VISIBLE);
            noDataFound.setVisibility(View.GONE);
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
                if (routeListAdapter != null) {
                    if(!query.isEmpty()) {
                        routeListAdapter.getFilter().filter(query);
                    }}

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (routeListAdapter != null) {
                    if(!newText.isEmpty()) {
                        routeListAdapter.getFilter().filter(newText);
                    }
                }
                return false;
            }
        });

        searchClose.setOnClickListener(v -> {

            searchUser.onActionViewCollapsed();
            if (routeListAdapter != null) {
                routeListAdapter.getFilter().filter("");
            }
        });

    }


    @Override
    public void SetOnItemClickListener(RouteListModel response, int position) {
        Intent intent = new Intent(mContext, CustomerListActivity.class);
        startActivity(intent);
    }
}