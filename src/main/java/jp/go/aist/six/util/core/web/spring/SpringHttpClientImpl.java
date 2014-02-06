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
package jp.go.aist.six.util.core.web.spring;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.go.aist.six.util.web.HttpClient;
import jp.go.aist.six.util.web.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;



/**
 * An HTTP method execution utility class.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: SpringHttpClientImpl.java 562 2013-04-12 10:22:39Z nakamura5akihito@gmail.com $
 */
public class SpringHttpClientImpl
    implements HttpClient
{

    /**
     * Logger.
     */
    private static final Logger  _LOG_ = LoggerFactory.getLogger( SpringHttpClientImpl.class );



    public static final List<MediaType>  DEFAULT_ACCEPT_MEDIA_TYPES =
        Arrays.asList( new MediaType[] {
                        MediaType.ALL
//                        MediaType.TEXT_XML, MediaType.APPLICATION_XML
                        } );


    public static final MediaType  DEFAULT_OBJECT_MEDIA_TYPE = MediaType.APPLICATION_XML;

    private MediaType  _object_media_type;


    private List<HttpMessageConverter<?>>  _messageConverters;




    /**
     * Generic HTTP method execution.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    protected <T> T _execute(
                    final String string_url,
                    final HttpMethod method,
                    final RequestCallback callback,
                    final ResponseExtractor<T> extractor
                    )
    {
        _LOG_.debug( "HTTP: method = " + method + ", URL=" + string_url );

        URI  uri = null;
        try {
            URL  url = new URL( string_url );   //throws MalformedURLException
            uri = url.toURI();                  //throws URISyntaxException
        } catch (Exception ex) {
            _LOG_.error( "HTTP client error: " + ex );
            throw new HttpException( ex );
        }

        T  response = null;
        try {
            RestTemplate  rest = new RestTemplate();
            response = rest.execute( uri, method, callback, extractor );
                            //throws RestClientException
        } catch (Exception ex) {
            _LOG_.error( "HTTP client error: " + ex );
            throw new HttpException( ex );
        }

        return response;
    }



    /**
     */
    public void setObjectMediaType(
                    final String media_type
                    )
    {
        _object_media_type = MediaType.valueOf( media_type );
    }


    public MediaType getObjectMediaType()
    {
        return (_object_media_type == null ? DEFAULT_OBJECT_MEDIA_TYPE : _object_media_type);
    }



//    /**
//     */
//    private List<MediaType> _toMediaTypeObjects(
//                    final String[] media_types
//                    )
//    {
//        if (media_types == null  ||  media_types.length == 0) {
//            return DEFAULT_ACCEPT_MEDIA_TYPES;
//        }
//
//        List<MediaType>  list = new ArrayList<MediaType>();
//        for (String  media_type : media_types) {
//            list.add( MediaType.valueOf( media_type ));
//        }
//
//        return list;
//    }



    /**
     */
    public void setMessageConverters(
                    final List<HttpMessageConverter<?>> converters
                    )
    {
        _messageConverters = new ArrayList<HttpMessageConverter<?>>( converters );
    }


    public List<HttpMessageConverter<?>> getMessageConverters()
    {
        return _messageConverters;
    }



    /**
     * scope="prototype";
     * i.e. each time this method is called, a new instance is created.
     */
    private RestTemplate _newRestTemplate()
    {
        RestTemplate  template = new RestTemplate();

        List<HttpMessageConverter<?>>  converters = getMessageConverters();
        if (converters != null) {
            template.setMessageConverters( getMessageConverters() );
        }

        return template;
    }



    /**
     */
    private String _toUrl(
                    final String base_url,
                    final String path,
                    final String params
                    )
    {
        final String  path_delim = "/";
        final String  param_delim = "?";

        StringBuilder  s = new StringBuilder( base_url );

        if (path != null) {
            if (! base_url.endsWith( path_delim )) {
                s.append( path_delim );
            }
            s.append( path );
        }

        if (params != null) {
            if (! params.startsWith( param_delim )) {
                s.append( param_delim );
            }
            s.append( params );
        }

        return s.toString();
    }



    //*********************************************************************
    //  implements HttpClient
    //*********************************************************************

    //=====================================================================
    //  HTTP GET
    //=====================================================================

    public <T> T getObject(
                    final String url,
                    final Class<T> response_type,
                    final Object... uri_variables
                    )
    {
        _LOG_.debug( "HTTP GET: URL=" + url
                        + ", response type=" + response_type
                        + ", variables=" + Arrays.toString( uri_variables ) );

        HttpHeaders  request_headers = new HttpHeaders();
        request_headers.setContentType( getObjectMediaType() );
        HttpEntity<?> request_entity = new HttpEntity<Void>( request_headers );

        HttpEntity<T>  response = null;
        try {
            response = _newRestTemplate().exchange( url, HttpMethod.GET,
                            request_entity, response_type, uri_variables );
            //throws RestClientException
        } catch (Exception ex) {
            throw new HttpException( ex );
        }

        T  body = response.getBody();

        return body;
    }



    /**
     * HTTP GET: Receives the contents from the specified URL and writes them to the given stream.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public void getToWrite(
                    final String url,
                    final OutputStream output
                    )
    {
        getToWrite( url, output, (String[])null );
    }



    /**
     * HTTP GET: Receives the contents from the specified URL and writes them to the given stream.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public void getToWrite(
                    final String url,
                    final OutputStream output,
                    final String... accept_media_types
                    )
    {
        _execute( url, HttpMethod.GET,
                        new AcceptHeaderRequestCallback( accept_media_types ),
                        new OutputStreamResponseExtractor( output ) );
    }



    /**
     * HTTP GET: Receives the contents from the specified URL and writes them to the given stream.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public void getToWrite(
                    final String url,
                    final Writer output
                    )
    {
        getToWrite( url, output, (String[])null );
    }



    /**
     * HTTP GET: Receives the contents from the specified URL and writes them to the given stream.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public void getToWrite(
                    final String url,
                    final Writer output,
                    final String... accept_media_types )
    {
        _execute( url, HttpMethod.GET,
                        new AcceptHeaderRequestCallback( accept_media_types ),
                        new WriterResponseExtractor( output ) );
    }




    //=====================================================================
    //  HTTP POST
    //=====================================================================

    /**
     * HTTP POST
     *
     * @return
     *  the location, as an URI, where the resource is created.
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public <T> String postObject(
                    final String url,
                    final T object,
                    final Class<T> type
                    )
    {
        _LOG_.debug( "HTTP POST: URL=" + url
                        + ", request type=" + type );

        HttpHeaders  request_headers = new HttpHeaders();
        request_headers.setContentType( getObjectMediaType() );
        HttpEntity<T> request_entity = new HttpEntity<T>( object, request_headers );

        URI  location = null;
        try {
            location= _newRestTemplate().postForLocation( url, request_entity );
            //throws RestClientException
        } catch (Exception ex) {
            throw new HttpException( ex );
        }

        return location.toString();
    }



    /**
     * HTTP POST: Reads the contents from the specified stream and sends them to the URL.
     *
     * @return
     *  the location, as an URI, where the resource is created.
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public String postByRead(
                    final String url,
                    final InputStream input,
                    final String media_type
                    )
    {
        InputStreamRequestCallback  callback =
                        new InputStreamRequestCallback( input, MediaType.valueOf( media_type ) );
        String  location = _execute( url, HttpMethod.POST,
                        callback,
                        new LocationHeaderResponseExtractor() );

        return location;
    }



    /**
     * HTTP POST: Reads the contents from the specified reader and sends them to the URL.
     *
     * @return
     *  the location, as an URI, where the resource is created.
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public String postByRead(
                    final String url,
                    final Reader input,
                    final String media_type
                    )
    {
        String  location = _execute( url, HttpMethod.POST,
                        new ReaderRequestCallback( input, MediaType.parseMediaType( media_type ) ),
                        new LocationHeaderResponseExtractor() );

        return location;
    }





    ///////////////////////////////////////////////////////////////////////
    //  nested classes
    ///////////////////////////////////////////////////////////////////////

    /**
     * A simple callback for the Spring RestTemplate.
     */
    private static class AcceptHeaderRequestCallback
    implements RequestCallback
    {

        private final List<MediaType>  _acceptMediaTypes;


        /**
         * @throws  HttpException
         */
        private AcceptHeaderRequestCallback(
                        final String[] accept_media_types
                        )
        {
            List<MediaType>  list = null;

            if (accept_media_types == null  ||  accept_media_types.length == 0) {
                list = DEFAULT_ACCEPT_MEDIA_TYPES;
            } else {
                list = new ArrayList<MediaType>();
                for (String  media_type : accept_media_types) {
                    MediaType  mt = null;
                    try {
                        mt = MediaType.parseMediaType( media_type );
                    } catch (Exception ex) {
                        _LOG_.error( "HTTP client error: " + ex );
                        throw new HttpException( ex );
                    }

                    list.add( mt );
                }
            }

            _acceptMediaTypes = list;
        }


        private AcceptHeaderRequestCallback(
                        final List<MediaType> accept_media_types
                        )
        {
            if (accept_media_types == null  ||  accept_media_types.size() == 0) {
                _acceptMediaTypes = DEFAULT_ACCEPT_MEDIA_TYPES;
            } else {
                _acceptMediaTypes = new ArrayList<MediaType>( accept_media_types );
            }
        }



        public void doWithRequest(
                        final ClientHttpRequest request
                        )
        throws IOException
        {
            request.getHeaders().setAccept( _acceptMediaTypes );
        }
    }
    //



    /**
     */
    private static class LocationHeaderResponseExtractor
    implements ResponseExtractor<String>
    {

        public LocationHeaderResponseExtractor()
        {
        }


        public String extractData(
                        final ClientHttpResponse response
                        )
        throws IOException
        {
            HttpHeaders  headers = response.getHeaders();
            String  location = headers.getLocation().toString();

            return location;
        }
    }
    //

}
//

