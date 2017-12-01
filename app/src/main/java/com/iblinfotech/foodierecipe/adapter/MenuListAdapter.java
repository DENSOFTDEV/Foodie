package com.iblinfotech.foodierecipe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iblinfotech.foodierecipe.MainActivity;
import com.iblinfotech.foodierecipe.R;
import com.iblinfotech.foodierecipe.utils.GlobalClass;

public class MenuListAdapter extends BaseAdapter {
    Context context;
    String[] itemName;
    public static TextView tv_itemName;
    private int currentPosition;
    private Typeface Robotoregular;
    private LayoutInflater inflater;

    public MenuListAdapter(Context context, String[] itemName, int currentPosition) {
        this.context = context;
        this.itemName = itemName;
        this.currentPosition = currentPosition;
    }

    @Override
    public int getCount() {
        return itemName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (context != null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_list_menu, parent, false);

        }
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Robotoregular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");

        tv_itemName = (TextView) convertView.findViewById(R.id.tv_itemName);
        if (GlobalClass.getPrefrenceBoolean(context, "isLogin", false) == true) {
            if (itemName[position].contains("LOGIN")) {
                tv_itemName.setVisibility(View.GONE);
            } else {
                tv_itemName.setVisibility(View.VISIBLE);
            }
        } else {
            if (itemName[position].contains("LOGOUT") || itemName[position].contains("CHANGE PASSWORD")) {
                tv_itemName.setVisibility(View.GONE);
            } else {
                tv_itemName.setVisibility(View.VISIBLE);
            }
        }

//        if (itemName[position].contains("LOGOUT") && !GlobalClass.fbLogIn) {
//            if (itemName[position].contains("CHANGE PASSWORD")) {
//                tv_itemName.setVisibility(View.GONE);
//            } else {
//                tv_itemName.setVisibility(View.VISIBLE);
//            }
//        }

        tv_itemName.setText(itemName[position]);
        if (position == MainActivity.mSelectedItem) {
            tv_itemName.setTextColor(Color.parseColor("#FFFFFF"));
            tv_itemName.setTypeface(Robotoregular);
            notifyDataSetChanged();
        }

        return convertView;
    }
}
