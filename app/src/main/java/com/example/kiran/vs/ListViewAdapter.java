package com.example.kiran.vs;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by Kiran on 1/21/2018.
 */

public class ListViewAdapter extends ArrayAdapter<AddItemToCart> {

    customRemoveListener customremovelistener;
    CustomChangeQuantity customChangeQuantity;
    private Context context;
    private int layoutResourceId;
    private ArrayList<AddItemToCart> data;



    public ListViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<AddItemToCart> objects) {
       super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        data = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.tvitemname);
            holder.image = (ImageView) row.findViewById(R.id.cartitems);
            holder.quantity = (Spinner)row.findViewById(R.id.tvquantity);
            Context context1 = getContext();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context1,R.array.quantity,android.R.layout.simple_spinner_dropdown_item);
            holder.quantity.setAdapter(adapter);
            holder.quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                    if(customChangeQuantity!=null)
                    {
                        if(position1!=holder.spinQuantity)
                        {

                            holder.spinQuantity = position1;
                            try{
                                customChangeQuantity.onSpinnerClickListener(position1+1,data.get(position));
                            }catch (Exception e)
                            {
                                Log.w("ChanGeQuantity",e.getMessage());
                            }

                        }

                    }
                    Log.w("Quantity",String.valueOf(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            holder.image.getLayoutParams().height = 300;
            holder.image.getLayoutParams().width = 250;
            holder.button = (Button)row.findViewById(R.id.btnremove);
            holder.price = (TextView)row.findViewById(R.id.tvitemprice);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(customremovelistener!=null)
                    {
                        customremovelistener.onButtonClickListener(position,data.get(position));
                    }
                }
            });

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        AddItemToCart item = data.get(position);
        holder.imageTitle.setText(item.getItemName());
        holder.image.setImageBitmap(item.getImage_path());
        holder.price.setText("Rs "+item.getPrice());
        holder.quantity.setSelection(item.getQuantity()-1);
        holder.spinQuantity = item.getQuantity()-1;
        return row;
    }


    public interface customRemoveListener{
        public void onButtonClickListener(int position,AddItemToCart item);
    }

    public void setButtonListener(customRemoveListener customremovelistener)
    {
        this.customremovelistener = customremovelistener;
    }

    public interface CustomChangeQuantity{
        public void onSpinnerClickListener(int position,AddItemToCart item);
    }

    public void setSpinnerListener(CustomChangeQuantity customChangeQuantity)
    {
        this.customChangeQuantity = customChangeQuantity;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
        Spinner quantity;
        Button button;
        int spinQuantity;
        TextView price;
    }

}
