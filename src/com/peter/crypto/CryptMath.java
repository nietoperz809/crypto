package com.peter.crypto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 * To change this template use File | Settings | File Templates.
 */
public class CryptMath implements BigIntValues
{
    private static final String Digits36 = "0123456789abcdefghijklmnopqrstuvwxyz";

    private static String nsHelper (BigInteger number, int base, String str)
    {
        if (number.compareTo(BigInteger.ZERO) != 0)
        {
            str = nsHelper (number.divide(BigInteger.valueOf(base)), base, str);
            str += Digits36.charAt (number.mod(BigInteger.valueOf(base)).intValue());
        }
        return str;
    }

    /**
     * Returns string representation of a number
     * @param number Number to be converted
     * @param base Radix
     * @return The new string
     */
    public static String numberString (BigInteger number, int base)
    {
        if (number.compareTo(BigInteger.ZERO) == 0)
            return "0";
        String s = "";
        return nsHelper(number, base, s);
    }

    /**
      * Returns string representation of a number
      * @param number Number to be converted
      * @param base Radix
      * @return The new string
      */
     public static String numberString (long number, int base)
     {
        return numberString (BigInteger.valueOf(number), base);
     }


    public static BigInteger stringNumber (String number, int base)
    {
        BigInteger exp = BigInteger.ONE;
        BigInteger sum = BigInteger.ZERO;
        BigInteger err = BigInteger.valueOf(-1);
        int len = number.length() - 1;
        int idx;
        char c;
        for (int n=len; n>=0; n--)
        {
            c = number.charAt(n);
            idx = Digits36.indexOf(c);
            if (idx == -1 || idx >= base)
                return err;
            sum = sum.add(BigInteger.valueOf(idx).multiply(exp));
            /*
            System.out.println (number.charAt(n));
            System.out.println (exp);
            System.out.println (idx);
            System.out.println ("-----------");
            */
            exp = exp.multiply(BigInteger.valueOf(base));
        }
        return sum;
    }

    public static BigInteger stringNumber (String number)
    {
        return stringNumber (number, 36);
    }


    /**
     * Quersumme
     * @param number
     * @param base
     * @return
     */
    public static long numberStringQS (BigInteger number, int base)
    {
        long ret = 0;
        String num = numberString (number, base);
        for (int s=0; s<num.length(); s++)
        {
            char c = num.charAt(s);
            if (c >= 'a')
                c -= 39;
            ret = ret + c - '0';
        }
        return ret;
    }

    /**
     * Quersumme
     * @param number
     * @param base
     * @return
     */
    public static long numberStringQS (int number, int base)
    {
        return numberStringQS (BigInteger.valueOf(number), base);
    }

    /**
     * Test if a=b congruent mod n
     * @param a Value 1
     * @param b Value 2
     * @param n Divisor
     * @return true if they are
     */
    public static boolean congruence (BigInteger a, BigInteger b, BigInteger n)
    {
        return a.remainder(n).compareTo(b.remainder(n)) == 0;
    }

    /**
     * Calculates Jacobi symbol (a/b)
     * @param a value a
     * @param b value b, must be odd
     * @return Jacobio symbol 1, 0, -1
     * @throws Exception if b is even
     */
    private static int jacobiSymbol (BigInteger a, BigInteger b) throws Exception
    {
        int e = 0;
        int s = 1;

        if (!b.testBit(0))
        {
            throw new Exception ("b must be odd");
        }

        if (a.compareTo(ZERO) == 0 || a.compareTo(ONE) == 0)
            return a.intValue();
        while (a.mod(TWO).compareTo(ZERO) == 0)
        {
            a = a.divide(TWO);
            e++;
        }
        if (e%2 != 0 && (b.mod(EIGHT).compareTo(THREE) == 0 || b.mod(EIGHT).compareTo(FIVE) == 0 ))
        {
            s = -1;
        }
        if (b.mod(FOUR).compareTo(THREE) == 0 && a.mod(FOUR).compareTo(THREE) == 0)
        {
            s = -s;
        }
        if (a.compareTo(ONE) == 0)
            return s;
        return s * jacobiSymbol(b.mod(a), a);
    }

    /**
     * Calculates Legendre symbol (a/b)
     * @param a value a
     * @param b value b, must be odd
     * @return Jacobio symbol 1, 0, -1
     * @throws Exception if b is even
     */
    public static int legendreSymbol (BigInteger a, BigInteger b) throws Exception
    {
        if (!millerRabinPrimeTest(b))
        {
            throw new Exception ("b must be prime");
        }
        return jacobiSymbol (a,b);
    }

    /**
     * If gcd(x,y) == 1 then these numbers are relatively prime
     * @param x Number 1
     * @param y Number 2
     * @return true if x and 1 are relatively prime
     */
    private static boolean relativelyPrime(BigInteger x, BigInteger y)
    {
        return x.gcd(y).compareTo(ONE) == 0;
    }

    /**
     * This function returns a list of numbers from 1..xx that are relatively prime to xx
     * @param xx Base value
     * @return a list of so called reduced residues
     */
    private static BigInteger[] reducedSetOfResidues(BigInteger xx)
    {
        ArrayList<BigInteger> list = new ArrayList<BigInteger>();
        for (BigInteger s = ONE; s.compareTo(xx) <= 0; s = s.add(ONE))
        {
            if (relativelyPrime(s, xx))
            {
                list.add(s);
            }
        }
        BigInteger[] arr = new BigInteger[list.size()];
        return list.toArray(arr);
    }

    /**
     * Calculates Euler phi-function
     * @param x Source value
     * @return The PHI function
     */
    public static BigInteger totient(BigInteger x)
    {
        return BigInteger.valueOf(reducedSetOfResidues(x).length);
    }

    /**
     * Get list of all divisors of x
     * @param x Input value
     * @return List of divisors
     */
    public static BigInteger[] divisors(BigInteger x)
    {
        ArrayList<BigInteger> list = new ArrayList<>();
        BigInteger sqrt = sqrt(x);
        for (BigInteger n = ONE; n.compareTo(sqrt) <= 0; n = n.add(ONE))
        {
            if (x.mod(n).compareTo(ZERO) == 0)
            {
                list.add(n);
                list.add(x.divide(n));
            }
        }
        if (list.get(list.size()-1).equals(list.get(list.size()-2)))
            list.remove(list.size()-1);
        BigInteger[] arr = new BigInteger[list.size()];
        return list.toArray(arr);
    }

    /**
     * Get list of all divisors of x
     * @param x Input value
     * @return List of divisors
     */
    private static long[] divisors(long x)
    {
        ArrayList<Long> list = new ArrayList<Long>();
        long sqr = (long)Math.sqrt(x);
        for (long n = 1; n <= sqr; n++)
        {
            if (x%n == 0)
            {
                list.add(n);
                list.add(x/n);
            }
        }
        if (list.get(list.size()-1).equals(list.get(list.size()-2)))
            list.remove(list.size()-1);
        long[] arr = new long[list.size()];
        for (int s=0; s<list.size(); s++)
        {
            arr[s] = list.get(s);
        }
        return arr;
    }

    /**
     * Returns list of best two divisors
     * @param x Input value
     * @return Array of two elements
     */
    public static long[] bestTwoDivisors(long x)
    {
        long[] out = new long[2];
        long sqr = (long)Math.sqrt(x);
        for (long s=sqr; s>=1; s--)
        {
            //System.out.printf ("%d %d %d\n", s, x%s, x/s);
            if (x%s == 0)
            {
                out[0] = s;
                out[1] = x/s;
                break;
            }
        }
        return out;
    }

    /**
     * Returns sum of all divisors
     * @param x Source value
     * @return The sum
     */
    public static BigInteger sumDivisors(BigInteger x)
    {
        BigInteger sum = ZERO;
        BigInteger[] div = divisors(x);
        for (BigInteger aDiv : div)
        {
            sum = sum.add(aDiv);
        }
        return sum;
    }

    public static long sumDivisors(long x)
    {
        long sum = 0;
        long[] div = divisors(x);
        for (long aDiv : div)
        {
            sum = sum + aDiv;
        }
        return sum;
    }

    public static long sumArray(long[] x)
    {
        long sum = 0;
        for (long aDiv : x)
        {
            sum = sum + aDiv;
        }
        return sum;
    }

    /**
     * Returns sum of all digits of x
     * @param x    Source value
     * @param step Number of digits for a single value
     * @return The sum
     */
    public static BigInteger qSum(BigInteger x, int step)
    {
        BigInteger res = ZERO;
        BigInteger t = BigInteger.valueOf((long) Math.pow(10, step));

        while (x.compareTo(ZERO) != 0)
        {
            BigInteger n = x.mod(t);
            res = res.add(n);
            x = x.divide(t);
        }
        return res;
    }

    /**
     * Calculates alternating Qsum
     * For 3 that means: 123,471,023,473 --> 473 - 23 + 471 - 123 = 798
     * @param x    Input value
     * @param step number of digits of one element
     * @return The qsum
     */
    private static BigInteger alternatingQSum(BigInteger x, int step)
    {
        BigInteger res = ZERO;
        BigInteger t = BigInteger.valueOf((long) Math.pow(10, step));
        boolean flag = false;

        while (x.compareTo(ZERO) != 0)
        {
            BigInteger n = x.mod(t);
            if (!flag)
            {
                res = res.add(n);
                flag = true;
            }
            else
            {
                res = res.subtract(n);
                flag = false;
            }
            x = x.divide(t);
        }

        return res;
    }

    public static BigInteger positiveAlternatingQSum(BigInteger x, int alt)
    {
        return alternatingQSum(x, alt).abs();
    }

    /**
     * Calculates log2 of BigInteger
     * @param x The source
     * @return The log2 value
     */
    public static int log2(BigInteger x)
    {
        int res = 0;
        for (; ;)
        {
            x = x.shiftRight(1);
            if (x.compareTo(ZERO) == 0)
            {
                break;
            }
            res++;
        }
        return res;
    }

    /**
     * Returns log10(x) by simply counting the digits
     * @param x Source value
     * @return log10
     */
    public static int log10(BigInteger x)
    {
        return x.toString().length();
    }

    /**
     * Tests if BigInteger is a power of 2
     * @param x BigInteger to test
     * @return true if x is perfect power of 2
     */
    public static boolean isPowerOfTwo(BigInteger x)
    {
        return x.bitCount() == 1 && x.compareTo(ONE) != 0;
    }

    /**
     * Calculates faculty from BigInteger
     * @param x The source value
     * @return The faculty
     */
    public static BigInteger faculty(BigInteger x)
    {
        BigInteger res = ONE;
        while (x.compareTo(ONE) > 0)
        {
            res = res.multiply(x);
            x = x.subtract(ONE);
        }
        return res;
    }

    /**
     * Calculates SQRT from BigInteger the Newton way
     * @param x Input value
     * @return SQRT(x)
     */
    public static BigInteger sqrt(BigInteger x)
    {
        if (x.compareTo(ONE) <= 0)
        {
            return x;
        }

        int s = x.bitCount() * 2 + 1;
        BigInteger g0 = ONE.shiftLeft(s);
        BigInteger g1 = (g0.add(x.shiftRight(s))).shiftRight(1);

        while (g1.compareTo(g0) < 0)
        {
            g0 = g1;
            g1 = (g0.add(x.divide(g0))).shiftRight(1);
        }
        return g0;
    }

    /**
     * Tests if BigInteger is a square
     * @param x Value to test
     * @return true if x is a square
     */
    public static boolean isSquare(BigInteger x)
    {
        BigInteger root = sqrt(x);
        BigInteger test = root.multiply(root);
        return test.compareTo(x) == 0;
    }

    public static double squareFibonacci(long n)
    {
        double sqrfive = Math.sqrt(5);
        return Math.floor(1 / sqrfive * Math.pow((1 + sqrfive) / 2, n) + 0.5);
    }

    /**
     * Calculates Fibonacci numbers
     * @param n Input value
     * @return The Fibonacci number
     */
    public static BigInteger loopFibonacci(long n)
    {
        BigInteger zero = ZERO;
        BigInteger one = ONE;
        if (n == 0)
        {
            return zero;
        }
        if (n == 1)
        {
            return one;
        }
        BigInteger i = one;
        BigInteger a = zero;
        BigInteger b = one;
        while (i.compareTo(BigInteger.valueOf(n)) == -1)
        {
            BigInteger temp = b;
            b = b.add(a);
            a = temp;
            i = i.add(one);
        }
        return b;
    }

    /**
     * Computes the GCD of a and b
     * @param a Input a
     * @param b Input b
     * @return The GCD
     */
    public static long gcd(long a, long b)
    {
        // a must be > b
        if (a <= b)
        {
            long x = a;
            a = b;
            b = x;
        }
        while (b != 0)
        {
            long r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public static long lcm (long a, long b)
    {
        return a*b/gcd(a,b);
    }

    /**
     * Computes GCD for BigIntegers
     * @param a Input a
     * @param b Input b
     * @return The GCD
     */
    public static BigInteger gcd (BigInteger a, BigInteger b)
    {
        return a.gcd(b);
    }

    public static BigInteger lcm (BigInteger a, BigInteger b)
    {
        return a.multiply(b).divide(gcd(a,b));
    }

    /**
     * Checks a number if it is prime the fermat way
     * @param n      Number to test
     * @param trials Security counter, the bigger the better is the test
     * @return true if prime
     */
    public static boolean fermatPrimeTest (BigInteger n, int trials)
    {
        if (n.compareTo(THREE) < 0)
        {
            return false;
        }
        if (!n.testBit(0))   // Even?
        {
            return false;
        }
        for (int s = 0; s < trials; s++)
        {
            BigInteger rnd = new BigInteger(n.bitLength(), new Random());
            if (rnd.modPow(n.subtract(ONE), n).intValue() != 1)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates mersenne number
     * @param n The input value
     * @return Mersenne number
     */
    public static BigInteger mersenne(int n)
    {
        return TWO.pow(n).subtract(ONE);
    }

    /**
     * Checks if a given number is a prime number
     * @param p Number to test
     * @return TRUE if number is prime
     */
    public static boolean millerRabinPrimeTest(long p)
    {
        if (p == 2) // two is prime
        {
            return true;
        }
        else if (p <= 1 || ((p & 1) == 0)) // 0, 1 and even numbers are not prime
        {
            return false;
        }

        Random rnd = new Random();
        BigInteger bg = BigInteger.valueOf(p);

        // Find a and m such that m is odd and this == 1 + 2**a * m
        BigInteger thisMinusOne = bg.subtract(ONE);
        BigInteger m = thisMinusOne;
        int a = m.getLowestSetBit();
        m = m.shiftRight(a);

        for (int i = 0; i < 50; i++)
        {
            // Generate a uniform random on (1, this)
            BigInteger b;
            do
            {
                b = new BigInteger(bg.bitLength(), rnd);
            }
            while (b.compareTo(ONE) <= 0 || b.compareTo(bg) >= 0);

            int j = 0;
            BigInteger z = b.modPow(m, bg);
            while (!((j == 0 && z.equals(ONE)) || z.equals(thisMinusOne)))
            {
                if (j > 0 && z.equals(ONE) || ++j == a)
                {
                    return false;
                }
                z = z.modPow(TWO, bg);
            }
        }
        return true;
    }

    public static boolean millerRabinPrimeTest(BigInteger p)
    {
        // Two is prime
        if (p.compareTo(TWO) == 0)
        {
            return true;
        }
        // Even numbers, one and zero are not prime
        if (p.testBit(0) == false || p.compareTo(ONE) == 0 || p.compareTo(ZERO) == 0)
        {
            return false;
        }

        Random rnd = new Random();

        // Find a and m such that m is odd and this == 1 + 2**a * m
        BigInteger thisMinusOne = p.subtract(ONE);
        BigInteger m = thisMinusOne;
        int a = m.getLowestSetBit();
        m = m.shiftRight(a);

        for (int i = 0; i < 50; i++)
        {
            // Generate a uniform random on (1, this)
            BigInteger b;
            do
            {
                b = new BigInteger(p.bitLength(), rnd);
            }
            while (b.compareTo(ONE) <= 0 || b.compareTo(p) >= 0);

            int j = 0;
            BigInteger z = b.modPow(m, p);
            while (!((j == 0 && z.equals(ONE)) || z.equals(thisMinusOne)))
            {
                if (j > 0 && z.equals(ONE) || ++j == a)
                {
                    return false;
                }
                z = z.modPow(TWO, p);
            }
        }
        return true;
    }


    public static BigInteger[] primeFilter (BigInteger[] in)
    {
        ArrayList<BigInteger> list = new ArrayList<BigInteger>();
        for (int s=0; s<in.length; s++)
        {
            if (millerRabinPrimeTest (in[s]))
                list.add(in[s]);
        }
        BigInteger[] out = new BigInteger[list.size()];
        return list.toArray(out);
    }

    /**
     * Counts up to the next prime number if p is not prime itself
     * @param p Number to check
     * @return p+x that is the first prime number
     */
    public static long getNextPrimeAbove (long p)
    {
        while (!millerRabinPrimeTest(p))
        {
            p++;
        }
        return p;
    }

    public static BigInteger getNextPrimeAbove(BigInteger p)
    {
        while (!millerRabinPrimeTest(p))
        {
            p = p.add(ONE);
        }
        return p;
    }
}
