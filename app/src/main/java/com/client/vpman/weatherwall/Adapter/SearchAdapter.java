package com.client.vpman.weatherwall.Adapter;

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
import android.view.animation.AnimationUtils;

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
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.Model.SearchModel;
import com.client.vpman.weatherwall.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder>
{

    private Context context;
    private List<SearchModel> list;

    public SearchAdapter(Context context, List<SearchModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.search_adapter,parent,false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchHolder holder, int position) {
        LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
            @Override
            protected int sizeOf(String key, Bitmap image) {
                return image.getByteCount() / 1024;
            }
        };
        SharedPref1 pref1 = new SharedPref1(context);
        SearchModel modelData1 = list.get(position);
        Bitmap image = memCache.get("imagefile");
        if (image != null) {
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
            if (pref1.getImageLoadQuality().equals("Default")) {
                Glide.with(context)
                        .load(modelData1.getLarge2x())
                        .thumbnail(
                                Glide.with(context).load(modelData1.getLarge())
                        )
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(holder.imageView);
            } else if (pref1.getImageLoadQuality().equals("High Quality")) {
                Glide.with(context)
                        .load(modelData1.getOriginal())
                        .thumbnail(
                                Glide.with(context).load(modelData1.getLarge2x())
                        )
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })

                        .into(holder.imageView);
            } else {
                Glide.with(context)
                        .load(modelData1.getLarge2x())
                        .thumbnail(
                                Glide.with(context).load(modelData1.getLarge())
                        )
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                return false;
                            }
                        })
                        .into(holder.imageView);
            }
            holder.imageView.requestLayout();
            holder.imageView.setOnClickListener(view -> {
                Intent intent = new Intent(context, TestFullActivity.class);
                intent.putExtra("large", modelData1.getOriginal());
                intent.putExtra("img", modelData1.getLarge2x());
                intent.putExtra("imgSmall", modelData1.getLarge());
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
        return list.size();
    }

    public static class SearchHolder extends RecyclerView.ViewHolder
    {
        private ShapeableImageView imageView;
        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.searchImageList);
        }
    }
}