package com.peter.crypto;

import java.util.Arrays;

class BaseTranslator
{
    private static boolean checkInput (int[] digits, int inputBase,
                                         int outputBase, int outputLength)
    {
        // If inputBase < 2, outputBase < 2, outputLength < 1, or the input digits is empty, return null
        if ((inputBase < 2) || (outputBase < 2) || (outputLength < 1) || (digits == null))
        {
            return false;
        }

        // If digits[i] < 0 or digits[i] >= inputBase for any i, return null
        for (int digit : digits)
        {
            if ((digit < 0) || (digit >= inputBase))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Converts an array where the ith digit corresponds to (1 / inputBase)^(i + 1)
     * digits[i], return an array output of size outputLength where the ith digit
     * corresponds to (1 / outputBase)^(i + 1) * output[i].
     * <p>
     * Stated in another way, digits is the fractional part of a number
     * expressed in inputBase with the most significant digit first. The output is
     * the same number expressed in outputBase with the most significant digit first.
     * <p>
     * To implement, logically, you're repeatedly multiplying the number by
     * outputBase and chopping off the most significant digit at each iteration:
     * <p>
     * for (i < outputLength) {
     * 1. Keep a carry, initialize to 0.
     * 2. From RIGHT to LEFT
     * a. x = multiply the ith digit by outputBase and add the carry
     * b. the new ith digit is x % inputBase
     * c. carry = x / inputBase
     * 3. output[i] = carry
     * <p>
     * If digits[i] < 0 or digits[i] >= inputBase for any i, return null
     * If inputBase < 2, outputBase < 2, or outputLength < 1, return null
     *
     * @param digits     The input array to translate. This array is not mutated.
     * @param inputBase      The base that the input array is expressed in.
     * @param outputBase      The base to translate into.
     * @param outputLength The number of digits of precision the output should
     *                   have.
     * @return An array of size outputLength expressing digits in outputBase.
     */
    public static int[] convertFraction (int[] digits, int inputBase,
                                         int outputBase, int outputLength)
    {
        if (!checkInput(digits, inputBase, outputBase, outputLength))
            return null;

        long[] opDigits = new long[digits.length];
        for (int t = 0; t < digits.length; t++)
        {
            opDigits[t] = (long) digits[t];
        }

        int[] outputArray = new int[outputLength];

        for (int i = 0; i < outputLength; i++)
        {
            long carry = 0;
            for (int j = digits.length - 1; j >= 0; j--)
            {
                long x = (opDigits[j] * outputBase) + carry;
                opDigits[j] = x % inputBase;
                carry = x / inputBase;
            }
            outputArray[i] = (int) carry;
        }
        return outputArray;
    }

    private static final String digits = "0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * Converts a String of Digits (0...z) to an int-Array
     * @param in String of (0...z) characters
     * @return int Array
     */
    public static int[] toDigits (String in)
    {
        int[] res = new int[in.length()];
        for (int s=0; s<in.length(); s++)
        {
            res[s] = digits.indexOf(in.charAt(s));
        }
        return res;
    }

    /**
     * Converst a int array to String
     * @param in int array of values 0 ... 36
     * @return String of chars (0 ... z)
     */
    public static String toString (int[] in)
    {
        StringBuilder sb = new StringBuilder();
        for (int anIn : in)
        {
            sb.append(digits.charAt(anIn));
        }
        return sb.toString();
    }

    /**
     * Converts int array from one base to another
     * @param digits input array
     * @param inputBase base of input array
     * @param outputBase base of output array
     * @return converted int array
     */
    public static int[] convertInteger  (int[] digits, int inputBase,
                                  int outputBase /*, int outputLength*/)
    {
//        if (!checkInput(digits, inputBase, outputBase, outputLength))
//            return null;
        String digs = toString (digits);
        String s = Integer.toString(Integer.parseInt(digs, inputBase), outputBase);
        return toDigits(s);
    }

    public static void main (String[] args)
    {
        String in = "1.01";
        String[] parts = in.split("\\.");
        int[] integer  = toDigits(parts[0]);
        int[] fraction = toDigits(parts[1]);

        int[] la = convertInteger (integer, 2, 10);
        int[] lb = convertFraction (fraction, 2, 10, 10);
        System.out.println(Arrays.toString(la));
        System.out.println(Arrays.toString(lb));

//        int[] ll = toDigits("123f");
//        String ss = toString(ll);
//        System.out.println(Arrays.toString(ll));
//        System.out.println(ss);
//        int arr[] = {15,0,0,0};
//
//        int res[] = convertFraction(arr, 16, 10, 6);
//        System.out.println(Arrays.toString(res));
    }
}