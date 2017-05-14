package com.mindtickle.assignment1.adapter;

/**
 * Created by beyonder on 14/5/17.
 */


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.mindtickle.assignment1.R;
import com.mindtickle.assignment1.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private int itemsCount = 0;
    private int lastAnimatedPosition = -1;

    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;

    private MainActivity activity;

    public FeedAdapter(Context context, MainActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        final FeedItem feedItem = new FeedItem(view);

        return feedItem;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        runEnterAnimation(viewHolder.itemView, position);
        
        FeedItem holder = (FeedItem) viewHolder;
        holder.ivPhoto.setImageBitmap(activity.mSlideShowPhotos.get(position));

    }

    private void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    public void updateItems(int count) {
        itemsCount = count;
        notifyDataSetChanged();
    }


    public static class FeedItem extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;
        

        public FeedItem(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
