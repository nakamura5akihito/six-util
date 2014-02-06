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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



/**
 * The SearchResult encapsulates the result of search.
 * The result is a list of objects.
 * A timestamp can be assigned to the result to denote
 * the date/time when the search was executed,
 * but the usage of this property is not limited.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: SearchResult.java 492 2013-03-06 07:57:10Z nakamura5akihito@gmail.com $
 */
public class SearchResult<T>
    implements Serializable, Iterable<T>
{
    private static final long  serialVersionUID = -5405606853917556638L;



    /**
     * The result objects (ordered).
     */
    private final List<T>  _elements = new ArrayList<T>();



    /**
     * The timestamp of this result.
     */
    private Date  _timestamp;



    /**
     * The search criteria that generated this result.
     */
    //private SearchCriteria  _criteria;



    /**
     * Constructor.
     */
    public SearchResult()
    {
        this( new Date(), null );
    }


    /**
     * Constructor.
     *
     * @param   timestamp
     *  the timestamp of this result.
     */
    public SearchResult(
                    final Date timestamp
                    )
    {
        this( timestamp, null );
    }


    /**
     * Constructor.
     *
     * @param   elements
     *  the result objects.
     */
    public SearchResult(
                    final Collection<? extends T> elements
                    )
    {
        this( new Date(), elements );
    }


    /**
     * Constructor.
     *
     * @param   elements
     *  the result objects.
     * @param   timestamp
     *  the timestamp of this result.
     */
    public SearchResult(
                    final Date timestamp,
                    final Collection<? extends T> elements
                    )
    {
        setTimestamp( timestamp );
        setElements( elements );
    }



    /**
     * Sets the result objects.
     * The objects are appended in the order that they are returned
     * by the collection's iterator.
     *
     * @param   elements
     *  the result objects.
     */
    public void setElements(
                    final Collection<? extends T> elements
                    )
    {
        clear();
        if (elements == null  ||  elements.size() == 0) {
            return;
        }

        for (T  obj : elements) {
            addElement( obj );
        }
    }


    /**
     * Sets the result objects.
     * The objects are appended in the order that they are returned
     * by the collection's iterator.
     *
     * @param   elements
     *  the result objects.
     */
    public void addElements(
                    final Collection<? extends T> elements
                    )
    {
        if (elements == null  ||  elements.size() == 0) {
            return;
        }

        for (T  obj : elements) {
            addElement( obj );
        }
    }


    /**
     * Appends the specified object to the end of the result list.
     *
     * @param   element
     *  the object to be appended.
     */
    public void addElement(
                    final T element
                    )
    {
        _elements.add( element );
    }


    /**
     * Returns all the result objects.
     *
     * @return
     *  the result objects.
     */
    public List<T> getElements()
    {
        return _elements;
    }


    /**
     * Returns the result object at the specified position.
     *
     * @param   index
     *  the index of the first entry is 0, the second is 1, ...
     * @return
     *  the object at the specified position.
     * @throws  IndexOutOfBoundsException
     *  if the index is out of range.
     */
    public T getElementAt(
                    final int index
                    )
    {
        return _elements.get( index );
    }



//    /**
//     * Returns an iterator over the result objects.
//     *
//     * @return
//     *  an iterator over the result elements.
//     */
//    public Iterator<T> elements()
//    {
//        return _elements.iterator();
//    }


    /**
     * Removes the projection list.
     */
    public void clear()
    {
        _elements.clear();
    }


    /**
     * Returns the number of result objects.
     *
     * @return
     *  the number of result objects.
     */
    public int size()
    {
        return _elements.size();
    }



    /**
     * Sets the timestamp of this result.
     *
     * @param   timestamp
     *  the timestamp.
     */
    public void setTimestamp(
                    final Date timestamp
                    )
    {
        _timestamp = timestamp;
    }


    /**
     * Returns the timestamp of this result.
     *
     * @return
     *  the timestamp.
     */
    public Date getTimestamp()
    {
        return _timestamp;
    }



    //**************************************************************
    //  Iterable
    //**************************************************************

    public Iterator<T> iterator()
    {
        return _elements.iterator();
    }



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    @Override
    public String toString()
    {
        return "SearchResult[#elements=" + size()
            + ", elements=" + getElements()
            + "]";
    }

}
// SearchResult
