package com.soldano;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.soldano.exception.BookNotFoundException;
import com.soldano.model.Book;
import com.soldano.repository.BookRepository;
import org.apache.http.HttpStatus;

import java.util.Map;

import static com.soldano.utils.ApiGatewayUtils.getPathParamValue;
import static com.soldano.utils.ApiGatewayUtils.getResponse;
import static com.soldano.utils.MapperUtils.mapToJsonString;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final BookRepository bookRepository;
    private static final String PATH_PARAM_AUTHOR = "author";
    private static final String PATH_PARAM_ISBN = "isbn";
    private static final String TABLE_NAME_BOOKS = "Books";

    public Handler() {
        this.bookRepository = new BookRepository();
    }

    //Constructor for unit testing only
    public Handler(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent req, Context context) {
        String author = getPathParamValue(req, PATH_PARAM_AUTHOR);
        String isbn = getPathParamValue(req, PATH_PARAM_ISBN);
        try {
            Book book = bookRepository.getItemByPrimaryKey(TABLE_NAME_BOOKS, Map.of(PATH_PARAM_AUTHOR, author, PATH_PARAM_ISBN, isbn));

            return getResponse(HttpStatus.SC_OK, mapToJsonString(book));
        } catch (BookNotFoundException e) {
            return getResponse(HttpStatus.SC_NOT_FOUND);
        }
    }
}
