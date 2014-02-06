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
package jp.go.aist.six.util.core.config.spring;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import jp.go.aist.six.util.config.ConfigurationException;
import jp.go.aist.six.util.config.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;



/**
 * Application Context using Spring Framework.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: SpringContext.java 600 2013-06-17 10:19:02Z nakamura5akihito@gmail.com $
 */
public class SpringContext
    implements Context
{

    /**
     * Logger.
     */
    private static final Logger  _LOG_ = LoggerFactory.getLogger( SpringContext.class );



  //TODO: Test this!!! Move this to the super class Context.
 // Note that Environment has NO method to obtain a set of property keys or to iterate properties.
//     @Autowired
//     private Environment  _env;

 //Example code:
     //ApplicationContext ctx = new GenericApplicationContext();
     //Environment env = ctx.getEnvironment();
     //boolean containsFoo = env.containsProperty("foo");


    private String  _configLocation;
    private ApplicationContext  _context;

    private String[]  _configPropertyLocations;
//  private List<String>  _configPropertyBeans;
    private Properties  _configProperties;



    /**
     * Constructor.
     */
    protected SpringContext()
    {
    }


    public SpringContext(
                    final String config_location
                    )
    {
        this( config_location, null );
    }



    public SpringContext(
                    final String config_location,
                    final String[] property_locations
                    )
    {
        _configLocation = config_location;
        _configPropertyLocations = property_locations;

        _LOG_.info( "configuration location: " + _configLocation );
        _LOG_.info( "property locations: " + Arrays.toString( _configPropertyLocations ) );
    }



//    public SpringContext(
//                    final String config_location,
//                    final String[] property_beans
//                    )
//    {
//        _configLocation = config_location;
//
//        if (property_beans == null) {
//            _configPropertyBeans = Collections.emptyList();
//        } else {
//            _configPropertyBeans = Arrays.asList( property_beans );
//        }
//
//        _LOG_.info( "configuration location: " + _configLocation );
//        _LOG_.info( "property beans: " + _configPropertyBeans );
//
////      _env = _getContext().getEnvironment();
////      if (_env != null) {
////          String  key = "six.vuln.datastore.engine";
////          String  value = _env.getProperty( key );
////          _LOG_.debug( "property by Spring Environment: " + key + "=" + value );
////      }
//    }



    /**
     * @throws  ConfigurationException
     */
    protected ApplicationContext _getContext()
    {
        if (_context == null) {
            try {
                _context = new ClassPathXmlApplicationContext( _configLocation );
                           //throws BeansException/runtime
            } catch (BeansException ex) {
                throw new ConfigurationException( ex );
            }
        }

        return _context;
    }




    //*********************************************************************
    //  implements Context
    //*********************************************************************

    public String getProperty(
                    final String key
                    )
    {
        String  value = System.getProperty( key );
        if (value != null) {
            return value;
        }

        value = _getConfigProperties().getProperty( key );

        return value;
    }



    public String getProperty(
                    final String key,
                    final String defaultValue
                    )
    {
        String  value = System.getProperty( key );
        if (value != null) {
            return value;
        }

        value = _getConfigProperties().getProperty( key, defaultValue );

        return value;
    }



    public Set<String> getPropertyKeys()
    {
        Set<String>  keys = new HashSet<String>( _getConfigProperties().stringPropertyNames() );
        keys.addAll( System.getProperties().stringPropertyNames() );

        return keys;
    }



    private Properties _getConfigProperties()
    {
        if (_configProperties == null) {
            _configProperties = new Properties();
            if (_configPropertyLocations != null) {
                for (String  location : _configPropertyLocations) {
                    try {
                        Resource  resource = _getContext().getResource( location );
                        _LOG_.info( "property resource: " + (resource.exists() ? "exist - " : "inexist - ") + resource.getFile() );
                        if (resource.exists()) {
                            _configProperties.load( resource.getInputStream() );
                        }
                    } catch (IOException ex) {
                        _LOG_.warn( "property resource ERROR (skip): " + ex );
                    }

//                _configProperties.list( System.out );
                }
            }
        }

        return _configProperties;
    }
//    private Properties _getConfigProperties()
//    {
//        if (_configProperties == null) {
//            _configProperties = new Properties();
//            for (String  bean : _configPropertyBeans) {
//                Properties  props = null;
//                try {
//                    props = getBean( bean, Properties.class );
//                } catch (Exception ex) {
//                    _LOG_.warn( "failed to find Properties bean: name=" + bean, ex );
//                }
//
//                if (props != null) {
//                    for (String  key : props.stringPropertyNames()) {
//                        _configProperties.setProperty( key, props.getProperty( key ) );
//                    }
//                }
//            }
//        }
//
//        return _configProperties;
//    }



    public <T> T getBean(
                    final Class<T> requiredType
                    )
    {
        T  bean = null;
        try {
            bean = _getContext().getBean( requiredType );
        } catch (BeansException ex) {
            _LOG_.warn( "No such bean: type=" + requiredType );
            throw new ConfigurationException( ex );
        }

        return bean;
    }



    public <T> T getBean(
                    final String name,
                    final Class<T> requiredType
                    )
    {
        T  bean = null;
        try {
            bean = _getContext().getBean( name, requiredType );
        } catch (BeansException ex) {
            _LOG_.warn( "No such bean: name=" + name + ", type=" + requiredType );
            throw new ConfigurationException( ex );
        }

        return bean;
    }



    public boolean containsBean(
                    final String name
                    )
    {
        return _getContext().containsBean( name );
    }

}
//

