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

import org.castor.spring.orm.CastorTemplate;
import org.castor.spring.orm.support.CastorDaoSupport;
import org.exolab.castor.jdo.JDOManager;



/**
 * This class extends CastorDaoSupport by supporting ExtendedCastorTemplate.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: ExtendedCastorDaoSupport.java 540 2013-03-08 08:09:29Z nakamura5akihito@gmail.com $
 */
public abstract class ExtendedCastorDaoSupport
extends CastorDaoSupport
{

    /**
     * Constructor.
     */
    public ExtendedCastorDaoSupport()
    {
    }



    /**
     */
    public ExtendedCastorTemplate getExtendedCastorTemplate()
    {
        return ExtendedCastorTemplate.class.cast( getCastorTemplate() );
    }



    //**************************************************************
    //  CastorDaoSupport
    //**************************************************************

    @Override
    protected CastorTemplate createCastorTemplate(
                    final JDOManager jdoManager
                    )
    {
        return new ExtendedCastorTemplate( jdoManager );
    }

}
// ExtendedCastorDaoSupport
