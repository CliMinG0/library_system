package dao;

import util.DataBaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowDAO {

    //도서 대출을 처리 기능
    public void borrowBook(int bookId, int studentPk) throws SQLException{
        //대출 가능여부 - select (books)
        //대출 가능하다면 - insert(borrows)
        //대출이 실행되었다면 - update(student)

        String checkSQL = "select available from books where id = ? ";
        try(Connection conn = DataBaseUtil.getConnection();
            PreparedStatement checkPstmt = conn.prepareStatement(checkSQL)) {
            checkPstmt.setInt(1,bookId);
            ResultSet rs1 = checkPstmt.executeQuery();
            if (rs1.next() && rs1.getBoolean("available")){
                //insert,update
                String insertSql = "insert into borrows (student_id, book_id, borrow_date) \n" +
                        "values (?, ?, CURRENT_DATE) ";
                String updateSql = "update books set available = false where id = ? ";

                try(PreparedStatement borrowStmt = conn.prepareStatement(insertSql);
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    borrowStmt.setInt(1,studentPk); //borrow
                    borrowStmt.setInt(2,bookId);
                    updateStmt.setInt(1,bookId); //update
                    borrowStmt.executeUpdate(); //excute
                    updateStmt.executeUpdate();
                }

            }else {
                throw new SQLException("도서가 대출 불가능");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static void main(String[] args) {

        BorrowDAO borrowDAO = new BorrowDAO();
        try {
            borrowDAO.borrowBook(1,3);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
