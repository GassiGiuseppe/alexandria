package com.swgroup.alexandria.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.swgroup.alexandria.ui.comic.ComicParser;

public class ComicViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ComicParser comic;

    public ComicViewPagerAdapter(Context context, ComicParser comicParser) {
        this.context = context;
        this.comic = comicParser;
    }

    @Override
    public int getCount() {
        return comic.numPages();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);

        Glide.with(context)
                .load(comic.getPagesUri().get(position))
                .fitCenter()
                .into(imageView);

        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
