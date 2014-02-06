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
 * A binding that negates a binding.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: NotBinding.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class NotBinding
    implements Binding
{
    private static final long  serialVersionUID = -8103978922062218638L;




    /**
     */
    private Binding  _bindingToNegate;




    /**
     * Default constructor.
     */
    public NotBinding()
    {
    }



    /**
     * Constructs a NotBinding with the specified binding to negate.
     *
     * @param   bindingToNegate
     *  the binding to negate.
     * @throws  IllegalArgumentException
     *  if the specified binding is 'this' binding.
     */
    public NotBinding(
                    final Binding bindingToNegate
                    )
    {
        setBindingToNegate( bindingToNegate );
    }




    /**
     * Sets the binding to negate.
     *
     * @param   binding
     *  the binding to negate.
     * @throws  IllegalArgumentException
     *  if the specified binding is 'this' binding.
     */
    public void setBindingToNegate(
                    final Binding binding
                    )
    {
        if (binding == null) {
            throw new IllegalArgumentException( "no binding to negate specified" );
        }

        if (binding == this) {
            throw new IllegalArgumentException( "binding to negate is 'this' binding" );
        }

        _bindingToNegate = binding;
    }



    /**
     * Gets the binding to negate.
     *
     * @return
     *  the binding to negate.
     */
    public Binding getBindingToNegate()
    {
        return _bindingToNegate;
    }




    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    /**
     * Determines whether another object is equal to this NotBinding.
     * The result is true if and only if the argument is not null
     * and is a NotBinding object that has the same property,
     * relation type, and value.
     *
     * @param   obj
     *  the object to test for equality with this NotBinding.
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

        if (!(obj instanceof NotBinding)) {
            return false;
        }

        NotBinding  other = (NotBinding)obj;

        Binding  other_binding = other.getBindingToNegate();
        Binding   this_binding =       getBindingToNegate();
        if (this_binding == other_binding
                ||  (this_binding != null
                                &&  this_binding.equals( other_binding ))) {
            return true;
        }

        return false;
    }



    /**
     * Computes the hash code for this NotBinding.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        final int  prime = 37;
        int  result = 17;

        Binding  binding = getBindingToNegate();
        result = prime * result + (binding == null ? 0 : binding.hashCode());

        return result;
    }



    /**
     * Returns a string representation of this NotBinding.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this NotBinding.
     */
    @Override
    public String toString()
    {
        return "NOT[" + getBindingToNegate() + "]";
    }

}
// NotBinding
