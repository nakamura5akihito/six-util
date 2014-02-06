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



/**
 * Thrown to indicate that
 * an object could not be retrieved via its identifier.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: ObjectNotFoundException.java 573 2013-04-16 05:48:06Z nakamura5akihito@gmail.com $
 */
public class ObjectNotFoundException
    extends RepositoryException
{

    private String  _typeName;
    private String  _idValue;



    /**
     * Constructs this exception with no detail message.
     */
    public ObjectNotFoundException()
    {
        super();
    }



    /**
     * Constructs this exception with the specified detail message.
     *
     * @param  message
     *   the detail message.
     */
    public ObjectNotFoundException(
                    final String message
                    )
    {
        super( message );
    }



    /**
     * Constructs this exception with the specified cause.
     *
     * @param   cause
     *  the cause.
     */
    public ObjectNotFoundException(
                    final Throwable cause
                    )
    {
        super( cause );
    }



    /**
     * Constructs this exception with the specified detail message and cause.
     *
     * @param   message
     *  the detail message.
     * @param   cause
     *  the cause.
     */
    public ObjectNotFoundException(
                    final String message,
                    final Throwable cause
                    )
    {
        super( message, cause );
    }



    /**
     * @param type
     *  the type of object.
     */
    public void setType(
                    final Class<?> type
                    )
    {
        setTypeName( (type == null ? "unknown" : type.getName()) );
    }


    /**
     * @param type_name
     *  the type name of object.
     */
    public void setTypeName(
                    final String type_name
                    )
    {
        _typeName = (type_name == null ? "unknown" : type_name);
    }


    /**
     * @return
     *  the type name of object.
     */
    public String getTypeName()
    {
        return _typeName;
    }



    /**
     * @param id
     *  the identity.
     */
    public void setId(
                    final Object id
                    )
    {
        setIdValue( (id == null ? null : id.toString()) );
    }


    /**
     * @param id_value
     *  the identity in a string representation.
     */
    public void setIdValue(
                    final String id_value
                    )
    {
        _idValue = id_value;
    }


    /**
     * @return
     *  the identity in a string representation.
     */
    public String getIdValue()
    {
        return _idValue;
    }

}
//
