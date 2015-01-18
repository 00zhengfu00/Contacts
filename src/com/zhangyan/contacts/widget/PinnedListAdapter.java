package com.zhangyan.contacts.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

/**
 * Created by ku on 2015/1/18.
 */
public class PinnedListAdapter extends BaseAdapter implements PinnedHeaderListView.PinnedHeaderAdapter,
        SectionIndexer, AbsListView.OnScrollListener {
    private Context mContext;
   // private
    public PinnedListAdapter(Context mContext){
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getPinnedHeaderState(int position) {
        return 0;
    }

    @Override
    public void configurePinnedHeader(View header, int position, int alpha) {

    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
