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
import jp.go.aist.six.util.persist.PersistenceException;



/**
 * A registry of Dao instances.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: DaoRegistry.java 540 2013-03-08 08:09:29Z nakamura5akihito@gmail.com $
 */
public interface DaoRegistry
{

    /**
     * Returns a Dao instance which supports the specified entity object type.
     *
     * @param <K>   the type of persistent ID of the supported entity objects.
     * @param <T>   the type of entity objects supported by the returned Dao.
     * @param type  the type of entity objects supported by the returned Dao.
     * @return
     *  the Dao.
     * @throws  PersistenceException;
     *  when an exceptional condition occurred.
     */
    public <K, T extends Persistable<K>> CastorDao<K, T> getDao( Class<T> type );

}
//
