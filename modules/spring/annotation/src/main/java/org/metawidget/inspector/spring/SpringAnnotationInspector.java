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

package org.metawidget.inspector.spring;

import static org.metawidget.inspector.spring.SpringInspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Metawidget's Spring support (declared in this same package).
 *
 * @author Richard Kennard
 */

public class SpringAnnotationInspector
	extends BaseObjectInspector {

	//
	// Constructor
	//

	public SpringAnnotationInspector() {

		this( new BaseObjectInspectorConfig() );
	}

	public SpringAnnotationInspector( BaseObjectInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// SpringLookup

		UiSpringLookup springLookup = property.getAnnotation( UiSpringLookup.class );

		if ( springLookup != null ) {
			attributes.put( SPRING_LOOKUP, springLookup.value() );

			String itemValue = springLookup.itemValue();

			if ( !"".equals( itemValue ) ) {
				attributes.put( SPRING_LOOKUP_ITEM_VALUE, itemValue );
			}

			String itemLabel = springLookup.itemLabel();

			if ( !"".equals( itemLabel ) ) {
				attributes.put( SPRING_LOOKUP_ITEM_LABEL, itemLabel );
			}
		}

		return attributes;
	}
}
