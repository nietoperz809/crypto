import com.peter.crypto.IO;
import com.peter.crypto.PiDigitsHex;

import java.math.BigInteger;
import java.util.Arrays;


public class Test
{
    public static void main (String[] args) throws Exception
    {
//        System.out.println(PiDigits2.getPiString(100));
//        System.out.println(PiDigits.pi_digits(100));

//        System.out.println(PiDigitsHex.piDigit(0));
//        System.out.println(PiDigitsHex.piDigit(1));
//        System.out.println(PiDigitsHex.piDigit(2));
//        System.out.println(PiDigitsHex.piDigit(3));
//        System.out.println(PiDigitsHex.piDigit(4));
//        System.out.println(PiDigitsHex.piDigit(5));
//        System.out.println(PiDigitsHex.piDigit(6));
//        System.out.println(PiDigitsHex.piDigit(7));

//        System.out.println(PiDigitsHex.packedPi8(0));
//        System.out.println(PiDigitsHex.packedPi16(0));
//        System.out.println(PiDigitsHex.packedPi32(0));

        BigInteger bi = PiDigitsHex.packedPi(0,10000);
        byte[] bb = bi.toByteArray();
        IO.writeFile("c:\\pidigits.bin", bb);

        //System.out.println (Arrays.toString(bi.toByteArray()));
        //System.out.println(bi);
//        String password = "mxyzptlk";
//        StringShuffler sf = new StringShuffler(password);
//        byte[] b1 = Files.readAllBytes(Paths.get("C:\\Users\\Administrator\\Desktop\\testfile.txt"));
//        String plain = new String (b1);
//        String encrypted = sf.shuffle (plain);
//        String decrypted = sf.deshuffle(encrypted);
//        System.out.println(encrypted);
//        System.out.println("----------------------");
//        System.out.println(decrypted);

//        byte[] b1 = Files.readAllBytes(Paths.get("C:\\Users\\Administrator\\Desktop\\hackmail.txt"));
//        String plain = new String(b1);
//
//        char[][] t = Stringmatrix.toMatrix(plain);
//
//        t = Stringmatrix.revMatrix(t);
//
//        String h = Stringmatrix.fromMatrix(t);
//        System.out.println(h);
//
//        new Thread (()->{System.out.println("blah");}).start();

//        int xy = (int)Math.ceil(Math.sqrt(plain.length()));
//        System.out.println(plain.length());
//        System.out.println(xy*xy);

//        String base = "abcdefghijklmnopqrstuvwxyz";
//        String seek = "peter";
//        for (int s=0;s<100; s++)
//        {
//            String str = CryptTools.generateRandomString (seek/*, 5*/);
//            SimilarStrings ss = new SimilarStrings (seek,str);
//            double res = ss.getResult();
//            System.out.println (str+" - "+res);
//        }

//        List<String> l = PermutationArrayList.generateStringPermutations("peter");
//        System.out.println(l.toString());
//        NumberField n = NumberFieldFactory.divTable(256);
//        NumberFieldDisplay nd = new NumberFieldDisplay(n);
//        nd.setVisible(true);
    }
}

/*

        String in =
            "Weil Frieden bedeutet, dass der Staat Israel seine Grenzen definieren "
                + "und fixieren müsste." +
            "Damit ist der Traum von Erez zu Ende.";
        NumberField n = NumberFieldFactory.squareFromString(in);

        StringMatch dl = new StringMatch();
        String start = n.asFlatString();
        System.out.println(start.length());
        for (int s=0; s<100; s++)
        {
            n = n.reverse();
            n = n.rotateColumnsDownIncremental();
            n = n.transpose();
            String vgl = n.asFlatString();
            //double nn = 10000 * new SimilarStrings (start, vgl).getResult() -9000;

            int nn = StringMatch.getLevenshteinDistance(start, vgl);
            System.out.println(s+" "+nn);
        }


 */

/*
    public static void main(String[] args) throws Exception
    {
        String in = 
            "Weil Frieden bedeutet, dass der Staat Israel seine Grenzen definieren "
                + "und fixieren müsste." +
            "Damit ist der Traum von Erez zu Ende.";
        NumberField n = NumberFieldFactory.squareFromString(in);
        
        StringMatch dl = new StringMatch();
        String start = n.asFlatString();
        System.out.println(start.length());
        for (int s=0; s<100; s++)
        {
            n = n.reverse();
            n = n.rotateColumnsDownIncremental();
            n = n.transpose();
            String vgl = n.asFlatString();
            //double nn = 10000 * new SimilarStrings (start, vgl).getResult() -9000;
            int nn = StringMatch.getLevenshteinDistance(start, vgl);
            System.out.println(s+" "+nn);
        }
    }


int rots = in.length();
        for (int s = 0; s < rots; s++)
        {
            n = n.reverse();
            n = n.rotateColumnsDownIncremental();
            n = n.transpose();
        }
        
        String flat = n.asFlatString();
        NumberField n2 = NumberFieldFactory.squareFromString(flat);
        for (int s = 0; s < rots; s++)
        {
            n2 = n2.transpose();
            n2 = n2.rotateColumnsUpIncremental();
            n2 = n2.reverse();
        }
        
//        System.out.println(n.toCharString());
//        System.out.println(in);
//        System.out.println(flat);
        
        System.out.println(n2.getSize()+"\n"+n2.asFlatString());
        System.out.println(n.getSize()+"\n"+n.asFlatString());

        double nn = new SimilarStrings (n.asFlatString(), n2.asFlatString()).getResult();
        System.out.println(nn);


static String rotxor (String in) throws UnsupportedEncodingException
 {
 byte[] b1 = in.getBytes("UTF-8");
 byte[] rot = CryptTools.rotateArrayLeft(b1, 1);
 byte[] xr = CryptTools.xor(b1, rot);
 System.out.println(Arrays.toString(b1));
 System.out.println(Arrays.toString(rot));
 System.out.println(Arrays.toString(xr));
 System.out.println("----------------");
 return new String (xr, "UTF-8");
 }

 String a = "3333";
 String s1 = rotxor (a);
 String s2 = rotxor (s1);
 String s3 = rotxor (s2);
 String s4 = rotxor (s3);
 String s5 = rotxor (s4);
 System.out.println(s1);
 System.out.println(s2);

 */
