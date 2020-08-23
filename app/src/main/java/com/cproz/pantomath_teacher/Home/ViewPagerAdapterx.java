package com.cproz.pantomath_teacher.Home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.jackandphantom.circularimageview.RoundedImage;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapterx extends PagerAdapter {

    private Context mContext;
    private String[] imageUrls;
    Intent intent;

    public ViewPagerAdapterx(Context mContext, String[] imageUrls) {
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
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final RoundedImage roundedImage = new RoundedImage(mContext);
        roundedImage.setRoundedRadius(20);
        Picasso.get()
                .load(imageUrls[position])
                .fit()
                .centerCrop()
                .into(roundedImage);
        container.addView(roundedImage);

        roundedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0){

                    intent = new Intent(mContext, Image.class);
                    intent.putExtra("Photo1", imageUrls[0]);
                    mContext.startActivity(intent);


                }else if (position == 1){
                    intent = new Intent(mContext, Image.class);
                    intent.putExtra("Photo1", imageUrls[1]);
                    mContext.startActivity(intent);
                }
            }
        });

        return roundedImage;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
