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
package jp.go.aist.six.util.web;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;




/**
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: HttpClient.java 554 2013-04-12 09:00:30Z nakamura5akihito@gmail.com $
 */
public interface HttpClient
{

//    public void setBaseUrl( String url );
//    public String getBaseUrl();

//    public String getObjectMediaType();




    //=====================================================================
    //  HTTP GET
    //=====================================================================

    /**
     * HTTP GET
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public <T> T getObject( String url, Class<T> response_type, Object... uri_variables );



    /**
     * HTTP GET: Receives the contents from the specified URL and writes them to the given stream.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public void getToWrite( String url, OutputStream output );



    /**
     * HTTP GET: Receives the contents from the specified URL and writes them to the given stream.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public void getToWrite( String url, OutputStream output, String... accept_media_types );



    /**
     * HTTP GET: Receives the contents from the specified URL and writes them to the given stream.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public void getToWrite( String url, Writer output );



    /**
     * HTTP GET: Receives the contents from the specified URL and writes them to the given stream.
     *
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public void getToWrite( String url, Writer output, String... accept_media_types );



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
    public <T> String postObject( String url_path, T object, Class<T> type );



    /**
     * HTTP POST: Reads the contents from the specified stream and sends them to the URL.
     *
     * @return
     *  the location, as an URI, where the resource is created.
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public String postByRead( String url, InputStream input, String media_type );



    /**
     * HTTP POST: Reads the contents from the specified reader and sends them to the URL.
     *
     * @return
     *  the location, as an URI, where the resource is created.
     * @throws  HttpException
     *  when an exceptional condition occurred during the HTTP method execution.
     */
    public String postByRead( String url, Reader input, String media_type );

}
//
