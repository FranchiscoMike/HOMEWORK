package uz.pdp.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import uz.pdp.model.Currency;
import uz.pdp.raspiratory.DataBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Scanner;

public class CurrencyConverterService {

    public static void converter() {

        URL url = null;
        try {
            url = new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");
            URLConnection urlConnection = url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Type type = new TypeToken<List<Currency>>() {
            }.getType();


            List<Currency> latestCurrencies = gson.fromJson(reader, type);

            DataBase.currencies.addAll(latestCurrencies);

            //clone()
            List<Currency> myCurrencies = DataBase.currencies;


            int i=1;
            for (Currency myCurrency : myCurrencies) {
                System.out.println((i++)+". "+myCurrency.getCcy());
            }
            System.out.println("Choose: ");
            int option =new Scanner(System.in).nextInt();
            System.out.println("Enter amount :");
            double amount = new Scanner(System.in).nextDouble();
            String c1 = myCurrencies.get(option-1).getCcy();


            Currency currency1 = myCurrencies.get(option-1);

           // myCurrencies.remove(myCurrencies.get(option-1));

            System.out.println(c1+"  "+amount+" ->" +Double.parseDouble(currency1.getRate())*amount+" UZS");

//            int m=1;
//            for (Currency myCurrency : myCurrencies) {
//                System.out.println((m++)+". "+myCurrency.getCcy());
//            }
//            System.out.println("Choose: ");
//            int option2 =new Scanner(System.in).nextInt();
//
//            String c2 = myCurrencies.get(option2-1).getCcy();
//            Currency currency2 = myCurrencies.get(option2-1);
//            double convertedAmount = amount * Double.parseDouble(currency1.getRate()) / Double.parseDouble(currency2.getRate());
//            System.out.println( amount+" "+c1+" => "+convertedAmount+" "+c2);




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
