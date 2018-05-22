package com.android.leezp.learncarproject.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.leezp.learncarproject.ActivityCollector;
import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.adapter.ExamCheatsAdapter;
import com.android.leezp.learncarproject.entity.DriverProcessEntity;
import com.android.leezp.learncarproject.entity.ExamCheatsEntity;

import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leezp on 2018/4/1.
 */

public class ExamCheatsFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private View view;
    private RadioGroup group;
    private RecyclerView recycler;
    private ExamCheatsAdapter adapter;
    private Map<Integer, String> titleData = new HashMap<>();
    private Map<Integer, List<ExamCheatsEntity>> contentData = new HashMap<>();
    private ExamCheatsHandler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exam_cheats, container, false);
        group = ((RadioGroup) view.findViewById(R.id.fragment_exam_cheats_group));
        recycler = ((RecyclerView) view.findViewById(R.id.fragment_exam_cheats_recycler));

        handler = new ExamCheatsHandler(this);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ExamCheatsAdapter(null);
        recycler.setAdapter(adapter);

        group.setOnCheckedChangeListener(this);
        if (group.getChildCount() > 0) {
            ((RadioButton) group.getChildAt(0)).setChecked(true);
        }

        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        RadioButton child = null;
        for (int i = 0; i < radioGroup.getChildCount(); ++i) {
            child = ((RadioButton) radioGroup.getChildAt(i));
            if (child.isChecked() == true) {
                break;
            }
        }
        Object object = child.getTag();
        if (object != null) {
            int processOrder = Integer.valueOf(String.valueOf(object));
            adapter.notifyDataChange(contentData.get(processOrder));
        }

    }

    public void refreshTitlePart() {
        ActivityCollector.cachedThreadPool.execute(new ExamCheatsRunnable(this));
    }

    public void refreshContentPart() {
        ActivityCollector.cachedThreadPool.execute(new ExamCheatsRunnable(this, 1));
    }

    public void setRadioGroup() {
        group.removeAllViews();
        if (titleData.size() > 0) {
            for (Map.Entry<Integer, String> entry : titleData.entrySet()) {
                RadioButton radioButton = ((RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.fragment_exam_cheats_radio_button, null));
                radioButton.setText(entry.getValue());
                radioButton.setTag(entry.getKey());
                group.addView(radioButton);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                layoutParams.setMargins(1, 2, 1, 2);
                radioButton.setLayoutParams(layoutParams);
            }
            ((RadioButton) group.getChildAt(0)).setChecked(true);
        }
    }

    private static class ExamCheatsRunnable implements Runnable {
        private WeakReference<ExamCheatsFragment> referenceFragment;
        private int operateWay = 0;

        public ExamCheatsRunnable(ExamCheatsFragment fragment) {
            referenceFragment = new WeakReference<>(fragment);
        }

        public ExamCheatsRunnable(ExamCheatsFragment fragment, int operateWay) {
            referenceFragment = new WeakReference<>(fragment);
            this.operateWay = operateWay;
        }

        @Override
        public void run() {
            if (operateWay == 0) {
                loadTitleData();
            }
            if (operateWay == 1) {
                loadContentData();
            }
        }

        private void loadContentData() {
            List<ExamCheatsEntity> entities = DataSupport.findAll(ExamCheatsEntity.class);
            referenceFragment.get().contentData.clear();
            if (entities != null && entities.size() > 0) {
                for (ExamCheatsEntity entity : entities) {
                    List<ExamCheatsEntity> list = referenceFragment.get().contentData.get(entity.getProcess_order());
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(entity);
                    referenceFragment.get().contentData.put(entity.getProcess_order(), list);
                }
                Message message = Message.obtain();
                message.what = 1;
                referenceFragment.get().handler.sendMessage(message);
            }
        }

        private void loadTitleData() {
            List<DriverProcessEntity> entities = DataSupport.select("process_order", "title").find(DriverProcessEntity.class);
            referenceFragment.get().titleData.clear();
            if (entities != null && entities.size() > 0) {
                for (DriverProcessEntity entity : entities) {
                    referenceFragment.get().titleData.put(entity.getProcess_order(), entity.getTitle());
                }
            }
            Message message = Message.obtain();
            message.what = 0;
            while (referenceFragment.get().handler== null);
            referenceFragment.get().handler.sendMessage(message);
        }
    }

    private static class ExamCheatsHandler extends Handler {
        private WeakReference<ExamCheatsFragment> referenceFragment;

        public ExamCheatsHandler(ExamCheatsFragment fragment) {
            referenceFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    referenceFragment.get().setRadioGroup();
                    break;
                case 1:
                    ((RadioButton) referenceFragment.get().group.getChildAt(0)).setChecked(true);
                    break;
            }
        }
    }
}
