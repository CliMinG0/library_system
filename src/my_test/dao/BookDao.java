package my_test.dao;

import my_test.dto.Book;
import my_test.util.DataBaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {

    public void addBook(Book book) throws SQLException {
        String sql "INSERT INTO books (title, author, publisher, publication_year, isbn) " +
                "VALUES (?, ?, ?, ?, ?) ";
        try(Connection conn = DataBaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setInt(4, book.getPublicationYear());
            pstmt.setString(5, book.getIsbn());
            pstmt.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM books ";
        try(Connection conn = DataBaseUtil.getConnection());
        Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt("id");
            }
            }
        }
    }
}
