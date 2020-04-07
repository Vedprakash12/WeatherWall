package com.client.vpman.weatherwall.Fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.util.LruCache;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.Activity.ExploreQuotesAndPhoto;
import com.client.vpman.weatherwall.CustomeDesignViewPager.VerticalViewPageAdapter;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.google.android.material.textview.MaterialTextView;
import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;
import com.kc.unsplash.models.SearchResults;


import java.util.List;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class Image extends Fragment {

    View view;

    public Image() {
        // Required empty public constructor
    }


    ImageView imageView;
    String query;

    RelativeLayout relativeLayout;
    SharedPref1 sharedPref1;

    private final String CLIENT_ID = "fcd5073926c7fdd11b9eb62887dbd6398eafbb8f3c56073035b141ad57d1ab5f";
    private Unsplash unsplash;
    MaterialTextView desc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_image, container, false);

        imageView = view.findViewById(R.id.NatureUn);
        unsplash = new Unsplash(CLIENT_ID);

        desc=view.findViewById(R.id.desc);

        relativeLayout=view.findViewById(R.id.relative);

       /* if (getActivity()!=null)
        {
            sharedPref1=new SharedPref1(getActivity());
            if (sharedPref1.getTheme().equals("Light"))
            {
                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.design_assets_black);
                relativeLayout.setBackground(drawable);
                desc.setTextColor(Color.parseColor("#FFFFFF"));

            }
            else if (sharedPref1.getTheme().equals("Dark"))
            {
                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.design_assets);
                relativeLayout.setBackground(drawable);
                desc.setTextColor(Color.parseColor("#3C3C3C"));

            }
            else
            {
                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.design_assets_black);
                relativeLayout.setBackground(drawable);
                desc.setTextColor(Color.parseColor("#FFFFFF"));

            }
        }*/

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(System.currentTimeMillis())).encodeQuality(70);
        requestOptions.priority(Priority.IMMEDIATE);
        requestOptions.skipMemoryCache(false);
        requestOptions.onlyRetrieveFromCache(true);
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.priority(Priority.HIGH);
        requestOptions.isMemoryCacheable();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);

        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.centerCrop();



        query = "Nature";


        unsplash.searchPhotos(query, new Unsplash.OnSearchCompleteListener() {
            @Override
            public void onComplete(SearchResults results) {
                Log.d("Photos", "Total Results Found " + results.getTotal());

                List<Photo> photos = results.getResults();


                Random random = new Random();
                int n = random.nextInt(photos.size());

                if (isAdded()) {

                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount() / 1024;
                        }
                    };
                    if (getActivity() != null) {
                        Display display = getActivity().getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x; //width of screen in pixels
                        int height = size.y;
                        Bitmap image = memCache.get("imagefile");
                        if (image != null) {
                            //Bitmap exists in cache.
                            imageView.setImageBitmap(image);
                        } else {
                            Glide.with(getContext())
                                    .load(photos.get(n).getUrls().getFull())
                                    .thumbnail(
                                            Glide.with(getActivity()).load(photos.get(n).getUrls().getRegular())
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

                                    .into(imageView);
                        }

                        imageView.setOnClickListener(v -> {
                            Intent intent = new Intent(getActivity(), ExploreQuotesAndPhoto.class);
                            intent.putExtra("imgData", photos.get(n).getUrls().getFull());
                            intent.putExtra("imgDataSmall", photos.get(n).getUrls().getRegular());
                            intent.putExtra("query", query);
                            intent.putExtra("text", "Nature");

                            Pair[] pairs = new Pair[1];
                            pairs[0] = new Pair<View, String>(imageView, "imgData");


                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    getActivity(), pairs
                            );

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                startActivity(intent, optionsCompat.toBundle());
                            } else {
                                startActivity(intent);
                            }
                        });

                    }

                }


            }

            @Override
            public void onError(String error) {
                Log.d("Unsplash", error);
            }
        });


        imageView.setTranslationZ(40);


        return view;
    }


    public static Image newInstance(String text) {
        Image f = new Image();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }


}
