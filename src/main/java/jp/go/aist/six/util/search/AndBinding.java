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

import java.util.List;



/**
 * A logical binding that connects element bindings by the "AND" operator.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: AndBinding.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class AndBinding
    extends LogicalBinding
{
    private static final long  serialVersionUID = -2194120364635429966L;



    /**
     * Default constructor.
     */
    public AndBinding()
    {
    }


    /**
     * Constructs an AndBinding with the specified augend
     * and addend bindings.
     *
     * @param   augend
     *  the left side binding.
     * @param   addend
     *  the right side binding.
     */
    public AndBinding(
                    final Binding augend,
                    final Binding addend
                    )
    {
        super( augend, addend );
    }


    /**
     * Constructs an AndBinding with the specified bindings.
     *
     * @param   elements
     *  the element bindings.
     */
    public AndBinding(
                    final List<? extends Binding> elements
                    )
    {
        super( elements );
    }


    /**
     * Constructs an AndBinding with the specified bindings.
     *
     * @param   elements
     *  the element bindings.
     */
    public AndBinding(
                    final Binding[] elements
                    )
    {
        super( elements );
    }



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    /**
     * Determines whether another object is equal to this AndBinding.
     * The result is true if and only if the argument is not null
     * and is an AndBinding object that has the same property,
     * relation type, and value.
     *
     * @param   obj
     *  the object to test for equality with this AndBinding.
     * @return
     *  true if the given object equals this one;
     *  false otherwise.
     */
    @Override
    public boolean equals(
                    final Object obj
                    )
    {
        if (!(obj instanceof AndBinding)) {
            return false;
        }

        return super.equals( obj );
    }



    /**
     * Computes the hash code for this AndBinding.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }



    /**
     * Returns a string representation of this AndBinding.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this AndBinding.
     */
    @Override
    public String toString()
    {
        return "AND" + super.toString();
    }

}
// AndBinding
