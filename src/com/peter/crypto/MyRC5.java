package com.peter.crypto;

/**
 * To change this template use File | Settings | File Templates.
 */
class MyRC5
{
    private int[] expandedKey;
    private int rounds;

    MyRC5 (int r)
    {
        rounds = r;
        expandedKey = new int[r * 2 + 2];
    }

    void encrypt (int[] data)
    {
        int d;
        int h, i, rc;

        d = 0;

        for (h = 0; h < data.length/2; h++)
        {
            data[d] += expandedKey[0];
            data[1 + d] += expandedKey[1];
            for (i = 0; i < rounds * 2; i += 2)
            {
                data[d] ^= data[1 + d];
                rc = data[1 + d] & 31;
                data[d] = Integer.rotateLeft(data[d], rc);
                data[d] += expandedKey[i + 2];
                data[1 + d] ^= data[d];
                rc = data[d] & 31;
                data[1 + d] = Integer.rotateLeft(data[1 + d], rc);
                data[1 + d] += expandedKey[3 + i];
            }
            d += 2;
        }
    }

    void decrypt (int[] data)
    {
        int d;
        int h, i, rc;

        d = 0;
        for (h = 0; h < data.length/2; h++)
        {
            for (i = rounds * 2 - 2; i >= 0; i -= 2)
            {
                data[1+d] -= expandedKey[3+i];
                rc = data[d] & 31;
                data[1+d] = Integer.rotateRight(data[1+d], rc);
                data[1+d] ^= data[d];
                data[d] -= expandedKey[i+2];
                rc = data[1+d] & 31;
                data[d] = Integer.rotateRight(data[d], rc);
                data[d] ^= data[1+d];
            }
            data[d] -= expandedKey[0];
            data[1+d] -= expandedKey[1];
            d += 2;
        }
    }

    void key (byte[] key)
    {
        int[] pk;
        int A, B; /* padded key */
        int xk_len, pk_len, i, num_steps, rc;

        xk_len = rounds * 2 + 2;
        pk_len = key.length / 4;
        if ((key.length % 4) != 0)
        {
            pk_len += 1;
        }

        pk = new int[pk_len];

        // Initialize pk
        for (i = 0; i < pk_len; i++)
        {
            pk[i] = 0;
        }
        for (i = 0; i < key.length; i++)
        {
            int k = key[i];
            pk[i / 4] = pk[i / 4] | k << (8 * (i % 4));
        }

        /* Initialize expandedKey. */
        expandedKey[0] = 0xb7e15163; /* P32 */
        for (i = 1; i < xk_len; i++)
        {
            expandedKey[i] = expandedKey[i - 1] + 0x9e3779b9; /* Q32 */
        }

        /* Expand key into expandedKey. */
        if (pk_len > xk_len)
        {
            num_steps = 3 * pk_len;
        }
        else
        {
            num_steps = 3 * xk_len;
        }

        A = B = 0;
        for (i = 0; i < num_steps; i++)
        {
            A = expandedKey[i % xk_len] = Integer.rotateLeft(expandedKey[i % xk_len] + A + B, 3);
            rc = (A + B) & 31;
            B = pk[i % pk_len] = Integer.rotateLeft(pk[i % pk_len] + A + B, rc);
        }
    }

    static void test()
    {
        MyRC5 rc5 = new MyRC5(10); // 10 rounds
        int[] data = {0, 1, 2, 3, 4, 5, 6, 7};
        byte key[] = {'A', 'B', 'C', 'D', 'E'};
        int i;

        rc5.key(key);

        rc5.encrypt(data);
        System.out.println("should be: caa24ebd5a310fbcdc0f6852a3cbf4883d6c4480e90dcfcb65c3699132473c5f");
        System.out.print  ("is:        ");
        for (i = 0; i < data.length; i++)
        {
            System.out.printf("%x", data[i]);
        }
        System.out.println();

        rc5.decrypt(data);
        System.out.println("should be: 01234567");
        System.out.print  ("is:        ");
        for (i = 0; i < data.length; i++)
        {
            System.out.printf("%x", data[i]);
        }
        System.out.println();
    }
}
