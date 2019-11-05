package com.googultaxi.taxiuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.googultaxi.taxiuser.Help.AboutApp;
import com.googultaxi.taxiuser.Help.AboutUs;
import com.googultaxi.taxiuser.Help.ContactUs;
import com.googultaxi.taxiuser.Help.DeveloperDetails;
import com.googultaxi.taxiuser.Help.TravelCatalogue;

public class HelpActivity extends AppCompatActivity {
    ListView listView;
    TextView textView;
    String[] listItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().hide();
        listView=(ListView)findViewById(R.id.listview);
        textView=(TextView)findViewById(R.id.textView);
        listItem = getResources().getStringArray(R.array.array_technology);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Intent intent=new Intent(HelpActivity.this, AboutUs.class);
                    startActivity(intent);
                }
                else if(position==1)
                {
                    Intent intent=new Intent(HelpActivity.this, ContactUs.class);
                    startActivity(intent);
                }
                else if(position==2)
                {
                    Intent intent=new Intent(HelpActivity.this, TravelCatalogue.class);
                    startActivity(intent);
                }
                else if(position==3)
                {
                    Intent intent=new Intent(HelpActivity.this,AboutApp.class);
                    startActivity(intent);
                }
else if(position==4)
                {
                    Intent intent=new Intent(HelpActivity.this, DeveloperDetails.class);
                    startActivity(intent);
                }
            }
        });
    }
}
