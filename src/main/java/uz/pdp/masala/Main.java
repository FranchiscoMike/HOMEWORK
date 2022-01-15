package uz.pdp.masala;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        int c=0;
        for (int i = 1000; i <10000 ; i++) {
            String a = i+"";
            if (!a.contains(0+"")){

                Set set = new HashSet();
                char[] chars = a.toCharArray();
                for (char aChar : chars) {
                    set.add(aChar);
                }
                if (set.size()>3){
                    c++;
                }
            }
        }
        System.out.println(c);
    }
}
