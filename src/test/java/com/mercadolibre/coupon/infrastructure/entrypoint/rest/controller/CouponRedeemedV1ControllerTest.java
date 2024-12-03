package com.mercadolibre.coupon.infrastructure.entrypoint.rest.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.COUPON_PATH;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.X_API_VERSION;
import static com.mercadolibre.coupon.infrastructure.MockUtils.asJsonString;
import static com.mercadolibre.coupon.infrastructure.MockUtils.getCouponRqV1;
import static com.mercadolibre.coupon.infrastructure.MockUtils.getCouponRqV1InvalidAmount;
import static com.mercadolibre.coupon.infrastructure.MockUtils.getCouponRqV1NegativeAmount;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CouponRedeemedV1ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testCalculateBestOfferCouponWhenOk() throws Exception {
        mvc.perform(post(COUPON_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(X_API_VERSION, "v1")
                        .header("Accepted-Language", "es")
                        .content(asJsonString(getCouponRqV1())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(X_API_VERSION, "v1"))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    public void testCalculateBestOfferCouponWhenErrRequestContentType() throws Exception {
        mvc.perform(post(COUPON_PATH)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header(X_API_VERSION, "v1")
                        .header("Accepted-Language", "es")
                        .content(asJsonString(getCouponRqV1())))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error_response").exists());
    }

    @Test
    public void testCalculateBestOfferCouponWhenErrRequestVersion() throws Exception {
        mvc.perform(post(COUPON_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accepted-Language", "es")
                        .content(asJsonString(getCouponRqV1())))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error_response").exists());
    }

    @Test
    public void testCalculateBestOfferCouponWhenErrRequestAmount() throws Exception {
        mvc.perform(post(COUPON_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accepted-Language", "es")
                        .header(X_API_VERSION, "v1")
                        .content(asJsonString(getCouponRqV1NegativeAmount())))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error_response").exists());
    }

    @Test
    public void testCalculateBestOfferCouponWhenErrBusinessException() throws Exception {
        mvc.perform(post(COUPON_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accepted-Language", "es")
                        .header(X_API_VERSION, "v1")
                        .content(asJsonString(getCouponRqV1InvalidAmount())))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error_response").exists());
    }

}
