package com.example.wang34_mycarfootprint;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wang34_mycarfootprint.model.GasStationListItem;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        //get intent data
        GasStationListItem gasStationListItem = (GasStationListItem) getIntent().getSerializableExtra("item");
        int pos = getIntent().getIntExtra("index",0);

        //component
        Button buttonEdit = (Button) findViewById(R.id.button_edit);
        Button buttonDelete = (Button) findViewById(R.id.button_delete);

        //edit dialog button listener
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });

        //delete dialog button listener
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog(gasStationListItem, pos);
            }
        });
    }

    void showEditDialog() {

        //inflate dialog
        final Dialog dialog = new Dialog(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_dialog, null);

        //initialize view

        final EditText nameET = view.findViewById(R.id.dialog_edit_gas_station_name);
        final EditText dateET = view.findViewById(R.id.dialog_edit_date);
        final EditText amountET = view.findViewById(R.id.dialog_edit_litre_amount);
        final EditText priceET = view.findViewById(R.id.dialog_edit_price_per_litre);
        final Button btnApplyChange = view.findViewById(R.id.button_apply_change);

        btnApplyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //show dialog
        dialog.setCancelable(true);
        dialog.setTitle("Edit gas station visit");
        dialog.setContentView(view);
        dialog.show();
    }

    void showDeleteDialog(GasStationListItem gasStationListItem, int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //build a delete dialog
        builder.setMessage("Confirm to delete this visit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Confirm deletion
                        Intent intent = new Intent();
                        intent.putExtra("operation","delete");
                        intent.putExtra("index",pos);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });

        //display dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}