package com.stefanmuenchow.arithmetic.operation;

import com.stefanmuenchow.arithmetic.Operations;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class FloatOperations implements Operations<Float>
{

	@Override
	public Float add(Float a, Float b) {
		return a + b;
	}

	@Override
	public Float sub(Float a, Float b) {
		return a - b;
	}

	@Override
	public Float mul(Float a, Float b) {
		return a * b;
	}

	@Override
	public Float div(Float a, Float b) {
		return a / b;
	}

	@Override
	public Float max(Float a, Float b) {
		return Math.max(a, b);
	}

	@Override
	public Float min(Float a, Float b) {
		return Math.min(a, b);
	}

	@Override
	public Float abs(Float a) {
		return Math.abs(a);
	}

	@Override
	public Float neg(Float a) {
		return a * (-1);
	}

    @Override
    public Float mod (Float a, Float b)
    {
        return a % b;
    }

    @Override
    public Float and (Float a, Float b)
    {
        throw new NotImplementedException();
    }

    @Override
    public Float or (Float a, Float b)
    {
        throw new NotImplementedException();
    }

    @Override
    public Float xor (Float a, Float b)
    {
        throw new NotImplementedException();
    }

    @Override
    public Float equ (Float a, Float b)
    {
        throw new NotImplementedException();
    }

    @Override
    public Float imp (Float a, Float b)
    {
        throw new NotImplementedException();
    }

	@Override
	public int compare (Float a, Float b)
	{
		return (int)(a-b);
	}
}
