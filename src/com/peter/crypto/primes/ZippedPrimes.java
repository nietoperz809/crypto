/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.crypto.primes;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 *
 * @author Administrator
 */
public class ZippedPrimes
{
    private static long max;
    private static String filename = "primes.txt";
    static
    {
        InputStream in = ClassLoader.getSystemResourceAsStream(filename);
        try
        {
            max = in.available();
        }
        catch (IOException ex)
        {
            max = 0;
        }
    }
    
    public static BigInteger isFirstPrimeDivisor(BigInteger x)
    {
        InputStream in = ClassLoader.getSystemResourceAsStream(filename);
        for (long n=0; n<max; n++)
        {
            char c;
            try
            {
                c = (char)in.read();
            }
            catch (IOException ex)
            {
                return null;
            }
            if (c == '_')
                continue;
            BigInteger test = BigInteger.valueOf(n+2);
            if (x.mod(test).compareTo(BigInteger.ZERO) == 0)
            {
                return test;
            }
        }
        return null;
    }
    
    public static long numPrimes() throws Exception
    {
        return max;
    }
    
    public static boolean isPrime (long x) throws Exception
    {
        assert x >= 0 && x <= max;
        if (x == 1)
            return true;
        InputStream in = ClassLoader.getSystemResourceAsStream("primes.txt");
        in.skip(x-2);
        return in.read() == (int)'p';
    }
    
    public static boolean isPrime (BigInteger x) throws Exception
    {
        return isPrime (x.longValue());
    }
    
    public static void main(String[] args) throws Exception
    {
        //System.out.println(isPrime(29));
        System.out.println(isFirstPrimeDivisor(BigInteger.valueOf(17)));
    }
  
}
