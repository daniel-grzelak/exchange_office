package com.daniel.exchangeoffice.classes;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Data
//lombok nie dziala
public class DataNBP {


    private static List<BigDecimal> rateUSD = new ArrayList<>(100);
    private static List<BigDecimal> rateEUR = new ArrayList<>(100);
    private static List<BigDecimal> rateGBP = new ArrayList<>(100);


    public static List<BigDecimal> getRateUSD() {
        return rateUSD;
    }

    public static void setRateUSD(List<BigDecimal> rateUSD) {
        DataNBP.rateUSD = rateUSD;
    }

    public static List<BigDecimal> getRateEUR() {
        return rateEUR;
    }

    public static void setRateEUR(List<BigDecimal> rateEUR) {
        DataNBP.rateEUR = rateEUR;
    }

    public static List<BigDecimal> getRateGBP() {
        return rateGBP;
    }

    public static void setRateGBP(List<BigDecimal> rateGBP) {
        DataNBP.rateGBP = rateGBP;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public static void checkCurrency() {
        try {
            URL urlGPB = new URL("http://api.nbp.pl/api/exchangerates/rates/a/gbp/last/100");
            URLConnection urlConnectionGBP = urlGPB.openConnection();
            BufferedReader bufferedReaderGBP = new BufferedReader(new InputStreamReader(urlConnectionGBP.getInputStream()));
            URL urlEUR = new URL("http://api.nbp.pl/api/exchangerates/rates/a/eur/last/100");
            URLConnection urlConnectionEUR = urlEUR.openConnection();
            BufferedReader bufferedReaderEUR = new BufferedReader(new InputStreamReader(urlConnectionEUR.getInputStream()));
            URL urlUSD = new URL("http://api.nbp.pl/api/exchangerates/rates/a/usd/last/100");
            URLConnection urlConnectionUSD = urlUSD.openConnection();
            BufferedReader bufferedReaderUSD = new BufferedReader(new InputStreamReader(urlConnectionUSD.getInputStream()));
            JSONParser parser = new JSONParser();
            String line = "";


            while ((line = bufferedReaderGBP.readLine()) != null) {

                JSONObject jsonObject = (JSONObject) parser.parse(line);


                JSONArray jsonArray = (JSONArray) jsonObject.get("rates");
                DataNBP.getRateGBP().clear();
                for (Object o : jsonArray) {
                    JSONObject oo = (JSONObject) o;
                    DataNBP.getRateGBP().add(new BigDecimal(oo.get("mid").toString()));
                }


            }


            while ((line = bufferedReaderEUR.readLine()) != null) {

                JSONObject jsonObject = (JSONObject) parser.parse(line);


                JSONArray jsonArray = (JSONArray) jsonObject.get("rates");
                DataNBP.getRateEUR().clear();
                for (Object o : jsonArray) {
                    JSONObject oo = (JSONObject) o;
                    DataNBP.getRateEUR().add(new BigDecimal(oo.get("mid").toString()));
                }


            }

            while ((line = bufferedReaderUSD.readLine()) != null) {

                JSONObject jsonObject = (JSONObject) parser.parse(line);


                JSONArray jsonArray = (JSONArray) jsonObject.get("rates");
                DataNBP.getRateUSD().clear();
                for (Object o : jsonArray) {
                    JSONObject oo = (JSONObject) o;
                    DataNBP.getRateUSD().add(new BigDecimal(oo.get("mid").toString()));
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

    public static int isCurrencyRising(List<BigDecimal> list) {
        if (list.get(list.size() - 2).compareTo(list.get(list.size() - 1)) == 1) {
            return 1;
        } else if (list.get(list.size() - 2).compareTo(list.get(list.size() - 1)) == 0) {
            return 0;
        } else {
            return -1;
        }
    }
}
