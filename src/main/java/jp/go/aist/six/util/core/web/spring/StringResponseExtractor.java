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
import java.io.InputStreamReader;
import java.io.StringWriter;
import jp.go.aist.six.util.IoUtil;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;



/**
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: StringResponseExtractor.java 527 2013-03-08 06:41:36Z nakamura5akihito@gmail.com $
 */
public class StringResponseExtractor
    implements ResponseExtractor<String>
{

    /**
     * Constructor.
     */
    protected StringResponseExtractor()
    {
    }



    //**************************************************************
    //  ResponseExtractor<T>
    //**************************************************************

    public String extractData(
                    final ClientHttpResponse response
                    )
    throws IOException
    {
        StringWriter  output = new StringWriter();
        IoUtil.copy( new InputStreamReader( response.getBody() ), output );

        return output.toString();
    }

}
//

