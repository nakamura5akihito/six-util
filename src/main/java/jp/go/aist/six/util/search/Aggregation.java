/**
 * SIX UTIL - https://staff.aist.go.jp/nakamura-akihito/six/util/
 * Copyright (C) 2008
 *   National Institute of Advanced Industrial Science and Technology (AIST)
 *   Registration Number: H20PRO-863
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.go.aist.six.util.search;



/**
 * An aggregation of sets of objects.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: Aggregation.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 * @see Function
 */
public class Aggregation
    implements Projection
{
    private static final long  serialVersionUID = 3941437174859447411L;



    /**
     * An expression that denotes all the objects: "*".
     */
    public static final String  WHOLE_OBJECT_EXPRESSION = "*";



    /**
     * Creates a 'COUNT(*)' aggregation.
     * This is a shortcut to:
     * <blockquote>
     *   <code>new Aggregation( "*", AggregateFunction.COUNT )</code>
     * </blockquote>
     *
     * @return
     *  a 'COUNT(*)' aggregation.
     */
    public static Aggregation countAll()
    {
        return (new Aggregation( WHOLE_OBJECT_EXPRESSION, Function.COUNT ));
    }




    /**
     * Creates a 'COUNT' aggregation.
     * This is a shortcut to:
     * <blockquote>
     *   <code>new Aggregation( expr, AggregateFunction.COUNT )</code>
     * </blockquote>
     *
     * @param   expr
     *  the expression.
     * @return
     *  a 'COUNT' aggregation.
     */
    public static Aggregation count(
                    final String expr
                    )
    {
        return (new Aggregation( expr, Function.COUNT ));
    }



    /**
     * Creates a 'MAX' aggregation.
     *
     * @param   expr
     *  the expression.
     * @return
     *  a 'MAX' aggregation.
     */
    public static Aggregation max(
                    final String expr
                    )
    {
        return (new Aggregation( expr, Function.MAX ));
    }



    /**
     * Creates a 'MIN' aggregation.
     *
     * @param   expr
     *  the expression.
     * @return
     *  a 'MIN' aggregation.
     */
    public static Aggregation min(
                    final String expr
                    )
    {
        return (new Aggregation( expr, Function.MIN ));
    }



    /**
     * Creates a 'SUM' aggregation.
     *
     * @param   expr
     *  the expression.
     * @return
     *  a 'SUM' aggregation.
     */
    public static Aggregation sum(
                    final String expr
                    )
    {
        return (new Aggregation( expr, Function.SUM ));
    }



    /**
     * Creates a 'AVG' aggregation.
     *
     * @param   expr
     *  the expression.
     * @return
     *  a 'AVG' aggregation.
     */
    public static Aggregation avg(
                    final String expr
                    )
    {
        return (new Aggregation( expr, Function.AVG ));
    }





    /**
     * The expression.
     */
    private String  _expression;



    /**
     * The aggregate function.
     */
    private Function  _function;

    public static final Function  DEFAULT_FUNCTION = Function.COUNT;




    /**
     * Default constructor.
     */
    public Aggregation()
    {
        this( WHOLE_OBJECT_EXPRESSION, DEFAULT_FUNCTION );
    }



    /**
     * Constructs an AggregateFunction with the specified expression
     * and function.
     *
     * @param   expr
     *  the expression.
     * @param   function
     *  the function.
     */
    public Aggregation(
                    final String expr,
                    final Function function
                    )
    {
        setExpression( expr );
        setFunction( function );
    }




    /**
     * Sets the expression.
     *
     * @param   expr
     *  the expression.
     */
    public void setExpression(
                    final String expr
                    )
    {
        _expression = expr;
    }



    /**
     * Returns the expression.
     *
     * @return
     *  the expression.
     */
    public String getExpression()
    {
        return _expression;
    }



    /**
     * Sets the aggregate function.
     *
     * @param   function
     *  the function.
     */
    public void setFunction(
                    final Function function
                    )
    {
        _function = function;
    }



    /**
     * Returns the aggregate function.
     *
     * @return
     *  the function.
     */
    public Function getFunction()
    {
        return (_function == null ? DEFAULT_FUNCTION : _function);
    }



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    /**
     * Determines whether another object is equal to this Aggregation.
     * The result is true if and only if the argument is not null
     * and is an Aggregation object that has the same expression
     * and function.
     *
     * @param   obj
     *  the object to test for equality with this Aggregation.
     * @return
     *  true if the given object equals this one;
     *  false otherwise.
     */
    @Override
    public boolean equals(
                    final Object obj
                    )
    {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Aggregation)) {
            return false;
        }

        Aggregation  other = (Aggregation)obj;
        String  other_expr = other.getExpression();
        String   this_expr =  this.getExpression();
        if (other_expr == this_expr
                ||  (other_expr != null  &&  other_expr.equals( this_expr ))) {
            if (other.getFunction() == this.getFunction()) {
                return true;
            }
        }

        return false;
    }



    /**
     * Computes the hash code for this Aggregation.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        final int  prime = 37;
        int  result = 17;

        String  expr = getExpression();
        result = prime * result + (expr == null ? 0 : expr.hashCode());

        Function  func = getFunction();
        result = prime * result + (func == null ? 0 : func.hashCode());

        return result;
    }



    /**
     * Returns a string representation of this Aggregation.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this Aggregation.
     */
    @Override
    public String toString()
    {
        return "Aggregation[expression=" + getExpression()
                    + ", function=" + getFunction()
                    + "]";
    }

}
// Aggregation
