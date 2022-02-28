package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductBuilderImpl;
import com.es.phoneshop.service.cart.CartService;
import com.es.phoneshop.service.order.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckOutPageServletTest {
    private final Currency USD = Currency.getInstance("USD");
    private final Long idTest = 1L;
    private final String codeTest = "test-product";
    private final String descriptionTest = "Samsung Galaxy S";
    private final BigDecimal priceTest = new BigDecimal(100);
    private final int stockTest = 100;
    private final String imageUrlTest = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg";
    private final String BASE_NAME_PATH = "error";
    private final String DELIVERY_DATE_PARAMETER = "deliveryDate";

    @Mock
    private CartService cartService;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private OrderService orderService;
    @InjectMocks
    private CheckoutPageServlet servlet = new CheckoutPageServlet();

    private ResourceBundle messages;

    @Before
    public void setup() throws ServletException {
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);
        messages = ResourceBundle.getBundle(BASE_NAME_PATH, Locale.getDefault());
    }

    @Test
    public void shouldVerifyDoGetMethodsWhenDoGetInvoked() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request, times(2)).setAttribute(any(), any());
        verify(request.getRequestDispatcher(anyString())).forward(request, response);
    }

    @Test
    public void shouldInvokeAllDoPostMethodsWhenCorrectInputData() throws ServletException, IOException {
        Cart cart = new Cart();
        servlet.init(config);
        Product testProduct = new ProductBuilderImpl().setId(idTest)
                .setCode(codeTest)
                .setDescription(descriptionTest)
                .setPrice(priceTest)
                .setCurrency(USD)
                .setStock(stockTest)
                .setImageUrl(imageUrlTest)
                .build();
        CartItem cartItem = new CartItem(testProduct, 5);
        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);
        when(request.getParameter(DELIVERY_DATE_PARAMETER)).thenReturn(LocalDate.now().toString());
        when(request.getSession()).thenReturn(session);

        servlet.doPost(request, response);
    }

    @Test
    public void shouldVerifyHandleErrorWhenExceptionsExist() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        servlet.init(config);

        servlet.doPost(request, response);

        verify(request, times(3)).setAttribute(any(), any());
    }
}