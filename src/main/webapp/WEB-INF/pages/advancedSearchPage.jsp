<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>

<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
      <div>
        Product code
        <input name="searchProductCode" value="${param.searchProductCode}">
      </div>
      <div>
          Min price
          <input name="searchMinPrice" value="${param.searchMinPrice}">
      </div>
      <div>
        Max price
         <input name="searchMaxPrice" value="${param.searchMaxPrice}">
      </div>
      <div>
          Min stock
          <input name="searchMinStock" value="${param.searchMinStock}">
       </div>
      <button>Search</button>

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
        <c:forEach var="product" items="${products}">
              <tr>
                <td>
                  <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                        ${product.description}
                    </a>
                </td>
                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
              </tr>
        </c:forEach>
      </table>

</tags:master>