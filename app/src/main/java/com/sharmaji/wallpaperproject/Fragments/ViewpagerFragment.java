package com.sharmaji.wallpaperproject.Fragments;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sharmaji.wallpaperproject.R;

import java.io.IOException;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ViewpagerFragment extends Fragment {
    Bitmap wallpaper;

    public ViewpagerFragment() {
        // Required empty public constructor
    }
    public static final ViewpagerFragment newInstance(String url) {
        ViewpagerFragment fragment = new ViewpagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("WallpaperURl", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);
        ImageView mainImage = view.findViewById(R.id.AnimePic);
        ImageView blurImage = view.findViewById(R.id.blurPic);
        FloatingActionButton setWallpaperFab = view.findViewById(R.id.setAsWallpaperFAB);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        setWallpaperFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAsWallpaper(wallpaper);
            }
        });
        String url = getArguments().getString("WallpaperURl");
        Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                wallpaper = resource;
                setWallpaperFab.setVisibility(View.VISIBLE);
                Glide.with(getContext()).asBitmap().load(resource).apply(RequestOptions.bitmapTransform(new BlurTransformation(25,2))).into(blurImage);
                mainImage.setImageBitmap(resource);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
        return view;
    }
    private void setAsWallpaper(Bitmap bitmap){
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Set Wallpaper As ?");
        builder.setIcon(R.drawable.ic_baseline_wallpaper_24);
        builder.setNegativeButton("Home Screen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    wallpaperManager.setBitmap(bitmap,null,true,WallpaperManager.FLAG_SYSTEM);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNeutralButton("Lock Screen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    wallpaperManager.setBitmap(bitmap,null,true,WallpaperManager.FLAG_LOCK);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setPositiveButton("Both", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    wallpaperManager.setBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.create().show();
    }
}