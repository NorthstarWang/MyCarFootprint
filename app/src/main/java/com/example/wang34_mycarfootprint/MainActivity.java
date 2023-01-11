package com.example.wang34_mycarfootprint;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wang34_mycarfootprint.design.GasStationListAdapter;
import com.example.wang34_mycarfootprint.model.GasStationListItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public final ArrayList<GasStationListItem> gasStationListItems = new ArrayList<>();
    private final GasStationListAdapter gasStationListAdapter = new GasStationListAdapter(MainActivity.this, gasStationListItems);

    //View components
    private ListView listView;
    private TextView footprintTV;
    private TextView costTV;

    //Activity Result Launcher(listen result returned from activity)
    private ActivityResultLauncher<Intent> dataActivityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialized listview data from gasStationListItems
        listView = findViewById(R.id.custom_listview);
        listView.setAdapter(gasStationListAdapter);

        //initialized result launcher on listView item click
        dataActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        if (data.hasExtra("operation")) {
                            switch (data.getStringExtra("operation")) {
                                //operation decide what to do with the corresponding item
                                case "delete":
                                    int posToDelete = data.getIntExtra("index",-1);
                                    if(posToDelete!=-1)
                                        gasStationListItems.remove(posToDelete);

                                    //update mainActivity
                                    gasStationListAdapter.notifyDataSetChanged();
                                    calculateTotal();
                                    break;
                                case "edit":
                                    int posToEdit = data.getIntExtra("index",-1);
                                    //get the edited object
                                    GasStationListItem gasStationListItemEdit = (GasStationListItem)data.getSerializableExtra("object");
                                    if(posToEdit!=-1)
                                        //replace original item with edited version
                                        gasStationListItems.set(posToEdit,gasStationListItemEdit);

                                    //update mainActivity
                                    gasStationListAdapter.notifyDataSetChanged();
                                    calculateTotal();

                                    //reopen activity with edited item
                                    Intent intent = new Intent(this, DataActivity.class);
                                    intent.putExtra("item", gasStationListItems.get(posToEdit));
                                    intent.putExtra("index",posToEdit);
                                    dataActivityResultLauncher.launch(intent);
                                    break;
                            }
                        }
                    }
                });
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, DataActivity.class);
            intent.putExtra("item", gasStationListItems.get(i));
            intent.putExtra("index",i);
            dataActivityResultLauncher.launch(intent);
        });

        //initialize textView
        footprintTV = findViewById(R.id.total_footprint);
        costTV = findViewById(R.id.total_fuel_cost);

        //generate total data from empty array to make the initial value 0
        calculateTotal();
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
        //set the empty view for listView
        View empty = findViewById(R.id.empty);
        ListView list = findViewById(R.id.custom_listview);
        list.setEmptyView(empty);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.item_add) {
            showAddDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    private void showAddDialog() {
        //inflate dialog
        final Dialog dialog = new Dialog(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View view = layoutInflater.inflate(R.layout.custom_dialog, null);

        //initialize view
        final EditText nameET = view.findViewById(R.id.dialog_edit_gas_station_name);
        final EditText dateET = view.findViewById(R.id.dialog_edit_date);
        final EditText amountET = view.findViewById(R.id.dialog_edit_litre_amount);
        final EditText priceET = view.findViewById(R.id.dialog_edit_price_per_litre);
        final AutoCompleteTextView fuelTypeTV = view.findViewById(R.id.dialog_edit_fuel_type_textview);
        final Button btnAdd = view.findViewById(R.id.button_apply_change);
        btnAdd.setText("Add visit");

        //date textview click: date picker popup
        final Calendar myCalendar= Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, null, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dateET.setOnClickListener(view12 -> {
            datePickerDialog.getDatePicker().init(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH), (datePicker, i, i1, i2) -> {
                        myCalendar.set(i, i1, i2);
                        //update label
                        String myFormat="yyyy-MM-dd";
                        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                        dateET.setText(dateFormat.format(myCalendar.getTime()));
                        datePickerDialog.dismiss();
                    });
            datePickerDialog.show();
        });

        //set fuelType dropdown selector
        ArrayAdapter<String> fuelTypeAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, GasStationListItem.getItems());
        fuelTypeTV.setAdapter(fuelTypeAdapter);

        //Add visit button click: add visit to arrayList
        btnAdd.setOnClickListener(view1 -> {
            //validate input
            if(nameET.getText().toString().length()>30||nameET.getText().toString().isEmpty()){
                //alert if either length exceed 30 characters or no input.
                Toast.makeText(MainActivity.this,"Invalid gas station name input",Toast.LENGTH_SHORT).show();
            }else if(dateET.getText().toString().isEmpty()){
                //no date selected
                Toast.makeText(MainActivity.this,"Please select a date",Toast.LENGTH_SHORT).show();
            }else if(fuelTypeTV.getText().toString().isEmpty()){
                //no fuel type selected
                Toast.makeText(MainActivity.this,"Please select a fuel type",Toast.LENGTH_SHORT).show();
            }else if(amountET.getText().toString().isEmpty()){
                //no amount input
                Toast.makeText(MainActivity.this,"Invalid amount input",Toast.LENGTH_SHORT).show();
            }else if(priceET.getText().toString().isEmpty()){
                //no price input
                Toast.makeText(MainActivity.this,"Invalid price input",Toast.LENGTH_SHORT).show();
            }else{
                //if all pass, add visit and update view
                GasStationListItem.FuelType tempFuelType = fuelTypeTV.getText().toString().equals("Gasoline")?GasStationListItem.FuelType.Gasoline: GasStationListItem.FuelType.Diesel;
                SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd",Locale.CANADA);
                try {
                    gasStationListItems.add(new GasStationListItem(nameET.getText().toString(),dateParser.parse(dateET.getText().toString()), tempFuelType, Integer.parseInt(amountET.getText().toString()), Math.round(Double.parseDouble(priceET.getText().toString()) * 100.0) / 100.0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                gasStationListAdapter.notifyDataSetChanged();
                calculateTotal();
                dialog.dismiss();
            }
        });

        //show dialog
        dialog.setCancelable(true);
        dialog.setTitle("Add gas station visit");
        dialog.setContentView(view);
        dialog.show();
    }

    private void calculateTotal(){
        // calculate the total footprint and cost from gasStationListItems
        double tempTotalFootPrint = 0;
        double tempTotalCost = 0;
        for (GasStationListItem gasStationListItem:gasStationListItems
             ) {
            tempTotalFootPrint+=gasStationListItem.getFootprint();
            tempTotalCost+=gasStationListItem.getCalculatedPrice();
        }

        //update total data to textView
        footprintTV.setText(String.format(Locale.CANADA,"%d",Math.round(tempTotalFootPrint)));
        costTV.setText(String.format(Locale.CANADA,"%.2f",tempTotalCost));
    }
}