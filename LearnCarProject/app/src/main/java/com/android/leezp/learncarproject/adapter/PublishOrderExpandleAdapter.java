package com.android.leezp.learncarproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;

import java.util.List;
import java.util.Map;

public class PublishOrderExpandleAdapter extends BaseExpandableListAdapter {
    private List<String> groupData;
    private Map<String, List<String>> childData;
    private Map<String, List<Integer>> publishOrderIdData;
    private OnItemClickListener itemClickListener;

    public PublishOrderExpandleAdapter(List<String> groupData, Map<String, List<String>> childData, Map<String, List<Integer>> publishOrderIdData) {
        this.groupData = groupData;
        this.childData = childData;
        this.publishOrderIdData = publishOrderIdData;
    }

    public void notifyDataChange(List<String> groupData, Map<String, List<String>> childData, Map<String, List<Integer>> publishOrderIdData) {
        this.groupData = groupData;
        this.childData = childData;
        this.publishOrderIdData = publishOrderIdData;
        notifyDataSetChanged();
    }

    public void addOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getGroupCount() {
        return groupData == null ? 0 : groupData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        List<String> childs = childData.get(groupData.get(i));
        return childs == null ? 0 : childs.size();
    }

    @Override
    public Object getGroup(int i) {
        return groupData.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childData.get(groupData.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return publishOrderIdData.get(groupData.get(i)).get(i1);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder holder = null;
        if (view == null) {
            holder = new GroupViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_publish_order_group_item, viewGroup, false);
            holder.date = ((TextView) view.findViewById(R.id.activity_publish_order_group_item_date));
            holder.indicator = ((ImageView) view.findViewById(R.id.activity_publish_order_group_item_indicator));
            view.setTag(holder);
        } else {
            holder = (GroupViewHolder) view.getTag();
        }
        if (b) {
            holder.indicator.setImageResource(R.drawable.activity_publish_order_down);
        } else {
            holder.indicator.setImageResource(R.drawable.activity_publish_order_right);
        }
        holder.date.setText(groupData.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder holder = null;
        if (view == null) {
            holder = new ChildViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_publish_order_child_item, viewGroup, false);
            holder.timeSlot = ((TextView) view);
            view.setTag(holder);
        } else {
            holder = ((ChildViewHolder) view.getTag());
        }
        holder.timeSlot.setText(childData.get(groupData.get(i)).get(i1));
        view.setTag(R.id.tag_first, publishOrderIdData.get(groupData.get(i)).get(i1));
        view.setTag(R.id.tag_second, groupData.get(i));
        holder.timeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, 0);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GroupViewHolder {
        private TextView date;
        private ImageView indicator;
    }

    private class ChildViewHolder {
        private TextView timeSlot;
    }
}
