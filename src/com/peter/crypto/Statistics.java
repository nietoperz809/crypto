package com.peter.crypto;

/**
 * To change this template use File | Settings | File Templates.
 */
public class Statistics
{
    /**
     * Calculates entropy of an array
     * @param arr The source array
     * @return entropy value
     */
    public static double entropy (byte[] arr)
    {
        double en = 0;
        for (byte anArr : arr)
        {
            if (anArr != 0)
            {
                double d = (double)(anArr & 0xff) / 255.0;
                double log = Math.log(d) / Math.log(2.0);
                en = en + d * log;
            }
        }
        en = -en ;
        if (en == 0.0)
            return 0;
        return en / arr.length * differentValues(arr);
    }

    /**
     * Returns a histogram of all bytes in an array
     * @param arr
     * @return
     */
    public static int[] histogram (byte[] arr)
    {
        int[] vals = new int[256];
        for (byte anArr : arr)
        {
            vals[anArr & 0xff]++;
        }
        return vals;
    }

    /**
     * Counts how many different values are in an array
     * @param arr
     * @return
     */
    public static int differentValues (byte[] arr)
    {
        int[] vals = histogram (arr);
        int diffs = 0;
        for (int anVals : vals)
        {
            if (anVals != 0)
                diffs++;
        }
        return diffs;
    }

    /**
     * Calculates the avarage of an array
     * @param arr
     * @return
     */
    public static double average (byte[] arr)
    {
        double av = 0;
        for (byte anArr : arr)
        {
             double d = (double)(anArr & 0xff);
             av = av + d;
        }
        return av / arr.length;
    }
}
