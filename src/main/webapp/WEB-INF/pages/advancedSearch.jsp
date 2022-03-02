<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced Search">
        <p>
            <h1>Advanced search page</h1>
        </p>
        <c:if test="${not empty errors}">
            <p class="error" id="product-description-text">
                There were errors searching
            </p>
        </c:if>
    <form>
        <table>
            <tags:advancedSearchFormRow name="productCode" label="Product code" errors="${errors}"></tags:advancedSearchFormRow>
            <tags:advancedSearchFormRow name="minPrice" label="Min price" errors="${errors}"></tags:advancedSearchFormRow>
            <tags:advancedSearchFormRow name="maxPrice" label="Max price"  errors="${errors}"></tags:advancedSearchFormRow>
            <tags:advancedSearchFormRow name="minStock" label="Min stock" errors="${errors}"></tags:advancedSearchFormRow>
        </table>
        <button>Search</button>
    </form>

    <table>
        <thead>
        <tr>
            <th>Image</th>
            <th>Description</th>
            <th>Price</th>
        </tr>
        </thead>
        <p class="success">
            <c:if test="${not empty products}">
                Found ${products.size()} products
            </c:if>
        </p>
    <c:forEach var="product" items="${products}">
        <tr>
                <td>
                    <img src="${product.imageUrl}">
                </td>
                <td>
                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
        </tr>
    </c:forEach>
    </table>
</tags:master>