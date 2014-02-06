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
package jp.go.aist.six.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;




/**
 * An utility class for ISO date/time format.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: IsoDate.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class IsoDate
{

    /**
     * The date format.
     */
    private static SimpleDateFormat  _DATE_FORMATTER_ =
        new SimpleDateFormat( "yyyy-MM-dd" );



    /**
     * The date/time format.
     */
    private static SimpleDateFormat  _DATETIME_FORMATTER_ =
        new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );


    /**
     * The date/time format, including milliseconds.
     */
    private static SimpleDateFormat  _DATETIME_MILLISEC_FORMATTER_ =
        new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS" );



    /**
     * The date/time format, including milliseconds and time zone.
     */
    private static SimpleDateFormat  _DATETIME_MILLISEC_TZ_FORMATTER_ =
        new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ" );




    /**
     */
    public static String format(
                    final Date date
                    )
    {
        if (date == null) {
            return null;
        }

        return _DATETIME_FORMATTER_.format( date );
    }



    /**
     */
    public static String formatDate(
                    final Date date
                    )
    {
        if (date == null) {
            return null;
        }

        return _DATE_FORMATTER_.format( date );
    }



    /**
     */
    public static Date valueOf(
                    final String value
                    )
    {
        Date  date = null;
        if (value != null) {
            SimpleDateFormat  format = null;
            if (value.length() <= 10) {
                format = _DATE_FORMATTER_;
            } else if (value.length() <= 19) {
                format = _DATETIME_FORMATTER_;
            } else if (value.length() <= 23) {
                format = _DATETIME_MILLISEC_FORMATTER_;
            } else {
                format = _DATETIME_MILLISEC_TZ_FORMATTER_;
            }
            try {
                date = format.parse( value );
            } catch (ParseException ex) {
                throw new IllegalArgumentException( ex.getMessage() );
            }
        }

        return date;
    }



    /**
     */
    public static Date dateValueOf(
                    final String value
                    )
    {
        Date  date = null;
        if (value != null) {
            try {
                date = _DATE_FORMATTER_.parse( value );
            } catch (ParseException ex) {
                throw new IllegalArgumentException( ex.getMessage() );
            }
        }

        return date;
    }

}
// IsoDate

/* vim:set tabstop=4:set expandtab:set shiftwidth=4: */

