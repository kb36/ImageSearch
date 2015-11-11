package com.github.kb36.imagesearch.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.kb36.imagesearch.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Shows detailed view of the image
 * Created by kb36 on 11/10/2015.
 */
public class ImageDetailActivity extends Activity {
    private ImageView mImageView;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.show();
        setContentView(R.layout.image_item_layout);
        mImageView = (ImageView) findViewById(R.id.imageView);
        String url = getIntent().getStringExtra("url");
        Picasso.with(this)
                .load(url)
                .fit()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.img_not_found)
                .tag(this)
                .into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if(mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        if(mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }
}
