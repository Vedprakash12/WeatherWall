package com.client.vpman.weatherwall.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.Activity.TestFullActivity;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.Model.InstagramModel;
import com.client.vpman.weatherwall.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

public class LatestAdapter extends RecyclerView.Adapter<LatestAdapter.PicsumHolder>
{
    Context context;
    List<InstagramModel> lists;

    public LatestAdapter(Context context, List<InstagramModel> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public PicsumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.latest_adapter, parent, false);
        return new PicsumHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull LatestAdapter.PicsumHolder holder, int position) {

        InstagramModel modelData1 = lists.get(position);
        LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
            @Override
            protected int sizeOf(String key, Bitmap image) {
                return image.getByteCount() / 1024;
            }
        };

        Bitmap image = memCache.get("imagefile");
        if (image != null) {
            //Bitmap exists in cache.
            holder.imageView.setImageBitmap(image);
        } else {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .signature(new ObjectKey(System.currentTimeMillis())).encodeQuality(70);
            requestOptions.priority(Priority.IMMEDIATE);
            requestOptions.skipMemoryCache(false);
            requestOptions.onlyRetrieveFromCache(true);
            requestOptions.priority(Priority.HIGH);

            requestOptions.placeholder(Utils.getRandomDrawbleColor());
            requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);

            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
            requestOptions.centerCrop();




            Glide.with(context)
                    .load(modelData1.getDisplayUrl())
                    .thumbnail(
                            Glide.with(context).load(modelData1.getThumbnail_src())
                    )
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //  spinKitView.setVisibility(View.GONE);


                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            return false;
                        }
                    })

                    .into(holder.imageView);

            holder.imageView.setOnClickListener(v -> {
                Intent intent = new Intent(context, TestFullActivity.class);
                intent.putExtra("large", modelData1.getDisplayUrl());
                intent.putExtra("img", modelData1.getThumbnail_src());

                intent.putExtra("PhotoUrl", modelData1.getPhotoUrl());

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(holder.imageView, "imageData");


                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) context, pairs
                );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    context.startActivity(intent, optionsCompat.toBundle());
                } else {
                    context.startActivity(intent);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class PicsumHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageView;
        public PicsumHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.latestMain);
        }
    }
}
