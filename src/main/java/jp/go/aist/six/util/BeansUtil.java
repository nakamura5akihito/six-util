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
package jp.go.aist.six.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * An utility functions for Java Beans.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: BeansUtil.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class BeansUtil
{

    /**
     * Logger.
     */
    private static final Logger  _LOG_ = LoggerFactory.getLogger( BeansUtil.class );



    /**
     * Constructor.
     */
    protected BeansUtil()
    {
    }



    /**
     * Returns the property value of the specified object.
     * If the object has no such property, it is notified with an exception.
     *
     * @param   src
     *  the object.
     * @param   propertyName
     *  the property name.
     * @return
     *  the property value.
     * @throws  ObjectTypeException
     *  if the object has no such property.
     */
    public static Object getProperty(
                    final Object src,
                    final String propertyName
                    )
    {
        if (src == null) {
            throw new IllegalArgumentException( "no source object specified" );
        }

        if (propertyName == null) {
            throw new IllegalArgumentException( "no property specified" );
        }

        Class<?>  clazz = src.getClass();
        PropertyAccessor  accessor = _findAccessor( clazz, propertyName );
        if (accessor == null) {
            throw new IllegalStateException(
                            "getter not found: class=" + clazz.getName()
                            + ", property=" + propertyName );
        }

        return accessor.getProperty( src );
    }



    /**
     * Sets the property value of the specified object.
     * If the object has no such property, it is notified with an exception.
     *
     * @param   dst
     *  the object.
     * @param   propertyName
     *  the property name.
     * @param   value
     *  the property value.
     * @throws  ObjectTypeException
     *  if the object has no such property.
     */
    public static void setProperty(
                    final Object dst,
                    final String propertyName,
                    final Object value
                    )
    {
        if (dst == null) {
            throw new IllegalArgumentException( "no destination object specified" );
        }
        if (propertyName == null) {
            throw new IllegalArgumentException( "no property specified" );
        }

        Class<?>  clazz = dst.getClass();
        PropertyAccessor  accessor = _findAccessor( clazz, propertyName );
                             //throws ObjectTypeException
        if (accessor == null) {
            throw new IllegalStateException(
                            "setter not found: class=" + clazz.getName()
                            + ", property=" + propertyName );
        }

        accessor.setProperty( dst, value );
    }



    /**
     * Tests if the property value of the specified object can be got.
     *
     * @return
     *  true if the property value can be got.
     */
    public static boolean isPropertyGettable(
                    final Object src,
                    final String propertyName
                    )
    {
        if (src == null) {
            throw new IllegalArgumentException( "no source object specified" );
        }

        return isPropertyGettable( src.getClass(), propertyName );
    }



    /**
     * Tests if the property value of the specified class can be got.
     *
     * @return
     *  true if the property value can be got.
     */
    public static boolean isPropertyGettable(
                    final Class<?> clazz,
                    final String propertyName
                    )
    {
        if (clazz == null) {
            throw new IllegalArgumentException( "no class specified" );
        }

        if (propertyName == null) {
            throw new IllegalArgumentException( "no property specified" );
        }

        PropertyAccessor  accessor = _findAccessor( clazz, propertyName );

        return (accessor == null ? false : accessor.isGettable());
    }



    /**
     * Tests if the property value of the specified object can be set.
     *
     * @return
     *  true if the property value can be set.
     */
    public static boolean isPropertySettable(
                    final Object dst,
                    final String propertyName
                    )
    {
        if (dst == null) {
            throw new IllegalArgumentException( "no destination object specified" );
        }

        return isPropertySettable( dst.getClass(), propertyName );
    }



    /**
     * Tests if the property value of the specified class can be set.
     *
     * @return
     *  true if the property value can be set.
     */
    public static boolean isPropertySettable(
                    final Class<?> clazz,
                    final String propertyName
                    )
    {
        if (clazz == null) {
            throw new IllegalArgumentException( "no class specified" );
        }

        if (propertyName == null) {
            throw new IllegalArgumentException( "no property specified" );
        }

        PropertyAccessor  accessor = _findAccessor( clazz, propertyName );

        return (accessor == null ? false : accessor.isSettable());
    }



    /**
     * Returns all the property names of the specified object.
     *
     * @return
     *  the property names.
     */
    public static Collection<String> getPropertyNames(
                    final Object object
                    )
    {
        if (object == null) {
            throw new IllegalArgumentException( "no object specified" );
        }

        return getPropertyNames( object.getClass() );
    }



    /**
     * Returns all the property names of the specified class.
     *
     * @return
     *  the property names.
     */
    public static Collection<String> getPropertyNames(
                    final Class<?> clazz
                    )
    {
        if (clazz == null) {
            throw new IllegalArgumentException( "no class specified" );
        }

        Map<String, PropertyAccessor>  accessors = _getAccessors( clazz );

        return accessors.keySet();
    }



    /**
     * Copies the property from the object to the other object.
     *
     * @param   dst
     *  the destination object to which the property value is set.
     * @param   src
     *  the source object from which the property value is got.
     * @param   propertyName
     *  the property name.
     * @return
     *  the number of properties copied; zero or one.
     */
    public static int copyProperty(
                    final Object dst,
                    final Object src,
                    final String propertyName
                    )
    {
        int  count = 0;
        if (isPropertyGettable( src, propertyName )
                        &&  isPropertySettable( dst, propertyName )) {
            Object  value = getProperty( src, propertyName );
            setProperty( dst, propertyName, value );
            count++;
        }

        return count;
    }



    /**
     * Copies the properties from the object to the other object.
     *
     * @param   dst
     *  the destination object to which the property value is set.
     * @param   src
     *  the source object from which the property value is got.
     * @param   properties
     *  the names of the properties to be copied.
     * @return
     *  the number of properties copied.
     */
    public static int copyProperties(
                    final Object dst,
                    final Object src,
                    final String[] properties
                    )
    {
        if (properties == null) {
            throw new IllegalArgumentException( "no property list specified" );
        }

        int  count = 0;
        for (String  p : properties) {
            if (p == null) {
                continue;
            }

            count += copyProperty( dst, src, p );
        }

        return count;
    }



    /**
     * Copies the properties from the object to the other object.
     *
     * @param   dst
     *  the destination object to which the property value is set.
     * @param   src
     *  the source object from which the property value is got.
     * @param   properties
     *  the names of the properties that should not be copied.
     * @return
     *  the number of properties copied.
     */
    public static int copyPropertiesExcept(
                    final Object dst,
                    final Object src,
                    final String[] properties
                    )
    {
        if (dst == null) {
            throw new IllegalArgumentException( "no destination object specified" );
        }

        if (src == null) {
            throw new IllegalArgumentException( "no source object specified" );
        }

        Collection<String>  excepts = null;
        if (properties != null  &&  properties.length > 0) {
            excepts = Arrays.asList( properties );
        }

        int  count = 0;
        Collection<String>  names = getPropertyNames( src );
        for (String  name : names) {
            if (excepts != null  &&  excepts.contains( name )) {
                continue;
            }

            count += copyProperty( dst, src, name );
        }

        return count;
    }



    /**
     * Copies all the properties from the source object to the
     * destination object.
     * Note that this method performs "shallow copy" of the properties
     * according to the JavaBean specification.
     * That is, a property is copied if the source has the getter method
     * or the public field,
     * and the destination has the setter method or the public field.
     *
     * @param   dst
     *  the destination object whose properties are modified.
     * @param   src
     *  the source object whose properties are retrieved.
     * @return
     *  the number of properties copied.
     * @throws  IllegalArgumentException
     * @throws  ObjectTypeException
     */
    public static int copyProperties(
                    final Object dst,
                    final Object src
                    )
    {
        return copyPropertiesExcept( dst, src, null );
    }



    /**
     */
    private static final PropertyAccessor _findAccessor(
            final Class<?> clazz,
            final String property
            )
    {
        Map<String, PropertyAccessor>  accessors = _getAccessors( clazz );
                         //throws ObjectTypeException

        return accessors.get( property );    //may null.
    }



//    /**
//     */
//    private static final Accessor _findAccessor(
//            final Object object, final String property )
//        throws ObjectTypeException
//    {
//        return _findAccessor( object.getClass(), property );    //may null.
//    }



    ////////////////////////////////////////////////////////////////
    //  Property Accessor
    ////////////////////////////////////////////////////////////////

    /**
     * Property accessors registry.
     */
    private static final Map<Class<?>, Map<String, PropertyAccessor>>  _ACCESSORS_REG_ =
        new HashMap<Class<?>, Map<String, PropertyAccessor>>();



    /**
     * Returns an accessor list for the specified class.
     * The result list is cached for future use.
     */
    private static Map<String, PropertyAccessor> _getAccessors(
                    final Class<?> clazz
                    )
    {
        Map<String, PropertyAccessor>  accessors = null;

        synchronized (_ACCESSORS_REG_) {
            accessors = _ACCESSORS_REG_.get( clazz );
            if (accessors == null) {
                accessors = PropertyAccessor.findAll( clazz );
                _ACCESSORS_REG_.put( clazz, accessors );
            }
        }

        return accessors;
    }



    /**
     * A property accessor.
     * Type of an accessor is
     * (1) PropertyDescriptor, if getter/setter defined, and/or
     * (2) Field, if public field defined.
     */
    private static final class PropertyAccessor
    {
        private PropertyDescriptor  _desc;
        private Field  _field;
        private String  _propertyName;


        public PropertyAccessor(
                        final PropertyDescriptor desc
                        )
        {
            setPropertyDescriptor( desc );
        }


        public PropertyAccessor(
                        final Field field
                        )
        {
            setField( field );
        }


        public void setPropertyDescriptor(
                        final PropertyDescriptor desc
                        )
        {
            if (desc == null) {
                throw new IllegalArgumentException( "no PropertyDescriptor specified" );
            }
            _desc = desc;
            _propertyName = _desc.getName();
        }


        public void setField(
                        final Field field
                        )
        {
            if (field == null) {
                throw new IllegalArgumentException( "no Field specified" );
            }
            _field = field;
            _propertyName = _field.getName();
        }



//        public String getPropertyName()
//        {
//            return _propertyName;
//        }



        public void setProperty(
                        final Object object,
                        final Object value
                        )
        {
            if (_LOG_.isTraceEnabled()) {
                _LOG_.trace( "property=" + _propertyName + ", value=" + value );
            }

            Member  setter = _findSetter();
            if (setter == null) {
                throw new IllegalStateException(
                                "setter not found: property=" + _propertyName );
            }

            try {
                if (setter instanceof Method) {
                    ((Method)setter).invoke( object, new Object[] { value } );
                                     //throws IllegalAccessException
                                     //throws IllegalArgumentException
                                     //throws InvocationTargetException
                                     //throws NullPointerException

                } else if (setter instanceof Field) {
                    ((Field)setter).set( object, value );
                                    //throws IllegalAccessException
                                    //throws IllegalArgumentException
                                    //throws NullPointerException
                }

            } catch (Exception ex) {
                throw new NestedRuntimeException( ex );
            }
        }



        public Object getProperty(
                        final Object object
                        )
        {
            if (_LOG_.isTraceEnabled()) {
                _LOG_.trace( "property=" + _propertyName );
            }

            Member  getter = _findGetter();
            if (getter == null) {
                throw new IllegalStateException(
                                "getter not found: property=" + _propertyName );
            }

            Object  value = null;
            try {
                if (getter instanceof Method) {
                    value = ((Method)getter).invoke( object, (Object[])null );
                                             //throws IllegalAccessException
                                             //throws IllegalArgumentException
                                             //throws InvocationTargetException
                                             //throws NullPointerException

                } else if (getter instanceof Field) {
                    value = ((Field)getter).get( object );
                                            //throws IllegalAccessException
                                            //throws IllegalArgumentException
                                            //throws NullPointerException
                }
            } catch (Exception ex) {
                throw new NestedRuntimeException( ex );
            }

            if (_LOG_.isTraceEnabled()) {
                _LOG_.trace( "property value got: property=" + _propertyName
                                + ", value=" + value );
            }

            return value;
        }



        public boolean isSettable()
        {
            return (_findSetter() != null);
        }



        public boolean isGettable()
        {
            return (_findGetter() != null);
        }



        private Member _findSetter()
        {
            Member  setter = null;
            if (_desc != null) {
                setter = _desc.getWriteMethod();
            }

            if (setter == null) {
                setter = _field;
            }

            return setter;
        }



        private Member _findGetter()
        {
            Member  getter = null;
            if (_desc != null) {
                getter = _desc.getReadMethod();
            }

            if (getter == null) {
                getter = _field;
            }

            return getter;
        }



        public static Map<String, PropertyAccessor> findAll(
                        final Class<?> clazz
                        )
        {
            if (_LOG_.isTraceEnabled()) {
                _LOG_.trace( "class=" + clazz.getName() );
            }

            Map<String, PropertyAccessor>  accessors = new HashMap<String, PropertyAccessor>();

            // step 1: Collects PropertyDescriptors.
            BeanInfo  bean_info = null;
            try {
                bean_info = Introspector.getBeanInfo( clazz );
                                         //throws IntrospectionException
            } catch (IntrospectionException ex) {
                throw new NestedRuntimeException( ex );
            }

            PropertyDescriptor[]  descriptors = bean_info.getPropertyDescriptors();
                                                          //may return null.
            if (descriptors != null) {
                for (PropertyDescriptor  d : descriptors) {
                    String  propertyName = d.getName();
                    if (propertyName.equals( "class" )) {
                        // "class" property excluded.
                        continue;
                    }

                    accessors.put( propertyName, new PropertyAccessor( d ) );
                    if (_LOG_.isTraceEnabled()) {
                        _LOG_.trace( "PropertyDescriptor found: property name="
                                        + propertyName );
                    }
                }
            }

            // step 2: Collects public Fields except static ones.
            Field[]  fields = clazz.getFields();
                                    //Returns an array of length 0
                                    //if there is no accessible public field.
            if (fields != null) {
                for (Field  f : fields) {
                    if (Modifier.isStatic( f.getModifiers() )) {
                        continue;
                    }

                    String  propertyName = f.getName();
                    PropertyAccessor  accessor = accessors.get( propertyName );
                    if (accessor == null) {
                        accessors.put( propertyName, new PropertyAccessor( f ) );
                    } else {
                        accessor.setField( f );
                    }
                    if (_LOG_.isTraceEnabled()) {
                        _LOG_.trace( "Field found: property name=" + propertyName );
                    }

                }
            }

            return accessors;
        }

    }
    //PropertyAccessor

}
//BeansUtil
