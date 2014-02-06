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

import jp.go.aist.six.util.NestedRuntimeException;



/**
 * An exceptional condition that occurred during the object-persistence process.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: PersistenceException.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class PersistenceException
    extends NestedRuntimeException
{

    /**
     * Constructs a PersistenceException with no detail message.
     */
    public PersistenceException()
    {
        super();
    }



    /**
     * Constructs a PersistenceException with the specified detail message.
     *
     * @param  message
     *   the detail message.
     */
    public PersistenceException(
                    final String message
                    )
    {
        super( message );
    }



    /**
     * Constructs a PersistenceException with the specified cause.
     *
     * @param   cause
     *  the cause.
     */
    public PersistenceException(
                    final Throwable cause
                    )
    {
        super( cause );
    }



    /**
     * Constructs a PersistenceException with the specified
     * detail message and cause.
     *
     * @param   message
     *  the detail message.
     * @param   cause
     *  the cause.
     */
    public PersistenceException(
                    final String message,
                    final Throwable cause
                    )
    {
        super( message, cause );
    }

}
// PersistenceException
