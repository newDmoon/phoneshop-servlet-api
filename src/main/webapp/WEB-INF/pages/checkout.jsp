<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout Page">
    <c:if test="${not empty order.cartItems}">
        <p>
            Cart: ${order.cartItems}
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
                There were errors placing order
            </p>
        </c:if>
        <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
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
                </tr>
                </thead>
                <c:forEach var="orderItem" items="${order.cartItems}" varStatus="status">
                    <tr>
                        <td>
                            <img class="product-tile" src="${orderItem.product.imageUrl}">
                        </td>
                        <td>
                            <a href="${pageContext.servletContext.contextPath}/products/${orderItem.product.id}">
                                    ${orderItem.product.description}
                            </a>
                        </td>
                        <td class="quantity">
                                ${orderItem.quantity}
                        </td>
                        <td class="price">
                            <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${orderItem.product.id}">
                                <fmt:formatNumber value="${orderItem.product.price}" type="currency"
                                                  currencySymbol="$"/>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="2">
                    </td>
                    <td colspan="2">
                        Subtotal:
                        <fmt:formatNumber value="${order.subTotal}" type="currency"
                                          currencySymbol="$"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                    </td>
                    <td colspan="2">
                        Delivery cost:
                        <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                          currencySymbol="$"/>
                    </td>
                </tr>
                </tr>
                <tr>
                    <td colspan="2">
                    </td>
                    <td colspan="2">
                        Total cost:
                        <fmt:formatNumber value="${order.totalCost}" type="currency"
                                          currencySymbol="$"/>
                    </td>
                </tr>
            </table>
            <h2>Your details</h2>
            <table>
                <tags:orderFormRow name="firstName" label="First Name" order="${order}" errors="${errors}"></tags:orderFormRow>
                <tags:orderFormRow name="lastName" label="Last Name" order="${order}" errors="${errors}"></tags:orderFormRow>
                <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}"></tags:orderFormRow>
                <tags:orderFormRow name="deliveryDate" label="Delivery Date" order="${order}" errors="${errors}"></tags:orderFormRow>
                <tags:orderFormRow name="deliveryAddress" label="Delivery Address" order="${order}" errors="${errors}"></tags:orderFormRow>
                <tr>
                    <td>Payment method<span class="required">*</span></td>
                    <td><select name="paymentMethod">
                        <c:forEach var="paymentMethod" items="${paymentMethods}">
                            <option>${paymentMethod}</option>
                        </c:forEach>
                    </select></td>
                </tr>
            </table>
            <p>
                <button>Checkout</button>
            </p>
        </form>
        <form id="deleteCartItem" method="post"></form>
    </c:if>
</tags:master>