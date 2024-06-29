ALTER TABLE chapters
    ADD CONSTRAINT FK_CHAPTERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE chapters_friends
    ADD CONSTRAINT fk_chafri_on_chapter FOREIGN KEY (chapter_id) REFERENCES chapters (id);

ALTER TABLE chapters_friends
    ADD CONSTRAINT fk_chafri_on_user FOREIGN KEY (friends_id) REFERENCES users (id);

ALTER TABLE chats
    ADD CONSTRAINT FK_CHATS_ON_RECEIVER FOREIGN KEY (receiver_id) REFERENCES users (id);

ALTER TABLE chats
    ADD CONSTRAINT FK_CHATS_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (id);

ALTER TABLE chats_link_publications
    ADD CONSTRAINT fk_chalinpub_on_chat FOREIGN KEY (chat_id) REFERENCES chats (id);

ALTER TABLE chats_link_publications
    ADD CONSTRAINT fk_chalinpub_on_link__publication FOREIGN KEY (link_publications_id) REFERENCES link_publications (id);

ALTER TABLE comments_notifications
    ADD CONSTRAINT uc_comments_notifications_notifications UNIQUE (notifications_id);

ALTER TABLE inner_comment
    ADD CONSTRAINT uc_inner_comment_innercomments UNIQUE (inner_comments_id);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_PUBLICATION FOREIGN KEY (publication_id) REFERENCES publications (id);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE comments_likes
    ADD CONSTRAINT fk_comlik_on_comment FOREIGN KEY (comment_id) REFERENCES comments (id);

ALTER TABLE comments_likes
    ADD CONSTRAINT fk_comlik_on_like FOREIGN KEY (likes_id) REFERENCES likes (id);

ALTER TABLE comments_notifications
    ADD CONSTRAINT fk_comnot_on_comment FOREIGN KEY (comment_id) REFERENCES comments (id);

ALTER TABLE comments_notifications
    ADD CONSTRAINT fk_comnot_on_notification FOREIGN KEY (notifications_id) REFERENCES notifications (id);

ALTER TABLE inner_comment
    ADD CONSTRAINT fk_inncom_on_comment FOREIGN KEY (comment_id) REFERENCES comments (id);

ALTER TABLE inner_comment
    ADD CONSTRAINT fk_inncom_on_innercomments FOREIGN KEY (inner_comments_id) REFERENCES comments (id);

ALTER TABLE educations
    ADD CONSTRAINT FK_EDUCATIONS_ON_PROFILE FOREIGN KEY (profile_id) REFERENCES profiles (id);


ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);


ALTER TABLE message_content
    ADD CONSTRAINT FK_MESSAGE_CONTENT_ON_CHAT FOREIGN KEY (chat_id) REFERENCES chats (id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_COMMENT FOREIGN KEY (comment_id) REFERENCES comments (id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_LIKE FOREIGN KEY (like_id) REFERENCES likes (id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_PUBLICATION FOREIGN KEY (publication_id) REFERENCES publications (id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_STORY FOREIGN KEY (story_id) REFERENCES stories (id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_USER_NOTIFICATION FOREIGN KEY (user_notification_id) REFERENCES users (id);

ALTER TABLE pablic_profiles_block_users
    ADD CONSTRAINT uc_pablic_profiles_block_users_blockusers UNIQUE (block_users_id);

ALTER TABLE pablic_profiles
    ADD CONSTRAINT FK_PABLIC_PROFILES_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id);

ALTER TABLE pablic_profiles_block_users
    ADD CONSTRAINT fk_pabproblouse_on_pablic_profile FOREIGN KEY (pablic_profile_id) REFERENCES pablic_profiles (id);

ALTER TABLE pablic_profiles_block_users
    ADD CONSTRAINT fk_pabproblouse_on_user FOREIGN KEY (block_users_id) REFERENCES users (id);

ALTER TABLE pablic_profiles_users
    ADD CONSTRAINT fk_pabprouse_on_pablic_profile FOREIGN KEY (pablic_profile_id) REFERENCES pablic_profiles (id);

ALTER TABLE pablic_profiles_users
    ADD CONSTRAINT fk_pabprouse_on_user FOREIGN KEY (users_id) REFERENCES users (id);

ALTER TABLE profiles
    ADD CONSTRAINT FK_PROFILES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE publications
    ADD CONSTRAINT FK_PUBLICATIONS_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id);

ALTER TABLE publications
    ADD CONSTRAINT FK_PUBLICATIONS_ON_PABLIC_PROFILE FOREIGN KEY (pablic_profile_id) REFERENCES pablic_profiles (id);

ALTER TABLE publications_likes
    ADD CONSTRAINT fk_publik_on_like FOREIGN KEY (likes_id) REFERENCES likes (id);

ALTER TABLE publications_likes
    ADD CONSTRAINT fk_publik_on_publication FOREIGN KEY (publication_id) REFERENCES publications (id);

ALTER TABLE publications_link_publications
    ADD CONSTRAINT fk_publinpub_on_link__publication FOREIGN KEY (link_publications_id) REFERENCES link_publications (id);

ALTER TABLE publications_link_publications
    ADD CONSTRAINT fk_publinpub_on_publication FOREIGN KEY (publication_id) REFERENCES publications (id);

ALTER TABLE publications_tag_friends
    ADD CONSTRAINT fk_pubtagfri_on_publication FOREIGN KEY (publication_id) REFERENCES publications (id);

ALTER TABLE publications_tag_friends
    ADD CONSTRAINT fk_pubtagfri_on_user FOREIGN KEY (tag_friends_id) REFERENCES users (id);

ALTER TABLE stories
    ADD CONSTRAINT FK_STORIES_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id);

ALTER TABLE stories_likes
    ADD CONSTRAINT fk_stolik_on_like FOREIGN KEY (likes_id) REFERENCES likes (id);

ALTER TABLE stories_likes
    ADD CONSTRAINT fk_stolik_on_story FOREIGN KEY (story_id) REFERENCES stories (id);

ALTER TABLE stories_link_publications
    ADD CONSTRAINT fk_stolinpub_on_link__publication FOREIGN KEY (link_publications_id) REFERENCES link_publications (id);

ALTER TABLE stories_link_publications
    ADD CONSTRAINT fk_stolinpub_on_story FOREIGN KEY (story_id) REFERENCES stories (id);

ALTER TABLE stories_tag_friends
    ADD CONSTRAINT fk_stotagfri_on_story FOREIGN KEY (story_id) REFERENCES stories (id);

ALTER TABLE stories_tag_friends
    ADD CONSTRAINT fk_stotagfri_on_user FOREIGN KEY (tag_friends_id) REFERENCES users (id);