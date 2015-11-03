
import com.peter.crypto.NumberField;
import com.peter.crypto.NumberFieldFactory;
import com.peter.crypto.StringMatch;




public class Test
{
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
}


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
