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

import jp.go.aist.six.util.core.xml.castor.CastorXmlMapper;
import jp.go.aist.six.util.xml.XmlException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: SearchXmlMapper.java 540 2013-03-08 08:09:29Z nakamura5akihito@gmail.com $
 */
public class SearchXmlMapper
    extends CastorXmlMapper
{

    /**
     * The Spring application context specification.
     */
    private static final String _SPRING_APP_CONTEXT_
        = "/jp/go/aist/six/util/castor/six-util_spring-app-context.xml";


    /**
     */
    public static SearchXmlMapper newInstance()
    {
        SearchXmlMapper  xmlMapper = null;

        try {
            ApplicationContext  appContext =
                new ClassPathXmlApplicationContext( _SPRING_APP_CONTEXT_ );
                //throws BeansException

            xmlMapper =
                (SearchXmlMapper)appContext.getBean( "searchXmlMapper" );
            //throws BeansException
        } catch (Exception ex) {
            throw new XmlException( ex );
        }

        return xmlMapper;
    }



    /**
     * Constructor.
     */
    public SearchXmlMapper()
    {
    }

}
// SearchXmlMapper
