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

import jp.go.aist.six.util.persist.AbstractPersistable;
import org.exolab.castor.jdo.TimeStampable;



/**
 * A persistent entity for Castor DAO.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: CastorPersistable.java 540 2013-03-08 08:09:29Z nakamura5akihito@gmail.com $
 */
public abstract class CastorPersistable<K>
    extends AbstractPersistable<K>
    implements TimeStampable
{

    private long  _timestamp;



    /**
     * Constructor.
     */
    public CastorPersistable()
    {
    }


    /**
     * Constructor.
     */
    public CastorPersistable(
                    final K pid
                    )
    {
        super( pid );
    }



    //**************************************************************
    //  TimeStampable
    //**************************************************************

    public void jdoSetTimeStamp(
                    final long timestamp
                    )
    {
        _timestamp = timestamp;
    }


    public long jdoGetTimeStamp()
    {
        return _timestamp;
    }

}
// CastorPersistable
