package com.peter.crypto;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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
        String str = Arrays.toString(arr);
        ps.println (str);
    }

    /**
     * Reads file into byte array
     * @param name File (path) name
     * @return A new byte array
     * @throws java.io.IOException If file operation fails
     */
    public static byte[] readFile(String name) throws IOException
    {
        Path path = Paths.get(name);
        return Files.readAllBytes(path);
    }

    /**
     * Writes byte array into file
     * @param name    Path of new file
     * @param content Array to be written
     * @throws java.io.IOException If file operation fails
     */
    public static void writeFile(String name, byte[] content) throws IOException
    {
        Path path = Paths.get(name);
        Files.write(path, content);
    }

    public static void printHexMonitorStyle (byte[] in, int items, PrintStream ps)
    {
        StringBuilder sb = new StringBuilder();
        for (int t=0; t<in.length; t+=items)
        {
            StringBuilder sb2 = new StringBuilder();
            for (int s = 0; s < items; s++)
            {
                byte b = in[s+t];
                sb.append(String.format("%02x ", b));
                if (!Character.isISOControl(b))
                    sb2.append ((char)b);
                else
                    sb2.append(".");
            }
            sb.append (" --  ").append(sb2).append("\r\n");
        }
        ps.println(sb.toString());
    }

    // test
    public static void main (String[] args)
    {
        byte[] b = ArrayGenerators.makeCountedValueBytes(0, 255);
        printHexMonitorStyle (b, 16, System.out);
    }
}
