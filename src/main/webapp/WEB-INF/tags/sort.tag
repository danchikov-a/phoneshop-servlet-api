<%@ attribute name="order" required="true" %>
<%@ attribute name="field" required="true" %>

<a href="?field=${field}&order=${order}&query=${param.query}"
style="${field eq param.field and order eq param.order ? 'color:red' : '' }">${order}</a>