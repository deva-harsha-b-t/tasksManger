package com.dojo.dojotodo;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class linearLayoutMangerWrapper extends LinearLayoutManager {
    public linearLayoutMangerWrapper(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        }
        catch (IndexOutOfBoundsException e){
            Log.d("IOFB", "onLayoutChildren: " + e.getMessage());
        }
    }
}
