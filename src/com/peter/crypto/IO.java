package com.peter.crypto;

import java.io.*;

/**
 * To change this template use File | Settings | File Templates.
 */
public class IO
{
    /**
     * Prints byte array as hex values
     * @param byteArray Array to be printed
     * @param ps The output stream
     */
    public static void printAsHex(byte[] byteArray, PrintStream ps)
    {
        for (byte aByteArray : byteArray)
        {
            ps.printf("%02x ", aByteArray);
        }
        ps.println();
    }

    /**
     * Prints array as derive compatible vector
     * @param arr The array
     * @param ps PrintStream that receives output
     */
    public static void printAsVector (int[] arr, PrintStream ps)
    {
        ps.print('[');
        for (int s=0; s<arr.length; s++)
        {
            ps.printf ("[%d,%d]", s, arr[s]);
            if (s != arr.length-1)
                ps.print(",");
        }
        ps.println(']');
    }

    /**
     * Reads file into byte array
     * @param name File (path) name
     * @return A new byte array
     * @throws java.io.IOException If file operation fails
     */
    public static byte[] readFile(String name) throws IOException
    {
        File f = new File(name);
        byte[] content = new byte[(int) f.length()];
        FileInputStream fi = new FileInputStream(f);
        if (fi.read(content) != f.length())
        {
            throw new IllegalArgumentException();
        }
        fi.close();
        return content;
    }

    /**
     * Writes byte array into file
     * @param name    Path of new file
     * @param content Array to be written
     * @throws java.io.IOException If file operation fails
     */
    public static void writeFile(String name, byte[] content) throws IOException
    {
        File f = new File(name);
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(content);
        fo.close();
    }
}
