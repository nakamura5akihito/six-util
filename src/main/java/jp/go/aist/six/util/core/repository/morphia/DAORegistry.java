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

import jp.go.aist.six.util.repository.ObjectTypeException;
import jp.go.aist.six.util.repository.RepositoryConfigurationException;
import org.mongodb.morphia.dao.DAO;



/**
 * A DAO registry.
 *
 * @author	Akihito Nakamura, AIST
 * @version $Id: DAORegistry.java 568 2013-04-15 09:15:44Z nakamura5akihito@gmail.com $
 */
public interface DAORegistry
{

    /**
     * Returns a DAO for the given entity type.
     *
     * @throws  ObjectTypeException
     *  if the given type is null.
     * @throws  RepositoryConfigurationException
     *  when a DAO for the given entity type was not found.
     */
    public <T, K> DAO<T, K> getDAO( Class<T> entity_type );

}
// DAORegistry
