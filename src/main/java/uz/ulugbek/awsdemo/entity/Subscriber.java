package uz.ulugbek.awsdemo.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscribers")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscribers_id_seq")
    @SequenceGenerator(name = "subscribers_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "arn")
    private String arn;

}
