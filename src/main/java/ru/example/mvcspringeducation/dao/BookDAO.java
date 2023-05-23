package ru.example.mvcspringeducation.dao;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.example.mvcspringeducation.model.Book;
import ru.example.mvcspringeducation.model.Person;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query(
                "SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }
    public List<Book> noBookPerson() {
        return jdbcTemplate.query(
                "SELECT * FROM book WHERE book.person_id is null ", new BeanPropertyRowMapper<>(Book.class));
    }


    public Book show(UUID book_id){
        return jdbcTemplate.query(
                "SELECT * FROM book WHERE book_id=?", new BeanPropertyRowMapper<>(Book.class), book_id)
                .stream().findAny().orElse(null);
    }

    public Optional<Person> getOwnerBook (UUID book_id){
        return jdbcTemplate.query(
               "SELECT * FROM book JOIN person on book.person_id = person.person_id WHERE book_id=?",
                        new BeanPropertyRowMapper<>(Person.class),book_id)
                .stream().findAny();
    }

    public void save(Book book){
        jdbcTemplate.update(
                "INSERT into book(name, author, year) VALUES(?,?,?)",
                book.getName(), book.getAuthor(), book.getYear());
    }

    public void update(UUID book_id, Book updateBook){
        jdbcTemplate.update(
                "UPDATE book SET name=?, author=?, year=? WHERE book_id=?",
                updateBook.getName(), updateBook.getAuthor(),updateBook.getYear(),book_id);
    }
    public void delete(UUID book_id){
        jdbcTemplate.update("DELETE FROM book WHERE book_id=?", book_id);
    }

    public void deleteOwnerBook(UUID book_id){
        jdbcTemplate.update(
                "UPDATE book SET person_id=null WHERE book_id=?",book_id);
    }

    public void appointOwnerBook(UUID book_id, UUID person_id){
        jdbcTemplate.update(
                "UPDATE book SET person_id=? WHERE book_id=?", person_id ,book_id);
    }

    public void BatchUpdate(int size){
        List<Book> books = createRandomBook(size);
        jdbcTemplate.batchUpdate("INSERT INTO book(name, author, year) VALUES (?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, books.get(i).getName());
                        ps.setString(2, books.get(i).getAuthor());
                        ps.setInt(3, books.get(i).getYear());
                    }
                    @Override
                    public int getBatchSize() {
                        return books.size();
                    }
                });
    }

    private List<Book> createRandomBook(int size) {
        List<Book> books = new ArrayList<>();
        Faker faker = new Faker(new Locale("ru"));
        for(int i = 0; i < size; i++){
            books.add(new Book(
                    faker.book().title(),
                    faker.book().author(),
                    faker.number().numberBetween(1900,2023)
                    )
            );
        }
        return books;
    }
}


