<%@ tag language="java" %>

<html>
	<head>
		<title>${pageContext.session.servletContext.servletContextName}</title>		
		<link rel="stylesheet" type="text/css" href="css/addressbook.css" media="all">		
	</head>	
	<body>
			
		<jsp:doBody />
		
		<div id="small-print">
			Powered by <a href="http://www.metawidget.org">Metawidget</a>. Icons by <a href="http://www.freeiconsweb.com">Free Icons Web</a>
		</div>
		
	</body>	    
</html>
