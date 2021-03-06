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

package org.metawidget.faces.component.html.widgetbuilder.richfaces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.awt.Color;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.FacesException;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockComponent;
import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.FacesMetawidgetTests.MockMethodBinding;
import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.util.CollectionUtils;
import org.richfaces.component.UICalendar;
import org.richfaces.component.UISuggestionBox;
import org.richfaces.component.html.HtmlInputNumberSlider;
import org.richfaces.component.html.HtmlInputNumberSpinner;
import org.richfaces.component.html.HtmlSuggestionBox;

/**
 * @author Richard Kennard
 */

public class RichFacesWidgetBuilderTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	public void testRichFacesWidgetBuilder()
		throws Exception {

		RichFacesWidgetBuilder widgetBuilder = new RichFacesWidgetBuilder();

		// Read-only pass throughs

		Map<String, String> attributes = CollectionUtils.newHashMap();
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.put( READ_ONLY, TRUE );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.put( LOOKUP, TRUE );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( LOOKUP );
		attributes.put( FACES_LOOKUP, TRUE );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( FACES_LOOKUP );
		attributes.put( HIDDEN, TRUE );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( HIDDEN );
		attributes.put( TYPE, "foo" );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		// Active pass throughs

		attributes.remove( READ_ONLY );
		attributes.put( LOOKUP, TRUE );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( LOOKUP );
		attributes.put( FACES_LOOKUP, TRUE );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( FACES_LOOKUP );
		attributes.put( HIDDEN, TRUE );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( HIDDEN );
		attributes.put( TYPE, "foo" );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		// Sliders

		attributes.put( TYPE, int.class.getName() );
		attributes.put( MINIMUM_VALUE, "1" );
		attributes.put( MAXIMUM_VALUE, "1024" );
		HtmlInputNumberSlider slider = (HtmlInputNumberSlider) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "1", slider.getMinValue() );
		assertEquals( "1024", slider.getMaxValue() );

		attributes.put( TYPE, Long.class.getName() );
		attributes.put( MINIMUM_VALUE, "2" );
		attributes.put( MAXIMUM_VALUE, "1023" );
		slider = (HtmlInputNumberSlider) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "2", slider.getMinValue() );
		assertEquals( "1023", slider.getMaxValue() );

		// Spinners

		attributes.put( TYPE, int.class.getName() );
		attributes.put( MAXIMUM_VALUE, "" );
		HtmlInputNumberSpinner spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "2", spinner.getMinValue() );
		attributes.put( MAXIMUM_VALUE, "1024" );

		attributes.put( MINIMUM_VALUE, "" );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "1024", spinner.getMaxValue() );

		// (lower bound)

		attributes.put( TYPE, byte.class.getName() );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( String.valueOf( Byte.MIN_VALUE ), spinner.getMinValue() );

		attributes.put( TYPE, short.class.getName() );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( String.valueOf( Short.MIN_VALUE ), spinner.getMinValue() );

		attributes.put( TYPE, int.class.getName() );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( String.valueOf( Integer.MIN_VALUE ), spinner.getMinValue() );

		attributes.put( TYPE, long.class.getName() );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( String.valueOf( Long.MIN_VALUE ), spinner.getMinValue() );

		attributes.put( TYPE, float.class.getName() );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( String.valueOf( -Float.MAX_VALUE ), spinner.getMinValue() );

		attributes.put( TYPE, double.class.getName() );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( String.valueOf( -Double.MAX_VALUE ), spinner.getMinValue() );

		// (upper bound)

		attributes.put( MAXIMUM_VALUE, "" );

		attributes.put( TYPE, byte.class.getName() );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( String.valueOf( Byte.MAX_VALUE ), spinner.getMaxValue() );

		attributes.put( TYPE, short.class.getName() );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( String.valueOf( Short.MAX_VALUE ), spinner.getMaxValue() );

		attributes.put( TYPE, int.class.getName() );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( String.valueOf( Integer.MAX_VALUE ), spinner.getMaxValue() );

		attributes.put( TYPE, long.class.getName() );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( String.valueOf( Long.MAX_VALUE ), spinner.getMaxValue() );

		attributes.put( TYPE, float.class.getName() );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( String.valueOf( Float.MAX_VALUE ), spinner.getMaxValue() );

		attributes.put( TYPE, double.class.getName() );
		spinner = (HtmlInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( String.valueOf( Double.MAX_VALUE ), spinner.getMaxValue() );

		// Calendars

		attributes.put( TYPE, Date.class.getName() );
		attributes.put( DATETIME_PATTERN, "dd-MM-yyyy" );
		attributes.put( LOCALE, "en-AU" );
		attributes.put( TIME_ZONE, "Australia/Sydney" );
		UICalendar calendar = (UICalendar) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "dd-MM-yyyy", calendar.getDatePattern() );
		assertEquals( new Locale( "en-AU" ), calendar.getLocale() );
		assertEquals( TimeZone.getTimeZone( "Australia/Sydney" ), calendar.getTimeZone() );

		// Suggest

		attributes.put( TYPE, String.class.getName() );
		attributes.put( FACES_SUGGEST, "#{foo.bar}" );

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setStyle( "aStyle" );
		metawidget.setStyleClass( "aStyleClass" );
		UIStub stub = (UIStub) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( stub.getId(), null );
		assertTrue( stub.getChildCount() == 2 );

		HtmlInputText htmlInputText = (HtmlInputText) stub.getChildren().get( 0 );
		UISuggestionBox suggestionBox = (UISuggestionBox) stub.getChildren().get( 1 );
		assertTrue( htmlInputText.getId().startsWith( "suggestionText_" ) );
		assertEquals( "aStyle", htmlInputText.getStyle() );
		assertEquals( "aStyleClass", htmlInputText.getStyleClass() );
		assertTrue( suggestionBox.getId().startsWith( "suggestionBox_" ) );
		assertEquals( suggestionBox.getFor(), htmlInputText.getId() );
		assertEquals( "#{foo.bar}", suggestionBox.getSuggestionAction().getExpressionString() );
		assertTrue( Object.class == ( (MockMethodBinding) suggestionBox.getSuggestionAction() ).getParams()[0] );
		assertTrue( 1 == ( (MockMethodBinding) suggestionBox.getSuggestionAction() ).getParams().length );
		assertTrue( suggestionBox.getChildCount() == 1 );
		assertTrue( suggestionBox.getChildren().get( 0 ) instanceof UIColumn );
		// (a org.richfaces.component.UIColumn did not POST back properly)
		assertTrue( !( suggestionBox.getChildren().get( 0 ) instanceof org.richfaces.component.UIColumn ));
		assertTrue( suggestionBox.getChildren().get( 0 ).getId() != null );
		HtmlOutputText htmlOutputText = (HtmlOutputText) suggestionBox.getChildren().get( 0 ).getChildren().get( 0 );
		assertTrue( htmlOutputText.getId() != null );
		assertEquals( FacesUtils.wrapExpression( suggestionBox.getVar() ), htmlOutputText.getValueBinding( "value" ).getExpressionString() );

		attributes.remove( FACES_SUGGEST );

		// ColorPickers (as of RichFaces 3.3.1)

		attributes.put( TYPE, Color.class.getName() );
		MockComponent mockComponent = (MockComponent) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "org.richfaces.ColorPicker", mockComponent.getFamily() );

		attributes.put( READ_ONLY, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof HtmlOutputText );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp()
		throws Exception {

		super.setUp();

		mContext = new MockRichFacesFacesContext();
	}

	@Override
	protected void tearDown()
		throws Exception {

		super.tearDown();

		mContext.release();
	}

	//
	// Inner class
	//

	protected static class MockRichFacesFacesContext
		extends MockFacesContext {

		//
		// Protected methods
		//

		@Override
		public UIComponent createComponent( String componentName )
			throws FacesException {

			// RichFaces 3.x uses lowercase 'i'

			if ( "org.richfaces.inputNumberSlider".equals( componentName ) ) {
				return new HtmlInputNumberSlider();
			}

			// RichFaces 3.x uses lowercase 'i'

			if ( "org.richfaces.inputNumberSpinner".equals( componentName ) ) {
				return new HtmlInputNumberSpinner();
			}

			if ( UICalendar.COMPONENT_TYPE.equals( componentName ) ) {
				return new UICalendar();
			}

			// Should *not* use org.richfaces.component.html.HtmlColumn

			if ( HtmlSuggestionBox.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlSuggestionBox();
			}

			return super.createComponent( componentName );
		}
	}
}
