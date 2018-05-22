package com.android.leezp.learncarproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.adapter.HelpAdapter;
import com.android.leezp.learncarproject.entity.HelpEntity;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Leezp on 2018/4/2.
 */

public class HelpFragment extends Fragment implements View.OnClickListener, OnItemClickListener, View.OnTouchListener {

    private View view;
    private TextView opinionBtn;
    private RecyclerView helpRecycler;
    private HelpAdapter adapter;
    private LinearLayout showPart;
    private TextView helpTitle;
    private TextView helpContent;

    //数据源
    List<HelpEntity> data = null;
    private float startX;
    private float startY;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help, container, false);
        opinionBtn = ((TextView) view.findViewById(R.id.fragment_help_opinion_btn));
        helpRecycler = ((RecyclerView) view.findViewById(R.id.fragment_help_recycler));
        showPart = ((LinearLayout) view.findViewById(R.id.fragment_help_show_part));
        helpTitle = ((TextView) view.findViewById(R.id.fragment_help_show_part_title));
        helpContent = ((TextView) view.findViewById(R.id.fragment_help_show_part_content));

        helpRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        helpRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new HelpAdapter(data);
        helpRecycler.setAdapter(adapter);

        loadData();
        adapter.addOnItemClickListener(this);
        opinionBtn.setOnClickListener(this);
        showPart.setOnTouchListener(this);
        return view;
    }

    /**
     * 加载数据
     */
    private void loadData() {
        data = DataSupport.findAll(HelpEntity.class);
        if (data.size() > 0) {
            adapter.notifyDataChange(data);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_help_opinion_btn:
                showMessage("该功能在下个版本才开放！");
                break;
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fragment_show_part_in);
        showPart.startAnimation(animation);
        showPart.setVisibility(View.VISIBLE);
        HelpEntity entity = data.get(position);
        if (entity == null) {
            Object object = view.getTag();
            if (object != null) {
                String netId = String.valueOf(object);
                List<HelpEntity> entities = DataSupport.where("netId=?", netId).find(HelpEntity.class);
                if (entities.size() > 0) {
                    entity = entities.get(0);
                }
            }
        }
        if (entity != null) {
            helpTitle.setText(entity.getTitle());
            helpContent.setText(entity.getContent());
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            startX = motionEvent.getX();
            startY = motionEvent.getY();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (Math.abs(startY - y) < 20 && x - startX < 20) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fragment_show_part_out);
                showPart.startAnimation(animation);
                showPart.setVisibility(View.GONE);
            }
        }
        return false;
    }
}
