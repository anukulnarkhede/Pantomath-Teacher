package com.cproz.pantomath_teacher.Home;



import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.jackandphantom.circularimageview.RoundedImage;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private String[] imageUrls;

    public ViewPagerAdapter(Context mContext, String[] imageUrls) {
        this.mContext = mContext;
        this.imageUrls = imageUrls;

    }


    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        RoundedImage roundedImage = new RoundedImage(mContext);
        roundedImage.setRoundedRadius(20);
        Picasso.get()
                .load(imageUrls[position])
                .fit()
                .centerCrop()
                .into(roundedImage);
        container.addView(roundedImage);

        return roundedImage;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
