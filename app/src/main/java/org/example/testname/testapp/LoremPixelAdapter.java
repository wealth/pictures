package org.example.testname.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Dima on 3/1/2016.
 */
public class LoremPixelAdapter extends BaseAdapter {
    private Context context;
    private ArrayList listData;
    private LayoutInflater layoutInflater;

    public LoremPixelAdapter(Context context, ArrayList listData) {
        this.context = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_image, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder.imageView != null) {
            String filename = "lorempixel-" + Integer.toString(position);
            Log.e("ImageStorage", "Check if it's stored...");
            if (ImageStorage.checkifImageExists(filename)) {
                Log.e("ImageStorage", "Yep it is.");
                Bitmap image = BitmapFactory.decodeFile(ImageStorage.getImageWithCache(filename).getAbsolutePath());
                holder.imageView.setImageBitmap(image);
            }
            else {
                Log.e("ImageStorage", "Nope, nothing. Let's down the fuck load it!");
                new ImageDownloaderTask(holder.imageView, filename).execute(context.getResources().getString(R.string.url));
            }
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
