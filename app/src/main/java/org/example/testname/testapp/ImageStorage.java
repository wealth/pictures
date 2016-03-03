package org.example.testname.testapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Dima on 3/1/2016.
 */
public class ImageStorage {

    public static String saveToSdCard(Bitmap bitmap, String filename) {

        String stored = null;

        File sdcard = Environment.getExternalStorageDirectory() ;

        File folder = new File(sdcard.getAbsoluteFile(), ".testapp");//the dot makes this directory hidden to the user
        File tmpFolder = new File(sdcard.getAbsoluteFile(), ".testapp/tmp");
        folder.mkdirs();
        tmpFolder.mkdir();

        Log.e("ImageStorage", "Trying to store...");

        File file = new File(folder.getAbsoluteFile(), filename + ".jpg") ;
        if (file.exists())
            return stored ;

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            stored = "success";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stored;
    }

    public static void ClearTemporaryFolder() {
        File sdcard = Environment.getExternalStorageDirectory() ;
        File tmpFolder = new File(sdcard.getAbsoluteFile(), ".testapp/tmp");
        final File[] files = tmpFolder.listFiles();
        if (files != null)
            for (File f: files) f.delete();
    }

    public static void SaveFromCache() {
        File sdcard = Environment.getExternalStorageDirectory() ;
        File tmpFolder = new File(sdcard.getAbsoluteFile(), ".testapp/tmp");
        final File[] files = tmpFolder.listFiles();
        for (File f: files) {
            String name = f.getName();
            f.renameTo(new File(f.getParentFile().getParentFile(), name));
        }
    }

    public static File getImageWithCache(String filename) {
        File image = getImage(filename);
        if (image.exists())
            return image;

        File tmpImage = getImage("tmp/" + filename);
        return tmpImage;
    }

    public static File getImage(String imagename) {

        File mediaImage = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            if (!myDir.exists())
                return null;

            mediaImage = new File(myDir.getPath() + "/.testapp/"+imagename+".jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaImage;
    }

    public static boolean checkifImageExists(String imagename)
    {
        Bitmap b = null ;
        File file = ImageStorage.getImageWithCache("/"+imagename);
        String path = file.getAbsolutePath();

        if (path != null)
            b = BitmapFactory.decodeFile(path);

        if(b == null ||  b.equals(""))
        {
            return false ;
        }
        return true ;
    }

    public static void Clear() {
        File sdcard = Environment.getExternalStorageDirectory() ;
        File folder = new File(sdcard.getAbsoluteFile(), ".testapp");
        final File[] files = folder.listFiles();
        if (files != null)
            for (File f: files)
                if (f.isFile())
                    f.delete();

        ImageStorage.ClearTemporaryFolder();
    }
}
