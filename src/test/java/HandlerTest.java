import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.tests.EventLoader;
import com.soldano.Handler;
import com.soldano.exception.BookNotFoundException;
import com.soldano.model.Book;
import com.soldano.repository.BookRepository;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandlerTest {

    @InjectMocks
    private Handler handler;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private Context context;

    @Test
    void testHandleRequest_Success() throws BookNotFoundException {
        APIGatewayProxyRequestEvent request = EventLoader.loadApiGatewayRestEvent("get_book_request_example.json");
        when(bookRepository.getItemByPrimaryKey(any(), any())).thenReturn(new Book());

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        assertEquals(HttpStatus.SC_OK, response.getStatusCode());

    }

    @Test
    void testHandleRequest_BookNotFound() throws BookNotFoundException {
        APIGatewayProxyRequestEvent request = EventLoader.loadApiGatewayRestEvent("get_book_request_example.json");
        when(bookRepository.getItemByPrimaryKey(any(), any())).thenThrow(new BookNotFoundException("Not found"));

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode());
    }
}