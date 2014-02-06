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
package jp.go.aist.six.util.repository;

import java.io.Serializable;
import java.util.List;



/**
 * A prescription for the low-level persistent data store.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: Datastore.java 567 2013-04-15 08:49:40Z nakamura5akihito@gmail.com $
 */
public interface Datastore
{

    /**
     * Loads the object of the specified identifier.
     * If no such object exists in the storage, this method returns null.
     *
     * @param   type
     *  the type of the object.
     * @param   id
     *  the identifier of the object.
     * @return
     *  the object if exists, or null otherwise.
     * @throws  ObjectNotFoundException
     *  if the object not found.
     * @throws  RepositoryException
     *  in case of repository errors.
     */
    public <T, K extends Serializable>
    T findById( Class<T> type, K id );



    /**
     * Tests if the object of the specified identifier exists.
     *
     * @param   type
     *  the type of the object.
     * @param   id
     *  the identifier of the object.
     * @return
     *  true if the object exists, or false otherwise.
     * @throws  RepositoryException
     *  in case of repository errors.
     */
    public <T, K extends Serializable>
    boolean exists( Class<T> type, K id );



    /**
     * Loads all the objects of the specified type.
     *
     * @param   type
     *  the type of the object.
     * @return
     *  all the objects of the specified type.
     * @throws  RepositoryException
     *  in case of repository errors.
     */
    public <T, K extends Serializable>
    List<T> find( Class<T> type );



//    /**
//     * Loads all the objects of the specified type with the given identifiers.
//     *
//     * @param   type
//     *  the type of the object.
//     * @return
//     *  all the objects of the specified type.
//     */
//    public <T, K extends Serializable>
//    List<T> find( Class<T> type, Iterable<K> ids );



    /**
     * Searches for the objects that match the specified query parameters.
     *
     * @param   type
     *  the type of the object.
     * @param   params
     *  the parameters to select the objects.
     * @return
     *  all the objects of the specified type.
     * @throws  QueryException
     *  in case of query errors.
     * @throws  RepositoryException
     *  in case of repository errors.
     */
    public <T, K extends Serializable>
    List<T> find( Class<T> type, QueryParams params );



    /**
     * Returns identifiers of all the objects of the specified type.
     *
     * @param   type
     *  the type of the object.
     * @return
     *  the identifiers of all the objects.
     * @throws  RepositoryException
     *  in case of repository errors.
     */
    public <T, K extends Serializable>
    List<K> findId( Class<T> type );



    /**
     * Searches for the identifiers of the objects that match the specified query parameters.
     *
     * @param   type
     *  the type of the object.
     * @param   params
     *  the parameters to select the objects.
     * @return
     *  the identifiers of all the objects.
     */
    public <T, K extends Serializable>
    List<K> findId( Class<T> type, QueryParams params );



    /**
     * Counts the number of all the objects of the specified type.
     *
     * @param   type
     *  the type of the object.
     * @return
     *  the number of the objects.
     */
    public <T, K extends Serializable>
    long count( Class<T> type );



    /**
     * Counts the number of the objects that match the specified query parameters.
     *
     * @param   type
     *  the type of the object.
     * @param   params
     *  the parameters to select the objects.
     * @return
     *  the number of the objects.
     * @throws  QueryException
     *  in case of query errors.
     * @throws  RepositoryException
     *  in case of repository errors.
     */
    public <T, K extends Serializable>
    long count( Class<T> type, QueryParams params );



    /**
     * Saves the object, either inserting or updating the existing one.
     *
     * @param   type
     *  the type of the object.
     * @param   object
     *  the object.
     * @return
     *  the identifier of the object.
     * @throws  ObjectIntegrityException
     *  in case of object integrity errors.
     * @throws  RepositoryException
     *  in case of repository errors.
     */
    public <T, K extends Serializable>
    K save( Class<T> type, T object );



    /**
     * Removes the object of the specified identifier.
     * If no such object exists, this method returns immediately without any exception.
     *
     * @param   type
     *  the type of the object.
     * @param   id
     *  the identifier of the object.
     * @throws  ObjectNotFoundException
     *  if the object not found.
     * @throws  RepositoryException
     *  in case of repository errors.
     */
    public <T, K extends Serializable>
    void deleteById( Class<T> type, K id );



    /**
     * Removes all the object of the specified type.
     *
     * @param   type
     *  the type of the object.
     * @throws  RepositoryException
     *  in case of repository errors.
     */
    public <T, K extends Serializable>
    void delete( Class<T> type );

}
//
