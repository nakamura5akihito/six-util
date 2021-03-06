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
package jp.go.aist.six.util.core.xml.castor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import jp.go.aist.six.util.core.xml.spring327.CastorMarshaller;
import jp.go.aist.six.util.xml.XmlException;
import jp.go.aist.six.util.xml.XmlMapper;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
//import org.springframework.oxm.castor.CastorMarshaller;



/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: CastorXmlMapper.java 527 2013-03-08 06:41:36Z nakamura5akihito@gmail.com $
 */
public class CastorXmlMapper
    implements XmlMapper
{

    public static final String  DEFAULT_CHARSET = "UTF-8";



    private Marshaller  _marshaller;
    private Unmarshaller  _unmarshaller;

    private String  _charset = DEFAULT_CHARSET;



    /**
     * Constrcutor.
     */
    public CastorXmlMapper()
    {
    }



    /**
     */
    public void setMarshaller(
                    final Marshaller marshaller
                    )
    {
        _marshaller = marshaller;
    }


    /**
     */
    public void setUnmarshaller(
                    final Unmarshaller unmarshaller
                    )
    {
        _unmarshaller = unmarshaller;
    }



    /**
     */
    public void setCharset(
                    final String charset
                    )
    {
        _charset = charset;
    }



//    /**
//     */
//    private Object _unmarshal(
//                    final InputStream input
//                    )
//    throws Exception
//    {
////      BufferedReader  reader = new BufferedReader( new InputStreamReader( input ) );
//        /*
//         * If we do not specify the character set, "UTF-8",
//         * the Castor Unmarshaller fails to read Japanese characters
//         * that are generated by Windows commands.
//         */
//
//        BufferedReader  reader = new BufferedReader(
//                        new InputStreamReader( input, Charset.forName( "UTF-8" ) ) );
//
//        return _unmarshaller.unmarshal( new StreamSource( reader ) );
//    }



    //**************************************************************
    //  XmlMapper
    //**************************************************************

    @Override
    public void marshal(
                    final Object obj,
                    final Result result
                    )
    {
        try {
            _marshaller.marshal( obj, result );
                        //@throws IOException
                        //@throws XmlMappingException
        } catch (Exception ex) {
            throw new XmlException( ex );
        }
    }



    @Override
    public void marshal(
                    final Object obj,
                    final OutputStream stream
                    )
    {
        if (_marshaller instanceof CastorMarshaller) {
            CastorMarshaller  cmarshaller = CastorMarshaller.class.cast( _marshaller );
            cmarshaller.setEncoding( _charset );
        }

        try {
            Writer  writer = new BufferedWriter(
                            new OutputStreamWriter( stream, _charset ) );
            _marshaller.marshal( obj, new StreamResult( writer ) );
                        //@throws IOException
                        //@throws XmlMappingException
        } catch (Exception ex) {
            throw new XmlException( ex );
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                //ignorable
            }
        }
    }
//    {
//        OutputStream  output = ((stream instanceof BufferedOutputStream)
//                        ? stream
//                        : (new BufferedOutputStream( stream )));
//        try {
//            _marshaller.marshal( obj, new StreamResult( output ) );
//                        //@throws IOException
//                        //@throws XmlMappingException
//        } catch (Exception ex) {
//            throw new XmlException( ex );
//        } finally {
//            try {
//                stream.close();
//            } catch (IOException ex) {
//                //ignorable
//            }
//        }
//    }



    @Override
    public void marshal(
                    final Object obj,
                    final Writer writer
                    )
    {
        Writer  w = ((writer instanceof BufferedWriter)
                        ? writer
                        : (new BufferedWriter( writer )));
        try {
            _marshaller.marshal( obj, new StreamResult( w ) );
                        //@throws IOException
                        //@throws XmlMappingException
        } catch (Exception ex) {
            throw new XmlException( ex );
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                //ignorable
            }
        }
    }



    @Override
    public String marshalToString(
                    final Object obj
                    )
    {
        StringWriter  writer = new StringWriter();
        Result  result = new StreamResult( new BufferedWriter( writer ) );
        marshal( obj, result );
        //@throws XmlException

        return writer.toString();
    }



    @Override
    public Object unmarshal(
                    final Source source
                    )
    {
        Object  obj = null;
        try {
            obj = _unmarshaller.unmarshal( source );
                                //@throws IOException
                                //@throws XmlMappingException
        } catch (Exception ex) {
            throw new XmlException( ex );
        }

        return obj;
    }



    @Override
    public <T> T unmarshal(
                    final Source source,
                    final Class<T> type
                    )
    {
        Object  obj = unmarshal( source );
        return type.cast( obj );
    }



    @Override
    public Object unmarshal(
                    final InputStream stream
                    )
    {
        Object  obj = null;
        try {
            obj = _unmarshaller.unmarshal( new StreamSource(
                            new BufferedReader(
                                            new InputStreamReader( stream, _charset ) ) ) );
                                //@throws IOException
                                //@throws XmlMappingException
        } catch (Exception ex) {
            throw new XmlException( ex );
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                //ignorable
            }
        }

        return obj;
    }



    @Override
    public <T> T unmarshal(
                    final InputStream stream,
                    final Class<T> type
                    )
    {
        Object  obj = unmarshal( stream );
        return type.cast( obj );
    }



    @Override
    public Object unmarshal(
                    final Reader reader
                    )
    {
        Object  obj = null;
        Reader  r = ((reader instanceof BufferedReader)
                        ? reader
                        : (new BufferedReader( reader )));
        try {
            obj = _unmarshaller.unmarshal( new StreamSource( r ) );
                                //@throws IOException
                                //@throws XmlMappingException
        } catch (Exception ex) {
            throw new XmlException( ex );
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                //ignorable
            }
        }

        return obj;
    }



    @Override
    public <T> T unmarshal(
                    final Reader reader,
                    final Class<T> type
                    )
    {
        Object  obj = unmarshal( reader );
        return type.cast( obj );
    }



    @Override
    public Object unmarshalFromString(
                    final String xml
                    )
    {
        Reader  reader = new BufferedReader( new StringReader( xml ) );
        Object  obj = unmarshal( reader );
                      //@throws XmlException

        return obj;
    }



    @Override
    public <T> T unmarshalFromString(
                    final String xml,
                    final Class<T> type
                    )
    {
        Object  obj = unmarshalFromString( xml );
        return type.cast( obj );
    }

}
// CastorXmlMapper
