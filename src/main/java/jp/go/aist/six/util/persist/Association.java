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
package jp.go.aist.six.util.persist;



/**
 * An association between two types of Persistent objects.
 *
 * @author	Akihito Nakamura, AIST
 * @version $Id: Association.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class Association<K, L, A extends Persistable<L>, M, D extends Persistable<M>>
    extends AssociationEntry<K, L, M>
{
    private A  _antecendent;
    private D  _dependent;



    /**
     * Constructor.
     */
    public Association()
    {
    }


    /**
     * Constructor.
     */
    public Association(
                    final A antecendent,
                    final D dependent
                    )
    {
        setAntecendent( antecendent );
        setDependent( dependent );
    }



    public void setAntecendent(
                    final A antecendent
                    )
    {
        _antecendent = antecendent;
    }



    public A getAntecendent()
    {
        return _antecendent;
    }



    public void setDependent(
                    final D dependent
                    )
    {
        _dependent = dependent;
    }


    public D getDependent()
    {
        return _dependent;
    }



    //**************************************************************
    //  AssociationEntry
    //**************************************************************

    @Override
    public void setAntecendentPersistentID(
                    final L id
                    )
    {
    }


    @Override
    public L getAntecendentPersistentID()
    {
        return (_antecendent == null ? null : _antecendent.getPersistentID());
    }



    @Override
    public void setDependentPersistentID(
                    final M id
                    )
    {
    }


    @Override
    public M getDependentPersistentID()
    {
        return (_dependent == null ? null : _dependent.getPersistentID());
    }



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    @Override
    public int hashCode()
    {
        final int  prime = 37;
        int  result = 17;

        A  a = getAntecendent();
        result = prime * result + ((a == null) ? 0 : a.hashCode());

        D  d = getDependent();
        result = prime * result + ((d == null) ? 0 : d.hashCode());

        return result;
    }



    @Override
    public boolean equals(
                    final Object obj
                    )
    {
        if (this == obj) {
            return true;
        }

        if (! Association.class.isInstance( obj )) {
            return false;
        }

        try {
            @SuppressWarnings( "unchecked" )
            Association<K, L, A, M, D>  other = (Association<K, L, A, M, D>)obj;
            A  other_a = other.getAntecendent();
            A   this_a =  this.getAntecendent();
            if (this_a == other_a
                            ||  (this_a != null  &&  this_a.equals( other_a ))) {
                D  other_d = other.getDependent();
                D   this_d =  this.getDependent();
                if (this_d == other_d
                                ||  (this_d != null  &&  this_d.equals( other_d ))) {
                    return true;
                }
            }
        } catch (ClassCastException ex) {
            return false;
        }

        return false;
    }



    @Override
    public String toString()
    {
        return "[antecendent=" + getAntecendent()
                + ", dependent=" + getDependent()
                + "]";
    }

}
// Association
