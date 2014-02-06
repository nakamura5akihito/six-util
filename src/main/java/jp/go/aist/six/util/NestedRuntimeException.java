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



/**
 * A runtime exceptional condition.
 *
 * This is a subset of Spring Framework's NestedRuntimeException.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: NestedRuntimeException.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 * @see <a href="http://www.springframework.org">Spring Framework</a>
 */
public class NestedRuntimeException
    extends RuntimeException
{

    /**
     * Constructs a NestedRuntimeException with no detail message.
     */
    public NestedRuntimeException()
    {
        super();
    }



    /**
     * Constructs a NestedRuntimeException with the specified detail message.
     *
     * @param  message
     *   the detail message.
     */
    public NestedRuntimeException(
                    final String message
                    )
    {
        super( message );
    }



    /**
     * Constructs a NestedRuntimeException with the specified cause.
     *
     * @param   cause
     *  the cause.
     */
    public NestedRuntimeException(
                    final Throwable cause
                    )
    {
        super( cause );
    }



    /**
     * Constructs a NestedRuntimeException with the specified
     * detail message and cause.
     *
     * @param   message
     *  the detail message.
     * @param   cause
     *  the cause.
     */
    public NestedRuntimeException(
                    final String message,
                    final Throwable cause
                    )
    {
        super( message, cause );
    }



    /**
     * Retrieve the most specific cause of this exception, that is,
     * either the innermost cause (root cause) or this exception itself.
     *
     * @return
     *  the most specific cause (never <code>null</code>)
     */
    public Throwable getMostSpecificCause()
    {
        Throwable rootCause = null;
        Throwable cause = getCause();
        while (cause != null  &&  cause != rootCause) {
            rootCause = cause;
            cause = cause.getCause();
        }

        return (rootCause == null ? this : rootCause);
    }

}
// NestedRuntimeException

