package ru.lytvest.chessserver.entities;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;



    @NonNull
    @Column(length = 100)
    String playerWhite;

    @NonNull
    @Column(length = 100)
    String playerBlack;

    @NonNull
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game")
    @ToString.Exclude
    List<Turn> turns;

    @NonNull
    String winner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Game game = (Game) o;
        return id == game.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void moveWhite(String turn) {
        turns.add(new Turn(turn, "x", turns.size() + 1, this));
    }
    public void moveBlack(String turn) {
        if (turns.isEmpty() || !"x".equals(turns.get(turns.size() - 1).getBlack())){
            System.err.println("Game non correct add turn black " + turn);
            return;
        }
        turns.get(turns.size() - 1).setBlack(turn);
    }

}
