<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<jsp:useBean id="cartItems" type="java.util.List" scope="request"/>

<tags:master pageTitle="Order Overview">
<br>
  <table>
      <thead>
        <tr>
          <td>Image</td>
          <td>
              Description
          </td>
          <td class="price">
            Quantity
          </td>
          <td class="price">
              Price
          </td>
        </tr>
      </thead>

      <c:forEach var="cartItem" items="${cartItems}" varStatus="status">
        <tr>
          <td>
            <img class="product-tile" src="${cartItem.product.imageUrl}">
          </td>
          <td>
              <a href="${pageContext.servletContext.contextPath}/products/${cartItem.product.id}">
                  ${cartItem.product.description}
              </a>
          </td>
          <td>
              ${cartItem.quantity}
          </td>
          <td class="price">
             <fmt:formatNumber value="${cartItem.product.price}" type="currency" currencySymbol="${cartItem.product.currency.symbol}"/>
          </td>
        </tr>
        <tr>
        </tr>
      </c:forEach>
    </table>
    <p>
        Subtotal:

        <fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="${curr.symbol}"/>
    </p>
    <p>
        Delivery cost:
        <fmt:formatNumber value="${order.deliveryCost}" type="currency" currencySymbol="${curr.symbol}"/>
    </p>
    <p>
        Total cost:
        <fmt:formatNumber value="${order.totalCost}" type="currency" currencySymbol="${curr.symbol}"/>
    </p>
    <table>
        <tags:overviewField fieldName="First name" fieldInput="firstName"
            order="${order}"></tags:overviewField>
        <tags:overviewField fieldName="Last name" fieldInput="lastName"
            order="${order}"></tags:overviewField>
        <tags:overviewField fieldName="Phone" fieldInput="phone"
            order="${order}"></tags:overviewField>
        <tr>
            <td>
                Delivery date</span>
            </td>
            <td>
                <fmt:parseDate value="${order.deliveryDate}" pattern="yyyy-MM-dd"  var="parsedDate" type="date" />
                <fmt:formatDate value="${parsedDate}" var="formatedDate" type="date" pattern="dd.MM.yyyy" />
                ${formatedDate}
            </td>
        </tr>
        <tags:overviewField fieldName="Delivery address" fieldInput="deliveryAddress"
                        order="${order}"></tags:overviewField>
        <tr>
            <td>
                Payment method</span>
            </td>
            <td>
                ${order.paymentMethod}
            </td>
        </tr>
    </table>
</tags:master>