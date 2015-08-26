/*
This routine is an exact implementation of Boris Hagelin's
cryptographic machine.  See U. S. Patent #2,089,603.
*/
package com.peter.crypto.unix;

import java.io.IOException;


/**
 *
 * @author Administrator
 */
public class Unix_routines
{
    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) throws IOException
   {
       char[] txt = "hello/0".toCharArray();
       txt = new String(txt).substring(1).toCharArray();
       System.out.println (txt);
        //testit();
   }
   
   public static void testit() throws IOException
   {
        System.out.println(UnixMath.sin(45));
        System.out.println(Math.sin(45));
        System.out.println(UnixMath.sqrt(9.0));
        System.out.println(UnixMath.log10(10000.0));

        System.out.println(UnixMath.exp(3));
        System.out.println(Math.exp(3));

        System.out.println(UnixMath.tan(3));
        System.out.println(Math.tan(3));

        HagelinCrypt hc1 = new HagelinCrypt();
        HagelinCrypt hc2 = new HagelinCrypt();
        
        String encrypted = hc1.crypt("+#;.ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        String clear = hc2.crypt(encrypted);
        System.out.println (clear);
        System.out.println (encrypted);
        
        /*DES75 Test*/
        char[] salt = {'0','c',0};
        char[] pwd = {'m','a','r','i','l','y','n',0};
        char[] sp = DES75.crypt(pwd, salt);
        for (int i=0; i<13; i++)
            System.out.print (sp[i]);
        System.out.println();
    }

}
