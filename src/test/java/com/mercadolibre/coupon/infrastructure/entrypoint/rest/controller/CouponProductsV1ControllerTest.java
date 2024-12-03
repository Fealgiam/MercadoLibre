package com.mercadolibre.coupon.infrastructure.entrypoint.rest.controller;

import com.mercadolibre.coupon.application.inbound.ProductInPort;
import com.mercadolibre.coupon.infrastructure.mapper.ProductRsV1Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.COUPON_PRODUCTS_COUNTRY_PATH;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.COUPON_PRODUCTS_PATH;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.NUMBER_RECORD_PARAM;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.X_API_VERSION;
import static com.mercadolibre.coupon.infrastructure.MockUtils.COUNTRY_CODE;
import static com.mercadolibre.coupon.infrastructure.MockUtils.LIMIT;
import static com.mercadolibre.coupon.infrastructure.MockUtils.getProducts;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CouponProductsV1ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private ProductInPort productService;

    @Mock
    private ProductRsV1Mapper productRsV1Mapper;


    @Test
    public void testFetchTopProductsRedeemedByCouponWhenOk() throws Exception {
        when(productService.fetchRedeemedProducts(any())).thenReturn(getProducts());
        when(productRsV1Mapper.mapper(any())).thenCallRealMethod();

        mvc.perform(get(COUPON_PRODUCTS_PATH)
                        .param(NUMBER_RECORD_PARAM, LIMIT.get().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(X_API_VERSION, "v1")
                        .header("Accepted-Language", "es"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(X_API_VERSION, "v1"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void testFetchTopProductsRedeemedByCouponWhenErr() throws Exception {
        when(productService.fetchRedeemedProducts(any())).thenReturn(getProducts());
        when(productRsV1Mapper.mapper(any())).thenCallRealMethod();

        mvc.perform(get(COUPON_PRODUCTS_PATH)
                        .param(NUMBER_RECORD_PARAM, LIMIT.get().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accepted-Language", "es"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error_response").exists());
    }

    @Test
    public void testFetchTopProductsRedeemedByCouponByCountryOk() throws Exception {
        when(productService.fetchRedeemedProductsByCountry(COUNTRY_CODE, LIMIT)).thenReturn(getProducts());
        when(productRsV1Mapper.mapper(any())).thenCallRealMethod();

        mvc.perform(get(COUPON_PRODUCTS_PATH + COUPON_PRODUCTS_COUNTRY_PATH, COUNTRY_CODE)
                        .param(NUMBER_RECORD_PARAM, LIMIT.get().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(X_API_VERSION, "v1")
                        .header("Accepted-Language", "es"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void testFetchTopProductsRedeemedByCouponByCountryErrRequestVersion() throws Exception {
        when(productService.fetchRedeemedProducts(any())).thenReturn(getProducts());
        when(productRsV1Mapper.mapper(any())).thenCallRealMethod();

        mvc.perform(get(COUPON_PRODUCTS_PATH + COUPON_PRODUCTS_COUNTRY_PATH, COUNTRY_CODE)
                        .param(NUMBER_RECORD_PARAM, LIMIT.get().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accepted-Language", "es"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error_response").exists());
    }

    @Test
    public void testFetchTopProductsRedeemedByCouponByCountryErrRequestContentType() throws Exception {
        when(productService.fetchRedeemedProducts(any())).thenReturn(getProducts());
        when(productRsV1Mapper.mapper(any())).thenCallRealMethod();

        mvc.perform(get(COUPON_PRODUCTS_PATH + COUPON_PRODUCTS_COUNTRY_PATH, COUNTRY_CODE)
                        .param(NUMBER_RECORD_PARAM, LIMIT.get().toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(X_API_VERSION, "v1")
                        .header("Accepted-Language", "es"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error_response").exists());
    }

    @Test
    public void testFetchTopProductsRedeemedByCouponByCountryErrRequestCountry() throws Exception {
        when(productService.fetchRedeemedProducts(any())).thenReturn(getProducts());
        when(productRsV1Mapper.mapper(any())).thenCallRealMethod();

        mvc.perform(get(COUPON_PRODUCTS_PATH + COUPON_PRODUCTS_COUNTRY_PATH, "AAA")
                        .param(NUMBER_RECORD_PARAM, LIMIT.get().toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(X_API_VERSION, "v1")
                        .header("Accepted-Language", "es"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error_response").exists());
    }

}
