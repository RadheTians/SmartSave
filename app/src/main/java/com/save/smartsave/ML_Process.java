package com.save.smartsave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.save.smartsave.MLcode.Datasheet;
import com.save.smartsave.MLcode.MainSheet;
import com.save.smartsave.MLcode.SMS_Extracted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import static com.save.smartsave.LoginScreen.currentUserUid;
import static com.save.smartsave.LoginScreen.fillUserDetail;


public class ML_Process extends AppCompatActivity {


    private ProgressBar spinner;
    FirebaseFunctions functions;

    private ArrayList<MainSheet> sheetSamples = new ArrayList<>();
    private ArrayList<SMS_Extracted> smsExtracted = new ArrayList<>();
    private HashMap<String, Object> finaldata = new HashMap<>();

    Datasheet datasheet = new Datasheet();
    List<List<String>> objMer = datasheet.createMerchant();
    List<String> bnk = objMer.get(0);
    List<String> mname = objMer.get(1);

    String selectedDate;
    SharedPreferences sharedpreferences;
    private AlertDialog alertDialogSMS;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ml__process);
        functions = FirebaseFunctions.getInstance();
        spinner = findViewById(R.id.progressBar101);
        spinner.setVisibility(View.GONE);
        sharedpreferences = getSharedPreferences("Time", Context.MODE_PRIVATE);
        selectedDate = sharedpreferences.getString("time", "01-01-2020" + "T" + "00:00:00");
        db=FirebaseFirestore.getInstance();

        if (ContextCompat.checkSelfPermission(ML_Process.this,Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ML_Process.this, new String[] {Manifest.permission.READ_SMS}, 1);
        } else{
            spinner.setVisibility(View.VISIBLE);
            //refreshSmsInbox();
            try {
                readAndInsert();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ML_Process.this);
            SharedPreferences.Editor editor = sp.edit();
            for(int loop = 0; loop < bnk.size();loop++){
                if ((sp.getString(bnk.get(loop), "0.0") == "0.0") || (sp.getString(bnk.get(loop), "0.0") == "500000.0")){
                    editor.putString(bnk.get(loop), "0.0");
                    editor.commit();
                }
            }
            mainFn();
        }

    }

    private void alertDialogSMS() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("SMS Permission").setMessage("Permission needed to read your SMS");
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!(ActivityCompat.checkSelfPermission(ML_Process.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(ML_Process.this, new String[]{Manifest.permission.READ_SMS}, 1);
                }
            }
        });

        alertDialogSMS = builder.create();
        alertDialogSMS.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(ML_Process.this,Manifest.permission.READ_SMS)==  PackageManager.PERMISSION_GRANTED) {
                        spinner.setVisibility(View.VISIBLE);
                        Log.i("hhh","hhh");
                        try {
                            readAndInsert();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        //refreshSmsInbox();
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ML_Process.this);
                        SharedPreferences.Editor editor = sp.edit();
                        for(int loop = 0; loop < bnk.size();loop++){
                            if ((sp.getString(bnk.get(loop), "0.0") == "0.0") || (sp.getString(bnk.get(loop), "0.0") == "500000.0")) {
                                editor.putString(bnk.get(loop), "0.0");
                                editor.commit();
                            }
                        }
                        mainFn();
                    }
                }
                else {
                    alertDialogSMS();
                }
                break;
        }
    }


    private void readAndInsert() throws UnsupportedEncodingException {
        AssetManager assetManager = getAssets();
        InputStream is = null;

        try {
            is = assetManager.open("convertcsv.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";
        try {

            while ((line = reader.readLine()) != null) {
                String b = "";
                String[] st = line.split(",");
                b = st[4];
                b = b.replaceAll("(?<=\\d),(?=\\d)", "");
                b = b.replaceAll("(-?\\d+\\.\\d+)", " $1 ");
                b = b.replaceAll("(-?[a-zA-Z\\-]+)(\\d+)", "$1 $2");
                b = b.replace("\n", " ");

                MainSheet obj= new MainSheet();
                //your attributes
                obj.setTime("12:41:24");
                obj.setDate("01-05-2020");
                obj.setHead(st[0]);
                obj.setBody(b);
                sheetSamples.add(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("TEST1", "Reading done.");
    }


    public void refreshSmsInbox() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'hh:mm:ss");
        //Log.i("heyy", selectedDate);

        Date dateStart = null;
        try {
            dateStart = formatter.parse(selectedDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        String filter = "date>=" + dateStart.getTime();
        //Log.i("Hey", millisToDate(dateStart.getTime()));

        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, filter, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int indexDate = smsInboxCursor.getColumnIndex("date");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) {
            return;
        }
        int i=0;

        do {
            String date = millisToDate(smsInboxCursor.getLong(indexDate));
            String date1 = "" + date;
            date1 = date1.replace(" ", ",");
            date1 = date1.replace("/", "-");
            String b = "" + smsInboxCursor.getString(indexBody);
            b = b.replaceAll("(?<=\\d),(?=\\d)", "");
            b = b.replaceAll("(-?\\d+\\.\\d+)", " $1 ");
            b = b.replaceAll("(-?[a-zA-Z\\-]+)(\\d+)", "$1 $2");
            b = b.replace("\n", " ");

            String[] tokens = date1.split(",");
            MainSheet sample = new MainSheet();

            sample.setDate(tokens[0]);
            sample.setTime(tokens[1]);
            sample.setHead(smsInboxCursor.getString(indexAddress));
            sample.setBody(b);

            Log.i("SMS sheetSamples",sample.toString());

            sheetSamples.add(sample);
            i++;

        } while (smsInboxCursor.moveToNext());

        selectedDate = formatter.format(new Date());
        sharedpreferences.edit().putString("time", selectedDate).apply();
    }

    public static String millisToDate(long currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }

    private String headcheck(String a) {

        for (int i = 0; i < bnk.size(); i++) {
            if (a.toLowerCase().contains(bnk.get(i)))
                return "BANK";
        }
        for (int i = 0; i < mname.size(); i++) {
            if (a.toLowerCase().contains(mname.get(i)))
                return "MERCHANT";
        }
        return "OTHER";
    }

    private boolean is_index(int n) {

        try {
            sheetSamples.get(n);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    private ArrayList<Integer> getIndex(ArrayList<String> l) {
        ArrayList<Integer> indexPosList = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            if ((l.get(i).equals("rs")) || (l.get(i).equals("inr")) || (l.get(i).equals("rupees")) || (l.get(i).equals("rupee")) || (l.get(i).equals("rs.")))
                indexPosList.add(i);
        }
        return indexPosList;
    }

    private void mainFn() {

        List<List<String>> objAuto = datasheet.createAutocorrect();
        List<String> unigram = datasheet.createUnigram();
        Map<String,String> abc=new HashMap<>();


        for (int i = 0; i < sheetSamples.size(); i++) {

            MainSheet sms = new MainSheet();
            sms.setDate(sheetSamples.get(i).getDate());
            sms.setTime(sheetSamples.get(i).getTime());

            if ((headcheck(sheetSamples.get(i).getHead()).equals("BANK")) && (is_index(i - 1))) {
                if (headcheck(sheetSamples.get(i - 1).getHead()).equals("MERCHANT")) {
                    sms.setHead(sheetSamples.get(i).getHead());
                    sms.setBody(sheetSamples.get(i).getBody() + " " + sheetSamples.get(i-1).getBody());
                } else {
                    sms.setHead(sheetSamples.get(i).getHead());
                    sms.setBody(sheetSamples.get(i).getBody());
                }
            }

            if ((headcheck(sheetSamples.get(i).getHead()).equals("BANK")) && (is_index(i - 1) == false)) {
                sms.setHead(sheetSamples.get(i).getHead());
                sms.setBody(sheetSamples.get(i).getBody());
            }

            if ((headcheck(sheetSamples.get(i).getHead()) == "OTHER") || headcheck(sheetSamples.get(i).getHead()).equals("MERCHANT")) {
                sms.setHead("OTHER");
                sms.setBody("OTHER");
            }

            if ((!sms.getHead().equals("OTHER")) && (!sms.getBody().equals("OTHER"))) {
                try {
                    String bank = "";
                    for (int j = 0; j < bnk.size(); j++) {
                        if (sms.getHead().toLowerCase().contains(bnk.get(j))) {
                            bank = bnk.get(j);
                            break;
                        }
                    }

                    String txt = sms.getBody().toLowerCase();
                    txt = txt.replace("-", " ");

                    String[] z = txt.split(" ");
                    String txt1 = "";

                    for (int j = 0; j < z.length; j++)
                        txt1 = txt1 + " " + z[j];

                    for (int j = 0; j < unigram.size(); j++) {
                        if ((unigram.get(j).equals("rs")) || (unigram.get(j).equals("inr")) || (unigram.get(j).equals("rupees")) || (unigram.get(j).equals("rs.")) ||
                                (unigram.get(j).equals("lic")))
                            txt1 = txt1.replace("." + unigram.get(j), " " + unigram.get(j));
                        else
                            txt1 = txt1.replace(unigram.get(j), " " + unigram.get(j) + " ");
                    }


                    z = txt1.split(" ");

                    ArrayList<String> s = new ArrayList<>();
                    for (int j = 0; j < unigram.size(); j++) {
                        s.add(unigram.get(j));
                    }
                    for (int j = 0; j < z.length; j++) {
                        if (z[j].matches("-?\\d+(\\.\\d+)?")) {
                            s.add(z[j]);
                        }
                    }

                    ArrayList<String> t = new ArrayList<>();
                    for (int j = 0; j < z.length; j++) {
                        for (int k = 0; k < s.size(); k++) {
                            if (z[j].equalsIgnoreCase(s.get(k))) {
                                t.add(z[j]);
                            }
                        }
                    }

                    List<String> otp = objMer.get(10);
                    List<String> ess = objMer.get(11);

                    List<String> crd = objAuto.get(0);
                    List<String> dbt = objAuto.get(1);
                    List<String> rs = objAuto.get(2);
                    List<String> bln = objAuto.get(3);

                    ArrayList<String> l = new ArrayList<>();
                    for (int j = 0; j < t.size(); j++) {
                        for (int k = 0; k < crd.size(); k++) {
                            if (crd.get(k).equals(t.get(j)))
                                l.add(crd.get(k));
                        }
                        for (int k = 0; k < dbt.size(); k++) {
                            if (dbt.get(k).equals(t.get(j)))
                                l.add(dbt.get(k));
                        }
                        for (int k = 0; k < rs.size(); k++) {
                            if (rs.get(k).equals(t.get(j)))
                                l.add(rs.get(k));
                        }
                        for (int k = 0; k < bln.size(); k++) {
                            if (bln.get(k).equals(t.get(j)))
                                l.add(bln.get(k));
                        }
                        for (int k = 0; k < mname.size(); k++) {
                            if (mname.get(k).equals(t.get(j)))
                                l.add(mname.get(k));
                        }
                        for (int k = 0; k < otp.size(); k++) {
                            if (otp.get(k).equals(t.get(j)))
                                l.add(otp.get(k));
                        }

                        try {
                            Double.parseDouble(t.get(j));
                            l.add(t.get(j));
                        } catch (Exception e) {
                        }
                    }

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                    String av1 = sp.getString(bank, "0.0");
                    double av = Double.parseDouble(av1);
                    SMS_Extracted data = new SMS_Extracted();
                    data.setDATE(sms.getDate());
                    data.setTIME(sms.getTime());
                    data.setBANK(bank);

                    ArrayList<String> p = new ArrayList<>();

                    ArrayList<Integer> jl;

                    for (int j = 0; j < l.size(); j++) {
                        if (l.get(j).equalsIgnoreCase("CREDIT"))
                            data.setCREDIT(1);
                        if (l.get(j).equalsIgnoreCase("DEBIT"))
                            data.setDEBIT(1);
                        if (l.get(j).equalsIgnoreCase("ATM"))
                            data.setATM(1);
                        else
                            data.setNET_BANKING(1);
                        if ((l.get(j).equalsIgnoreCase("rs")) || l.get(j).equalsIgnoreCase("inr") || l.get(j).equalsIgnoreCase("rupees")
                                || l.get(j).equalsIgnoreCase("rupee") || l.get(j).equalsIgnoreCase("rs."))
                            p.add(l.get(j));
                    }

                    data.setMERCHANT(" ");
                    for (int j = 0; j < l.size(); j++) {
                        for (int k = 0; k < mname.size(); k++) {
                            if (l.get(j).equals(mname.get(k)))
                                data.setMERCHANT(mname.get(k));
                        }
                        for (int k = 0; k < objMer.get(2).size(); k++) {
                            if (l.get(j).equals(objMer.get(2).get(k)))
                                data.setFOOD(1);
                        }
                        for (int k = 0; k < objMer.get(3).size(); k++) {
                            if (l.get(j).equals(objMer.get(3).get(k)))
                                data.setSHOPPING(1);
                        }
                        for (int k = 0; k < objMer.get(4).size(); k++) {
                            if (l.get(j).equals(objMer.get(4).get(k)))
                                data.setGROCERY(1);
                        }
                        for (int k = 0; k < objMer.get(5).size(); k++) {
                            if (l.get(j).equals(objMer.get(5).get(k)))
                                data.setTRAVEL(1);
                        }
                        for (int k = 0; k < objMer.get(6).size(); k++) {
                            if (l.get(j).equals(objMer.get(6).get(k)))
                                data.setMEDICAL(1);
                        }
                        for (int k = 0; k < objMer.get(7).size(); k++) {
                            if (l.get(j).equals(objMer.get(7).get(k)))
                                data.setBILLS(1);
                        }
                        for (int k = 0; k < objMer.get(8).size(); k++) {
                            if (l.get(j).equals(objMer.get(8).get(k)))
                                data.setSUBSCRIPTION(1);
                        }
                        for (int k = 0; k < objMer.get(9).size(); k++) {
                            if (l.get(j).equals(objMer.get(9).get(k)))
                                data.setEMI(1);
                        }
                    }
                    SharedPreferences.Editor editor = sp.edit();
                    jl = getIndex(l);

                    data.setAVL_BAL(av);

                    if (p.size() > 2) {
                        data.setTRX_AMT(Double.parseDouble(l.get(jl.get(0) + 1)));
                        data.setAVL_BAL(Double.parseDouble(l.get(jl.get(1) + 1)));
                        editor.putString(bank, l.get(jl.get(1) + 1));
                        editor.commit();

                    }
                    if (p.size() == 2) {
                        if (jl.get(0) == jl.get(1)) {
                            data.setTRX_AMT(Double.parseDouble(l.get(jl.get(0) + 1)));
                            if (data.getCREDIT() == 1) {
                                data.setAVL_BAL(av + Double.parseDouble(l.get(jl.get(0) + 1)));
                                editor.putString(bank, Double.toString(av + Double.parseDouble(l.get(jl.get(0) + 1))));
                                editor.commit();
                            } else {
                                editor.putString(bank, Double.toString(av - Double.parseDouble(l.get(jl.get(0) + 1))));
                                editor.commit();
                                data.setAVL_BAL(av - Double.parseDouble(l.get(jl.get(0) + 1)));
                            }
                        } else {
                            data.setTRX_AMT(Double.parseDouble(l.get(jl.get(0) + 1)));
                            data.setAVL_BAL(Double.parseDouble(l.get(jl.get(1) + 1)));
                            editor.putString(bank, l.get(jl.get(1) + 1));
                            editor.commit();
                        }
                    }
                    if (p.size() == 1) {
                        data.setTRX_AMT(Double.parseDouble(l.get(jl.get(0) + 1)));
                        if (data.getCREDIT() == 1) {
                            data.setAVL_BAL(av + Double.parseDouble(l.get(jl.get(0) + 1)));
                            editor.putString(bank, Double.toString(av + Double.parseDouble(l.get(jl.get(0) + 1))));
                            editor.commit();
                        } else {
                            data.setAVL_BAL(av - Double.parseDouble(l.get(jl.get(0) + 1)));
                            editor.putString(bank, Double.toString(av - Double.parseDouble(l.get(jl.get(0) + 1))));
                            editor.commit();
                        }
                    }

                    for (int j = 0; j < ess.size(); j++) {
                        if (data.getMERCHANT() == ess.get(j)) {
                            data.setESS(1);
                        } else {
                            data.setNON_ESS(1);
                        }
                    }

                    String t1 = "";
                    for (int j = 0; j < t.size(); j++) {
                        t1 = t1 + t.get(j);
                    }
                    String o = "";
                    for (int j = 0; j < otp.size(); j++) {
                        if (Pattern.matches(otp.get(j), t1) == true) {
                            o = otp.get(j);
                            break;
                        }
                    }
                    if (o.isEmpty() == true) {
                        Log.d("Dataa " + i, sms.getBody() + "\n" + data.printData());
                        smsExtracted.add(data);
                    }
                }
                catch (Exception e){
                    Log.i("Error1", sms.toString());
                    abc.put(sms.getDate() + sms.getTime(),sms.toString());
                }
            }
        }

        if (smsExtracted.size() > 0) {
            ArrayList<Integer> smsMedical = new ArrayList<>();
            ArrayList<Integer> smsBills = new ArrayList<>();
            ArrayList<Integer> smsSubscription = new ArrayList<>();
            ArrayList<Integer> smsEMI = new ArrayList<>();
            ArrayList<Integer> smsRec = new ArrayList<>();
            ArrayList<Integer> smsNonRec = new ArrayList<>();
            ArrayList<Integer> smsESS = new ArrayList<>();
            ArrayList<Integer> smsNonESS = new ArrayList<>();
            ArrayList<Integer> smsCredit = new ArrayList<>();
            ArrayList<Integer> smsDebit = new ArrayList<>();
            ArrayList<Integer> smsATM = new ArrayList<>();
            ArrayList<Integer> smsNetBanking = new ArrayList<>();
            ArrayList<Integer> smsFood = new ArrayList<>();
            ArrayList<Integer> smsShopping = new ArrayList<>();
            ArrayList<Integer> smsGrocery = new ArrayList<>();
            ArrayList<Integer> smsTravel = new ArrayList<>();
            ArrayList<String> smsDate = new ArrayList<>();
            ArrayList<String> smsTime = new ArrayList<>();
            ArrayList<String> smsBank = new ArrayList<>();
            ArrayList<String> smsMerchant = new ArrayList<>();
            ArrayList<Double> smsTrxAmt = new ArrayList<>();
            ArrayList<Double> smsAvlBal = new ArrayList<>();

            for (int j = 0; j < smsExtracted.size(); j++) {
                smsMedical.add(smsExtracted.get(j).getMEDICAL());
                smsBills.add(smsExtracted.get(j).getBILLS());
                smsSubscription.add(smsExtracted.get(j).getSUBSCRIPTION());
                smsEMI.add(smsExtracted.get(j).getEMI());
                smsRec.add(smsExtracted.get(j).getREC());
                smsNonRec.add(smsExtracted.get(j).getNON_REC());
                smsESS.add(smsExtracted.get(j).getESS());
                smsNonESS.add(smsExtracted.get(j).getNON_ESS());
                smsCredit.add(smsExtracted.get(j).getCREDIT());
                smsDebit.add(smsExtracted.get(j).getDEBIT());
                smsATM.add(smsExtracted.get(j).getATM());
                smsNetBanking.add(smsExtracted.get(j).getNET_BANKING());
                smsFood.add(smsExtracted.get(j).getFOOD());
                smsShopping.add(smsExtracted.get(j).getSHOPPING());
                smsGrocery.add(smsExtracted.get(j).getGROCERY());
                smsTravel.add(smsExtracted.get(j).getTRAVEL());
                smsDate.add(smsExtracted.get(j).getDATE());
                smsTime.add(smsExtracted.get(j).getTIME());
                smsBank.add(smsExtracted.get(j).getBANK());
                smsMerchant.add(smsExtracted.get(j).getMERCHANT());
                smsTrxAmt.add(smsExtracted.get(j).getTRX_AMT());
                smsAvlBal.add(smsExtracted.get(j).getAVL_BAL());
            }
            finaldata.put("uid", currentUserUid);
            Log.i("mainabhinav",currentUserUid);
            finaldata.put("length",smsTrxAmt.size());
            finaldata.put("medicals", smsMedical);
            finaldata.put("bills", smsBills);
            finaldata.put("subscription", smsSubscription);
            finaldata.put("emi", smsEMI);
            finaldata.put("REC", smsRec);
            finaldata.put("NON-REC", smsNonRec);
            finaldata.put("ESS", smsESS);
            finaldata.put("NON-ESS", smsNonESS);
            finaldata.put("credit", smsCredit);
            finaldata.put("debit", smsDebit);
            finaldata.put("atm", smsATM);
            finaldata.put("netBanking", smsNetBanking);
            finaldata.put("food", smsFood);
            finaldata.put("shopping", smsShopping);
            finaldata.put("grocery", smsGrocery);
            finaldata.put("travel", smsTravel);
            finaldata.put("date", smsDate);
            finaldata.put("time", smsTime);
            finaldata.put("bank", smsBank);
            finaldata.put("merchant", smsMerchant);
            finaldata.put("trxAmt", smsTrxAmt);
            finaldata.put("availBal", smsAvlBal);
        }

        Log.i("TESTING", "Test");
        //db.collection("SMS").document(currentUserUid).set(abc, SetOptions.merge());
        //db.collection("SMS").document(currentUserUid).set(abc);


        //sendMail(finaldata);
        Intent intent = new Intent(ML_Process.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public String msgSend;

    private Task<String> sendMail(Map<String, Object> dataToSend ){
        return functions
                .getHttpsCallable("uploadsms")
                .call(dataToSend)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        Log.i("SAVES", "BEFORE");
                        msgSend = (String) task.getResult().getData();
                        Log.i("SAVES", "MSG1");
                        Log.i("SAVES", msgSend);
                        Intent intent = new Intent(ML_Process.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return msgSend;
                    }
                });
    }

}