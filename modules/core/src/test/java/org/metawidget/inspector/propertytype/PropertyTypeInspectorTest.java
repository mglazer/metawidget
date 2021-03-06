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

package org.metawidget.inspector.propertytype;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.*;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.ClassUtilsTest;
import org.metawidget.util.ClassUtilsTest.AlienClassLoader;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtilsTest;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class PropertyTypeInspectorTest
	extends TestCase {

	//
	// Private members
	//

	private Inspector	mInspector;

	//
	// Public methods
	//

	public void testInspection() {

		PersonalContact personalContact = new PersonalContact();
		Document document = XmlUtils.documentFromString( mInspector.inspect( personalContact, PersonalContact.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( PersonalContact.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties (should be sorted alphabetically)

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "address", property.getAttribute( NAME ) );
		assertEquals( Address.class.getName(), property.getAttribute( TYPE ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "communications", property.getAttribute( NAME ) );
		assertEquals( Set.class.getName(), property.getAttribute( TYPE ) );

		// Test declared-class

		DeclaredTypeTester tester = new DeclaredTypeTester();
		tester.value = personalContact;

		document = XmlUtils.documentFromString( mInspector.inspect( tester, DeclaredTypeTester.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( DeclaredTypeTester.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "fOO", property.getAttribute( NAME ) );
		assertEquals( Object.class.getName(), property.getAttribute( TYPE ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "foo", property.getAttribute( NAME ) );
		assertEquals( Object.class.getName(), property.getAttribute( TYPE ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "value", property.getAttribute( NAME ) );
		assertEquals( PersonalContact.class.getName(), property.getAttribute( ACTUAL_CLASS ) );
		assertEquals( Contact.class.getName(), property.getAttribute( TYPE ) );
	}

	public void testInspectString() {

		// Test pointed directly at an actual String

		String xml = mInspector.inspect( "foo", String.class.getName() );
		Document document = XmlUtils.documentFromString( xml );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( String.class.getName(), entity.getAttribute( TYPE ) );
		assertTrue( 1 == entity.getAttributes().getLength() );
		assertTrue( 0 == entity.getChildNodes().getLength() );

		// Test pointed directly at a null String

		xml = mInspector.inspect( null, String.class.getName() );
		document = XmlUtils.documentFromString( xml );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( String.class.getName(), entity.getAttribute( TYPE ) );
		assertTrue( 1 == entity.getAttributes().getLength() );
		assertTrue( 0 == entity.getChildNodes().getLength() );

		// Test pointed indirectly at an actual String

		StringHolder stringHolder = new StringHolder();
		stringHolder.string = "foo";
		xml = mInspector.inspect( stringHolder, StringHolder.class.getName(), "string" );
		document = XmlUtils.documentFromString( xml );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( String.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "string", entity.getAttribute( NAME ) );
		assertTrue( 2 == entity.getAttributes().getLength() );
		assertTrue( 0 == entity.getChildNodes().getLength() );

		// Test pointed indirectly at a null String

		stringHolder.string = null;
		xml = mInspector.inspect( stringHolder, StringHolder.class.getName(), "string" );
		document = XmlUtils.documentFromString( xml );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( String.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "string", entity.getAttribute( NAME ) );
		assertTrue( 2 == entity.getAttributes().getLength() );
		assertTrue( 0 == entity.getChildNodes().getLength() );
	}

	public void testTraverseViaParent() {

		// Indirectly pointed at a not null complex type

		DeclaredTypeTester tester = new DeclaredTypeTester();
		tester.value = new PersonalContact();
		String xml = mInspector.inspect( tester, DeclaredTypeTester.class.getName(), "value" );
		Document document = XmlUtils.documentFromString( xml );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Contact.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "value", entity.getAttribute( NAME ) );
		assertEquals( PersonalContact.class.getName(), entity.getAttribute( ACTUAL_CLASS ) );
		assertTrue( 3 == entity.getAttributes().getLength() );

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "address" );
		assertEquals( PROPERTY, property.getNodeName() );

		// Indirectly pointed at a null complex type

		tester.value = null;
		xml = mInspector.inspect( tester, DeclaredTypeTester.class.getName(), "value" );
		document = XmlUtils.documentFromString( xml );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Contact.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "value", entity.getAttribute( NAME ) );
		assertTrue( 2 == entity.getAttributes().getLength() );

		// (should be no children because value was null)

		assertTrue( 0 == entity.getChildNodes().getLength() );
	}

	public void testCovariantReturn() {

		// Superclass

		Document document = XmlUtils.documentFromString( mInspector.inspect( new SuperFoo(), SuperFoo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );

		assertEquals( SuperFoo.class.getName(), entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "contact", property.getAttribute( NAME ) );
		assertEquals( Contact.class.getName(), property.getAttribute( TYPE ) );

		// Subclass

		document = XmlUtils.documentFromString( mInspector.inspect( new SubFoo(), SubFoo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( SubFoo.class.getName(), entity.getAttribute( TYPE ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( NO_GETTER ) );
		assertEquals( String.class.getName(), property.getAttribute( TYPE ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "contact", property.getAttribute( NAME ) );
		assertEquals( PersonalContact.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( TRUE, property.getAttribute( NO_SETTER ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		// Check there are no more properties (eg. public static int notVisible)

		assertEquals( property.getNextSibling(), null );
	}

	public void testCovariantSetter() {

		Document document = XmlUtils.documentFromString( mInspector.inspect( new SubFoo2(), SubFoo2.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( SubFoo2.class.getName(), entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "contact", property.getAttribute( NAME ) );
		assertEquals( PersonalContact.class.getName(), property.getAttribute( TYPE ) );
		assertTrue( 2 == property.getAttributes().getLength() );

		// Check there are no more properties (eg. public static int notVisible)

		assertEquals( property.getNextSibling(), null );
	}

	public void testInfiniteRecursion() {

		RecursiveFoo recursiveFoo = new RecursiveFoo();
		recursiveFoo.setFoo( recursiveFoo );

		// Top level

		Document document = XmlUtils.documentFromString( mInspector.inspect( recursiveFoo, RecursiveFoo.class.getName() ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( RecursiveFoo.class.getName(), entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "foo", property.getAttribute( NAME ) );
		assertEquals( RecursiveFoo.class.getName(), property.getAttribute( ACTUAL_CLASS ) );
		assertEquals( Object.class.getName(), property.getAttribute( TYPE ) );
		assertTrue( 3 == property.getAttributes().getLength() );
		assertEquals( property.getNextSibling(), null );

		// Second level (should block)

		assertEquals( mInspector.inspect( recursiveFoo, RecursiveFoo.class.getName(), "foo" ), null );

		if ( LogUtils.getLog( PropertyTypeInspector.class ).isTraceEnabled() ) {
			assertEquals( "PropertyTypeInspector prevented infinite recursion on org.metawidget.inspector.propertytype.PropertyTypeInspectorTest$RecursiveFoo/foo. Consider annotating foo as @UiHidden", LogUtilsTest.getLastTraceMessage() );
		} else {
			assertEquals( "Prevented infinite recursion on {0}{1}. Consider marking {2} as hidden", LogUtilsTest.getLastTraceMessage() );
			assertEquals( "org.metawidget.inspector.propertytype.PropertyTypeInspectorTest$RecursiveFoo", LogUtilsTest.getLastTraceArguments()[0] );
			assertEquals( "/foo", LogUtilsTest.getLastTraceArguments()[1] );
			assertEquals( "foo", LogUtilsTest.getLastTraceArguments()[2] );
		}

		// Start over

		RecursiveFoo recursiveFoo2 = new RecursiveFoo();
		recursiveFoo.setFoo( recursiveFoo2 );
		recursiveFoo2.setFoo( recursiveFoo );

		// Second level

		document = XmlUtils.documentFromString( mInspector.inspect( recursiveFoo, RecursiveFoo.class.getName(), "foo" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Object.class.getName(), entity.getAttribute( TYPE ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "foo", property.getAttribute( NAME ) );
		assertTrue( 3 == property.getAttributes().getLength() );
		assertEquals( RecursiveFoo.class.getName(), property.getAttribute( ACTUAL_CLASS ) );
		assertEquals( Object.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( property.getNextSibling(), null );

		// Third level (should block)

		assertEquals( mInspector.inspect( recursiveFoo, RecursiveFoo.class.getName(), "foo", "foo" ), null );
	}

	public void testBadName() {

		assertTrue( mInspector.inspect( new SubFoo(), "no-such-type" ) != null );
		assertEquals( mInspector.inspect( new SubFoo(), SubFoo.class.getName(), "no-such-name" ), null );
		assertEquals( mInspector.inspect( new SubFoo(), SubFoo.class.getName(), "no-such-parent-name", "foo" ), null );
	}

	public void testNullType() {

		assertTrue( null == mInspector.inspect( null, null ) );
	}

	public void testBoolean() {

		// Boolean (big 'b')

		Document document = XmlUtils.documentFromString( mInspector.inspect( null, Boolean.class.getName() ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Boolean.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "true, false", entity.getAttribute( LOOKUP ) );
		assertEquals( "Yes, No", entity.getAttribute( LOOKUP_LABELS ) );
		assertTrue( 3 == entity.getAttributes().getLength() );

		// boolean (little 'b')

		document = XmlUtils.documentFromString( mInspector.inspect( null, boolean.class.getName() ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( boolean.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( LOOKUP ) );
		assertTrue( 1 == entity.getAttributes().getLength() );

		// boolean with a value

		document = XmlUtils.documentFromString( mInspector.inspect( new BooleanHolder(), BooleanHolder.class.getName() ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "littleBoolean" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( boolean.class.getName(), property.getAttribute( TYPE ) );
		assertFalse( property.hasAttribute( LOOKUP ) );
		assertTrue( 2 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bigBoolean" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( Boolean.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( "true, false", property.getAttribute( LOOKUP ) );
		assertEquals( "Yes, No", property.getAttribute( LOOKUP_LABELS ) );
		assertTrue( 4 == property.getAttributes().getLength() );

		// Pointed directly at a boolean (little 'b')

		document = XmlUtils.documentFromString( mInspector.inspect( new BooleanHolder(), BooleanHolder.class.getName(), "littleBoolean" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "littleBoolean", entity.getAttribute( NAME ) );
		assertEquals( boolean.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( LOOKUP ) );
		assertFalse( entity.hasAttribute( LOOKUP_LABELS ) );
		assertEquals( 2, entity.getAttributes().getLength() );
	}

	public void testTraverseActualTypes() {

		DeclaredTypeTester test = new DeclaredTypeTester();
		test.foo = new DeclaredTypeTester();
		( (DeclaredTypeTester) test.foo ).foo = new DeclaredTypeTester();

		// Traversal from foo to foo.foo will fail if PropertyTypeInspector is using
		// the declared type (java.lang.Object) instead of the actual type (DeclaredTypeTester)

		Document document = XmlUtils.documentFromString( mInspector.inspect( test, test.getClass().getName(), "foo", "foo" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "foo", entity.getAttribute( NAME ) );
		assertEquals( Object.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( DeclaredTypeTester.class.getName(), entity.getAttribute( ACTUAL_CLASS ) );

		// Traversal to a null property can still return information about the properties type

		document = XmlUtils.documentFromString( mInspector.inspect( test, test.getClass().getName(), "foo", "foo", "foo" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "foo", entity.getAttribute( NAME ) );
		assertEquals( Object.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( ACTUAL_CLASS ) );

		// Traversal any further should fail gracefully (ie. not NullPointerException)

		assertTrue( null == mInspector.inspect( test, test.getClass().getName(), "foo", "foo", "foo", "foo" ) );
		assertTrue( null == mInspector.inspect( test, test.getClass().getName(), "foo", "foo", "foo", "foo", "foo" ) );
	}

	/**
	 * Test BaseObjectInspector under high concurrency.
	 */

	public void testConcurrency()
		throws Exception {

		final List<Exception> concurrencyFailures = CollectionUtils.newArrayList();

		// Try a few times (just to make sure)...

		for ( int tryAFewTimes = 0; tryAFewTimes < 10; tryAFewTimes++ ) {

			// ...prepare some Threads...

			final CountDownLatch startSignal = new CountDownLatch( 1 );
			final CountDownLatch doneSignal = new CountDownLatch( 50 );

			for ( int concurrentThreads = 0; concurrentThreads < doneSignal.getCount(); concurrentThreads++ ) {

				new Thread( new Runnable() {

					public void run() {

						try {
							startSignal.await();
						} catch ( InterruptedException e ) {
							// (do nothing)
						}

						try {
							testInspection();
						} catch ( Exception e ) {
							concurrencyFailures.add( e );
							assertTrue( "Concurrency failure: " + e.getClass() + " " + e.getMessage(), false );
						} finally {
							doneSignal.countDown();
						}
					}
				} ).start();
			}

			// ...and run them all simultaneously

			startSignal.countDown();
			doneSignal.await();

			if ( !concurrencyFailures.isEmpty() ) {
				break;
			}
		}

		assertTrue( concurrencyFailures.isEmpty() );
	}

	public void testAbortTraversingPastNull() {

		Document document = XmlUtils.documentFromString( mInspector.inspect( new TraversePastNullTester(), TraversePastNullTester.class.getName(), "contact" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "contact", entity.getAttribute( NAME ) );
		assertEquals( PersonalContact.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( 2, entity.getAttributes().getLength() );

		// Should not return any children, because abortTraversingPastNull = true

		assertEquals( 0, entity.getChildNodes().getLength() );
	}

	/**
	 * Test a WidgetBuilder (or something after the Inspector) trying to determine the type of an
	 * alien class.
	 */

	public void testTraverseAlienClass()
		throws Exception {

		ClassLoader alienClassLoader = new AlienClassLoader();

		// Entity-level

		AlienClassHolder alienClassHolder = new AlienClassHolder();
		alienClassHolder.set = (Set<?>) alienClassLoader.loadClass( "org.metawidget.util.AlienSet" ).newInstance();

		try {
			assertTrue( null == ClassUtils.niceForName( "org.metawidget.util.AlienSet" ) );
			mInspector.inspect( alienClassHolder.set, "org.metawidget.util.AlienSet" );
			assertTrue( Set.class.isAssignableFrom( ClassUtils.niceForName( "org.metawidget.util.AlienSet" ) ) );
		} finally {
			ClassUtilsTest.unregisterAllAlienClassLoaders();
		}

		// Property-level

		try {
			assertTrue( null == ClassUtils.niceForName( "org.metawidget.util.AlienSet" ) );
			mInspector.inspect( alienClassHolder, AlienClassHolder.class.getName(), "set" );
			assertTrue( Set.class.isAssignableFrom( ClassUtils.niceForName( "org.metawidget.util.AlienSet" ) ) );
		} finally {
			ClassUtilsTest.unregisterAllAlienClassLoaders();
		}
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp() {

		mInspector = new PropertyTypeInspector( new BaseObjectInspectorConfig().setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) ) );
	}

	//
	// Inner classes
	//

	protected static class PersonalContact
		extends Contact {

		public Address getAddress() {

			return null;
		}

		public Set<?> getCommunications() {

			return null;
		}
	}

	protected static class Contact {

		// For testing 'is a' relationship
	}

	protected static class Address {

		// For testing 'has a' relationship
	}

	protected static class DeclaredTypeTester {

		public Object	foo;

		public Object	fOO;

		public Contact	value;
	}

	protected static class SuperFoo {

		public Contact getContact() {

			return null;
		}

		protected String getShouldntFindMe() {

			return null;
		}

		@SuppressWarnings( "unused" )
		private String getShouldntFindMeEither() {

			return null;
		}
	}

	protected static class SubFoo
		extends SuperFoo {

		public static final int	notVisible	= 0;

		//
		// Public methods
		//

		@Override
		public PersonalContact getContact() {

			return null;
		}

		public final PropertyChangeListener[] getPropertyChangeListeners() {

			return null;
		}

		public final VetoableChangeListener[] getVetoableChangeListeners() {

			return null;
		}

		/**
		 * @param bar
		 */

		public void setBar( String bar ) {

			// Do nothing
		}
	}

	protected static class SubFoo2
		extends SuperFoo {

		//
		// Public methods
		//

		@Override
		public PersonalContact getContact() {

			return null;
		}

		/**
		 * @param contact
		 */

		public void setContact( Contact contact ) {

			// Do nothing
		}
	}

	public static class RecursiveFoo {

		private Object	mFoo;

		public Object getFoo() {

			return mFoo;
		}

		public void setFoo( Object foo ) {

			mFoo = foo;
		}
	}

	public static class StringHolder {

		public String	string;
	}

	public static class BooleanHolder {

		public boolean	littleBoolean;

		public Boolean	bigBoolean;
	}

	protected static class TraversePastNullTester {

		public PersonalContact	contact;
	}

	public static class AlienClassHolder {

		public Set<?>	set;
	}
}
