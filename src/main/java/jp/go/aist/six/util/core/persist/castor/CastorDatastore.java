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

import java.util.Collection;
import java.util.List;
import jp.go.aist.six.util.persist.Datastore;
import jp.go.aist.six.util.persist.Persistable;
import jp.go.aist.six.util.persist.PersistenceException;
import jp.go.aist.six.util.search.Binding;
import jp.go.aist.six.util.search.Limit;
import jp.go.aist.six.util.search.Order;
import jp.go.aist.six.util.search.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;



/**
 * A data store implementation.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: CastorDatastore.java 540 2013-03-08 08:09:29Z nakamura5akihito@gmail.com $
 */
public class CastorDatastore
    implements Datastore //, DaoRegistry
{

    /**
     * Logger.
     */
    private static final Logger  _LOG_ =
        LoggerFactory.getLogger( CastorDatastore.class );
//    private static Log  _LOG = LogFactory.getLog( CastorDataStore.class );



    private PlatformTransactionManager  _txManager;
    private DaoRegistry  _daoRegistry;



    /**
     * Constructor.
     */
    public CastorDatastore()
    {
    }



    public void setTransactionManager(
                    final PlatformTransactionManager manager
                    )
    {
        _txManager = manager;
    }


    public PlatformTransactionManager getTransactionManager()
    {
        return _txManager;
    }



    public void setDaoRegistry(
                    final DaoRegistry registry
                    )
    {
        _daoRegistry = registry;
    }



    //**************************************************************
    //  DaoRegistry
    //**************************************************************

    public <K, T extends Persistable<K>>
    CastorDao<K, T> getDao(
                    final Class<T> type
                    )
    {
        CastorDao<K, T>  dao = _daoRegistry.getDao( type );
        dao.setDaoRegistry( _daoRegistry );

        return dao;
    }



    //**************************************************************
    //  DataStore
    //**************************************************************

    public <K, T extends Persistable<K>>
    K create(
                    final Class<T> type,
                    final T object
                    )
    {
        K  p_id = _executeTx( "create", type, object,
                        new TransactionCallback<K>()
                        {
                            public K doInTransaction( final TransactionStatus status )
                            {
                                return getDao( type ).create( object );
                            }
                        }
        );

        return p_id;
    }



    public <K, T extends Persistable<K>>
    void update(
                    final Class<T> type,
                    final T object
                    )
    {
        _executeTx( "update", type, object,
                        new TransactionCallbackWithoutResult()
                        {
                            @Override
                            public void doInTransactionWithoutResult( final TransactionStatus status )
                            {
                                getDao( type ).update( object );
                            }
                        }
        );
    }



    public <K, T extends Persistable<K>>
    void remove(
                    final Class<T> type,
                    final T object
                    )
    {
        _executeTx( "remove", type, object,
                        new TransactionCallbackWithoutResult()
                        {
                            @Override
                            public void doInTransactionWithoutResult( final TransactionStatus status )
                            {
                                getDao( type ).remove( object );
                            }
                        }
        );
    }



    public <K, T extends Persistable<K>>
    T sync(
                    final Class<T> type,
                    final T object
                    )
    {
        T  p_object = _executeTx( "sync", type, object,
                        new TransactionCallback<T>()
                        {
                            public T doInTransaction( final TransactionStatus status )
                            {
                                return getDao( type ).sync( object );
                            }
                        }
        );

        return p_object;
    }



    public <K, T extends Persistable<K>>
    List<T> syncAll(
                    final Class<T> type,
                    final List<? extends T> objects
                    )
    {
        List<T>  p_objects = _executeTx( "syncAll", type,
                        new TransactionCallback<List<T>>()
                        {
                            public List<T> doInTransaction( final TransactionStatus status )
                            {
                                return getDao( type ).syncAll( objects );
                            }
                        }
        );

        return p_objects;
    }



    public <K, T extends Persistable<K>>
    int count(
                    final Class<T> type
                    )
    {
        Integer  p_count = _executeTx( "countAll", type,
                        new TransactionCallback<Integer>()
                        {
                            public Integer doInTransaction( final TransactionStatus status )
                            {
                                return getDao( type ).count();
                            }
                        }
        );

        return p_count.intValue();
    }



    public <K, T extends Persistable<K>>
    int count(
                    final Class<T> type,
                    final Binding filter
                    )
    {
        Integer  p_count = _executeTx( "count", type, filter,
                        new TransactionCallback<Integer>()
                        {
                            public Integer doInTransaction( final TransactionStatus status )
                            {
                                return getDao( type ).count( filter );
                            }
                        }
        );

        return p_count.intValue();
    }



    public <K, T extends Persistable<K>>
    T load(
                    final Class<T> type,
                    final K identity
                    )
    {
        T  p_object = _executeTx( "load", type, identity,
                        new TransactionCallback<T>()
                        {
                            public T doInTransaction( final TransactionStatus status )
                            {
                                return getDao( type ).load( identity );
                            }
                        }
        );

        return p_object;
    }



    public <K, T extends Persistable<K>>
    List<T> loadAll(
                    final Class<T> type,
                    final List<? extends K> identities
                    )
    {
        List<T>  p_objects = _executeTx( "loadAll", type, identities,
                        new TransactionCallback<List<T>>()
                        {
                            public List<T> doInTransaction( final TransactionStatus status )
                            {
                                return getDao( type ).loadAll( identities );
                            }
                        }
        );

        return p_objects;
    }



    public <K, T extends Persistable<K>>
    Collection<T> find(
                    final Class<T> type
                    )
    {
        Collection<T>  p_objects = _executeTx( "getAll", type,
                        new TransactionCallback<Collection<T>>()
                        {
                            public Collection<T> doInTransaction( final TransactionStatus status )
                            {
                                return getDao( type ).find();
                            }
                        }
        );

        return p_objects;
    }



    public <K, T extends Persistable<K>>
    Collection<T> find(
                    final Class<T> type,
                    final Binding filter
                    )
    {
        return find( type, filter, null, null );
    }



    public <K, T extends Persistable<K>>
    Collection<T> find(
                    final Class<T> type,
                    final Binding filter,
                    final List<? extends Order> ordering,
                    final Limit limit
                    )
    {
        Collection<T>  p_objects = _executeTx( "find", type, filter,
                        new TransactionCallback<Collection<T>>()
                        {
                            public Collection<T> doInTransaction( final TransactionStatus status )
                            {
                                return getDao( type ).find( filter, ordering, limit );
                            }
                        }
        );

        return p_objects;
    }



    public <K, T extends Persistable<K>>
    Collection<K> findIdentity(
                    final Class<T> type
                    )
    {
        Collection<K>  p_ids = _executeTx( "findIdentity", type,
                        new TransactionCallback<Collection<K>>()
                        {
                            public Collection<K> doInTransaction( final TransactionStatus status )
                            {
                                return getDao( type ).findIdentity();
                            }
                        }
        );

        return p_ids;
    }



    public <K, T extends Persistable<K>>
    Collection<K> findIdentity(
                    final Class<T> type,
                    final Binding filter
                    )
    {
        return findIdentity( type, filter, null, null );
    }



    public <K, T extends Persistable<K>>
    Collection<K> findIdentity(
                    final Class<T> type,
                    final Binding filter,
                    final List<? extends Order> ordering,
                    final Limit limit
                    )
    {
        Collection<K>  p_ids = _executeTx( "findIdentity", type, filter,
                        new TransactionCallback<Collection<K>>()
                        {
                            public Collection<K> doInTransaction( final TransactionStatus status )
                            {
                                return getDao( type ).findIdentity( filter, ordering, limit );
                            }
                        }
        );

        return p_ids;
    }



    public <K, T extends Persistable<K>>
    List<Object> search(
                    final Class<T> type,
                    final SearchCriteria criteria
                    )
    {
        List<Object>  p_objects = _executeTx( "search", type, criteria,
                        new TransactionCallback<List<Object>>()
                        {
                            public List<Object> doInTransaction( final TransactionStatus status )
                            {
                                return getDao( type ).search( criteria );
                            }
                        }
        );

        return p_objects;
    }



    /**
     * Executes the specified action in a new transaction.
     */
    protected <T> T _executeTx(
                    final String operation,
                    final Class<? extends Persistable<?>> type,
                    final TransactionCallback<T> action
                    )
    {
        return _executeTx( operation, type, null, action );
    }



    protected <T> T _executeTx(
                    final String operation,
                    final Class<? extends Persistable<?>> type,
                    final Object value,
                    final TransactionCallback<T> action
                    )
    {
        Tx<T>  tx = new Tx<T>( operation, type, value, action, getTransactionManager() );

        return tx.execute();
    }



    /**
     * A helper object for a transaction.
     */
    private static class Tx<T>
    {
        private final String  _message;
        private final String  _value;
        private final TransactionCallback<T> _action;
        private final TransactionTemplate  _template;



        public Tx(
                        final String operation,
                        final Class<? extends Persistable<?>> type,
                        final Object value,
                        final TransactionCallback<T> action,
                        final PlatformTransactionManager txmgr
                        )
        {
            _message = ": " + operation + " - " + type.getName();
            _action = action;
            _template = new TransactionTemplate( txmgr );
            _template.setPropagationBehavior( TransactionDefinition.PROPAGATION_REQUIRED );
            _value = (value == null ? "" : (" - " + String.valueOf( value )));
        }



        public T execute()
        {
            if (_LOG_.isInfoEnabled()) {
                _LOG_.info( "TX begin" + _message + _value );
            }

            long  timestamp = System.currentTimeMillis();
            T  result = null;
            try {
                result = _template.execute( _action );
                                   //throws TransactionException
            } catch (TransactionException ex) {
                if (_LOG_.isErrorEnabled()) {
                    _LOG_.error( ex.getMessage() );
                }
                throw new PersistenceException( ex.getMostSpecificCause() );
            }

            if (_LOG_.isInfoEnabled()) {
                timestamp = System.currentTimeMillis() - timestamp;
                _LOG_.info( "TX end" + _message
                                + ": elapsed time (ms)=" + timestamp );
            }

            return result;
        }
    }
    // Tx

}
//
