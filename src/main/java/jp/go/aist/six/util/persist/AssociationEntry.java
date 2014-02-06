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
 * This association is represented as a pair of persistent IDs of the associated objects.
 *
 * @author	Akihito Nakamura, AIST
 * @version $Id: AssociationEntry.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 */
public class AssociationEntry<K, L, M>
//public class AssociationEntry<K, L, A extends Persistable<L>, M, D extends Persistable<M>>
    extends AbstractPersistable<K>
{

    private L  _antecendentPersistentID;
    private M  _dependentPersistentID;



    /**
     * Constructor.
     */
    public AssociationEntry()
    {
    }


    /**
     * Constructor.
     */
    public <A extends Persistable<L>, D extends Persistable<M>>
    AssociationEntry(
                    final A antecendent,
                    final D dependent
//                                    final <? extends Persistable<L>> antecendent,
//                                                    final <? extends Persistable<M>> dependent
                    )
    {
        setAntecendentPersistentID( antecendent.getPersistentID() );
        setDependentPersistentID( dependent.getPersistentID() );
    }


    /**
     * Constructor.
     */
    public AssociationEntry(
                    final L antecendentPID,
                    final M dependentPID
                    )
    {
        setAntecendentPersistentID( antecendentPID );
        setDependentPersistentID( dependentPID );
    }




    public void setAntecendentPersistentID(
                    final L pid
                    )
    {
        _antecendentPersistentID = pid;
    }


    public L getAntecendentPersistentID()
    {
        return _antecendentPersistentID;
    }



    public void setDependentPersistentID(
                    final M pid
                    )
    {
        _dependentPersistentID = pid;
    }


    public M getDependentPersistentID()
    {
        return _dependentPersistentID;
    }



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    @Override
    public int hashCode()
    {
        final int  prime = 37;
        int  result = 17;

        L  a_pid = getAntecendentPersistentID();
        result = prime * result + ((a_pid == null) ? 0 : a_pid.hashCode());

        M  d_pid = getDependentPersistentID();
        result = prime * result + ((d_pid == null) ? 0 : d_pid.hashCode());

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

        if (! AssociationEntry.class.isInstance( obj )) {
            return false;
        }

        try {
            @SuppressWarnings( "unchecked" )
            AssociationEntry<K, L, M>  other = (AssociationEntry<K, L, M>)obj;
//            AssociationEntry<K, L, A, M, D>  other = (AssociationEntry<K, L, A, M, D>)obj;
            L  other_a_pid = other.getAntecendentPersistentID();
            L   this_a_pid =  this.getAntecendentPersistentID();
            if (this_a_pid == other_a_pid
                            ||  (this_a_pid != null  &&  this_a_pid.equals( other_a_pid ))) {
                M  other_d_pid = other.getDependentPersistentID();
                M   this_d_pid =  this.getDependentPersistentID();
                if (this_d_pid == other_d_pid
                                ||  (this_d_pid != null  &&  this_d_pid.equals( other_d_pid ))) {
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
        return "[antecendentID=" + getAntecendentPersistentID()
                        + ", dependentID=" + getDependentPersistentID()
                        + "]";
    }

}
// AssociationEntry
