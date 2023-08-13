import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.soldano.exception.BookNotFoundException;
import com.soldano.model.Book;
import com.soldano.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRepositoryTest {

    @InjectMocks
    private BookRepository bookRepository;

    @Mock
    private DynamoDB dynamoDB;

    @Mock
    Table table;

    @Mock
    Item item;

    @Test
    void testBookRepository_Success() throws BookNotFoundException {
        when(dynamoDB.getTable(anyString())).thenReturn(table);
        when(table.getItem((PrimaryKey) any())).thenReturn(item);
        when(item.toJSON()).thenReturn("{\"author\": \"Brandon Sanderson\"}");

        Book actual = bookRepository.getItemByPrimaryKey("Books", Map.of("author", "Brandon Sanderson"));

        assertEquals("Brandon Sanderson", actual.getAuthor());
    }

    @Test
    void testBookRepository_BookNotFound() throws BookNotFoundException {
        when(dynamoDB.getTable(anyString())).thenReturn(table);
        when(table.getItem((PrimaryKey) any())).thenReturn(null);

        assertThrows(BookNotFoundException.class, () ->
                bookRepository.getItemByPrimaryKey("Books", Map.of("author", "Brandon Sanderson"))
        );
    }
}