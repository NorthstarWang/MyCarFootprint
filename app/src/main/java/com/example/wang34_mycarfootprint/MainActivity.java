package com.example.wang34_mycarfootprint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wang34_mycarfootprint.design.GasStationListAdapter;
import com.example.wang34_mycarfootprint.model.GasStationListItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    private final ArrayList<GasStationListItem> gasStationListItems = new ArrayList<GasStationListItem>();
    private double totalFuelCost = 0.0;
    private double totalFootprint = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.custom_listview);

        gasStationListItems.add(new GasStationListItem("Test Gas Station 1", new Date("2023/01/01"), GasStationListItem.FuelType.Gasoline,20,3.3));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 2", new Date("2023/01/02"), GasStationListItem.FuelType.Diesel,30,3.3));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 3", new Date("2023/01/03"), GasStationListItem.FuelType.Gasoline,20,2));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 1", new Date("2023/01/01"), GasStationListItem.FuelType.Gasoline,20,3.3));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 2", new Date("2023/01/02"), GasStationListItem.FuelType.Diesel,30,3.3));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 3", new Date("2023/01/03"), GasStationListItem.FuelType.Gasoline,20,2));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 1", new Date("2023/01/01"), GasStationListItem.FuelType.Gasoline,20,3.3));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 2", new Date("2023/01/02"), GasStationListItem.FuelType.Diesel,30,3.3));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 3", new Date("2023/01/03"), GasStationListItem.FuelType.Gasoline,20,2));
        GasStationListAdapter gasStationListAdapter = new GasStationListAdapter(MainActivity.this,gasStationListItems);
        listView.setAdapter(gasStationListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        View empty = findViewById(R.id.empty);
        ListView list = (ListView) findViewById(R.id.custom_listview);
        list.setEmptyView(empty);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_delete:
                //if no item yet, disable delete button
                if(gasStationListItems.size()==0){
                    Toast.makeText(this, "There is no data to delete yet.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "There is data to delete.", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.item_add:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}