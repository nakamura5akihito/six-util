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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * An XML transform processor.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: XmlTransformer.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class XmlTransformer
{

    /**
     * Logger.
     */
    private static final Logger  _LOG_ = LoggerFactory.getLogger( XmlTransformer.class );



    /**
     * The stylesheet location.
     */
    private URL  _stylesheet;



    /**
     * The XSLT parameters that was explicitly set.
     */
    private final Map<String, Object>  _params = new HashMap<String, Object>();



    /**
     * The runtime representation of the stylesheet.
     */
    private Templates  _templates;



    /**
     * Constructs an XmlTransformer without an XSLT stylesheet.
     * That is, this transformer copies the source to the result,
     * i.e. identity transform.
     */
    public XmlTransformer()
    {
        this( (URL)null );
    }



    /**
     * Constructs an XmlTransformer with the specified XSLT stylesheet.
     *
     * @param   stylesheet
     *  the XSLT stylesheet file.
     * @throws  MalformedURLException
     *  if the error occurs while converting the filepath to an URL.
     */
    public XmlTransformer(
                    final File stylesheet
                    )
        throws MalformedURLException
    {
        this( stylesheet.toURI().toURL() );
                                 //throws MalformedURLException
    }



    /**
     * Constructs an XmlTransformer with the specified XSLT stylesheet.
     *
     * @param   stylesheet
     *  the location of XSLT stylesheet.
     */
    public XmlTransformer(
                    final URL stylesheet
                    )
    {
        _stylesheet = stylesheet;
        _LOG_.debug( "stylesheet: " + _stylesheet );
    }



    /**
     * Constructs an XmlTransformer with the specified XSLT stylesheet.
     *
     * @param   stylesheet
     *  the location of XSLT stylesheet.
     */
    public XmlTransformer(
                    final String stylesheet
                    )
    {
        if (stylesheet != null) {
            try {
                if (stylesheet.startsWith( "http:" )
                                ||  stylesheet.startsWith( "https:" )
                                ||  stylesheet.startsWith( "ftp:" )) {
                    _stylesheet = new URL( stylesheet );
                } else {
                    _stylesheet = (new File( stylesheet )).toURI().toURL();
                }
            } catch (MalformedURLException ex) {
                throw new XmlException( ex );
            }
            _LOG_.debug( "stylesheet: " + _stylesheet );
        }
    }




    /**
     * Adds a parameter for the transformation.
     *
     * @param   name
     *  the name of the parameter, which may begin with
     *  a namespace URI in curly braces {}.
     * @param   value
     *  the parameter value.
     */
    public void setParameter(
                    final String name,
                    final Object value
                    )
    {
        _params.put( name, value );
    }



    /**
     * Gets a parameter that was explicitly set.
     *
     * @param   name
     *  the name of the parameter, which may begin with
     *  a namespace URI in curly braces {}.
     * @return
     *  the parameter value.
     */
    public Object getParameter(
                    final String name
                    )
    {
        return _params.get( name );
    }



    /**
     * Transforms the source XML to the result.
     * If no stylesheet was specified, the result is
     * identical with the source.
     *
     * @param   source
     *  the XML input to transform.
     * @param   result
     *  the result of transforming the source.
     * @throws  XmlException
     *  if the transformation fails.
     */
    public void transform(
                    final Source source,
                    final Result result
                    )
    {
        Transformer  transformer = _newTransformer();
                                   //throws XmlException

        try {
            transformer.transform( source, result );
                        //throws TransformerException
        } catch (Exception ex) {
            throw new XmlException( ex );
        }
    }



    /**
     * Transforms the source XML to the result.
     * If no stylesheet was specified, the result is
     * identical with the source.
     *
     * @param   is
     *  the stream which the source XML is read from.
     * @param   os
     *  the stream which the transformed result is write to.
     * @throws  XmlException
     *  if the transformation fails.
     */
    public void transform(
                    final InputStream is,
                    final OutputStream os
                    )
    {
        transform( new StreamSource( is ), new StreamResult( os ) );
    }



//    /**
//     * Writes the contents read from the input stream
//     * to the output stream.
//     *
//     * @param   is
//     *  the stream which the source contents are read from.
//     * @param   os
//     *  the stream which the contents are write to.
//     * @deprecated
//     */
//    @Deprecated
//    protected void _writeRawXml( final InputStream is, final OutputStream os )
//        throws IOException
//    {
//        while (true) {
//            int  c = is.read();
//                        //throws IOException
//            if (c == -1) {
//                break;
//            }
//
//            os.write( c );
//               //throws IOException
//        }
//    }
//
//
//
//    /**
//     * Writes the transformed result to the output stream.
//     * The source XML is read from the given input stream.
//     *
//     * Since a JAXP Transformer object may not be used in multiple threads,
//     * an instance sould be created each time.
//     *
//     * @param   is
//     *  the stream which the source XML is read from.
//     * @param   os
//     *  the stream which the transformed result is write to.
//     * @throws  TransformerException
//     *  if the creation of a Transformer object or transformation fails.
//     * @throws  IOException
//     *  if an IO error occurs to the stylesheet.
//     * @see javax.xml.transform.Transformer
//     * @deprecated
//     */
//    @Deprecated
//    protected void _writeStyledXml( final InputStream is, final OutputStream os )
//        throws TransformerException, IOException
//    {
//        Transformer  transformer = _newTransformer();
//                                   //throws TransformerConfigurationException, IOException
//        transformer.transform( new StreamSource( is ), new StreamResult( os ) );
//                    //throws TransformerException
//    }



    /**
     * Creates a new JAXP Transformer.
     * If there are parameters specified,
     * they are passed to the Transformer.
     *
     * @return
     *  a new JAXP Transformer.
     * @throws  XmlException
     *  if the creation of a Transformer object fails.
     */
    protected Transformer _newTransformer()
    {
        Transformer  transformer = null;

        if (_stylesheet == null) {
            try {
                transformer = TransformerFactory.newInstance().newTransformer();
            } catch (Exception ex) {
                throw new XmlException( ex );
            }

            return transformer;
        }

        if (_templates == null) {
            _templates = _getTemplates( _stylesheet );
        }

        try {
            transformer = _templates.newTransformer();
                                     //throws TransformerConfigurationException

            if (_params.size() > 0) {
                for (String  name : _params.keySet()) {
                    Object  value = _params.get( name );
                    transformer.setParameter( name, value );
                }
            }
        } catch (Exception ex) {
            throw new XmlException( ex );
        }

        return transformer;
    }



    /**
     * A cache of the compiled XSLT stylesheets.
     * The keys are URLs of the XSLT stylesheets and
     * the values are Templates objects.
     */
    private static Map<URL, Templates>  _templatesCache = new HashMap<URL, Templates>();



    /**
     * Returns a Templates object, i.e. a compiled XSLT stylesheet
     * of the specified URL.
     * Once the stylesheet is loaded, it is cached for the next time.
     * That is, firstly, the cache is searched for the URL.
     *
     * @param   stylesheet
     *  the URL of the stylesheet.
     * @return
     *  a Templates object.
     * @throws  XmlException
     *  if the loading of the XSLT stylesheet fails.
     */
    protected static Templates _getTemplates(
                    final URL stylesheet
                    )
    {
        Templates  templates = _templatesCache.get( stylesheet );

        if (templates == null) {
            InputStream  is = null;
            try {
                is = stylesheet.openStream();
                                //throws IOException
                templates =
                    TransformerFactory.newInstance().newTemplates( new StreamSource( is ) );
                                                  //throws TransformerConfigurationException
            } catch (Exception ex) {
                throw new XmlException( ex );
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException io_ex) {
                    // not a fatal error
                    _LOG_.warn( io_ex.toString() );
                }
            }

            _templatesCache.put( stylesheet, templates );
        }

        return templates;
    }

}
//XmlTransformer
