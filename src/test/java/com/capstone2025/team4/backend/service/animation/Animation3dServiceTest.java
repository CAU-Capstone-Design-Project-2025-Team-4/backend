package com.capstone2025.team4.backend.service.animation;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.animation3d.Animation3D;
import com.capstone2025.team4.backend.domain.animation3d.Animation3dType;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.repository.animation3d.Animation3dRepository;
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
class Animation3dServiceTest {

    @Autowired
    Animation3dService animation3dService;

    @Autowired
    Animation3dRepository animation3dRepository;

    @Autowired
    EntityManager em;

    class UserElementIds {
        private final User user;
        private final Spatial spatial;
        private final Animation3D animation3D;

        public UserElementIds(User user, Spatial spatial, Animation3D animation3D) {
            this.user = user;
            this.spatial = spatial;
            this.animation3D = animation3D;
        }

        @Override
        public String toString() {
            return "UserElementIds{" +
                    "user=" + user +
                    ", spatial=" + spatial +
                    ", animation3D=" + animation3D +
                    '}';
        }
    }
    private UserElementIds prepareUsersElementWithAnimation3D(String userEmail, boolean needsAnimation3D) {
        User user = User.builder()
                .email(userEmail)
                .build();

        Design design = Design.builder()
                .user(user).build();

        Slide slide = Slide.builder()
                .design(design)
                .build();

        Spatial spatial = Spatial.builder()
                .slide(slide)
                .build();

        em.persist(user);
        em.persist(design);
        em.persist(slide);
        em.persist(spatial);

        if (needsAnimation3D) {
            Animation3D animation = Animation3D.builder()
                    .type(Animation3dType.NORMAL)
                    .spatial(spatial)
                    .build();
            em.persist(animation);
            return new UserElementIds(user, spatial, animation);
        }

        return new UserElementIds(user, spatial, null);
    }

    @Test
    void addAnimationToElement(){
        //given
        UserElementIds userElementIds = prepareUsersElementWithAnimation3D("userEmail", false);

        em.flush();
        em.clear();

        //when
        Animation3D animation = animation3dService.addAnimationToElement(userElementIds.user.getId(), userElementIds.spatial.getId(), Animation3dType.SLOW);

        //then
        assertThat(animation).isNotNull();
        assertThat(animation.getId()).isNotNull();
        assertThat(animation.getType()).isEqualTo(Animation3dType.SLOW);
        assertThat(animation.getSpatial().getId()).isEqualTo(userElementIds.spatial.getId());
    }


    @Test
    void deleteAnimation(){
        //given
        Spatial spatial = Spatial.builder()
                .build();
        Animation3dType type = Animation3dType.FAST;
        Animation3D animation3D = Animation3D.builder()
                .spatial(spatial)
                .type(type)
                .build();

        em.persist(spatial);
        em.persist(animation3D);

        em.flush();
        em.clear();

        //when, then
        Assertions.assertDoesNotThrow(() -> animation3dService.deleteAnimationFromElement(animation3D.getId(), spatial.getId()));
        assertThat(em.find(Animation3D.class, animation3D.getId())).isNull();
    }

    @Test
    @Rollback(value = false)
    public void testCopyElementsAnimations() {
        // given
        UserElementIds src = prepareUsersElementWithAnimation3D("src", true);
        UserElementIds dest = prepareUsersElementWithAnimation3D("dest", false);
        System.out.println("src = " + src);
        System.out.println("dest = " + dest);

        em.flush();
        em.clear();

        // when
        Spatial destElement = em.find(Spatial.class, dest.spatial.getId());
        animation3dService.copyElementsAnimations(src.spatial.getId(), destElement);

        // then
        List<Animation3D> copied = animation3dRepository.findAllBySpatialId(dest.spatial.getId());
        assertThat( copied.size()).isEqualTo(1);
        assertThat(copied.getFirst().getId()).isNotEqualTo(src.animation3D.getId());
        assertThat( copied.getFirst().getType()).isEqualTo(src.animation3D.getType());
    }

    @Test
    void allInSlide(){
        //given
        Slide slide = Slide.builder()
                .build();

        Spatial spatial1 = Spatial.builder()
                .slide(slide)
                .build();

        Spatial spatial2 = Spatial.builder()
                .slide(slide)
                .build();


        Animation3D animation1 = Animation3D.builder()
                .spatial(spatial1).build();

        Animation3D animation2 = Animation3D.builder()
                .spatial(spatial2).build();

        em.persist(slide);
        em.persist(spatial1);
        em.persist(spatial2);
        em.persist(animation1);
        em.persist(animation2);

        em.flush();
        em.clear();

        //when
        List<Animation3D> result = animation3dService.getAnimationsInSlide(slide.getId());

        //then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting(Animation3D::getId).containsOnly(animation1.getId(), animation2.getId());
    }
}