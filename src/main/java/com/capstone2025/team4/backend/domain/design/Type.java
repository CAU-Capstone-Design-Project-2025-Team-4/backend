package com.capstone2025.team4.backend.domain.design;

public enum Type {
    IMAGE, ICON, SHAPE, LINE, MODEL, TEXT;

    public boolean isTextType() {
        return this == Type.TEXT || this == Type.ICON || this == Type.LINE || this == Type.SHAPE;
    }

    public boolean isFileType() {
        return this == Type.MODEL || this == Type.IMAGE;
    }
}
