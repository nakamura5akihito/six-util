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
package jp.go.aist.six.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Basic I/O functions.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: IoUtil.java 525 2013-03-08 06:32:23Z nakamura5akihito@gmail.com $
 */
public class IoUtil
{

    /**
     * Logger.
     */
    private static final Logger  _LOG_ = LoggerFactory.getLogger( IoUtil.class );



    /**
     * Reads the contents from the file and writes them to the specified stream.
     *
     * @param   input
     *  the input file.
     * @param   output
     *  the output stream.
     * @return
     *  the total number of bytes written to the output.
     */
    public static long copy(
                    final File input,
                    final OutputStream output
                    )
    throws IOException
    {
        long  size = 0L;
        InputStream  file_input = null;
        try {
            file_input = new FileInputStream( input );
                       //throws FileNotFoundException
            size = copy( file_input, output );
                   //throws IOException
        } finally {
            try {
                file_input.close();
            } catch (Exception ex) {
                //ignorable
            }
        }

        return size;
    }



    /**
     * Reads the contents from the specified stream and writes them to the file.
     *
     * @param   input
     *  the input stream.
     * @param   output
     *  the output file.
     * @return
     *  the total number of bytes written to the output.
     */
    public static long copy(
                    final InputStream input,
                    final File output
                    )
    throws IOException
    {
        OutputStream  stream_output = null;
        long  size = 0L;
        try {
            stream_output = new FileOutputStream( output );
                            //throws FileNotFoundException
            size = copy( input, stream_output );
                   //throws IOException
        } finally {
            try {
                stream_output.close();
            } catch (Exception ex) {
                //ignorable
            }
        }

        return size;

    }



    /**
     * Reads the contents from the specified input stream and writes them to the output stream.
     * In this method, the given streams are not closed.
     *
     * @param   input
     *  the input stream.
     * @param   output
     *  the output stream.
     * @return
     *  the total number of bytes written to the output.
     */
    public static long copy(
                    final InputStream  input,
                    final OutputStream output
                    )
    throws IOException
    {
        _LOG_.debug( "begin copy" );


        @SuppressWarnings( "resource" )
        BufferedInputStream  b_input = (BufferedInputStream.class.isInstance( input )
                                        ? BufferedInputStream.class.cast( input )
                                        : new BufferedInputStream( input ));
        @SuppressWarnings( "resource" )
        BufferedOutputStream  b_output = (BufferedOutputStream.class.isInstance( output )
                                        ? BufferedOutputStream.class.cast( output )
                                        : new BufferedOutputStream( output ));

        byte[]  buffer = new byte[512];
        long  size = 0;
        try {
            while (true) {
                int  n = b_input.read( buffer );
                                 //@throws IOException
                if (n == -1) {
                    break;
                }
                b_output.write( buffer, 0, n );
                     //@throws IOException
                size += n;
            }
        } finally {
            try {
                b_output.flush();
            } catch (Exception ex) {
                //ignorable
            }
        }

        _LOG_.debug( "end copy: #bytes=" + size );
        return size;
    }



    /**
     * Reads the contents from the specified input and writes them to the output.
     * In this method, the given streams are not closed.
     *
     * @param   input
     *  the input reader.
     * @param   output
     *  the output writer.
     * @return
     *  the total number of bytes written to the output.
     */
    public static long copy(
                    final Reader input,
                    final Writer output
                    )
    throws IOException
    {
        _LOG_.debug( "begin copy" );

        @SuppressWarnings( "resource" )
        BufferedReader  b_input = (BufferedReader.class.isInstance( input )
                                    ? BufferedReader.class.cast( input )
                                    : new BufferedReader( input ));
        @SuppressWarnings( "resource" )
        BufferedWriter  b_output = (BufferedWriter.class.isInstance( output )
                        ? BufferedWriter.class.cast( output )
                        : new BufferedWriter( output ));

        char[]  buffer = new char[512];
        long  size = 0;
        try {
            while (true) {
                int  n = b_input.read( buffer );
                               //@throws IOException
                if (n == -1) {
                    break;
                }
                b_output.write( buffer, 0, n );
                       //@throws IOException
                size += n;
            }
        } finally {
            try {
                b_output.flush();
            } catch (Exception ex) {
                //ignorable
            }
        }

        _LOG_.debug( "end copy: #bytes=" + size );
        return size;
    }



    /**
     * Reads the contents characters from the file.
     *
     * @param file
     *  the input file.
     * @return
     *  the read string.
     * @throws IOException
     */
    public static String readCharacters(
                    final File file
                    )
    throws IOException
    {
        Reader  reader = new BufferedReader( new InputStreamReader(
                        new FileInputStream( file ), Charset.forName( "UTF-8" ) ) );
        // Note:
        // The character set should be specified to parse XML
        // containing Japanese characters.

        StringWriter  sw = new StringWriter();
        Writer  writer = new BufferedWriter( sw );

        copy( reader, writer );

        String  characters = sw.toString();

        return characters;
    }

}
//

