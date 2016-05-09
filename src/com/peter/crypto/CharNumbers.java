package com.peter.crypto;

import java.math.BigInteger;

/**
 * Class to convert Numbers in Strings and vice versa
 */
public class CharNumbers
{
    private final String charset;

    /**
     * Constructor
     * @param cs  Charset to use. Length of cs is also the base
     */
    public CharNumbers (String cs)
    {
        charset = cs;
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

    /**
     * Converts number into string
     * @param number input number
     * @return the string
     */
    public String toString (BigInteger number)
    {
        if (number.compareTo(BigInteger.ZERO) == 0)
            return "0";
        String s = "";
        return nsHelper (number, charset.length(), s);
    }

    /**
     * Converts string to number
     * @param number input number
     * @return output string
     * @throws Exception if input contains illegal chars
     */
    public BigInteger toNumber (String number) throws Exception
    {
        BigInteger exp = BigInteger.ONE;
        BigInteger sum = BigInteger.ZERO;
        int len = number.length() - 1;
        int idx;
        char c;
        for (int n=len; n>=0; n--)
        {
            c = number.charAt(n);
            idx = charset.indexOf(c);
            if (idx < 0)
                throw new Exception ("Input Set Mismatch");
            sum = sum.add(BigInteger.valueOf(idx).multiply(exp));
            exp = exp.multiply(BigInteger.valueOf (charset.length()));
        }
        return sum;
    }

    /**
     * Test code
     * @param args not used
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        CharNumbers cn = new CharNumbers ("0123456789abcdef");
        
        String s = cn.toString(BigInteger.valueOf(65536));
        System.out.println(s);

        String ss="243f"; //6a8885a308d31319";
        //ss = new StringBuilder(ss).reverse().toString();
        BigInteger i = cn.toNumber(ss);
        System.out.println(i);
    }
}
