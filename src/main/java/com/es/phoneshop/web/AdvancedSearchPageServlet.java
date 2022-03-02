package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.util.ProductSearchFilter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Parser;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AdvancedSearchPageServlet extends HttpServlet {
    private final String ADVANCED_SEARCH_PAGE = "/WEB-INF/pages/advancedSearch.jsp";
    private final String PRODUCTS_PARAMETER = "products";
    private final String CODE_PARAMETER = "productCode";
    private final String MIN_PRICE_PARAMETER = "minPrice";
    private final String MAX_PRICE_PARAMETER = "maxPrice";
    private final String MIN_STOCK_PARAMETER = "minStock";
    private final String ERRORS_ATTRIBUTE = "errors";
    private final String ERROR_MESSAGE_NUMBER_FORMAT = "Not a number";

    private ProductDao productDao;
    private ProductSearchFilter productSearchFilter;

    @Override
    public void init() throws ServletException {
        productSearchFilter = new ProductSearchFilter();
        productDao = ArrayListProductDao.getInstance();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();

        setRequiredParameter(request, CODE_PARAMETER, errors, productSearchFilter::setCode);
        setRequiredParameterPrice(request, MIN_PRICE_PARAMETER, errors, productSearchFilter::setMinPrice);
        setRequiredParameterPrice(request, MAX_PRICE_PARAMETER, errors, productSearchFilter::setMaxPrice);
        setRequiredParameterStock(request, MIN_STOCK_PARAMETER, errors, productSearchFilter::setMinStock);

        request.setAttribute(ERRORS_ATTRIBUTE, errors);
        request.setAttribute(PRODUCTS_PARAMETER, productDao.findProducts(productSearchFilter));

        request.getRequestDispatcher(ADVANCED_SEARCH_PAGE).forward(request, response);
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors,
                                      Consumer<String> consumer) {
        String value = request.getParameter(parameter);

        consumer.accept(value);
    }


    private void setRequiredParameterStock(HttpServletRequest request, String parameter, Map<String, String> errors,
                                           Consumer<Integer> consumer) {
        String value = request.getParameter(parameter);

        try {
            validateIsEmpty(value);
            Integer parsedInteger = parseInteger(value);
            consumer.accept(parsedInteger);
        } catch (NullPointerException e) {
            consumer.accept(-1);
        } catch (NumberFormatException e) {
            errors.put(parameter, ERROR_MESSAGE_NUMBER_FORMAT);
            consumer.accept(null);
        }
    }

    private void setRequiredParameterPrice(HttpServletRequest request, String parameter, Map<String, String> errors,
                                           Consumer<BigDecimal> consumer) {
        String value = request.getParameter(parameter);

        try {
            validateIsEmpty(value);
            BigDecimal parsedValue = parseBigDecimal(value);
            consumer.accept(parsedValue);
        } catch (NullPointerException e) {
            consumer.accept(null);
        } catch (NumberFormatException e) {
            errors.put(parameter, ERROR_MESSAGE_NUMBER_FORMAT);
            consumer.accept(null);
        }
    }

    private Integer parseInteger(String value) throws NumberFormatException {
        Integer parsedValue = Integer.valueOf(value);
        return parsedValue;
    }

    private BigDecimal parseBigDecimal(String value) throws NumberFormatException {
        BigDecimal parsedValue = BigDecimal.valueOf(Long.parseLong(value));
        return parsedValue;
    }

    private void validateIsEmpty(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new NullPointerException();
        }
    }
}
