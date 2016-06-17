package com.dellno1.picscramble;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.emilsjolander.flipview.FlipView;

public class ImageAdapter extends RecyclerView.Adapter {
    private List<ResponseObject.ItemObject> itemObjectList;
    private Context context;
    private String question;
    MainActivity.OnAnswered onAnswered;

    public ImageAdapter(Context context, List<ResponseObject.ItemObject> itemObjectList, MainActivity.OnAnswered onAnswered) {
        this.context = context;
        this.itemObjectList = itemObjectList;
        this.onAnswered = onAnswered;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void question(String url) {
        question = url;
    }

    public static class ViewHolderMain extends RecyclerView.ViewHolder {
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;

        @BindView(R.id.flipLayout)
        FlipView flipLayout;

        public ViewHolderMain(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderMain(LayoutInflater.from(context).inflate(R.layout.game_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final ResponseObject.ItemObject itemObject = itemObjectList.get(holder.getAdapterPosition());
        final ViewHolderMain viewHolderMain = (ViewHolderMain) holder;
        List<String> stringList = new ArrayList<>();
        stringList.add(null);
        stringList.add(itemObject.getMedia().getM());
        FlipAdapter adapter = new FlipAdapter(stringList, context, holder.getAdapterPosition(),
                new GameHandler.OnClicked() {
                    @Override
                    public void onClick() {
                        if (MainActivity.isGameStarted) {
                            viewHolderMain.flipLayout.smoothFlipTo(1);
                            if (itemObject.getMedia().getM().equals(question)) {
                                onAnswered.answer();
                            }
                        }
                    }
                });
        viewHolderMain.flipLayout.setAdapter(adapter);
        if (itemObject.isFlipped()) {
            viewHolderMain.flipLayout.smoothFlipTo(1);
        } else {
            viewHolderMain.flipLayout.smoothFlipTo(0);
        }
    }

    @Override
    public int getItemCount() {
        return itemObjectList.size();
    }
}
