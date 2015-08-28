package com.peter.crypto;

import java.math.BigInteger;

/**
 * Created by IntelliJ IDEA.
 */
public class CharNumbers
{
    private final String charset;

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

    public String toString (BigInteger number)
    {
        if (number.compareTo(BigInteger.ZERO) == 0)
            return "0";
        String s = "";
        return nsHelper (number, charset.length(), s);
    }

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
    
    public static void main(String[] args) throws Exception
    {
        CharNumbers cn = new CharNumbers ("0123456789abcdef");
        
        String s = cn.toString(BigInteger.valueOf(65536));
        System.out.println(s);
        
        BigInteger i = cn.toNumber("0");
        System.out.println(i);
    }
}
