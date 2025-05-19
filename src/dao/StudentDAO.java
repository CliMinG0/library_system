package dao;

import dto.Student;
import util.DataBaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    //새학생을 데이터 베이스에 추가하는 기능
    public void addStudent(Student student) throws SQLException{
        String sql = "INSERT INTO students (name, student_id) values (?, ?) ";
        try(Connection conn  = DataBaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getStudentId());
            pstmt.executeQuery();
        }
    }

    //모든학생을 조회한 기능
    public List<Student> getAllStudents() throws SQLException {
        List<Student> studentList = new ArrayList<>();
        String sql = "SELECT * FROM students ";
        try(Connection conn  = DataBaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Student studentdto = new Student();
                studentdto.setId(rs.getInt("id"));
                studentdto.setName(rs.getString("name"));
                studentdto.setStudentId(rs.getString("student_id"));
                studentList.add(studentdto);
            }
        }
        return studentList;
    }

    //student id 로 학생인증 기능 만들기
    public Student authenticateStudent(String studentId) throws  SQLException{
        //학생이 정확한 학번을 입력하면 Student 객체가 만들어져 리턴됨
        //학생이 잘못된 학번을 입력하면 null값을 반환
        //if -- return new student();
        String sql = "SELECT * FROM books where title like '?'";
        try(Connection conn  = DataBaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                Student studentDTO = new Student();
                studentDTO.setId(rs.getInt("id"));
                studentDTO.setName(rs.getString("name"));
                studentDTO.setStudentId(rs.getString("student_id"));
                return studentDTO;
            }
        }
        return null;
    }
}
