package com.company;
import java.math.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Diffie {

    public static void algoritm(int p, int g) {
        int a,b,A,B,K1,K2;
        a=RandSimpleNumber.randSimpleNumber();
        System.out.println("Aлиса сгенерировала число "+a);
        b=RandSimpleNumber.randSimpleNumber();
        System.out.println("Боб сгенерировал число "+b);
        A=formula(g,a,p);
        B=formula(g,b,p);
        System.out.println("A= "+A);
        System.out.println("B= "+B);
        K1=formula(formula(g,b,p),a,p);
        K2=formula(formula(g,a,p),b,p);
        System.out.println("K1= "+K1);
        System.out.println("K2= "+K2);
    }
    public static int formula(int x, int y, int z) {
        BigInteger X = new BigInteger(Integer.toString(x));
        BigInteger Y = new BigInteger(Integer.toString(y));
        BigInteger Z = new BigInteger(Integer.toString(z));
        return (X.modPow(Y,Z)).intValue();
    }
}