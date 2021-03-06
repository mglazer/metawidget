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

package org.metawidget.inspector.jsp;

import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Metawidget's JSP support (declared in this same package).
 *
 * @author Richard Kennard
 */

public class JspAnnotationInspector
	extends BaseObjectInspector {

	//
	// Constructor
	//

	public JspAnnotationInspector() {

		this( new BaseObjectInspectorConfig() );
	}

	public JspAnnotationInspector( BaseObjectInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// UiJspLookup

		UiJspLookup jspLookup = property.getAnnotation( UiJspLookup.class );

		if ( jspLookup != null ) {
			attributes.put( JSP_LOOKUP, jspLookup.value() );
		}

		return attributes;
	}
}
