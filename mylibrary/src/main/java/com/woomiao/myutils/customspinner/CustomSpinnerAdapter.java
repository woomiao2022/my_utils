package com.woomiao.myutils.customspinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.woomiao.myutils.R;

import java.util.List;

public class CustomSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private float txtSize;

    public CustomSpinnerAdapter(Context context, List<String> list, float txtSize) {
        this.context = context;
        this.list = list;
        this.txtSize = txtSize;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_item, null);
        }
        TextView textView = convertView.findViewById(R.id.customSpinner_content_tv);
        textView.getPaint().setTextSize(txtSize);
        textView.setText(list.get(position));
        return convertView;
    }

    public void update(List<String> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
