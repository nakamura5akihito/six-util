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
package jp.go.aist.six.util.search;



/**
 * The Function represents a type of function used in search queries.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: Function.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public enum Function
{
    /**
     * COUNT function to obtain the number of objects.
     */
    COUNT,

    /**
     * MAX function to obtain the maximum value.
     */
    MAX,

    /**
     * MIN function to obtain the minimum value.
     */
    MIN,

    /**
     * SUM function to obtain the sum value.
     */
    SUM,

    /**
     * AVG function to obtain the average value.
     */
    AVG;
}
// Function
