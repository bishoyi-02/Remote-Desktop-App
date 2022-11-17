package com.aumbishoyi.anywhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class Connection extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public void ip_address_submit(View view){
        TextView ip_address = (TextView) findViewById(R.id.ip_address);
        TextView port = (TextView) findViewById(R.id.port);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("ip_address", ip_address.getText());
        intent.putExtra("port", port.getText());
//        startActivity(intent);
    }

    public void selectItem(int position) {
        Log.i("msg0", "HI"+String.valueOf(position));
        Intent intent = null;
        switch(position) {
            case 0:
                intent = new Intent(this,MainActivity.class);
                break;
            case 1:
                intent = new Intent(this, Connection.class);
                break;

            default :
                intent = new Intent(this, Connection.class); // Activity_0 as default
                break;
        }

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
//            drawerLayout.closeDrawer(drawerListView);

        }
    }
}