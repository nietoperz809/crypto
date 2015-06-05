package com.peter.crypto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class Test
{
    public static char processGen (String in)
    {
        char c1 = in.charAt(0);
        char c2 = in.charAt(1);
        
        if (c1 == c2)
        {
            return c1;
        }
        if (Character.isUpperCase(c1))
        {
            return c1;
        }    
        if (Character.isUpperCase(c2))
        {
            return c2;
        }    
        return c1;
    }
    
    public static void processGen2 (String s1, String s2)
    {
        String n1 = ""+s1.charAt(0)+s2.charAt(0);
        String n2 = ""+s1.charAt(0)+s2.charAt(1);
        String n3 = ""+s1.charAt(1)+s2.charAt(0);
        String n4 = ""+s1.charAt(1)+s2.charAt(1);
        char c1 = processGen(n1);
        char c2 = processGen(n2);
        char c3 = processGen(n3);
        char c4 = processGen(n4);
        System.out.println (""+c1+c2+c3+c4);
    }
    
    public static void main (String[] args) throws Exception
    {
        processGen2("aa","AA");    
        processGen2("bb","Bb");    
        processGen2("Dd","Dd");    
        processGen2("Ee","ee");    
    }
}