import com.peter.crypto.Factorizer;

import java.math.BigInteger;

public class Test
{
    public static void main (String[] args) throws Exception
    {
        BigInteger bi = BigInteger.valueOf(2).pow(64);
        System.out.println(bi);

        BigInteger[] list = Factorizer.factByBrent(bi);
        for (BigInteger b2 : list)
        {
            System.out.println(b2);
        }

//        char[] chars = "*+/-".toCharArray();
//        List<Character> l = new ArrayList<>();
//        for (char c : chars)
//        {
//            l.add(c);
//        }
//        PermutationArrayList<Character> p = new PermutationArrayList<>(l);
//        Character cc[][] = p.getAllPermutationsArrays();
//        int z = 1;
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
//        engine.eval("print('Hello World!');");
//        for (Character c[] :  cc)
//        {
//            String s = "result = 2";
//            for (Character c1 : c)
//            {
//                s = s + c1 + "2";
//            }
//            engine.eval(s);
//            System.out.println(""+(z++)+" --" +s + " : result = " + engine.get("result"));
//        }
    }
}
