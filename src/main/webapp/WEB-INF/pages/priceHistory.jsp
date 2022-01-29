<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="priceHistory" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Price History">
  <p>
    Price History
  </p>
  <p>
    ${description}
  </p>

  <table>
    <thead>
        <tr>
          <td>
              Start date
          </td>
          <td class="price">
              Price
          </td>
        </tr>
      </thead>
    <c:forEach var="priceHistoryElement" items="${priceHistory}">
      <tr>
        <td>
            <fmt:parseDate value="${priceHistoryElement.startDate}" pattern="yyyy-MM-dd"  var="parsedDate" type="date" />
            <fmt:formatDate value="${parsedDate}" var="stdDatum" type="date" pattern="dd.MM.yyyy" />
            ${stdDatum}
        </td>
        <td class="price">
          <fmt:formatNumber value="${priceHistoryElement.price}" type="currency" currencySymbol="${priceHistoryElement.currency.symbol}"/>
        </td>
      </tr>
    </c:forEach>
  </table>
</tags:master>