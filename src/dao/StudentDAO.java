package dao;

import dto.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    //새학생을 데이터 베이스에 추가하는 기능
    public void addStudent(Student student) throws SQLException{
        String sql = "INSERT INTO books (title, author, publisher, publication_year, isbn) " +
                "values (?, ?, ?, ?, ?) ";
    }

    //모든학생을 조회한 기능
    public List<Student> getAllStudents() throws SQLException {
        List<Student> studentList = new ArrayList<>();

        return studentList;
    }

    //student id 로 학생인증 기능 만들기
    public Student authenticateStudent(String studentId) throws  SQLException{
        //학생이 정확한 학번을 입력하면 Student 객체가 만들어져 리턴됨
        //학생이 잘못된 학번을 입력하면 null값을 반환
        //if -- return new student();

        return null;
    }
}
