package com.reactivespring.learnreactivespring.router;

import com.reactivespring.learnreactivespring.handler.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.reactivespring.learnreactivespring.constants.ItemConstants.FUNCTIONAL_ITEM_END_POINT_V1;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ItemsRouter {

    @Bean
    public RouterFunction<ServerResponse> itemsRoute(ItemsHandler itemsHandler) {
        return RouterFunctions
                .route(GET(FUNCTIONAL_ITEM_END_POINT_V1).and(accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::getAllItems)
                .andRoute(GET(FUNCTIONAL_ITEM_END_POINT_V1 + "/{id}").and(accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::getOneItem)
                .andRoute(POST(FUNCTIONAL_ITEM_END_POINT_V1).and(accept(MediaType.APPLICATION_JSON)),);
    }
}
