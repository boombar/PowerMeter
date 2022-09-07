package io.nqode.powermeter.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "meter")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "meter_identifier", nullable = false, unique = true)
    private String meterIdentifier;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "meter", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<Reading> readings = new ArrayList<>();

}