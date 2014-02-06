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
 * A NullBinding is a restriction on the property value with NULL.
 * There are two modes: affirmation (default) and negation.
 * If the mode is affirmation, the property value must be NULL.
 * While in negation mode, the value is NOT NULL.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: NullBinding.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 * @see Relation
 */
public class NullBinding
    extends PropertyBinding
{

    /**
     * The default negation mode: false.
     */
    public static final boolean  DEFAULT_NOT_NULL = false;



    /**
     */
    private boolean  _notNull = DEFAULT_NOT_NULL;



    /**
     * Default constructor.
     */
    public NullBinding()
    {
    }


    /**
     */
    public NullBinding(
                    final String property
                    )
    {
        this( property, DEFAULT_NOT_NULL );
    }


    /**
     */
    public NullBinding(
                    final String property,
                    final boolean notNull
                    )
    {
        super( property );
        setNotNull( notNull );
    }



    /**
     */
    public void setNotNull(
                    final boolean notNull
                    )
    {
        _notNull = notNull;
    }



    /**
     * Returns the mode.
     * It returns true if the mode is negation,
     * true otherwise.
     */
    public boolean isNotNull()
    {
        return _notNull;
    }



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    /**
     * Determines whether another object is equal to this NullBinding.
     *
     * @param   obj
     *  the object to test for equality with this NullBinding.
     * @return
     *  true if the given object equals this one;
     *  false otherwise.
     */
    @Override
    public boolean equals(
                    final Object obj
                    )
    {
        if (!(obj instanceof NullBinding)) {
            return false;
        }

        if (super.equals( obj )) {
            NullBinding  other = (NullBinding)obj;
            return (isNotNull()  ==  other.isNotNull());
        }

        return false;
    }



    /**
     * Computes the hash code for this NullBinding.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        final int  prime = 37;
        int  result = super.hashCode();

        result = prime * result + (isNotNull() ? 0 : 1);

        return result;
    }



    /**
     * Returns a string representation of this NullBinding.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this NullBinding.
     */
    @Override
    public String toString()
    {
        return "" + getProperty() + " IS "
            + (isNotNull() ? "NOT NULL" : "NULL");
    }

}
// NullBinding
