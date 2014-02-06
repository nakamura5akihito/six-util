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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import jp.go.aist.six.util.BeansUtil;
import jp.go.aist.six.util.persist.Dao;
import jp.go.aist.six.util.persist.Persistable;
import jp.go.aist.six.util.persist.PersistenceException;
import jp.go.aist.six.util.search.Binding;
import jp.go.aist.six.util.search.Limit;
import jp.go.aist.six.util.search.Order;
import jp.go.aist.six.util.search.SearchCriteria;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.TimeStampable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;



/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: CastorDao.java 540 2013-03-08 08:09:29Z nakamura5akihito@gmail.com $
 */
public class CastorDao<K, T extends Persistable<K>>
    extends ExtendedCastorDaoSupport    //extends CastorDaoSupport
    implements Dao<K, T>
{

    /**
     * Logger.
     */
    private static final Logger  _LOG_ =
        LoggerFactory.getLogger( CastorDao.class );
//    private static Log  _LOG = LogFactory.getLog( CastorDao.class );


    private Class<? extends T>  _objectType;

    private String  _objectTypeName;

    private PersistenceHelper<? super T>  _helper;

    private DaoRegistry  _registry;


    private String  _daoProfile;



    /**
     * Constructor.
     */
    public CastorDao()
    {
    }


    /**
     * Constructor.
     */
    public CastorDao(
                    final Class<? extends T> type
                    )
    {
        this( type, new PersistenceHelper<T>() );
    }


    /**
     * Constructor.
     */
    public CastorDao(
                    final Class<? extends T> type,
                    final PersistenceHelper<? super T> helper
                    )
    {
        setObjectType( type );
        setPersistenceHelper( helper );
    }



    public void setDaoRegistry(
                    final DaoRegistry registry
                    )
    {
        _registry = registry;
    }



    public <L, S extends Persistable<L>>
    CastorDao<L, S> getForwardingDao(
                    final Class<S> type
                    )
    {
        return _registry.getDao( type );
    }



    /**
     */
    public void setObjectType(
                    final Class<? extends T> type
                    )
    {
        if (type == null) {
            throw new IllegalArgumentException( "null object type" );
        }

        if (_objectType != null) {
            throw new IllegalStateException( "object type already set: "
                            + _objectType.getName() );
        }

        _objectType = type;
        _objectTypeName = _objectType.getName();
        _daoProfile = _objectTypeName + ", DAO=" + getClass().getName();
        if(_LOG_.isDebugEnabled()) {
            _LOG_.debug( _daoProfile );
        }
    }



    /**
     */
    public void setPersistenceHelper(
                    final PersistenceHelper<? super T> helper
                    )
    {
        if (helper == null) {
            throw new IllegalArgumentException( "null helper" );
        }

        if (_helper != null) {
            throw new IllegalStateException( "helper already set: "
                            + _helper.getClass().getName() );
        }

        _helper = helper;
        if(_LOG_.isDebugEnabled()) {
            _LOG_.debug( "helper="
                            + (_helper == null ? "null" : _helper.getClass().getName())
                            + ", "
                            + (_daoProfile == null ? getClass().getName() : _daoProfile)
            );
        }
    }



    /**
     * Converts the type of specified object to array.
     * If the type of the object is array, it is simply casted.
     * Otherwise, an unary array that contains only the specified object
     * is created.
     */
    private Object[] _asArray(
                    final Object obj
                    )
    {
        Object[]  array = null;
        if (obj instanceof Object[]) {
            array = (Object[])obj;
            if (array.length == 0) {
                throw new IllegalArgumentException( "ERROR: empty array" );
            }
        } else {
            array = new Object[] { obj };
        }

        return array;
    }



//    /**
//     * The specified array is valid if it is not null,
//     * the length is greater than or equal to 1, and
//     * it does not contain null.
//     */
//    private boolean _isValidArray(
//                    final Object[] array
//                    )
//    {
//        if (array == null) {
//            return false;
//        } else if (array.length == 0) {
//            return false;
//        }
//
//        boolean  valid = true;
//        for (Object  o : array) {
//            if (o == null) {
//                valid = false;
//                break;
//            }
//        }
//
//        return valid;
//    }



//    private boolean _containsNull(
//                    final Object[] array
//                    )
//    {
//        boolean  contains = false;
//        for (Object  o : array) {
//            if (o == null) {
//                contains = true;
//                break;
//            }
//        }
//
//        return contains;
//    }



    //**************************************************************
    // low-level methods:
    //  All the Castor and Spring dependent exceptions are translated
    //  to SIX exceptions at this level.
    //**************************************************************

    /**
     */
    protected final boolean _jdoIsPersistent(
                    final Object object
                    )
    {
        boolean  persistent = false;

        try {
            persistent = getExtendedCastorTemplate().isPersistent( object );
        } catch (DataAccessException ex) {
            throw new PersistenceException( ex.getMostSpecificCause() );
        }

        return persistent;
    }



    /**
     * Load the object of the specified identity.
     * If no such object exist, this method returns NULL.
     */
    protected T _jdoLoad(
                    final K id
                    )
    {
        if (id == null) {
            return null;
        }

        Object  p_object = null;
        try {
            p_object = getCastorTemplate().load( _objectType, id );
        } catch (DataAccessException ex) {
            Throwable  cause = ex.getMostSpecificCause();
            if (ObjectNotFoundException.class.isInstance( cause )) {
                p_object = null;
            } else {
                throw new PersistenceException( cause );
            }
        }


        T  typedObject = _objectType.cast( p_object );
//        _afterLoad( typedObject );

        return typedObject;
    }



//    /**
//     */
//    private List<T> _jdoFind(
//                    final String filter,
//                    final Object[] params,
//                    final String ordering
//                    )
//    {
//        List<T>  p_objects = null;
//        try {
//            @SuppressWarnings( "unchecked" )
//            List<T>  pp_objects = (List<T>)getCastorTemplate().find(
//                            _objectType, filter, params, ordering );
//            p_objects = pp_objects;
//        } catch (DataAccessException ex) {
//            throw new PersistenceException( ex.getMostSpecificCause() );
//        }
//
//        return p_objects;
//    }



    /**
     * Executes the specified OQL query.
     */
    private List<Object> _jdoExecuteQuery(
                    final String oql,
                    final Object[] params
                    )
    {
        List<Object>  results = null;
        try {
            results = getExtendedCastorTemplate().findByQuery( oql, params );
        } catch (DataAccessException ex) {
            throw new PersistenceException( ex.getMostSpecificCause() );
        }

        return results;
    }



    /**
     */
    private void _jdoCreate(
                    final Object object
                    )
    {
        try {
            getCastorTemplate().create( object );
        } catch (DataAccessException ex) {
            Throwable  cause = ex.getMostSpecificCause();
            if (DuplicateIdentityException.class.isInstance( cause )) {
                throw new PersistenceException( cause );
                //TODO:
                //throw new DuplicateObjectException( cause );
            } else {
                throw new PersistenceException( cause );
            }
        }
    }



    private void _jdoUpdate(
                    final T object
                    )
    {
        try {
            getCastorTemplate().update( object );
        } catch (DataAccessException ex) {
            throw new PersistenceException( ex.getMostSpecificCause() );
        }
    }



    private void _jdoRemove(
                    final T object
                    )
    {
        try {
            getCastorTemplate().remove( object );
        } catch (DataAccessException ex) {
            throw new PersistenceException( ex.getMostSpecificCause() );
        }
    }




    //**************************************************************
    //  mid-level methods
    //**************************************************************

//    /**
//     * low-level method ???
//     */
//    private List<T> _find(
//                    final String filterStatement,
//                    final Object[] paramValues,
//                    final String orderingStatement,
//                    final Limit limit
//                    )
//    {
//        if(_LOG.isDebugEnabled()) {
//            _LOG.debug( "filter: " + filterStatement );
//            _LOG.debug( "params: " + Arrays.toString( paramValues ) );
//            _LOG.debug( "ordering: " + orderingStatement );
//            _LOG.debug( "limit: " + limit );
//        }
//
//        List<T>  p_objects = _jdoFind(
//                        filterStatement, paramValues, orderingStatement );
//
//        if (p_objects != null) {
//            if (limit != null) {
//                p_objects = limit.apply( p_objects );
//            }
//
//            for (T  p_object : p_objects) {
//                if (p_object != null) {
//                    _daoAfterLoad( p_object );
//                }
//            }
//        }
//
//        return p_objects;
//    }



    /**
     *
     */
    private List<Object> _search(
                    final SearchCriteria criteria
                    )
    {
        OQL  oql = new OQL( _objectType, "o", criteria );
        String  oqlStatement = oql.getStatement();
        Object[]  params = oql.getParameterValues();

        List<Object>  pObjs = _jdoExecuteQuery( oqlStatement, params );
        if (pObjs != null  &&  pObjs.size() > 0) {
            for (Object  pObj : pObjs) {
                if (_objectType.isInstance( pObj )) {
                    @SuppressWarnings( "unchecked" )
                    T  obj = (T)pObj;
                    _daoAfterLoad( obj );
                }
            }
        }

        return pObjs;
    }



    /**
     */
    private List<T> _find(
                    final Binding filter,
                    final List<? extends Order> ordering,
                    final Limit limit
                    )
    {
        List<K>  ids = _findIdentity( filter, ordering, limit );
        List<T>  objs = _loadAll( ids );

        return objs;
    }
//    {
//        String  filterStatement = null;
//        String  orderingStatement = null;
//        Object[]  paramValues = null;
//
//        if (filter != null  ||  ordering != null) {
//            SearchCriteria  criteria = new SearchCriteria();
//            criteria.setBinding( filter );
//            criteria.setOrders( ordering );
//
//            OQL  oql = new OQL( _objectType, "o", criteria );
//            filterStatement = oql.getWhereClause();
//            orderingStatement = oql.getOrdering();
//            paramValues = oql.getParameterValues();
//        }
//        if(_LOG.isDebugEnabled()) {
//            _LOG.debug( "filter: " + filterStatement );
//            _LOG.debug( "params: " + Arrays.toString( paramValues ) );
//            _LOG.debug( "ordering: " + orderingStatement );
//        }
//
//        List<T>  p_objects = _jdoFind(
//                        filterStatement, paramValues, orderingStatement );
//
//        if (p_objects != null) {
//            if (limit != null) {
//                p_objects = limit.apply( p_objects );
//            }
//
//            for (T  p_object : p_objects) {
//                _daoAfterLoad( p_object );
//            }
//        }
//
//        return p_objects;
//    }



    /**
     */
    private List<K> _findIdentity(
                    final Binding filter,
                    final List<? extends Order> ordering,
                    final Limit limit
                    )
    {
        String  oqlFilter = null;
        String  oqlOrdering = null;
        Object[]  params = null;

        if (filter != null  ||  ordering != null) {
            SearchCriteria  criteria = new SearchCriteria();
            criteria.setBinding( filter );
            criteria.setOrders( ordering );

            OQL  oql = new OQL( _objectType, "o", criteria );
            oqlFilter = oql.getWhereClause();
            oqlOrdering = oql.getOrdering();
            params = oql.getParameterValues();
        }

        List<K>  ids = _findIdentity( oqlFilter, params, oqlOrdering );
        if (ids != null) {
            if (limit != null) {
                ids = limit.apply( ids );
            }
        }

        return ids;
    }
//    {
//        String  oqlWhere = null;
//        String  oqlOrdering = null;
//        Object[]  params = null;
//
//        StringBuilder  s = new StringBuilder();
//        s.append( _helper.getIdentitySelector() ).append( " " );
//        s.append( " FROM " + _objectTypeName + " o " );
//
//        if (filter != null  ||  ordering != null) {
//            SearchCriteria  criteria = new SearchCriteria();
//            criteria.setBinding( filter );
//            criteria.setOrders( ordering );
//
//            OQL  oql = new OQL( _objectType, "o", criteria );
//            oqlWhere = oql.getWhereClause();
//            oqlOrdering = oql.getOrdering();
//            params = oql.getParameterValues();
//
//            if (oqlWhere != null) {
//                s.append( oqlWhere );
//            }
//            if (oqlOrdering != null ) {
//                s.append( oqlOrdering );
//            }
//        }
//
//        String  oqlStatement = s.toString();
//        if(_LOG.isDebugEnabled()) {
//            _LOG.debug( "OQL statement: " + oqlStatement );
//            _LOG.debug( "OQL params: " + Arrays.toString( params ) );
//        }
//
//        @SuppressWarnings( "unchecked" )
//        List<K>  p_ids = (List<K>)_jdoExecuteQuery( oqlStatement, params );
//
//        if (p_ids != null) {
//            if (limit != null) {
//                p_ids = limit.apply( p_ids );
//            }
//        }
//
//        return p_ids;
//    }



    /**
     */
    private List<K> _findIdentity(
                    final String filter,
                    final Object[] params,
                    final String ordering
                    )
    {
        StringBuilder  oqlTemp = new StringBuilder();
        oqlTemp.append( _helper.getIdentitySelector() ).append( " " );
        oqlTemp.append( " FROM " + _objectTypeName + " o " );

        if (filter != null) {
            oqlTemp.append( filter );
        }
        if (ordering != null ) {
            oqlTemp.append( ordering );
        }

        String  oql = oqlTemp.toString();
        if(_LOG_.isDebugEnabled()) {
            _LOG_.debug( "OQL statement: " + oql );
            _LOG_.debug( "OQL params: " + Arrays.toString( params ) );
        }

        @SuppressWarnings( "unchecked" )
        List<K>  ids = (List<K>)_jdoExecuteQuery( oql, params );

        return ids;
    }



    // load ////////////////////////////////////////////////////////

    /**
     * TEMPLATE:
     */
    protected void _daoAfterLoad(
                    final T p_object
                    )
    {
        //DEFAULT: do nothing.
        if(_LOG_.isTraceEnabled()) {
            _LOG_.trace( "NOP: " + _daoProfile );
        }
    }



    /**
     */
    private T _load(
                    final K id
                    )
    {
        if (id == null) {
            return null;
        }

        T  obj = _jdoLoad( id );
        if (obj != null) {
            _daoAfterLoad( obj );
        }

        return obj;
    }



    /**
     */
    private final List<T> _loadAll(
                    final List<? extends K> ids
                    )
    {
        List<T>  objs = new ArrayList<T>();
        for (K  id : ids) {
            T  obj = _load( id );
            objs.add( obj );
        }

        return objs;
    }




//    /**
//     * Support method:
//     */
//    protected <L, S extends Persistable<L>>
//    S _load(
//                    final Class<S> type,
//                    final S object
//                    )
//    {
//        CastorDao<L, S>  dao = getForwardingDao( type );
//        S  p_object = dao._loadCorrespondentOf( object );
//
//        return p_object;
//    }



    /**
     */
    private T _loadByUnique(
                    final T object
                    )
    {
        if (! _helper.hasUnique()) {
            return null;
        }

        String  filter = _helper.getUniqueFilter();
        Object[]  params = _asArray( _helper.getUnique( object ) );
        List<K>  ids = _findIdentity( filter, params, null );

        T  obj = null;
        if (ids != null  &&  ids.size() > 0) {
            if (ids.size() > 1) {
                final String  message = "uniqueness integrity violation";
                PersistenceException  ex = new PersistenceException( message );
                if (_LOG_.isErrorEnabled()) {
                    _LOG_.error( message );
                }
                throw ex;
            }

            K  id = ids.iterator().next();
            obj = _load( id );
        }

        return obj;
    }
//    {
//        if (! _helper.hasUnique()) {
//            return null;
//        }
//
//        String  filter = _helper.getUniqueFilter();
//        Object[]  params = _asArray( _helper.getUnique( object ) );
//        if (_LOG.isDebugEnabled()) {
//            _LOG.debug( "filter: " + filter );
//            _LOG.debug( "params: " + Arrays.toString( params ) );
//        }
//
////        Collection<T>  p_objects = _find( filter, params, null, null );
//        Collection<T>  p_objects = _jdoFind( filter, params, null );
//        if (p_objects.size() > 1) {
//            PersistenceException  ex =
//                new PersistenceException( "uniqueness integrity violation" );
//            if (_LOG.isErrorEnabled()) {
//                _LOG.error( ex );
//            }
//
//            throw ex;
//        }
//
//        return ((p_objects.size() == 0) ? null : p_objects.iterator().next());
//    }



    /**
     */
    private T _loadCorrespondent(
                    final T object
                    )
    {
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace( "object: " + object );
        }

        T  p_object = _load( object.getPersistentID() );
        if (p_object == null) {
            p_object = _loadByUnique( object );
        }

        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace( "correspondent: " + p_object );
        }
        return p_object;
    }



    /**
     * Template method:
     * Loads the correspondent object, or creates if it has not persisted.
     *
     * @return
     *  the correspondent object, or null if the object is created.
     */
    protected <L, S extends Persistable<L>>
    S _daoLoadOrCreate(
                    final Class<S> type,
                    final S object
                    )
    {
        CastorDao<L, S>  dao = getForwardingDao( type );
        S  p_object = dao._loadCorrespondent( object );
        if (p_object == null) {
            dao._create( object );
        }

        return p_object;
    }
//    {
//        CastorDao<L, S>  dao = getForwardingDao( type );
//        S  p_object = dao._loadCorrespondent( object );
//        if (p_object == null) {
//            _jdoCreate( object );
////            p_object = object;
//        }
//
//        return p_object;
//    }



    // create //////////////////////////////////////////////////////

    /**
     * TEMPLATE:
     */
    protected void _daoBeforeCreate(
                    final T object
                    )
    {
        //DEFAULT: do nothing.
        if(_LOG_.isTraceEnabled()) {
            _LOG_.trace( "NOP: " + _daoProfile );
        }
    }



    /**
     */
    private K _create(
                    final T object
                    )
    {
        if (_jdoIsPersistent( object )) {
            return object.getPersistentID();
        }

        _daoBeforeCreate( object );
        _jdoCreate( object );
        //throws DuplicateObjectException

        return object.getPersistentID();
    }



    // update //////////////////////////////////////////////////////

    protected <L, S extends Persistable<L>>
    void _update(
                    final Class<S> type,
                    final S object
                    )
    {
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace( "type: " + type.getName() );
            _LOG_.trace( "object: " + object );
        }

        CastorDao<L, S>  dao = getForwardingDao( type );
        dao._update( object );
    }



    /**
     */
    private void _update(
                    final T object
                    )
    {
        if (! TimeStampable.class.isInstance( object )) {
            if (_LOG_.isDebugEnabled()) {
                _LOG_.debug( "NOT TimeStampable: " + object );
            }
            return;
        }
        _daoBeforeUpdate( object );
        _jdoUpdate( object );
    }



    /**
     * TEMPLATE:
     */
    protected void _daoBeforeUpdate(
                    final T object
                    )
    {
        //DEFAULT: do nothing.
        if(_LOG_.isTraceEnabled()) {
            _LOG_.trace( "NOP: " + _daoProfile );
        }
    }



    // sync ////////////////////////////////////////////////////////

    protected <L, S extends Persistable<L>>
    S _sync(
                    final Class<S> type,
                    final S object
                    )
    {
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace( "type: " + type.getName() );
            _LOG_.trace( "object: " + object );
        }

        if (object == null) {
            return null;
        }

        CastorDao<L, S>  dao = getForwardingDao( type );
        S  p_object = dao._sync( object );

        return p_object;
    }



    /**
     * @return
     *  null if the specified object is created.
     *  Otherwise, the persisted object loaded from the store.
     */
    private T _sync(
                    final T object
                    )
    {
        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace( "object: " + object );
        }

        if (_jdoIsPersistent( object )) {
            if (_LOG_.isDebugEnabled()) {
                _LOG_.debug( "already persistent: " + object );
            }
            return null;
        }

        T  p_object = _loadCorrespondent( object );
        if (p_object != null) {
//            _syncProperties( object, p_object );
            _daoBeforeSync( object, p_object );
        }

        if (p_object == null) {
            _create( object );
//            _jdoCreate( object );
        }

        if (_LOG_.isTraceEnabled()) {
            _LOG_.trace( "persistent object: " + p_object );
        }
        return p_object;
    }



    /**
     * TEMPLATE:
     */
    protected void _daoBeforeSync(
                    final T object,
                    final T p_object
                    )
    {
        _syncProperties( object, p_object );
    }



    /**
     * Template method:
     * Copies the properties.
     */
    protected void _syncProperties(
                    final T object,
                    final T p_object
                    )
    {
        BeansUtil.copyPropertiesExcept(
                        p_object,
                        object,
                        new String[] { "persistentID" }
        );
    }



    // remove //////////////////////////////////////////////////////

    /**
     * TEMPLATE:
     */
    protected void _daoBeforeRemove(
                    final T object
                    )
    {
        //DEFAULT: do nothing.
        if(_LOG_.isTraceEnabled()) {
            _LOG_.trace( "NOP: " + _daoProfile );
        }
    }



    private void _remove(
                    final T object
                    )
    {
        //TODO: _loadCorrespondent(object)???
        T  p_object = _load( object.getPersistentID() );
        if (p_object != null) {
            _daoBeforeRemove( p_object );
            _jdoRemove( p_object );
        }
    }



    //**************************************************************
    //  implements Dao
    //**************************************************************

    /**
     */
    protected void _daoAssertOperation(
                    final String operation,
                    final boolean condition,
                    final String errorMessage
                    )
    {
        if(_LOG_.isDebugEnabled()) {
            _LOG_.debug( operation + ": " + _daoProfile );
        }

        if (! condition) {
            throw new IllegalArgumentException( errorMessage );
        }
    }



    public K create(
                    final T object
                    )
    {
        _daoAssertOperation( "create", object != null, "null object" );

        K  id = _create( object );

        return id;
    }



    public void update(
                    final T object
                    )
    {
        _daoAssertOperation( "update", object != null, "null object" );

//        if (!(object instanceof TimeStampable)) {
//            throw new IllegalArgumentException(
//                            "object not TimeStampable: type=" + _objectTypeName );
//        }

        _update( object );
    }



    public void remove(
                    final T object
                    )
    {
        _daoAssertOperation( "remove", object != null, "null object" );

        _remove( object );
    }



    public T sync(
                    final T object
                    )
    {
        _daoAssertOperation( "sync", object != null, "null object" );

        T  p_object = _sync( object );
        return (p_object == null ? object : p_object);
    }



    public final List<T> syncAll(
                    final List<? extends T> objects
                    )
    {
        _daoAssertOperation( "syncAll", objects != null, "null object list" );

        List<T>  p_objects = new ArrayList<T>();
        if (objects.size() > 0) {
            for (T  object : objects) {
                T  p_object = _sync( object );
                p_objects.add( p_object );
            }
        }

        return p_objects;
    }



    public final int count()
    {
        return count( null );
    }



    public int count(
                    final Binding filter
                    )
    {
        _daoAssertOperation( "count", true, "not an error" );

        // NOTE:
        // Castor does NOT return intuitive result for counting query.
        // For example, if the query spans 1:M relation,
        // M-side objects are counted.
        // So, we count the unique identities for the same filter.
        Collection<K>  p_ids = _findIdentity( filter, null, null );

        return p_ids.size();
    }



    public T load(
                    final K id
                    )
    {
        _daoAssertOperation( "load", id != null, "null identity" );

        return _load( id );
    }



    public final List<T> loadAll(
                    final List<? extends K> ids
                    )
    {
        _daoAssertOperation( "loadAll", ids != null, "null identity list" );

        return _loadAll( ids );

        // OLD implementation
//        InBinding  binding = new InBinding( "persistentID", identities );
//        return find( binding );
    }



    public final Collection<T> find()
    {
        return find( null );
    }



    public final Collection<T> find(
                    final Binding filter
                    )
    {
        return find( filter, null, null );
    }



    public Collection<T> find(
                    final Binding filter,
                    final List<? extends Order> ordering,
                    final Limit limit
                    )
    {
        _daoAssertOperation( "find", true, "not an error" );

        return _find( filter, ordering, limit );
    }



    public final Collection<K> findIdentity()
    {
        return findIdentity( null, null, null );
    }



    public final Collection<K> findIdentity(
                    final Binding filter
                    )
    {
        return findIdentity( filter, null, null );
    }



    public Collection<K> findIdentity(
                    final Binding filter,
                    final List<? extends Order> ordering,
                    final Limit limit
                    )
    {
        _daoAssertOperation( "findIdentity", true, "not an error" );

        return _findIdentity( filter, ordering, limit );
    }



    public final List<Object> search(
                    final SearchCriteria criteria
                    )
    {
        _daoAssertOperation( "search", true, "not an error" );

        return _search( criteria );
    }

}
//
