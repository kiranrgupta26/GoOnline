package com.example.kiran.vs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Kiran on 1/21/2018.
 */

public class ImageDownloader extends AsyncTask<String,Void,Bitmap> {
    private final WeakReference<ImageView> imageViewWeakReference;

    public ImageDownloader(ImageView imageView)
    {
        imageViewWeakReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        HttpURLConnection httpURLConnection=null;
        try{
            URL url = new URL(params[0]);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            return bitmap;
        }catch(Exception e)
        {
            httpURLConnection.disconnect();
            Log.w("ImageDownloader","Error Downloading Image "+params[0]);
        }
        finally {
            httpURLConnection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(imageViewWeakReference!=null)
        {
            ImageView imageView = imageViewWeakReference.get();
            if(imageView!=null)
            {
                if(bitmap!=null)
                {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
