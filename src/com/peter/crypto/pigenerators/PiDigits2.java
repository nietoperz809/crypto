package com.peter.crypto.pigenerators;

import java.math.BigDecimal;

/**
 * Created by Administrator on 4/28/2016.
 */
public class PiDigits2
{
    public static String getPiString (int start, int last)
    {
        String pi = getPiString(last);
        return pi.substring(start);
    }

    public static String getPiString (int digits)
    {
        return computePi(digits).toString().replace(".", "");
    }

    public static BigDecimal computePi (int digits)
    {
        int scale = digits + 5;
        BigDecimal arctan1_5 = arctan(5, scale);
        BigDecimal arctan1_239 = arctan(239, scale);
        BigDecimal pi = arctan1_5.multiply(BigDecimal.valueOf(4)).subtract(arctan1_239).multiply(BigDecimal.valueOf(4));
        return pi.setScale(digits, BigDecimal.ROUND_HALF_UP);
    }

    private static BigDecimal arctan (int inverseX, int scale)
    {
        BigDecimal result, numer, term;
        BigDecimal invX = BigDecimal.valueOf(inverseX);
        BigDecimal invX2
                = BigDecimal.valueOf((long) inverseX * inverseX);

        numer = BigDecimal.ONE.divide(invX,
                scale, BigDecimal.ROUND_HALF_EVEN);

        result = numer;
        int i = 1;
        do
        {
            numer = numer.divide(invX2, scale, BigDecimal.ROUND_HALF_EVEN);
            int denom = 2 * i + 1;
            term = numer.divide(BigDecimal.valueOf(denom), scale, BigDecimal.ROUND_HALF_EVEN);
            if ((i % 2) != 0)
            {
                result = result.subtract(term);
            }
            else
            {
                result = result.add(term);
            }
            i++;
        }
        while (term.compareTo(BigDecimal.ZERO) != 0);
        return result;
    }
}
