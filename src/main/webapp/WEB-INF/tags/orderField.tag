<%@ attribute name="fieldInput" required="true" %>
<%@ attribute name="fieldName" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="errors" required="true" type="java.util.Map"%>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order"%>

<tr>
    <td>
        ${fieldName} <span style="color:red">*</span>
    </td>
    <td>
        <c:set var="error" value="${errors[fieldInput]}"/>
        <input name="${fieldInput}" value="${not empty errors ? param[fieldInput] : order[fieldInput]}" />
        <c:if test="${not empty error}">
            <div class="error">
                ${error}
            </div>
        </c:if>
    </td>
</tr>
