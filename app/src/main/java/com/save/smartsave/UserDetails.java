package com.save.smartsave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.save.smartsave.LoginScreen.userName;
import static com.save.smartsave.LoginScreen.userPhoto;

public class UserDetails extends AppCompatActivity {

    Button button;
    CircleImageView profilePic;
    TextView profileName;
    String ageData;
    String locData;
    String professionData;
    ArrayList<String> listSpinner=new ArrayList<String>();
    ArrayList<String> listAll=new ArrayList<String>();
    ArrayList<String> listAge=new ArrayList<String>();
    ArrayList<String> listProfession=new ArrayList<String>();
    AutoCompleteTextView act;
    Spinner age;
    Spinner profession;


    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        button=findViewById(R.id.nextButton);
        profileName=findViewById(R.id.user_detail_profile_name);
        profilePic=findViewById(R.id.user_detail_profile_picture);
        if(profilePic!=null)Picasso.get().load(userPhoto).into(profilePic);
        profileName.setText(userName);
        addAgeAndProfession();
        callAll();
        age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ageData=listAge.get(position);
                locData= String.valueOf(act.getText());
                Toast.makeText(UserDetails.this, ageData+ locData, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        profession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                professionData=listProfession.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        db = FirebaseFirestore.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(UserDetails.this, ML_Process.class);
                    startActivity(intent);
                    finish();
            }
        });
    }

    public void addAgeAndProfession(){
        for(int i=18;i<=80;i++){
            listAge.add(i+"");
        }
        listProfession.add("Student");
        listProfession.add("Employment");
        listProfession.add("Business");
        listProfession.add("Home maker");
        listProfession.add("Entrepreneur");
    }
    public void callAll() {
        obj_list();
        addToSpinner();
        addToAll();

    }
    // Get the content of cities.json from assets directory and store it as string
    public String getJson() {
        String json=null;
        try
        {
            // Opening cities.json file
            InputStream is = getAssets().open("cities.json");
            // is there any content in the file
            int size = is.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            is.read(buffer);
            // close the stream --- very important
            is.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return json;
        }
        return json;
    }
    // This add all JSON object's data to the respective lists
    void obj_list() {
        // Exceptions are returned by JSONObject when the object cannot be created
        try
        {
            // Convert the string returned to a JSON object
            JSONObject jsonObject=new JSONObject(getJson());
            // Get Json array
            JSONArray array=jsonObject.getJSONArray("array");
            // Navigate through an array item one by one
            for(int i=0;i<array.length();i++)
            {
                // select the particular JSON data
                JSONObject object=array.getJSONObject(i);
                String city=object.getString("name");
                String state=object.getString("state");
                // add to the lists in the specified format
                listSpinner.add(String.valueOf(i+1)+" : "+city+" , "+state);
                listAll.add(city+" , "+state);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
    // Add the data items to the spinner
    void addToSpinner() {
        Spinner spinner=(Spinner)findViewById(R.id.spCity);
        // Adapter for spinner
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    // The first auto complete text view
    void addToAll() {
        act=(AutoCompleteTextView)findViewById(R.id.actAll);
        age=(Spinner) findViewById(R.id.age);
        profession=(Spinner)findViewById(R.id.profession);
        adapterSetting(listAll);
        adapterAge(listAge);
        adapterProfession(listProfession);
    }
    // setting adapter for auto complete text views
    void adapterAge(ArrayList arrayList) {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,arrayList);
        age.setAdapter(adapter);
        hideKeyBoard();
    }
    // setting adapter for auto complete text views
    void adapterProfession(ArrayList arrayList) {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,arrayList);
        profession.setAdapter(adapter);
        hideKeyBoard();
    }
    // setting adapter for auto complete text views
    void adapterSetting(ArrayList arrayList) {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        act.setAdapter(adapter);
        hideKeyBoard();
    }
    // hide keyboard on selecting a suggestion
    public void hideKeyBoard() {
        act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }
}
