package io.nqode.powermeter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Month;

@Data
@Entity
@Table(name = "fraction")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Fraction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(name = "`month`", nullable = false)
    private Month month;

    @Column(name = "fraction_value", nullable = false)
    private BigDecimal fractionValue;

}
