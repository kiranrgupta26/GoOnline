package com.example.kiran.vs;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by Kiran on 2/20/2018.
 */

public class MyOrdersListView extends ArrayAdapter<MyOrderPOJO> {

    CustomCancelOrder customCancelOrder;
    private Context context;
    private int layoutResourceId;
    private ArrayList<MyOrderPOJO> data;

    public MyOrdersListView(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MyOrderPOJO> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        data = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        final MyOrdersListView.ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new MyOrdersListView.ViewHolder();
            holder.orderid = (TextView) row.findViewById(R.id.tvorderid);
            holder.itemdetails = (TextView) row.findViewById(R.id.tvdisplayorderitem);
            holder.cancelorder = (Button) row.findViewById(R.id.btncancelorder);
            holder.cancelorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(customCancelOrder!=null)
                    {
                        MyOrderPOJO myOrderPOJO = data.get(position);
                        customCancelOrder.onCancelOrderListener(myOrderPOJO.getOrderid());
                    }
                    //send data to server to cancel that order
                }
            });
            row.setTag(holder);
        } else {
            holder = (MyOrdersListView.ViewHolder) row.getTag();
        }


        MyOrderPOJO item = data.get(position);
        holder.orderid.setText(item.getOrderid());
        holder.itemdetails.setText(item.getName());

        return row;
    }

    public void setCancelOrderListener(CustomCancelOrder customCancelOrder)
    {
        this.customCancelOrder = customCancelOrder;
    }
    public interface CustomCancelOrder{
        public void onCancelOrderListener(String orderid);
    }
    static class ViewHolder {
        TextView orderid;
        TextView itemdetails;
        Button cancelorder;
    }
}
