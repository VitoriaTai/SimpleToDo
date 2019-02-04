package com.example.vitoriatai.simpletodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;  //model
    ArrayAdapter<String> itemsAdapter;
    ListView ListItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems(); // replace this items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items); //'this' references to the MainActivity
        ListItem = (ListView) findViewById(R.id.ListItem); //cast output id level to the ListView
        ListItem.setAdapter(itemsAdapter);

        //mock data
        //items.add("First Item");
        //items.add("Second Item");

        setupListViewListener();

    }

    public void onAddItem(View v){

        EditText TextSquare = (EditText) findViewById(R.id.TextSquare);
        String itemText = TextSquare.getText().toString();
        itemsAdapter.add(itemText);
        TextSquare.setText("");
        writeItems(); //add writing
        Toast.makeText(getApplicationContext(), "Item Added to the List", Toast.LENGTH_SHORT).show(); //used to indicate that the app has complete it sucessfully

    }

    private void setupListViewListener(){
        Log.i("MainActivity", "Setting up listener on List View"); //i stands for level

        ListItem .setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActicity", "Item remove from list" + position); //only be called when the long clicekd is intercepted and it shows on Logcat(debuging purposes) does not affect the program
                items.remove(position);
                itemsAdapter.notifyDataSetChanged(); //has to be called to be used
                writeItems(); //remove writing

                return true;
            }
        });
    }

    //this part is to keep the app persistent from the start 
    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset())); //read lines one at a time and returns a list of strings, passes data file
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e); //e stands for exception
            items = new ArrayList<>();

        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e); //file system persistence from readItems

        }
    }
}
