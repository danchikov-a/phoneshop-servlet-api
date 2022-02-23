<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<jsp:useBean id="paymentMethods" type="java.util.List" scope="request"/>

<tags:master pageTitle="Order Details">
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
      Total cost: <fmt:formatNumber value="${cart.totalCost}" type="currency" currencySymbol="${cartItem.product.currency.symbol}"/>
      Total quantity: ${cart.totalQuantity}
      <c:forEach var="cartItem" items="${cart.cartItems}" varStatus="status">
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


    <form method="post">
        <table>
            <tags:orderField fieldName="First name" fieldInput="firstName"
                errors="${errors}" order="${order}"></tags:orderField>
            <tags:orderField fieldName="Last name" fieldInput="lastName"
                errors="${errors}" order="${order}"></tags:orderField>
            <tags:orderField fieldName="Phone" fieldInput="phone"
                errors="${errors}" order="${order}"></tags:orderField>
            <tr>
                <td>
                    Delivery date <span style="color:red">*</span>
                </td>
                <td>
                    <c:set var="error" value="${errors['deliveryDate']}"/>
                    <input type="date"
                    name="deliveryDate" value="${not empty errors ? param['deliveryDate'] : order['deliveryDate']}" />
                    <c:if test="${not empty error}">
                        <div class="error">
                            ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
            <tags:orderField fieldName="Delivery address" fieldInput="deliveryAddress"
                            errors="${errors}" order="${order}"></tags:orderField>
            <tr>
                <td>
                    Payment method <span style="color:red">*</span>
                </td>
                <td>
                    <c:set var="error" value="${errors['paymentMethod']}"/>
                    <c:choose>
                      <c:when test="${not empty error}">
                         <select name="paymentMethod">
                            <option selected="selected"></option>
                            <c:forEach var="paymentMethod" items="${paymentMethods}">
                                <option>${paymentMethod}</option>
                            </c:forEach>
                        </select>
                      </c:when>
                      <c:otherwise>
                          <select name="paymentMethod">
                              <option></option>
                              <c:forEach var="paymentMethod" items="${paymentMethods}">
                                  <option ${paymentMethod == param.paymentMethod ? 'selected="selected"' : ''}>${paymentMethod}</option>
                              </c:forEach>
                          </select>
                      </c:otherwise>
                    </c:choose>

                    <c:if test="${not empty error}">
                        <div class="error">
                            ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <button>Place order</button>
    </form>
</tags:master>