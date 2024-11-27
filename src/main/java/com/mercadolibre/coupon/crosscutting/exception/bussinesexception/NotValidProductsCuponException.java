package com.mercadolibre.coupon.crosscutting.exception.bussinesexception;

public class NotValidProductsCuponException extends RuntimeException {

	private static final long serialVersionUID = 7802114020056418465L;

	
	public NotValidProductsCuponException(String message ) {
		super(message);
	}
	
	public NotValidProductsCuponException(String message, Throwable cause) {
		super(message, cause);
	}

}
