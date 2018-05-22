package com.android.leezp.learncarproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.activity.UserLoginActivity;
import com.android.leezp.learncarproject.adapter.DriverProcessAdapter;
import com.android.leezp.learncarproject.entity.ActivityConstantEntity;
import com.android.leezp.learncarproject.entity.DriverProcessEntity;
import com.android.leezp.learncarproject.entity.StudentEntity;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Leezp on 2018/4/1.
 */

public class DriverProcessFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView title;
    private RecyclerView recycler;
    private DriverProcessAdapter adapter;
    private SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_driver_process, container, false);
        title = ((TextView) view.findViewById(R.id.fragment_driver_process_title));
        recycler = ((RecyclerView) view.findViewById(R.id.fragment_driver_process_recycler));
        preferences = getActivity().getSharedPreferences(ActivityConstantEntity.personalInformation_sharePreferences_name, Context.MODE_PRIVATE);

        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        List<DriverProcessEntity> entities = DataSupport.findAll(DriverProcessEntity.class);
        adapter = new DriverProcessAdapter(entities);
        recycler.setAdapter(adapter);

        title.setOnClickListener(this);
        setTitleClickable();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_driver_process_title:
                UserLoginActivity.startUserLoginActivity(getActivity());
                break;
        }
    }

    public void setTitleClickable() {
        if (preferences != null) {
            String information = preferences.getString("personalInformation", null);
            if (information != null) {
                StudentEntity studentEntity = new Gson().fromJson(information, StudentEntity.class);
                List<DriverProcessEntity> processEntities = DataSupport.where("process_order=?", String.valueOf(studentEntity.getDriverProcess())).find(DriverProcessEntity.class);
                if (processEntities.size() > 0) {
                    title.setClickable(false);
                    title.setText("当前处于" + processEntities.get(0).getTitle());
                    return;
                }
            }
            title.setClickable(true);
            title.setText("未登录");
        }
    }
}
