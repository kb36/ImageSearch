package com.github.kb36.imagesearch;

import android.content.Context;
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
        super(context, resource, data);
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
        if(convertView == null) {
            convertView  = mInflater.inflate(mResource, parent, false);
        }

        QueryResult.Result res = getItem(position);
        Log.d(TAG, "Getting the view at position:" + position);
        Picasso.with(mContext)
                .load(res.tbUrl)
                .resize(450, 450)
                .centerCrop()
                .into((ImageView) convertView);

        return convertView;
    }
}
