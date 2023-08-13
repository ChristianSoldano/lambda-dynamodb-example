package com.soldano.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.soldano.exception.BookNotFoundException;
import com.soldano.model.Book;

import java.util.Map;

import static com.soldano.utils.MapperUtils.mapFromJsonString;

public class BookRepository {

    private final DynamoDB dynamoDB;

    public BookRepository() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        this.dynamoDB = new DynamoDB(client);
    }

    //Constructor for unit testing only
    public BookRepository(DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    public Book getItemByPrimaryKey(String tableName, Map<String, String> primaryKey) throws BookNotFoundException {
        Table table = dynamoDB.getTable(tableName);
        KeyAttribute[] keyAttributes = primaryKey.entrySet().stream()
                .map(entry -> new KeyAttribute(entry.getKey(), entry.getValue()))
                .toArray(KeyAttribute[]::new);
        Item item = table.getItem(new PrimaryKey(keyAttributes));
        if (item == null) {
            throw new BookNotFoundException("Book not found");
        }

        return mapFromJsonString(item.toJSON(), Book.class);
    }
}
