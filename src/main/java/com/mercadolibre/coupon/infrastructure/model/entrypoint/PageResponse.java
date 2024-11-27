package com.mercadolibre.coupon.infrastructure.model.entrypoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -7678798685062147366L;

    private Integer nextPage;

    private Integer TotalPage;

    private Integer totalElements;

}
