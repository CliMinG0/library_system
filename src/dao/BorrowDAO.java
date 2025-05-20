package dao;

import dto.Borrow;
import util.DataBaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Borrow> getBorrowBooks () throws SQLException {
        List<Borrow> borrowList = new ArrayList<>();
        String sql = "select * from borrows where return_date is null ";
        try (Connection conn = DataBaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                Borrow borrowDTO = new Borrow();
                borrowDTO.setId(rs.getInt("id"));
                borrowDTO.setBookId(rs.getInt("Book_id"));
                borrowDTO.setStudentId(rs.getInt("Student_id"));
                //JAVA DTO에서 데이터 타입은 LOCALDATE이다
                //하지만 JDBC API에서 아직은 LocalDate 타입을 지원하지 않는다
                //JDBC API제공하는 날짜 데이터 타입은 Date이다
                //rs.getLocalDate - 아직 지원안함
                //rs.getDate("borrow_date");
                borrowDTO.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
                borrowList.add(borrowDTO);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return borrowList;
    }

    //도서반납을 처리하는 기능

    //borrows 테이블에 책 정보 조회 - select (복합조건)
    //borrows 테이블에 return_date 수정 - update
    //books 테이블에 availdable 수정 - update
    public void returnBook(int bookId, int studentPK) throws SQLException{
        //studentPK - borrow 테이블에 student_id 컬럼이다
        //students 테이블에 pk 의미
        //try with resource
        //try catch
        //트랜잭션
        Connection conn = null;

        try {
            conn = DataBaseUtil.getConnection();
            //트랜잭션 시작
            conn.setAutoCommit(false);

            //이 쿼리의 결과 집합에서 필요한것은 borrows의 PK(id)의 값이다
            int borrowId = 0;
            String checkSql = "SELECT * FROM borrows " +
                                "WHERE book_id = ? " +
                                "AND student_id  = ? " +
                                "AND return_date IS NULL ";

            try(PreparedStatement checkPstmt = conn.prepareStatement(checkSql)){
                checkPstmt.setInt(1,bookId);
                checkPstmt.setInt(2,studentPK);
                ResultSet rs = checkPstmt.executeQuery();
                if (!rs.next()){
                    throw new SQLException("해당 대출 기록이 존재 않거나 이미 반납 되었습니다");
                }
                borrowId = rs.getInt("id");
            }


            String updateBorrowSql = "UPDATE borrows SET return_date = ? WHERE id = ? ";
            String updateBookSql = "UPDATE books SET avaliable = true WHERE id = ? ";

            try(PreparedStatement borrowPstmt = conn.prepareStatement(updateBorrowSql);
                PreparedStatement bookPstmt = conn.prepareStatement(updateBookSql)){
                borrowPstmt.setInt(1,borrowId);
                borrowPstmt.executeUpdate();

                bookPstmt.setInt(1,bookId);
                bookPstmt.executeUpdate();
            }

            conn.commit();

            //1.반납하려는 특정 책을 찾아야 함 (book_id)
            //2.책을 빌린 학생을 찾기위함 (student_id)
            //3.다른책을 빌린 이력도 있을 수 있다 (다른 혼동을 막기위해)
            //4.아직 반납 되지 않은 대출 기록만 찾아야함
            //5.같은 학생이 예전에 여러번 빌린 이력이 있을수 있다.

            //conn.commit(); 영구히 저장
        } catch (SQLException e) {
            if (conn != null){
                conn.rollback(); //오류 발생시 롤백처리
            }
            System.err.println("rollback err");
        } finally {
            if (conn != null){
                conn.setAutoCommit(true); //오토커밋 재설정
                conn.close(); //자원 닫아야 메모리 누수가 안 일어남
            }
        }
    }



    public static void main(String[] args) {

        BorrowDAO borrowDAO = new BorrowDAO();

        try {
            borrowDAO.borrowBook(1,3);
        //      현재 대출중인 책 목록 조회
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
