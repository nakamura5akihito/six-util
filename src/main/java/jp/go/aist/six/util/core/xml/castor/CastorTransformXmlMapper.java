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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import jp.go.aist.six.util.xml.XmlTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: CastorTransformXmlMapper.java 527 2013-03-08 06:41:36Z nakamura5akihito@gmail.com $
 */
public class CastorTransformXmlMapper
    extends CastorXmlMapper
{

    /**
     * Logger.
     */
    private static final Logger  _LOG_ = LoggerFactory.getLogger( CastorTransformXmlMapper.class );



    private XmlTransformer  _transformer;



    /**
     * Constructor.
     */
    public CastorTransformXmlMapper()
    {
        _LOG_.trace( "instantiated" );
    }



    /**
     */
    public void setTransformer(
                    final XmlTransformer transformer
                    )
    {
        _transformer = transformer;
    }



    private String _simpleMarshalToString(
                    final Object obj
                    )
    {
        StringWriter  writer = new StringWriter();
        super.marshal( obj, new StreamResult( writer ) );

        return writer.toString();
    }




    //*********************************************************************
    //  extends CastorXmlMapper
    //*********************************************************************

    @Override
    public void marshal(
                    final Object obj,
                    final Result result
                    )
    {
        if (_transformer == null) {
            super.marshal( obj, result );
        } else {
            String  xml = _simpleMarshalToString( obj );
            Source  source = new StreamSource( new StringReader( xml ) );
            _transformer.transform( source, result );
        }
    }



    @Override
    public void marshal(
                    final Object obj,
                    final OutputStream stream
                    )
    {
        if (_transformer == null) {
            super.marshal( obj, stream );
        } else {
            String  xml = _simpleMarshalToString( obj );
            Source  source = new StreamSource( new StringReader( xml ) );
            Result  result = new StreamResult( stream );
            _transformer.transform( source, result );
        }
    }



    @Override
    public void marshal(
                    final Object obj,
                    final Writer writer
                    )
    {
        if (_transformer == null) {
            super.marshal( obj, writer );
        } else {
            String  xml = _simpleMarshalToString( obj );
            Source  source = new StreamSource( new StringReader( xml ) );
            Result  result = new StreamResult( writer );
            _transformer.transform( source, result );
        }
    }



    @Override
    public String marshalToString(
                    final Object obj
                    )
    {
        StringWriter  writer = new StringWriter();
        marshal( obj, writer );
        String  xml = writer.toString();

        return xml;
    }



    @Override
    public Object unmarshal(
                    final InputStream stream
                    )
    {
        Object  obj = null;

        if (_transformer == null) {
            obj = super.unmarshal( stream );
        } else {
            StringWriter  writer = new StringWriter();
            Result  result = new StreamResult( writer );
            _transformer.transform( new StreamSource( stream ), result );

            String  xml = writer.toString();
//            _LOG_.debug( "transformed XML: \n" + xml );
            obj = super.unmarshal( new StringReader( xml ) );
        }

        return obj;
    }



    @Override
    public Object unmarshal(
                    final Reader reader
                    )
    {
        Object  obj = null;
        if (_transformer == null) {
            obj = super.unmarshal( reader );
        } else {
            StringWriter  writer = new StringWriter();
            Result  result = new StreamResult( writer );
            _transformer.transform( new StreamSource( reader ), result );

            obj = super.unmarshal( new StringReader( writer.toString() ) );
        }

        return obj;
    }



    @Override
    public Object unmarshalFromString(
                    final String xml
                    )
    {
        Reader  reader = new BufferedReader( new StringReader( xml ) );
        Object  obj = unmarshal( reader );

        return obj;
    }

}
//

