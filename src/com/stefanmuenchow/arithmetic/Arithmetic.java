/**
 * Copyright (c) Stefan Münchow. All rights reserved.
 * <p>
 * The use and distribution terms for this software are covered by the
 * Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 * which can be found in the file epl-v10.html at the root of this distribution.
 * By using this software in any fashion, you are agreeing to be bound by
 * the terms of this license.
 * You must not remove this notice, or any other, from this software.
 **/

package com.stefanmuenchow.arithmetic;

import com.stefanmuenchow.arithmetic.converter.ConverterRepository;
import com.stefanmuenchow.arithmetic.operation.OperationsRepository;

import java.lang.reflect.Array;

/**
 * Provides generic arithmetic for all subclasses of class {@link Number}.
 *
 * @author Stefan Münchow
 */
public class Arithmetic<X extends Number> extends Number
{
    private Class<X> targetClass;
    private X value;
    private transient TypeConverter<X> converter;
    private transient Operations<X> operations;

    /**
     * Create a new instance with initial value.
     *
     * @param value    Initial value
     */
    @SuppressWarnings("unchecked")
    public Arithmetic (X value)
    {
        this.targetClass = (Class<X>) value.getClass();
        this.value = value;
    }

    /**
     * Constructor
     * @param clazz Type of work Object of this class
     */
    public Arithmetic (Class<X> clazz)
    {
        try
        {
            this.targetClass = clazz;
            this.value = createNumberObject(clazz); //clazz.getConstructor (String.class).newInstance("0");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new target type for generic Arithmetic. After this method is called once,
     * the new target type is known by all newly created {@link Arithmetic} instances.
     *
     * @param <Y>			Target type
     * @param targetType    Class object of target type
     * @param converter        {@link TypeConverter} for target type
     * @param operations    {@link Operations} for target type
     */
    public static <Y extends Number> void addTargetType (Class<Y> targetType,
                                                         TypeConverter<Y> converter, Operations<Y> operations)
    {
        ConverterRepository.getInstance().addConverter(targetType, converter);
        OperationsRepository.getInstance().addOperations(targetType, operations);
    }

    /**
     * Return current value of this instance.
     * @return Current value of type X.
     */
    public X value ()
    {
        return value;
    }

    /**
     * Get the Type of work object used by this class
     * @return see above
     */
    public Class<X> type ()
    {
        return targetClass;
    }


    /**
     * Add operand to current value of this instance.
     *
     * @param <A>		Operand type
     * @param operand    Operand value
     * @return            {@link Arithmetic} with modified value
     */
    public <A extends Number> Arithmetic<X> add (A operand)
    {
        value = getOperations().add(value, convertNumber(operand));
        return this;
    }

    public static <A extends Number> Arithmetic<A> add (A op1, A op2)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.add(op2);
        return ar;
    }

    private Operations<X> getOperations ()
    {
        if (operations == null)
        {
            operations = OperationsRepository.getInstance().getOperations(targetClass);
        }
        return operations;
    }

    private X convertNumber (Number n)
    {
        if (converter == null)
        {
            converter = ConverterRepository.getInstance().getConverter(targetClass);
        }
        return converter.convertNumber(n);
    }

    /**
     * Subtract operand from current value of this instance.
     *
     * @param <A>		Operand type
     * @param operand    Operand value
     * @return            {@link Arithmetic} with modified value
     */
    public <A extends Number> Arithmetic<X> sub (A operand)
    {
        value = getOperations().sub(value, convertNumber(operand));
        return this;
    }

    public static <A extends Number> Arithmetic<A> sub (A op1, A op2)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.sub(op2);
        return ar;
    }

    /**
     * Divide current value of this instance by operand.
     *
     * @param <A>		Operand type
     * @param operand    Operand value
     * @return            {@link Arithmetic} with modified value
     */
    public <A extends Number> Arithmetic<X> div (A operand)
    {
        value = getOperations().div(value, convertNumber(operand));
        return this;
    }

    public static <A extends Number> Arithmetic<A> div (A op1, A op2)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.div(op2);
        return ar;
    }
    /**
     * Multiply current value of this instance with operand.
     *
     * @param <A>		Operand type
     * @param operand    Operand value
     * @return            {@link Arithmetic} with modified value
     */
    public <A extends Number> Arithmetic<X> mul (A operand)
    {
        value = getOperations().mul(value, convertNumber(operand));
        return this;
    }

    public static <A extends Number> Arithmetic<A> mul (A op1, A op2)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.mul(op2);
        return ar;
    }

    /**
     * Return maximum value of current value of this instance and operand.
     *
     * @param <A>		Operand type
     * @param operand    Operand value
     * @return            {@link Arithmetic} with maximum as new value
     */
    public <A extends Number> Arithmetic<X> max (A operand)
    {
        value = getOperations().max(value, convertNumber(operand));
        return this;
    }

    public static <A extends Number> Arithmetic<A> max (A op1, A op2)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.max(op2);
        return ar;
    }

    /**
     * Return minimum value of current value of this instance and operand.
     *
     * @param <A>		Operand type
     * @param operand    Operand value
     * @return            {@link Arithmetic} with minimum as new value
     */
    public <A extends Number> Arithmetic<X> min (A operand)
    {
        value = getOperations().min(value, convertNumber(operand));
        return this;
    }

    public static <A extends Number> Arithmetic<A> min (A op1, A op2)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.min(op2);
        return ar;
    }

    /**
     * Create absolute of the current value of this instance.
     *
     * @param <A>		Operand type
     * @return            {@link Arithmetic} with absolute value
     */
    public <A extends Number> Arithmetic<X> abs ()
    {
        value = getOperations().abs(value);
        return this;
    }

    public static <A extends Number> Arithmetic<A> abs (A op1)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.abs();
        return ar;
    }

    /**
     * Negate the current value of this instance.
     *
     * @param <A>		Operand type
     * @return            {@link Arithmetic} with negated value
     */
    public <A extends Number> Arithmetic<X> neg ()
    {
        value = getOperations().neg(value);
        return this;
    }

    public static <A extends Number> Arithmetic<A> neg (A op1)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.neg();
        return ar;
    }

    public <A extends Number> Arithmetic<X> and (A operand)
    {
        value = getOperations().and(value, convertNumber(operand));
        return this;
    }

    public static <A extends Number> Arithmetic<A> and (A op1, A op2)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.and(op2);
        return ar;
    }

    public <A extends Number> Arithmetic<X> or (A operand)
    {
        value = getOperations().or(value, convertNumber(operand));
        return this;
    }

    public static <A extends Number> Arithmetic<A> or (A op1, A op2)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.or(op2);
        return ar;
    }

    public <A extends Number> Arithmetic<X> xor (A operand)
    {
        value = getOperations().xor(value, convertNumber(operand));
        return this;
    }

    public static <A extends Number> Arithmetic<A> xor (A op1, A op2)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.xor(op2);
        return ar;
    }

    public <A extends Number> Arithmetic<X> equ (A operand)
    {
        value = getOperations().equ(value, convertNumber(operand));
        return this;
    }

    public static <A extends Number> Arithmetic<A> equ (A op1, A op2)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.equ(op2);
        return ar;
    }

    public <A extends Number> Arithmetic<X> imp (A operand)
    {
        value = getOperations().imp(value, convertNumber(operand));
        return this;
    }

    public static <A extends Number> Arithmetic<A> imp (A op1, A op2)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.imp(op2);
        return ar;
    }

    public <A extends Number> Arithmetic<X> mod (A operand)
    {
        value = getOperations().mod(value, convertNumber(operand));
        return this;
    }

    public static <A extends Number> Arithmetic<A> mod (A op1, A op2)
    {
        Arithmetic<A> ar = new Arithmetic<>(op1);
        ar.mod(op2);
        return ar;
    }

    public X[] createNumberArray (int size)
    {
        return (X[]) Array.newInstance(targetClass, size);
    }

    public X[][] createNumberArray (int w, int h)
    {
        return (X[][]) Array.newInstance(targetClass, w,h);
    }

    public X createNumberObject (String num)
    {
        return createNumberObject(targetClass, num);
    }

    public X createNumberObject (int num)
    {
        return createNumberObject(targetClass, ""+num);
    }

    public X createNumberObject (double num)
    {
        return createNumberObject(targetClass, ""+num);
    }

/////////////////////// static funcs /////////////////////

    public static <A extends Number> A createNumberObject (Class<A> clazz)
    {
        return createNumberObject(clazz, "0");
    }

    public static <A extends Number> A createNumberObject (Class<A> clazz, String init)
    {
        try
        {
            return (A) clazz.getConstructor (String.class).newInstance(init);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static <A extends Number> A[] createNumberArray (Class<A> clazz, int size)
    {
        return (A[]) Array.newInstance(clazz, size);
    }

    public static <A extends Number> A[][] createNumberArray (Class<A> clazz, int w, int h)
    {
        return (A[][]) Array.newInstance(clazz, w,h);
    }

    @Override
    public int intValue ()
    {
        return value.intValue();
    }

    @Override
    public long longValue ()
    {
        return value.longValue();
    }

    @Override
    public float floatValue ()
    {
        return value.floatValue();
    }

    @Override
    public double doubleValue ()
    {
        return value.doubleValue();
    }
}
