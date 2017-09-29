package com.daniel.exchangeoffice.classes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DataNBP {


    private static List<String> rateUSD = new ArrayList<>();
    private static List<String> rateEUR = new ArrayList<>();
    private static List<String> rateGBP = new ArrayList<>();


    private String name;
    private String exchangeRate;


    public DataNBP() {

    }


    public static List<String> getRateUSD() {
        return rateUSD;
    }

    public static void setRateUSD(List<String> rateUSD) {
        DataNBP.rateUSD = rateUSD;
    }

    public static List<String> getRateEUR() {
        return rateEUR;
    }

    public static void setRateEUR(List<String> rateEUR) {
        DataNBP.rateEUR = rateEUR;
    }

    public static List<String> getRateGBP() {
        return rateGBP;
    }

    public static void setRateGBP(List<String> rateGBP) {
        DataNBP.rateGBP = rateGBP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataNBP that = (DataNBP) o;

        if (rateUSD != null ? !rateUSD.equals(that.rateUSD) : that.rateUSD != null) return false;
        if (rateEUR != null ? !rateEUR.equals(that.rateEUR) : that.rateEUR != null) return false;
        return rateGBP != null ? rateGBP.equals(that.rateGBP) : that.rateGBP == null;
    }

    @Override
    public int hashCode() {
        int result = rateUSD != null ? rateUSD.hashCode() : 0;
        result = 31 * result + (rateEUR != null ? rateEUR.hashCode() : 0);
        result = 31 * result + (rateGBP != null ? rateGBP.hashCode() : 0);
        return result;
    }


    @Scheduled(cron = "0 0 8 * * *")
    public static void checkCurrency() {
        try {
            URL urlGPB = new URL("http://api.nbp.pl/api/exchangerates/rates/a/gbp/");
            URLConnection urlConnectionGBP = urlGPB.openConnection();
            BufferedReader bufferedReaderGBP = new BufferedReader(new InputStreamReader(urlConnectionGBP.getInputStream()));
            URL urlEUR = new URL("http://api.nbp.pl/api/exchangerates/rates/a/eur/");
            URLConnection urlConnectionEUR = urlEUR.openConnection();
            BufferedReader bufferedReaderEUR = new BufferedReader(new InputStreamReader(urlConnectionEUR.getInputStream()));
            URL urlUSD = new URL("http://api.nbp.pl/api/exchangerates/rates/a/usd/");
            URLConnection urlConnectionUSD = urlUSD.openConnection();
            BufferedReader bufferedReaderUSD = new BufferedReader(new InputStreamReader(urlConnectionUSD.getInputStream()));
            JSONParser parser = new JSONParser();
            String line = "";


            while ((line = bufferedReaderGBP.readLine()) != null) {

                JSONObject jsonObject = (JSONObject) parser.parse(line);


                JSONArray jsonArray = (JSONArray) jsonObject.get("rates");
                for (Object o : jsonArray) {
                    JSONObject oo = (JSONObject) o;
                    DataNBP.getRateGBP().add(oo.get("mid").toString());
                }


            }


            while ((line = bufferedReaderEUR.readLine()) != null) {

                JSONObject jsonObject = (JSONObject) parser.parse(line);


                JSONArray jsonArray = (JSONArray) jsonObject.get("rates");
                for (Object o : jsonArray) {
                    JSONObject oo = (JSONObject) o;
                    DataNBP.getRateEUR().add(oo.get("mid").toString());
                }


            }

            while ((line = bufferedReaderUSD.readLine()) != null) {

                JSONObject jsonObject = (JSONObject) parser.parse(line);


                JSONArray jsonArray = (JSONArray) jsonObject.get("rates");

                for (Object o : jsonArray) {
                    JSONObject oo = (JSONObject) o;
                    DataNBP.getRateUSD().add(oo.get("mid").toString());
                }


            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static List<String> currencyNames() {
        List<String> currencyNames = new ArrayList<>();
        try {
            URL urlGPB = new URL("http://api.nbp.pl/api/exchangerates/rates/a/gbp/");
            URLConnection urlConnectionGBP = urlGPB.openConnection();
            BufferedReader bufferedReaderGBP = new BufferedReader(new InputStreamReader(urlConnectionGBP.getInputStream()));
            URL urlEUR = new URL("http://api.nbp.pl/api/exchangerates/rates/a/eur/");
            URLConnection urlConnectionEUR = urlEUR.openConnection();
            BufferedReader bufferedReaderEUR = new BufferedReader(new InputStreamReader(urlConnectionEUR.getInputStream()));
            URL urlUSD = new URL("http://api.nbp.pl/api/exchangerates/rates/a/usd/");
            URLConnection urlConnectionUSD = urlUSD.openConnection();
            BufferedReader bufferedReaderUSD = new BufferedReader(new InputStreamReader(urlConnectionUSD.getInputStream()));
            JSONParser parser = new JSONParser();
            String line = "";


            while ((line = bufferedReaderGBP.readLine()) != null) {

                JSONObject jsonObject = (JSONObject) parser.parse(line);
                currencyNames.add((String) jsonObject.get("code"));


            }


            while ((line = bufferedReaderEUR.readLine()) != null) {

                JSONObject jsonObject = (JSONObject) parser.parse(line);
                currencyNames.add((String) jsonObject.get("code"));

            }

            while ((line = bufferedReaderUSD.readLine()) != null) {

                JSONObject jsonObject = (JSONObject) parser.parse(line);
                currencyNames.add((String) jsonObject.get("code"));

            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return currencyNames;
    }
}
