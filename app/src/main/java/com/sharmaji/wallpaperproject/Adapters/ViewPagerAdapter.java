package com.sharmaji.wallpaperproject.Adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sharmaji.wallpaperproject.Fragments.ViewpagerFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    ArrayList<String> imageUrls = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<String> imageUrls) {
        super(fragmentManager, lifecycle);
        this.imageUrls = imageUrls;
        Log.d("AdapterImageUrl",imageUrls.toString());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ViewpagerFragment.newInstance(imageUrls.get(position));
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
        //return 2;
    }
}