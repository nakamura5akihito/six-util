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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;



/**
 * A composit binding where
 * bindings are connected by a same logical operator.
 * The logical operator is "AND" or "OR".
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: LogicalBinding.java 492 2013-03-06 07:57:10Z nakamura5akihito@gmail.com $
 */
public abstract class LogicalBinding
    implements Binding, Iterable<Binding>
{
    private static final long  serialVersionUID = 421052390405521923L;




    /**
     * The binding list.
     */
    private final List<Binding>  _elements = new ArrayList<Binding>();



    /**
     * Default constructor.
     */
    public LogicalBinding()
    {
    }


    /**
     * Constructs a LogicalBinding with the specified augend and added bindings.
     *
     * @param   augend
     *  the left side binding.
     * @param   addend
     *  the right side binding.
     */
    public LogicalBinding(
                    final Binding augend,
                    final Binding addend
                    )
    {
        if (augend == null  ||  addend == null) {
            throw new IllegalArgumentException( "null binding" );
        }

        addElement( augend );
        addElement( addend );
    }


    /**
     * Constructs a LogicalBinding with the specified bindings.
     *
     * @param   elements
     *  the element bindings.
     */
    public LogicalBinding(
                    final List<? extends Binding> elements
                    )
    {
        setElements( elements );
    }


    /**
     * Constructs a LogicalBinding with the specified bindings.
     *
     * @param   elements
     *  the element bindings.
     */
    public LogicalBinding(
                    final Binding[] elements
                    )
    {
        setElements( Arrays.asList( elements ) );
    }



    /**
     * Sets the element bindings of this binding.
     *
     * <p>If the specified list is null or empty,
     * it clears the previous list.
     * If one of the element in the list is null,
     * the previous list is not modified and an exception is thrown.
     * </p>
     *
     * @param   bindings
     *  a list of element bindings.
     * @throws  IllegalArgumentException
     *  if the specified list contains a null element.
     *  The previous list of element bindings are preserved.
     */
    public void setElements(
                    final List<? extends Binding> bindings
                    )
    {
        if (bindings == null  ||  bindings.size() == 0) {
            clear();
            return;
        }

        for (Binding  binding : bindings) {
            if (binding == null) {
                throw new IllegalArgumentException( "null elemnt" );
            }
        }

        clear();
        for (Binding  binding : bindings) {
            addElement( binding );
        }
    }
//    public void setElements(
//                    final Binding[] bindings
//                    )
//    {
//        if (bindings == null  ||  bindings.length == 0) {
//            clear();
//            return;
//        }
//
//        List<Binding>  list = Arrays.asList( bindings );
//        for (Binding  binding : list) {
//            if (binding == null) {
//                throw new IllegalArgumentException( "array contains null" );
//            }
//        }
//
//        clear();
//        for (Binding  binding : list) {
//            addElement( binding );
//        }
//    }



    /**
     * Appends the specified binding to the end of the element bindings.
     *
     * @param   binding
     *  the binding to be appended.
     * @throws  IllegalArgumentException
     *  if the specified binding is null.
     */
    public void addElement(
                    final Binding binding
                    )
    {
        if (binding == null) {
            throw new IllegalArgumentException( "no binding specified" );
        }

        _elements.add( binding );
    }



    /**
     * Returns a list of the element bindings.
     *
     * @return
     *  a list of the element bindings.
     */
    public List<Binding> getElements()
    {
        return _elements;
    }
//    public Binding[] getElements()
//    {
//        Binding[]  empty = new Binding[0];
//        if (_elements == null) {
//            return empty;
//        }
//
//        return (Binding[])(_elements.toArray( empty ));
//    }



    /**
     * Returns the element binding at the specified position.
     *
     * @param   index
     *  the index of the first entry is 0, the second is 1, ...
     * @return
     *  a list of the element bindings.
     * @throws  IndexOutOfBoundsException
     *  if the index is out of range.
     */
    public Binding getElementAt(
                    final int index
                    )
    {
        return _elements.get( index );
    }



//    /**
//     * Returns an iterator over the element bindings.
//     *
//     * @return
//     *  an iterator over the element bindings.
//     */
//    public Iterator<Binding> elements()
//    {
//        return _elements.iterator();
//    }


    /**
     * Removes all of the element bindings from this binding.
     */
    public void clear()
    {
        _elements.clear();
    }



    /**
     * Returns the number of element bindings.
     *
     * @return
     *  the number of element bindings.
     */
    public int size()
    {
        return _elements.size();
    }



    //**************************************************************
    //  Iterable
    //**************************************************************

    public Iterator<Binding> iterator()
    {
        return _elements.iterator();
    }



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    /**
     * Determines whether another object is equal to this LogicalBinding.
     * The result is true if and only if the argument is not null
     * and is a LogicalBinding object that has the same property
     * and value list.
     *
     * @param   obj
     *  the object to test for equality with this LogicalBinding.
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

        if (!(obj instanceof LogicalBinding)) {
            return false;
        }

        LogicalBinding  other = (LogicalBinding)obj;
        final List<Binding>  other_elements = other.getElements();
        final List<Binding>   this_elements =  this.getElements();
        if (this_elements == other_elements
                        ||  (this_elements != null
                                        &&  this_elements.equals( other_elements ))) {
                    return true;
        }

        return false;
    }



    /**
     * Computes the hash code for this LogicalBinding.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        final int  prime = 37;
        int  result = 17;

        List<Binding>  elements = getElements();
        result = prime * result + (elements == null ? 0 : elements.hashCode());

        return result;
    }



    /**
     * Returns a string representation of this LogicalBinding.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this LogicalBinding.
     */
    @Override
    public String toString()
    {
        return String.valueOf( getElements() );
    }

}
// LogicalBinding
