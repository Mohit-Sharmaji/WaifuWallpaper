package com.sharmaji.wallpaperproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.sharmaji.wallpaperproject.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> headerTitles;
    HashMap<String,List<String>> childTitles;

    public ExpandableListAdapter(Context context, List<String> headerTitles, HashMap<String, List<String>> childTitles) {
        this.context = context;
        this.headerTitles = headerTitles;
        this.childTitles = childTitles;
    }
// i == Group Position && i1 = Child Position

    @Override
    public int getGroupCount() {
        return headerTitles.size();
    }
    @Override
    public int getChildrenCount(int i) {
        return this.childTitles.get(this.headerTitles.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.headerTitles.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childTitles.get(this.headerTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle= (String) headerTitles.get(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_group_style, null);
        }
        TextView textView = view.findViewById(R.id.expandable_header);
        textView.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String) getChild(i,i1);
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_child_style,null);
        }
        TextView textView = view.findViewById(R.id.expandable_child);
        textView.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int childPosition) {
        return true;
    }
}
