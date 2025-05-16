package deu.rest.api.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//공지사항
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    private String title;
    private String date;
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "infoSquare_Id", referencedColumnName = "infoSquareId")
    private InfoSquare infoSquareId;
}
