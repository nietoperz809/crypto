package com.peter.crypto.galois;

/**
 * A class to perform finite field arithmetic for Galois fields of order 2<sup>8</sup>. The implementation of this class
 * works by treating Java's byte as an unsigned byte, where the positive numbers are 0-127 and the negative numbers are
 * 128-255. Credit to http://www.cs.utsa.edu/~wagner/laws/FFM.html for providing the logic and pseudocode of
 * this class.
 * 
 * @author Nick Wuensch
 */
public class FiniteByteField
{
	public static final int MAX_VALUE = 255;
	
	private static final byte[] exp = new byte[MAX_VALUE + 1];
	private static final byte[] log = new byte[MAX_VALUE + 1];
	private static final byte[][] mul = new byte[MAX_VALUE + 1][MAX_VALUE + 1];
	private static final byte[][] div = new byte[MAX_VALUE + 1][MAX_VALUE + 1];
	private static final byte[] sqr = new byte[MAX_VALUE + 1];
	private static final byte[] sqrt = new byte[MAX_VALUE + 1];

	private static final byte generator = 0x03;

	static
	{
		byte x = (byte) 0x01;
		exp[0] = x;
		
		for(int i = 1; i <= MAX_VALUE; i++)
		{
			byte y = slowMul(x, generator);
			exp[i] = y;
			x = y;
		}
		
		for(int i = 0; i <= MAX_VALUE; i++)
			log[exp[i] & 0xff] = (byte) i;
		
		for(int i = 0; i <= MAX_VALUE; i++)
		{
			for(int j = 0; j <= MAX_VALUE; j++)
			{
				mul[i][j] = fastMul((byte)(i & 0xff), (byte)(j & 0xff));
				div[i][j] = fastDiv((byte)(i & 0xff), (byte)(j & 0xff));
			}
		}
		
		for(int i = 0; i <= MAX_VALUE; i++)
			sqr[i] = mul((byte)(i & 0xff), (byte)(i & 0xff));
		
		for(int i = 0; i <= MAX_VALUE; i++)
			sqrt[sqr[i] & 0xff] = (byte) i;
	}
	
	private FiniteByteField () { /* Prevent instantiation */ }
	
	/**
	 * Returns the given number raised to the power of 2.
	 * 
	 * @see #pow(byte, byte)
	 * @param a The base
	 * @return The GF(2<sup>8</sup>) square of the given number;
	 */
	public static byte sqr(byte a)
	{
		return sqr[a & 0xff];
	}
	
	/**
	 * Returns the square root of the given number. This is calculated as the inverse of {@link #sqr(byte)}, but could also
	 * have been calculated as:<pre>
	 * <code>
	 * In GF(2<sup>m</sup>),
	 * a = a<sup>2<sup>m</sup></sup>
	 * Therefore,
	 * a<sup>1/2</sup> = a<sup>2<sup>m</sup>/2</sup>
	 * a<sup>1/2</sup> = a<sup>2<sup>m-1</sup></sup>
	 * In GF(2<sup>8</sup>),
	 * sqrt(a) = a<sup>128</sup>
	 * </code></pre>
	 * 
	 * @param a The base
	 * @return The GF(2<sup>8</sup>) square root of the given number;
	 */
	public static byte sqrt(byte a)
	{
		return sqrt[a & 0xff];
	}
	
	/**
	 * This is a version of {@link #mul(byte, byte)} optimized for multiplying by two.
	 * @param a The byte value to double.
	 * @return The given byte valued, multiplied by two.
	 */
	public static byte dbl(byte a)
	{
		return (byte) ((a << 1) ^ ((a & 0x80) != 0 ? 0x1b : 0));
	}
	
	/**
	 * This multiplies the two bytes, as if they were unsigned, using GF(2<sup>8</sup>).
	 * 
	 * @param a The multiplier
	 * @param b The multiplicand
	 * @return The GF(2<sup>8</sup>) product
	 */
	public static byte mul(byte a, byte b)
	{
		return mul[a & 0xff][b & 0xff];
	}
	
	/**
	 * This multiplies the given bytes, as if they were unsigned, using GF(2<sup>8</sup>) by repeatedly calling
	 * {@link #mul(byte, byte)}.
	 * 
	 * @param a The first byte to be included in the product.
	 * @param bytes The remaining bytes to be included in the product.
	 * @return The resulting of calling {@link #mul(byte, byte)} on the given bytes.
	 * @see #mul(byte, byte)
	 */
	public static byte mul(byte a, byte[] bytes)
	{
		byte product = a;
		for(byte c : bytes)
			product = mul(product, c);
		return product;
	}
	
	public static byte div(byte a, byte b)
	{
		return div[a & 0xff][b & 0xff];
	}
	
	/**
	 * This adds the given bytes, which in GF(2<sup>8</sup>) is simply the same as applying XOR.
	 * 
	 * @param a The augend
	 * @param bytes Many addends
	 * @return The GF(2<sup>8</sup>) sum
	 */
	public static byte add(byte a, byte... bytes)
	{
		byte sum = a;
		for(byte bb : bytes)
			sum ^= bb;
		return sum;
	}
	
	/**
	 * This adds the given bytes, which in GF(2<sup>8</sup>) is simply the same as applying XOR.
	 * 
	 * @param bytes The bytes to add
	 * @return The GF(2<sup>8</sup>) sum
	 */
	public static byte add(byte[] bytes)
	{
		byte sum = 0;
		for(byte bb : bytes)
			sum ^= bb;
		return sum;
	}
	
	/**
	 * This subtracts two bytes, which in GF(2<sup>8</sup>) is simply the same as applying XOR. Note that this is the
	 * same as addition.
	 * 
	 * @param a The minuend
	 * @param bytes Many subtrahends
	 * @return The GF(2<sup>8</sup>) difference
	 */
	public static byte sub(byte a, byte... bytes)
	{
		return add(a, bytes);
	}
	
	/**
	 * This repeatedly multiplies the two bytes to arrive at the power.
	 * 
	 * @param i The base
	 * @param e The exponent
	 * @return The GF(2<sup>8</sup>) power
	 */
	public static byte pow(byte i, byte e)
	{
		if(e == 0)
			return 1;
		byte product = i;
		for(int count = 1; count < (e & 0xff); count++)
			product = mul(product, i);
		return (byte) product;
	}
	
	/**
	 * Calculates the dot product of the two given byte arrays.
	 * 
	 * @param vector1 The first byte array
	 * @param vector2 The second byte array
	 * @return The dot product, arrived at with addition and multiplication in GF(2<sup>8</sup>)
	 * @throws IllegalArgumentException if the vectors are not the same length
	 */
	public static byte dot(byte[] vector1, byte[] vector2)
	{
		if(vector1.length != vector2.length)
			throw new IllegalArgumentException("Byte vector lengths must be equal");
		
		int length = vector1.length;
		byte product = 0;
		for(int i = 0; i < length; i++)
			product = add(product, mul(vector1[i], vector2[i]));
		return product;
	}
	
	/**
	 * This multiplies the two bytes, as if they were unsigned, using the Russian peasant technique (modified to work
	 * with the Galois field)
	 * {@link en.wikipedia.org/wiki/Ancient_Egyptian_multiplication#Russian_peasant_multiplication}.
	 * 
	 * @param a The multiplier
	 * @param b The multiplicand
	 * @return The GF(2<sup>8</sup>) product
	 */
	private static byte slowMul(byte a, byte b)
	{
		byte counter = a;
		byte value = b;
		byte r = 0;
		byte t;
		while(counter != 0)
		{
			if((counter & 1) != 0) // if a is odd
				r = (byte) (r ^ value); // add value of b to the result
			t = (byte) (value & 0x80);
			value = (byte) (value << 1); // double b
			if(t != 0)
				value = (byte) (value ^ 0x1b); // add polynomial representation to b
			counter = (byte) ((counter & 0xff) >> 1); // divide a in half ( "& 0xff >>" equivalent to ">>>" for unsigned byte)
		}
		return r;
	}
	
	/**
	 * This multiplies the two bytes, as if they were unsigned, using GF(2<sup>8</sup>) logarithms and inverse
	 * logarithms: <code>
	 * product = ilog( log(multiplier) + log(multiplicand) )
	 * </code>
	 * 
	 * @param a The multiplier
	 * @param b The multiplicand
	 * @return The GF(2<sup>8</sup>) product
	 */
	private static byte fastMul(byte a, byte b)
	{
		if(a == 0 || b == 0)
			return 0;
		int t = (log[a & 0xff] & 0xff) + (log[b & 0xff] & 0xff);
		if(t > MAX_VALUE)
			t -= MAX_VALUE;
		return exp[t & 0xff];
	}
	
	/**
	 * This divides the two bytes, as if they were unsigned, using GF(2<sup>8</sup>) logarithms and inverse
	 * logarithms: <code>
	 * product = ilog( log(dividend) - log(divisor) )
	 * </code>
	 * 
	 * @param a The dividend
	 * @param b The divisor
	 * @return The GF(2<sup>8</sup>) quotient
	 */
	private static byte fastDiv(byte a, byte b)
	{
		if(a == 0 || b == 0)
			return 0;
		int t = (log[a & 0xff] & 0xff) - (log[b & 0xff] & 0xff);
		if(t < 0)
			t += MAX_VALUE;
		return exp[t & 0xff];
	}
}
