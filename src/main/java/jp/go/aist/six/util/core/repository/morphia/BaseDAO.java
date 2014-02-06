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

import jp.go.aist.six.util.repository.RepositoryConfigurationException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.dao.DAO;
import com.mongodb.Mongo;



/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: BaseDAO.java 567 2013-04-15 08:49:40Z nakamura5akihito@gmail.com $
 */
public abstract class BaseDAO<T, K>
    extends BasicDAO<T, K>
{

    private DAORegistry  _registry;


    /**
     * Constructor.
     */
    public BaseDAO(
                    final Class<T> entityClass,
                    final Datastore ds
                    )
    {
        super( entityClass, ds );
    }


    public BaseDAO(
                    final Class<T> entityClass,
                    final Mongo mongo,
                    final Morphia morphia,
                    final String dbName
                    )
    {
        super( entityClass, mongo, morphia, dbName );
    }



    /**
     */
    public void setDAORegistry(
                    final DAORegistry registry
                    )
    {
        this._registry = registry;
    }



    /**
     */
    protected <S, J> DAO<S, J> _getForwardingDAO(
                    final Class<S> entity_type
                    )
    {
        DAO<S, J>  dao = null;

        if (this._registry != null) {
            dao = this._registry.getDAO( entity_type );
        }

        if (dao == null) {
            throw new RepositoryConfigurationException( "No such DAO: entity class=" + entity_type );
        }

        return dao;
    }

}
//

