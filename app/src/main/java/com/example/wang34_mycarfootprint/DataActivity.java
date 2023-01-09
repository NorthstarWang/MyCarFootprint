package com.example.wang34_mycarfootprint;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wang34_mycarfootprint.model.GasStationListItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DataActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        //get intent data
        GasStationListItem gasStationListItem = (GasStationListItem) getIntent().getSerializableExtra("item");
        int pos = getIntent().getIntExtra("index",0);

        //component
        Button buttonEdit = findViewById(R.id.button_edit);
        Button buttonDelete = findViewById(R.id.button_delete);
        final TextView nameTV = findViewById(R.id.textView_gas_station_name);
        final TextView dateTV = findViewById(R.id.textView_date);
        final TextView amountTV = findViewById(R.id.textView_litre_amount);
        final TextView priceTV = findViewById(R.id.textView_price_per_litre);
        final TextView fuelTypeTV = findViewById(R.id.textView_fuel_type);
        final TextView totalPriceTV = findViewById(R.id.textView_total_price);
        final TextView totalFootprintTV = findViewById(R.id.textView_total_footprint);

        //append value
        nameTV.setText(gasStationListItem.getGasStationName());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CANADA);
        fuelTypeTV.setText(gasStationListItem.getFuelType());
        dateTV.setText(dateFormat.format(gasStationListItem.getGasStationVisitDate()));
        amountTV.setText(gasStationListItem.getLitreAmount()+" L");
        priceTV.setText(String.format(Locale.CANADA,"%.2f CAD/L", gasStationListItem.getPricePerLitre()));
        totalPriceTV.setText(String.format(Locale.CANADA,"%.2f CAD ",gasStationListItem.getCalculatedPrice()));
        totalFootprintTV.setText(String.format(Locale.CANADA,"%d kg CO2",Math.round(gasStationListItem.getFootprint())));

        //edit dialog button listener
        buttonEdit.setOnClickListener(view -> showEditDialog(gasStationListItem,pos));

        //delete dialog button listener
        buttonDelete.setOnClickListener(view -> showDeleteDialog(pos));
    }

    void showEditDialog(GasStationListItem gasStationListItem, int pos) {
        String[] items = {"Gasoline","Diesel"};

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
        final Button btnApplyChange = view.findViewById(R.id.button_apply_change);

        //fill in value
        nameET.setText(gasStationListItem.getGasStationName());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CANADA);
        dateET.setText(dateFormat.format(gasStationListItem.getGasStationVisitDate()));
        amountET.setText(String.valueOf(gasStationListItem.getLitreAmount()));
        priceET.setText(String.format(Locale.CANADA,"%.2f", gasStationListItem.getPricePerLitre()));

        //date textview click: date picker popup
        final Calendar myCalendar= Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(DataActivity.this, null, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dateET.setOnClickListener(view12 -> {
            datePickerDialog.getDatePicker().init(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH), (datePicker, i, i1, i2) -> {
                        myCalendar.set(i, i1, i2);
                        //update label
                        String myFormat="yyyy-MM-dd";
                        SimpleDateFormat dateFormat1 =new SimpleDateFormat(myFormat, Locale.US);
                        dateET.setText(dateFormat1.format(myCalendar.getTime()));
                        datePickerDialog.dismiss();
                    });
            datePickerDialog.show();
        });

        //set fuelType dropdown selector
        ArrayAdapter<String> fuelTypeAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, items);
        fuelTypeTV.setAdapter(fuelTypeAdapter);
        fuelTypeTV.setText(gasStationListItem.getFuelType(),false);

        btnApplyChange.setOnClickListener(view1 -> {
            //validate input
            if(nameET.getText().toString().length()>30||nameET.getText().toString().isEmpty()){
                //alert if either length exceed 30 characters or no input.
                Toast.makeText(DataActivity.this,"Invalid gas station name input",Toast.LENGTH_SHORT).show();
            }else if(dateET.getText().toString().isEmpty()){
                //no date selected
                Toast.makeText(DataActivity.this,"Please select a date",Toast.LENGTH_SHORT).show();
            }else if(fuelTypeTV.getText().toString().isEmpty()){
                //no fuel type selected
                Toast.makeText(DataActivity.this,"Please select a fuel type",Toast.LENGTH_SHORT).show();
            }else if(amountET.getText().toString().isEmpty()){
                //no amount input
                Toast.makeText(DataActivity.this,"Invalid amount input",Toast.LENGTH_SHORT).show();
            }else if(priceET.getText().toString().isEmpty()){
                //no price input
                Toast.makeText(DataActivity.this,"Invalid price input",Toast.LENGTH_SHORT).show();
            }else{
                //if all pass, sent back the editted visit and delete the old one, and reopen the activity
                GasStationListItem.FuelType tempFuelType = fuelTypeTV.getText().toString().equals("Gasoline")?GasStationListItem.FuelType.Gasoline: GasStationListItem.FuelType.Diesel;
                SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
                try {
                    GasStationListItem gasStationListItemEditted = new GasStationListItem(nameET.getText().toString(),dateParser.parse(dateET.getText().toString()), tempFuelType, Integer.parseInt(amountET.getText().toString()), Math.round(Double.parseDouble(priceET.getText().toString()) * 100.0) / 100.0);
                    Intent intent = new Intent();
                    intent.putExtra("operation","edit");
                    intent.putExtra("object",gasStationListItemEditted);
                    intent.putExtra("index",pos);
                    setResult(RESULT_OK,intent);
                    dialog.dismiss();
                    finish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        //show dialog
        dialog.setCancelable(true);
        dialog.setTitle("Edit gas station visit");
        dialog.setContentView(view);
        dialog.show();
    }

    void showDeleteDialog(int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //build a delete dialog
        builder.setMessage("Confirm to delete this visit?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    // Confirm deletion
                    Intent intent = new Intent();
                    intent.putExtra("operation","delete");
                    intent.putExtra("index",pos);
                    setResult(RESULT_OK,intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, id) -> {
                    // User cancelled the dialog
                    dialog.dismiss();
                });

        //display dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}