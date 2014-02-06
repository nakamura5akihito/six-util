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
 * A data store facade that supports multiple types of objects.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: Datastore.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public interface Datastore
{

    public <K, T extends Persistable<K>>
    K create( Class<T> type, T object );


    public <K, T extends Persistable<K>>
    void update( Class<T> type, T object );


    public <K, T extends Persistable<K>>
    void remove( Class<T> type, T object );


    public <K, T extends Persistable<K>>
    T sync( Class<T> type, T object );


    public <K, T extends Persistable<K>>
    List<T> syncAll( Class<T> type, List<? extends T> objects );


    public <K, T extends Persistable<K>>
    int count( Class<T> type );


    public <K, T extends Persistable<K>>
    int count( Class<T> type, Binding filter );


    public <K, T extends Persistable<K>>
    T load( Class<T> type, K id );


    public <K, T extends Persistable<K>>
    List<T> loadAll( Class<T> type, List<? extends K> ids );


    public <K, T extends Persistable<K>>
    Collection<T> find( Class<T> type );


    public <K, T extends Persistable<K>>
    Collection<T> find( Class<T> type, Binding filter );


    public <K, T extends Persistable<K>>
    Collection<T> find( Class<T> type, Binding filter, List<? extends Order> ordering, Limit limit );


    public <K, T extends Persistable<K>>
    Collection<K> findIdentity( Class<T> type );


    public <K, T extends Persistable<K>>
    Collection<K> findIdentity( Class<T> type, Binding filter );


    public <K, T extends Persistable<K>>
    Collection<K> findIdentity( Class<T> type, Binding filter, List<? extends Order> ordering, Limit limit );


    public <K, T extends Persistable<K>>
    List<Object> search( Class<T> type, SearchCriteria criteria );

}
//
