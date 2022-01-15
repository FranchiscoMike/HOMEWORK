package uz.pdp.raspiratory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public interface DataBase {

    //https://cbu.uz/oz/arkhiv-kursov-valyut/json/ – параметр кўрсатилмаган ушбу сўров
    // ёрдамида жорий санадаги валюта курслари берилади;

    //https://cbu.uz/oz/arkhiv-kursov-valyut/json/all/2018-12-11/  - ушбу сўров ёрдамида «2018-12-11»
    // санасидаги барча валюталар курслари берилади;

    //https://cbu.uz/oz/arkhiv-kursov-valyut/json/RUB/2019-01-01/ – ушбу сўров ёрдамида сана ва валюта
    // кодини кўрсатган ҳолда, «RUB» санадаги «2019-01-01» кодли валютанинг курси берилади;

    public static List<uz.pdp.model.Currency> currencies = new ArrayList<>();
    public static List<uz.pdp.model.Currency> currenciesByDate = new ArrayList<>();

   static void complate(){

       try {
           URL url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");

           URLConnection connection = url.openConnection();

           Gson gson = new GsonBuilder().setPrettyPrinting().create(); // chiroyli chiqaradi

           BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

          List latestCurrencies = gson.fromJson(reader,new TypeToken<List<Currency>>(){}.getType());


           System.out.println(latestCurrencies.size());


       } catch (MalformedURLException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

}
