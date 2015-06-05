package com.peter.crypto;

/**
 * Galois field 2^8
 */
public class GaloisField256
{
    // define the Size & Prime Polynomial of this Galois field (2^8)
    private final int GF = 256;

    // establish global Log and Antilog arrays
    private int[] Log = new int[GF];
    private int[] ALog = new int[GF];

    static GaloisField256 m_instance = null;

    private GaloisField256()
    {
        int i;
        /*
            For illustration we will take the widely-used case of GF(256),
            the Galois field for 2^8, with a prime polynomial of x^8 + x^5 + x^3 + x^2 + 1
            whose equivalent value P is binary 100101101 or decimal 301.         
        */
        int PP = 301;
        Log[0] = 1-GF;
        ALog[0] = 1;
        for (i=1; i<GF; i++)
        {
            ALog[i] = ALog[i-1] * 2;
            if (ALog[i] >= GF)
                ALog[i] ^= PP;
            Log[ALog[i]] = i;
        }
    }

    public int[] getLog()
    {
        return Log;
    }

    public int[] getALog()
    {
        return ALog;
    }

    public static GaloisField256 getInstance()
    {
        if (m_instance == null)
            m_instance = new GaloisField256();
        return m_instance;
    }

    public int Product (int A, int B)
    {
        if ((A == 0) || (B == 0))
            return (0);
        return (ALog[(Log[A] + Log[B]) % (GF-1)]);
    }

    public int Quotient (int A, int B)
    {
        if (B == 0)
        {
            return GF;
        }
        if (A == 0)
            return (0);
        return (ALog[(Log[A] - Log[B] + (GF-1)) % (GF-1)]);
    }

    public int Sum (int A, int B)
    {
        return (A ^ B);
    }

    public int Difference (int A, int B)
    {
        return (A ^ B);
    }
}
