<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>

<tags:master pageTitle="Cart Details">
<br>
  <div class="error">
    ${error}
  </div>
  <div class="success">
    <c:if test="${empty error and not empty param.message}">
        Product added to cart
    </c:if>
   </div>
  <table>
      <thead>
        <tr>
          <td>Image</td>
          <td>
              Description
          </td>
          <td class="price">
              Price
          </td>
        </tr>
      </thead>
      <c:forEach var="cartItem" items="${cart.cartItems}">
        <tr>
          <td>
            <img class="product-tile" src="${cartItem.product.imageUrl}">
          </td>
          <td>
              ${cartItem.product.description}
          </td>
          <td class="price">
             <fmt:formatNumber value="${cartItem.product.price}" type="currency" currencySymbol="${cartItem.product.currency.symbol}"/>
          </td>
        </tr>
      </c:forEach>
    </table>

</tags:master>