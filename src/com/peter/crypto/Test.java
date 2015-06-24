package com.peter.crypto;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public class Test
{
    static String rotxor (String in) throws UnsupportedEncodingException
    {
        byte[] b1 = in.getBytes("UTF-8");
        byte[] rot = CryptTools.rotateArrayLeft(b1, 1);
        byte[] xr = CryptTools.xor(b1, rot);
        System.out.println(Arrays.toString(b1));
        System.out.println(Arrays.toString(rot));
        System.out.println(Arrays.toString(xr));
        System.out.println("----------------");
        return new String (xr, "UTF-8");
    }
    
    public static void main (String[] args) throws Exception
    {
        String a = "abc";
        for (int s=0; s<100; s++)
        {
//            a = CryptTools.grayString(a);
//            a = CryptTools.
//            System.out.println(a);
        }
    }
}


/*

        String a = "3333";
        String s1 = rotxor (a);
        String s2 = rotxor (s1);
        String s3 = rotxor (s2);
        String s4 = rotxor (s3);
        String s5 = rotxor (s4);
        System.out.println(s1);
        System.out.println(s2);

*/
