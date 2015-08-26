package com.peter.crypto;

import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 */
public class CharNumbers
{
    private final String charset;
    private final int cslen;

    public CharNumbers (String cs)
    {
        charset = cs;
        cslen = cs.length();
    }

    private String nsHelper (BigInteger number, int base, String str)
    {
        if (number.compareTo(BigInteger.ZERO) != 0)
        {
            str = nsHelper (number.divide(BigInteger.valueOf(base)), base, str);
            str += charset.charAt (number.mod(BigInteger.valueOf(base)).intValue());
        }
        return str;
    }

    public String numberString (BigInteger number)
    {
        if (number.compareTo(BigInteger.ZERO) == 0)
            return "0";
        String s = "";
        return nsHelper (number, cslen, s);
    }

    public String numberString (long number)
    {
        return numberString (BigInteger.valueOf(number));
    }
    
    public BigInteger stringNumber (String number)
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
            idx = charset.indexOf(c);
            if (idx == -1 || idx >= cslen)
                return err;
            sum = sum.add(BigInteger.valueOf(idx).multiply(exp));
            exp = exp.multiply(BigInteger.valueOf (cslen));
        }
        return sum;
    }
    
    public static void main(String[] args)
    {
        CharNumbers cn = new CharNumbers ("01");
        String s = cn.numberString(BigInteger.valueOf(1234));
        System.out.println(s);
    }
}
