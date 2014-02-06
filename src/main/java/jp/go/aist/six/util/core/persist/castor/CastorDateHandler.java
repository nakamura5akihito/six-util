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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.exolab.castor.mapping.ConfigurableFieldHandler;
import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.ValidityException;



/**
 * A Castor FieldHandler for the Date type.
 *
 * The subclasses may extend this class by defining a constructor
 * that passes the date/time format pattern description
 * to this class's constructor.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: CastorDateHandler.java 540 2013-03-08 08:09:29Z nakamura5akihito@gmail.com $
 */
public class CastorDateHandler
    extends GeneralizedFieldHandler
    implements ConfigurableFieldHandler
{

    /**
     * The date/time formatter and parser.
     */
    private SimpleDateFormat  _formatter;


    /**
     * The default date format pattern.
     */
    public static final String PATTERN = "yyyy-MM-dd";



    /**
     * Constructor.
     */
    public CastorDateHandler()
    {
        this( PATTERN );
    }



    /**
     * Constructor.
     *
     * @param   pattern
     *  the date/time format pattern description.
     */
    public CastorDateHandler(
                    final String pattern
                    )
    {

        _formatter = new SimpleDateFormat( pattern );
    }



    /**
     */
    public void setPattern(
                    final String pattern
                    )
    {
        _formatter = new SimpleDateFormat( pattern );
    }



    //**************************************************************
    //  ConfigurableFieldHandler
    //**************************************************************

    @Override
    public void setConfiguration(
                    final Properties config
                    )
    throws ValidityException
    {
        String  pattern = config.getProperty( "pattern" );
        if (pattern == null) {
            throw new ValidityException( "no 'pattern' parameter" );
        }

        try {
            _formatter = new SimpleDateFormat( pattern );
                         //throws IllegalArgumentException
        } catch (Exception ex) {
            throw new ValidityException( "invalid 'pattern' parameter: "
                            + ex.getMessage() );
        }
    }



    //**************************************************************
    //  FieldHandler
    //**************************************************************

    // from Date to String
    @Override
    public Object convertUponGet(
                    final Object value
                    )
    {
        if (value == null) {
            return null;
        }

        return _formatter.format( (Date)value );
    }



    // from String to Date
    @Override
    public Object convertUponSet(
                    final Object value
                    )
    {
        Date  date = null;
        if (value != null) {
            try {
                date = _formatter.parse( (String)value );
            } catch (ParseException p_ex) {
                throw new IllegalArgumentException( p_ex.getMessage() );
            }
        }

        return date;
    }



    @Override
    public Class<Date> getFieldType()
    {
        return Date.class;
    }



    @Override
    public Object newInstance(
                    final Object parent
                    )
        throws IllegalStateException
    {
        return null;
    }

}
//extends GeneralizedFieldHandler
//{
//
//    /**
//     * The date/time formatter and parser.
//     */
//    private SimpleDateFormat  _formatter;
//
//
//    /**
//     * The default date format pattern.
//     */
//    public static final String PATTERN = "yyyy-MM-dd";
//
//
//
//
//    /**
//     * Constructor.
//     */
//    public CastorDateHandler()
//    {
//        this( PATTERN );
//    }
//
//
//
//    /**
//     * Constructor.
//     *
//     * @param   pattern
//     *  the date/time format pattern description.
//     */
//    public CastorDateHandler(
//                    final String pattern
//                    )
//    {
//        _formatter = new SimpleDateFormat( pattern );
//    }
//
//
//
//    /**
//     */
//    public void setPattern(
//                    final String pattern
//                    )
//    {
//        _formatter = new SimpleDateFormat( pattern );
//    }
//
//
//
//    //**************************************************************
//    //  GeneralizedFieldHandler
//    //**************************************************************
//
//    // from Date to String
//    public Object convertUponGet(
//                    final Object value
//                    )
//    {
//        if (value == null) {
//            return null;
//        }
//
//        return _formatter.format( (Date)value );
//    }
//
//
//
//    // from String to Date
//    public Object convertUponSet(
//                    final Object value
//                    )
//    {
//        Date  date = null;
//        if (value != null) {
//            try {
//                date = _formatter.parse( (String)value );
//            } catch (ParseException p_ex) {
//                throw new IllegalArgumentException( p_ex.getMessage() );
//            }
//        }
//
//        return date;
//    }
//
//
//
//    public Class<Date> getFieldType()
//    {
//        return Date.class;
//    }
//
//
//
//    public Object newInstance(
//                    final Object parent
//                    )
//        throws IllegalStateException
//    {
//        return null;
//    }
//
//}
// CastorDateHandler
