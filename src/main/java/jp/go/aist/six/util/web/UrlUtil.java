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

import jp.go.aist.six.util.repository.QueryParams;



/**
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: UrlUtil.java 563 2013-04-12 10:38:15Z nakamura5akihito@gmail.com $
 */
public class UrlUtil
{

    /**
     *
     * @param params
     * @return
     * 	converts the given query parameters object to an URL query parameters string.
     */
    public static String toString(
                    final QueryParams params
                    )
    {
        if (params == null  ||  params.size() == 0) {
            return "";
        }

        StringBuilder  s = new StringBuilder();
        boolean  first_key = true;
        for (String  key : params.keys()) {
            if (first_key) {
                s.append( "?" );
                first_key = false;
            } else {
                s.append( "&" );
            }

            String  value = params.get( key );
            s.append( key ).append( "=" ).append( value );
        }

        return s.toString();
    }

}
//
