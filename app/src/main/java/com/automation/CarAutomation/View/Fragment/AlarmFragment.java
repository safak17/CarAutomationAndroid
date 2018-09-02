package com.automation.CarAutomation.View.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.automation.CarAutomation.Controller.AlarmAdapter;
import com.automation.CarAutomation.Controller.App;
import com.automation.CarAutomation.Model.ArduinoVariableContainer;
import com.automation.CarAutomation.R;

public class AlarmFragment extends Fragment {

    public AlarmFragment() { }

    ArduinoVariableContainer arduinoVariableContainer = ArduinoVariableContainer.getInstance();

    View rootView;
    RelativeLayout emptyRelativeLayout;
    RecyclerView alarmRecyclerView;
    public AlarmAdapter alarmAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_alarm, container, false);

        alarmRecyclerView   =  rootView.findViewById(R.id.rv_alarm_list);
        emptyRelativeLayout =  rootView.findViewById(R.id.rl_empty_view);

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("onActivityCreated", "AlarmFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "AlarmFragment");

        if( arduinoVariableContainer.alarmList.size() == 0 )
            showEmptyAlarmListScreen();
        else
            showAlarmList();
    }


    private void showEmptyAlarmListScreen(){
        alarmRecyclerView.setVisibility(View.GONE);
        emptyRelativeLayout.setVisibility(View.VISIBLE);
    }

    private void showAlarmList(){
        alarmAdapter = new AlarmAdapter(App.getContext(), arduinoVariableContainer.alarmList);
        alarmRecyclerView.setAdapter(alarmAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        alarmRecyclerView.setLayoutManager(linearLayoutManager);

        alarmRecyclerView.setVisibility(View.VISIBLE);
        emptyRelativeLayout.setVisibility(View.GONE);

    }
}
