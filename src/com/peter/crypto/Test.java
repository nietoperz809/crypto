package com.peter.crypto;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Test
{

    public static void main(String[] args) throws Exception
    {
        String in = 
            "Weil Frieden bedeutet, dass der Staat Israel seine Grenzen definieren "
                + "und fixieren müsste." +
            "Damit ist der Traum von Erez zu Ende.";
        NumberField n = NumberFieldFactory.squareFromString(in);
        for (int s = 0; s < in.length(); s++)
        {
            n = n.reverse();
            n = n.rotateColumnsDownIncremental();
            n = n.transpose();
        }
        
        String flat = n.asFlatString();
        NumberField n2 = NumberFieldFactory.squareFromString(flat);
        for (int s = 0; s < in.length(); s++)
        {
            n2 = n2.transpose();
            n2 = n2.rotateColumnsUpIncremental();
            n2 = n2.reverse();
        }
        
        System.out.println(n.toCharString());
        System.out.println(in);
        System.out.println(flat);
        System.out.println(n2.asFlatString());
    }
}


/*
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

 String a = "3333";
 String s1 = rotxor (a);
 String s2 = rotxor (s1);
 String s3 = rotxor (s2);
 String s4 = rotxor (s3);
 String s5 = rotxor (s4);
 System.out.println(s1);
 System.out.println(s2);

 */
