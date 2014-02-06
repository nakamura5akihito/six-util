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

import java.util.HashMap;
import java.util.Map;
import jp.go.aist.six.util.persist.Persistable;
import jp.go.aist.six.util.persist.PersistenceException;
import org.castor.spring.orm.support.CastorDaoSupport;
import org.exolab.castor.jdo.JDOManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: FactoryDaoRegistry.java 540 2013-03-08 08:09:29Z nakamura5akihito@gmail.com $
 */
public class FactoryDaoRegistry
    implements DaoRegistry
{

    /**
     * Logger.
     */
    private static final Logger  _LOG_ =
        LoggerFactory.getLogger( FactoryDaoRegistry.class );
//   private static Log  _LOG = LogFactory.getLog( FactoryDaoRegistry.class );



    private final Map<String, String>  _typeMapping = new HashMap<String, String>();


    private final Map<Class<? extends Persistable<?>>, CastorDao<?, ?>>  _daoMapping =
        new HashMap<Class<? extends Persistable<?>>, CastorDao<?, ?>>();


    private JDOManager  _jdoManager;



    /**
     * Constructor.
     */
    public FactoryDaoRegistry()
    {
    }



    /**
     */
    public void setJDOManager(
                    final JDOManager manager
                    )
    {
        _jdoManager = manager;
//        InstanceFactory  factory = _jdoManager.getInstanceFactory();  // null!!!
//        if (_LOG.isTraceEnabled()) {
//            _LOG.trace( "Castor JDOManager's InstanceFactory: " + factory );
//        }
//        _instanceFactory.setInstancefactory( factory );

//        _jdoManager.setInstanceFactory( _instanceFactory );
    }



    /**
     */
    public void setDaoTypeMapping(
                    final Map<String, String> map
                    )
    {
        if (map == null) {
            return;
//            throw new IllegalArgumentException( "null mapping" );
        }

        _typeMapping.putAll( map );
        if (_LOG_.isDebugEnabled()) {
            _LOG_.debug( "Dao type mapping: " + _typeMapping );
        }
    }



    /**
     * Creates a Dao for the specified object type.
     */
    private <K, T extends Persistable<K>> CastorDao<K, T> _createDao(
                    final Class<T> type
                    )
    {
        String  daoClazzName = _typeMapping.get( type.getName() );
        if (_LOG_.isDebugEnabled()) {
            _LOG_.debug( "creating Dao: object type=" + type
                            + ", Dao type=" + daoClazzName );
        }

        CastorDao<K, T>  dao = null;
        if (daoClazzName == null) {
            dao = new CastorDao<K, T>( type );
        } else {
            try {
                @SuppressWarnings( "unchecked" )
                Class<CastorDao<K, T>>  daoClazz =
                    (Class<CastorDao<K, T>>)Class.forName( daoClazzName );
                dao = daoClazz.newInstance();
                               //throws InstantiationException, IllegalAccessException
            } catch (Exception ex) {
                throw new PersistenceException( ex );
            }
        }

        if (CastorDaoSupport.class.isInstance( dao )) {
            CastorDaoSupport  support = CastorDaoSupport.class.cast( dao );
            support.setJDOManager( _jdoManager );
        }

        return dao;
    }



    //**************************************************************
    //  DaoRegistry
    //**************************************************************

    public <K, T extends Persistable<K>> CastorDao<K, T> getDao(
                    final Class<T> type
                    )
    {
        if (type == null) {
            throw new IllegalArgumentException( "null type" );
        }

        @SuppressWarnings( "unchecked" )
        CastorDao<K, T>  dao = (CastorDao<K, T>)_daoMapping.get( type );

        if (dao == null) {
            dao = _createDao( type );
            _daoMapping.put( type, dao );
        }

        return dao;
    }

}
//
