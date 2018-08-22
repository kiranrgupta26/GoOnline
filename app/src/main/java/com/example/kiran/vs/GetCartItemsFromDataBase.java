package com.example.kiran.vs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Kiran on 1/22/2018.
 */

public class GetCartItemsFromDataBase{

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "CartItems";
    private static final String TABLE_NAME = "myorder";
    private static final String KEY_ID = "itemid";
    private static final String KEY_NAME = "itemname";
    private static final String KEY_QUANTITY = "itemquantity";
    private static final String KEY_IMAGE="itemimage";
    private static final String KEY_PRICE="itemprice";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_SUBCATEGORY1 = "subcat1";
    private static final String KEY_SUBCATEGORY2 = "subcat2";

    private Context context;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase sqLiteDatabase;
    private int totalCost;


    public  GetCartItemsFromDataBase(Context context)
    {
            this.context = context;
    }

    private class DataBaseHelper extends SQLiteOpenHelper
    {
        public  DataBaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            //onCreate();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_PRICE + " INTEGER," + KEY_IMAGE + " BLOB,"
                    + KEY_QUANTITY + " INTEGER," + KEY_CATEGORY + " TEXT," + KEY_SUBCATEGORY1 + " TEXT," + KEY_SUBCATEGORY2 + " TEXT)";
            try {
                db.execSQL(CREATE_CART_TABLE);
            }catch (Exception e)
            {
                Log.d("SQL TABLE EXC ",e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void openDataBase()
    {
        openHelper = new DataBaseHelper(context);

        try{
            sqLiteDatabase = openHelper.getWritableDatabase();
        }
        catch(Exception e)
        {
           Log.d("Exception",e.getMessage());
        }
    }

    public void closeDataBase()
    {
        sqLiteDatabase.close();
    }

    public void addItemToCart(AddItemToCart addItemToCart)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME,addItemToCart.getItemName());
        contentValues.put(KEY_IMAGE,getBytes(addItemToCart.getImage_path()));
        contentValues.put(KEY_PRICE,addItemToCart.getPrice());
        contentValues.put(KEY_QUANTITY,addItemToCart.getQuantity());
        contentValues.put(KEY_CATEGORY,addItemToCart.getCategory());
        contentValues.put(KEY_SUBCATEGORY1,addItemToCart.getSubcat1());
        contentValues.put(KEY_SUBCATEGORY2,addItemToCart.getSubcat2());
        String te = addItemToCart.getSubcat2();
        try {
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        }catch(SQLException e)
        {
            Log.d("Exception",e.getMessage());
        }

    }

    public void updateItemQuantity(int quantity,AddItemToCart addItemToCart)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME,addItemToCart.getItemName());
        contentValues.put(KEY_IMAGE,getBytes(addItemToCart.getImage_path()));
        contentValues.put(KEY_PRICE,addItemToCart.getPrice());
        contentValues.put(KEY_QUANTITY,quantity);
        contentValues.put(KEY_CATEGORY,addItemToCart.getCategory());
        contentValues.put(KEY_SUBCATEGORY1,addItemToCart.getSubcat1());
        contentValues.put(KEY_SUBCATEGORY2,addItemToCart.getSubcat2());

        try{
            sqLiteDatabase.update(TABLE_NAME,contentValues,KEY_NAME + " = ?",new String[] { String.valueOf(addItemToCart.getItemName()) });
        }catch (Exception e)
        {
            Log.d("SQlException",e.getMessage());
        }
    }

    public ArrayList<AddItemToCart> removeItemFromCart(AddItemToCart item)
    {
        try{
            sqLiteDatabase.delete(TABLE_NAME, KEY_NAME + " = ?",new String[] { String.valueOf(item.getItemName()) });
        }catch (Exception e)
        {
            Log.d("SQLExceptiom",e.getMessage());
        }
        return getDbCartList();
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public ArrayList<AddItemToCart> getDbCartList()
    {
        ArrayList<AddItemToCart> dbcartList = new ArrayList<AddItemToCart>();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        totalCost=0;
        if(cursor.moveToFirst())
        {
            do{
                AddItemToCart item = new AddItemToCart();
                item.setItemName(cursor.getString(1));
                item.setImage_path(getImage(cursor.getBlob(3)));
                item.setPrice(cursor.getInt(2));
                item.setCategory(cursor.getString(5));
                item.setSubcat1(cursor.getString(6));
                item.setSubcat2(cursor.getString(7));
                item.setQuantity(cursor.getInt(4));
                totalCost = totalCost+cursor.getInt(2);
                dbcartList.add(item);
            }while(cursor.moveToNext());
        }
        return  dbcartList;
    }

    public void removeAllCartItems()
    {
        try {
            sqLiteDatabase.execSQL("Delete from " + TABLE_NAME);
        }catch(Exception e)
        {
         Log.d("Remove Exception",e.getMessage());
        }
    }

    public int getTotalCost()
    {
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        totalCost=0;
        if(cursor.moveToFirst())
        {
            do{
                totalCost = totalCost+(cursor.getInt(2)*cursor.getInt(4));
            }while(cursor.moveToNext());
        }
        return  totalCost;
    }

    public boolean searchItemInCart(String item)
    {
        //String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_NAME+ " =?";
        String whereClause = KEY_NAME +"=?";
        String[] whereArgs = new String[]{item};
        Cursor cursor=null;
        try {
            cursor = sqLiteDatabase.query(TABLE_NAME,null,whereClause,whereArgs,null,null,null);
        }catch (Exception e)
        {
            Log.d("Cursor Excep",e.getMessage());
        }

        if(cursor !=null)
        {
            if(cursor.moveToFirst())
            {
                do{
                   String temp = cursor.getString(1);
                }while(cursor.moveToNext());
                return true;
            }
            return false;
        }
        else {
            return false;
        }
    }

    /*public int getTotalCost1()
    {
        return totalCost;
    }*/

}
