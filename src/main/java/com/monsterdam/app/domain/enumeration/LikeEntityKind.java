package com.monsterdam.app.domain.enumeration;

/**
 * The LikeEntityKind enumeration.
 */
public enum LikeEntityKind {
    VIDEO("multimedia.entity.kind.video"),
    PHOTO("multimedia.entity.kind.photo"),
    POST("like.entityType.post"),
    MESSAGE("like.entityType.message"),
    COMMENT("like.entityType.comment"),
    STORY("like.entityType.videoStory");

    private final String value;

    LikeEntityKind(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
