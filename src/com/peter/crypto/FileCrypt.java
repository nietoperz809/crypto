package com.peter.crypto;

import java.io.*;

/**
 * New Class.
 * User: Administrator
 * Date: 28.02.2009
 * Time: 21:09:04
 */
public class FileCrypt
{
    /**
     * Encrypts file with RC5 algorithm
     * @param key RC5 compatible key
     * @param in Input file path
     * @param out Output file path
     * @throws java.io.IOException If file can't be loaded/saved
     */
    public static void encryptRc5andSave(byte[] key, String in, String out) throws IOException
    {
        byte[] content = IO.readFile (in);
        int[] i1 = CryptTools.toIntArray(content);
        MyRC5 rc5 = new MyRC5(11);

        rc5.key(key);
        rc5.encrypt(i1);

        IO.writeFile (out, CryptTools.toByteArray(i1));
    }

    /**
     * Encrypts file with AES256 algorithm
     * @param key AES compatible key
     * @param in Input file path
     * @param out Output file path
     * @throws java.io.IOException If file can't be loaded/saved
     */
    public static void encryptAes256AndSave(byte[] key, String in, String out) throws IOException
    {
        FileInputStream fin = new FileInputStream(in);
        FileOutputStream fout = new FileOutputStream (out);
        byte[] content = new byte[16];

        MyAES256 aes = new MyAES256();
        aes.init(key);

        for (;;)
        {
            int r = fin.read(content);
            aes.encrypt(content);
            fout.write(content);
            if (r != content.length)
                break;
        }
        fin.close();
        fout.close();
    }

    /**
     * Decrypts file with AES256 algorithm
     * @param key AES compatible key
     * @param in Input file path
     * @param out Output file path
     * @throws java.io.IOException If file can't be loaded/saved
     */
    public static void decryptAes256AndSave(byte[] key, String in, String out) throws IOException
    {
        FileInputStream fin = new FileInputStream(in);
        FileOutputStream fout = new FileOutputStream (out);
        byte[] content = new byte[16];

        MyAES256 aes = new MyAES256();
        aes.init(key);

        for (;;)
        {
            int r = fin.read(content);
            if (r != content.length)
                break;
            aes.decrypt(content);
            fout.write(content);
        }
        fin.close();
        fout.close();
    }

    /**
     * Encrypts file with AES256 algorithm and cypher block chaining
     * @param key AES compatible key
     * @param iv In itialisation vector
     * @param in Input file path
     * @param out Output file path
     * @throws java.io.IOException If file can't be loaded/saved
     */
    public static void encryptAes256AndSaveCBCwithFileLength (byte[] key, byte[] iv, String in, String out) throws IOException
    {
        File fileIn = new File (in);
        FileInputStream fin = new FileInputStream (fileIn);
        FileOutputStream fout = new FileOutputStream (out);
        byte[] content1 = new byte[16];
        byte[] content2 = new byte[16];

        // Write file size and IV
        DataOutputStream o = new DataOutputStream (fout);
        o.writeLong (fileIn.length());
        o.write(iv);

        MyAES256 aes = new MyAES256();
        aes.init(key);

        for (long counter = 0;;counter++)
        {
            int r = fin.read(content1);
            if (counter == 0)
            {
                CryptTools.xor (content1, iv);
            }
            else
            {
                CryptTools.xor (content1, content2);
            }
            aes.encrypt(content1);
            System.arraycopy (content1, 0, content2, 0, 16);
            fout.write(content1);
            if (r != 16)
                break;
        }
        fin.close();
        fout.close();
    }

    /**
     * Decrypts file with AES256 algorithm and cypher block chaining
     * @param key AES compatible key
     * @param in Input file path
     * @param out Output file path
     * @throws java.io.IOException If file can't be loaded/saved
     */
    public static void decryptAes256AndSaveCBCwithFileLength(byte[] key, String in, String out) throws IOException
    {
        FileInputStream fin = new FileInputStream(in);
        FileOutputStream fout = new FileOutputStream (out);
        byte[] content1 = new byte[16];
        byte[] content2 = new byte[16];
        byte[] content3 = new byte[16];
        byte[] iv = new byte[16];
        long counter = 0;

        // Read file size and IV
        DataInputStream i = new DataInputStream (fin);
        long size = i.readLong();
        if (i.read(iv) != 16)
        {
            throw new IOException ("Missing IV");
        }

        MyAES256 aes = new MyAES256();
        aes.init(key);

        while (size > 0)
        {
            int r = fin.read (content1);
            if (r == -1)
            {
                throw new IOException ("Read Error -1");
            }
            else
            {
                size -= r;
            }
            if (counter == 0)
            {
                System.arraycopy (content1, 0, content2, 0, 16);
                aes.decrypt(content1);
                CryptTools.xor (content1, iv);
            }
            else
            {
                System.arraycopy (content1, 0, content3, 0, 16);
                aes.decrypt(content1);
                CryptTools.xor(content1, content2);
                System.arraycopy (content3, 0, content2, 0, 16);
            }
            if (size <= 0)
                r = 16 + (int)size;
            fout.write (content1, 0, r);
            counter++;
        }
        fin.close();
        fout.close();
    }

    /**
     * Encrypts file with 'Peter1' algorithm
     * @param passwd
     * @param in
     * @param out
     * @throws Exception
     */
    public static void encryptFilePeter1 (String passwd, String in, String out) throws Exception
    {
        File fileIn = new File (in);
        FileInputStream fin = new FileInputStream(fileIn);
        FileOutputStream fout = new FileOutputStream (out);
        byte[] buff = new byte[32];
        byte[] key = CryptTools.passwordHash (passwd.getBytes());
        long size = fileIn.length();

        while (size != 0)
        {
            key = CryptTools.passwordHash (key);
            int r = fin.read (buff);
            if (r == -1)
            {
                throw new Exception ("fin.read returns -1");
            }
            buff = CryptTools.xor(buff, key);
            fout.write(buff, 0, r);
            size -= r;
        }
        fin.close();
        fout.close();
    }

    /**
     * Encrypts file with 'Peter2' algorithm
     * @param passwd
     * @param in
     * @param out
     * @throws Exception
     */
    public static void encryptFilePeter2 (String passwd, String in, String out) throws Exception
    {
        byte[] indat = IO.readFile(in);
        byte[] key = CryptTools.passwordHash (passwd.getBytes());

        indat = CryptTools.rotateArrayBitsRightCount (indat, key[0]);
        indat = CryptTools.grayByteArray(indat);
        for (int s=1; s<32; s++)
        {
            indat = CryptTools.rotateArrayBitsRightCount (indat, key[s]);
            indat = CryptTools.grayByteArray(indat);
        }

        IO.writeFile (out, indat);
    }

    /**
     * Decrpyts file with 'Peter3' algorithm
     * @param passwd
     * @param in
     * @param out
     * @throws Exception
     */
    public static void decryptFilePeter2(String passwd, String in, String out) throws Exception
    {
        byte[] indat = IO.readFile(in);
        byte[] key = CryptTools.passwordHash (passwd.getBytes());

        indat = CryptTools.ungrayByteArray(indat);
        indat = CryptTools.rotateArrayBitsLeftCount (indat, key[31]);
        for (int s=30; s>=0; s--)
        {
            indat = CryptTools.ungrayByteArray(indat);
            indat = CryptTools.rotateArrayBitsLeftCount (indat, key[s]);
        }

        IO.writeFile (out, indat);
    }

    public static void encryptFilePeter3 (String passwd, String in, String out) throws Exception
    {
        byte[] indat = IO.readFile(in);
        byte[] key = CryptTools.passwordHash (passwd.getBytes());

        indat = CryptTools.xor(indat, key);
        indat = CryptTools.addChain(indat,key);
        indat = CryptTools.xorChain(indat, key);

        IO.writeFile (out, indat);
    }


    public static void decryptFilePeter3 (String passwd, String in, String out) throws Exception
    {
        byte[] indat = IO.readFile(in);
        byte[] key = CryptTools.passwordHash (passwd.getBytes());

        indat = CryptTools.unxorChain(indat, key);
        indat = CryptTools.unaddChain(indat, key);
        indat = CryptTools.xor(indat, key);

        IO.writeFile (out, indat);
    }

    public static void encryptFilePeter4 (String passwd, String in, String out) throws Exception
    {
        byte[] indat = IO.readFile(in);
        byte[] key = CryptTools.passwordHash (passwd.getBytes());
        key = ArrayGenerators.exludeValue(key, 0, passwd.getBytes()[0]);
        final int rounds = 17;

        for (int s=1; s<rounds; s++)
        {
            indat = CryptTools.grayByteArray(indat);
            indat = CryptTools.galoisFieldMult(indat, key);
            indat = CryptTools.xorChain (indat, key);
            indat = CryptTools.rotateArrayBitsLeftCount(indat, s);
            indat = CryptTools.xor(indat, key);
            indat = CryptTools.addChain(indat, key);
            indat = CryptTools.yellowCode(indat);
        }
        IO.writeFile (out, indat);
    }

    public static void decryptFilePeter4 (String passwd, String in, String out) throws Exception
    {
        byte[] indat = IO.readFile(in);
        byte[] key = CryptTools.passwordHash (passwd.getBytes());
        key = ArrayGenerators.exludeValue(key, 0, passwd.getBytes()[0]);
        final int rounds = 17;

        for (int s=1; s<rounds; s++)
        {
            indat = CryptTools.yellowCode(indat);
            indat = CryptTools.unaddChain(indat, key);
            indat = CryptTools.xor(indat, key);
            indat = CryptTools.rotateArrayBitsRightCount(indat, rounds-s);
            indat = CryptTools.unxorChain (indat, key);
            indat = CryptTools.galoisFieldDiv(indat, key);
            indat = CryptTools.ungrayByteArray(indat);
        }
        IO.writeFile (out, indat);
    }

}
