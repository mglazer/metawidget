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

package org.metawidget.statically.spring.layout;

import java.util.Map;

import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.html.layout.HtmlTableLayout;
import org.metawidget.statically.jsp.html.layout.HtmlTableLayoutConfig;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTableHeader;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTableRow;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.statically.spring.widgetbuilder.FormLabelTag;
import org.metawidget.statically.spring.widgetprocessor.PathProcessor;

/**
 * Layout to arrange widgets using an HTML table and Spring &lt;form:label&gt; tags.
 *
 * @author Ryan Bradley
 */

public class SpringTableLayout
	extends HtmlTableLayout {

	//
	// Constructors
	//

	public SpringTableLayout() {

		super();
	}

	public SpringTableLayout( HtmlTableLayoutConfig config ) {

		super( config );
	}

	//
	// Public methods
	//

	/**
	 * Overridden to use Spring's &lt;form:label&gt;.
	 */

	@Override
	protected boolean layoutLabel( HtmlTableRow row, StaticXmlWidget widgetNeedingLabel, String elementName, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

		FormLabelTag label = new FormLabelTag();
		String id = getWidgetId( widgetNeedingLabel );

		if ( id != null ) {
			label.putAttribute( "for", id );
		}

		metawidget.getWidgetProcessor( PathProcessor.class ).processWidget( label, elementName, attributes, (StaticSpringMetawidget) metawidget );
		String labelText = metawidget.getLabelString( attributes );
		label.setTextContent( labelText );

		HtmlTableHeader labelCell = new HtmlTableHeader();
		labelCell.getChildren().add( label );
		row.getChildren().add( labelCell );

		return true;
	}
}
