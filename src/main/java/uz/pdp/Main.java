package uz.pdp;

import uz.pdp.services.CurrencyConverterService;
import uz.pdp.services.CurrencyService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int option =-1;

        while (option!=0){

            System.out.println("Welcome to the Central Bank of Ubekistan!");
            System.out.println("\t\t\t\tServices :");
            System.out.println("1. Show All currencies around the world.");
            System.out.println("2. Show All currencies by the date you wanted.");
            System.out.println("3. Show  special currency you wanted in your chosen time.");
            System.out.println("4. Currency converter.");
            System.out.println("NOTICE : All currencies are according to the Central Bank of Uzbekistan");

            System.out.println("Choose : ");
            try {
                option=new Scanner(System.in).nextInt();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            switch (option) {
                case 1:{
                    CurrencyService.showAll(); // now
                }break;
                case 2:{
                    CurrencyService.showByDate();
                }break;
                case 3:{
                    CurrencyService.showByName();
                }break;
                case 4:{
                    CurrencyConverterService.converter();
                }break;
                default:{
                    System.out.println("Wrong option is chosen!");
                }

            }
            System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

        }


    }
}
