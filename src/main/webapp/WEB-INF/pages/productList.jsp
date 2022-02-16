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
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
    <form method="post">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                    <tags:sortLink sort="description" order="asc"/>
                    <tags:sortLink sort="description" order="desc"/>
                </td>
                <td class="quantity">Quantity</td>
                <td class="price">
                    Price
                    <tags:sortLink sort="price" order="asc"/>
                    <tags:sortLink sort="price" order="desc"/>
                </td>
                <td></td>
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
                    <td>
                        <input class="quantity" name="quantity" value="1">
                    </td>
                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${product.id}">
                            <fmt:formatNumber value="${product.price}" type="currency"
                                              currencySymbol="${product.currency.symbol}"/>
                        </a>
                    </td>
                    <td>
                        <button>Add to cart</button>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </form>
    <c:if test="${not empty recentlyViewedProducts}">
        <div class="recentlyViewedProducts">
            <h3>Recently viewed</h3>
            <table>
                <tr>
                    <c:forEach var="recentlyViewedProduct" items="${recentlyViewedProducts}">
                        <td>
                            <div><img src="${recentlyViewedProduct.imageUrl}"/></div>
                            <div>
                                <a href="${pageContext.servletContext.contextPath}/products/${recentlyViewedProduct.id}">
                                        ${recentlyViewedProduct.description}
                                </a></div>
                            <div>
                                <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${recentlyViewedProduct.id}">
                                    <fmt:formatNumber value="${recentlyViewedProduct.price}" type="currency"
                                                      currencySymbol="${recentlyViewedProduct.currency.symbol}"/>
                                </a></div>
                        </td>
                    </c:forEach>
                </tr>
            </table>
        </div>
    </c:if>
</tags:master>