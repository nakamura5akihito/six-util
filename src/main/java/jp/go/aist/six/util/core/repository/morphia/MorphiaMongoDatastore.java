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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.go.aist.six.util.repository.CommonQueryParams;
import jp.go.aist.six.util.repository.Datastore;
import jp.go.aist.six.util.repository.ObjectNotFoundException;
import jp.go.aist.six.util.repository.ObjectTypeException;
import jp.go.aist.six.util.repository.QueryException;
import jp.go.aist.six.util.repository.QueryParams;
import jp.go.aist.six.util.repository.RepositoryConfigurationException;
import jp.go.aist.six.util.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.DAO;
import org.mongodb.morphia.query.Query;



/**
 * A MongoDB/Morphia implementation of the Datastore.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: MorphiaMongoDatastore.java 578 2013-04-16 06:33:01Z nakamura5akihito@gmail.com $
 */
public abstract class MorphiaMongoDatastore
    implements Datastore, DAORegistry
{

    /**
     * Logger.
     */
    private static final Logger  _LOG_ = LoggerFactory.getLogger( MorphiaMongoDatastore.class );



    /**
     * Class - DAO
     */
    private final Map<Class<?>, DAO<?, ?>>  _daoMap = new HashMap<Class<?>, DAO<?, ?>>();



    private QueryBuilderFactory  _query_builder_factory;



    /**
     * Constructor.
     */
    public MorphiaMongoDatastore()
    {
    }


    public MorphiaMongoDatastore(
                    final QueryBuilderFactory query_builder_factory
                    )
    {
        setQueryBuilderFactory( query_builder_factory );
    }



    /**
     * DAO list injection point.
     */
    public void setDAO(
                    final Collection<? extends DAO<?, ?>> dao_list
                                    )
    {
        if (dao_list == null) {
            return;
        }

        for (DAO<?, ?> dao : dao_list) {
            if (dao == null) {
                continue;
            }

            Class<?>  entityClass = dao.getEntityClass();
            _LOG_.debug( "adding DAO: " + entityClass );
            _daoMap.put( entityClass, dao );

            if (dao instanceof BaseDAO) {
                BaseDAO.class.cast( dao ).setDAORegistry( this );
            }
        }
    }



    /**
     * QueryBuilderFactory injection point.
     */
    public void setQueryBuilderFactory(
                    final QueryBuilderFactory factory
                    )
    {
        _query_builder_factory = factory;
    }


    public QueryBuilderFactory _getQueryBuilderFactory()
    {
        if (_query_builder_factory == null) {
            throw new RepositoryConfigurationException( "no QueryBuilderFactory configured" );
        }

        return _query_builder_factory;
    }



//    private final void _startOperation(
//                    final String operation_name,
//                    final String message
//                    )
//    {
//        _LOG_.info( operation_name + ": " + message );
//    }
//
//
//    private final void _endOperation(
//                    final String operation_name,
//                    final long start_timestamp,
//                    final String message
//                    )
//    {
//        _LOG_.info( operation_name
//                        + ": elapsed time (ms)="
//                        +  (System.currentTimeMillis() - start_timestamp) );
//        _LOG_.debug( message );
//    }




    //*********************************************************************
    //  query helper methods
    //*********************************************************************

    protected <T, K extends Serializable>
    Query<T> _buildQuery(
                    final DAO<T, K>  dao,
                    final Class<T> type,
                    final QueryParams params
                    )
    {
        Query<T>  query = null;
        try {
            query = dao.createQuery();
            QueryBuilder  builder = _getQueryBuilderFactory().newBuilder( type );
            query = builder.build( query, params );
        } catch (RepositoryConfigurationException ex) {
            throw ex;
        } catch (QueryException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new QueryException( ex );
        }

        return query;
    }


//    protected abstract <T, K extends Serializable>
//    Query<T> _buildQuery(
//                    final DAO<T, K>  dao,
//                    final Class<T> type,
//                    final QueryParams params
//                    )
//    {
//        Query<T>  query = null;
//        try {
//            query = dao.createQuery();
//            QueryBuilder  builder = MorphiaQueryBuilder.getInstance( type );
//            query = builder.build( query, params );
//        } catch (Exception ex) {
//            throw new VulnException( ex );
//        }
//
//        return query;
//    }



    /**
     * Converts Morphia Key<T> to K, i.e. "_id".
     *
     * @throws  RepositoryException
     */
    protected static final <T, K extends Serializable>
    K _key2Id(
                    final Key<T> key
                    )
    {
        K  id = null;
        try {
            @SuppressWarnings( "unchecked" )
            K  obj = (K)key.getId();
            id = obj;
        } catch (Exception ex) {
            throw new RepositoryException( ex );
        }

        return id;
    }



    /**
     * Converts Morphia Key<T> list to K, i.e. "_id", list.
     *
     * @throws  OvalRepositoryException
     */
    protected static final <T, K extends Serializable>
    List<K> _keys2Ids(
                    final Collection<Key<T>> keys
                    )
    {
        List<K>  ids = new ArrayList<K>();
        if (keys != null ) {
            for (Key<T>  key : keys) {
                @SuppressWarnings( "unchecked" )
                K  id = (K)key.getId();
                ids.add( id );
            }
        }

        return ids;
    }



    //*********************************************************************
    //  implements OvalDatastore
    //*********************************************************************

    public <T, K extends Serializable>
    T findById(
                    final Class<T> type,
                    final K id
                    )
    {
       _LOG_.info( "findById: type=" + type + ", ID=" + id );
       long  ts_start = System.currentTimeMillis();

       T  p_object = null;
       try {
           p_object = getDAO( type ).get( id );
       } catch (Exception ex) {
           throw new RepositoryException( ex );
       }

       _LOG_.info( "findById: elapsed time (ms)=" +  (System.currentTimeMillis() - ts_start) );
       _LOG_.debug( (p_object == null ? "findById: object NOT found" : "findById: object found") );
       if (p_object == null) {
           ObjectNotFoundException  ex = new ObjectNotFoundException( "type=" + type + ", id=" + id );
           ex.setType( type );
           ex.setId( id );
           throw ex;
       }

       return p_object;
   }



    public <T, K extends Serializable>
    boolean exists( final Class<T> type, final K id )
    {
        _LOG_.info( "exists: type=" + type + ", ID=" + id );
        long  ts_start = System.currentTimeMillis();

        boolean  exists = false;
        try {
            exists = getDAO( type ).exists( "_id", id );    //TODO: Test this!
//            exists = (getDAO( type ).get( id ) != null);
        } catch (Exception ex) {
            throw new RepositoryException( ex );
        }

        _LOG_.info( "exists: elapsed time (ms)=" +  (System.currentTimeMillis() - ts_start) );
        _LOG_.debug( (exists ? "exists: object NOT found" : "exists: object found") );
        return exists;
    }



    public <T, K extends Serializable>
    List<T> find(
                    final Class<T> type
                    )
    {
        return find( type, null );

//       _LOG_.info( "find: type=" + type );
//       long  ts_start = System.currentTimeMillis();
//
//       DAO<T, K>  dao = getDAO( type );
//       List<T>  list = dao.find().asList();
//
//       _LOG_.info( "find: elapsed time (ms)=" +  (System.currentTimeMillis() - ts_start) );
//       _LOG_.debug( "find: #objects=" + (list == null ? 0 : list.size()) );
//       return list;
    }



    public <T, K extends Serializable>
    List<T> find(
                    final Class<T> type,
                    final QueryParams params
                    )
    {
        _LOG_.info( "find: type=" + type + ", params=" + params );
        long  ts_start = System.currentTimeMillis();

        List<T>  list = null;
        try {
            DAO<T, K>  dao = getDAO( type );
            if (params == null) {
                list = dao.find().asList();
            } else {
                Query<T>  query = _buildQuery( dao, type, params );
                _LOG_.debug( "query=" + query );
                list = dao.find( query ).asList();
            }
        } catch (RepositoryConfigurationException ex) {
            throw ex;
        } catch (QueryException ex) {
            throw ex;
        } catch (RepositoryException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RepositoryException( ex );
        }

        _LOG_.info( "find: elapsed time (ms)=" + (System.currentTimeMillis() - ts_start) );
        _LOG_.debug( "find: #objects=: " + (list == null ? 0 : list.size()) );
        return list;
    }



    public <T, K extends Serializable>
    List<K> findId(
                    final Class<T> type
                    )
    {
        return findId( type, null );

//        _LOG_.info( "findId: type=" + type );
//        long  ts_start = System.currentTimeMillis();
//
//        DAO<T, K>  dao = getDAO( type );
//        List<Key<T>>  list = dao.find().asKeyList();
//
//        _LOG_.info( "findId: elapsed time (ms)=" + (System.currentTimeMillis() - ts_start) );
//        _LOG_.debug( "findId: #IDs=: " + (list == null ? 0 : list.size()) );
//        return _keys2Ids( list );
    }



    public <T, K extends Serializable>
    List<K> findId(
                    final Class<T> type,
                    final QueryParams params
                    )
    {
        _LOG_.debug( "findId: type=" + type + ", params=" + params );
        long  ts_start = System.currentTimeMillis();

        List<Key<T>>  list = null;
        try {
            DAO<T, K>  dao = getDAO( type );
            if (params == null) {
                list = dao.find().asKeyList();
            } else {
                Query<T>  query = _buildQuery( dao, type, params );
                _LOG_.debug( "query=" + query );
                list = dao.find( query ).asKeyList();
            }
        } catch (RepositoryConfigurationException ex) {
            throw ex;
        } catch (QueryException ex) {
            throw ex;
        } catch (RepositoryException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RepositoryException( ex );
        }

        _LOG_.info( "findId: elapsed time (ms)=" + (System.currentTimeMillis() - ts_start) );
        _LOG_.debug( "findId: #IDs=: " + (list == null ? 0 : list.size()) );
        return _keys2Ids( list );
    }



    public <T, K extends Serializable>
    long count(
                    final Class<T> type
                    )
    {
        return count( type, null );

//        _LOG_.info( "count: type=" + type );
//        long  ts_start = System.currentTimeMillis();
//
//        DAO<T, K>  dao = getDAO( type );
//        long  count = dao.count();
//
//        _LOG_.info( "count: elapsed time (ms)="
//                        + (System.currentTimeMillis() - ts_start) );
//        _LOG_.debug( "count: count=" + count );
//        return count;
    }



    public <T, K extends Serializable>
    long count(
                    final Class<T> type,
                    final QueryParams params
                    )
    {
        _LOG_.info( "count: type=" + type + ", params=" + params );
        long  ts_start = System.currentTimeMillis();

        long  count = 0L;
        try {
            DAO<T, K>  dao = getDAO( type );
            if (params == null) {
                count = dao.count();
            } else {
                // NOTE: count() query ignores the "count" parameter.
                QueryParams  adjusted_params = params;
                if (params.containsKey( CommonQueryParams.Key.COUNT )) {
                    try {
                        adjusted_params = QueryParams.class.cast( params.clone() );
                    } catch (CloneNotSupportedException ex) {
                        throw new RepositoryException( ex );
                    }

                    adjusted_params.remove( CommonQueryParams.Key.COUNT );
                }

                Query<T>  query = _buildQuery( dao, type, adjusted_params );
                _LOG_.debug( "query=" + query );
                count = dao.count( query );
            }
        } catch (RepositoryConfigurationException ex) {
            throw ex;
        } catch (QueryException ex) {
            throw ex;
        } catch (RepositoryException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RepositoryException( ex );
        }

        _LOG_.info( "count: elapsed time (ms)=" + (System.currentTimeMillis() - ts_start) );
        _LOG_.debug( "count: count=" + count );
        return count;
    }



    public <T, K extends Serializable>
    K save(
                    final Class<T> type,
                    final T object
                    )
    {
        _LOG_.info( "save: type=" + type );
        long  ts_start = System.currentTimeMillis();

        Key<T>  key = null;
        try {
            key = getDAO( type ).save( object );
        } catch (RepositoryConfigurationException ex) {
            throw ex;
        } catch (RepositoryException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RepositoryException( ex );
        }

        K  id = _key2Id( key );

        _LOG_.info( "save: elapsed time (ms)=" + (System.currentTimeMillis() - ts_start) );
        _LOG_.info( "save: ID=" + id );
        return id;
   }



    public <T, K extends Serializable>
    void deleteById(
                    final Class<T> type,
                    final K id
                    )
    {
        _LOG_.info( "deleteById: type=" + type + ", ID=" + id );
        long  ts_start = System.currentTimeMillis();

        try {
            getDAO( type ).deleteById( id );
        } catch (RepositoryConfigurationException ex) {
            throw ex;
        } catch (RepositoryException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RepositoryException( ex );
        }

        _LOG_.info( "deleteById: elapsed time (ms)=" + (System.currentTimeMillis() - ts_start) );
    }



    public <T, K extends Serializable>
    void delete(
                    final Class<T> type
                    )
    {
        _LOG_.debug( "delete: type=" + type );
        long  ts_start = System.currentTimeMillis();

        /*
         * TODO: performance Is it possible to delete all the objects by query?
         * dao.deleteByQuery( Query q )
         */
        try {
            List<K>  id_list = findId( type );
            if (id_list != null  &&  id_list.size() > 0) {
                DAO<T, K>  dao = getDAO( type );
                for (K  id : id_list) {
                    dao.deleteById( id );
                }
            }
        } catch (RepositoryConfigurationException ex) {
            throw ex;
        } catch (RepositoryException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RepositoryException( ex );
        }

        _LOG_.info( "delete: elapsed time (ms)=" + (System.currentTimeMillis() - ts_start) );
    }



    //**********************************************************************
    //  implements DAORegistry
    //**********************************************************************

    public <T, K> DAO<T, K> getDAO(
                    final Class<T> entityClass
                    )
    {
        if (entityClass == null) {
            throw new ObjectTypeException( "entity type NOT specified (null)" );
        }

        DAO<T, K>  dao = null;
        try {
            @SuppressWarnings( "unchecked" )
            DAO<T, K>  dao_tmp = (DAO<T, K>)_daoMap.get( entityClass );
                                 //throws ClassCastException
            dao = dao_tmp;
        } catch (Exception ex) {
            throw new RepositoryConfigurationException( ex );
        }

        if (dao == null) {
            throw new RepositoryConfigurationException( "unknown entity type: " + entityClass );
        }

        return dao;
    }

}
//
