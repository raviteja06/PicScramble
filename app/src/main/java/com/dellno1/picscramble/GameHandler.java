package com.dellno1.picscramble;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameHandler {
    Context context;
    LinearLayout linearLayout;
    List<ResponseObject.ItemObject> itemObjectList;
    ImageAdapter imageAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    MainActivity.OnAnswered onAnswered;

    public void reset() {
        if (imageAdapter != null) {
            imageAdapter.notifyDataSetChanged();
        }
    }

    public GameHandler(Context context, List<ResponseObject.ItemObject> itemObjectList, LinearLayout linearLayout, MainActivity.OnAnswered onAnswered) {
        this.context = context;
        this.linearLayout = linearLayout;
        this.itemObjectList = itemObjectList;
        this.onAnswered = onAnswered;
    }

    public void build() {
        @SuppressLint("InflateParams")
        View rootView = LayoutInflater.from(context).inflate(R.layout.handler_game, null, true);
        ButterKnife.bind(this, rootView);
        if (linearLayout != null) {
            linearLayout.removeAllViews();
            linearLayout.addView(rootView);
        }
        loadData();
    }

    private void loadData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(context, itemObjectList, onAnswered);
        recyclerView.setAdapter(imageAdapter);
    }

    public void question(String url) {
        if (imageAdapter != null) {
            imageAdapter.question(url);
        }
    }

    public interface OnClicked {
        void onClick();
    }
}
