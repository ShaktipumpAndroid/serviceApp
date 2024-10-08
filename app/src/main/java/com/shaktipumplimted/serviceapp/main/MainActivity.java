package com.shaktipumplimted.serviceapp.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.Utility;
import com.shaktipumplimted.serviceapp.main.bootomTabs.complaints.complaintList.fragment.ComplaintListFragment;
import com.shaktipumplimted.serviceapp.main.bootomTabs.profile.fragment.ProfileFragment;
import com.shaktipumplimted.serviceapp.main.bootomTabs.routes.routeList.fragment.RouteListFragment;
import com.shaktipumplimted.serviceapp.main.bootomTabs.unsync.UnsyncListFragment;
import com.shaktipumplimted.serviceapp.webService.extra.Constant;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    Toolbar toolbar;
    String apiCall ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
        retrieveValue();
    }

    private void retrieveValue() {
        if(getIntent().getExtras()!=null){
            apiCall = getIntent().getStringExtra(Constant.APICALL);
        }
        listner();
    }

    private void listner() {

        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {


                case R.id.navigation_complaint:
                    Fragment fragment = new ComplaintListFragment();
                    Bundle mBundle = new Bundle();
                    mBundle.putString(Constant.APICALL, apiCall);
                    fragment.setArguments(mBundle);
                    Utility.loadFragment(MainActivity.this, fragment,
                            false, null);

                    return true;

                case R.id.navigation_unsync:


                    Utility.loadFragment(MainActivity.this, new UnsyncListFragment(),
                            false, null);

                    break;
                case R.id.navigation_profile:

                    Utility.loadFragment(MainActivity.this, new ProfileFragment(), false,
                            null);

                    return true;
            }
            return true;
        });
        navigationView.setSelectedItemId(R.id.navigation_complaint);
    }

    private void Init() {
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(navigationView);

        toolbar.setTitle(getResources().getString(R.string.app_name));
    }

    private static class BottomNavigationViewHelper {

        @SuppressLint("RestrictedApi")
        static void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item =
                            (BottomNavigationItemView) menuView.getChildAt(i);
                    item.setShifting(false);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }
    }
}