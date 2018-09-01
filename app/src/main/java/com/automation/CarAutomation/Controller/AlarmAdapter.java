package com.automation.CarAutomation.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.automation.CarAutomation.Model.Alarm;
import com.automation.CarAutomation.R;

import java.util.ArrayList;


public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    private ArrayList<Alarm> alarmList;
    private LayoutInflater inflater;

    public AlarmAdapter(Context context, ArrayList<Alarm> alarmList) {
        inflater = LayoutInflater.from(context);
        this.alarmList = alarmList;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycleview_item_alarm, parent, false);

       return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        Alarm selectedAlarm = alarmList.get(position);
        holder.setUserInterfaceData(selectedAlarm);
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}
