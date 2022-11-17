package com.aumbishoyi.anywhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
public class MainActivity extends AppCompatActivity {
    TextView touchpad;
    boolean flag=false;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    Intent intent;
    String ip_address;
    String port;


    protected ListView mDrawerList;
    protected String[] listArray = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };



    public void leftClick(View view){
        String my_url = String.format("http://%s:%s/?command=leftClick",ip_address,port);
        new MyHttpRequestTask().execute(my_url);
    }
    public void rightClick(View view){
        String my_url = String.format("http://%s:%s/?command=rightClick",ip_address,port);
        new MyHttpRequestTask().execute(my_url);
    }
    public void scrollUp(View view){
        String my_url = String.format("http://%s:%s/?command=scrollUp",ip_address,port);
        new MyHttpRequestTask().execute(my_url);
    }
    public void scrollDown(View view){
        String my_url = String.format("http://%s:%s/?command=scrollDown",ip_address,port);
        new MyHttpRequestTask().execute(my_url);
    }


    public void selectItem(int position) {
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

    private class MyHttpRequestTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            String my_url = params[0];
            String result;
            String inputLine;
            try {
                URL url = new URL(my_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                // setting the  Request Method Type
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStreamReader streamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
                // adding the headers for request



            }catch (Exception e){
               Log.i("msg0", String.valueOf(e));
               result = null;
            }

            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = getIntent();
        String ip_address = intent.getStringExtra("ip_address");
        String port = intent.getStringExtra("port");






        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listArray));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                openActivity(position);
            }
        });




        touchpad=(TextView) findViewById(R.id.touchpad);
        final float[] oldx = new float[1];
        final float[] oldy = new float[1];
        View.OnTouchListener handleTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x = (float) event.getX();
                float y = (float) event.getY();
//                Log.i("msg0", "start: (" + oldx + ", " + oldy + ")");

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldx[0] = (float) event.getX();
                        oldy[0] = (float) event.getY();
                        flag = false;
                        Log.i("msg0", "touched down");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("msg0", "moving: (" + (oldx[0]-x) + ", " + (oldy[0]-y) + ")");
                        String my_url = String.format("http://%s:%s/?command=move&x=%f&y=%f",ip_address,port,(x-oldx[0])/5,(y-oldy[0])/5);// Replace this with your own url
                        new MyHttpRequestTask().execute(my_url);
                        oldx[0]=x;
                        flag=true;
                        oldy[0]=y;
                        break;
                    case MotionEvent.ACTION_UP:
                        if(!flag){
                            leftClick(v);
                        }
                        Log.i("msg0", "touched up");
                        break;
                }

                return true;
            }
        };
        touchpad.setOnTouchListener(handleTouch);

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
    protected void openActivity(int position) {

        /**
         * We can set title & itemChecked here but as this BaseActivity is parent for other activity,
         * So whenever any activity is going to launch this BaseActivity is also going to be called and
         * it will reset this value because of initialization in onCreate method.
         * So that we are setting this in child activity.
         */
//		mDrawerList.setItemChecked(position, true);
//		setTitle(listArray[position]);
        drawerLayout.closeDrawer(mDrawerList);
        MainActivity.position = position; //Setting currently selected position in this field so that it will be available in our child activities.

        switch (position) {
            case 0:
                startActivity(new Intent(this, Item1Activity.class));
                break;
            case 1:
                startActivity(new Intent(this, Item2Activity.class));
                break;
            case 2:
                startActivity(new Intent(this, Item3Activity.class));
                break;
            case 3:
                startActivity(new Intent(this, Item4Activity.class));
                break;
            case 4:
                startActivity(new Intent(this, Item5Activity.class));
                break;

            default:
                break;
        }

        Toast.makeText(this, "Selected Item Position::"+position, Toast.LENGTH_LONG).show();
    }
}