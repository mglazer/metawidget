// Metawidget
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.metawidget.inspector.faces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates the value in the field should belong to the set returned by the given EL expression.
 *
 * @author Richard Kennard
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiFacesLookup {

	/**
	 * An EL expression for the lookup, of the form <code>#{...}</code>.
	 */

	String value();

	/**
	 * Name of the EL var to be referenced in <code>itemValue</code> and <code>itemLabel</code> (JSF
	 * 2 only).
	 */

	String var() default "";

	/**
	 * An EL expression for the item value, of the form <code>#{...}</code> (JSF 2 only). Typically
	 * this will make reference to the <code>var</code> attribute, for example
	 * <code>#{_item.value}</code>.
	 */

	String itemValue() default "";

	/**
	 * An EL expression for the item value, of the form <code>#{...}</code> (JSF 2 only). Typically
	 * this will make reference to the <code>var</code> attribute, for example
	 * <code>#{_item.label}</code>.
	 */

	String itemLabel() default "";
}
