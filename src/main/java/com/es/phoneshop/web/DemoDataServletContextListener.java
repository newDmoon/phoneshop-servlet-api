package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.PriceHistoryItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

public class DemoDataServletContextListener implements ServletContextListener {
    private final Currency USD = Currency.getInstance("USD");
    private final String CONTEXT_PARAM_INSERT_DEMO_DATA = "insertDemoData";
    private List<PriceHistoryItem> priceHistoryItems;
    private ProductDao productDao;

    public DemoDataServletContextListener() {
        this.productDao = ArrayListProductDao.getInstance();
        this.priceHistoryItems = new ArrayList<>();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        boolean insertDemoData = Boolean
                .parseBoolean(event.getServletContext().
                        getInitParameter(CONTEXT_PARAM_INSERT_DEMO_DATA));
        if(insertDemoData){
            saveSampleProducts();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public void savePriceHistoryItems(){
        PriceHistoryItem priceHistoryItem = new PriceHistoryItem(new Date(), new BigDecimal(200), USD);
        PriceHistoryItem priceHistoryItem1 = new PriceHistoryItem(new Date(), new BigDecimal(125), USD);
        PriceHistoryItem priceHistoryItem2 = new PriceHistoryItem(new Date(), new BigDecimal(300), USD);
        priceHistoryItems.add(priceHistoryItem);
        priceHistoryItems.add(priceHistoryItem1);
        priceHistoryItems.add(priceHistoryItem2);
    }

    public void saveSampleProducts() {
        savePriceHistoryItems();
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", priceHistoryItems));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), USD, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg", priceHistoryItems));
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), USD, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", priceHistoryItems));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), USD, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg", priceHistoryItems));
        productDao.save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", priceHistoryItems));
        productDao.save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), USD, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg", priceHistoryItems));
        productDao.save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg", priceHistoryItems));
        productDao.save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg", priceHistoryItems));
        productDao.save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg", priceHistoryItems));
        productDao.save(new Product("palmp", "Palm Pixi", new BigDecimal(170), USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg", priceHistoryItems));
        productDao.save(new Product("simc56", "Siemens C56", new BigDecimal(70), USD, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg", priceHistoryItems));
        productDao.save(new Product("simc61", "Siemens C61", new BigDecimal(80), USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg", priceHistoryItems));
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), USD, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg", priceHistoryItems));
    }
}
