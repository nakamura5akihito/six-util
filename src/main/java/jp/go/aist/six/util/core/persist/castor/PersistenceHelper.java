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

import jp.go.aist.six.util.persist.Persistable;



/**
 * A helper class for the Persistable object.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: PersistenceHelper.java 540 2013-03-08 08:09:29Z nakamura5akihito@gmail.com $
 */
public class PersistenceHelper<T extends Persistable<?>>
{

    /**
     * Returns a selector clause of a query statement to retrieve
     * the identity of object.
     * Typically, it is a "SELECT" clause in an SQL statement.
     */
    public String getIdentitySelector()
    {
        // NOTE:
        // "DISTINCT" is required for Castor.
        // If it is not, duplicated IDs are returned.
        return "SELECT DISTINCT o.persistentID ";
    }



    /**
     * Returns true if the type has the unique property.
     */
    public boolean hasUnique()
    {
        return false;
    }



    /**
     * Returns the unique value of the object.
     * The value must uniquely identify the object of the type T
     * in a data store.
     * Typically, it may be a key in an RDB.
     * If the uniqueness is provided by multiple-values,
     * the return value may be an array.
     */
    public Object getUnique(
                    final T object
                    )
    {
        throw new IllegalStateException( "uniqueness not defined for this type" );
    }



    /**
     * Returns a filter clause of a query statement to retrieve
     * the unique object.
     * Typically, it is a "WHERE" clause in an SQL statement.
     */
    public String getUniqueFilter()
    {
        return "";
//        return " WHERE o.persistentID = $1";
    }

}
// PersistenceHelper
