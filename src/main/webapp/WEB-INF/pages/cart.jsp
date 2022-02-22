<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart List">
    <c:if test="${not empty cart.cartItems}">
        <p>
            Cart: ${cart}
        </p>
        <c:if test="${empty errors}">
            <div class="success">
                <p>
                    Successfully updated
                </p>
            </div>
        </c:if>
        <c:if test="${not empty errors}">
            <p class="error" id="product-description-text">
                There were errors updating cart
            </p>
        </c:if>
        <form method="post" action="${pageContext.servletContext.contextPath}/cart">
            <table>
                <thead>
                <tr>
                    <td>Image</td>
                    <td>
                        Description
                    </td>
                    <td class="quantity">
                        Quantity
                    </td>
                    <td class="price">
                        Price
                    </td>
                    <td></td>
                </tr>
                </thead>
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
                        <td class="quantity">
                            <c:set var="error" value="${errors[cartItem.product.id]}"/>
                            <input class="quantity"
                                   name="quantity"
                                   value="${not empty error ? paramValues['quantity'][status.index] : cartItem.quantity}">
                            <input type="hidden" name="productId" value="${cartItem.product.id}">
                            <c:if test="${not empty error}">
                                <div class="error">
                                        ${error}
                                </div>
                            </c:if>
                        </td>
                        <td class="price">
                            <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${cartItem.product.id}">
                                <fmt:formatNumber value="${cartItem.product.price}" type="currency"
                                                  currencySymbol="$"/>
                            </a>
                        </td>
                        <td>
                            <button form="deleteCartItem"
                                    formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${cartItem.product.id}">
                                Delete
                            </button>
                        </td>
                    </tr>
                </c:forEach>

                <tr>
                    <td colspan="2">
                        Total Quantity: ${cart.totalQuantity}
                    </td>
                    <td colspan="2">
                        Total cost:
                        <fmt:formatNumber value="${cart.totalCost}" type="currency"
                                          currencySymbol="$"/>
                    </td>
                    <td></td>
                </tr>
            </table>
            <p>
                <button>Update</button>
            </p>
        </form>
        <form id="deleteCartItem" method="post"></form>
    </c:if>
</tags:master>