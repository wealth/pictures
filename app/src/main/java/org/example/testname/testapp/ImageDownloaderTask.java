package org.example.testname.testapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dima on 3/1/2016.
 */
class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private final String filename;
    public static Integer TasksCount = 0;

    public ImageDownloaderTask(ImageView imageView, String filename) {
        this.imageViewReference = new WeakReference<ImageView>(imageView);
        this.filename = filename;

        ImageDownloaderTask.TasksCount++;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return downloadBitmap(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    Log.e("ImageDownloader", "Downloaded!");
                    imageView.setImageBitmap(bitmap);
                    Log.e("ImageDownloader", "Now storing!");
                    ImageStorage.saveToSdCard(bitmap, "tmp/" + filename);
                } else {
                    Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                    imageView.setImageDrawable(placeholder);
                }
            }
        }

        ImageDownloaderTask.TasksCount--;
        Log.e("ImageDownloader", "Tasks to go: " + String.valueOf(ImageDownloaderTask.TasksCount));
        if (ImageDownloaderTask.TasksCount == 0){
            Log.e("ImageDownloader", "Firing notification..");
            Context ctx = imageViewReference.get().getContext();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(ctx)
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("Тест")
                            .setContentText("Загрузка изображений завершена.");

            Intent resultIntent = new Intent(ctx, MainActivity.class);
            resultIntent.setAction("HELLO");
            resultIntent.putExtra("FromNotification", true);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

            stackBuilder.addParentStack(MainActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
            Log.e("ImageDownloader", "Done.");
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.e("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
