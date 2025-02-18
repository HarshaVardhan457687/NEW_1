//package com.api_gateway.api_gateway.filter;
//
//import jakarta.ws.rs.core.HttpHeaders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestClient;
//import reactor.core.publisher.Mono;
//import org.springframework.web.server.ServerWebExchange;
//
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
//
//    public AuthenticationFilter() {
//        super(Config.class);
//    }
//
//    public static class Config{
//    }
//
//    @Autowired
//    private RouteValidator validator;
////    @Autowired
////    private RestClient restClient;
//
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return ((exchange, chain)->{
////for the uris NOT specified in the RouteValidator do the following steps
//            if(validator.isSecured.test(exchange.getRequest())) {
//// check if the exchange request header contains the Authorization header
//                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                    return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
//                }
//// take out the AUthorization header
//                String authHeaderToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
//                if(authHeaderToken != null && authHeaderToken.startsWith("Bearer")) {
//// remove Bearer from front
//                    authHeaderToken = authHeaderToken.substring(7);
//                }
//                try {
//// now consume /api/auth/validate/token of authentication-service using RestClient
//// can keep this call in a seperate JwtUtil class and call
//                    RestClient restClient=RestClient.create();
//                    System.out.println("hiiiii");
//                    //restClient.get().uri("http://localhost:8088/api/auth/validate/token?token="+authHeaderToken).retrieve().body(Boolean.class);
//                    System.out.println("helloooo");
//                    if(validator.requiresAdminAccess.test(exchange.getRequest())){
//                        restClient.get()
//                                .uri("http://localhost:8088/api/admin")
//                                .header(HttpHeaders.AUTHORIZATION,"Bearer "+authHeaderToken)
//                                .retrieve()
//                                .body(Boolean.class);
//                        System.out.println("====================================================================================");
//                    }
//// also instead of making a RestClient call for every request, we can validate the token here in api-gateway itself
//                }catch(Exception e) {
//                    System.out.println(e.getMessage());
//                    return onError(exchange, "Invalid Access: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//                }
//            }
//
////for other uris simply chain the request.
//            return chain.filter(exchange);
//
//        });
//
//
//    }
//    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
//        exchange.getResponse().setStatusCode(status);
//        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
//        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
//        DataBuffer buffer = dataBufferFactory.allocateBuffer(bytes.length);
//        buffer.write(bytes);
//        return exchange.getResponse().writeWith(Mono.just(buffer));
//    }
//
//}