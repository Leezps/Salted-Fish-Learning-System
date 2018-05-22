package com.android.leezp.learncartrainproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.leezp.learncartrainproject.R;

import java.util.List;
import java.util.Map;

public class HomePagerExpandAdapter extends BaseExpandableListAdapter {
    private List<String> groupData;
    private Map<String, List<String>> childData;

    public HomePagerExpandAdapter(List<String> groupData, Map<String, List<String>> childData) {
        this.groupData = groupData;
        this.childData = childData;
    }

    public void notifyDataChange(List<String> groupData, Map<String, List<String>> childData) {
        this.groupData = groupData;
        this.childData = childData;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return groupData == null ? 0 : groupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<String> childs = childData.get(groupData.get(groupPosition));
        return childs == null ? 0 : childs.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(groupData.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_pager_group_item, parent, false);
            holder.date = convertView.findViewById(R.id.activity_home_pager_group_item_date);
            holder.indicator = convertView.findViewById(R.id.activity_home_pager_group_item_indicator);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        if (isExpanded) {
            holder.indicator.setImageResource(R.drawable.activity_home_pager_group_item_down);
        } else {
            holder.indicator.setImageResource(R.drawable.activity_home_pager_group_item_right);
        }
        holder.date.setText(groupData.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_pager_child_item, parent, false);
            holder.timeSlot = ((TextView) convertView);
            convertView.setTag(holder);
        } else {
            holder = ((ChildViewHolder) convertView.getTag());
        }
        holder.timeSlot.setText(childData.get(groupData.get(groupPosition)).get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
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
