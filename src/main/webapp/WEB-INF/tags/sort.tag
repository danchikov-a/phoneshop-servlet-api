<%@ attribute name="order" required="true" %>
<%@ attribute name="field" required="true" %>

<a href="?field=${field}&order=${order}&query=${param.query}">${order}</a>