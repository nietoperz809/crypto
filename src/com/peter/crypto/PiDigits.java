package com.peter.crypto;

/**
 * Created by Administrator on 4/28/2016.
 */
public class PiDigits
{
    private static final int SCALE = 10000;
    private static final int ARRINIT = 2000;

    public static void main (String[] args)
    {
        System.out.println(PiDigits.pi_digits(10000));
    }

    public static String pi_digits (int digits)
    {
        digits = (int) (digits*3.5+1);
        StringBuffer pi = new StringBuffer();
        int[] arr = new int[digits + 1];
        int carry = 0;

        for (int i = 0; i <= digits; ++i)
        {
            arr[i] = ARRINIT;
        }

        for (int i = digits; i > 0; i -= 14)
        {
            int sum = 0;
            for (int j = i; j > 0; --j)
            {
                sum = sum * j + SCALE * arr[j];
                arr[j] = sum % (j * 2 - 1);
                sum /= j * 2 - 1;
            }

            pi.append(String.format("%04d", carry + sum / SCALE));
            carry = sum % SCALE;
        }
        return pi.toString();
    }
}