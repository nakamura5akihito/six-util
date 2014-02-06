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
import java.util.List;



/**
 * The SearchCriteria represents criteria of objects in search queries.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: SearchCriteria.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class SearchCriteria
    implements Serializable
{
    private static final long  serialVersionUID = -1038347150952773581L;




    /**
    private final Class  _targetType;
     */



    /**
     * The condition that the objects must satisfy to be selected.
     */
    private Binding  _binding;



//    /**
//     * The full-text search constraint.
//     */
//    private FulltextMatch  _fulltextMatch;



    /**
     * The names of properties by which the result objects are sorted.
     */
    private final List<Order>  _orders = new ArrayList<Order>();



    /**
     * The constraint on the number of objects returned by the query.
     */
    private Limit  _limit;



    /**
     * The properties that must be included in the search result.
     */
    private final List<Projection>  _projections = new ArrayList<Projection>();



    /**
     * A flag to indicate that duplicate objects in the result
     * should be removed.
     * The default value is TRUE.
     */
    private boolean  _distinct = true;




    /**
     * Default constructor.
     */
    public SearchCriteria()
    {
    }



    /**
     * Constructor.
     */
    public SearchCriteria(
                    final Binding binding
                    )
    {
        setBinding( binding );
    }




    /**
     * Sets the condition that the objects must satisfy to be selected.
     *
     * @param   binding
     *  the condition.
     */
    public SearchCriteria setBinding(
                    final Binding binding
                    )
    {
        _binding = binding;

        return this;
    }


    /**
     * Gets the condition that the objects must satisfy to be selected.
     *
     * @return
     *  the condition.
     */
    public Binding getBinding()
    {
        return _binding;
    }



//    /**
//     * Sets the constraint on the full-text search.
//     *
//     * @param   match
//     *  the constraint on the full-text search.
//     */
//    public SearchCriteria setFulltextMatch(
//                    final FulltextMatch match
//                    )
//    {
//        _fulltextMatch = match;
//
//        return this;
//    }
//
//
//    /**
//     * Gets the constraint on the full-text search.
//     *
//     * @return
//     *  the constraint on the full-text search.
//     */
//    public FulltextMatch getFulltextMatch()
//    {
//        return _fulltextMatch;
//    }



    /**
     * Sets the specified ordering criteria to sort the result objects.
     *
     * <p>If the specified list is null or empty,
     * it clears the previous list.
     * If one of the element in the list is null,
     * the previous list is not modified and an exception is thrown.
     * </p>
     *
     * @param   orders
     *  the ordering criteria.
     * @throws  IllegalArgumentException
     *  if an element in the specified list is null.
     */
    public SearchCriteria setOrders(
                    final List<? extends Order> orders
                    )
    {
        if (orders == getOrders()) {
            return this;
        }

        if (orders == null  ||  orders.size() == 0) {
            clearOrders();
            return this;
        }

        synchronized (orders) {
            for (Order  order : orders) {
                if (order == null) {
                    throw new IllegalArgumentException( "null element" );
                }
            }

            clearOrders();
            for (Order  order : orders) {
                addOrder( order );
            }
        }

        return this;
    }

//    public SearchCriteria setOrders(
//                    final Order[] orders
//                    )
//    {
//        if (orders == null  ||  orders.length == 0) {
//            clearOrders();
//            return this;
//        }
//
//        List<Order>  list = Arrays.asList( orders );
//        for (Order  order : list) {
//            if (order == null) {
//                throw new NullPointerException( "array contains null" );
//            }
//        }
//
//        clearOrders();
//        for (Order  order : list) {
//            addOrder( order );
//        }
//
//        return this;
//    }

    /**
     * Appends the specified ordering criterion.
     *
     * @param   order
     *  the ordering criterion.
     * @throws  NullPointerException
     *  if the argument is null.
     */
    public SearchCriteria addOrder(
                    final Order order
                    )
    {
        if (order == null) {
            throw new IllegalArgumentException( "no order specified" );
        }

        _orders.add( order );

        return this;
    }


    /**
     * Returns all the order list.
     *
     * @return
     *  the order list (non-null).
     */
    public List<Order> getOrders()
    {
        return _orders;
    }


//    /**
//     * Returns an iterator over the ordering criteria.
//     *
//     * @return
//     *  an iterator over the ordering criteria.
//     */
//    public Iterator<Order> orders()
//    {
//        return _orders.iterator();
//    }


    /**
     * Removes all the elements in the order list.
     */
    public void clearOrders()
    {
        _orders.clear();
    }



    /**
     * Sets the constraint on the number of objects returned by the query.
     * If the argument is null, it clears the constraint.
     *
     * @param   limit
     *  the constraint on the number of objects.
     */
    public SearchCriteria setLimit(
                    final Limit limit
                    )
    {
        _limit = limit;

        return this;
    }


    /**
     * Gets the constraint on the number of objects returned by the query.
     *
     * @return
     *  the constraint on the number of objects.
     */
    public Limit getLimit()
    {
        return _limit;
    }



    /**
     * Sets the specified projection to restrict the result values.
     *
     * <p>If the specified list is null or empty,
     * it clears the previous list.
     * If one of the element in the list is null,
     * the previous list is not modified and an exception is thrown.
     * </p>
     *
     * @param   projections
     *  the projection list.
     * @throws  IllegalArgumentException
     *  if an element in the specified list is null.
     */
    public SearchCriteria setProjections(
                    final List<? extends Projection> projections
                    )
    {
        if (projections == null  ||  projections.size() == 0) {
            clearProjections();
            return this;
        }

        synchronized (projections) {
            for (Projection  proj : projections) {
                if (proj == null) {
                    throw new IllegalArgumentException( "null element" );
                }
            }

            clearProjections();
            for (Projection  proj : projections) {
                addProjection( proj );
            }
        }

        return this;
    }
//    public SearchCriteria setProjections(
//                    final Projection[] projections
//                    )
//    {
//        if (projections == null  ||  projections.length == 0) {
//            clearProjections();
//            return this;
//        }
//
//        List<Projection>  list = Arrays.asList( projections );
//        for (Projection  proj : list) {
//            if (proj == null) {
//                throw new NullPointerException( "array contains null" );
//            }
//        }
//
//        clearProjections();
//        for (Projection  proj : list) {
//            addProjection( proj );
//        }
//
//        return this;
//    }


    /**
     * Appends the projection.
     *
     * @param   projection
     *  the projection.
     */
    public SearchCriteria addProjection(
                    final Projection projection
                    )
    {
        if (projection == null) {
            throw new IllegalArgumentException( "no projection specified" );
        }

        _projections.add( projection );

        return this;
    }


    /**
     * Returns the projection list.
     *
     * @return
     *  the projection list (not null).
     */
    public List<Projection> getProjections()
    {
        return _projections;
    }


//    /**
//     * Returns an iterator over the projection list.
//     *
//     * @return
//     *  an iterator over the projection list.
//     */
//    public Iterator<Projection> projections()
//    {
//        return _projections.iterator();
//    }


    /**
     * Removes all the elements in the projection list.
     */
    public void clearProjections()
    {
        _projections.clear();
    }



    /**
     * Specifies whether duplicate objects should be returned.
     * The boolean value TRUE indicates duplicate objects
     * in the result should be removed.
     *
     * @param   distinct
     *  true if duplicate objects in the result should be removed;
     *  false otherwise.
     */
    public SearchCriteria setDistinct(
                    final boolean distinct
                    )
    {
        _distinct = distinct;

        return this;
    }


    /**
     * Determines whether duplicate objects should be returned.
     * The boolean value TRUE indicates duplicate objects
     * in the result should be removed.
     * The initial value is TRUE.
     *
     * @return
     *  true if duplicate objects in the result should be removed;
     *  false otherwise.
     */
    public boolean isDistinct()
    {
        return _distinct;
    }



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    /**
     * Determines whether another object is equal to this SearchCriteria.
     * The result is true if and only if the argument is not null
     * and is a SearchCriteria object that has the same property
     * and value list.
     *
     * @param   obj
     *  the object to test for equality with this SearchCriteria.
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

        if (!(obj instanceof SearchCriteria)) {
            return false;
        }

        final SearchCriteria  other = (SearchCriteria)obj;

        final Limit  other_limit = other.getLimit();
        final Limit   this_limit =  this.getLimit();
        if (this_limit == other_limit
                ||  (this_limit != null  &&  this_limit.equals( other_limit ))) {
            final List<Order>  other_orders = other.getOrders();
            final List<Order>   this_orders =  this.getOrders();
            if (this_orders == other_orders
                        ||  (this_orders != null  &&  this_orders.equals( other_orders ))) {
                final Binding  other_binding = other.getBinding();
                final Binding   this_binding =  this.getBinding();
                if (this_binding == other_binding
                                ||  (this_binding != null  &&  this_binding.equals( other_binding ))) {
                    final List<Projection>  other_projs = other.getProjections();
                    final List<Projection>   this_projs =  this.getProjections();
                    if (this_projs == other_projs
                                    ||  (this_projs != null  &&  this_projs.equals( other_projs ))) {
//                        final FulltextMatch  other_match = other.getFulltextMatch();
//                        final FulltextMatch   this_match =  this.getFulltextMatch();
//                        if (this_match == other_match
//                                        ||  (this_match != null  &&  this_match.equals( other_match ))) {
                            if (isDistinct() == other.isDistinct()) {
                                return true;
                            }
//                        }
                    }
                }
            }
        }

        return false;
    }



    /**
     * Computes the hash code for this SearchCriteria.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        final int  prime = 37;
        int  result = 17;

        final Binding  binding = getBinding();
        result = prime * result + (binding == null ? 0 : binding.hashCode());

//        final FulltextMatch  match = getFulltextMatch();
//        result = prime * result + (match == null ? 0 : match.hashCode());

        List<Order>  orders = getOrders();
        result = prime * result + (orders == null ? 0 : orders.hashCode());

        List<Projection>  projs = getProjections();
        result = prime * result + (projs == null ? 0 : projs.hashCode());

        final Limit  limit = getLimit();
        result = prime * result + (limit == null ? 0 : limit.hashCode());

        result = prime * result + (isDistinct() ? 0 : 1);

        return result;
    }



    /**
     * Returns a string representation of this SearchCriteria.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this SearchCriteria.
     */
    @Override
    public String toString()
    {
        StringBuffer  s = new StringBuffer( "SearchCriteria[binding=");
        s.append( getBinding() );
//        s.append( ", fulltextMatch=" ).append( getFulltextMatch() );
        s.append( ", orders=" ).append( getOrders() );
        s.append( ", distinct=" ).append( isDistinct() );
        s.append( ", projections=" ).append( getProjections() );
        s.append( ", limit=" ).append( getLimit() );
        s.append( "]" );

        return s.toString();
    }

}
// SearchCriteria
