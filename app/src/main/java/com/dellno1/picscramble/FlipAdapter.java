package com.dellno1.picscramble;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlipAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> image = new ArrayList<>();
    private int imagePosition;
    private GameHandler.OnClicked onClicked;

    public FlipAdapter(List<String> image, Context context, int imagePosition, GameHandler.OnClicked onClicked) {
        this.context = context;
        this.image = image;
        this.imagePosition = imagePosition;
        this.onClicked = onClicked;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderMain viewHolderMain;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_view_layout, parent, false);
            viewHolderMain = new ViewHolderMain(convertView);
            convertView.setTag(viewHolderMain);
        } else {
            viewHolderMain = (ViewHolderMain) convertView.getTag();
        }

        String url = image.get(position);
        if (url != null) {
            viewHolderMain.image.setVisibility(View.VISIBLE);
            viewHolderMain.count.setVisibility(View.GONE);
            if (!viewHolderMain.imageSet) {
                displayFile(viewHolderMain.image, url);
                viewHolderMain.imageSet = true;
            }
        } else {
            viewHolderMain.image.setVisibility(View.GONE);
            viewHolderMain.count.setVisibility(View.VISIBLE);
            viewHolderMain.count.setText(String.valueOf(imagePosition + 1));
        }
        viewHolderMain.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicked.onClick();
            }
        });
        return convertView;
    }

    static class ViewHolderMain extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.count)
        TextView count;
        public boolean imageSet = false;

        public ViewHolderMain(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void displayFile(ImageView imageView, String url) {
        Picasso.with(context)
                .load(url)
                .resize((int) context.getResources().getDimension(R.dimen._300sdp),
                        (int) context.getResources().getDimension(R.dimen._300sdp))
                .centerCrop()
                .into(imageView);
    }
}