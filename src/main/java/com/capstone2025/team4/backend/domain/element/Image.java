package com.capstone2025.team4.backend.domain.element;

import com.capstone2025.team4.backend.domain.File;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("TEXT_BOX")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class Image extends Element {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    @Override
    protected Element createNewInstance() {
        return new Image();
    }

    @Override
    protected void copyElementFields(Element copy) {
        ((Image) copy).file = this.file;
    }
}
