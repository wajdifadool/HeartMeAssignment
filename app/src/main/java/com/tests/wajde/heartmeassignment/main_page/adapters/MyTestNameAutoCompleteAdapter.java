package com.tests.wajde.heartmeassignment.main_page.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;


public class MyTestNameAutoCompleteAdapter extends ArrayAdapter<String> {

    private ArrayList<String> mKeys; // the test names/Category
    private ArrayList<String> mKeysFilterd = new ArrayList<>();


    public MyTestNameAutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource,
                                         @NonNull ArrayList objects) {
        super(context, resource, objects);
        this.mKeys = objects;

        for (String s : mKeys) {
            mKeysFilterd.add(s);
        }
    }


    @Override
    public int getCount() {
        return mKeysFilterd.size();
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public String getItem(int position) {
        return mKeysFilterd.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            //simple filtering logic
            if (constraint != null) {
                ArrayList<String> tempList = new ArrayList<>();

                mKeysFilterd.clear();
                for (String key : mKeys) {
                    if (key.toLowerCase().contains(((String) constraint).toLowerCase().trim())) {
                        System.out.println("performFiltering() ");
                        tempList.add(key);

                    }
                }
                mKeysFilterd = tempList;
                results.values = mKeysFilterd;
                results.count = mKeysFilterd.size();
                return results;
            }

            for (String s : mKeys) {
                mKeysFilterd.add(s);
            }
            results.values = mKeysFilterd;
            results.count = mKeysFilterd.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mKeysFilterd = (ArrayList<String>) results.values;
            notifyDataSetChanged();
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }


        }
    };


}


