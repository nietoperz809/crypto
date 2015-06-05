/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.crypto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SimpleCryptor
{
    private static String substituteWord(String in, boolean mode)
    {
        char arr1[] =
        {
            'g', 'm', 'j', 's', 'o', 'r', 'v', 'e', 'w', 'f', 'i', 'a', 'p', 'c', 'q', 'u', 't', 'z', 'l', 'b', 'd', 'x', 'y', 'n', 'h', 'k'
        };
        char arr2[] =
        {
            'l', 't', 'n', 'u', 'h', 'j', 'a', 'y', 'k', 'c', 'z', 's', 'b', 'x', 'e', 'm', 'o', 'f', 'd', 'q', 'p', 'g', 'i', 'v', 'w', 'r'
        };

        char[] exchg = mode ? arr1 : arr2;

        in = in.toLowerCase();
        String out = "";

        for (int s = 0; s < in.length(); s++)
        {
            out += exchg[in.charAt(s) - 'a'];
        }

        return out;
    }

    private static String substituteWord(String in, boolean mode, int times)
    {
        times = times % 19;
        while (times-- != 0)
        {
            in = substituteWord(in, mode);
        }
        return in;
    }

    public static String substituteText(String in, boolean mode)
    {
        String out = "";
        String[] splitted = in.split(" ");

        for (int s = 0; s < splitted.length; s++)
        {
            out += substituteWord(splitted[s], mode, s + 1) + " ";
        }

        return out;
    }

    public static void main(String... args)
    {
        Integer a[] = new Integer[19];
        for (int s = 0; s < 19; s++)
        {
            a[s] = s;
        }

        List<Integer> l = Arrays.asList(a);
        Collections.shuffle(l);

        System.out.print("int exchg[] = {");
        for (Integer i : l)
        {
            System.out.print("'" + i + "', ");
        }
        System.out.println("};");

        String s = substituteText("abc abc abc abc abc abc", true);
        System.out.println(s);

        String t = substituteText(s, false);
        System.out.println(t);
    }
}

/*
 static void makeReverse()
 {
 String crypt =   "gmjsorvewfiapcqutzlbdxynhk";
        
        
        
 ArrayList<Character> list = new ArrayList<>();
 for (int s=0; s<26; s++)
 {
 list.add(' ');
 }
        
 //list.ensureCapacity(30);
        
 for (int s=0; s<26; s++)
 {
 int pos = crypt.charAt(s) - 'a';
 list.set(pos, (char)('a'+s));
 }
 System.out.println (Arrays.toString(list.toArray()));
 }
    
 */
/*
 Character a[] = new Character[26];
 for (int s=0; s<26; s++)
 {
 a[s] = (char)('a'+s);
 }
        
 List<Character> l = Arrays.asList(a);
 Collections.shuffle(l);
        
 System.out.print ("int exchg[] = {");
 for (Character i :l)
 {
 System.out.print ("'"+i+"', ");
 }
 System.out.println ("};");

 */
//        Integer a[] = new Integer[26];
//        for (int s=0; s<26; s++)
//        {
//            a[s] = s;
//        }
//        
//        List<Integer> l = Arrays.asList(a);
//        Collections.shuffle(l);
//        
//        System.out.print ("int exchg[] = {");
//        for (Integer i :l)
//        {
//            System.out.print (i+", ");
//        }
//        System.out.println ("};");
