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



/**
 * A base class for persistable objects.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: AbstractPersistable.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public abstract class AbstractPersistable<K>
    implements Persistable<K>
{

	/**
	 *
	 */
	static final long serialVersionUID = -6079495317624421544L;



    /**
     * The identifier.
     */
    private K  _persistentID;



	/**
	 * Constructor.
	 */
    public AbstractPersistable()
    {
    }



    /**
     * Constructor.
     */
    public AbstractPersistable(
                    final K pid
                    )
    {
        setPersistentID( pid );
    }



    //**************************************************************
    //  Persistable
    //**************************************************************

    public void setPersistentID(
                    final K pid
                    )
    {
        _persistentID = pid;
    }


    public K getPersistentID()
    {
        return _persistentID;
    }

}
// AbstractPersistable
