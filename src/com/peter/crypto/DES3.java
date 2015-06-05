package com.peter.crypto;

/**
 * Created by IntelliJ IDEA.
 * UNTESTED !!!
 */
public class DES3
{
    DES d1;
    DES d2;
    DES d3;

    public DES3 (byte[] key1, byte[] key2, byte[] key3)
    {
        d1 = new DES (key1);
        d2 = new DES (key2);
        d3 = new DES (key3);
    }

    public byte[] encrypt (byte[] in)
    {
        byte[] out = new byte[8];
        d1.encrypt (in, 0, in, 0);
        d2.decrypt (in, 0, in, 0);
        d3.encrypt (in, 0, out, 0);
        return out;
    }

    public byte[] decrypt (byte[] in)
    {
        byte[] out = new byte[8];
        d1.decrypt (in, 0, in, 0);
        d2.encrypt (in, 0, in, 0);
        d3.decrypt (in, 0, out, 0);
        return out;
    }
}
