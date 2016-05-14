package com.peter.crypto.pigenerators;

import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * Parallel Pi hexdigits engine
 */
public class ParallelPiHexEngine
{
    static ForkJoinPool pool;

    static
    {
        pool = new ForkJoinPool(8); //ForkJoinPool.commonPool();
    }

    public static void main (String[] args) throws Exception
    {
        int pos = 800000;
        for (int s = 0; s < 10; s++)
        {
            long t = System.currentTimeMillis();
            int p = taskedPiDigit(pos);
            t = System.currentTimeMillis() - t;
            System.out.println(p);
            System.out.println("runtime: " + t);
        }
    }

    /**
     * Computes the nth digit of Pi in base-16.
     */
    public static int taskedPiDigit (int n) throws Exception
    {
        if (n < 0)
        {
            return -1;
        }
        n--;

        Future<Double> f1 = pool.submit(new Term(1, n, 4));
        Future<Double> f2 = pool.submit(new Term(4, n, -2));
        Future<Double> f3 = pool.submit(new Term(5, n, -1));
        Future<Double> f4 = pool.submit(new Term(6, n, -1));

        double x = f1.get()
                + f2.get()
                + f3.get()
                + f4.get();

        x = x - Math.floor(x);

        return (int) (x * 16);
    }

    static class Term implements Callable<Double>
    {
        int j, n, mult;

        Term (int j, int n, int mult)
        {
            this.j = j;
            this.n = n;
            this.mult = mult;
        }

        @Override
        public Double call () throws Exception
        {
            return piTerm(j, n) * mult;
        }

        public static double piTerm (int j, int n) throws Exception
        {
            Future<Double> s = pool.submit(new LeftSum(j, n));
            Future<Double> t = pool.submit(new RightSum(j, n));
            return s.get() + t.get();
        }


        static class LeftSum implements Callable<Double>
        {
            int j, n;

            LeftSum (int j, int n)
            {
                this.j = j;
                this.n = n;
            }

            @Override
            public Double call () throws Exception
            {
                // Calculate the left sum
                double s = 0;
                for (int k = 0; k <= n; ++k)
                {
                    int r = 8 * k + j;
                    s += powerMod(16, n - k, r) / (double) r;
                    s = s - Math.floor(s);
                }
                return s;
            }

            /**
             * Computes a^b mod m
             */
            private static long powerMod (int value, int power, long mod)
            {
                BigInteger v = BigInteger.valueOf(value);
                BigInteger x = v.modPow(BigInteger.valueOf(power),
                        BigInteger.valueOf(mod));
                return x.longValue();
            }
        }

        static class RightSum implements Callable<Double>
        {
            int j, n;

            RightSum (int j, int n)
            {
                this.j = j;
                this.n = n;
            }

            @Override
            public Double call () throws Exception
            {
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
                    t = newt;
                    ++k;
                }
                return t;
            }
        }
    }
}
