package com.example.dartswithfriends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FriendInvitesLvAdapter extends BaseAdapter {

    private List<SMS> sms_list;
    private int layoutId;
    private LayoutInflater inflater;

    public FriendInvitesLvAdapter(Context ctx, int layoutId, List<SMS> sms_list) {
        this.sms_list = sms_list;
        this.layoutId = layoutId;
        this.inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return sms_list.size();
    }

    @Override
    public Object getItem(int position) {
        return sms_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        SMS sms = sms_list.get(i);
        View listItem = (view == null) ? inflater.inflate(this.layoutId, null) : view;
        ((TextView) listItem.findViewById(R.id.number)).setText(sms.getNumber());
        ((TextView) listItem.findViewById(R.id.msg)).setText(sms.getMsg());
        return listItem;
    }
}
