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

import java.util.ArrayList;
import java.util.List;
import org.castor.spring.orm.CastorCallback;
import org.castor.spring.orm.CastorTemplate;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.springframework.dao.DataAccessException;



/**
 * This class extends the CastorTemplate by adding count function.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: ExtendedCastorTemplate.java 540 2013-03-08 08:09:29Z nakamura5akihito@gmail.com $
 */
public class ExtendedCastorTemplate
extends CastorTemplate
{

    /**
     * Constructor.
     */
    public ExtendedCastorTemplate()
    {
    }


    /**
     * Constructor.
     */
    public ExtendedCastorTemplate(
                    final JDOManager jdoManager
                    )
    {
        super( jdoManager );
    }


    /**
     * Constructor.
     */
    public ExtendedCastorTemplate(
                    final JDOManager jdoManager,
                    final boolean allowCreate
                    )
    {
        super( jdoManager, allowCreate );
    }



    /**
     */
    public boolean isPersistent(
                    final Object entity
                    )
    throws DataAccessException
    {
        Boolean isPersistent = (Boolean)execute(new CastorCallback() {
            public Object doInCastor(final Database database) throws PersistenceException {
                boolean  result = database.isPersistent( entity );
                return Boolean.valueOf( result );
            }
        });

        return isPersistent.booleanValue();
    }



    /**
     */
    public List<Object> findByQuery(
                    final String oql,
                    final Object[] params
                    )
    throws DataAccessException
    {
        @SuppressWarnings( "unchecked" )
        List<Object>  results = (List<Object>)executeFind(new CastorCallback() {
            public Object doInCastor(final Database database) throws PersistenceException {
                OQLQuery  query = database.getOQLQuery( oql );
                if (params != null) {
                    for (int  i = 0; i < params.length; i++) {
                        query.bind( params[i] );
                    }
                }
                prepareQuery( query );
                QueryResults  queryResults = query.execute();
                List<Object>  objects = new ArrayList<Object>();
                while (queryResults.hasMore()) {
                    objects.add( queryResults.next() );
                }
                return objects;
            }
        });

        return results;
    }

}
// ExtendedCastorTemplate
