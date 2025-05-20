package my_test.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Borrow {
    private int id;
    private int studentId;
    private int bookId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
}
