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

package org.metawidget.integrationtest.android.allwidgets;

import java.text.DateFormat;
import java.text.ParseException;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets;
import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.integrationtest.shared.allwidgets.proxy.AllWidgetsProxy.AllWidgets_$$_javassist_1;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.LogUtils;
import org.metawidget.util.simple.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;

/**
 * @author Richard Kennard
 */

public class AllWidgetsActivity
	extends Activity {

	//
	// Protected members
	//

	protected AllWidgets	mAllWidgets;

	protected DateFormat	mFormat;

	//
	// Public methods
	//

	@Override
	public void onCreate( Bundle bundle ) {

		super.onCreate( bundle );

		mAllWidgets = new AllWidgets_$$_javassist_1();

		mFormat = DateFormat.getDateInstance( DateFormat.SHORT );
		mFormat.setLenient( false );

		// Layout from resource

		setContentView( R.layout.main );

		// Metawidget

		AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );
		metawidget.setToInspect( mAllWidgets );

		mapToMetawidget();
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {

		super.onCreateOptionsMenu( menu );

		menu.add( "Save" );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {

		if ( item.getItemId() == 0 ) {
			AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );

			try {
				// Already saved?

				if ( metawidget.isReadOnly() ) {
					return false;
				}

				// Save

				mapFromMetawidget();

				// Show result

				metawidget.setReadOnly( true );
				mapToMetawidget();

				// New, read-only View will be a lot shorter

				( (ScrollView) metawidget.getParent() ).fullScroll( View.FOCUS_UP );
			} catch ( Exception e ) {
				LogUtils.getLog( AllWidgetsActivity.class ).error( "Save error", e );

				String message = e.getMessage();

				if ( message == null || "".equals( message ) ) {
					message = e.getClass().getSimpleName();
				}

				AlertDialog.Builder builder = new AlertDialog.Builder( metawidget.getContext() );
				builder.setTitle( "Save error" );
				builder.setMessage( "Unable to save:\n" + message );
				builder.setPositiveButton( "OK", null );
				builder.show();
			}
		}

		return false;
	}

	//
	// Private methods
	//

	/**
	 * Manual mapping to Metawidget.
	 */

	private void mapToMetawidget() {

		AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );

		metawidget.setValue( mAllWidgets.getTextbox(), "textbox" );
		metawidget.setValue( mAllWidgets.getLimitedTextbox(), "limitedTextbox" );
		metawidget.setValue( mAllWidgets.getTextarea(), "textarea" );
		metawidget.setValue( mAllWidgets.getPassword(), "password" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getBytePrimitive() ), "bytePrimitive" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getByteObject() ), "byteObject" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getShortPrimitive() ), "shortPrimitive" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getShortObject() ), "shortObject" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getIntPrimitive() ), "intPrimitive" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getIntegerObject() ), "integerObject" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getRangedInt() ), "rangedInt" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getRangedInteger() ), "rangedInteger" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getLongPrimitive() ), "longPrimitive" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getLongObject() ), "longObject" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getFloatPrimitive() ), "floatPrimitive" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getFloatObject() ), "floatObject" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getDoublePrimitive() ), "doublePrimitive" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getDoubleObject() ), "doubleObject" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getCharPrimitive() ), "charPrimitive" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getCharacterObject() ), "characterObject" );
		metawidget.setValue( mAllWidgets.isBooleanPrimitive(), "booleanPrimitive" );
		metawidget.setValue( mAllWidgets.getBooleanObject(), "booleanObject" );
		metawidget.setValue( mAllWidgets.getDropdown(), "dropdown" );
		metawidget.setValue( mAllWidgets.getDropdownWithLabels(), "dropdownWithLabels" );
		metawidget.setValue( mAllWidgets.getNotNullDropdown(), "notNullDropdown" );
		metawidget.setValue( mAllWidgets.getNotNullObjectDropdown(), "notNullObjectDropdown" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getNestedWidgets().getFurtherNestedWidgets().getNestedTextbox1() ), "nestedWidgets", "furtherNestedWidgets", "nestedTextbox1" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getNestedWidgets().getFurtherNestedWidgets().getNestedTextbox2() ), "nestedWidgets", "furtherNestedWidgets", "nestedTextbox2" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getNestedWidgets().getNestedTextbox1() ), "nestedWidgets", "nestedTextbox1" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getNestedWidgets().getNestedTextbox2() ), "nestedWidgets", "nestedTextbox2" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getReadOnlyNestedWidgets().getNestedTextbox1() ), "readOnlyNestedWidgets", "nestedTextbox1" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getReadOnlyNestedWidgets().getNestedTextbox2() ), "readOnlyNestedWidgets", "nestedTextbox2" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getNestedWidgetsDontExpand() ), "nestedWidgetsDontExpand" );
		metawidget.setValue( StringUtils.quietValueOf( mAllWidgets.getReadOnlyNestedWidgetsDontExpand() ), "readOnlyNestedWidgetsDontExpand" );
		synchronized ( mFormat ) {
			metawidget.setValue( mFormat.format( mAllWidgets.getDate() ), "date" );
		}
		metawidget.setValue( mAllWidgets.getReadOnly(), "readOnly" );
	}

	/**
	 * Manual mapping from Metawidget.
	 */

	private void mapFromMetawidget()
		throws ParseException {

		AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );

		mAllWidgets.setTextbox( (String) metawidget.getValue( "textbox" ) );
		mAllWidgets.setLimitedTextbox( (String) metawidget.getValue( "limitedTextbox" ) );
		mAllWidgets.setTextarea( (String) metawidget.getValue( "textarea" ) );
		mAllWidgets.setPassword( (String) metawidget.getValue( "password" ) );
		mAllWidgets.setBytePrimitive( Byte.parseByte( (String) metawidget.getValue( "bytePrimitive" ) ) );

		String byteObject = metawidget.getValue( "byteObject" );

		if ( byteObject == null || "".equals( byteObject ) ) {
			mAllWidgets.setByteObject( null );
		} else {
			mAllWidgets.setByteObject( Byte.valueOf( byteObject ) );
		}

		mAllWidgets.setShortPrimitive( Short.parseShort( (String) metawidget.getValue( "shortPrimitive" ) ) );

		String shortObject = metawidget.getValue( "shortObject" );

		if ( shortObject == null || "".equals( shortObject ) ) {
			mAllWidgets.setShortObject( null );
		} else {
			mAllWidgets.setShortObject( Short.valueOf( shortObject ) );
		}

		mAllWidgets.setIntPrimitive( Integer.parseInt( (String) metawidget.getValue( "intPrimitive" ) ) );

		String integerObject = metawidget.getValue( "integerObject" );

		if ( integerObject == null || "".equals( integerObject ) ) {
			mAllWidgets.setIntegerObject( null );
		} else {
			mAllWidgets.setIntegerObject( Integer.valueOf( integerObject ) );
		}

		mAllWidgets.setRangedInt( Integer.parseInt( (String) metawidget.getValue( "rangedInt" ) ) );

		String rangedInteger = metawidget.getValue( "rangedInteger" );

		if ( rangedInteger == null || "".equals( rangedInteger ) ) {
			mAllWidgets.setRangedInteger( null );
		} else {
			mAllWidgets.setRangedInteger( Integer.valueOf( rangedInteger ) );
		}

		mAllWidgets.setLongPrimitive( Long.parseLong( (String) metawidget.getValue( "longPrimitive" ) ) );

		String longObject = metawidget.getValue( "longObject" );

		if ( longObject == null || "".equals( longObject ) ) {
			mAllWidgets.setLongObject( null );
		} else {
			mAllWidgets.setLongObject( Long.valueOf( longObject ) );
		}

		mAllWidgets.setFloatPrimitive( Float.parseFloat( (String) metawidget.getValue( "floatPrimitive" ) ) );

		String floatObject = metawidget.getValue( "floatObject" );

		if ( floatObject == null || "".equals( floatObject ) ) {
			mAllWidgets.setFloatObject( null );
		} else {
			mAllWidgets.setFloatObject( Float.valueOf( floatObject ) );
		}

		mAllWidgets.setDoublePrimitive( Double.parseDouble( (String) metawidget.getValue( "doublePrimitive" ) ) );

		String doubleObject = metawidget.getValue( "doubleObject" );

		if ( doubleObject == null || "".equals( doubleObject ) ) {
			mAllWidgets.setDoubleObject( null );
		} else {
			mAllWidgets.setDoubleObject( Double.valueOf( doubleObject ) );
		}

		mAllWidgets.setCharPrimitive( ( (String) metawidget.getValue( "charPrimitive" ) ).charAt( 0 ) );
		mAllWidgets.setCharacterObject( ( (String) metawidget.getValue( "characterObject" ) ).charAt( 0 ) );

		mAllWidgets.setBooleanPrimitive( (Boolean) metawidget.getValue( "booleanPrimitive" ) );

		String booleanObject = metawidget.getValue( "booleanObject" );

		if ( booleanObject == null || "".equals( booleanObject ) ) {
			mAllWidgets.setBooleanObject( null );
		} else {
			mAllWidgets.setBooleanObject( Boolean.valueOf( booleanObject ) );
		}

		mAllWidgets.setDropdown( (String) metawidget.getValue( "dropdown" ) );
		mAllWidgets.setDropdownWithLabels( (String) metawidget.getValue( "dropdownWithLabels" ) );
		mAllWidgets.setNotNullDropdown( Byte.parseByte( (String) metawidget.getValue( "notNullDropdown" ) ) );
		mAllWidgets.setNotNullObjectDropdown( (String) metawidget.getValue( "notNullObjectDropdown" ) );
		mAllWidgets.getNestedWidgets().getFurtherNestedWidgets().setNestedTextbox1( (String) metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox1" ) );
		mAllWidgets.getNestedWidgets().getFurtherNestedWidgets().setNestedTextbox2( (String) metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox2" ) );
		mAllWidgets.getNestedWidgets().setNestedTextbox1( (String) metawidget.getValue( "nestedWidgets", "nestedTextbox1" ) );
		mAllWidgets.getNestedWidgets().setNestedTextbox2( (String) metawidget.getValue( "nestedWidgets", "nestedTextbox2" ) );
		mAllWidgets.getNestedWidgets().setNestedTextbox1( (String) metawidget.getValue( "nestedWidgets", "nestedTextbox1" ) );
		mAllWidgets.getNestedWidgets().setNestedTextbox2( (String) metawidget.getValue( "nestedWidgets", "nestedTextbox2" ) );

		String nestedWidgetsDontExpandString = metawidget.getValue( "nestedWidgetsDontExpand" );
		String[] values = ArrayUtils.fromString( nestedWidgetsDontExpandString );

		if ( values.length != 0 ) {
			NestedWidgets nestedWidgetsDontExpand = new NestedWidgets();
			nestedWidgetsDontExpand.setNestedTextbox1( values[0] );

			if ( values.length > 1 ) {
				nestedWidgetsDontExpand.setNestedTextbox2( values[1] );
			}

			mAllWidgets.setNestedWidgetsDontExpand( nestedWidgetsDontExpand );
		}

		String date = metawidget.getValue( "date" );

		if ( date == null || "".equals( date ) ) {
			mAllWidgets.setDate( null );
		} else {
			mAllWidgets.setDate( mFormat.parse( date ) );
		}

		mAllWidgets.setReadOnly( (String) metawidget.getValue( "readOnly" ) );
	}
}
