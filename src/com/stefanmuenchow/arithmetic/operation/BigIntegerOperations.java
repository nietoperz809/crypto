package com.stefanmuenchow.arithmetic.operation;

import com.stefanmuenchow.arithmetic.Operations;

import java.math.BigInteger;

public class BigIntegerOperations implements Operations<BigInteger>
{

    @Override
    public BigInteger add (BigInteger a, BigInteger b)
    {
        return a.add(b);
    }

    @Override
    public BigInteger sub (BigInteger a, BigInteger b)
    {
        return a.subtract(b);
    }

    @Override
    public BigInteger mul (BigInteger a, BigInteger b)
    {
        return a.multiply(b);
    }

    @Override
    public BigInteger div (BigInteger a, BigInteger b)
    {
        return a.divide(b);
    }

    @Override
    public BigInteger max (BigInteger a, BigInteger b)
    {
        return a.max(b);
    }

    @Override
    public BigInteger min (BigInteger a, BigInteger b)
    {
        return a.min(b);
    }

    @Override
    public BigInteger abs (BigInteger a)
    {
        return a.abs();
    }

    @Override
    public BigInteger neg (BigInteger a)
    {
        return a.negate();
    }

    @Override
    public BigInteger mod (BigInteger a, BigInteger b)
    {
        return a.mod(b);
    }

    @Override
    public BigInteger and (BigInteger a, BigInteger b)
    {
        return a.and(b);
    }

    @Override
    public BigInteger or (BigInteger a, BigInteger b)
    {
        return a.or(b);
    }

    @Override
    public BigInteger xor (BigInteger a, BigInteger b)
    {
        return a.xor(b);
    }

    @Override
    public BigInteger equ (BigInteger a, BigInteger b)
    {
        return (a.xor(b)).negate();
    }

    @Override
    public BigInteger imp (BigInteger a, BigInteger b)
    {
        return (a.xor(b).negate()).or(b);
    }
}

//  ((~(a ^ b)) | b);

