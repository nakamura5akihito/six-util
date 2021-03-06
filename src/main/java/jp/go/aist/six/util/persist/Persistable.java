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

import java.io.Serializable;



/**
 * A marker interface of persistent objects.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: Persistable.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 * @param   <K> the type of persistent ID.
 */
public interface Persistable<K>
    extends Serializable
{

    /**
     * Sets the identifier.
     *
     * @param pid
     *  the identifier of this entity.
     */
    public void setPersistentID( K pid );



    /**
     * Returns the identifier.
     *
     * @return
     *  the identifier of this entity.
     */
    public K getPersistentID();

}
// Persistable
