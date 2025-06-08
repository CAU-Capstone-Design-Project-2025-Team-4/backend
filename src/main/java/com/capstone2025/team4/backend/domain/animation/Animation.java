package com.capstone2025.team4.backend.domain.animation;

import com.capstone2025.team4.backend.domain.element.Element;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@SuperBuilder
public class Animation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "element_id")
    private Element element;

    @Enumerated(value = EnumType.STRING)
    private AnimationType animationType;

    private Integer duration;

    @Enumerated(value = EnumType.STRING)
    private AnimationTiming timing;

    public Animation copy(Element element) {
        Animation copy = createNewInstance();
        copy.element = element;
        copy.animationType = this.animationType;
        copy.duration = this.duration;
        copy.timing = this.timing;
        copySpecific(copy);
        return copy;
    }

    protected Animation createNewInstance() {
        return new Animation();
    }
    protected void copySpecific(Animation destAnimation) {
    }
}
