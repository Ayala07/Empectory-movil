package com.example.tonyayala.empectory.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyayala.empectory.R;

import java.util.ArrayList;

public class HelpAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Integer> listId;
    private ArrayList<String> nameList;
    private ArrayList<String> subnameList;

    public HelpAdapter(Context context, ArrayList<Integer> listId, ArrayList<String> nameList, ArrayList<String> subnameList) {
        this.context = context;
        this.listId = listId;
        this.nameList = nameList;
        this.subnameList = subnameList;
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.help_options, null);
        }

        ImageView iconImg =convertView.findViewById(R.id.help_icon_option);
        TextView tittleOption = convertView.findViewById(R.id.help_tittle_option);
        TextView subTittleOption = convertView.findViewById(R.id.help_sub_tittle_option);

        iconImg.setImageResource(listId.get(position));
        tittleOption.setText(nameList.get(position));
        subTittleOption.setText(subnameList.get(position));

        return convertView;
    }
}
