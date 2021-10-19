package com.sharmaji.wallpaperproject.BackgroundTasks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

public class GetImages extends AsyncTask<ArrayList<String>,String, List<Drawable>> {

    Context context;

    public GetImages(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected List<Drawable> doInBackground(ArrayList<String>... arrayLists) {
        Log.d("AsyncTask Running","true");
        List<Drawable> drawables = new ArrayList<>();
        for(int i = 0; i<arrayLists[0].size();i++) {
            Glide.with(context).load(arrayLists[0].get(i)).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    drawables.add(resource);
                }
                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }
            });
        }
        return drawables;
    }

    @Override
    protected void onPostExecute(List<Drawable> drawables) {
        super.onPostExecute(drawables);

    }
}
