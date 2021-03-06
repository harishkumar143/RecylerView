package com.impelsys.harish.telstra;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecylerViewAdapater recylerViewAdapater;
    List<HashMap<String,String>> listdata;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recylerview);
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Fetching data...");
        dialog.show();
       initAdapater();
    }


    public void initAdapater() {

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        // adapter.setHasStableIds(true);
        recylerViewAdapater=new RecylerViewAdapater(this);
        recyclerView.setAdapter(recylerViewAdapater);
        updatelist();
    }

    private  List<HashMap<String,String>> getJsonObject(){

        listdata=new ArrayList<HashMap<String, String>>();

        String data = ReadFromfile("facts.json", MainActivity.this);
        Log.e("Data", "Data is " + data);
         if(data.isEmpty()){
             Toast.makeText(MainActivity.this,"Unable to read json file.",Toast.LENGTH_SHORT).show();
         }else {
             try {
                 JSONObject jsonObject = new JSONObject(data);
                 String title = jsonObject.getString("title");
                 getSupportActionBar().setTitle(title);
                 JSONArray jsonArray = jsonObject.getJSONArray("rows");

                 for (int i = 0; i < jsonArray.length(); i++) {
                     JSONObject object = jsonArray.getJSONObject(i);
                     HashMap<String, String> map = new HashMap<>();
                     map.put("title", object.getString("title"));
                     map.put("description", object.getString("description"));
                     map.put("imageurl", object.getString("imageHref"));
                     listdata.add(map);
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }

        return listdata;

    }

    public String ReadFromfile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets().open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    private void updatelist(){
        listdata=getJsonObject();
         if(recylerViewAdapater!=null){
             recylerViewAdapater.setListdata(listdata);
         }else{
             Toast.makeText(MainActivity.this,"Something went wrong try again",Toast.LENGTH_SHORT).show();
         }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh_list:
                dialog.show();
                updatelist();
                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

