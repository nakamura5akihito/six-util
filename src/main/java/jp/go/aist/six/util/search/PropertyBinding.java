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
 * A PropertyBinding represents a restriction on a property of object.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: PropertyBinding.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public abstract class PropertyBinding
    implements Binding
{
    private static final long  serialVersionUID = -5015282155036621389L;



    /**
     * The property.
     */
    private String  _property;



    /**
     * Default constructor.
     */
    public PropertyBinding()
    {
    }


    /**
     * Constructs a PropertyBinding for the specified property.
     *
     * @param   property
     *  the property to be restricted.
     */
    public PropertyBinding(
                    final String property
                    )
    {
        setProperty( property );
    }




    /**
     * Sets the property being restricted.
     *
     * @param   property
     *  the property.
     */
    public void setProperty(
                    final String property
                    )
    {
        _property = property;
    }


    /**
     * Returns the property being restricted.
     *
     * @return
     *  the property.
     */
    public String getProperty()
    {
        return _property;
    }



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    /**
     * Determines whether another object is equal to this PropertyBinding.
     *
     * @param   obj
     *  the object to test for equality with this PropertyBinding.
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

        if (!(obj instanceof PropertyBinding)) {
            return false;
        }

        PropertyBinding  other = (PropertyBinding)obj;
        String  other_prop = other.getProperty();
        String   this_prop =       getProperty();
        if (other_prop == this_prop
                ||  (other_prop != null  &&  other_prop.equals( this_prop ))) {
            return true;
        }

        return false;
    }



    /**
     * Computes the hash code for this PropertyBinding.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        final int  prime = 37;
        int  result = 17;

        String  prop = getProperty();
        result = prime * result + (prop == null ? 0 : prop.hashCode());

        return result;
    }

}
// PropertyBinding
