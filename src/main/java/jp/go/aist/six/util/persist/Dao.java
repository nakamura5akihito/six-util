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

import java.util.Collection;
import java.util.List;
import jp.go.aist.six.util.search.Binding;
import jp.go.aist.six.util.search.Limit;
import jp.go.aist.six.util.search.Order;
import jp.go.aist.six.util.search.SearchCriteria;



/**
 * A prescription of the Data Access Object (DAO) which is responsible
 * for managing objects in a data store.
 * A data store is a persistent storage, generally a database.
 *
 * <p>Each DAO instance supports a specific <em>single</em> type of object.
 * It is mandatory the type implements <code>Persistable</code> interface.
 * That is, the type defines the property for identity of objects.
 * Generally, it is the primary key in the relational database.
 * </p>
 *
 * <p>In addition to the identity, the type may have property(s)
 * whose value is unique among the objects.
 * In some cases, such a property can be used to identify the object.
 * If so, two objects are equivalent if they have the same unique value of that property.
 * This is the same semantics as the Java <code>equals</code> method.
 * </p>
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: Dao.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public interface Dao<K, T extends Persistable<K>>
{

    /**
     * Creates a new object in the data store.
     *
     * @param   object
     *  the object to create.
     * @return
     *  the identity of the created object.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public K create( T object );



    /**
     * Updates the object in the data store.
     * An object that has the same identity with the specified object
     * must exist in the data store.
     *
     * @param   object
     *  the object to update.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public void update( T object );



    /**
     * Removes the object from the data store.
     * If no such object exists, this method returns immediately
     * without any exception.
     *
     * @param   object
     *  the object to remove.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public void remove( T object );



    /**
     * Synchronizes the correspond object in the data store
     * with the specified object.
     * The correspondence is determined by the equivalency relation.
     * <ul>
     *   <li>If the equivalent object exists, it is updated.
     *   </li>
     *   <li>Otherwise, the object is created.
     *   </li>
     * </ul>
     *
     * <p>The returned value may refer to a different Java object.
     * </p>
     *
     * @param   object
     *  the object to synchronize.
     * @return
     *  the object.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public T sync( T object );



    /**
     * Synchronizes the correspond objects in the data store
     * with the specified objects.
     * This may be a short hand to call multiple sync(T).
     *
     * @param   objects
     *  the objects to synchronize.
     * @return
     *  the objects.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public List<T> syncAll( List<? extends T> objects );



    /**
     * Counts the number of all the objects.
     *
     * @return
     *  the number of all the objects.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public int count();



    /**
     * Counts the number of the objects that match the specified filter.
     *
     * @param   filter
     *  the filter.
     * @return
     *  the number of the objects.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public int count( Binding filter );



    /**
     * Returns the object of the specified identity.
     * If no such object exists, this method returns null.
     *
     * @param   id
     *  the identity of the object.
     *  It may be an array in case of a composite identity.
     * @return
     *  the object if exists, or null otherwise.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public T load( K id );



    /**
     * Returns the object of the specified identity list.
     *
     * @param   ids
     *  the identity list of the objects.
     * @return
     *  the entities.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public List<T> loadAll( List<? extends K> ids );



    /**
     * Returns all the objects in the data store.
     *
     * @return
     *  all the objects.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public Collection<T> find();



    /**
     * Searches for the objects that match the specified filter.
     *
     * @param   filter
     *  the filter.
     * @return
     *  the objects.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public Collection<T> find( Binding filter );



    /**
     * Searches for the objects that match the specified filter.
     *
     * @param   filter
     *  the filter.
     * @param   ordering
     *  the ordering of the result objects.
     * @param   limit
     *  the number of objects and offset of the first object.
     * @return
     *  the objects.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public Collection<T> find( Binding filter, List<? extends Order> ordering, Limit limit );



    /**
     * Returns identities of all the objects in the data store.
     *
     * @return
     *  identities of all the objects.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public Collection<K> findIdentity();



    /**
     * Searches for the persistent IDs of objects that match the specified filter.
     *
     * @param   filter
     *  the filter.
     * @return
     *  the persistent IDs.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public Collection<K> findIdentity( Binding filter );



    /**
     * Searches for the persistent IDs of objects that match the specified filter.
     *
     * @param   filter
     *  the filter.
     * @param   ordering
     *  the ordering of the result objects.
     * @param   limit
     *  the number of objects and offset of the first object.
     * @return
     *  the persistent IDs.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public Collection<K> findIdentity( Binding filter, List<? extends Order> ordering, Limit limit );



    /**
     * Searches for the objects that match the specified criteria.
     *
     * @param   criteria
     *  the criteria.
     * @return
     *  the objects.
     * @throws  PersistenceException
     *  when an exceptional condition occurred during the object-persistence processing.
     */
    public List<Object> search( SearchCriteria criteria );

}
//

