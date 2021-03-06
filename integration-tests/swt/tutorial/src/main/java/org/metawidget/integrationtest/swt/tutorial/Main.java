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

package org.metawidget.integrationtest.swt.tutorial;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.metawidget.swt.SwtMetawidget;

/**
 * @author Stefan Ackermann, Richard Kennard
 */

public class Main {

	public static void main( String[] args ) {

		// Data model

		Person person = new Person();

		// Metawidget

		Display display = new Display();
		Shell shell = new Shell( display );
		shell.setLayout( new MigLayout( new LC().fill().debug( 500 ) ) );

		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.None );
		metawidget.setLayoutData( new CC().height( "200px" ).width( "200px" ) );
		metawidget.setToInspect( person );

		// Shell

		shell.setVisible( true );
		shell.open();

		while ( !shell.isDisposed() ) {
			if ( !display.readAndDispatch() ) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
