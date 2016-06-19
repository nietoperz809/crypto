package com.peter.crypto;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 17296950165164170047139891882388300467691593 --> HARD!!!
 * Brent is fastest!
 */
public class Factorizer implements BigIntValues
{
    /**
     * List of first 1001 primes
     */
    private static final long firstPrimes[] =
    {
        2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61,
        67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137,
        139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199,
        211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277,
        281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359,
        367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439,
        443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521,
        523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607,
        613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683,
        691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773,
        787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863,
        877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967,
        971, 977, 983, 991, 997, 1009
    };

    /**
     * Seeks prime factor in firstPrime list
     *
     * @param x Number to factor
     * @return Prime if found or <b>null</b> if not found
     */
    public static BigInteger isSmallPrimeDivisor(BigInteger x)
    {
        for (long firstPrime : firstPrimes)
        {
            BigInteger test = BigInteger.valueOf(firstPrime);
            if (x.mod(test).compareTo(ZERO) == 0)
            {
                return test;
            }
        }
        return null;
    }

    public static List<BigInteger> getFirstPrimeDivisors(BigInteger x)
    {
        List<BigInteger> list = new ArrayList<>();
        for (long firstPrime : firstPrimes)
        {
            BigInteger test = BigInteger.valueOf(firstPrime);
            if (x.mod(test).compareTo(ZERO) == 0)
            {
                list.add(test);
            }
        }
        return list;
    }
    
    
    /**
     * Gets prime factor by trial division
     *
     * @param x Number to factor
     * @return Prime if found or the number itself if not found
     */
    public static BigInteger getTrialDivisor(BigInteger x)
    {
        BigInteger s;
        BigInteger end = CryptMath.sqrt(x);
        if (end.compareTo(THREE) <= 0)
            end = x;
        for (s = TWO; s.compareTo(end) < 0; s = s.add(ONE))
        {
            BigInteger mod = x.mod(s);
            if (mod.compareTo(ZERO) == 0)
            {
                return s;
            }
        }
        return x;
    }

    /**
     * Decomposits a number int primes by trial division
     *
     * @param x The number
     * @return A list of all prime factors
     */
    public static BigInteger[] factByTrialDivision(BigInteger x)
    {
        if (x.equals(BigInteger.ZERO))
            return new BigInteger[0];
        ArrayList<BigInteger> list = new ArrayList<>();
        for (;;)
        {
            BigInteger div = isSmallPrimeDivisor(x);
            if (div == null)
            {
                div = getTrialDivisor(x);
            }
            if (div.compareTo(ONE) == 0)
            {
                break;
            }
            list.add(div);
            x = x.divide(div);
        }
        BigInteger[] arr = new BigInteger[list.size()];
        return list.toArray(arr);
    }
    
    /**
     * Finds prime factor using the BRENT method
     *
     * @param n Number to factor
     * @return A prime number that is a prime factor of n
     */
    public static BigInteger brentFactor(BigInteger n)
    {
        if (n.bitCount() == 1)
        {
            return n;
        }
        if (CryptMath.isPowerOfTwo(n))
        {
            return n;
        }
        if (CryptMath.millerRabinPrimeTest(n))
        {
            return n;
        }

        BigInteger xi = TWO;
        BigInteger xm = TWO;
        BigInteger s;
        for (int i = 1;; i++)
        {
            xi = xi.pow(2).add(ONE).mod(n);
            s = (xi.subtract(xm)).gcd(n);
            if (s.compareTo(ONE) != 0 && s.compareTo(n) != 0)
            {
                return s;
            }
            if ((i & (i - 1)) == 0)
            {
                xm = xi;
            }
            /*
             if (i%10000 == 0)     // peter!!!
             {
             xm = xm.add (CryptMath.getNextPrimeAbove(n));
             }
             */
        }
    }

    /**
     * Decomposit a number into primes using BRENT method
     *
     * @param x Number to factor
     * @return List of all prime factors
     */
    public static BigInteger[] factByBrent(BigInteger x)
    {
        ArrayList<BigInteger> list = new ArrayList<>();
        for(;;) // first check small primes
        {
            BigInteger div = isSmallPrimeDivisor(x);
            if (div == null)
                break;
            list.add(div);
            x = x.divide(div);
        }
        for (;;) // then use Brent
        {
            BigInteger div = brentFactor(x);
            if (div.compareTo(ONE) == 0)
            {
                break;
            }
            list.add(div);
            x = x.divide(div);
        }
        return list.toArray(new BigInteger[list.size()]);
    }
    
    /**
     * Omega function, 
     * see: https://en.wikipedia.org/wiki/Prime_factor#Omega_function
     * @param from first number
     * @param to last number
     * @return array of number of prime divisors
     */
    public static int[] omega (int from, int to)
    {
        int size = to - from + 1;
        int[] res = new int[size];
        for (int s=0; s<size; s++)
        {
            BigInteger bi = BigInteger.valueOf(s+from);
            res[s] = factByBrent (bi).length;
        }
        return res;
    }
    
    public static void main(String[] args) throws IOException
    {
        //BigInteger b1 = new BigInteger("111111111111111111111111111111111111111111111111111111111111111111111111111");
        //BigInteger b1 = new BigInteger("111111111111111111111111111111111111111111111111");
        //BigInteger b1 = new BigInteger(""+(5*5*5));

        int[] a = omega (30, 60);
        System.out.println(Arrays.toString(a));
        
        System.out.print(" ");
        for (int s = 30; s<=60; s++)
        {
            BigInteger bi = BigInteger.valueOf(s);
            BigInteger[] prim = factByTrialDivision (bi);
            System.out.print(prim.length + ", ");
        }
        
        
        // PT2M26.022S
//        // [55351781210701, 312491301758148310989239616493]
//        BigInteger b1 = new BigInteger("17296950165164170047139891882388300467691593");
//
//        //BigInteger b1 = new BigInteger("123456");
//        Instant start = Instant.now();
//        BigInteger[] list = factByBrent(b1);
//        Instant end = Instant.now();
//        System.out.println(Duration.between(start, end));
//        System.out.println(Arrays.toString(list));

//        start = Instant.now();
//        list = factByRho(b1);
//        end = Instant.now();
//        System.out.println(Duration.between(start, end));
//        System.out.println(Arrays.toString(list));
//    
    }
}

/*
        byte[] arr = new byte[10000000];
        for (int s=2; s<10000002; s++)
        {
            BigInteger n = BigInteger.valueOf(s);
            BigInteger b = Factorizer.getTrialDivisor(n);
            if (b.equals(n))
                arr[s-2]=(byte)'p';
            else
                arr[s-2]=(byte)'-';
        }
        IO.writeFile("c:\\primes.txt", arr);

*/