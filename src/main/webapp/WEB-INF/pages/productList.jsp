<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="recentProducts" type="java.util.LinkedList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="query" value="${param.query}">
    <button>Search</button>
  </form>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
            Description
            <tags:sort field="description" order="asc"/>
            <tags:sort field="description" order="desc"/>
        </td>
        <td class="price">
            Price
            <tags:sort field="price" order="asc"/>
            <tags:sort field="price" order="desc"/>
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
  <div class="labelReviewedProducts">
      Reviewed products
    </div>
  <div>
        <c:if test="${not empty recentProducts}">
          <c:forEach var="recentProduct" items="${recentProducts}">
              <div class="lastReviewedProduct">
                  <img class="product-tile" src="${recentProduct.imageUrl}">
                  <a href="${pageContext.servletContext.contextPath}/products/${recentProduct.id}">
                    ${recentProduct.description}
                  </a>
                  <fmt:formatNumber value="${recentProduct.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
              </div>
          </c:forEach>
        </c:if>
    </div>
</tags:master>