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
 * A projection by a property.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: PropertyProjection.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class PropertyProjection
    implements Projection
{
//    private static final long  serialVersionUID = ;


    public static final PropertyProjection  ALL_PROJECTION =
        new PropertyProjection( "*" );



    /**
     * The projection property.
     */
    private String  _property;



    /**
     * Constructor.
     */
    public PropertyProjection()
    {
    }



    /**
     * Constructs a PropertyProjection with the specified property.
     *
     * @param   property
     *  the property.
     */
    public PropertyProjection(
                    final String property
                    )
    {
        setProperty( property );
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



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    /**
     * Determines whether another object is equal to this PropertyProjection.
     * The result is true if and only if the argument is not null
     * and is an PropertyProjection object that has the same property.
     *
     * @param   obj
     *  the object to test for equality with this PropertyProjection.
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

        if (!(obj instanceof PropertyProjection)) {
            return false;
        }

        final PropertyProjection  other = (PropertyProjection)obj;
        final String   this_p =  this.getProperty();
        final String  other_p = other.getProperty();
        if (this_p == other_p
                        ||  (this_p != null  &&  this_p.equals( other_p ))) {
                return true;
        }

        return false;
    }



    /**
     * Computes the hash code for this PropertyProjection.
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

        return result;
    }



    /**
     * Returns a string representation of this PropertyProjection.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this PropertyProjection.
     */
    @Override
    public String toString()
    {
        return "PropertyProjection[property=" + getProperty()
            + "]";
    }

}
// PropertyProjection
