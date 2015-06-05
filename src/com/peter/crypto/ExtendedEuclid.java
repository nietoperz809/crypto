package com.peter.crypto;

/**
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedEuclid
{
    class Result
    {
        long factor1;
        long factor2;
        long gcd; // gcd
    }

    private boolean isEven(long x)
    {
        return (x & 0x01) == 0;
    }

    private boolean isOdd(long x)
    {
        return !isEven(x);
    }

    private Result calc (long u, long v)
    {
        Result res = new Result();
        long k;
        long swap;
        long t1;
        long t2;
        long t3;

        if (u < v)
        {
            swap = u;
            u = v;
            v = swap;
        }
        for (k = 0; isEven(u) && isEven(v); ++k)
        {
            u >>>= 1;
            v >>>= 1;
        }
        res.factor1 = 1;
        res.factor2 = 0;
        res.gcd = u;
        t1 = v;
        t2 = u - 1;
        t3 = v;
        do
        {
            do
            {
                if (isEven(res.gcd))
                {
                    if (isOdd(res.factor1) || isOdd(res.factor2))
                    {
                        res.factor1 += v;
                        res.factor2 += u;
                    }
                    res.factor1 >>>= 1;
                    res.factor2 >>>= 1;
                    res.gcd >>>= 1;
                }
                if (isEven(t3) || res.gcd < t3)
                {
                    swap = res.factor1;
                    res.factor1 = t1;
                    t1 = swap;

                    swap = res.factor2;
                    res.factor2 = t2;
                    t2 = swap;

                    swap = res.gcd;
                    res.gcd = t3;
                    t3 = swap;
                }
            } while (isEven(res.gcd));

            while (res.factor1 < t1 || res.factor2 < t2)
            {
                res.factor1 += v;
                res.factor2 += u;
            }

            res.factor1 -= t1;
            res.factor2 -= t2;
            res.gcd -= t3;
        } while (t3 > 0);

        while (res.factor1 >= v && res.factor2 >= u)
        {
            res.factor1 -= v;
            res.factor2 -= u;
        }
        res.factor1 <<= k;
        res.factor2 <<= k;
        res.gcd <<= k;

        return res;
    }

    public static void test(long u, long v) throws Exception
    {
        ExtendedEuclid ext = new ExtendedEuclid();

        if (u <= 0 || v <= 0)
        {
            throw new Exception("Arguments must be positive!");
        }

        // warning: u and v will be swapped if u < v
        Result res = ext.calc(u, v);
        System.out.printf("(%d|%d) --> ", u, v);
        System.out.printf("%d * %d - %d * %d = %d ", res.factor1, u, res.factor2, v, res.gcd);
        if (res.gcd == 1)
        {
            System.out.printf("the inverse of %d mod %d is %d", v, u, u - res.factor2);
        }
        System.out.println();
    }
}
