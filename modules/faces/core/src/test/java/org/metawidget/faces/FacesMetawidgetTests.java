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

package org.metawidget.faces;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.render.RenderKit;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;

import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

@SuppressWarnings( "all" )
public class FacesMetawidgetTests {

	//
	// Inner class
	//

	public static class MockPageContext
		extends PageContext {

		//
		// Private members
		//

		Map<String, Object>	mAttributes	= CollectionUtils.newHashMap();

		//
		// Supported public methods
		//

		@Override
		public ServletContext getServletContext() {

			return null;
		}

		@Override
		public Object getAttribute( String name ) {

			return mAttributes.get( name );
		}

		@Override
		public void setAttribute( String name, Object value ) {

			mAttributes.put( name, value );
		}

		//
		// Unsupported public methods
		//

		@Override
		public void forward( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public Exception getException() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Object getPage() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ServletRequest getRequest() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ServletResponse getResponse() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ServletConfig getServletConfig() {

			throw new UnsupportedOperationException();
		}

		@Override
		public HttpSession getSession() {

			return null;
		}

		@Override
		public void handlePageException( Exception arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void handlePageException( Throwable arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void include( String arg0, boolean arg1 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void include( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void initialize( Servlet arg0, ServletRequest arg1, ServletResponse arg2, String arg3, boolean arg4, int arg5, boolean arg6 )
			throws IllegalStateException, IllegalArgumentException {

			throw new UnsupportedOperationException();
		}

		@Override
		public void release() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Object findAttribute( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public Object getAttribute( String name, int index ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public Enumeration getAttributeNamesInScope( int arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public int getAttributesScope( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public ExpressionEvaluator getExpressionEvaluator() {

			throw new UnsupportedOperationException();
		}

		@Override
		public JspWriter getOut() {

			throw new UnsupportedOperationException();
		}

		@Override
		public void removeAttribute( String arg0, int arg1 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void removeAttribute( String arg0 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void setAttribute( String arg0, Object arg1, int arg2 ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public ELContext getELContext() {

			throw new UnsupportedOperationException();
		}

		@Override
		public javax.servlet.jsp.el.VariableResolver getVariableResolver() {

			throw new UnsupportedOperationException();
		}
	}

	public static class MockFacesContext
		extends FacesContext {

		//
		// Protected members
		//

		protected Map<String, Object>	mApplicationMap	= CollectionUtils.newHashMap();

		//
		// Private members
		//

		private ExternalContext			mExternalContext;

		//
		// Constructor
		//

		public MockFacesContext() {

			FacesContext.setCurrentInstance( this );

			// We generally unit test JSF 1.x, and use webtest for JSF 2.x

			@SuppressWarnings( "unchecked" )
			Map<String, String> initParameterMap = getExternalContext().getInitParameterMap();
			initParameterMap.put( "org.metawidget.faces.component.DONT_USE_PRERENDER_VIEW_EVENT", "true" );
		}

		//
		// Public methods
		//

		public void unregisterCurrentInstance() {

			FacesContext.setCurrentInstance( null );
		}

		//
		// Supported public methods
		//

		@Override
		public Application getApplication() {

			return new Application() {

				//
				// Supported public methods
				//

				@Override
				public UIComponent createComponent( String componentType )
					throws FacesException {

					return MockFacesContext.this.createComponent( componentType );
				}

				public UIComponent createComponent( FacesContext context, String componentType, String rendererType ) {

					return MockFacesContext.this.createComponent( componentType );
				}

				@Override
				public ValueBinding createValueBinding( String expressionString )
					throws ReferenceSyntaxException {

					return new MockValueBinding( expressionString );
				}

				@Override
				public Validator createValidator( String validatorName )
					throws FacesException {

					if ( "javax.faces.LongRange".equals( validatorName ) ) {
						return new LongRangeValidator();
					}

					if ( "javax.faces.DoubleRange".equals( validatorName ) ) {
						return new DoubleRangeValidator();
					}

					if ( "javax.faces.Length".equals( validatorName ) ) {
						return new LengthValidator();
					}

					throw new UnsupportedOperationException( "Unknown validator '" + validatorName + "'" );
				}

				//
				// Unsupported public methods
				//

				@Override
				public void addComponent( String s, String s1 ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public ExpressionFactory getExpressionFactory() {

					return new ExpressionFactory() {

						@Override
						public Object coerceToType( Object arg0, Class<?> arg1 )
							throws ELException {

							throw new UnsupportedOperationException();
						}

						@Override
						public MethodExpression createMethodExpression( ELContext elContext, final String expression, Class<?> returnType, Class<?>[] parameters )
							throws ELException, NullPointerException {

							return new MethodExpression() {

								@Override
								public MethodInfo getMethodInfo( ELContext arg0 )
									throws NullPointerException, javax.el.PropertyNotFoundException, javax.el.MethodNotFoundException, ELException {

									throw new UnsupportedOperationException();
								}

								@Override
								public Object invoke( ELContext arg0, Object[] arg1 )
									throws NullPointerException, javax.el.PropertyNotFoundException, javax.el.MethodNotFoundException, ELException {

									throw new UnsupportedOperationException();
								}

								@Override
								public boolean equals( Object arg0 ) {

									throw new UnsupportedOperationException();
								}

								@Override
								public String getExpressionString() {

									return expression;
								}

								@Override
								public int hashCode() {

									throw new UnsupportedOperationException();
								}

								@Override
								public boolean isLiteralText() {

									throw new UnsupportedOperationException();
								}
							};
						}

						@Override
						public ValueExpression createValueExpression( Object arg0, Class<?> arg1 ) {

							throw new NoSuchMethodError( "MockFacesContext mimics JSF 1.1" );
						}

						@Override
						public ValueExpression createValueExpression( ELContext elContext, String arg1, Class<?> arg2 )
							throws NullPointerException, ELException {

							throw new NoSuchMethodError( "MockFacesContext mimics JSF 1.1" );
						}
					};
				}

				@Override
				public void addConverter( String s, String s1 ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void addConverter( Class class1, String s ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void addValidator( String s, String s1 ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public UIComponent createComponent( ValueBinding valuebinding, FacesContext facescontext, String s )
					throws FacesException {

					throw new UnsupportedOperationException();
				}

				@Override
				public Converter createConverter( final String converterId ) {

					return new Converter() {

						public Object getAsObject( FacesContext context, UIComponent component, String value ) {

							return value;
						}

						public String getAsString( FacesContext context, UIComponent component, Object value ) {

							return value + " (from converter " + converterId + ")";
						}
					};
				}

				@Override
				public Converter createConverter( Class class1 ) {

					return null;
				}

				@Override
				public MethodBinding createMethodBinding( String expression, Class[] params )
					throws ReferenceSyntaxException {

					return new MockMethodBinding( expression, params );
				}

				@Override
				public ActionListener getActionListener() {

					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<String> getComponentTypes() {

					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<String> getConverterIds() {

					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<Class<?>> getConverterTypes() {

					throw new UnsupportedOperationException();
				}

				@Override
				public Locale getDefaultLocale() {

					throw new UnsupportedOperationException();
				}

				@Override
				public String getDefaultRenderKitId() {

					throw new UnsupportedOperationException();
				}

				@Override
				public String getMessageBundle() {

					return null;
				}

				@Override
				public NavigationHandler getNavigationHandler() {

					return null;
				}

				@Override
				public PropertyResolver getPropertyResolver() {

					throw new UnsupportedOperationException();
				}

				@Override
				public StateManager getStateManager() {

					return new StateManager() {

						@Override
						public UIViewRoot restoreView( FacesContext context, String viewId, String renderKitId ) {

							throw new UnsupportedOperationException();
						}
					};
				}

				@Override
				public Iterator<Locale> getSupportedLocales() {

					throw new UnsupportedOperationException();
				}

				@Override
				public Iterator<String> getValidatorIds() {

					throw new UnsupportedOperationException();
				}

				@Override
				public VariableResolver getVariableResolver() {

					throw new UnsupportedOperationException();
				}

				@Override
				public ViewHandler getViewHandler() {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setActionListener( ActionListener actionlistener ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setDefaultLocale( Locale locale ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setDefaultRenderKitId( String s ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setMessageBundle( String s ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setNavigationHandler( NavigationHandler navigationhandler ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setPropertyResolver( PropertyResolver propertyresolver ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setStateManager( StateManager statemanager ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setSupportedLocales( Collection<Locale> arg0 ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setVariableResolver( VariableResolver variableresolver ) {

					throw new UnsupportedOperationException();
				}

				@Override
				public void setViewHandler( ViewHandler viewhandler ) {

					throw new UnsupportedOperationException();
				}
			};
		}

		@Override
		public UIViewRoot getViewRoot() {

			return new UIViewRoot() {

				@Override
				public String createUniqueId() {

					return "unique-id";
				}
			};
		}

		//
		// Unsupported public methods
		//

		@Override
		public ELContext getELContext() {

			return new ELContext() {

				@Override
				public ELResolver getELResolver() {

					throw new UnsupportedOperationException();
				}

				@Override
				public FunctionMapper getFunctionMapper() {

					throw new UnsupportedOperationException();
				}

				@Override
				public VariableMapper getVariableMapper() {

					throw new UnsupportedOperationException();
				}
			};
		}

		@Override
		public void addMessage( String s, FacesMessage facesmessage ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<String> getClientIdsWithMessages() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ExternalContext getExternalContext() {

			if ( mExternalContext == null ) {
				mExternalContext = new ExternalContext() {

					//
					// Private members
					//

					private Map<String, String>	mInitParameters	= CollectionUtils.newHashMap();

					private Map<String, Object>	mRequestMap		= CollectionUtils.newHashMap();

					//
					// Supported public methods
					//

					@Override
					public Map<String, Object> getApplicationMap() {

						return MockFacesContext.this.mApplicationMap;
					}

					@Override
					public URL getResource( String arg0 )
						throws MalformedURLException {

						return null;
					}

					@Override
					public Map getInitParameterMap() {

						return mInitParameters;
					}

					@Override
					public String getInitParameter( String name ) {

						return mInitParameters.get( name );
					}

					//
					// Unsupported public methods
					//

					@Override
					public void dispatch( String arg0 )
						throws IOException {

						throw new UnsupportedOperationException();
					}

					@Override
					public String encodeActionURL( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public String encodeNamespace( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public String encodeResourceURL( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public String getAuthType() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Object getContext() {

						throw new UnsupportedOperationException();
					}

					@Override
					public String getRemoteUser() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Object getRequest() {

						throw new UnsupportedOperationException();
					}

					@Override
					public String getRequestContextPath() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, Object> getRequestCookieMap() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, String> getRequestHeaderMap() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, String[]> getRequestHeaderValuesMap() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Locale getRequestLocale() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Iterator<Locale> getRequestLocales() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, Object> getRequestMap() {

						return mRequestMap;
					}

					@Override
					public Map<String, String> getRequestParameterMap() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Iterator<String> getRequestParameterNames() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, String[]> getRequestParameterValuesMap() {

						throw new UnsupportedOperationException();
					}

					@Override
					public String getRequestPathInfo() {

						throw new UnsupportedOperationException();
					}

					@Override
					public String getRequestServletPath() {

						throw new UnsupportedOperationException();
					}

					@Override
					public InputStream getResourceAsStream( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public Set<String> getResourcePaths( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public Object getResponse() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Object getSession( boolean arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public Map<String, Object> getSessionMap() {

						throw new UnsupportedOperationException();
					}

					@Override
					public Principal getUserPrincipal() {

						throw new UnsupportedOperationException();
					}

					@Override
					public boolean isUserInRole( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public void log( String arg0 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public void log( String arg0, Throwable arg1 ) {

						throw new UnsupportedOperationException();
					}

					@Override
					public void redirect( String arg0 )
						throws IOException {

						throw new UnsupportedOperationException();
					}
				};
			}

			return mExternalContext;
		}

		@Override
		public Severity getMaximumSeverity() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<FacesMessage> getMessages() {

			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<FacesMessage> getMessages( String s ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public RenderKit getRenderKit() {

			throw new UnsupportedOperationException();
		}

		@Override
		public boolean getRenderResponse() {

			throw new UnsupportedOperationException();
		}

		@Override
		public boolean getResponseComplete() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ResponseStream getResponseStream() {

			throw new UnsupportedOperationException();
		}

		@Override
		public ResponseWriter getResponseWriter() {

			throw new UnsupportedOperationException();
		}

		@Override
		public void release() {

			FacesContext.setCurrentInstance( null );
		}

		@Override
		public void renderResponse() {

			throw new UnsupportedOperationException();
		}

		@Override
		public void responseComplete() {

			throw new UnsupportedOperationException();
		}

		@Override
		public void setResponseStream( ResponseStream responsestream ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void setResponseWriter( ResponseWriter responsewriter ) {

			throw new UnsupportedOperationException();
		}

		@Override
		public void setViewRoot( UIViewRoot uiviewroot ) {

			throw new UnsupportedOperationException();
		}

		//
		// Supported protected methods
		//

		protected UIComponent createComponent( String componentName ) {

			if ( HtmlOutputText.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlOutputText();
			}

			if ( HtmlInputHidden.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlInputHidden();
			}

			if ( HtmlInputText.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlInputText();
			}

			if ( HtmlInputTextarea.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlInputTextarea();
			}

			if ( HtmlInputSecret.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlInputSecret();
			}

			if ( HtmlCommandButton.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlCommandButton();
			}

			if ( HtmlSelectOneMenu.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlSelectOneMenu();
			}

			if ( HtmlSelectManyCheckbox.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlSelectManyCheckbox();
			}

			if ( HtmlSelectBooleanCheckbox.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlSelectBooleanCheckbox();
			}

			if ( HtmlDataTable.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlDataTable();
			}

			if ( HtmlCommandLink.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlCommandLink();
			}

			if ( HtmlColumn.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlColumn();
			}

			if ( UISelectItems.COMPONENT_TYPE.equals( componentName ) ) {
				return new UISelectItems();
			}

			if ( UISelectItem.COMPONENT_TYPE.equals( componentName ) ) {
				return new UISelectItem();
			}

			if ( HtmlMetawidget.COMPONENT_TYPE.equals( componentName ) ) {
				return new HtmlMetawidget();
			}

			if ( UIStub.COMPONENT_TYPE.equals( componentName ) ) {
				return new UIStub();
			}

			if ( UIParameter.COMPONENT_TYPE.equals( componentName ) ) {
				return new UIParameter();
			}

			return new MockComponent( componentName );
		}
	}

	public static class MockComponent
		extends UIComponentBase {

		//
		// Private members
		//

		private String	mFamily;

		//
		// Constructor
		//

		public MockComponent( String family ) {

			mFamily = family;
		}

		//
		// Public methods
		//

		@Override
		public String getFamily() {

			return mFamily;
		}
	}

	public static class MockValueBinding
		extends ValueBinding {

		//
		// Private members
		//

		private String	mExpressionString;

		//
		// Constructor
		//

		public MockValueBinding( String expressionString ) {

			mExpressionString = expressionString;
		}

		//
		// Public methods
		//

		@Override
		public String getExpressionString() {

			return mExpressionString;
		}

		@Override
		public Class getType( FacesContext context )
			throws EvaluationException, PropertyNotFoundException {

			throw new UnsupportedOperationException();
		}

		@Override
		public Object getValue( FacesContext context )
			throws EvaluationException, PropertyNotFoundException {

			if ( mExpressionString.startsWith( "#{array" ) ) {
				return new String[] { mExpressionString, mExpressionString };
			}

			if ( mExpressionString.startsWith( "#{collection" ) ) {
				return CollectionUtils.newArrayList( mExpressionString, mExpressionString );
			}

			if ( "#{error}".equals( mExpressionString ) ) {
				throw new EvaluationException( "Forced error" );
			}

			if ( "#{null}".equals( mExpressionString ) ) {
				return null;
			}

			return "result of " + mExpressionString;
		}

		@Override
		public boolean isReadOnly( FacesContext context )
			throws EvaluationException, PropertyNotFoundException {

			throw new UnsupportedOperationException();
		}

		@Override
		public void setValue( FacesContext context, Object value )
			throws EvaluationException, PropertyNotFoundException {

			throw new UnsupportedOperationException();
		}
	}

	public static class MockMethodBinding
		extends MethodBinding {

		//
		// Private members
		//

		private String	mExpressionString;

		private Class[]	mParams;

		//
		// Constructor
		//

		public MockMethodBinding( String expressionString, Class[] params ) {

			mExpressionString = expressionString;
			mParams = params;
		}

		//
		// Public methods
		//

		@Override
		public String getExpressionString() {

			return mExpressionString;
		}

		public Class[] getParams() {

			return mParams;
		}

		@Override
		public Class getType( FacesContext context )
			throws MethodNotFoundException {

			throw new UnsupportedOperationException();
		}

		@Override
		public Object invoke( FacesContext context, Object[] args )
			throws EvaluationException, MethodNotFoundException {

			throw new UnsupportedOperationException();
		}
	}

	//
	// Private constructor
	//

	private FacesMetawidgetTests() {

		// Can never be called
	}
}
