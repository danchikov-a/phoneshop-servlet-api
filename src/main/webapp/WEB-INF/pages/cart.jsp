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
        Cart updated
    </c:if>
   </div>
  <form method="post" action="cart">
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
          <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="status">
            <tr>
              <td>
                <img class="product-tile" src="${cartItem.product.imageUrl}">
              </td>
              <td>
                  ${cartItem.product.description}
              </td>
              <td>
                <c:set var="error" value="${errors[cartItem.product.id]}"/>
                <input name="quantity" class="price" value=
                    "${not empty error ? paramValues['quantity'][status.index]: cartItem.quantity}">
                <input name="productId" type="hidden" value="${cartItem.product.id}">
                <c:if test="${not empty error}">
                    <div class="error">
                        ${errors[cartItem.product.id]}
                    </div>
                </c:if>
              </td>
              <td class="price">
                 <fmt:formatNumber value="${cartItem.product.price}" type="currency" currencySymbol="${cartItem.product.currency.symbol}"/>
              </td>
              <td class="price">
                <button form="deleteCartItem"
                    formAction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${cartItem.product.id}">Delete</button>
              </td>
            </tr>
          </c:forEach>
        </table>
        <button>Update</button>
    </form>
    <form id="deleteCartItem" method="post">
    </form>
</tags:master>