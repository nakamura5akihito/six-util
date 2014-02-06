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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;



/**
 * A full-text search constraint.
 * This base class has only the search string.
 * The grammer and interpretation of the search string
 * depend on the application.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: FulltextMatch.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 * @deprecated  This constraint is replaced by TextMatchBinding.
 */
@Deprecated
public class FulltextMatch
    implements Serializable
{
    private static final long  serialVersionUID = -393072030390870826L;



    /**
     *
     */
    private final Collection<String>  _properties = new ArrayList<String>();



    /**
     * The search strings.
     */
    private final List<String>  _patterns = new ArrayList<String>();



    /**
     * The match-all mode.
     * True means that target should  match all the strings,
     * while false does at least one.
     */
    private boolean  _matchAll = true;




    /**
     * Default constructor.
     */
    public FulltextMatch()
    {
    }


    /**
     * Constructs a FulltextMatch with the specified search strings
     * and the default match-all mode.
     *
     * @param   patterns
     *  the search strings.
     */
    public FulltextMatch(
                    final Collection<String> properties,
                    final Collection<String> patterns
                    )
    {
        this( properties, patterns, true );
    }

    public FulltextMatch(
                    final String[] properties,
                    final String[] patterns
                    )
    {
        this( properties, patterns, true );
    }




    /**
     * Constructs a FulltextMatch with the specified search strings
     * and the match-all mode.
     *
     * @param   patterns
     *  the search strings.
     * @param   matchAll
     *  the match-all mode.
     */
    public FulltextMatch(
                    final Collection<String> properties,
                    final Collection<String> patterns,
                    final boolean matchAll
                    )
    {
        setProperties( properties );
        setPatterns( patterns );
        setMatchAll( matchAll );
    }

    public FulltextMatch(
                    final String[] properties,
                    final String[] patterns,
                    final boolean matchAll
                    )
    {
        setProperties( properties );
        setPatterns( patterns );
        setMatchAll( matchAll );
    }



    /**
     * Sets the properties.
     * First, the previous elements are cleared and all of
     * the specified properties are appended.
     * The properties are appended in the array order.
     *
     * @param   properties
     *  a list of properties.
     * @throws  IllegalArgumentException
     *  if the specified array contains a null element.
     *  The previous list of properties are preserved.
     */
    public FulltextMatch setProperties(
                    final Collection<String> properties
                    )
    {
        if (properties == null  ||  properties.size() == 0) {
            clearProperties();
            return this;
        }

        synchronized (properties) {
            for (String  binding : properties) {
                if (binding == null) {
                    throw new IllegalArgumentException( "null element" );
                }
            }

            clearProperties();
            for (String  property : properties) {
                addProperty( property );
            }
        }

        return this;
    }

    public FulltextMatch setProperties(
                    final String[] properties
                    )
    {
        if (properties == null  ||  properties.length == 0) {
            clearProperties();
            return this;
        }

        List<String>  list = Arrays.asList( properties );
        for (String  binding : list) {
            if (binding == null) {
                throw new NullPointerException( "array contains null" );
            }
        }

        clearProperties();
        for (String  binding : list) {
            addProperty( binding );
        }

        return this;
    }


    /**
     * Appends the specified property.
     *
     * @param   property
     *  the property to be appended.
     * @throws  IllegalArgumentException
     *  if the specified property is null.
     */
    public FulltextMatch addProperty(
                    final String property
                    )
    {
        if (property == null) {
            throw new IllegalArgumentException( "no property specified" );
        }

        _properties.add( property );

        return this;
    }



    /**
     * Returns a list of the properties.
     *
     * @return
     *  a list of the properties.
     */
    public Collection<String> getProperties()
    {
        return _properties;
    }
//    public String[] getProperties()
//    {
//        String[]  empty = new String[0];
//        if (_properties== null) {
//            return empty;
//        }
//
//        return (String[])(_properties.toArray( empty ));
//    }


    /**
     * Returns an iterator over the properties.
     *
     * @return
     *  an iterator over the properties.
     */
    public Iterator<String> properties()
    {
        return _properties.iterator();
    }


    /**
     * Removes all of the properties from this match.
     */
    public FulltextMatch clearProperties()
    {
        _properties.clear();

        return this;
    }



    /**
     * Sets the matching patterns.
     *
     * @param   patterns
     *  the matching patterns
     */
    public FulltextMatch setPatterns(
                    final Collection<String> patterns
                    )
    {
        if (patterns == null  ||  patterns.size() == 0) {
            clearPatterns();
            return this;
        }

        synchronized (patterns) {
            for (String  pattern : patterns) {
                if (pattern == null  ||  pattern.length() == 0) {
                    throw new IllegalArgumentException( "null element" );
                }
            }

            clearPatterns();
            for (String  pattern : patterns) {
                addPattern( pattern );
            }
        }

        return this;
    }

    public FulltextMatch setPatterns(
                    final String[] patterns
                    )
    {
        if (patterns == null  ||  patterns.length == 0) {
            clearPatterns();
            return this;
        }

        List<String>  list = Arrays.asList( patterns );
        for (String  pattern : list) {
            if (pattern == null  ||  pattern.length() == 0) {
                throw new IllegalArgumentException( "array contains null or empty pattern" );
            }
        }

        clearPatterns();
        for (String  pattern : list) {
            addPattern( pattern );
        }

        return this;
    }


    /**
     * Appends the specified matching pattern.
     *
     * @param   pattern
     *  the matching pattern to be appended.
     * @throws  IllegalArgumentPointerException
     *  if the specified pattern is null.
     */
    public FulltextMatch addPattern(
                    final String pattern
                    )
    {
        if (pattern == null  ||  pattern.length() == 0) {
            throw new IllegalArgumentException( "no pattern specified" );
        }

        _patterns.add( pattern );

        return this;
    }


    /**
     * Returns the matching patterns.
     *
     * @return
     *  the matching patterns.
     */
    public Collection<String> getPatterns()
    {
        return _patterns;
    }
//    public String[] getPatterns()
//    {
//        String[]  empty = new String[0];
//        if (_patterns == null) {
//            return empty;
//        }
//
//        return (String[])(_patterns.toArray( empty ));
//    }


    /**
     * Removes all of the patterns.
     */
    public FulltextMatch clearPatterns()
    {
        _patterns.clear();

        return this;
    }



    /**
     * Sets the match-all mode.
     *
     * @param   mode
     *  true if the match-all mode is set to TRUE;
     *  otherwise FALSE.
     */
    public FulltextMatch setMatchAll(
                    final boolean mode
                    )
    {
        _matchAll = mode;

        return this;
    }



    /**
     * Returns true if the match-all mode is TRUE.
     *
     * @return
     *  true if the match-all mode is TRUE.
     */
    public boolean isMatchAll()
    {
        return _matchAll;
    }



    /**
     * Splits the specified string.
     * The string is divided around matches of the space character.
     * The continuous space characters are counted as one.
     *
     * @return
     *  a list of the result strings.
     */
    public static List<String> split(
                    final String str
                    )
    {
        String  s = str;
        if (s != null) {
            s = s.trim();
        }

        List<String>  list = new ArrayList<String>();
        if (s == null || s.length() == 0) {
            return list;
        }

        String[]  tokens = s.split( "[ \t\n\r\f]" );
        // NOTE: tokens[i] may be an empty string "".

        // excludes empty strings.
        int  n_tokens = tokens.length;
        for (int  i = 0; i < n_tokens; i++) {
            if (tokens[i].length() > 0) {
                list.add( tokens[i] );
            }
        }

        return list;
    }



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    /**
     * Determines whether another object is equal to this FulltextMatch.
     * The result is true if and only if the argument is not null
     * and is a FulltextMatch object that has the same property,
     * relation type, and value.
     *
     * @param   obj
     *  the object to test for equality with this FulltextMatch.
     * @return
     *  true if the given object equals this one;
     *  false otherwise.
     */
    @Override
    public boolean equals(
                    final Object obj
                    )
    {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FulltextMatch)) {
            return false;
        }

        FulltextMatch  other = (FulltextMatch)obj;
        Collection<String>  other_patterns = other.getPatterns();
        Collection<String>   this_patterns =  this.getPatterns();
        if (other_patterns == this_patterns
                        ||  (this_patterns != null
                                        &&  this_patterns.equals( other_patterns ))) {
            Collection<String>  other_props = other.getProperties();
            Collection<String>   this_props =  this.getProperties();
            if (other_props == this_props
                            ||  (this_props != null
                                            &&  this_props.equals( other_props ))) {
                if (isMatchAll() == other.isMatchAll()) {
                    return true;
                }
            }
        }

        return false;
    }



    /**
     * Computes the hash code for this FulltextMatch.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        final int  prime = 37;
        int  result = 17;

        result = prime * result + (isMatchAll() ? 0 : 1);

        Collection<String>  properties = getProperties();
        result = prime * result + (properties == null ? 0 : properties.hashCode());

        Collection<String>  patterns = getPatterns();
        result = prime * result + (patterns == null ? 0 : patterns.hashCode());

        return result;
    }



    /**
     * Returns a string representation of this FulltextMatch.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this FulltextMatch.
     */
    @Override
    public String toString()
    {
        StringBuffer  s = new StringBuffer( "FulltextMatch[matchAll=" );
        s.append( isMatchAll() );
        s.append( ", properties=" ).append( getProperties() );
        s.append( ", patterns=" ).append( getPatterns() );

//        String[]  strings = getPatterns();
//        if (strings == null || strings.length == 0) {
//            s.append( "{}" );
//        } else {
//            s.append( String.valueOf( Arrays.asList( strings ) ) );
//        }
        s.append( "]" );

        return s.toString();
    }

}
// FulltextMatch
