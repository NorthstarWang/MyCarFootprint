package com.example.wang34_mycarfootprint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wang34_mycarfootprint.design.GasStationListAdapter;
import com.example.wang34_mycarfootprint.model.GasStationListItem;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public final ArrayList<GasStationListItem> gasStationListItems = new ArrayList<GasStationListItem>();
    GasStationListAdapter gasStationListAdapter = new GasStationListAdapter(MainActivity.this, gasStationListItems);
    private double totalFuelCost = 0.0;
    private double totalFootprint = 0.0;
    public static final int LAUNCH_ITEM = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.custom_listview);

        gasStationListItems.add(new GasStationListItem("Test Gas Station 1", new Date("2023/01/01"), GasStationListItem.FuelType.Gasoline, 20, 3.3));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 2", new Date("2023/01/02"), GasStationListItem.FuelType.Diesel, 30, 3.3));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 3", new Date("2023/01/03"), GasStationListItem.FuelType.Gasoline, 20, 2));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 1", new Date("2023/01/01"), GasStationListItem.FuelType.Gasoline, 20, 3.3));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 2", new Date("2023/01/02"), GasStationListItem.FuelType.Diesel, 30, 3.3));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 3", new Date("2023/01/03"), GasStationListItem.FuelType.Gasoline, 20, 2));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 1", new Date("2023/01/01"), GasStationListItem.FuelType.Gasoline, 20, 3.3));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 2", new Date("2023/01/02"), GasStationListItem.FuelType.Diesel, 30, 3.3));
        gasStationListItems.add(new GasStationListItem("Test Gas Station 3", new Date("2023/01/03"), GasStationListItem.FuelType.Gasoline, 20, 2));

        listView.setAdapter(gasStationListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DataActivity.class);
                intent.putExtra("item", gasStationListItems.get(i));
                intent.putExtra("index",i);
                startActivityForResult(intent, LAUNCH_ITEM);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
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
                if (gasStationListItems.size() == 0) {
                    Toast.makeText(this, "There is no data to delete yet.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "There is data to delete.", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.item_add:
                showAddDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ITEM) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("operation")) {
                    switch (data.getStringExtra("operation")) {
                        case "delete":
                            int posToDelete = data.getIntExtra("index",-1);
                            if(posToDelete!=-1)
                                gasStationListItems.remove(posToDelete);
                            gasStationListAdapter.notifyDataSetChanged();
                            break;
                        default:
                            break;
                    }
                }
            }
        }

    }

    void showAddDialog() {

        //inflate dialog
        final Dialog dialog = new Dialog(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_dialog, null);

        //initialize view

        final EditText nameET = view.findViewById(R.id.dialog_edit_gas_station_name);
        final EditText dateET = view.findViewById(R.id.dialog_edit_date);
        final EditText amountET = view.findViewById(R.id.dialog_edit_litre_amount);
        final EditText priceET = view.findViewById(R.id.dialog_edit_price_per_litre);
        final Button btnAdd = view.findViewById(R.id.button_apply_change);

        btnAdd.setText("Add visit");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //show dialog
        dialog.setCancelable(true);
        dialog.setTitle("Add gas station visit");
        dialog.setContentView(view);
        dialog.show();
    }
}