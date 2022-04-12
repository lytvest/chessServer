package ru.lytvest.chessserver.entities;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NonNull
    @Column(length = 5)
    String white;

    @NonNull
    @Column(length = 5)
    String black;

    @NonNull
    int number;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    Game game;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Turn turn = (Turn) o;
        return id == turn.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
