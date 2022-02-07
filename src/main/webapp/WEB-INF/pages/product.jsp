<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="cartItems" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="recentProducts" type="java.util.LinkedList" scope="request"/>

<tags:master pageTitle="Product Details">
<br>
  Cart:
  <c:forEach var="cartItem" items="${cartItems}">
    <img class="product-tile" src="${cartItem.product.imageUrl}">
    ${cartItem.quantity}
  </c:forEach>
  <p>
    Product
  </p>
  <div class="error">
    ${error}
  </div>
  <div class="success">
    <c:if test="${empty error and not empty param.message}">
        Product added to cart
    </c:if>
   </div>
  <form method="post">
      <table>
          <tr>
            <td>
                Image
            </td>
            <td>
              <img class="product-tile" src="${product.imageUrl}">
            </td>
          </tr>
          <tr>
            <td>
                Description
            </td>
            <td>
                ${product.description}
            </td>
          </tr>
          <tr>
            <td>
                Stock
            </td>
            <td>
                ${product.stock}
            </td>
          </tr>
          <tr>
            <td>
                Price
            </td>
            <td class="price">
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </td>
          </tr>
          <tr>
            <td>
                Quantity
            </td>
            <td>
              <input name="quantity" value="${not empty param.quantity ? param.quantity : 1}">
            </td>
          </tr>
      </table>
      <button>Add to cart</button>
  <form>
  <div class="labelReviewedProducts">
    Reviewed products
  </div>

  <div>
      <c:if test="${not empty recentProducts}">
        <c:forEach var="recentProduct" items="${recentProducts}">
            <div class="lastReviewedProduct">
                <img class="product-tile" src="${recentProduct.imageUrl}">
                ${recentProduct.description}
                <fmt:formatNumber value="${recentProduct.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
            </div>
        </c:forEach>
      </c:if>
  </div>
</tags:master>