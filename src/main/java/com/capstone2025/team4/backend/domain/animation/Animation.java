package com.capstone2025.team4.backend.domain.animation;

import com.capstone2025.team4.backend.domain.element.Element;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Animation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id")
    private Element element;

    @Enumerated(value = EnumType.STRING)
    private AnimationType type;

    private Integer duration;

    @Enumerated(value = EnumType.STRING)
    private AnimationTiming timing;

    @Builder
    public Animation(Element element, AnimationType type, Integer duration, AnimationTiming timing) {
        this.element = element;
        this.type = type;
        this.duration = duration;
        this.timing = timing;
    }

    public Animation copy(Element element) {
        Animation copy = new Animation();
        copy.element = element;
        copy.type = this.type;
        copy.duration = this.duration;
        copy.timing = this.timing;
        return copy;
    }
}
