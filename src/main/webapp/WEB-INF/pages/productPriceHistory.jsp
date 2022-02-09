<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Price History">
    <h1>
        Price History
    </h1>
    <h2>
            ${product.description}
    </h2>
    <table>
        <tr>
            <th>Start date</th>
            <th>Price</th>
        </tr>
        <c:forEach var="productPriceHistoryItem" items="${product.priceHistoryItemList}">
        <tr>
            <td>
                <fmt:formatDate value="${productPriceHistoryItem.date}" dateStyle="medium"/>
            </td>
            <td>
                <fmt:formatNumber value="${productPriceHistoryItem.price}" type="currency"
                                  currencySymbol="${product.currency.symbol}"/>
            </td>
            </c:forEach>

    </table>
</tags:master>