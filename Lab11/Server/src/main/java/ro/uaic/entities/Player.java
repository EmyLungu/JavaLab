package ro.uaic.entities;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Transient;
import jakarta.persistence.Cacheable;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

/**
 * PlayerEntity
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double score = 0.0;

    public Player() {}
    public Player(String name) {
        this.name = name;
    }

    public void addScore(double score) {
        this.score += score;
    }

    public double getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    @Transient // == dont save to DB
    public Double response = 0.0;
    @Transient
    public long responseTime;
    @Transient
    public double lastScore;
}
