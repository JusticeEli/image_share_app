package com.justice.imageshareapp;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Picture> mExampleList;

    public MainAdapter(Context context, ArrayList<Picture> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Picture picture = mExampleList.get(position);
        String imageUrl = picture.getImageUrl();
        int likeCount = picture.getLikeCount();
        holder.mTextViewLikes.setText("" + likeCount);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_place_holder);
        Glide.with(mContext).applyDefaultRequestOptions(requestOptions).load(imageUrl).into(holder.pictureImageView);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pictureImageView;
        private TextView mTextViewLikes;
        private ImageView shareImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            pictureImageView = itemView.findViewById(R.id.pictureImageView);
            mTextViewLikes = itemView.findViewById(R.id.likeTxtView);
            shareImageView = itemView.findViewById(R.id.shareImageView);
            setOnClickListeners();
        }

        private void setOnClickListeners() {
            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new BackgroundAsync(pictureImageView).execute();
                }
            });


        }
    }



    private void sharePicture(File file) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.fromFile(file);
        sharingIntent.setType("image/png");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share image using"));
    }

    class BackgroundAsync extends AsyncTask<Void, Void, File> {
        BitmapDrawable drawable;
        ImageView mImageView;

        public BackgroundAsync(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            drawable = (BitmapDrawable) mImageView.getDrawable();

        }

        @Override
        protected File doInBackground(Void... voids) {
            Bitmap bitmap = drawable.getBitmap();
            File sdCardDirectory = Environment.getExternalStorageDirectory();
            File image = new File(sdCardDirectory, "download.png");

            boolean success = false;

// Encode the file as a PNG image.
            FileOutputStream outStream;
            try {

                outStream = new FileOutputStream(image);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                /* 100 to keep full quality of the image */

                outStream.flush();
                outStream.close();
                success = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (success) {
                //Display Downloaded
            } else {
                // display Error in Downloading
            }


            return image;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            sharePicture(file);
        }
    }
}