package com.company;

import java.util.*;
import java.util.HashSet;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        HashSet<Integer> resheto = new HashSet<Integer>();
        resheto=checkNum(resheto);
        System.out.println(randomNum());
    }

    public static long randomNum() {
        Random rand = new Random();
        String rNum="1";
        int bits = (1+rand.nextInt(20));
        switch (bits){
            case 1: return (2+rand.nextInt(2));
            case 2: return (5+2*rand.nextInt(2));
            case 3: return (11+2*rand.nextInt(2));
            default:
                for(int i = 0; i<bits;i++) {
                    rNum+=rand.nextInt(2);
                }
        }
        return Long.parseLong(rNum,2);
    }//генерация рандомного числа

    public static HashSet<Integer> checkNum(HashSet<Integer> resheto) {

        int index=0;
        double lim = 2000;              //диапазон генерации решета эратосфена
        int sqrtN=(int)Math.sqrt(lim);  //максимальное число, квадрат которого < 2000
        resheto.add(2); //для ускорения добавляем несколько базовых
        for(int i=3;i<lim;i+=2) {
            /*if((i%3!=0)&&(i%5!=0)&&(i%7!=0)){
                resheto.add(i);
            }*/
            resheto.add(i);
        }
        for(int i=3;i<=sqrtN;i++)
        {
            if(resheto.contains(i)){
                int finalI = i;
                resheto=resheto.stream()
                        .filter(x->x% finalI !=0 || x==finalI )
                        .collect(Collectors.toCollection(HashSet::new));
            }
        }
        return resheto;
    }

}
