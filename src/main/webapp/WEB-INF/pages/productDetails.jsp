<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="cart" scope="request" type="com.es.phoneshop.model.cart.Cart"/>
<tags:master pageTitle="Product Details">
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${not empty error}">
        <div class="error">
            There was an error adding to cart
        </div>
    </c:if>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <h1>
            ${product.description}
    </h1>
    <form method="post">
        <table>
            <tr>
                <td>Image</td>
                <td>
                    <img src="${product.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>Code</td>
                <td>
                        ${product.code}
                </td>
            </tr>
            <tr>
                <td>Stock</td>
                <td>
                        ${product.stock}
                </td>
            </tr>
            <tr>
                <td>Price</td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>Quantity</td>
                <td>
                    <input class="quantity" name="quantity" value="${not empty error ? param.quantity : 1}"
                           placeholder="Input quantity">
                    <c:if test="${not empty error}">
                        <div class="error">
                                ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>Add to cart</button>
        </p>
    </form>
    <c:if test="${not empty recentlyViewedProducts}">
        <div class="recentlyViewedProducts">
            <h3>Recently viewed</h3>
            <table>
                <tr>
                    <c:forEach var="recentlyViewedProduct" items="${recentlyViewedProducts}">
                        <td>
                            <div><img src="${recentlyViewedProduct.imageUrl}"/></div>
                            <div><a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                                    ${product.description}
                            </a></div>
                            <div>
                                <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${product.id}">
                                    <fmt:formatNumber value="${product.price}" type="currency"
                                                      currencySymbol="${product.currency.symbol}"/>
                                </a></div>
                        </td>
                    </c:forEach>
                </tr>
            </table>
        </div>
    </c:if>
</tags:master>