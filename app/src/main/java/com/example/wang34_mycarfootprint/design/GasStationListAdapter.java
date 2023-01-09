package com.example.wang34_mycarfootprint.design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wang34_mycarfootprint.R;
import com.example.wang34_mycarfootprint.model.GasStationListItem;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GasStationListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<GasStationListItem> gasStationList;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public GasStationListAdapter(Context context, ArrayList<GasStationListItem> gasStationList) {
        this.context = context;
        this.gasStationList = gasStationList;
    }

    @Override
    public int getCount() {
        return gasStationList.size();
    }

    @Override
    public Object getItem(int pos) {
        return gasStationList.get(pos);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ContainerView containerView;
        String dateVisitedString;
        String footprintString;
        String calculatedPrice;

        //inflate custom list with ContainerView class
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.custom_listview,viewGroup,false);
            containerView = new ContainerView(view);
            view.setTag(containerView);
        }else{
            containerView = (ContainerView) view.getTag();
        }

        GasStationListItem gasStationListItem = gasStationList.get(i);

        //format data into string to be shown
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        dateVisitedString = dateFormat.format(gasStationListItem.getGasStationVisitDate());
        calculatedPrice = df.format(gasStationListItem.getCalculatedPrice());
        footprintString = String.format(Locale.CANADA,"%d",Math.round(gasStationListItem.getFootprint()));

        //display data
        containerView.gasStationName.setText(gasStationListItem.getGasStationName());
        containerView.dateVisited.setText(dateVisitedString);
        containerView.calculatedPrice.setText(calculatedPrice);
        containerView.footPrint.setText(footprintString);

        return view;
    }

    private static class ContainerView{
        private final TextView gasStationName;
        private final TextView dateVisited;
        private final TextView calculatedPrice;
        private final TextView footPrint;

        public ContainerView(View view){
            gasStationName = view.findViewById(R.id.gas_station_name);
            dateVisited = view.findViewById(R.id.date_visited);
            footPrint = view.findViewById(R.id.footprint);
            calculatedPrice = view.findViewById(R.id.calculated_price);
        }
    }
}
