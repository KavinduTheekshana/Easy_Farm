package com.example.easyfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyfarm.Models.OutputDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class result extends AppCompatActivity {

    ListView listView;
    List<OutputDetails> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listView = (ListView) findViewById(R.id.productslist);
        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("OutputDetails");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    OutputDetails outputDetails = studentDatasnap.getValue(OutputDetails.class);
                    user.add(outputDetails);

                }

                MyAdapter adapter = new MyAdapter(result.this, R.layout.custom_results, (ArrayList<OutputDetails>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        TextView COL1;
        TextView COL2;
        TextView COL3;
        TextView COL4;
        TextView COL5;
    }

    class MyAdapter extends ArrayAdapter<OutputDetails> {
        LayoutInflater inflater;
        Context myContext;
        List<OutputDetails> user;


        public MyAdapter(Context context, int resource, ArrayList<OutputDetails> objects) {
            super(context, resource, objects);
            myContext = context;
            user = objects;
            inflater = LayoutInflater.from(context);
            int y;
            String barcode;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.custom_results, null);

                holder.COL1 = (TextView) view.findViewById(R.id.id);
                holder.COL2 = (TextView) view.findViewById(R.id.respond);
                holder.COL3 = (TextView) view.findViewById(R.id.status);
                holder.COL4 = (TextView) view.findViewById(R.id.date);
                holder.COL5 = (TextView) view.findViewById(R.id.time);


                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText("ID:- " + user.get(position).getId());
            holder.COL2.setText("Respond:- " + user.get(position).getRespond());
            holder.COL3.setText("Status:- " + user.get(position).getStatus());
            holder.COL4.setText("Date:- " + user.get(position).getDate());
            holder.COL5.setText("Time:- " + user.get(position).getTime());
            System.out.println(holder);

            return view;
        }
    }
}