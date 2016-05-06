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

    /**
     * Converts byte array to hex monitor style output
     * @param in the byte array
     * @param bytesPerLine number of bytes per line
     * @return a String that is the human readable data block
     */
    public static String toHexMonStyle (byte[] in, int bytesPerLine)
    {
        StringBuilder sb = new StringBuilder();
        for (int t=0; t<in.length; t+=bytesPerLine)
        {
            StringBuilder sb2 = new StringBuilder();
            sb.append(String.format("%08x : ", t));
            for (int s = 0; s < bytesPerLine; s++)
            {
                int idx = s+t;
                if (idx >= in.length)
                {
                    sb.append("   ");
                }
                else
                {
                    byte b = in[idx];
                    sb.append(String.format("%02x ", b));
                    if (b>0x1f)   // show 0x20 ... 0x7f
                        sb2.append ((char)b);
                    else
                        sb2.append(".");
                }
            }
            sb.append (" --  ").append(sb2).append("\r\n");
        }
        return sb.toString();
    }

    public static void printHexMonitorStyle (byte[] in, int bytesPerLine, PrintStream ps)
    {
        String s = toHexMonStyle(in, bytesPerLine);
        ps.println(s);
    }

    // test
    public static void main (String[] args)
    {
        byte[] b = ArrayGenerators.makeCountedValueBytes(0, 1000);
        printHexMonitorStyle (b, 16, System.out);
    }
}
