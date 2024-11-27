package com.mercadolibre.coupon.application.inbound.mercadolibre;

import com.mercadolibre.coupon.application.inbound.CouponService;
import com.mercadolibre.coupon.application.inbound.mercadolibre.steps.CalculateProductsByCoupon;
import com.mercadolibre.coupon.application.inbound.mercadolibre.steps.FetchProducts;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextCoupon;
import com.mercadolibre.coupon.domain.context.MessageContextEnum;
import com.mercadolibre.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static com.mercadolibre.coupon.crosscutting.constants.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextCoupon.COUPON;
import static java.lang.String.format;

@Log4j2
@Service
@RequiredArgsConstructor
public class CouponIterativeService implements CouponService {

    private static final String CLASS_NAME = CouponIterativeService.class.getSimpleName();

    // Steps
    private final FetchProducts fetchProducts;
    private final CalculateProductsByCoupon calculateProductsByCoupon;


    // Private Methods
    private MessageContext<Enum<MessageContextCoupon>, Object> initialPipelineData(Coupon coupon) {
        MessageContext<Enum<MessageContextCoupon>, Object> context = new MessageContextEnum(MessageContextCoupon.class);
        context.addItem(COUPON, coupon);

        return context;
    }

    // Custom Methods
    @Override
    public Coupon calculateProductsCoupon(Coupon coupon) {
        try {
            return fetchProducts
                    .andThen(calculateProductsByCoupon)
                    .apply(this.initialPipelineData(coupon))
                    .getItem(COUPON, Coupon.class);
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "calculateProductsCoupon"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}
