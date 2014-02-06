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
package jp.go.aist.six.util.core.persist.castor;

import java.util.ArrayList;
import java.util.List;
import jp.go.aist.six.util.search.Aggregation;
import jp.go.aist.six.util.search.AndBinding;
import jp.go.aist.six.util.search.Binding;
import jp.go.aist.six.util.search.InBinding;
import jp.go.aist.six.util.search.LikeBinding;
import jp.go.aist.six.util.search.Limit;
import jp.go.aist.six.util.search.LogicalBinding;
import jp.go.aist.six.util.search.NotBinding;
import jp.go.aist.six.util.search.NullBinding;
import jp.go.aist.six.util.search.OrBinding;
import jp.go.aist.six.util.search.Order;
import jp.go.aist.six.util.search.Projection;
import jp.go.aist.six.util.search.PropertyBinding;
import jp.go.aist.six.util.search.PropertyProjection;
import jp.go.aist.six.util.search.Relation;
import jp.go.aist.six.util.search.RelationalBinding;
import jp.go.aist.six.util.search.SearchCriteria;
import jp.go.aist.six.util.search.TextMatchBinding;



/**
 * A Castor OQL utility.
 * This class is used to compile the SearchCriteria to Castor OQL statement.
 *
 * <p>In the result statement,
 * the property values in the criteria are converted to place holders in the statement:
 * $1, $2, ...
 * The parameter value list, in the order of the place holders, can be obtained separately.
 * </p>
 *
 * <p>By default, the result statements and parameter value list do not contain
 * the LIMIT constraint.
 * A method {@link #setLimitEnabled(boolean)} changes the effectiveness
 * of the LIMIT in the result, and {@link #isLimitEnabled()} tests
 * the current state.
 * </p>
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: OQL.java 540 2013-03-08 08:09:29Z nakamura5akihito@gmail.com $
 */
public class OQL
{

    private static final String  _SELECT_        = "SELECT ";
    private static final String  _SELECT_COUNT_  = "SELECT count(*) ";
    private static final String  _DISTINCT_      = " DISTINCT ";
    private static final String  _FROM_          = " FROM ";
    private static final String  _WHERE_         = " WHERE ";
    private static final String  _AND_           = " AND ";
    private static final String  _OR_            = " OR ";
    private static final String  _LIKE_          = " LIKE ";
    private static final String  _IN_LIST_       = " IN LIST ";
    private static final String  _ORDER_BY_      = " ORDER BY ";
    private static final String  _DESC_          = " DESC";
    private static final String  _LIMIT_         = " LIMIT ";
    private static final String  _OFFSET_        = " OFFSET ";
    private static final String  _NIL_           = "nil";
    private static final String  _WILDCARD_      = "%";
    private static final char    _SPACE_         = ' ';
    private static final char    _PARAM_SIGN_    = '$';
    //private static final char  _PROP_CONNECTOR_  = '.';




    /**
     * The type of objects.
     */
    private Class<?>  _type;


    public static final String  DEFAULT_ALIAS = "o";
    private String  _alias;



    /**
     * The OQL SELECT clause.
     */
    private String  _selectClause;


    /**
     * The OQL FROM clause.
     */
    private String  _fromClause;


    /**
     * The OQL WHERE clause, including ORDER BY clause.
     */
    private String  _whereClause = "";


    /**
     * The OQL parameter values for WHERE clause.
     */
    private final List<Object>  _paramVlaues = new ArrayList<Object>();



    /**
     *
     */
    private List<Order>  _orders = null;


    /**
     * The OQL ORDER clause.
     */
    private String  _orderClause = "";



    /**
     * The OQL LIMIT.
     */
    private Limit  _limit = null;


    private String  _limitClause = "";


    /**
     * A flag to indicate that the results should contain the LIMIT constraint
     * in the statement and parameter list.
     */
    private boolean  _limitEnabled = false;




    /**
     * Constructor.
     */
    protected OQL()
    {
    }


    /**
     * Constructor.
     */
    public OQL(
                    final Class<?> type,
                    final SearchCriteria criteria
                    )
    {
        this( type, DEFAULT_ALIAS, criteria );
//        this( type, aliasFor( type ), criteria );
    }



    /**
     * Constructor.
     */
    public OQL(
                    final Class<?> type,
                    final String alias,
                    final SearchCriteria criteria
                    )
    {
        if (type == null) {
            throw new IllegalStateException( "no object type specified" );
        }

        _type = type;
        _alias = alias;
        _compile( criteria );
    }



    /**
     * Returns the OQL statement.
     *
     * @return
     *  an OQL statement.
     */
    private void _buildStatement(
                    final List<Order> orders,
                    final Limit limit,
                    final StringBuilder stmt
                    )
    {
        stmt.append( _selectClause ).append( _fromClause ).append( _whereClause );
        if (orders == null) {
            stmt.append( _orderClause );
        } else {
            _buildOrderClause( orders, stmt );
        }

        if (isLimitEnabled()) {
            if (limit == null) {
                stmt.append( _limitClause );
            } else {
                _buildLimit( limit, stmt );
            }
        }
    }


    /**
     * Returns the OQL statement.
     *
     * @return
     *  an OQL statement.
     */
    public String getStatement()
    {
        StringBuilder  stmt = new StringBuilder();
        _buildStatement( null, null, stmt );

        return stmt.toString();
    }


    /**
     */
    public String getStatement(
                    final List<Order> orders
                    )
    {
        StringBuilder  stmt = new StringBuilder();
        _buildStatement( orders, null, stmt );

        return stmt.toString();
    }


    /**
     */
    public String getStatement(
                    final Limit limit
                    )
    {
        StringBuilder  stmt = new StringBuilder();
        _buildStatement( null, limit, stmt );

        return stmt.toString();
    }


    /**
     */
    public String getStatement(
                    final List<Order> orders,
                    final Limit limit
                    )
    {
        StringBuilder  stmt = new StringBuilder();
        _buildStatement( orders, limit, stmt );

        return stmt.toString();
    }



    /**
     * Returns the OQL statement to count the objects.
     *
     * @return
     *  an OQL statement.
     */
    public String getCountStatement()
    {
        StringBuilder  stmt = new StringBuilder();
        stmt.append( _SELECT_COUNT_ ).append( _fromClause ).append( _whereClause );

        return stmt.toString();
    }



    /**
     */
    public Class<?> getType()
    {
        if (_type == null) {
            throw new IllegalStateException( "no object type specified" );
        }

        return _type;
    }



    /**
     * Returns the 'select' clause ("select" keyword included).
     */
    public String getSelectClause()
    {
        return _selectClause;
    }



    /**
     * Returns the 'where' clause ("where" keyword included).
     */
    public String getWhereClause()
    {
        return _whereClause;
    }



    /**
     */
    public String getOrdering()
    {
        return getOrdering( _orders );
    }



    /**
     */
    public String getOrdering(
                    final List<Order> orders
                    )
    {
        int  n_orders = (orders == null ? 0 : orders.size());
        if (n_orders == 0) {
            return null;
        }

        final StringBuilder  s = new StringBuilder();
        for (int  i = 0; i < n_orders; i++) {
            if (i > 0) {
                s.append( "," );
            }

            final Order  order = orders.get( i );
            _buildProperty( order.getProperty(), s );

            if (order.isDescending()) {
                s.append( _DESC_ );
            }
        }

        return s.toString();
    }



    /**
     * Returns the LIMIT object.
     *
     * @return
     *  the LIMIT object.
     */
    public Limit getLimit()
    {
        return _limit;
    }


    /**
     * Specifies whether LIMIT clauses should be created in the result statements.
     *
     * @param   enabled
     *  if true, LIMIT clauses should be created in the result statements.
     */
    public void setLimitEnabled(
                    final boolean enabled
                    )
    {
        _limitEnabled = enabled;
    }


    /**
     * Tests if LIMIT clauses should be created in the result statements.
     *
     * @return
     *  true if LIMIT clauses should be created in the result statements.
     */
    public boolean isLimitEnabled()
    {
        return _limitEnabled;
    }



    /**
     * Returns the parameter values to be bound to the OQL statement.
     *
     * @return
     *  the parameter values.
     */
    public Object[] getParameterValues()
    {
        Object[]  empty = new Object[0];
        if (_paramVlaues == null  ||  _paramVlaues.size() == 0) {
            return empty;
        }

        return _paramVlaues.toArray( empty );
    }



    /**
     */
    private void _compile(
                    final SearchCriteria criteria
                    )
    {
        if (criteria != null) {
            StringBuilder  whereClause = new StringBuilder();
            _buildWhere( criteria, whereClause, _paramVlaues );
            _whereClause = whereClause.toString();

            _orders = criteria.getOrders();
            StringBuilder  orderClause = new StringBuilder();
            _buildOrderClause( _orders, orderClause );
            _orderClause = orderClause.toString();

            Limit  limit = criteria.getLimit();
            if (limit != null) {
                _limit = new Limit( limit.getCount(), limit.getOffset() );
                StringBuilder  limitClause = new StringBuilder();
                _buildLimit( _limit, limitClause );
                _limitClause = limitClause.toString();
            }
        }

        StringBuilder  selectClause = new StringBuilder();
        _buildSelect( criteria, selectClause );
        _selectClause = selectClause.toString();

        StringBuilder  fromClause = new StringBuilder();
        _buildFrom( _alias, fromClause );
        _fromClause = fromClause.toString();
    }



//    /**
//     * Creates a statement part for the specified LIMIT constraint.
//     *
//     * @param   limit
//     *  the LIMIT constraint.
//     * @return
//     *  a statement part.
//     */
//    private String _createStatement(
//                    final Limit limit
//                    )
//    {
//        if (limit == null) {
//            throw new NullPointerException();
//        }
//
//        StringBuilder  stmt = new StringBuilder();
//        List<Object>  params = new ArrayList<Object>(2);
//        _buildLimit( limit, stmt, params );
//
//        return stmt.toString();
//    }



    /**
     * Limit
     *      LIMIT c OFFSET 0
     *      LIMIT c OFFSET o
     */
    private void _buildLimit(
                    final Limit limit,
                    final StringBuilder stmt
                    )
    {
        if (limit == null) {
            return;
        }

        stmt.append( _LIMIT_ );
        stmt.append( limit.getCount() );
        stmt.append( _OFFSET_ );
        stmt.append( limit.getOffset() );
    }



    /**
     * Order
     *      ORDER BY T.a
     *      ORDER BY T.a DESC
     *      ORDER BY T.a DESC, T.b
     */
    private void _buildOrderClause(
                    final List<Order> orders,
                    final StringBuilder stmt
                    )
    {
        int  n_orders = (orders == null ? 0 : orders.size());
        if (n_orders == 0) {
            return;
        }

        stmt.append( _ORDER_BY_ );
        for (int  i = 0; i < n_orders; i++) {
            if (i > 0) {
                stmt.append( "," );
            }

            final Order  order = orders.get( i );
            _buildProperty( order.getProperty(), stmt );
            //throws SearchException

            if (order.isDescending()) {
                stmt.append( _DESC_ );
            }
        }
    }



    //==============================================================
    //  WHERE clause
    //==============================================================

    /**
     * WHERE
     */
    private void _buildWhere(
                    final SearchCriteria criteria,
                    final StringBuilder stmt,
                    final List<Object> params
                    )
    {
        Binding  binding = (criteria == null ? null : criteria.getBinding());
//        FulltextMatch  match = (criteria == null ? null : criteria.getFulltextMatch());
//        Binding  matchBinding = null;

//        if (match != null) {
//            matchBinding = _toFulltextBinding( match );
//        }

//        if (binding == null) {
//            binding = matchBinding;
//        } else {
//            if (matchBinding != null) {
//                binding = new AndBinding( binding, matchBinding );
//            }
//        }

        if (binding != null ){
            stmt.append( _WHERE_ );
            _buildBinding( binding, stmt, params );
            //throws SearchException
        }
    }



    /**
     */
    private void _buildBinding(
                    final Binding binding,
                    final StringBuilder stmt,
                    final List<Object> params
                    )
    {
        if (binding instanceof PropertyBinding) {
            _buildPropertyBinding( (PropertyBinding)binding, stmt, params );
            //throws SearchException

        } else if (binding instanceof LogicalBinding) {
            _buildLogicalBinding( (LogicalBinding)binding, stmt, params );
            //throws SearchException

        } else if (binding instanceof NotBinding) {
            _buildNotBinding( (NotBinding)binding, stmt, params );
            //throws SearchException

        } else {
            throw new IllegalArgumentException( "unsupported Binding: "
                            + String.valueOf( binding ) );
        }
    }



    /**
     * LogicalBinding
     *      (b_1 AND b_2 AND ... AND b_n)
     *      (b_1 OR  b_2 OR  ... OR  b_n)
     */
    private void _buildLogicalBinding(
                    final LogicalBinding binding,
                    final StringBuilder stmt,
                    final List<Object> params
                    )
    {
        final int  size = binding.size();

        if (size < 2) {
            throw new IllegalArgumentException(
                            "LogicalBinding with less than two element bindings" );
        }

        final String  logic = (binding instanceof AndBinding) ? _AND_ : _OR_;
        stmt.append( " (" );
        for (int  i = 0; i < size; i++) {
            if (i > 0) {
                stmt.append( logic );
            }
            _buildBinding( binding.getElementAt( i ), stmt, params );
        }
        stmt.append( ")" );
    }



    /**
     * NotBinding
     *      NOT (...)
     */
    private void _buildNotBinding(
                    final NotBinding binding,
                    final StringBuilder stmt,
                    final List<Object> params
                    )
    {
        stmt.append( " NOT (" );
        _buildBinding( binding, stmt, params );
        stmt.append( ")" );
    }



    /**
     * PropertyBinding
     */
    private void _buildPropertyBinding(
                    final PropertyBinding binding,
                    final StringBuilder stmt,
                    final List<Object> params
                    )
    {
        if (binding instanceof RelationalBinding) {
            _buildRelationalBinding( (RelationalBinding)binding, stmt, params );
            //throws SearchException

        } else if (binding instanceof InBinding) {
            _buildInBinding( (InBinding)binding, stmt, params );
            //throws SearchException

        } else if (binding instanceof LikeBinding) {
            _buildLikeBinding( (LikeBinding)binding, stmt, params );
            //throws SearchException

        } else if (binding instanceof TextMatchBinding) {
            _buildTextMatchBinding( (TextMatchBinding)binding, stmt, params );
            //throws SearchException

        } else if (binding instanceof NullBinding) {
            _buildNullBinding( (NullBinding)binding, stmt );
            //throws SearchException

        } else {
            throw new IllegalArgumentException( "unsupported PropertyBinding: "
                            + String.valueOf( binding ) );
        }
    }



    /**
     * NullBinding
     *      is_undefiend(T.p)
     *      is_defnied(T.p)
     */
    private void _buildNullBinding(
                    final NullBinding binding,
                    final StringBuilder stmt
                    )
    {
        String  property = binding.getProperty();

        if (binding.isNotNull()) {
            stmt.append( " is_defined(" );
        } else {
            stmt.append( " is_undefined(" );
        }

        _buildProperty( property, stmt );
        //throws SearchException

        stmt.append( ")" );
    }



    /**
     * RelationalBinding
     *      p EQ 2001
     *      p GE 2001
     */
    private void _buildRelationalBinding(
                    final RelationalBinding binding,
                    final StringBuilder stmt,
                    final List<Object> params
                    )
    {
        String  property = binding.getProperty();
        Object  value = binding.getValue();
        Relation  rel = binding.getRelation();

        if (value == null) {
            NullBinding  nullBinding = new NullBinding();
            nullBinding.setProperty( property );
            if (Relation.EQUAL == rel) {
                nullBinding.setNotNull( false );
            } else if (Relation.NOT_EQUAL == rel) {
                nullBinding.setNotNull( true );
            } else {
                throw new IllegalArgumentException(
                                "invalid null-valued RelationalBinding: "
                                + String.valueOf( binding ) );
            }
            _buildNullBinding( nullBinding, stmt );
            //throws SearchException

        } else {
            _buildProperty( property, stmt );
            //throws SearchException
            stmt.append( _SPACE_ );
            stmt.append( rel.operator() );
            stmt.append( _SPACE_ );
            _buildParameter( value, stmt, params );
        }
    }



    /**
     * LikeBinding
     *      T.p LIKE 'foo%'
     */
    private void _buildLikeBinding(
                    final LikeBinding binding,
                    final StringBuilder stmt,
                    final List<Object> params
                    )
    {
        _buildProperty( binding.getProperty(), stmt );
        //throws SearchException
        stmt.append( _LIKE_ );
        _buildParameter( binding.getPattern(), stmt, params );
    }



    /**
     * InBinding
     *      T.p IN LIST(v_1, v_2, ..., v_n)
     *      v_i = "nil", if v_i == null.
     */
    private void _buildInBinding(
                    final InBinding binding,
                    final StringBuilder stmt,
                    final List<Object> params
                    )
    {
        // It's OK if null is contained.
//        if (n_values == 0) {
//            throw new SearchException( "invalid InBinding: empty value list" );
//        }


        // T.p
        _buildProperty( binding.getProperty(), stmt );
        //throws SearchException

        // T.p IN LIST(
        stmt.append( _IN_LIST_ ).append( "(" );

        // T.p IN LIST(v_1, v_2, ..., v_n
        boolean  containsNull = false;
        int  n_valuesAdded = 0;

        for (Object  value : binding.getValues()) {
            if (value == null) {
                containsNull = true;
            } else {
                if (n_valuesAdded > 0) {
                    stmt.append( "," );
                }
                _buildParameter( value, stmt, params );
                n_valuesAdded++;
            }
        }

        if (binding.isNullContained()  ||  containsNull) {
            if (n_valuesAdded > 0) {
                stmt.append( "," );
            }
            // T.p IN LIST(v_1, v_2, ..., v_n, nil
            stmt.append( _NIL_ );
        }

        // T.p IN LIST(v_1, v_2, ..., v_n)
        stmt.append( ")" );
    }



    /**
     * Appends a query parameter to the statement for the specified value
     * and appends the value to the parameter list.
     * The parameter is the form of "$i", where i is the position in the list.
     */
    private void _buildParameter(
                    final Object value,
                    final StringBuilder stmt,
                    final List<Object> params
                    )
    {
        params.add( value );
        stmt.append( _PARAM_SIGN_ ).append( params.size() );
    }



    //==============================================================
    //  FROM clause
    //==============================================================

    /**
     * FROM
     *      FROM foo.bar.Type Type
     */
    private void _buildFrom(
                    final String alias,
                    final StringBuilder stmt
                    )
    {
        stmt.append( _FROM_ ).append( getType().getName() );
        stmt.append( _SPACE_ ).append( alias );
    }



    //==============================================================
    //  SELECT clause
    //==============================================================

    /**
     * SELECT
     *      SELECT {DISTINCT} T
     *      SELECT {DISTINCT} T.a, T.b, T.c
     */
    private void _buildSelect(
                    final SearchCriteria criteria,
                    final StringBuilder stmt
                    )
    {
        final List<Projection>  projections =
            (criteria == null ? null : criteria.getProjections());
        final int  n_projections = (projections == null ? 0 : projections.size());
        final boolean  distinct =
            (criteria == null ? true : criteria.isDistinct());

        // SELECT
        stmt.append( _SELECT_ );
        if (distinct) {
            // SELECT DISTINCT
            stmt.append( _DISTINCT_ );
        }

        if (n_projections == 0) {
            //SELECT {DISTINCT} T
            stmt.append( _alias );
        } else {
            //SELECT {DISTINCT} T.a,T.b,T.c
            for (int  i = 0; i < n_projections; i++) {
                if (i > 0) {
                    stmt.append( "," );
                }
                _buildProjection( projections.get( i ), stmt );
                //throws SearchException
            }
        }
    }



    /**
     * Projection
     *      COUNT(*)
     *      MAX(T.a)
     *      T.a
     */
    private void _buildProjection(
                    final Projection p,
                    final StringBuilder stmt
                    )
    {
        if (p instanceof Aggregation) {
            Aggregation  aggr = (Aggregation)p;
            stmt.append( aggr.getFunction().name() );
            stmt.append( "(" );
            String  expr = aggr.getExpression();
            if (Aggregation.WHOLE_OBJECT_EXPRESSION.equals( expr )) {
                stmt.append( expr );
            } else {
                _buildProperty( expr, stmt );
                //throws SearchException
            }
            stmt.append( ")" );
        } else if (p instanceof PropertyProjection) {
            PropertyProjection  pp = (PropertyProjection)p;
            _buildProperty( pp.getProperty(), stmt );
            //throws SearchException
        } else {
            throw new IllegalArgumentException( "unsupported projection: " + p );
        }
    }



    /**
     * T.p
     */
    private void _buildProperty(
                    final String property,
                    final StringBuilder stmt
                    )
    {
        if (property == null  ||  property.length() == 0) {
            throw new IllegalArgumentException( "no property specified" );
        }

        stmt.append( _alias ).append( "." ).append( property );
    }



    /**
     * Returns an alias name of the specified Java type.
     * The alias name, say "alias" of class foo.bar.Baz, is used in an OQL like:
     * "SELECT alias FROM foo.bar.Baz alias WHERE alias.p = ...".
     *
     * <p>In this implementation, the alias name is equal to
     * the simple name of the class.
     * For example, for the above class foo.bar.Baz,
     * the alias name is "Baz" and is used like:
     * "SELECT Baz FROM foo.bar.Baz Baz WHERE Baz.year = 2001".
     * </p>
     *
     * @param   type
     *  the type.
     * @return
     *  the alias name.
     */
    public static String aliasFor(
                    final Class<?> type
                    )
    {
        if (type.isArray()) {
            throw new IllegalArgumentException( "array type" );
        }

        return type.getSimpleName();
    }





    ////////////////////////////////////////////////////////////////
    //  text match
    ////////////////////////////////////////////////////////////////

    /**
     * matchAll == true:
     *      (p LIKE w_1  AND  p LIKE w_2  AND ... AND  p LIKE w_m)
     *
     * matchAll == false:
     *      (p LIKE w_1  OR   p LIKE w_2  OR  ... OR   p LIKE w_m)
     */
    private void _buildTextMatchBinding(
                    final TextMatchBinding binding,
                    final StringBuilder stmt,
                    final List<Object> params
                    )
    {
        int  size = binding.getText().size();

        if (size == 1) {
            String  pattern = _WILDCARD_ + binding.getText().iterator().next() + _WILDCARD_;
            LikeBinding  like = new LikeBinding( binding.getProperty(), pattern );
            _buildLikeBinding( like, stmt, params );
        } else if (size > 1) {
            LogicalBinding  logic = null;
            if (binding.isMatchAll()) {
                logic = new AndBinding();
            } else {
                logic = new OrBinding();
            }
            String  prop = binding.getProperty();
            for (String  w : binding.getText()) {
                String  pattern = _WILDCARD_ + w + _WILDCARD_;
                LikeBinding  like = new LikeBinding( prop, pattern );
                logic.addElement( like );
            }
            _buildLogicalBinding( logic, stmt, params );
        } else {
            // size < 1
            throw new IllegalArgumentException( "no text in TextMatchBinding" );
        }
    }



//    /**
//     * matchAll == true:
//     *      (p_1 LIKE w_1  OR  p_2 LIKE w_1  OR ... OR  p_n LIKE w_1)
//     *  AND
//     *      (p_1 LIKE w_2  OR  p_2 LIKE w_2  OR ... OR  p_n LIKE w_2)
//     *      .....
//     *  AND
//     *      (p_1 LIKE w_m  OR  p_2 LIKE w_m  OR ... OR  p_n LIKE w_m)
//     *
//     * matchAll == false:
//     *      (p_1 LIKE w_1  OR  p_2 LIKE w_1  OR ... OR  p_n LIKE w_1)
//     *  OR
//     *      (p_1 LIKE w_2  OR  p_2 LIKE w_2  OR ... OR  p_n LIKE w_2)
//     *      .....
//     *  OR
//     *      (p_1 LIKE w_m  OR  p_2 LIKE w_m  OR ... OR  p_n LIKE w_m)
//     */
//    private Binding _toFulltextBinding(
//                    final FulltextMatch match
//                    )
//    {
//        if (match == null) {
//            return null;
//        }
//
//        Collection<String>  properties = match.getProperties();
//        Collection<String>  patterns = match.getPatterns();
//        int  n_patterns = patterns.size();
//        if (n_patterns == 0  ||  properties.size() == 0) {
//            return null;
//        }
//
//        if (n_patterns == 1) {
//            return _createFulltextBinding( properties, patterns.iterator().next() );
//        }
//
//        LogicalBinding  logicalBinding = null;
//        if (match.isMatchAll()) {
//            logicalBinding = new AndBinding();
//        } else {
//            logicalBinding = new OrBinding();
//        }
//
//        for (String  pattern : patterns) {
//            Binding  binding = _createFulltextBinding( properties, pattern );
//            logicalBinding.addElement( binding );
//        }
//
//        return logicalBinding;
//    }



//    /**
//     * Returns a Binding for the specified search word.
//     *      p_1 LIKE w
//     *      p_1 LIKE w  OR  p_2 LIKE w  OR ... OR  p_n LIKE w
//     *
//     * @param   pattern
//     *  the full-text search word.
//     * @return
//     *  the result binding
//     *  or null if the word is empty.
//     */
//    private Binding _createFulltextBinding(
//                    final Collection<String> properties,
//                    final String pattern
//                    )
//    {
//        if (properties == null  ||  properties.size() == 0) {
//            return null;
//        }
//
//        final int  n_properties = properties.size();
//        if (n_properties == 1) {
//            return _createFulltextBinding( properties.iterator().next(), pattern );
//        }
//
//        OrBinding  or_binding = new OrBinding();
//        for (String  property : properties) {
//            LikeBinding  binding = _createFulltextBinding( property, pattern );
//            or_binding.addElement( binding );
//        }
//
//        return or_binding;
//    }


//    /**
//     * Creates a LIKE binding for the specified property
//     * and full-text search word.
//     *
//     * @param   property
//     *  the name of the property.
//     * @param   pattern
//     *  the full-text search word.
//     * @return
//     *  the LIKE binding
//     *  or null if the word is empty.
//     */
//    private LikeBinding _createFulltextBinding(
//                    final String property,
//                    final String pattern
//                    )
//    {
//        LikeBinding  binding = new LikeBinding();
//        binding.setProperty( property );
//        binding.setPattern( _WILDCARD_ + pattern + _WILDCARD_ );
//
//        return binding;
//    }

}
// OQL
