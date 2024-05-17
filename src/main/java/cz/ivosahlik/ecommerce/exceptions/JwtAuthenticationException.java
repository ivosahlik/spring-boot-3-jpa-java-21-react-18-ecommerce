package cz.ivosahlik.ecommerce.exceptions;

public class JwtAuthenticationException extends RuntimeException {

    public JwtAuthenticationException(String message){super(message);}

}
