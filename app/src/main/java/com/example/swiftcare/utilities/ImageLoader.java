package com.example.swiftcare.utilities;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.swiftcare.R;

public class ImageLoader {

    public static void loadImage(String imageUrl, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .into(imageView);
    }

    public static void loadCircleImage(String imageUrl, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_profile_clean)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }
}
