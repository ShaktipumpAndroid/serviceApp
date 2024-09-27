package com.shaktipumplimted.serviceapp.Utils.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shaktipumplimted.serviceapp.R;
import com.shaktipumplimted.serviceapp.Utils.common.model.SpinnerDataModel;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<SpinnerDataModel> {

    Context mContext;
    public SpinnerAdapter(Context context,
                          List<SpinnerDataModel> algorithmList) {
        super(context, 0, algorithmList);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
    View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
    View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent) {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.spinnerTxt);
        ImageView dropImg = convertView.findViewById(R.id.dropImg);
        SpinnerDataModel currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            if (currentItem.getId() != null && !currentItem.getName().isEmpty()) {
                textViewName.setText(currentItem.getName());
            }
            if(position==0){
                dropImg.setVisibility(View.VISIBLE);
                textViewName.setTextColor(mContext.getResources().getColor(R.color.grayish));
            }
        }
        return convertView;
    }
}
