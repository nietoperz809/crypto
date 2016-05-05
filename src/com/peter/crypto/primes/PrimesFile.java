/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.crypto.primes;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses "primes.zip" file
 * @author Administrator
 */
public class PrimesFile
{
    private static long max;
    private static String filename = "primes.txt";
    
    /**
     * Static initializer
     * Get number of stored primes in file
     */
    static
    {
        try
        {
            InputStream in = ClassLoader.getSystemResourceAsStream(filename);
            max = in.available();
            System.out.println("Loaded "+max+" primes");
        }
        catch (Exception ex)
        {
            System.out.println("Could not load primes file");
            max = 0;
        }
    }
    
    public static List<BigInteger> factorize (BigInteger x)
    {
        ArrayList<BigInteger> list = new ArrayList<>();
        for(;;) 
        {
            BigInteger a = isSmallPrimeDivisor(x);
            if (a == null)
                break;
            list.add(a);
            x = x.divide(a);
        }
        return list;
    }
    
    /**
     * Find one prime divisor
     * @param x Number to seek for prime divisors
     * @return a Prime divisor or null (if none found)
     */
    public static BigInteger isSmallPrimeDivisor(BigInteger x)
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
            if (c == '-')
                continue;
            BigInteger test = BigInteger.valueOf(n+2);
            if (x.mod(test).compareTo(BigInteger.ZERO) == 0)
            {
                return test;
            }
        }
        return null;
    }
    
    /**
     * Get size of primes file
     * @return the size
     * @throws Exception 
     */
    public static long getMax() throws Exception
    {
        return max;
    }
    
    /**
     * Check if given number is prime
     * @param x input
     * @return true is number is prime else false
     * @throws Exception 
     */
    public static boolean isPrime (long x) throws Exception
    {
        assert x > 1 && x <= max;
        InputStream in = ClassLoader.getSystemResourceAsStream("primes.txt");
        in.skip(x-2);
        return in.read() == (int)'p';
    }
    
    /**
     * Check if given number is prime
     * @param x input
     * @return true is number is prime else false
     * @throws Exception 
     */
    public static boolean isPrime (BigInteger x) throws Exception
    {
        return isPrime (x.longValue());
    }
    
    // Test
    public static void main(String[] args) throws Exception
    {
        //System.out.println(isPrime(29));
        //System.out.println(isSmallPrimeDivisor(BigInteger.valueOf(18)));
        List<BigInteger> l = factorize(new BigInteger("123456")); // [2, 2, 2, 2, 2, 2, 3, 643]
        System.out.println(l);
    }
  
}
