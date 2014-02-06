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

import jp.go.aist.six.util.repository.QueryException;
import jp.go.aist.six.util.repository.QueryParams;
import org.mongodb.morphia.query.Query;





/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: QueryBuilder.java 567 2013-04-15 08:49:40Z nakamura5akihito@gmail.com $
 */
public interface QueryBuilder
{

    /**
     * @throws  QueryException
     *  in case of query errors.
     */
    public <T> Query<T> build( Query<T> query, QueryParams params );


//    // paging
//    public Query<T> build(
//                    Query<T> query,
//                    List<? extends Order> orders,
//                    Limit limit
//                    );

}
//

