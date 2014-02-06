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
 * A kind of the PropertyBinding that restricts a string property
 * to the matching pattern.
 *
 * <p>
 * This class does not define the meta characters
 * for regular expressions.
 * Therefore, the interpretation of the matching pattern is
 * left to users.
 * </p>
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: LikeBinding.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class LikeBinding
    extends PropertyBinding
{
    private static final long  serialVersionUID = 3485964549468893824L;



    /**
     * The matching pattern.
     */
    private String  _pattern;



    /**
     * Default constructor.
     */
    public LikeBinding()
    {
    }


    /**
     * Constructs a LikeBinding with the specified property
     * and matching pattern.
     *
     * @param   property
     *  the property to restrict.
     * @param   pattern
     *  the matching pattern.
     */
    public LikeBinding(
                    final String property,
                    final String pattern
                    )
    {
        super( property );
        setPattern( pattern );
    }



    /**
     * Sets the matching pattern of this binding.
     *
     * @param   pattern
     *  the matching pattern.
     */
    public void setPattern(
                    final String pattern
                    )
    {
        _pattern = pattern;
    }



    /**
     * Returns the matching pattern of this binding.
     *
     * @return
     *  the matching pattern.
     */
    public String getPattern()
    {
        return _pattern;
    }



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    /**
     * Determines whether another object is equal to this LikeBinding.
     * The result is true if and only if the argument is not null
     * and is a LikeBinding object that has the same property,
     * relation type, and value.
     *
     * @param   obj
     *  the object to test for equality with this LikeBinding.
     * @return
     *  true if the given object equals this one;
     *  false otherwise.
     */
    @Override
    public boolean equals(
                    final Object obj
                    )
    {
        if (!(obj instanceof LikeBinding)) {
            return false;
        }

        if (super.equals( obj )) {
            LikeBinding  other = (LikeBinding)obj;
            Object  other_pattern = other.getPattern();
            Object   this_pattern =  this.getPattern();
            if (other_pattern == this_pattern
                    ||  (other_pattern != null  &&  other_pattern.equals( this_pattern ))) {
                return true;
            }
        }

        return false;
    }



    /**
     * Computes the hash code for this LikeBinding.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        final int  prime = 37;
        int  result = super.hashCode();

        String  pattern = getPattern();
        result = prime * result + (pattern == null ? 0 : pattern.hashCode());

        return result;
    }



    /**
     * Returns a string representation of this LikeBinding.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this LikeBinding.
     */
    @Override
    public String toString()
    {
        return "" + getProperty() + " LIKE " + getPattern();
    }

}
// LikeBinding
