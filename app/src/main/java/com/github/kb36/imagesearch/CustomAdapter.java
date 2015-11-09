package com.github.kb36.imagesearch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nagarjuna.t1 on 10/23/2015.
 */
public class CustomAdapter extends ArrayAdapter<QueryResult.Result> {
    private final static String TAG = "CustomAdapter";
    private Context mContext;
    private int mResource;
    private LayoutInflater mInflater;
    /**
     * constructor for custom adapter
     * @param context
     * @param resource
     */
    public CustomAdapter(Context context, int resource, List<QueryResult.Result> data) {
        super(context, 0, data);
        mContext = context;
        mResource = resource;
        mInflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }



    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SquaredImageView view = (SquaredImageView)convertView;
        if(view == null) {
            view = new SquaredImageView(mContext);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //convertView  = mInflater.inflate(mResource, parent, false);
        }

        QueryResult.Result res = getItem(position);
        //Log.d(TAG, "Getting the view at position:" + position);
        Picasso.with(mContext)
                .load(res.tbUrl)
                .fit()
                .placeholder(R.drawable.placeholder)
                .tag(mContext)
                .into(view);

        return view;
    }

    private final class SquaredImageView extends ImageView {
        public SquaredImageView(Context context) {
            super(context);
        }

        public SquaredImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        }
    }
}
