package com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customersList;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.addCustomer.AddCustomerActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customerDetails.CustomerDetailActivity;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customersList.adapter.CustomerListAdapter;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.customersList.model.CustomerListModel;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

import java.util.ArrayList;
import java.util.List;

public class CustomerListActivity extends AppCompatActivity implements View.OnClickListener, CustomerListAdapter.ItemClickListener {
    Toolbar toolbar;
    RecyclerView customerList;

    List<CustomerListModel> customersArrayList;
    CustomerListAdapter customerListAdapter;
    TextView noDataFound;
    SwipeRefreshLayout pullToRefresh;

    FloatingActionButton addCustomerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        Init();
        listner();
    }


    private void Init() {
        toolbar = findViewById(R.id.toolbar);
        customerList = findViewById(R.id.customerList);
        noDataFound = findViewById(R.id.noDataFound);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        addCustomerBtn = findViewById(R.id.addCustomerBtn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.customerList));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        if (Utility.isInternetOn(getApplicationContext())) {
            getList();
        } else {
            Utility.ShowToast(getResources().getString(R.string.checkInternetConnection), getApplicationContext());
        }
    }

    private void getList() {
        customersArrayList = new ArrayList<>();


        customersArrayList.add(new CustomerListModel("Sanjay Choudhary", "Indore", "sanjay@shaktiPumps.com", "9827465890", "22.657523375603056", "75.9106951632684"));
        customersArrayList.add(new CustomerListModel("Tarun", "Indore", "tarun@shaktiPumps.com", "9028274618", "22.657523375603056", "75.9106951632684"));
        customersArrayList.add(new CustomerListModel("Rahul Patel", "Indore", "rahul@shaktiPumps.com", "92847648291", "22.657523375603056", "75.9106951632684"));
        customersArrayList.add(new CustomerListModel("Mr Dev Ji", "Indore", "dev@shaktiPumps.com", "9284758294", "22.657523375603056", "75.9106951632684"));


        if (customersArrayList.size() > 0) {
            customerListAdapter = new CustomerListAdapter(this, customersArrayList, noDataFound);
            customerList.setHasFixedSize(true);
            customerList.setAdapter(customerListAdapter);
            customerListAdapter.ItemClick(this);
            customerList.setVisibility(View.VISIBLE);
            noDataFound.setVisibility(View.GONE);
        } else {
            customerList.setVisibility(View.GONE);
            noDataFound.setVisibility(View.VISIBLE);
        }
    }

    private void listner() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        addCustomerBtn.setOnClickListener(this);
        pullToRefresh.setOnRefreshListener(() -> {
            if (Utility.isInternetOn(getApplicationContext())) {
                getList();
            } else {
                Utility.ShowToast(getResources().getString(R.string.checkInternetConnection), getApplicationContext());
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.addCustomerBtn:
                Intent intent = new Intent(getApplicationContext(), AddCustomerActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void SetOnItemClickListener(CustomerListModel response, int position) {
        Intent intent = new Intent(getApplicationContext(), CustomerDetailActivity.class);
        intent.putExtra(Constant.customerDetails,response);
        startActivity(intent);
    }
}