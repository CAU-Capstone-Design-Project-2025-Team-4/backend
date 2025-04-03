package com.capstone2025.team4.backend.domain.design.element;

import com.capstone2025.team4.backend.domain.design.Type;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "format")
@SuperBuilder
public class Element {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Type type;

    @Column(nullable = false)
    private Boolean isDefault;

    private Long x;

    private Long y;

    private Long width;

    private Long height;

    private Double angle;

}