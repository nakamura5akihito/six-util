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
import java.io.OutputStreamWriter;
import java.io.StringReader;
import jp.go.aist.six.util.IoUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RequestCallback;



/**
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: StringRequestCallback.java 527 2013-03-08 06:41:36Z nakamura5akihito@gmail.com $
 */
public class StringRequestCallback
    implements RequestCallback
{

    /**
     * The character sequence from which the HTTP request body is read.
     */
    private CharSequence  _string;


    private MediaType  _mediaType;



    /**
     * Constructor.
     */
    protected StringRequestCallback()
    {
    }


    public StringRequestCallback(
                    final CharSequence string,
                    final MediaType  mediaType
                    )
    {
        _string = string;
        _mediaType = mediaType;
    }



    //**************************************************************
    //  RequestCallback
    //**************************************************************

    public void doWithRequest(
                    final ClientHttpRequest request
                    )
    throws IOException
    {
        HttpHeaders  headers = request.getHeaders();
        headers.setContentType( _mediaType );

        long  size = IoUtil.copy( new StringReader( _string.toString() ),
                        new OutputStreamWriter( request.getBody() ) );
        headers.setContentLength( size );
    }

}
//

