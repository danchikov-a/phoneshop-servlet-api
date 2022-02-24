<%@ attribute name="fieldInput" required="true" %>
<%@ attribute name="fieldName" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order"%>

<tr>
    <td>
        ${fieldName}
    </td>
    <td>
        ${order[fieldInput]}
    </td>
</tr>
