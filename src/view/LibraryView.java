package view;

import dto.Student;
import service.LibraryService;

import java.sql.SQLException;
import java.util.Scanner;

public class LibraryView {

    /**
     * 사용자 인터페이스를 처리하는 뷰 클래스
     */

    private final LibraryService service = new LibraryService();
    private final Scanner scanner = new Scanner(System.in);
    private Integer currentStudentId = null;
    private String currentStudentName = null;
    //private Student principalUser = null; principal = 접근주체

    public void start(){

        while (true){
            System.out.println("도서관리 시스템");
            if (currentStudentId == null){
                System.out.println("현재 로그아웃 상태입니다");
            } else {
                System.out.println("현재 로그인 유저" + currentStudentName);
            }

            System.out.println("도서추가");
            System.out.println("도서목록");
            System.out.println("도서검색");
            System.out.println("학생등록");
            System.out.println("학생목록");
            System.out.println("도서대출");
            System.out.println("대출중인 도서목록");
            System.out.println("도서반납");
            System.out.println("로그인");
            System.out.println("로그아웃");
            System.out.println("종료");
            System.out.print("선택 : ");

            int choice;

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); //버퍼키우기
            } catch (Exception e) {
                System.out.println("정수값을 입력해주세요");
                scanner.nextLine();
                continue;
            }

            try{
                switch (choice){
                    case 1:
                        System.out.println("도서추가");
                        break;
                    case 2:
                        System.out.println("도서목록");
                        break;
                    case 3:
                        System.out.println("도서검색");
                        break;
                    case 4:
                        System.out.println("학생등록");
                        break;
                    case 5:
                        System.out.println("학생목록");
                        break;
                    case 6:
                        borrowBook();
                        break;
                    case 7:
                        System.out.println("대출중인 도서목록");
                        break;
                    case 8:
                        System.out.println("도서반납");
                        break;
                    case 9:
                        login();
                        break;
                    case 10:
                        logout();
                        break;
                    case 11:
                        System.out.println("시스템종료");
                        scanner.close(); //자원해제
                        return;
                    default:
                        System.out.println("잘못된 입력입니다");
                }
            } catch (SQLException e){
                System.out.println("오류 : " + e.getMessage());
            }catch (Exception e){
                System.out.println("전체 오류 : " + e.getMessage());
            }

        }//end while
    }//end of start

    //로그인 기능 만들어 보기
    private void login() throws SQLException {
        if (currentStudentId != null){
            System.out.println("이미 로그인 된 상태");
            return;
        }
        System.out.println("학번 : ");
        String studentId = scanner.nextLine().trim();
        if (studentId.isEmpty()){
            System.out.println("학번을 입력하세요");
            return;
        }
        //학번을 입력 받았다면 실제 학번이 맞는지 확인
        //데이터 접근해서 해당하는 학번 비밀번호가 맞는지 조회

        //결과는 두가지 객체존재 , null
        Student studentDTO = service.authenticateStudent(studentId);
        if (studentDTO ==  null){
            System.out.println("존재 하지 않는 학번");
        }else {
            currentStudentId = studentDTO.getId();
            currentStudentName = studentDTO.getName();
            System.out.println("로그인성공" + currentStudentName);
        }
    }

    //로그아웃

    private void logout(){
        if(currentStudentId == null){
            System.out.println("이미 로그인 상태가 아님");
        }else {
            currentStudentId = null;
            currentStudentName = null;
            System.out.println("로그아웃됨");
        }
    }

    private void borrowBook() throws SQLException{
        //로그인이 먼저 되어야 student_id 값을 넘겨줄수 있다.
        if (currentStudentId == null){
            System.out.println("로그인 먼저 해주세요");
            return;
        }
        System.out.println("도서 ID : ");
        int bookId;
        try{

            bookId = scanner.nextInt();
            if (bookId <= 0){
                System.out.println("유효한 도서 ID를 입력해주세요");
                return;
            }

        } catch (Exception e) {
            System.out.println("유효한 도서 ID를 입력해주세요");
            return;
        }
        scanner.nextLine(); //버퍼비우기
        service.borrowBook(bookId, currentStudentId);
        System.out.println("대출 완료");
    }

    //메인함수
    public static void main(String[] args) {
        LibraryView libraryView = new LibraryView();
        libraryView.start();
    }

}//end of class
