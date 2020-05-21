package com.save.smartsave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.save.smartsave.ui.home.HomeFragment;
import com.save.smartsave.ui.insight.InsightFragment;
import com.save.smartsave.ui.notifications.SaveMoneyFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.save.smartsave.LoginScreen.currentUser;
import static com.save.smartsave.LoginScreen.currentUserUid;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView navigationView;
    FirebaseFunctions functions;
    Toolbar toolbar;

    Map<String,ArrayList<Number>> getChartData=new HashMap<>();
    public static ArrayList<Number> credit=new ArrayList<>();
    public static ArrayList<Number> balance=new ArrayList<>();

    public static ArrayList<Number> currentMonthCategory=new ArrayList<>();
    public static ArrayList<Number> previousMonthCategory=new ArrayList<>();
    public static Number sumOfCategory;
    public static ArrayList<String> categoryName=new ArrayList<>(Arrays.asList("BILLS","FOOD","GROCERY","HEALTH","LOAN","SHOPPING","SUBSCRIPTION","TRAVEL"));
    public static ArrayList<Number> onlineTransaction=new ArrayList<>();
    public static ArrayList<Number> offlineTransaction=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUserUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        toolbar=findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        navigationView=findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        functions = FirebaseFunctions.getInstance();
        sendMail();
        getCategory();
        getTransactionMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.side_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.setting_menu:
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout_menu:
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity.this,LoginScreen.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return false;
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int i=menuItem.getItemId();
        switch(i){
            case R.id.bottom1:
                toolbar.setTitle("Home");
                Fragment fragment=new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                return true;
            case R.id.bottom2:
                toolbar.setTitle("Insights");
                Fragment fragment2=new InsightFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, fragment2, fragment2.getClass().getSimpleName()).addToBackStack(null).commit();
                return true;
            case R.id.bottom3:
                toolbar.setTitle("Save Money");
                Fragment fragment3=new SaveMoneyFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, fragment3, fragment3.getClass().getSimpleName()).addToBackStack(null).commit();

                return true;
        }
        return false;
    }

    private Task<String> sendMail() {
        Map<String, Object> data = new HashMap<>();
        data.put("uid",currentUserUid);
        return functions
                .getHttpsCallable("getChartData")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) {
                        try{
                            Object obj=new int[10];
                            obj=task.getResult().getData();
                            getChartData= (Map<String, ArrayList<Number>>) obj;
                            credit=getChartData.get("credit");
                            balance=getChartData.get("availableBal");
                            Log.i("hhh1","1");
                            Log.i("hhh1",balance.toString()+credit.toString());
                        }catch (Exception e){
                            Log.d("Error",e.toString());
                        }
                        toolbar.setTitle("Home");
                        Fragment fragment=new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                        return "abhinav";
                    }
                });
    }

    private Task<String> getCategory() {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", currentUserUid);
        data.put("month",3);
        return functions
                .getHttpsCallable("getDataByCateg")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        try{
                            Object result = task.getResult().getData();
                            Map<String,ArrayList<Number>> getDataByCateg=new HashMap<>();
                            getDataByCateg= (Map<String, ArrayList<Number>>) result;
                            currentMonthCategory=getDataByCateg.get("categoryData");
                            previousMonthCategory=getDataByCateg.get("categoryDataP");
                            Log.i("hhh2",currentMonthCategory.toString()+previousMonthCategory.toString());
                            Log.i("hhh2","2");
                            sortCategory();
                            Log.i("hhh2",currentMonthCategory.toString()+previousMonthCategory.toString());
                        }catch (Exception e){
                            Log.d("Error",e.toString());
                        }
                        return "abhinav";
                    }
                });
    }

    public void sortCategory(){
        for(int i=0;i<currentMonthCategory.size()-1;i++){
            for(int j=0;j<currentMonthCategory.size()-1-i;j++){
                if(currentMonthCategory.get(j).intValue()>currentMonthCategory.get(j+1).intValue()){
                    Number a=currentMonthCategory.get(j);
                    currentMonthCategory.set(j,currentMonthCategory.get(j+1));
                    currentMonthCategory.set(j+1,a);

                    Number b=previousMonthCategory.get(j);
                    previousMonthCategory.set(j,previousMonthCategory.get(j+1));
                    previousMonthCategory.set(j+1,b);

                    String c=categoryName.get(j);
                    categoryName.set(j,categoryName.get(j+1));
                    categoryName.set(j+1,c);
                }
            }
        }

        for(int i=0;i<currentMonthCategory.size();i++){
            sumOfCategory=currentMonthCategory.get(i);
        }
    }

    private Task<String> getTransactionMode() {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("uid",currentUserUid);
        data.put("month",3);


        return functions
                .getHttpsCallable("getTransactionMode")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        try{
                            Object result = task.getResult().getData();
                            Map<String,ArrayList<Number>> getDataByCateg=new HashMap<>();
                            getDataByCateg= (Map<String, ArrayList<Number>>) result;
                            onlineTransaction=getDataByCateg.get("online");
                            offlineTransaction=getDataByCateg.get("offline");
                            Log.i("hhh3",onlineTransaction.toString()+offlineTransaction.toString());
                        } catch (Exception e){
                            Log.d("Error",e.toString());
                        }
                        return "abhinav";
                    }
                });
    }




}
