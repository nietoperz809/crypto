package com.peter.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Shuffles a String reversibly
 */
public class StringShuffler
{
    private byte[] _seed;

    /**
     * Constructor
     * @param key Password for encryption/decryption
     * @throws NoSuchAlgorithmException when getMD fails
     */
    public StringShuffler(String key) throws NoSuchAlgorithmException
    {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        _seed = md5.digest(key.getBytes());
    }

    /**
     * Shuffles a string
     * @param in Input string
     * @return Shuffled string
     */
    public String shuffle (String in)
    {
        int len = in.length();
        int len2 = 19*len;
        SecureRandom r = new SecureRandom();
        r.setSeed(_seed);

        StringBuilder sb = new StringBuilder(in);
        for (int s=0; s<len2; s++)
        {
            int r1 = r.nextInt(len);
            int r2 = r.nextInt(len);
            char c1 = sb.charAt(r1);
            char c2 = sb.charAt(r2);
            sb.setCharAt(r1, c2);
            sb.setCharAt(r2, c1);
        }
        return sb.toString();
    }

    /**
     * De-shuffles a string
     * @param in input string
     * @return output string
     */
    public String deshuffle (String in)
    {
        int len = in.length();
        int len2 = 19*len;
        SecureRandom r = new SecureRandom();
        r.setSeed(_seed);

        StringBuilder sb = new StringBuilder(in);
        ArrayList<Integer> al = new ArrayList<>();
        for (int s=0; s<len2; s++)
        {
            al.add(r.nextInt(len));
            al.add(r.nextInt(len));
        }
        Collections.reverse(al);

        for (int s=0; s<al.size(); s+=2)
        {
            int r1 = al.get(s);
            int r2 = al.get(s+1);
            char c1 = sb.charAt(r1);
            char c2 = sb.charAt(r2);
            sb.setCharAt(r1, c2);
            sb.setCharAt(r2, c1);
        }
        return sb.toString();
    }
}
