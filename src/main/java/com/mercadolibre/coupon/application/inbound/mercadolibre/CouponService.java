package com.mercadolibre.coupon.application.inbound.mercadolibre;

import com.mercadolibre.coupon.application.inbound.CouponInPort;
import com.mercadolibre.coupon.application.inbound.mercadolibre.step.BuildCouponByCountry;
import com.mercadolibre.coupon.application.inbound.mercadolibre.step.FetchProducts;
import com.mercadolibre.coupon.application.inbound.mercadolibre.step.SaveCouponProducts;
import com.mercadolibre.coupon.application.inbound.mercadolibre.step.SelectBestOfferCoupon;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextEnum;
import com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre;
import com.mercadolibre.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUPON;
import static java.lang.String.format;

@Log4j2
@Service
@RequiredArgsConstructor
public class CouponService implements CouponInPort {

    private static final String CLASS_NAME = CouponService.class.getSimpleName();

    // Steps
    private final FetchProducts fetchProducts;
    private final SaveCouponProducts saveCouponProducts;
    private final BuildCouponByCountry buildCouponByCountry;
    private final SelectBestOfferCoupon selectBestOfferCoupon;


    // Private Methods
    private MessageContext<MessageContextMercadoLibre, Object> initialPipelineData(final Coupon coupon) {
        var context = new MessageContextEnum<MessageContextMercadoLibre, Object>(MessageContextMercadoLibre.class);

        context.addItem(COUPON, coupon);

        return context;
    }

    // Custom Methods
    @Override
    public Coupon calculateBestOfferCoupon(final Coupon coupon) {
        try {
            var contextCalculateBestOfferCoupon = this.initialPipelineData(coupon);

            var couponRedeemed = fetchProducts
                    .andThen(buildCouponByCountry)
                    .andThen(selectBestOfferCoupon)
                    .andThen(saveCouponProducts)
                    .apply(contextCalculateBestOfferCoupon)
                    .getItem(COUPON, Coupon.class);

            contextCalculateBestOfferCoupon.clean();

            return couponRedeemed;
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "calculateBestOfferCoupon"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}
