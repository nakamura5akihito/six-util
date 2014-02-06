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

import java.io.Serializable;



/**
 * A sorting order constraint.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: Order.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class Order
    implements Serializable
{
    private static final long  serialVersionUID = -8687323219153625259L;



    /**
     * The property by which the result objects are sorted.
     */
    private String  _property;



    /**
     * The order flag.
     * True means to sort in reverse order (descending);
     * false does ascending.
     */
    private boolean  _descending = false;




    /**
     * Constructor.
     */
    public Order()
    {
    }



    /**
     * Constructs an Order with the specified property.
     *
     * @param   property
     *  the property.
     */
    public Order(
                    final String property
                    )
    {
        _property = property;
    }



    /**
     * Constructs an Order with the specified target property and order.
     *
     * @param   property
     *  the property.
     * @param   desc
     *  true if the sort order is descending;
     *  otherwise false.
     */
    public Order(
                    final String property,
                    final boolean desc
                    )
    {
        this( property );
        _descending = desc;
    }



    /**
     * Sets the target property.
     *
     * @param   property
     *  the target property.
     */
    public void setProperty(
                    final String property
                    )
    {
        _property = property;
    }


    /**
     * Returns the target property.
     *
     * @return
     *  the target property.
     */
    public String getProperty()
    {
        return _property;
    }



    /**
     * Sets the sort order.
     * To sort in reverse order (descending),
     * set the parameter <code>desc</code> to true.
     * False means ascending order.
     *
     * @param   desc
     *  the sort order.
     *  True means descending order and false does ascending order.
     */
    public void setDescending(
                    final boolean desc
                    )
    {
        _descending = desc;
    }


    /**
     * Returns true if the the sort order is descending.
     *
     * @return
     *  true if the the sort order is descending.
     */
    public boolean isDescending()
    {
        return _descending;
    }




    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    /**
     * Determines whether another object is equal to this Order.
     * The result is true if and only if the argument is not null
     * and is an Order object that has the same property and sorting order.
     *
     * @param   obj
     *  the object to test for equality with this Order.
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

        if (!(obj instanceof Order)) {
            return false;
        }

        final Order  other = (Order)obj;
        if (isDescending() == other.isDescending()) {
            final String   this_p =  this.getProperty();
            final String  other_p = other.getProperty();
            if (this_p == other_p
                    ||  (this_p != null  &&  this_p.equals( other_p ))) {
                return true;
            }
        }

        return false;
    }



    /**
     * Computes the hash code for this Order.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        final int  prime = 37;
        int  result = 17;

        final String  p = getProperty();
        result = prime * result + (p == null ? 0 : p.hashCode());

        result = prime * result + (isDescending() ? 0 : 1);

        return result;
    }



    /**
     * Returns a string representation of this Order.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this Order.
     */
    @Override
    public String toString()
    {
        return "Order[property=" + getProperty()
            + (isDescending() ? ",DESC]" : "]");
    }

}
// Order
