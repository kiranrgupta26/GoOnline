package com.example.kiran.vs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kiran on 1/17/2018.
 */

public class GridViewAdapter extends ArrayAdapter<ImageItem> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList<ImageItem>();
    int width;
    int height;


    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<ImageItem> data,Point size) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        width = size.x;
        height = size.y;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.itemprice = (TextView)row.findViewById(R.id.itemprice);
            holder.image.getLayoutParams().width = width/2;
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        ImageItem item = data.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.itemprice.setText("Rs "+item.getPrice());
        if(holder.image!=null)
        {
            //try using GLide
            try{
                Picasso.with(context)
                        .load(item.getImage())
                        .into(holder.image);
            }
            catch(Exception e)
            {
                Log.d("Picasso Exception",e.getMessage());
            }

          //  new ImageDownloader(holder.image).execute(item.getImage());
        }
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
        TextView itemprice;
        TextView itemdiscount;
    }
}
