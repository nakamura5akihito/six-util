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

import java.util.Set;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.converters.DefaultConverters;
import org.mongodb.morphia.converters.TypeConverter;



/**
 * A factory for Morphia main object.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: MorphiaFactory.java 523 2013-03-08 06:26:46Z nakamura5akihito@gmail.com $
 */
public class MorphiaFactory
{

    /**
     * Construct a new instance.
     *
     * @return
     *  Morphia main object.
     */
    public static Morphia create()
    {
        return create( null );
    }



    /**
     * Construct a new instance with the classes to map.
     *
     * @param   classesToMap
     *  the classes to map
     * @return
     *  Morphia main object.
     */
    @SuppressWarnings( "rawtypes" )
    public static Morphia create(
                    final Set<Class> classesToMap
                    )
    {
        return create( classesToMap, null );
    }



    /**
     * Construct a new instance with the classes to map and converters.
     *
     * @param   classesToMap
     *  the classes to map
     * @param   converters
     *  the converters.
     * @return
     *  Morphia main object.
     */
	@SuppressWarnings( "rawtypes" )
    public static Morphia create(
	                final Set<Class> classesToMap,
	                final Set<Class<? extends TypeConverter>> converters
	                )
	{
        Morphia  morphia = (classesToMap == null
                        ? (new Morphia()) : (new Morphia( classesToMap )));

        if (converters != null  &&  converters.size() > 0) {
            DefaultConverters  defaultConverters = morphia.getMapper().getConverters();
            for (Class<? extends TypeConverter>  converter : converters) {
                defaultConverters.addConverter( converter );
            }
        }

        return morphia;
	}

}
// MorphiaFactory
