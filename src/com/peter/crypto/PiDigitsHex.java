package com.peter.crypto;

import java.math.BigInteger;

/**
 * Created by Administrator on 4/29/2016.
 */
public class PiDigitsHex
{
    private final static BigInteger sixteen = BigInteger.valueOf(16);

    public static int[] piArray (int offset, int size)
    {
        int[] arr = new int[size];
        for (int s=0; s<size; s++)
        {
            arr[s] = piDigit (s+offset);
        }
        return arr;
    }

    public static byte packedPi8 (int offset)
    {
        int b = piDigit(offset);
        b = b<<4;
        b |= piDigit(offset+1);
        return (byte)b;
    }

    public static BigInteger packedPi (int offset, int length)
    {
        BigInteger b = BigInteger.valueOf(piDigit(offset));
        for (int s=1; s<length; s++)
        {
            b = b.multiply(sixteen);
            b = b.add (BigInteger.valueOf(piDigit(offset+s)));
            //System.out.println(b.bitCount());
        }
        return b;
    }

    /**
     * Computes the nth digit of Pi in base-16.
     */
    public static int piDigit (int n)
    {
        if (n < 0)
        {
            return -1;
        }

        n -= 1;
        double x = 4 * piTerm(1, n) - 2 * piTerm(4, n) -
                piTerm(5, n) - piTerm(6, n);
        x = x - Math.floor(x);

        //System.out.println(n+" -- "+(int)(x*16));
        return (int) (x * 16);
    }


    private static double piTerm (int j, int n)
    {
        // Calculate the left sum
        double s = 0;
        for (int k = 0; k <= n; ++k)
        {
            int r = 8 * k + j;
            s += powerMod(16, n - k, r) / (double) r;
            s = s - Math.floor(s);
        }

        // Calculate the right sum
        double t = 0;
        int k = n + 1;
        // Keep iterating until t converges (stops changing)
        while (true)
        {
            int r = 8 * k + j;
            double newt = t + Math.pow(16, n - k) / r;
            if (t == newt)
            {
                break;
            }
            else
            {
                t = newt;
            }
            ++k;
        }

        return s + t;
    }

    /**
     * Computes a^b mod m
     */
    private static long powerMod (int value, int power, int mod)
    {
        BigInteger v = BigInteger.valueOf(value);
        BigInteger x = v.modPow (BigInteger.valueOf(power),
                BigInteger.valueOf(mod));
        return x.longValue();
    }
}
