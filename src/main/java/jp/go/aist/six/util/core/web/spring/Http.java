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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import jp.go.aist.six.util.web.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;



/**
 * An HTTP method execution utility class.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: Http.java 553 2013-04-12 08:45:25Z nakamura5akihito@gmail.com $
 */
public class Http
{

    /**
     * Logger.
     */
    private static final Logger  _LOG_ = LoggerFactory.getLogger( Http.class );



    public static final List<MediaType>  DEFAULT_ACCEPT_MEDIA_TYPES =
        Arrays.asList( new MediaType[] {
                        MediaType.ALL
//                        MediaType.TEXT_XML, MediaType.APPLICATION_XML
                        } );



    /**
     * Generic HTTP method execution.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    protected static <T> T _execute(
                    final URL from_url,
                    final HttpMethod method,
                    final RequestCallback callback,
                    final ResponseExtractor<T> extractor
                    )
    {
        _LOG_.debug( "HTTP: method = " + method + ", URL=" + from_url );

        URI  from_uri = null;
        try {
            from_uri = from_url.toURI();
                                //throws URISyntaxException
        } catch (URISyntaxException ex) {
            throw new HttpException( ex );
        }

        T  response = null;
        try {
            RestTemplate  rest = new RestTemplate();
            response = rest.execute( from_uri, method, callback, extractor );
        } catch (RestClientException ex) {
            _LOG_.error( "HTTP error: " + ex );
            throw new HttpException( ex );
        }

        return response;
    }



    //=====================================================================
    //  HTTP GET
    //=====================================================================

    /**
     * HTTP GET: Receives the contents from the specified URL and writes them to the given stream.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public static void getTo(
                    final URL from_url,
                    final OutputStream to_stream
                    )
    {
        getTo( from_url, to_stream, DEFAULT_ACCEPT_MEDIA_TYPES );
    }


    /**
     * HTTP GET: Receives the contents from the specified URL and writes them to the given stream.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public static void getTo(
                    final URL from_url,
                    final OutputStream to_stream,
                    final List<MediaType> accept_media_types
                    )
    {
        _LOG_.debug( "HTTP GET: from URL=" + from_url + ", accept=" + accept_media_types );

        AcceptHeaderRequestCallback  callback =
                        new AcceptHeaderRequestCallback( accept_media_types );
        OutputStreamResponseExtractor  extractor = new OutputStreamResponseExtractor( to_stream );
        _execute( from_url, HttpMethod.GET, callback, extractor );
    }



    /**
     * HTTP GET: Receives the contents from the specified URL and writes them to the given writer.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public static void getTo(
                    final URL from_url,
                    final Writer to_writer
                    )
    {
        getTo( from_url, to_writer, DEFAULT_ACCEPT_MEDIA_TYPES );
    }


    /**
     * HTTP GET: Receives the contents from the specified URL and writes them to the given writer.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public static void getTo(
                    final URL from_url,
                    final Writer to_writer,
                    final List<MediaType> accept_media_types
                    )
    {
        _LOG_.debug( "HTTP GET: from URL=" + from_url + ", accept=" + accept_media_types );

        AcceptHeaderRequestCallback  callback =
                        new AcceptHeaderRequestCallback( accept_media_types );
        WriterResponseExtractor  extractor = new WriterResponseExtractor( to_writer );
        _execute( from_url, HttpMethod.GET, callback, extractor );
    }



//    /**
//     * GET
//     */
//    public void getTo(
//                    final URL from_url,
//                    final File to_file
//                    )
//    {
//        getTo( from_url, to_file, _DEFAULT_ACCEPT_MEDIA_TYPES_ );
//    }
//
//
//
//    /**
//     * GET
//     */
//    public void getTo(
//                    final URL from_url,
//                    final File to_file,
//                    final List<MediaType> accept_media_types
//                    )
//    {
//        _LOG_.debug( "HTTP GET: from URL=" + from_url + ", to file=" + to_file );
//
//        URI  from_uri = null;
//        try {
//            from_uri = from_url.toURI();
//                           //throws URISyntaxException
//        } catch (URISyntaxException ex) {
//            throw new HttpException( ex );
//        }
//
//        RestTemplate  rest = _newRestTemplate();
//        try {
//            FileResponseExtractor  extractor = new FileResponseExtractor( to_file );
//            AcceptHeaderRequestCallback  callback =
//                            new AcceptHeaderRequestCallback( accept_media_types );
//            rest.execute( from_uri, HttpMethod.GET, callback, extractor );
//        } catch (RestClientException ex) {
//            _LOG_.error( "HTTP GET error: " + ex );
//            throw new HttpException( ex );
//        }
//    }



    //=====================================================================
    //  HTTP POST
    //=====================================================================

    /**
     * HTTP POST: Reads the contents from the specified stream and sends them to the URL.
     *
     * @return
     *  the location, as an URI,  where the resource is created.
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public static String postFrom(
                    final URL to_url,
                    final InputStream from_stream,
                    final MediaType media_type
                    )
    {
        InputStreamRequestCallback  callback =
                        new InputStreamRequestCallback( from_stream, media_type );
        String  location = _execute( to_url, HttpMethod.POST, callback,
                        new LocationHeaderResponseExtractor() );

        return location;
    }



    /**
     * HTTP POST: Reads the contents from the specified reader and sends them to the URL.
     *
     * @return
     *  the location, as an URI,  where the resource is created.
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public static String postFrom(
                    final URL to_url,
                    final Reader from_reader,
                    final MediaType media_type
                    )
    {
        ReaderRequestCallback  callback =
                        new ReaderRequestCallback( from_reader, media_type );
        String  location = _execute( to_url, HttpMethod.POST, callback,
                        new LocationHeaderResponseExtractor() );

        return location;
    }



//    /**
//     * POST
//     */
//    public void postFrom(
//                    final URL to_url,
//                    final File from_file,
//                    final MediaType media_type
//                    )
//    {
//        _LOG_.debug( "HTTP POST: to URL=" + to_url + ", from file=" + from_file );
//
//        URI  to_uri = null;
//        try {
//            to_uri = to_url.toURI();
//                            //throws URISyntaxException
//        } catch (URISyntaxException ex) {
//            throw new HttpException( ex );
//        }
//
//        RestTemplate  rest = _newRestTemplate();
//        try {
//            FileRequestCallback  callback =
//                            new FileRequestCallback( from_file, media_type );
//            rest.execute( to_uri, HttpMethod.POST, callback, null );
//        } catch (RestClientException ex) {
//            _LOG_.error( "HTTP POST error: " + ex );
//            throw new HttpException( ex );
//        }
//    }



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



        private AcceptHeaderRequestCallback(
                        final List<MediaType> accept_media_types
                        )
        {
            _acceptMediaTypes = accept_media_types;
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

