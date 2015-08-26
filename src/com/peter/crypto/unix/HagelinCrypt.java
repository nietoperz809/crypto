/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.crypto.unix;

import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class HagelinCrypt
{
    int cagetable[] = new int[]
    {
        0, 1, 1, 2, 2, 3, 4, 4, 5, 6, 8, 8, 9, 10, 12, 16,
        16, 17, 18, 20, 24, 32, 32, 33, 34, 36, 40, 48
    };
    int[] cage = new int[27];
    int warr1[] = new int[52];
    int warr2[] = new int[50];
    int warr3[] = new int[46];
    int warr4[] = new int[42];
    int warr5[] = new int[38];
    int warr6[] = new int[34];
    int wheel1;
    int wheel2;
    int wheel3;
    int wheel4;
    int wheel5;
    int wheel6;
    int key[] = new int[130];

    int ix, jx;

    /**
     *
     */
    public HagelinCrypt()
    {
        int ip, jp;
        int i;
        jp = 0;
        key[jp++] = 004;
        key[jp++] = 034;
        ip = 0;
        while (jp < 128)
        {
            key[jp] = key[jp - 1] ^ key[ip++];
            jp++;
        }
        setup(warr1, 26);
        setup(warr2, 25);
        setup(warr3, 23);
        setup(warr4, 21);
        setup(warr5, 19);
        setup(warr6, 17);
        jp = 0;
        i = 27;
        while (i-- != 0)
        {
            cage[i] = cagetable[key[jp++] % 28];
        }
    }

    int getbit()
    {
        int b;
        b = (key[jx] >>> ix) & 1;
        if (ix++ > 5)
        {
            jx++;
            ix = 0;
        }
        return (b);
    }

    private void setup(int list[], int n) 
    {
        int lp;
        lp = 0;
        while (--n != 0)
        {
            list[lp] = lp + 2;
            list[lp + 1] = getbit();
            lp += 2;
        }
        list[lp] = 0;
        list[lp + 1] = getbit();
    }

    /**
     * This routine is an exact implementation of Boris Hagelin's
     *   cryptographic machine.  See U. S. Patent #2,089,603.
     * @param in
     * @return
     * @throws IOException
     */
    public String crypt (String in) throws IOException
    {
        StringBuilder out = new StringBuilder();
        int precious;
        int crypt;
        int temp;
        int loop = 0;
        
        while (loop < in.length())
        {
            precious = in.charAt(loop++);
            temp = 040 * warr1[wheel1 + 1]; //0
            temp += 020 * warr2[wheel2 + 1]; //0
            temp += 010 * warr3[wheel3 + 1]; //8
            temp += 004 * warr4[wheel4 + 1]; // 12
            temp += 002 * warr5[wheel5 + 1]; // 14
            temp += 001 * warr6[wheel6 + 1]; // 15

            wheel1 = warr1[wheel1];
            wheel2 = warr2[wheel2];
            wheel3 = warr3[wheel3];
            wheel4 = warr4[wheel4];
            wheel5 = warr5[wheel5];
            wheel6 = warr6[wheel6];
            int random = 0;
            int i = 27;
            while (i-- != 0)
            {
                if ((temp & cage[i]) != 0)
                {
                    random++;
                }
                //random = random + ((temp & cage[i]) != 0);
            }
            random %= 26;

            if (precious == '\n' || precious == ' ')
            {
                crypt = precious;
            }
            else
            {
                crypt = ('a' + 'z' - precious + random) % 0400;
                if (crypt >= 'a' && crypt <= 'z' && precious > 'z')
                {
                    crypt += 26;
                }
                if ((crypt > 'z') && (precious >= 'a') & (precious <= 'z'))
                {
                    crypt -= 26;
                }
                if (crypt == '\n' || crypt == ' ')
                {
                    crypt = precious;
                }
            }
            out.append ((char)crypt);
        }
        return out.toString();
    }
}
