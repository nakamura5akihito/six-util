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
package jp.go.aist.six.util.core.repository.morphia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import jp.go.aist.six.util.repository.CommonQueryParams;
import jp.go.aist.six.util.repository.QueryException;
import jp.go.aist.six.util.repository.QueryParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;




/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: MorphiaQueryBuilder.java 612 2013-06-28 08:26:58Z nakamura5akihito@gmail.com $
 */
public abstract class MorphiaQueryBuilder
implements QueryBuilder
{

    /**
     * Logger.
     */
    private static final Logger  _LOG_ = LoggerFactory.getLogger( MorphiaQueryBuilder.class );




    public static final String  LIST_DELIMITER = ",";
    public static final String  WILD_CARD = "*";
    private static final String  _INTERNAL_WILD_CARD_ = ".*";


    /**
     * Constructor.
     */
    protected MorphiaQueryBuilder()
    {
    }



    ///////////////////////////////////////////////////////////////////////
    //  template methods
    ///////////////////////////////////////////////////////////////////////

    /**
     * Subclasses may override this to define own key-handler mapping.
     */
    protected abstract Map<String, Handler> _handlerMapping();


    protected Handler _getHandler(
                    final String key
                    )
    {
        Handler  handler = _handlerMapping().get( key );
        return (handler == null ? FilterHandler.INSTANCE : handler);
    }



    /**
     * Subclasses may override this to define own key-field mapping.
     */
    protected abstract Map<String, String> _fieldMapping();


    protected String _getField(
                    final String key
                    )
    {
        String  field = _fieldMapping().get( key );
        _LOG_.debug( "field mapping: " + key + " --> " + field );

        return (field == null ? key : field);
    }



    protected String _convertOrderingFields(
                    final String ordering
                    )
    {
        StringBuilder  s = new StringBuilder();
        String[]  keys = _asList( ordering );
        for (String  key : keys) {
            if (s.length() > 0) {
                s.append( "," );
            }

            if (key.startsWith( "-" )) {
                key = key.substring( 1 );
                s.append( "-" );
            }
            String  field = _getField( key );
            s.append( field );
        }

        return s.toString();
    }



    ////////////////////////////////////////////////////////////////
    //  utility functions
    ////////////////////////////////////////////////////////////////

    /**
     * "aaa,b*,cc" --> new String[] { "aaa", "b*", "cc" }
     * null or ""  --> new String[0]
     */
    protected static String[] _asList(
                    final String value
                    )
    {
        if (value == null  ||  value.length() == 0) {
            return (new String[0]);
        }

        return (value.contains( LIST_DELIMITER ) ? value.split( LIST_DELIMITER ) : (new String[] { value }));
    }



    protected static boolean _isList(
                    final String value
                    )
    {
        if (value == null  ||  value.length() == 0) {
            return false;
        }

        return value.contains( LIST_DELIMITER );
    }



    protected static boolean _isEmpty(
                    final String s
                    )
    {
        return (s == null  ||  s.length() == 0);
    }



    protected static boolean _isPattern(
                    final String s
                    )
    {
        return (_isEmpty( s ) ? false : s.contains( WILD_CARD ));
    }


    protected static Object _asMatchingObject(
                    final String s
                    )
    {
        if (!_isPattern( s )) {
            return s;
        }

        String  regex = s.replace( ".", "\\." );
        regex = s.replace( WILD_CARD, _INTERNAL_WILD_CARD_ );
        return Pattern.compile( regex, Pattern.CASE_INSENSITIVE );
    }



    //**************************************************************
    //  QueryBuilder
    //**************************************************************

    public <T>
    Query<T> build(
                    final Query<T> query,
                    final QueryParams params
                    )
    {
//        query.disableValidation();

        for (String  key : params.keys()) {
            Handler  handler = _getHandler( key );
            String  field = _getField( key );
            String  value = params.get( key );

            if (CommonQueryParams.Key.ORDER.equalsIgnoreCase( key )) {
                value = _convertOrderingFields( value );
            }

            handler.build( query, field, value );
        }

        return query;
    }




    //**************************************************************
    //  Handler variations
    //**************************************************************

    /**
     * A query parameter handler.
     * It appends an expression that represents the given key-value pair.
     * e.g. given the URI http://.../xxx?key1=value1&key2=value2&...
     */
    protected static interface Handler
    {

        /**
         */
        public abstract void build( Query<?> query, String field, String value );

    }
    // Handler



    protected static class IgnoringHandler
    implements Handler
    {
        public static final IgnoringHandler  INSTANCE = new IgnoringHandler();

        public IgnoringHandler()
        {
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            //Ignore the field!
        }
    }
    //IgnoringHandler



    protected static class FilterHandler
    implements Handler
    {
        public static final FilterHandler  INSTANCE = new FilterHandler();

        public static final String  DEFAULT_OPERATOR = "=";

        private static final char[]       _QUERY_OPERATORS_ = new   char[] { '!',  '<', '>' };
        private static final String[]  _INTERNAL_OPERATORS_ = new String[] { "!=", "<", ">" };


        public FilterHandler()
        {
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            String  op = DEFAULT_OPERATOR;
            String  v = value;
            char  c = value.charAt( 0 );
            for (int  i = 0; i < _QUERY_OPERATORS_.length; i++) {
                if (_QUERY_OPERATORS_[i] == c) {
                    op = _INTERNAL_OPERATORS_[i];
                    v = value.substring( 1 );
                    break;
                }
            }

            query.filter( field + " " + op, v );
        }
    }
    // FilterHandler

    protected static class FilterHandler2
    implements Handler
    {
        public static final FilterHandler2  INSTANCE = new FilterHandler2();

        public static final String  DEFAULT_OPERATOR = "=";

        private String  _operator;


        public FilterHandler2()
        {
        }


        public FilterHandler2(
                        final String operator
                        )
        {
            _operator = operator;
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            if (_operator == null) {
                query.filter( field, value );
            } else {
                query.filter( field + " " + _operator, value );
            }
        }
    }
    // Filter2



    protected static class IntegerHandler
    implements Handler
    {
        public static final IntegerHandler  INSTANCE = new IntegerHandler();


        public IntegerHandler()
        {
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            char  operator = value.charAt( 0 );
            if (operator == '>'  ||  operator == '<') {
                Integer  int_value = Integer.valueOf( value.substring( 1 ) );
                query.filter( field + " " + operator, int_value );
            } else {
                Integer  int_value = Integer.valueOf( value );
                query.filter( field, int_value );
            }
        }
    }
    //Integer



    /**
     * key=10.0
     * key=7.0,
     * key=,7.0
     * key=7.0,
     * key=3.0,7.0
     */
    protected static class DoubleRangeHandler
    implements Handler
    {
        public static final DoubleRangeHandler  INSTANCE = new DoubleRangeHandler();


        protected DoubleRangeHandler()
        {
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            if (_isList( value )) {
                final int  index = value.indexOf( LIST_DELIMITER );
                final int  length = value.length();
                if (index == 0) {
                    if (length == 1) {
                        return;
                    }

                    // cvss=,7.0
                    String  d2 = value.substring( 1 );
                    query.filter( field + " <=" , Double.valueOf( d2 ) );
                } else if ((index + 1) == length) {
                    // cvss=7.0,
                    String  d1 = value.substring( 0, index );
                    query.filter( field + " >=" , Double.valueOf( d1 ) );
                } else {
                    // cvss=3.0,7.0
                    String[]  value_elem = _asList( value );
                    if (value_elem.length != 2) {
                        throw new QueryException( "invalid query param: DoubleRangeHandler - "
                                        + field + "="+ value );
                    }
                    query.filter( field + " >=" , Double.valueOf( value_elem[0] ) );
                    query.filter( field + " <=" , Double.valueOf( value_elem[1] ) );
                }

            } else { //NOT list, a simple value, e.g. cvss=10.0
                query.filter( field + " =" , Double.valueOf( value ) );
            }
        }
    }
    //DoubleRangeHandler





    /**
     * URI query param: ref_source=DEBIAN,REDHAT
     * MongoDB: { field: { $in: [<value1>, <value2>, ... <valueN> ] } }
     */
    protected static class HasAnyOfHandler
    implements Handler
    {
        public static final HasAnyOfHandler  INSTANCE = new HasAnyOfHandler();


        public HasAnyOfHandler()
        {
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            String[]  value_elements = _asList( value );
            if (value_elements.length > 1) {
                query.filter( field + " in", value_elements );
            } else {
                query.filter( field, value );
            }
        }
    }
    //HasAnyOfHandler



    /**
     * A query param handler for result ordering.
     */
    protected static class OrderHandler
    implements Handler
    {

        public static final OrderHandler  INSTANCE = new OrderHandler();


        public OrderHandler()
        {
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            query.order( value );
        }

    }
    // OrderHandler



    /**
     * A query param handler generating string pattern match filter in the query.
     * e.g. {name:/Joe/} in MongoDB, name like ''%Joe%' in SQL
     */
    protected static class PatternHandler
    implements Handler
    {
        public static final PatternHandler INSTANCE = new PatternHandler();


        public PatternHandler()
        {
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }


            Pattern  pattern = Pattern.compile( ".*" + value + ".*", Pattern.CASE_INSENSITIVE );
            if (_isList( field )) {
                // e.g. title,comment = ".*buffer overflow.*"
                String[]  field_elem = _asList( field );
                int  num_field_elem = field_elem.length;
                Criteria[]  criteria = new Criteria[num_field_elem];
                for (int  i = 0; i < num_field_elem; i++) {
                    criteria[i] = query.criteria( field_elem[i] ).equal( pattern );
                }
                query.or( criteria );
            } else {
                query.criteria( field ).equal( pattern );
            }
        }
    }
    // PatternHandler


    /**
     * A query param handler generating string pattern match filter in the query.
     * e.g. {name:/Joe/} in MongoDB, name like ''%Joe%' in SQL
     */
    protected static class PatternHandler2
    implements Handler
    {
        public static final PatternHandler2  INSTANCE = new PatternHandler2();


        public PatternHandler2()
        {
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            boolean  negate = (value.startsWith( "!" ) ? true : false);
            String  pure_value = (negate ? value.substring( 1 ) : value);
            _LOG_.debug( "pure value=" + pure_value + ", negate=" + negate );

            Pattern  pattern = Pattern.compile( ".*" + pure_value + ".*", Pattern.CASE_INSENSITIVE );
            if (_isList( field )) {
                // e.g. title,comment = ".*buffer overflow.*"
                String[]  field_elem = _asList( field );
                int  num_field_elem = field_elem.length;
                Criteria[]  criteria = new Criteria[num_field_elem];
                for (int  i = 0; i < num_field_elem; i++) {
                    if (negate) {
                        criteria[i] = query.criteria( field_elem[i] ).notEqual( pattern );
                    } else {
                        criteria[i] = query.criteria( field_elem[i] ).equal( pattern );
                    }
                }
                query.or( criteria );
            } else {
                if (negate) {
                    query.criteria( field ).notEqual( pattern );
                } else {
                    query.criteria( field ).equal( pattern );
                }
            }
        }
    }
    // PatternHandler2


    /**
     */
    protected static class ContainsHandler
    implements Handler
    {
        public static final ContainsHandler  INSTANCE = new ContainsHandler();


        public ContainsHandler()
        {
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            boolean  negate = (value.startsWith( "!" ) ? true : false);
            String  pure_value = (negate ? value.substring( 1 ) : value);
            _LOG_.debug( "pure value=" + pure_value + ", negate=" + negate );

            if (_isList( field )) {
                // e.g. title,comment = "buffer overflow"
                String[]  field_elem = _asList( field );
                int  num_field_elem = field_elem.length;
                Criteria[]  criteria = new Criteria[num_field_elem];
                for (int  i = 0; i < num_field_elem; i++) {
                    if (negate) {
                        criteria[i] = query.criteria( field_elem[i] ).not().containsIgnoreCase( pure_value );
                    } else {
                        criteria[i] = query.criteria( field_elem[i] ).containsIgnoreCase( pure_value );
                    }
                }
                query.or( criteria );
            } else {
                if (negate) {
                    query.criteria( field ).not().containsIgnoreCase( pure_value );
                } else {
                    query.criteria( field ).containsIgnoreCase( pure_value );
                }
            }
        }
    }
    // Contains


    //NOTE: According to some tests, this implementation is slower than the SearchTermsHandler2.
    // A value-list can be represented as a regex ".*X|Y|Z.*" .
    // f1=.*X|Y|Z.* OR
    // f2=.*X|Y|Z.* OR
    // ...
    protected static class SearchTermsHandler
    implements Handler
    {
        public static final SearchTermsHandler  INSTANCE = new SearchTermsHandler();


        public SearchTermsHandler()
        {
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            String[] field_elem = _asList( field );
            int  num_field_elem = field_elem.length;

            String[] value_elem = _asList( value );
            int  num_value_elem = value_elem.length;

            Pattern  pattern = null;
            if (num_value_elem > 1) {
                //...=v1,v2,...
                StringBuilder  s = new StringBuilder();
                for (int  j = 0; j < num_value_elem; j++) {
                    if (s.length() > 0) {
                        s.append( "|" );
                    }
                    s.append( value_elem[j] );
                }
                pattern = Pattern.compile( ".*" + s.toString() + ".*", Pattern.CASE_INSENSITIVE );
            } else {
                //...=v1
                pattern = Pattern.compile( ".*" + value + ".*", Pattern.CASE_INSENSITIVE );
            }


            if (num_field_elem > 1) {
                //f1,f2,...=...
                Criteria[]  criteria = new Criteria[num_field_elem];
                for (int  i = 0; i < num_field_elem; i++) {
                    criteria[i] = query.criteria( field_elem[i] ).equal( pattern );
                }
                query.or( criteria );

            } else {
                //f1=...
                query.criteria( field ).equal( pattern );
            }
        }
    }
    // SearchTerms

    // f1=.*X.* OR f1=.*Y.* OR f1=.*Z.*" OR
    // f2=.*X.* OR f2=.*Y.* OR f2=.*Z.*" OR
    // ...
    protected static class SearchTermsHandler2
    implements Handler
    {
        public static final SearchTermsHandler2  INSTANCE = new SearchTermsHandler2();


        public SearchTermsHandler2()
        {
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            String[] field_elem = _asList( field );
            int  num_field_elem = field_elem.length;

            String[] value_elem = _asList( value );
            int  num_value_elem = value_elem.length;
            Pattern[]  pattern = new Pattern[num_value_elem];
            for (int  j = 0; j < num_value_elem; j++) {
                pattern[j] = Pattern.compile( ".*" + value_elem[j] + ".*", Pattern.CASE_INSENSITIVE );
            }


            if (num_field_elem > 1) {
                if (num_value_elem > 1) {
                    //f1,f2,...=v1,v2,...
                    int  num_criteria = num_field_elem * num_value_elem;
                    Criteria[]  criteria = new Criteria[num_criteria];
                    for (int  i = 0; i < num_field_elem; i++) {
                        for (int  j = 0; j < num_value_elem; j++) {
                            int  index = i * num_value_elem + j;
                            criteria[index] = query.criteria( field_elem[i] ).equal( pattern[j] );
                        }
                    }
                    query.or( criteria );
                } else {
                    //f1,f2,...=v1
                    int  num_criteria = num_field_elem;
                    Criteria[]  criteria = new Criteria[num_criteria];
                    for (int  i = 0; i < num_field_elem; i++) {
                        criteria[i] = query.criteria( field_elem[i] ).equal( pattern[0] );
                    }
                    query.or( criteria );
                }

            } else {
                if (num_value_elem > 1) {
                    //f1=v1,v2,...
                    int  num_criteria = num_value_elem;
                    Criteria[]  criteria = new Criteria[num_criteria];
                    for (int  j = 0; j < num_value_elem; j++) {
                        criteria[j] = query.criteria( field ).equal( pattern[j] );
                    }
                    query.or( criteria );
                } else {
                    //f1=v1
                    query.criteria( field ).equal( pattern[0] );
                }
            }
        }
    }
    // SearchTerms2



    /**
     * f=a
     * f=a,b,c
     * f=x*
     * f=x*,y
     * f=x*,y,z*
     */
    protected static class PatternListHandler
    implements Handler
    {
        public static final PatternListHandler INSTANCE = new PatternListHandler();


        public PatternListHandler()
        {
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            String[]  value_elem = _asList( value );
            int  num_value_elem = value_elem.length;

            if (_isPattern( value )) {
                if (num_value_elem > 1) {
                    // f=x*,y
                    // f=x*,y,z*
                    Criteria[]  criteria = new Criteria[num_value_elem];
                    for (int  i = 0; i < num_value_elem; i++) {
                        criteria[i] = query.criteria( field ).equal( _asMatchingObject( value_elem[i] ) );
                    }
                    query.or( criteria );
                } else {
                    // f=x*
                    query.filter( field, _asMatchingObject( value ) );
                }
            } else {
                if (num_value_elem > 1) {
                    // f=a,b,c
                    query.filter( field + " in", value_elem );
                } else {
                    // f=a
                    query.filter( field, value );
                }
            }
        }
    }
    //PatternListHandler



    protected static class EnumListHandler<T extends Enum<T>>
    implements Handler
    {

        private final Class<T>  _type;
//        private final Class<? extends Enum<?>>  _type;


        public EnumListHandler(
                        final Class<T> type
//                                        final Class<? extends Enum<?>> type
                        )
        {
            _type = type;
        }


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            if (_isList( value )) {
                String[]  value_elem = _asList( value );
                List<Object>  list = new ArrayList<Object>();
                for (String  v : value_elem) {
                    list.add( Enum.valueOf( _type, v ) );
                }
                query.filter( field + " in", list );
            } else {
                query.filter( field, Enum.valueOf( _type, value ) );
            }
        }

    }
    //EnumListHandler



    protected static class DatetimeHandler
    implements Handler
    {

        public DatetimeHandler()
        {
        }


        private static SimpleDateFormat  _DATE_FORMATTER_ =
            new SimpleDateFormat( "yyyy-MM-dd" );


        public void build(
                        final Query<?> query,
                        final String field,
                        final String value
                        )
        {
            if (_isEmpty( value )) {
                return;
            }

            // validation
            try {
                _DATE_FORMATTER_.parse( value );
            } catch (ParseException ex) {
                throw new QueryException( ex );
            }

            query.filter( field, value );
        }
    }
    // DatetimeHandler



    ///////////////////////////////////////////////////////////////////////
    //  QueryBuilder variations
    ///////////////////////////////////////////////////////////////////////

    /**
     */
    public static class CommonBuilder
    extends MorphiaQueryBuilder
    {
        public static final CommonBuilder  INSTANCE = new CommonBuilder();


        // Query key --> database field
        private static final Map<String, String>  _FIELDS_ = Collections.emptyMap();
        // CommonQueryParams contains keys which are NOT mapped to the fields.


        protected static Map<String, Handler> _createHandlerMapping()
        {
            Handler  offset_handler = new Handler()
            {
                public void build(
                                final Query<?> query,
                                final String field,
                                final String value
                                )
                {
                    if (_isEmpty( value )) {
                        return;
                    }

                    int  offset = Integer.valueOf( value ).intValue();
                    query.offset( offset );
                }
            };


            Handler  limit_handler = new Handler()
            {
                public void build(
                                final Query<?> query,
                                final String field,
                                final String value
                                )
                {
                    if (_isEmpty( value )) {
                        return;
                    }

                    int  limit = Integer.valueOf( value ).intValue();
                    query.limit( limit );
                }
            };

            Map<String, Handler>  handler_mapping = new HashMap<String, Handler>();
            handler_mapping.put( CommonQueryParams.Key.START_INDEX, offset_handler );
            handler_mapping.put( CommonQueryParams.Key.COUNT,       limit_handler );
            handler_mapping.put( CommonQueryParams.Key.ORDER,       OrderHandler.INSTANCE );

            return handler_mapping;
        }


        // Query key --> Handler
        private static final Map<String, Handler>  _HANDLERS_ = _createHandlerMapping();


        public CommonBuilder()
        {
        }


        @Override
        protected Map<String, Handler> _handlerMapping()
        {
            return _HANDLERS_;
        }


        @Override
        protected Map<String, String> _fieldMapping()
        {
            return _FIELDS_;
        }

    }
    //CommonBuilder

}
//

