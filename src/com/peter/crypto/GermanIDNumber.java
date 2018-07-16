package com.peter.crypto;

public class GermanIDNumber
{
    private String dateCode (int jj, int mm, int tt) throws Exception
    {
        String s1 = toTwoChars(jj)+toTwoChars(mm)+toTwoChars(tt);
        char check = (char) ('0'+ checksum (s1));
        return s1+check;
    }

    public String birthDay (int jjjj, int mm, int tt) throws Exception
    {
        if (jjjj < 1900 || jjjj > 1999)
            throw new Exception("wrong birthday");
        return dateCode (jjjj-1900, mm, tt);
    }

    public String lastDay (int jjjj, int mm, int tt) throws Exception
    {
        if (jjjj < 2000 || jjjj > 2099)
            throw new Exception("wrong birthday");
        return dateCode (jjjj-2000, mm, tt);
    }

    public String toTwoChars (int in) throws Exception
    {
        String s1 = ""+in;
        if (s1.length() > 2)
            throw new Exception("number too big");
        if (s1.length() == 1)
            return "0"+s1;
        return s1;
    }

    public int checksum (String in) throws Exception
    {
        int[] mult = {7,3,1};
        int idx = 0;
        int sum = 0;
        for (int s=0; s<in.length(); s++)
        {
            int n;
            char c = in.charAt(s);
            if (Character.isDigit(c))
                n = c-'0';
            else if (Character.isUpperCase(c))
                n = c-'A'+10;
            else
                throw new Exception ("wrong char");
            sum = sum + n*mult[idx];
            idx = (idx+1)%3;
        }
        return sum%10;
        // System.out.println(sum%10);
    }

    public static void main (String[] args) throws Exception
    {
        GermanIDNumber id = new GermanIDNumber();

        String num1 = "043514401";
        int check = id.checksum(num1);
        num1 = num1+check;

        String num2 = id.birthDay(1985, 12, 24);
        String num3 = id.lastDay(2042, 6, 22);

        String finalNum = num1+num2+num3;

        check = id.checksum(finalNum);
        finalNum = num1+"D"+num2+num3+check;

        System.out.println(finalNum);

        String idd = "IDD<<"+finalNum.substring(0,10)+"<<<<<<<<<<<<<<<"
                + finalNum.substring(11, 18)+"<"+finalNum.substring(18, 25)
                + finalNum.substring(10,11)+"<<<<<<<<<<<<<"+finalNum.substring(25, 26);
        System.out.println(idd);
    }
}
