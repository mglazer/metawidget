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

package org.metawidget.config;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import junit.framework.TestCase;

import org.metawidget.iface.MetawidgetException;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.faces.FacesInspector;
import org.metawidget.inspector.hibernate.validator.HibernateValidatorInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.propertystyle.struts.StrutsActionFormPropertyStyle;
import org.metawidget.inspector.jpa.JpaInspector;
import org.metawidget.inspector.jsp.JspAnnotationInspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.spring.SpringAnnotationInspector;
import org.metawidget.inspector.struts.StrutsAnnotationInspector;
import org.metawidget.inspector.struts.StrutsInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.mixin.base.BaseMetawidgetMixin;
import org.metawidget.mixin.w3c.MetawidgetMixin;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.widgetbuilder.SwingWidgetBuilder;
import org.metawidget.util.IOUtils;
import org.metawidget.util.LogUtilsTest;
import org.metawidget.widgetbuilder.composite.CompositeWidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.xml.sax.SAXException;

/**
 * @author Richard Kennard
 */

public class ConfigReaderTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testReader()
		throws Exception
	{
		// Configure

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"";
		xml += "	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
		xml += "	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "	<point xmlns=\"java:java.awt\">";
		xml += "		<location>";
		xml += "			<int>10</int>";
		xml += "			<int>20</int>";
		xml += "		</location>";
		xml += "	</point>";
		xml += "	<swingMetawidget xmlns=\"java:org.metawidget.swing\">";
		xml += "		<widgetBuilder>";
		xml += "			<compositeWidgetBuilder xmlns=\"java:org.metawidget.widgetbuilder.composite\" config=\"CompositeWidgetBuilderConfig\">";
		xml += "				<widgetBuilders>";
		xml += "					<array>";
		xml += "						<swingWidgetBuilder xmlns=\"java:org.metawidget.swing.widgetbuilder\"/>";
		xml += "					</array>";
		xml += "				</widgetBuilders>";
		xml += "			</compositeWidgetBuilder>";
		xml += "		</widgetBuilder>";
		xml += "		<inspector>";
		xml += "			<compositeInspector xmlns=\"java:org.metawidget.inspector.composite\"";
		xml += "					xsi:schemaLocation=\"java:org.metawidget.inspector.composite http://metawidget.org/xsd/org.metawidget.inspector.composite-1.0.xsd\"";
		xml += "					config=\"CompositeInspectorConfig\">";
		xml += "				<inspectors>";
		xml += "					<array>";
		xml += "						<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\"/>";
		xml += "						<facesInspector xmlns=\"java:org.metawidget.inspector.faces\"/>";
		xml += "						<hibernateValidatorInspector xmlns=\"java:org.metawidget.inspector.hibernate.validator\"/>";
		xml += "						<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\" config=\"org.metawidget.inspector.impl.BaseObjectInspectorConfig\">";
		xml += "							<propertyStyle>";
		xml += "								<javaBeanPropertyStyle xmlns=\"java:org.metawidget.inspector.impl.propertystyle.javabean\"/>";
		xml += "							</propertyStyle>";
		xml += "							<actionStyle>";
		xml += "								<metawidgetActionStyle xmlns=\"java:org.metawidget.inspector.impl.actionstyle.metawidget\"/>";
		xml += "							</actionStyle>";
		xml += "						</propertyTypeInspector>";
		xml += "						<jpaInspector xmlns=\"java:org.metawidget.inspector.jpa\"/>";
		xml += "						<jspAnnotationInspector xmlns=\"java:org.metawidget.inspector.jsp\"/>";
		xml += "						<springAnnotationInspector xmlns=\"java:org.metawidget.inspector.spring\"/>";
		xml += "						<strutsInspector xmlns=\"java:org.metawidget.inspector.struts\" config=\"StrutsInspectorConfig\">";
		xml += "							<inputStreams>";
		xml += "								<array>";
		xml += "									<resource>org/metawidget/inspector/struts/test-struts-config1.xml</resource>";
		xml += "									<resource>org/metawidget/inspector/struts/test-struts-config2.xml</resource>";
		xml += "								</array>";
		xml += "							</inputStreams>";
		xml += "						</strutsInspector>";
		xml += "						<strutsAnnotationInspector xmlns=\"java:org.metawidget.inspector.struts\"/>";
		xml += "						<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "							<inputStream>";
		xml += "								<resource>org/metawidget/example/swing/addressbook/metawidget-metadata.xml</resource>";
		xml += "							</inputStream>";
		xml += "						</xmlInspector>";
		xml += "						<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "							<inputStream>";
		xml += "								<resource>org/metawidget/example/swing/addressbook/metawidget-metadata.xml</resource>";
		xml += "							</inputStream>";
		xml += "						</xmlInspector>";
		xml += "					</array>";
		xml += "				</inspectors>";
		xml += "			</compositeInspector>";
		xml += "		</inspector>";
		xml += "		<name>";
		xml += "			<string>foo</string>";
		xml += "		</name>";
		xml += "		<opaque>";
		xml += "			<boolean>true</boolean>";
		xml += "		</opaque>";
		xml += "	</swingMetawidget>";
		xml += "	<compositeInspector xmlns=\"java:org.metawidget.inspector.composite\" config=\"CompositeInspectorConfig\">";
		xml += "		<inspectors>";
		xml += "			<array>";
		xml += "				<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\"/>";
		xml += "				<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\"/>";
		xml += "				<java5Inspector xmlns=\"java:org.metawidget.inspector.java5\"/>";
		xml += "			</array>";
		xml += "		</inspectors>";
		xml += "	</compositeInspector>";
		xml += "</metawidget>";

		// New Point

		ConfigReader configReader = new ValidatingConfigReader();
		Point point = configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Point.class );
		assertTrue( 10 == point.x );
		assertTrue( 20 == point.y );

		// Existing Point

		point = new Point();
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), point );
		assertTrue( 10 == point.x );
		assertTrue( 20 == point.y );

		// SwingMetawidget

		SwingMetawidget metawidget1 = new SwingMetawidget();
		assertTrue( null == metawidget1.getName() );
		assertTrue( !metawidget1.isOpaque() );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1 );
		assertTrue( "foo".equals( metawidget1.getName() ) );
		assertTrue( metawidget1.isOpaque() );

		// New SwingMetawidget with names

		metawidget1 = configReader.configure( new ByteArrayInputStream( xml.getBytes() ), SwingMetawidget.class, "name" );
		assertTrue( "foo".equals( metawidget1.getName() ) );
		assertTrue( !metawidget1.isOpaque() );

		// Existing SwingMetawidget with names

		metawidget1.setName( "newFoo" );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1, "opaque" );
		assertTrue( "newFoo".equals( metawidget1.getName() ) );
		assertTrue( metawidget1.isOpaque() );

		// SwingMetawidget2

		SwingMetawidget metawidget2 = new SwingMetawidget();
		assertTrue( null == metawidget2.getName() );
		assertTrue( !metawidget2.isOpaque() );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget2 );

		// Test WidgetBuilder

		Field mixinField = SwingMetawidget.class.getDeclaredField( "mMetawidgetMixin" );
		mixinField.setAccessible( true );
		@SuppressWarnings( "unchecked" )
		MetawidgetMixin<JComponent, SwingMetawidget> mixin1 = (MetawidgetMixin<JComponent, SwingMetawidget>) mixinField.get( metawidget1 );
		@SuppressWarnings( "unchecked" )
		MetawidgetMixin<JComponent, SwingMetawidget> mixin2 = (MetawidgetMixin<JComponent, SwingMetawidget>) mixinField.get( metawidget2 );

		Field widgetBuilderField = BaseMetawidgetMixin.class.getDeclaredField( "mWidgetBuilder" );
		widgetBuilderField.setAccessible( true );

		assertTrue( null == widgetBuilderField.get( mixin1 ) );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1, "widgetBuilder" );

		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilder<JComponent, SwingMetawidget> compositeWidgetBuilder1 = (CompositeWidgetBuilder<JComponent, SwingMetawidget>) widgetBuilderField.get( mixin1 );
		@SuppressWarnings( "unchecked" )
		CompositeWidgetBuilder<JComponent, SwingMetawidget> compositeWidgetBuilder2 = (CompositeWidgetBuilder<JComponent, SwingMetawidget>) widgetBuilderField.get( mixin2 );

		// Will be the same, even though InputStreams

		assertTrue( compositeWidgetBuilder1 == compositeWidgetBuilder2 );
		WidgetBuilder<JComponent, SwingMetawidget>[] widgetBuilders = compositeWidgetBuilder1.getWidgetBuilders();

		assertTrue( widgetBuilders.length == 1 );
		assertTrue( widgetBuilders[0] instanceof SwingWidgetBuilder );

		// Test Inspector

		Field inspectorField = BaseMetawidgetMixin.class.getDeclaredField( "mInspector" );
		inspectorField.setAccessible( true );

		assertTrue( null == inspectorField.get( mixin1 ) );
		configReader.configure( new ByteArrayInputStream( xml.getBytes() ), metawidget1, "inspector" );

		CompositeInspector compositeInspector1 = (CompositeInspector) inspectorField.get( mixin1 );
		CompositeInspector compositeInspector2 = (CompositeInspector) inspectorField.get( mixin1 );

		assertTrue( compositeInspector1 == compositeInspector2 );

		Field inspectorsField = CompositeInspector.class.getDeclaredField( "mInspectors" );
		inspectorsField.setAccessible( true );
		Inspector[] inspectors = (Inspector[]) inspectorsField.get( compositeInspector1 );

		assertTrue( inspectors.length == 11 );
		assertTrue( inspectors[0] instanceof MetawidgetAnnotationInspector );
		assertTrue( inspectors[1] instanceof FacesInspector );
		assertTrue( inspectors[2] instanceof HibernateValidatorInspector );
		assertTrue( inspectors[3] instanceof PropertyTypeInspector );
		assertTrue( inspectors[4] instanceof JpaInspector );
		assertTrue( inspectors[5] instanceof JspAnnotationInspector );
		assertTrue( inspectors[6] instanceof SpringAnnotationInspector );
		assertTrue( inspectors[7] instanceof StrutsInspector );
		assertTrue( inspectors[8] instanceof StrutsAnnotationInspector );
		assertTrue( inspectors[9] instanceof XmlInspector );
		assertTrue( inspectors[10] instanceof XmlInspector );
	}

	public void testNoDefaultConstructor()
		throws Exception
	{
		// With config hint

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\"/>";
		xml += "</metawidget>";

		ConfigReader configReader = new ConfigReader();

		try
		{
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "class org.metawidget.inspector.xml.XmlInspector does not have a default constructor. Did you mean config=\"XmlInspectorConfig\"?".equals( e.getMessage() ) );
		}

		// Without config hint

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<class xmlns=\"java:java.lang\"/>";
		xml += "</metawidget>";

		try
		{
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Class.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "class java.lang.Class does not have a default constructor".equals( e.getMessage() ) );
		}
	}

	public void testBadUrl()
		throws Exception
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "		<inputStream>";
		xml += "			<url>http://foo.nowhere</url>";
		xml += "		</inputStream>";
		xml += "	</xmlInspector>";
		xml += "</metawidget>";

		ConfigReader configReader = new ConfigReader();

		try
		{
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "java.net.UnknownHostException: foo.nowhere".equals( e.getMessage() ) );
		}
	}

	public void testBadFile()
		throws Exception
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget>";
		xml += "	<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "		<inputStream>";
		xml += "			<file>/tmp/no.such.file</file>";
		xml += "		</inputStream>";
		xml += "	</xmlInspector>";
		xml += "</metawidget>";

		ConfigReader configReader = new ConfigReader();

		try
		{
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( e.getMessage().startsWith( "java.io.FileNotFoundException:" ) );
		}
	}

	public void testForgottenConfigAttribute()
		throws Exception
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\">";
		xml += "<propertyStyle><groovyPropertyStyle xmlns=\"java:org.metawidget.inspector.impl.propertystyle.groovy\"/></propertyStyle>";
		xml += "</propertyTypeInspector></metawidget>";

		try
		{
			ConfigReader configReader = new ConfigReader();
			configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "java.lang.NoSuchMethodException: class org.metawidget.inspector.propertytype.PropertyTypeInspector.setPropertyStyle( class org.metawidget.inspector.impl.propertystyle.groovy.GroovyPropertyStyle )".equals( e.getMessage() ) );
		}
	}

	public void testSupportedTypes()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\">";
		xml += "<int><int>3</int></int>";
		xml += "<list>";
		xml += "<list>";
		xml += "<string>foo</string>";
		xml += "<string>bar</string>";
		xml += "<class>java.lang.String</class>";
		xml += "<class>java.util.Date</class>";
		xml += "<class>java.lang.Long</class>";
		xml += "<null/>";
		xml += "</list>";
		xml += "</list>";
		xml += "<set>";
		xml += "<set>";
		xml += "<string>baz</string>";
		xml += "</set>";
		xml += "</set>";
		xml += "<boolean><boolean>true</boolean></boolean>";
		xml += "<pattern><pattern>.*?</pattern></pattern>";
		xml += "<inputStream><resource>org/metawidget/swing/allwidgets/metawidget.xml</resource></inputStream>";
		xml += "<resourceBundle><bundle>org.metawidget.shared.allwidgets.resource.Resources</bundle></resourceBundle>";
		xml += "<stringArray><array><string>foo</string><string>bar</string></array></stringArray>";
		xml += "</testInspector>";
		xml += "</metawidget>";

		TestInspector inspector = new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
		assertTrue( 3 == inspector.getInt() );

		List<Object> list = inspector.getList();
		assertTrue( "foo".equals( list.get( 0 ) ) );
		assertTrue( "bar".equals( list.get( 1 ) ) );
		assertTrue( String.class.equals( list.get( 2 ) ) );
		assertTrue( Date.class.equals( list.get( 3 ) ) );
		assertTrue( Long.class.equals( list.get( 4 ) ) );
		assertTrue( null == list.get( 5 ) );
		assertTrue( 6 == list.size() );

		Set<Object> set = inspector.getSet();
		assertTrue( "baz".equals( set.iterator().next() ) );

		assertTrue( true == inspector.isBoolean() );
		assertTrue( ".*?".equals( inspector.getPattern().toString() ) );

		ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
		IOUtils.streamBetween( inspector.getInputStream(), streamOut );
		assertTrue( streamOut.toString().contains( "<metawidget xmlns=\"http://metawidget.org\"" ) );

		assertTrue( "Limited textbox (i18n)".equals( inspector.getResourceBundle().getString( "limitedTextbox" ) ) );

		assertTrue( 2 == inspector.getStringArray().length );
		assertTrue( "foo".equals( inspector.getStringArray()[0] ) );
		assertTrue( "bar".equals( inspector.getStringArray()[1] ) );
	}

	public void testUnsupportedType()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\">";
		xml += "<date><date>1/1/2001</date></date>";
		xml += "</testInspector>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( e.getMessage().endsWith( "No such class org.metawidget.config.Date or supported tag <date>" ) );
		}
	}

	public void testEmptyCollection()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\">";
		xml += "<list>";
		xml += "<list/>";
		xml += "</list>";
		xml += "<set>";
		xml += "<set/>";
		xml += "</set>";
		xml += "</testInspector>";
		xml += "</metawidget>";

		TestInspector inspector = new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
		assertTrue( inspector.getList().isEmpty() );
		assertTrue( inspector.getSet().isEmpty() );
	}

	public void testMetawidgetExceptionDuringConstruction()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\">";
		xml += "<failDuringConstruction><boolean>true</boolean></failDuringConstruction>";
		xml += "</testInspector>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "Failed during construction".equals( e.getCause().getMessage() ) );
		}
	}

	public void testSetterWithNoParameters()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\">";
		xml += "<noParameters/>";
		xml += "</testInspector>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "java.lang.UnsupportedOperationException: Called setNoParameters".equals( e.getMessage() ) );
		}
	}

	public void testNoInspector()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "No match for class org.metawidget.config.TestInspector within config".equals( e.getMessage() ) );
		}
	}

	public void testMultipleInspectors()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\"/>";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestInspectorConfig\"/>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "Already configured a class org.metawidget.config.TestInspector, ambiguous match with class org.metawidget.config.TestInspector".equals( e.getMessage() ) );
		}
	}

	public void testMissingResource()
	{
		ConfigReader configReader = new ConfigReader();

		try
		{
			configReader.configure( (String) null, null );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "java.io.FileNotFoundException: No resource specified".equals( e.getMessage() ) );
		}

		try
		{
			configReader.configure( "", null );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "java.io.FileNotFoundException: No resource specified".equals( e.getMessage() ) );
		}

		try
		{
			configReader.configure( " ", null );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "java.io.FileNotFoundException: No resource specified".equals( e.getMessage() ) );
		}

		try
		{
			configReader.configure( " foo", null );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "java.io.FileNotFoundException: Unable to locate  foo on CLASSPATH".equals( e.getMessage() ) );
		}
	}

	public void testCaching()
	{
		ValidatingConfigReader configReader = new ValidatingConfigReader();
		configReader.configure( "org/metawidget/swing/allwidgets/metawidget.xml", SwingMetawidget.class );
		configReader.configure( "org/metawidget/swing/allwidgets/metawidget.xml", SwingMetawidget.class );
		configReader.configure( "org/metawidget/swing/allwidgets/metawidget.xml", SwingMetawidget.class );
		configReader.configure( "org/metawidget/gwt/allwidgets/metawidget.xml", Inspector.class );
		configReader.configure( "org/metawidget/gwt/allwidgets/metawidget.xml", Inspector.class );
		configReader.configure( "org/metawidget/gwt/allwidgets/metawidget.xml", Inspector.class );

		// (4 because each metawidget.xml contains a metawidget-metadata.xml)

		assertTrue( 4 == configReader.getOpenedResource() );
	}

	public void testImmutableThreadsafe()
		throws Exception
	{
		// Via InputStream

		String startXml = "<?xml version=\"1.0\"?>";
		startXml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		startXml += "<compositeInspector xmlns=\"java:org.metawidget.inspector.composite\" config=\"CompositeInspectorConfig\">";
		startXml += "<inspectors>";
		startXml += "<array>";
		startXml += "<propertyTypeInspector xmlns=\"java:org.metawidget.inspector.propertytype\"/>";
		startXml += "<metawidgetAnnotationInspector xmlns=\"java:org.metawidget.inspector.annotation\"/>";
		startXml += "<strutsAnnotationInspector xmlns=\"java:org.metawidget.inspector.struts\" config=\"org.metawidget.inspector.impl.BaseObjectInspectorConfig\">";
		startXml += "<propertyStyle><strutsActionFormPropertyStyle xmlns=\"java:org.metawidget.inspector.impl.propertystyle.struts\"/></propertyStyle>";
		startXml += "</strutsAnnotationInspector>";
		startXml += "<springAnnotationInspector xmlns=\"java:org.metawidget.inspector.spring\" config=\"org.metawidget.inspector.impl.BaseObjectInspectorConfig\">";
		startXml += "<propertyStyle><strutsActionFormPropertyStyle xmlns=\"java:org.metawidget.inspector.impl.propertystyle.struts\"/></propertyStyle>";
		startXml += "</springAnnotationInspector>";
		String endXml = "</array>";
		endXml += "</inspectors>";
		endXml += "</compositeInspector>";
		endXml += "</metawidget>";

		String xml = startXml + endXml;

		ConfigReader configReader = new ConfigReader();
		Inspector inspector1 = configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
		Inspector inspector2 = configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );

		// Inspectors should be the same, even though InputStreams are not cached, because cached at
		// Config level

		assertTrue( inspector1 == inspector2 );

		xml = startXml;
		xml += "<xmlInspector xmlns=\"java:org.metawidget.inspector.xml\" config=\"XmlInspectorConfig\">";
		xml += "<inputStream><resource>org/metawidget/example/swing/tutorial/metawidget-metadata.xml</resource></inputStream>";
		xml += "</xmlInspector>";
		xml += endXml;

		inspector1 = configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );
		inspector2 = configReader.configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );

		// Inspectors should not be the same, because caching at Config level should be thwarted by
		// InputStream

		assertTrue( inspector1 != inspector2 );

		Field inspectorsField = CompositeInspector.class.getDeclaredField( "mInspectors" );
		inspectorsField.setAccessible( true );

		Inspector[] inspectors1 = (Inspector[]) inspectorsField.get( inspector1 );
		Inspector[] inspectors2 = (Inspector[]) inspectorsField.get( inspector2 );
		assertTrue( inspectors1 != inspectors2 );

		// Sub-inspectors should be the same, because are cached at the Config level

		assertTrue( inspectors1[0] == inspectors2[0] );
		assertTrue( inspectors1[1] == inspectors2[1] );
		assertTrue( inspectors1[2] == inspectors2[2] );
		assertTrue( inspectors1[3] == inspectors2[3] );

		// PropertyStyle should be shared across Inspectors

		Field propertyStyleField = BaseObjectInspector.class.getDeclaredField( "mPropertyStyle" );
		propertyStyleField.setAccessible( true );

		assertTrue( propertyStyleField.get( inspectors1[0] ) == propertyStyleField.get( inspectors1[1] ) );
		assertTrue( propertyStyleField.get( inspectors2[0] ) == propertyStyleField.get( inspectors2[0] ) );
		assertTrue( propertyStyleField.get( inspectors2[0] ) == propertyStyleField.get( inspectors2[1] ) );

		assertTrue( propertyStyleField.get( inspectors1[2] ) == propertyStyleField.get( inspectors1[3] ) );
		assertTrue( propertyStyleField.get( inspectors1[2] ) == propertyStyleField.get( inspectors2[2] ) );
		assertTrue( propertyStyleField.get( inspectors2[2] ) == propertyStyleField.get( inspectors2[3] ) );

		// Via resource

		SwingMetawidget metawidget1 = configReader.configure( "org/metawidget/swing/allwidgets/metawidget.xml", SwingMetawidget.class );
		SwingMetawidget metawidget2 = configReader.configure( "org/metawidget/swing/allwidgets/metawidget.xml", SwingMetawidget.class );

		assertTrue( metawidget1 != metawidget2 );

		Field mixinField = SwingMetawidget.class.getDeclaredField( "mMetawidgetMixin" );
		mixinField.setAccessible( true );
		@SuppressWarnings( "unchecked" )
		MetawidgetMixin<JComponent, SwingMetawidget> mixin1 = (MetawidgetMixin<JComponent, SwingMetawidget>) mixinField.get( metawidget1 );
		@SuppressWarnings( "unchecked" )
		MetawidgetMixin<JComponent, SwingMetawidget> mixin2 = (MetawidgetMixin<JComponent, SwingMetawidget>) mixinField.get( metawidget2 );
		assertTrue( mixin1 != mixin2 );

		// Inspectors should be the same, because resources are cached even though it contains
		// InputStreams

		assertTrue( mixin1.getInspector() == mixin2.getInspector() );

		// Test what got cached

		Map<Class<?>, Map<Object, Object>> immutableThreadsafeByClassCache = configReader.mImmutableThreadsafeByClassCache;
		assertTrue( !immutableThreadsafeByClassCache.containsKey( Class.class ) );

		Map<Object, Object> immutableThreadsafeByConfigCache = immutableThreadsafeByClassCache.get( CompositeInspector.class );
		assertTrue( !immutableThreadsafeByConfigCache.containsKey( ConfigReader.IMMUTABLE_THREADSAFE_NO_CONFIG ) );
		assertTrue( 4 == immutableThreadsafeByConfigCache.size() );

		immutableThreadsafeByConfigCache = immutableThreadsafeByClassCache.get( StrutsAnnotationInspector.class );
		assertTrue( !immutableThreadsafeByConfigCache.containsKey( ConfigReader.IMMUTABLE_THREADSAFE_NO_CONFIG ) );
		assertTrue( 1 == immutableThreadsafeByConfigCache.size() );
		assertTrue( inspectors1[2] == immutableThreadsafeByConfigCache.values().iterator().next() );

		immutableThreadsafeByConfigCache = immutableThreadsafeByClassCache.get( XmlInspector.class );
		assertTrue( !immutableThreadsafeByConfigCache.containsKey( ConfigReader.IMMUTABLE_THREADSAFE_NO_CONFIG ) );
		assertTrue( 3 == immutableThreadsafeByConfigCache.size() );

		immutableThreadsafeByConfigCache = immutableThreadsafeByClassCache.get( MetawidgetAnnotationInspector.class );
		assertTrue( immutableThreadsafeByConfigCache.containsKey( ConfigReader.IMMUTABLE_THREADSAFE_NO_CONFIG ) );
		assertTrue( 1 == immutableThreadsafeByConfigCache.size() );
		assertTrue( inspectors1[1] == immutableThreadsafeByConfigCache.get( ConfigReader.IMMUTABLE_THREADSAFE_NO_CONFIG ) );

		immutableThreadsafeByConfigCache = immutableThreadsafeByClassCache.get( SpringAnnotationInspector.class );
		assertTrue( !immutableThreadsafeByConfigCache.containsKey( ConfigReader.IMMUTABLE_THREADSAFE_NO_CONFIG ) );
		assertTrue( 1 == immutableThreadsafeByConfigCache.size() );
		assertTrue( inspectors1[3] == immutableThreadsafeByConfigCache.values().iterator().next() );

		immutableThreadsafeByConfigCache = immutableThreadsafeByClassCache.get( PropertyTypeInspector.class );
		assertTrue( immutableThreadsafeByConfigCache.containsKey( ConfigReader.IMMUTABLE_THREADSAFE_NO_CONFIG ) );
		assertTrue( 1 == immutableThreadsafeByConfigCache.size() );
		assertTrue( inspectors1[0] == immutableThreadsafeByConfigCache.get( ConfigReader.IMMUTABLE_THREADSAFE_NO_CONFIG ) );

		immutableThreadsafeByConfigCache = immutableThreadsafeByClassCache.get( StrutsActionFormPropertyStyle.class );
		assertTrue( immutableThreadsafeByConfigCache.containsKey( ConfigReader.IMMUTABLE_THREADSAFE_NO_CONFIG ) );
		assertTrue( 1 == immutableThreadsafeByConfigCache.size() );
		assertTrue( propertyStyleField.get( inspectors1[2] ) == immutableThreadsafeByConfigCache.get( ConfigReader.IMMUTABLE_THREADSAFE_NO_CONFIG ) );

		assertTrue( 7 == immutableThreadsafeByClassCache.size() );
	}

	public void testUppercase()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<TestInspector xmlns=\"java:org.metawidget.config\"/>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "XML node 'TestInspector' should start with a lowercase letter".equals( e.getMessage() ) );
		}
	}

	public void testBadConfigImplementation()
	{
		// No equals

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestNoEqualsInspectorConfig\"/>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "class org.metawidget.config.TestNoEqualsInspectorConfig does not override .equals(), so cannot cache reliably".equals( e.getMessage() ) );
		}

		// No hashCode

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestNoHashCodeInspectorConfig\"/>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "class org.metawidget.config.TestNoHashCodeInspectorConfig does not override .hashCode(), so cannot cache reliably".equals( e.getMessage() ) );
		}

		// Unbalanced

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestUnbalancedEqualsInspectorConfig\"/>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			// TODO: VM bug? assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "class org.metawidget.config.TestNoHashCodeInspectorConfig implements .equals(), but .hashCode() is implemented by class org.metawidget.config.TestUnbalancedEqualsInspectorConfig, so cannot cache reliably".equals( e.getMessage() ) );
		}

		// No such constructor

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"java.lang.String\"/>";
		xml += "</metawidget>";

		try
		{
			new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), TestInspector.class );
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "class org.metawidget.config.TestInspector does not have a constructor that takes a class java.lang.String, as specified by the config attribute".equals( e.getMessage() ) );
		}

		// Superclass does, but subclass doesn't, but no methods

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestNoEqualsSubclassInspectorConfig\"/>";
		xml += "</metawidget>";

		LogUtilsTest.clearLastWarnMessage();

		new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );

		assertTrue( null == LogUtilsTest.getLastWarnMessage() );

		// Superclass does, but subclass doesn't, and has methods

		xml = "<?xml version=\"1.0\"?>";
		xml += "<metawidget xmlns=\"http://metawidget.org\"	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"	xsi:schemaLocation=\"http://metawidget.org http://metawidget.org/xsd/metawidget-1.0.xsd\" version=\"1.0\">";
		xml += "<testInspector xmlns=\"java:org.metawidget.config\" config=\"TestNoEqualsHasMethodsSubclassInspectorConfig\"/>";
		xml += "</metawidget>";

		new ConfigReader().configure( new ByteArrayInputStream( xml.getBytes() ), Inspector.class );

		assertTrue( "class org.metawidget.config.TestNoEqualsHasMethodsSubclassInspectorConfig does not override .equals() (only its superclass org.metawidget.config.TestInspectorConfig does), so may not be cached reliably".equals( LogUtilsTest.getLastWarnMessage() ) );
	}

	//
	// Inner class
	//

	class ValidatingConfigReader
		extends ConfigReader
	{
		//
		// Private members
		//

		private int	mOpenedResource;

		//
		// Constructor
		//

		public ValidatingConfigReader()
		{
			SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			InputStream in = super.openResource( "org/metawidget/config/metawidget-1.0.xsd" );

			try
			{
				mFactory.setSchema( factory.newSchema( new StreamSource( in ) ) );
			}
			catch ( SAXException e )
			{
				throw MetawidgetException.newException( e );
			}
		}

		//
		// Public methods
		//

		@Override
		public InputStream openResource( String resource )
		{
			mOpenedResource++;

			return super.openResource( resource );
		}

		public int getOpenedResource()
		{
			return mOpenedResource;
		}
	}
}