package com.company;

import java.util.*;
import java.util.HashSet;
import java.math.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class RandSimpleNumber {

    public static int randSimpleNumber() {
        HashSet<Integer> resheto = new HashSet<Integer>();
        resheto=createResheto(resheto);
        int num = randomNum();
        System.out.println("Сгенерировано первоначальное число: "+num);
        num=checkNum(resheto,num);
        if(rabinMiller(num)){
            System.out.println("Test is passed; Simple num is "+num);
        }
        return num;
    }
    //m-1=(2^s)*t    t-нечет
    private static boolean rabinMiller(int num){
        //num = 55681;
        int t=-1;
        int s=2;
        int i=1;
        while (s<num-1){
            s=(int)Math.pow(2.0,i);
            if((num-1)%s==0) t=(num-1)/s;
            i++;
        }
        if(t!=-1)System.out.println("T найдено: "+t); else System.out.println("Не удалось подобрать T");
        System.out.println(checkOne(t,num));


        //t=(int)Math.pow(2.0,s)/(num-1);
        return true;
    }
    //(a^t)%m=1 -> простое
    private static boolean checkOne(int t, int num) {
        BigInteger M = new BigInteger(Integer.toString(num));
        BigInteger M1 = new BigInteger(Integer.toString(num-1));
        BigInteger A = new BigInteger(Integer.toString(3));
        BigInteger inc = new BigInteger(Integer.toString(1));
        BigInteger[] numsA = new BigInteger[5];
        int rounds = 5;
        while (rounds != 0){
            A=A.add(inc);
            //System.out.println("------------- "+A.compareTo(M1));
            if(A.equals(M1)) {
                System.out.println("Not fount a");
                return false;
            }else if(Integer.parseInt((A.pow(t)).mod(M).toString())==1) {
                numsA[5-rounds]=A;
                rounds--;
            }
        }
        for(BigInteger n:numsA) {
            System.out.println(n);
        }
        return true;
    }
    //или существует такое r, что ((a^t)^(2^r))%m=-1 - тогда простое.


    private static int checkNum(HashSet<Integer> resheto, int num){
        if(resheto.contains(num)) return num;
        else if(num<2000) {
            do{
                num+=2;
            }while (!resheto.contains(num));
            return num;
        }else{
            while (checkResheto(resheto, num)){
                num+=2;
            }
            System.out.println(num+" Вероятно простое "+!checkResheto(resheto, num));
            return num;
        }
    }

    private static boolean checkResheto(HashSet<Integer> resheto, int num){
        return resheto.stream().anyMatch( x->(num%x==0 && num!=x));
    }

    private static int randomNum() {
        Random rand = new Random();
        String rNum="1";
        int bits = (1+rand.nextInt(21));
        switch (bits){
            case 1: return (2+rand.nextInt(2));
            case 2: return (5+2*rand.nextInt(2));
            case 3: return (11+2*rand.nextInt(2));
            default:
                for(int i = 0; i<bits;i++) {
                    rNum+=rand.nextInt(2);
                }
                rNum+="1";
        }
        return Integer.parseInt(rNum,2);
    }//генерация рандомного числа

    private static HashSet<Integer> createResheto(HashSet<Integer> resheto) {

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
                        .filter(x->x%finalI !=0 || x==finalI )
                        .collect(Collectors.toCollection(HashSet::new));
            }
        }
        return resheto;
    }
}
