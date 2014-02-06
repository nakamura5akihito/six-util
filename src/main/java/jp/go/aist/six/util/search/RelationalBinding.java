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
 * A kind of the PropertyBinding that encapsulates property-value relation.
 * For example, in a binding "year = 2002",
 * the property "year" equals the value "2002".
 * The relation types are defined in the {@link Relation}.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: RelationalBinding.java 473 2013-02-20 08:41:32Z nakamura5akihito@gmail.com $
 * @see Relation
 */
public class RelationalBinding
    extends PropertyBinding
{
    private static final long  serialVersionUID = -3280243770318486077L;



    /**
     * Creates an 'EQUAL' binding.
     *
     * @param   property
     *  the property.
     * @param   value
     *  the value to which the property should be related.
     * @return
     *  an 'EQUAL' binding.
     */
    public static RelationalBinding equalBinding(
            final String property,
            final Object value
            )
    {
        return (new RelationalBinding( property, Relation.EQUAL, value ));
    }



    /**
     * Creates a 'NOT_EQUAL' binding.
     *
     * @param   property
     *  the property.
     * @param   value
     *  the value to which the property should be related.
     * @return
     *  a 'NOT_EQUAL' binding.
     */
    public static RelationalBinding notEqualBinding(
            final String property,
            final Object value
            )
    {
        return (new RelationalBinding( property, Relation.NOT_EQUAL, value ));
    }



    /**
     * Creates a 'LESS_THAN' binding.
     *
     * @param   property
     *  the property.
     * @param   value
     *  the value to which the property should be related.
     * @return
     *  a 'LESS_THAN' binding.
     */
    public static RelationalBinding lessThanBinding(
            final String property,
            final Object value
            )
    {
        return (new RelationalBinding( property, Relation.LESS_THAN, value ));
    }



    /**
     * Creates a 'LESS_EQUAL' binding.
     *
     * @param   property
     *  the property.
     * @param   value
     *  the value to which the property should be related.
     * @return
     *  a 'LESS_EQUAL' binding.
     */
    public static RelationalBinding lessEqualBinding(
            final String property,
            final Object value
            )
    {
        return (new RelationalBinding( property, Relation.LESS_EQUAL, value ));
    }



    /**
     * Creates a 'GREATER_THAN' binding.
     *
     * @param   property
     *  the property.
     * @param   value
     *  the value to which the property should be related.
     * @return
     *  a 'GREATER_THAN' binding.
     */
    public static RelationalBinding greaterThanBinding(
            final String property,
            final Object value
            )
    {
        return (new RelationalBinding( property, Relation.GREATER_THAN, value ));
    }



    /**
     * Creates a 'GREATER_EQUAL' binding.
     *
     * @param   property
     *  the property.
     * @param   value
     *  the value to which the property should be related.
     * @return
     *  a 'GREATER_EQUAL' binding.
     */
    public static RelationalBinding greaterEqualBinding(
            final String property,
            final Object value
            )
    {
        return (new RelationalBinding( property, Relation.GREATER_EQUAL, value ));
    }



    /**
     * The value of the property.
     */
    private Object  _value;



    /**
     */
    public static final Relation  DEFAULT_RELATION = Relation.EQUAL;



    /**
     * The relational operator to associate the property with value.
     */
    private Relation  _relation = DEFAULT_RELATION;



    /**
     * Default constructor.
     */
    public RelationalBinding()
    {
    }


    /**
     * Constructs a RelationalBinding with the specified property
     * and value with the default relation type; EQUAL.
     *
     * @param   property
     *  the property to be searched.
     * @param   value
     *  the value to which the property should be related.
     */
    public RelationalBinding(
                    final String property,
                    final Object value
                    )
    {
        this( property, DEFAULT_RELATION, value );
    }


    /**
     * Constructs a RelationalBinding with the specified property,
     * relation type, and value.
     *
     * @param   property
     *  the property to be searched.
     * @param   relation
     *  the type of the relation between the specified property and value.
     * @param   value
     *  the value to which the property should be related.
     */
    public RelationalBinding(
                    final String property,
                    final Relation relation,
                    final Object value
                    )
    {
        super( property );
        setRelation( relation );
        setValue( value );
    }




    /**
     * Sets the value.
     *
     * @param   value
     *  the value to which the property should be related.
     */
    public void setValue(
                    final Object value
                    )
    {
        _value = value;
    }


    /**
     * Returns the value.
     *
     * @return
     *  the value to which the property should be related.
     */
    public Object getValue()
    {
        return _value;
    }



    /**
     * Sets the type of the relation between the property and value.
     *
     * @param   relation
     *  the relation type.
     */
    public void setRelation(
                    final Relation relation
                    )
    {
        _relation = relation;
    }


    /**
     * Returns the type of the relation between the property and value.
     *
     * @return
     *  the relation type.
     */
    public Relation getRelation()
    {
        return ( _relation == null ? DEFAULT_RELATION : _relation );
    }



    //**************************************************************
    //  java.lang.Object
    //**************************************************************

    /**
     * Determines whether another object is equal to this RelationalBinding.
     * The result is true if and only if the argument is not null
     * and is a RelationalBinding object that has the same property,
     * relation type, and value.
     *
     * @param   obj
     *  the object to test for equality with this RelationalBinding.
     * @return
     *  true if the given object equals this one;
     *  false otherwise.
     */
    @Override
    public boolean equals(
                    final Object obj
                    )
    {
        if (!(obj instanceof RelationalBinding)) {
            return false;
        }

        if (super.equals( obj )) {
            final RelationalBinding  other = (RelationalBinding)obj;
            final Object  other_value = other.getValue();
            final Object   this_value =  this.getValue();
            if (other_value == this_value
                    ||  (this_value != null  &&  this_value.equals( other_value ))) {
                final Relation  other_rel = other.getRelation();
                final Relation   this_rel =  this.getRelation();
                if (other_rel == this_rel) {
                    return true;
                }
            }
        }

        return false;
    }



    /**
     * Computes the hash code for this RelationaBinding.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        final int  prime = 37;
        int  result = super.hashCode();

        final Relation  rel = getRelation();
        result = prime * result + (rel == null ? 0 : rel.hashCode());

        final Object  value = getValue();
        result = prime * result + (value == null ? 0 : value.hashCode());

        return result;
    }



    /**
     * Returns a string representation of this RelationalBinding.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this RelationalBinding.
     */
    @Override
    public String toString()
    {
        Relation  rel = getRelation();
        if (rel == null) {
            rel = DEFAULT_RELATION;
        }
        return "" + getProperty() + " "
                    + rel.operator() + " " + getValue();
    }

}
// RelationalBinding
