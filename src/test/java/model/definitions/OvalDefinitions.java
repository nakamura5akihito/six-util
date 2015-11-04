/**
 * SIX OVAL - http://code.google.com/p/six-oval/
 * Copyright (C) 2010
 *   National Institute of Advanced Industrial Science and Technology (AIST)
 *   Registration Number: H22PRO-1124
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
package model.definitions;

import model.common.AbstractDocument;
import model.common.GeneratorType;
import org.mongodb.morphia.annotations.Entity;



/**
 * An OVAL Definition Document.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: OvalDefinitions.java 3055 2013-02-22 10:26:33Z nakamura5akihito@gmail.com $
 * @see <a href="http://oval.mitre.org/language/">OVAL Language</a>
 */
@Entity( "oval.def.oval_definitions" )
public class OvalDefinitions
    extends AbstractDocument
{

//    private GeneratorType  generator = new GeneratorType();
//    //{1..1}




    /**
     * Constructor.
     */
    public OvalDefinitions()
    {
    }


    public OvalDefinitions(
                    final GeneratorType generator
                    )
    {
        super( generator );
//        setGenerator( generator );
    }



//    /**
//     */
//    public void setGenerator(
//                    final GeneratorType generator
//                    )
//    {
//        this.generator = generator;
//    }
//
//
//    public GeneratorType getGenerator()
//    {
//        return this.generator;
//    }

}
//OvalDefinitions
