package org.example.testname.testapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ListView list;
    private ArrayList listData;

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LayoutInflater inflaterNumberPicker = this.getLayoutInflater();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.dialog_download_message);

                View numberPickerFragment = inflaterNumberPicker.inflate(R.layout.number_scroll, null);
                builder.setView(numberPickerFragment);

                final NumberPicker np = (NumberPicker) numberPickerFragment.findViewById(R.id.np);
                np.setMinValue(1);
                np.setMaxValue(20);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int cnt = np.getValue();
                        int currentSize = listData.size();

                        Log.e("DATA", "Adding to size: " + String.valueOf(currentSize));
                        for (int i = 0; i < cnt; i++)
                            listData.add("lorempixel-" + Integer.toString(currentSize + i));
                        Log.e("DATA", "Added. Now size is: " + String.valueOf(listData.size()));
                        BaseAdapter adapter = (BaseAdapter) list.getAdapter();
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // get saved images
        Integer i = 0;
        listData = new ArrayList();
        while(ImageStorage.checkifImageExists("lorempixel-" + i.toString())) {
            listData.add("lorempixel-" + i.toString());
            i++;
        }

        list = (ListView) findViewById(R.id.list_view);
        list.setAdapter(new LoremPixelAdapter(this, listData));

        String action = getIntent().getAction();
        if (action == "HELLO") {
            int rndPos = ((int) Math.round(Math.random() * listData.size()));
            Log.e("SCROLL", "Scrolling to: " + String.valueOf(rndPos));
            list.setSelection(rndPos);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            ImageStorage.SaveFromCache();
            Snackbar.make(findViewById(R.id.fab), "Изображения сохранены", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else if (id == R.id.action_clear) {
            ImageStorage.Clear();
            Snackbar.make(findViewById(R.id.fab), "Изображения удалены", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            BaseAdapter adapter = (BaseAdapter) list.getAdapter();

            listData.clear();
            adapter.notifyDataSetChanged();
        }
        else
            finish();

        return super.onOptionsItemSelected(item);
    }
}
