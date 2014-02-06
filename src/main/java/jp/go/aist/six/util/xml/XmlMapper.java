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
package jp.go.aist.six.util.xml;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import javax.xml.transform.Result;
import javax.xml.transform.Source;



/**
 * A prescription of the interface of the XML-Object mapping service.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: XmlMapper.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public interface XmlMapper
{

    /**
     * Marshals the given object as XML to the specified output.
     *
     * @param   obj
     *  the object to marshal.
     * @param   result
     *  the result to marshal to.
     * @throws  XmlException
     *  when an exceptional condition occurred during the XML processing.
     */
    public void marshal( Object obj, Result result );



    /**
     * Marshals the given object as XML to the specified output.
     *
     * @param   obj
     *  the object to marshal.
     * @param   stream
     *  the result to marshal to.
     * @throws  XmlException
     *  when an exceptional condition occurred during the XML processing.
     */
    public void marshal( Object obj, OutputStream stream );



    /**
     * Marshals the given object as XML to the specified output.
     *
     * @param   obj
     *  the object to marshal.
     * @param   writer
     *  the result to marshal to.
     * @throws  XmlException
     *  when an exceptional condition occurred during the XML processing.
     */
    public void marshal( Object obj, Writer writer );



    /**
     * Marshals the given object as XML and returns it as a string.
     *
     * @param   obj
     *  the object to marshal.
     * @return
     *  the marshalled XML as a string.
     * @throws  XmlException
     *  when an exceptional condition occurred during the XML processing.
     */
    public String marshalToString( Object obj );



    /**
     * Unmarshals the given XML source into an object.
     *
     * @param   source
     *  the source to unmarshal from.
     * @return
     *  the object.
     * @throws  XmlException
     *  when an exceptional condition occurred during the XML processing.
     */
    public Object unmarshal( Source source );



    /**
     * Unmarshals the given XML source into an object.
     * This method is same as {{@link #unmarshal(Source)},
     * but provides a measure of type safety.
     *
     * @param   source
     *  the source to unmarshal from.
     * @param   type
     *  the type of the object.
     * @return
     *  the object.
     * @throws  XmlException
     *  when an exceptional condition occurred during the XML processing.
     */
    public <T> T unmarshal( Source source, Class<T> type );



    /**
     * Unmarshals the given XML source into an object.
     *
     * @param   stream
     *  the source to unmarshal from.
     * @return
     *  the object.
     * @throws  XmlException
     *  when an exceptional condition occurred during the XML processing.
     */
    public Object unmarshal( InputStream stream );



    /**
     * Unmarshals the given XML source into an object.
     * This method is same as {{@link #unmarshal(InputStream)},
     * but provides a measure of type safety.
     *
     * @param   stream
     *  the source to unmarshal from.
     * @param   type
     *  the type of the object.
     * @return
     *  the object.
     * @throws  XmlException
     *  when an exceptional condition occurred during the XML processing.
     */
    public <T> T unmarshal( InputStream stream, Class<T> type );



    /**
     * Unmarshals the given XML source into an object.
     *
     * @param   reader
     *  the source to unmarshal from.
     * @return
     *  the object.
     * @throws  XmlException
     *  when an exceptional condition occurred during the XML processing.
     */
    public Object unmarshal( Reader reader );



    /**
     * Unmarshals the given XML source into an object.
     * This method is same as {{@link #unmarshal(Reader)},
     * but provides a measure of type safety.
     *
     * @param   reader
     *  the source to unmarshal from.
     * @param   type
     *  the type of the object.
     * @return
     *  the object.
     * @throws  XmlException
     *  when an exceptional condition occurred during the XML processing.
     */
    public <T> T unmarshal( Reader reader, Class<T> type );



    /**
     * Unmarshals the given XML string into an object.
     *
     * @param   xml
     *  the source XML string to unmarshal from.
     * @return
     *  the object.
     * @throws  XmlException
     *  when an exceptional condition occurred during the XML processing.
     */
    public Object unmarshalFromString( String xml );



    /**
     * Unmarshals the given XML source into an object.
     * This method is same as {{@link #unmarshalFromString(String)},
     * but provides a measure of type safety.
     *
     * @param   xml
     *  the source to unmarshal from.
     * @param   type
     *  the type of the object.
     * @return
     *  the object.
     * @throws  XmlException
     *  when an exceptional condition occurred during the XML processing.
     */
    public <T> T unmarshalFromString( String xml, Class<T> type );

}
//
