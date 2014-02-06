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

import jp.go.aist.six.util.search.Relation;
import org.exolab.castor.mapping.GeneralizedFieldHandler;



/**
 * A Castor FieldHandler for the Relation type.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: RelationHandler.java 540 2013-03-08 08:09:29Z nakamura5akihito@gmail.com $
 */
public class RelationHandler
    extends GeneralizedFieldHandler
{

    public RelationHandler()
    {
        super();
    }


    @Override
    public Object convertUponGet( final Object value )
    {
        if (value == null) return null;
        Relation  type = (Relation)value;
        return type.name();
    }



    @Override
    public Object convertUponSet( final Object value )
    {
        return Relation.fromValue( (String)value );
    }



    @Override
    public Class<Relation> getFieldType()
    {
        return Relation.class;
    }


    @Override
    public Object newInstance( final Object parent )
        throws IllegalStateException
    {
        //-- Since it's marked as a string...just return null,
        //-- it's not needed.
        return null;
    }

}
// RelationHandler
