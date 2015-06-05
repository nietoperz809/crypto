package com.peter.crypto;

/**
 * New Class.    ******************** INCOMPLETE ***************************
 * User: Administrator
 * Date: 03.03.2009
 * Time: 17:50:43
 */
//public class GSMEncryption
//{
//    LFSR r1 = new LFSR (19, 0, new int[]{18,17,16,13});
//    LFSR r2 = new LFSR (22, 0, new int[]{21,20});
//    LFSR r3  = new LFSR (23, 0, new int[]{7,20,21,22});
//    final int r1_clock = 8;
//    final int r2_clock = 10;
//    final int r3_clock = 10;
//
//    int getKeyStreamBit()
//    {
//        return r1.getMSB() ^ r2.getMSB() ^ r3.getMSB();
//    }
//
//    void clock()
//    {
//        int clock_agree = r1.getBit(r1_clock) + r2.getBit(r2_clock) + r3.getBit(r3_clock);
//        if (clock_agree > 1)
//            clock_agree = 1;
//        else
//            clock_agree = 0;
//
//        if (r1.getBit(r1_clock) == clock_agree)
//            r1.clockLeft();
//        if (r2.getBit(r2_clock) == clock_agree)
//            r2.clockLeft();
//        if (r3.getBit(r3_clock) == clock_agree)
//            r3.clockLeft();
//    }
//
//    /**
//     *
//     * @param secret 64 bit secret key
//     * @param framenum 22 bit frame number
//     */
//    void initialize (long secret, int framenum)
//    {
//        framenum = framenum & 0x3FFFFF;
//
//        r1.clear();
//        r2.clear();
//        r3.clear();
//
//        for (int i=0; i<64; i++)
//        {
//            r1.setBit(0, (int) (r1.getBit(0) ^ secret));
//            r2.setBit(0, (int) (r2.getBit(0) ^ secret));
//            r3.setBit(0, (int) (r3.getBit(0) ^ secret));
//            r1.clockLeft();
//            r2.clockLeft();
//            r3.clockLeft();
//            secret >>>= 1;
//        }
//        for (int i= 0; i<22; i++)
//        {
//            r1.setBit(0, (int) (r1.getBit(0) ^ framenum));
//            r2.setBit(0, (int) (r2.getBit(0) ^ framenum));
//            r3.setBit(0, (int) (r3.getBit(0) ^ framenum));
//            r1.clockLeft();
//            r2.clockLeft();
//            r3.clockLeft();
//            framenum >>>= 1;
//        }
//        for (int i=0; i<100; i++)
//        {
//            clock();
//        }
//    }
//
//    public GSMEncryption (long secret, int framenum)
//    {
//        initialize (secret, framenum);
//    }
//
//    @Override
//    public String toString()
//    {
//        return new StringBuilder().append("R1:")
//                                  .append(r1.toString())
//                                  .append(" R2:")
//                                  .append(r2.toString())
//                                  .append(" R3:")
//                                  .append(r3.toString())
//                                  .toString();
//    }
//}
