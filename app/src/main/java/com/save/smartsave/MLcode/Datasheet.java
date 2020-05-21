package com.save.smartsave.MLcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Datasheet {


    // For merchant.csv File.
    private List<String> bank = Arrays.asList("vkgb", "cbi", "frbl", "idfcfirstbl", "cb", "cggb", "clabl", "kbslabl", "wbc", "sib", "rblbl", "db", "ba", "kbl", "tbt-mufjl", "pupgb",
            "kvbl", "abbl", "yesbl", "nbard", "bbp", "idbibl", "axis", "bob", "apgb", "edb", "dbgb", "alb", "apbl", "ib", "hdfcbl", "bgvb", "sbt", "mrb", "kgb", "jscvtbb", "sg", "bggb",
            "adcbl", "tmbl", "uob", "dbl", "cna", "usfbl", "crgb", "idb", "jpbl", "ubi", "j&kgb", "pmc", "shgb", "cnb", "trbs", "synb", "pbgb", "ubkgb", "ugb", "bb&kbsc", "sbi", "ktbpcl",
            "nsdlpbl", "cubl", "crua", "brkgb", "cen", "kmbl", "hpgb", "ibk", "asfbl", "sbh", "i&cbcl", "sgb", "iibl", "jsfbl", "pnb", "smbc", "sbl", "ucob", "ppbl", "bupgb", "vb", "slbl",
            "ippbl", "fadbpjsc", "pb", "nrb", "anzbgl", "sbj", "bom", "nl", "nesbl", "psb", "sibl", "iob", "bbl", "bc", "ab", "icicibl", "fsfbl", "abl", "nhb", "bb", "sbp", "pgb",
            "jpmcbna", "kmb", "j&kbl", "tgb", "uobl", "kebhb", "esfbl", "kvgb", "mbl", "jrgb", "ici", "nab", "enbdbpjsc", "apgvb", "dbsbl", "bns", "esafsfbl", "kgsgb", "iib", "inb",
            "p&sb", "scb", "dcbbl", "cac&ib", "mbpsc", "mpgb", "finopbl", "agvb", "axb", "e-ibi", "ogb", "obc", "mgb", "sb", "tngb", "abipbl", "bi", "sbm", "aebc", "bnpp", "ssfbl",
            "ptbmitbk", "ioba", "aprb", "ubgb", "ctbcbc,l", "rmgb", "wb", "fbl", "csag", "boi", "qnbsaq", "csbbl", "sidbi", "paytm", "hdf", "lvbl", "ybl", "csfbl", "hsbcl", "bm");

    private List<String> merchant = Arrays.asList("swiggy","zomato","uber eats","dominos","food panda","kfc","amazon","flipkart","myntra ","hathway","jabong","ajio","flpkrt","paytm",
            "club factory","bwkoof","Big basket","grofers","bigbkt","naturebasket","easyday","amazonfresh","spencers","irctci","goibib","MMT","ola","CoinTab","Mi Pay",
            "uber","zoom car","oyorms","trivago","irctci","IRCTC","oyo","olacab","redbus","makemytrip","OYO","netmeds","apollo","pharmeasy","1mg","jio","Cred","Omegaon","Udaan",
            "airtel","vodafone","BSNL","tata sky","dishtv","videocon","netflix","Amazon Pay","Google Pay","MobiKwik","Samsung Pay","WhatsApp","TrueCaller","PayBee",
            "amazon prime","hotstar","spotify","gaana","linkedin","youtube","EMI","emi","loan","policy","lic","Realme","MudraPay");

    private List<String> food = Arrays.asList("swiggy","zomato","uber eats","dominos","food panda","kfc","subway");
    private List<String> shopping = Arrays.asList("amazon","flipkart","myntra ","jabong","ajio","flpkrt","paytm","club factory","bwkoof","snapdeal","shopclues","homeshop18");
    private List<String> grocery = Arrays.asList("Big basket","grofers","bigbkt","naturebasket","easyday","amazonfresh","spencers");
    private List<String> travel = Arrays.asList("irctci","goibib","MMT","ola","uber","zoom car","oyorms","trivago","IRCTC","oyo","olacab","redbus","makemytrip","OYO");
    private List<String> medical = Arrays.asList("netmeds","apollo","pharmeasy","1mg");
    private List<String> bills = Arrays.asList("jio","airtel","vodafone","BSNL","tata sky","dishtv","videocon");
    private List<String> subscription = Arrays.asList("netflix","amazon prime","hotstar","spotify","gaana","linkedin","youtube", "hathway");
    private List<String> emi = Arrays.asList("emi","loan","policy","lic");
    private List<String> otp = Arrays.asList("OTP","requested");
    private List<String> ess = Arrays.asList("jio","airtel","vodafone","BSNL","tata sky","dishtv","videocon","emi","loan","policy","lic","netmeds","apollo","pharmeasy","1mg","irctci","IRCTC",
            "Big basket","grofers","bigbkt","naturebasket","easyday","amazonfresh","spencers","ola","olacab","uber","uberride","zoom car");



    // For Autocorrect.csv File.
    private List<String> credit = Arrays.asList("credit","credited","credt","crd","transaction","trnxn.","trnx.","trxn.","trx.","trnxn","trnx","trxn","trx");
    private List<String> debit = Arrays.asList("debited","debit","dbtd.","dbt.","deposited");
    private List<String> rs = Arrays.asList("rs","inr","rupees","rupee", "rs.");
    private List<String> balance = Arrays.asList("balance","bal","balnc","blnce","bal");



    // For unigram.csv File.
    private List<String> unigram = Arrays.asList("keywords","credit","credited","credt","crd","transaction","trnxn.","trnx.","trxn.","trx.","trnxn","trnx","trxn","trx","debited","debit","dbtd."
            ,"dbt.","deposited","rs","inr","rupees","rupee","balance","bal","balnc","blnce","bal.","rupee","swiggy","zomato","uber eats"
            ,"dominos","food panda","kfc,amazon","flipkart","myntra","jabong","ajio","flpkrt","paytm","club factory","bwkoof","Big basket","grofers",
            "bigbkt","naturebasket","easyday","amazonfresh","spencers","irctci","goibib","MMT","ola","uber","zoom car","oyorms","trivago","irctci","IRCTC",
            "oyo","olacab","redbus","makemytrip","OYO","netmeds","apollo","pharmeasy","1mg","jio,airtel","vodafone,BSNL","tata sky","dishtv","videocon",
            "netflix","amazon prime","hotstar","spotify","gaana","linkedin","youtube","emi","loan","policy","lic", "rs.", "axis", "hathway");


    // Function to create a autocorrect List of Lists containing all List of autocorrect.csv file.
    public List<List<String>> createAutocorrect(){

        List<List<String>> autocorrect = new ArrayList<>();

        autocorrect.add(credit);
        autocorrect.add(debit);
        autocorrect.add(rs);
        autocorrect.add(balance);

        return autocorrect;

    }


    // Function to create a merchantcsv List of Lists containing all List of merchant.csv file.
    public List<List<String>> createMerchant(){

        List<List<String>> merchantcsv = new ArrayList<>();

        merchantcsv.add(bank);
        merchantcsv.add(merchant);
        merchantcsv.add(food);
        merchantcsv.add(shopping);
        merchantcsv.add(grocery);
        merchantcsv.add(travel);
        merchantcsv.add(medical);
        merchantcsv.add(bills);
        merchantcsv.add(subscription);
        merchantcsv.add(emi);
        merchantcsv.add(otp);
        merchantcsv.add(ess);

        return merchantcsv;
    }


    // Function to create a unigram List of Lists containing all List of unigram.csv file.
    public List<String> createUnigram(){
        return unigram;
    }
}