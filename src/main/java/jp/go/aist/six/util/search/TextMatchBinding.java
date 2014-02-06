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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * A text matching constraint.
 * For the specified property, it's value must match the given words/phrases.
 * There are two modes: match-all (default) and match-one.
 * If the mode is match-all, the property value must contain all the words/phrases.
 * While in match-one mode, at least one word/phrase must be contained.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: TextMatchBinding.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class TextMatchBinding
    extends PropertyBinding
{

    /**
     * The matching words/phrases.
     */
    private final Collection<String>  _text = new ArrayList<String>();



    /**
     * The default matching mode: true.
     */
    public static final boolean  DEFAULT_MATCH_ALL = true;

    /**
     * The match-all mode.
     * True means that target should  match all the strings,
     * while false does at least one.
     */
    private boolean  _matchAll = DEFAULT_MATCH_ALL;




    /**
     * Default constructor.
     */
    public TextMatchBinding()
    {
    }


    /**
     * Constructs a TextMatch with the specified words/phrases
     * and the default match-all mode.
     *
     * @param   property
     *  the target property of this constraint.
     * @param   text
     *  the words/phrases.
     */
    public TextMatchBinding(
                    final String property,
                    final Collection<String> text
                    )
    {
        this( property, text, DEFAULT_MATCH_ALL );
    }



    /**
     * Constructs a TextMatch with the specified words/phrases
     * and the match-all mode.
     *
     * @param   property
     *  the target property of this constraint.
     * @param   text
     *  the words/phrases.
     * @param   matchAll
     *  the match-all mode.
     */
    public TextMatchBinding(
                    final String property,
                    final Collection<String> text,
                    final boolean matchAll
                    )
    {
        super( property );
        setText( text );
        setMatchAll( matchAll );
    }



    /**
     * Sets the words/phrases.
     *
     * @param   text
     *  the words/phrases.
     */
    public TextMatchBinding setText(
                    final Collection<String> text
                    )
    {
        if (text == null  ||  text.size() == 0) {
            clearText();
            return this;
        }

        synchronized (text) {
            for (String  pattern : text) {
                if (pattern == null  ||  pattern.length() == 0) {
                    throw new IllegalArgumentException( "null element" );
                }
            }

            clearText();
            for (String  pattern : text) {
                addText( pattern );
            }
        }

        return this;
    }


    /**
     * Appends the specified word/phrase.
     *
     * @param   text
     *  the word/phrase to be appended.
     * @throws  IllegalArgumentPointerException
     *  if the specified word/phrase is null or it's length is zero.
     */
    public TextMatchBinding addText(
                    final String text
                    )
    {
        if (text == null  ||  text.length() == 0) {
            throw new IllegalArgumentException( "no pattern specified" );
        }

        _text.add( text );

        return this;
    }


    /**
     * Returns the the words/phrases.
     *
     * @return
     *  the words/phrases.
     */
    public Collection<String> getText()
    {
        return _text;
    }


    /**
     * Removes all of the words/phrases.
     */
    public TextMatchBinding clearText()
    {
        _text.clear();

        return this;
    }



    /**
     * Sets the match-all mode.
     *
     * @param   mode
     *  true if the match-all mode is set to TRUE;
     *  otherwise FALSE.
     */
    public TextMatchBinding setMatchAll(
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
        for (String  t : tokens) {
            if (t.length() > 0) {
                list.add( t );
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
        if (!(obj instanceof TextMatchBinding)) {
            return false;
        }

        if (super.equals( obj )) {
            TextMatchBinding  other = (TextMatchBinding)obj;
            Collection<String>  other_text = other.getText();
            Collection<String>   this_text =  this.getText();
            if (this_text == other_text
                            ||  (this_text != null
                                            &&  this_text.equals( other_text ))) {
                if (this.isMatchAll() == other.isMatchAll()) {
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
        int  result = super.hashCode();

        Collection<String>  text = getText();
        result = prime * result + (text == null ? 0 : text.hashCode());

        result = prime * result + (isMatchAll() ? 0 : 1);

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
        StringBuffer  s = new StringBuffer( "TextMatch[matchAll=" );
        s.append( isMatchAll() );
        s.append( ", text=" ).append( getText() );
        s.append( "]" );

        return s.toString();
    }

}
// TextMatchBinding
