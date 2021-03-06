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
package jp.go.aist.six.util.persist;



/**
 * Thrown to indicate that
 * an exception with the query, either syntax, query parameters
 * or inability to perform the query against the data store.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: DataRetrievalException.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class DataRetrievalException
    extends PersistenceException
{

    /**
     * Constructs a DataRetrievalException with no detail message.
     */
    public DataRetrievalException()
    {
        super();
    }



    /**
     * Constructs a DataRetrievalException with the specified detail message.
     *
     * @param  message
     *   the detail message.
     */
    public DataRetrievalException(
                    final String message
                    )
    {
        super( message );
    }



    /**
     * Constructs a DataRetrievalException with the specified cause.
     *
     * @param   cause
     *  the cause.
     */
    public DataRetrievalException(
                    final Throwable cause
                    )
    {
        super( cause );
    }



    /**
     * Constructs a DataRetrievalException with the specified
     * detail message and cause.
     *
     * @param   message
     *  the detail message.
     * @param   cause
     *  the cause.
     */
    public DataRetrievalException(
                    final String message,
                    final Throwable cause
                    )
    {
        super( message, cause );
    }

}
// DataRetrievalException
