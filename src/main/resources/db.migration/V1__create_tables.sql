CREATE SEQUENCE IF NOT EXISTS chapter_seq START WITH 11 INCREMENT BY 1;

CREATE TABLE chapters
(
    id         BIGINT NOT NULL,
    group_name VARCHAR(255),
    user_id    BIGINT,
    CONSTRAINT pk_chapters PRIMARY KEY (id)
);

CREATE TABLE chapters_friends
(
    chapter_id BIGINT NOT NULL,
    friends_id BIGINT NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS chat_seq START WITH 21 INCREMENT BY 1;

CREATE TABLE chats
(
    id          BIGINT NOT NULL,
    sender_id   BIGINT,
    receiver_id BIGINT,
    CONSTRAINT pk_chats PRIMARY KEY (id)
);

CREATE TABLE chats_link_publications
(
    chat_id              BIGINT NOT NULL,
    link_publications_id BIGINT NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS comment_seq START WITH 21 INCREMENT BY 1;

CREATE TABLE comments
(
    id             BIGINT NOT NULL,
    message        VARCHAR(255),
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    user_id        BIGINT,
    publication_id BIGINT,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);

CREATE TABLE comments_likes
(
    comment_id BIGINT NOT NULL,
    likes_id   BIGINT NOT NULL
);

CREATE TABLE comments_notifications
(
    comment_id       BIGINT NOT NULL,
    notifications_id BIGINT NOT NULL
);

CREATE TABLE inner_comment
(
    comment_id        BIGINT NOT NULL,
    inner_comments_id BIGINT NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS edu_seq START WITH 21 INCREMENT BY 1;

CREATE TABLE educations
(
    id                      BIGINT NOT NULL,
    country                 VARCHAR(255),
    educational_institution VARCHAR(255),
    profile_id              BIGINT,
    CONSTRAINT pk_educations PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS likes_seq START WITH 21 INCREMENT BY 1;

CREATE TABLE likes
(
    id      BIGINT NOT NULL,
    user_id BIGINT,
    CONSTRAINT pk_likes PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS link_seq START WITH 21 INCREMENT BY 1;

CREATE TABLE link_publications
(
    id   BIGINT NOT NULL,
    link VARCHAR(10000),
    CONSTRAINT pk_link_publications PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS message_content_seq START WITH 19 INCREMENT BY 1;

CREATE TABLE message_content
(
    id               BIGINT  NOT NULL,
    content          VARCHAR(255),
    timestamp        TIMESTAMP WITHOUT TIME ZONE,
    read_or_not_read BOOLEAN NOT NULL,
    chat_id          BIGINT,
    CONSTRAINT pk_message_content PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS not_seq START WITH 11 INCREMENT BY 1;

CREATE TABLE notifications
(
    id                   BIGINT  NOT NULL,
    created_at           TIMESTAMP WITHOUT TIME ZONE,
    seen                 BOOLEAN NOT NULL,
    notification_message VARCHAR(255),
    like_id              BIGINT,
    user_notification_id BIGINT,
    sender_user_id       BIGINT,
    comment_id           BIGINT,
    publication_id       BIGINT,
    story_id             BIGINT,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS public_p_seq START WITH 21 INCREMENT BY 1;

CREATE TABLE pablic_profiles
(
    id                 BIGINT NOT NULL,
    cover              VARCHAR(10000),
    avatar             VARCHAR(10000),
    pablic_name        VARCHAR(255),
    description_public VARCHAR(255),
    tematica           VARCHAR(255),
    owner_id           BIGINT,
    CONSTRAINT pk_pablic_profiles PRIMARY KEY (id)
);

CREATE TABLE pablic_profiles_block_users
(
    pablic_profile_id BIGINT NOT NULL,
    block_users_id    BIGINT NOT NULL
);

CREATE TABLE pablic_profiles_users
(
    pablic_profile_id BIGINT NOT NULL,
    users_id          BIGINT NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS aa START WITH 21 INCREMENT BY 1;

CREATE TABLE profiles
(
    id              BIGINT  NOT NULL,
    avatar          VARCHAR(10000),
    cover           VARCHAR(10000),
    about_your_self VARCHAR(1000),
    first_name      VARCHAR(255),
    last_name       VARCHAR(255),
    patronymic_name VARCHAR(255),
    profession      VARCHAR(255),
    phone_number    VARCHAR(255),
    work_or_not     BOOLEAN NOT NULL,
    location        VARCHAR(255),
    user_id         BIGINT,
    CONSTRAINT pk_profiles PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS publications_seq START WITH 21 INCREMENT BY 1;

CREATE TABLE publications
(
    id                BIGINT  NOT NULL,
    description       VARCHAR(255),
    created_at        TIMESTAMP WITHOUT TIME ZONE,
    updated_at        TIMESTAMP WITHOUT TIME ZONE,
    location          VARCHAR(255),
    is_block_comment  BOOLEAN NOT NULL,
    owner_id          BIGINT,
    pablic_profile_id BIGINT,
    CONSTRAINT pk_publications PRIMARY KEY (id)
);

CREATE TABLE publications_likes
(
    publication_id BIGINT NOT NULL,
    likes_id       BIGINT NOT NULL
);

CREATE TABLE publications_link_publications
(
    publication_id       BIGINT NOT NULL,
    link_publications_id BIGINT NOT NULL
);

CREATE TABLE publications_tag_friends
(
    publication_id BIGINT NOT NULL,
    tag_friends_id BIGINT NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS story_seq START WITH 19 INCREMENT BY 1;

CREATE TABLE stories
(
    id         BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    text       VARCHAR(255),
    owner_id   BIGINT,
    CONSTRAINT pk_stories PRIMARY KEY (id)
);

CREATE TABLE stories_likes
(
    story_id BIGINT NOT NULL,
    likes_id BIGINT NOT NULL
);

CREATE TABLE stories_link_publications
(
    story_id             BIGINT NOT NULL,
    link_publications_id BIGINT NOT NULL
);

CREATE TABLE stories_tag_friends
(
    story_id       BIGINT NOT NULL,
    tag_friends_id BIGINT NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS user_seq START WITH 21 INCREMENT BY 1;

CREATE TABLE users
(
    id                BIGINT NOT NULL,
    user_name         VARCHAR(255),
    email             VARCHAR(255),
    password          VARCHAR(255),
    role              VARCHAR(255),
    is_block          BOOLEAN,
    block_account     BOOLEAN,
    confirmation_code VARCHAR(255),
    created_at        TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);