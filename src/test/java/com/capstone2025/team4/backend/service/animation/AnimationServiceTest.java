package com.capstone2025.team4.backend.service.animation;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.animation.Animation;
import com.capstone2025.team4.backend.domain.animation.AnimationTiming;
import com.capstone2025.team4.backend.domain.animation.AnimationType;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.repository.animation.AnimationRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AnimationServiceTest {

    public static final AnimationType ANIMATION_TYPE = AnimationType.APPEAR;
    public static final AnimationTiming ANIMATION_TIMING = AnimationTiming.ON_CLICK;
    public static final int ANIMATION_DURATION = 1000;
    @Autowired
    AnimationService animationService;

    @Autowired
    AnimationRepository animationRepository;

    @Autowired
    EntityManager em;

    class UserElementIds {
        private final User user;
        private final Element element;
        private final Animation animation;

        public UserElementIds(User user, Element element, Animation animation) {
            this.user = user;
            this.element = element;
            this.animation = animation;
        }

        @Override
        public String toString() {
            return "UserElementIds{" +
                    "user=" + user +
                    ", element=" + element +
                    ", animation=" + animation +
                    '}';
        }
    }
    private UserElementIds prepareUsersElementWithAnimation(String userEmail, boolean needsAnimation) {
        User user = User.builder()
                .email(userEmail)
                .build();

        Design design = Design.builder()
                .user(user).build();

        Slide slide = Slide.builder()
                .design(design)
                .build();

        Element element = Element.builder()
                .slide(slide)
                .build();

        em.persist(user);
        em.persist(design);
        em.persist(slide);
        em.persist(element);

        if (needsAnimation) {
            Animation animation = Animation.builder()
                    .element(element)
                    .type(ANIMATION_TYPE)
                    .duration(ANIMATION_DURATION)
                    .timing(ANIMATION_TIMING)
                    .build();
            em.persist(animation);
            return new UserElementIds(user, element, animation);
        }

        return new UserElementIds(user, element, null);
    }

    @Test
    void addAnimationToElement(){
        //given
        UserElementIds userElementIds = prepareUsersElementWithAnimation("userEmail", false);

        em.flush();
        em.clear();

        //when
        Animation animation = animationService.addAnimationToElement(userElementIds.user.getId(),
                userElementIds.element.getId(), ANIMATION_TYPE, ANIMATION_DURATION, ANIMATION_TIMING);

        //then
        assertThat(animation).isNotNull();
        assertThat(animation.getId()).isNotNull();
        assertThat(animation.getType()).isEqualTo(ANIMATION_TYPE);
        assertThat(animation.getDuration()).isEqualTo(ANIMATION_DURATION);
        assertThat(animation.getTiming()).isEqualTo(ANIMATION_TIMING);
        assertThat(animation.getElement().getId()).isEqualTo(userElementIds.element.getId());
    }


    @Test
    void deleteAnimation(){
        //given
        Element element = Element.builder()
                .build();
        AnimationType type = AnimationType.APPEAR;
        Integer duration = 10;
        AnimationTiming timing = AnimationTiming.ON_CLICK;
        Animation animation = Animation.builder()
                .element(element)
                .type(type)
                .duration(duration)
                .timing(timing).build();

        em.persist(element);
        em.persist(animation);

        em.flush();
        em.clear();

        //when, then
        Assertions.assertDoesNotThrow(() -> animationService.deleteAnimationFromElement(animation.getId(), element.getId()));
        assertThat(em.find(Animation.class, animation.getId())).isNull();
    }

    @Test
    @Rollback(value = false)
    public void testCopyElementsAnimations() {
        // given
        UserElementIds src = prepareUsersElementWithAnimation("src", true);
        UserElementIds dest = prepareUsersElementWithAnimation("dest", false);
        System.out.println("src = " + src);
        System.out.println("dest = " + dest);

        em.flush();
        em.clear();

        // when
        Element destElement = em.find(Element.class, dest.element.getId());
        animationService.copyElementsAnimations(src.element.getId(), destElement);

        // then
        List<Animation> copied = animationRepository.findAllByElementId(dest.element.getId());
        assertThat( copied.size()).isEqualTo(1);
        assertThat(copied.getFirst().getId()).isNotEqualTo(src.animation.getId());
        assertThat( copied.getFirst().getType()).isEqualTo(src.animation.getType());
        assertThat( copied.getFirst().getDuration()).isEqualTo(src.animation.getDuration());
    }

    @Test
    void allInSlide(){
        //given
        Slide slide = Slide.builder()
                .build();

        Element element1 = Element.builder()
                .slide(slide)
                .build();

        Element element2 = Element.builder()
                .slide(slide)
                .build();

        Animation animation1 = Animation.builder()
                .element(element1).build();

        Animation animation2 = Animation.builder()
                .element(element2).build();

        em.persist(slide);
        em.persist(element1);
        em.persist(element2);
        em.persist(animation1);
        em.persist(animation2);

        em.flush();
        em.clear();

        //when
        List<Animation> result = animationService.getAnimationsInSlide(slide.getId());

        //then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting(Animation::getId).containsOnly(animation1.getId(), animation2.getId());
    }
}