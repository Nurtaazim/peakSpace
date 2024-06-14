
insert into users(id, user_name, email, password, is_block,role)
values
    (1, 'Aliaskar', 'aliaskar@gmail.com', '$2a$12$V3VQ8h4pfGBNNQYGlggXlOHf6.eFNH4VChm0bxKHPDMRv7te/GOvK',
     false,'USER'),
    (2, 'Mirlan', 'mirlan@gmail.com', '$2a$12$nBn2dhJXXKXNLcx5LFH.LOrXKuDQSQUVGr5vh90Uo73XHSZ0YP636',
     false,'USER'),
    (3, 'Myrzaiym', 'myrzaiym@gmail.com', '$2a$12$bjn01purODRpvf714JFbj./91rXrvMRL5QgjCbgIEAYnQdeYoz68K',
     false,'USER'),
    (4, 'Nurtaazim', 'nurtaazim@gmail.com', '$2a$12$7w1javZANTDTW.PTHWoPMewNHO1WOPOpURSy5BGHOIcgc5V/gVv8S',
     false,'USER'),
    (5, 'Nurmukhammed', 'nurmukhammed@gmail.com', '$2a$12$7wuEoK1raMY0ACiHI0cpJOUB4q5MMAAbrJJdKpbdzyIKPCZ5o.fb2',
     false,'USER'),
    (6, 'Nurkamil', 'nurkamil@gmail.com', '$2a$12$rDuM/7QHNuls7F/uSnYORehUR6RJOAkvHDpjLxO7WLjf1TheZfcXu',
     false,'USER'),
    (7, 'Aiturgan', 'aiturgan@gmail.com', '$2a$12$gmRvdp9mjYsWvQomynXDV.QXkey69qZXogJcvh0wQLvHvCl0za6Oq',
     false,'USER'),
    (8, 'Gulukan', 'gulumkan@gmail.com', '$2a$12$4kLeZzjyQxv.n/dwRk./GeVo54IG4b8F.hZGk2jEc0oqQzSYQAVOi',
     false,'USER'),
    (9, 'Ajybek', 'ajybek@gmail.com', '$2a$12$lWjTBGDFDJecVBbmvEHAAeZOqL8VmGP2ESrGACr5xwfPJZUYirJMO',
     false,'USER'),
    (10, 'Nurislam', 'nurt@gmail.com', '$2a$12$t7nUpSW.BjMy2Fe2N2HTgOWY723XNMj6yC15piA9gTV2nkh5CLgNS',
     false,'USER'),
    (11, 'Admin', 'admin@gmail.com', '$2a$12$R/BCR0rspzVEztgDOqIGsufUUsDAFsQXrUmxTQNp0BS5M5bMdb9JO', false,'ADMIN'),
    (12, 'EmilyJohnson', 'emily.johnson@example.com', '$2a$12$encodedPassword2', false,'USER'),
    (13, 'MichaelWilliams', 'michael.williams@example.com', '$2a$12$encodedPassword3', false,'USER'),
    (14, 'EmmaBrown', 'emma.brown@example.com', '$2a$12$encodedPassword4', false,'USER'),
    (15, 'JamedssJones', 'james.jones@example.com', '$2a$12$encodedPassword5', false,'USER'),
    (16, 'OliviaGarcia', 'olivia.garcia@example.com', '$2a$12$encodedPassword6', false,'USER'),
    (17, 'WilliamMartinez', 'william.martinez@example.com', '$2a$12$encodedPassword7', false,'USER'),
    (18, 'SophiaRobinson', 'sophia.robinson@example.com', '$2a$12$encodedPassword8', false,'USER'),
    (19, 'BenjaminClark', 'benjamin.clark@example.com', '$2a$12$encodedPassword9', false,'USER'),
    (20, 'AvaRodriguez', 'ava.rodriguez@example.com', '$2a$12$encodedPassword10', false,'USER'),
    (28, 'Username', 'carparking170105@gmail.com', '$2a$10$pQ4GH3TMDhWB9CUMfe4VJ.VNNREt6XWcBNVjnmMcXSht6E56yvoY6', false,'USER');


insert into stories(id, owner_id, created_at)
values
       (1, 1, '2024-10-10T00:00:00+00:00'),
       (2, 1, '2024-09-05T00:00:00+00:00'),
       (3, 2, '2024-08-10T00:00:00+00:00'),
       (4, 3, '2024-07-12T00:00:00+00:00'),
       (5, 3, '2024-07-12T00:00:00+00:00'),
       (6, 4, '2024-07-12T00:00:00+00:00'),
       (7, 5, '2024-07-12T00:00:00+00:00'),
       (8, 6, '2024-07-12T00:00:00+00:00'),
       (9, 7, '2024-07-12T00:00:00+00:00'),
       (10, 8, '2024-07-12T00:00:00+00:00'),
       (11, 9, '2024-07-12T00:00:00+00:00'),
       (12, 10, '2024-07-12T00:00:00+00:00'),
       (13, 11, '2024-07-12T00:00:00+00:00'),
       (14, 12, '2024-07-12T00:00:00+00:00'),
       (15, 13, '2024-07-12T00:00:00+00:00'),
       (16, 14, '2024-07-12T00:00:00+00:00'),
       (17, 15, '2024-07-12T00:00:00+00:00'),
       (18, 16, '2024-07-12T00:00:00+00:00');



insert into likes(id, user_id)
values (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8),
       (9, 9),
       (10, 10),
       (11, 11),
       (12, 12),
       (13, 13),
       (14, 14),
       (15, 15),
       (16, 16),
       (17, 17),
       (18, 18),
       (19, 19),
       (20, 20);


INSERT INTO stories_likes(story_id, likes_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 5),
       (4, 6),
       (4, 7),
       (5, 8),
       (5, 9),
       (6, 10),
       (7, 11),
       (8, 12),
       (8, 13),
       (9, 14),
       (9, 15),
       (10, 16),
       (11, 17),
       (12, 18);


insert into publications(id, owner_id, pablic_profile_id,is_block_comment, description, created_at, updated_at, location)
values

       (1, 1, null,false, 'Summer time', '2024-10-10T00:00:00+00:00', '2024-11-01T00:00:00+00:00', 'Bishkek'),
       (2, 1, null,false, 'At the school', '2024-09-05T00:00:00+00:00', '2024-11-10T00:00:00+00:00', 'Naryn'),
       (3, 2, null,false, 'Happy times', '2024-08-10T00:00:00+00:00', '2024-10-01T00:00:00+00:00', 'Bishkek'),
       (4, 3, null,false,'Moments', '2024-07-12T00:00:00+00:00', '2024-09-01T00:00:00+00:00', 'Bishkek'),
       (5, 3, null,false, 'Birthday', '2024-05-12T00:00:00+00:00', '2024-08-01T00:00:00+00:00', 'Bishkek'),
       (6, 4, null,false, 'With family', '2024-05-12T00:00:00+00:00', '2024-08-01T00:00:00+00:00', 'OSH'),
       (7, 5, null,false, 'Brothers', '2024-05-12T00:00:00+00:00', '2024-08-01T00:00:00+00:00', 'Bishkek'),
       (8, 6, null,false, 'My vibe', '2024-05-12T00:00:00+00:00', '2024-08-01T00:00:00+00:00', 'OSH'),
       (9, 6, null,false, 'Adventure in the Mountains', '2024-07-20T00:00:00+00:00', '2024-08-15T00:00:00+00:00', 'Issyk-Kul'),
       (10, 8, null,false, 'Exploring the City', '2024-06-15T00:00:00+00:00', '2024-07-25T00:00:00+00:00', 'Karabalta'),
       (11, 9, null,false, 'Family Picnic', '2024-07-30T00:00:00+00:00', '2024-08-05T00:00:00+00:00', 'Cholpon-Ata'),
       (12, 9, null,false, 'Weekend Getaway', '2024-08-10T00:00:00+00:00', '2024-08-12T00:00:00+00:00', 'Talas'),
       (13, 11, null,false, 'Beach Day', '2024-06-25T00:00:00+00:00', '2024-06-26T00:00:00+00:00', 'Issyk-Kul'),
       (14, 12, null,false, 'Hiking in the Forest', '2024-07-05T00:00:00+00:00', '2024-07-06T00:00:00+00:00', 'Karakol'),
       (15, 12, null,false, 'Celebrating Graduation', '2024-07-15T00:00:00+00:00', '2024-07-16T00:00:00+00:00', 'Naryn'),
       (16, 14, null,false, 'Road Trip', '2024-08-20T00:00:00+00:00', '2024-08-25T00:00:00+00:00', 'Balykchy'),
       (17, 15, null,false, 'Summer Festival', '2024-06-30T00:00:00+00:00', '2024-07-05T00:00:00+00:00', 'Talas'),
       (18, 17, null,false, 'City Nightlife', '2024-08-05T00:00:00+00:00', '2024-08-10T00:00:00+00:00', 'Bishkek'),
       (19, 17, null,false, 'Camping Trip', '2024-07-10T00:00:00+00:00', '2024-07-15T00:00:00+00:00', 'Issyk-Kul'),
       (20, 17, null,false, 'Exploring Historical Sites', '2024-08-15T00:00:00+00:00', '2024-08-20T00:00:00+00:00', 'Osh');




INSERT INTO profiles(id, user_id, work_or_not, avatar, cover, about_your_self, last_name, first_name, patronymic_name, profession, location)
VALUES
    (1, 1, true, 'https://ca.slack-edge.com/T023L1WBFLH-U05UL2DHP08-132be68f9199-192', 'https://short.url/1', 'Powerfull', 'Temirbekov', 'Aliaskar', 'Temirbekovich', 'DataIng', 'Bishkek, Kyrgyzstan'),
    (2, 2, true, 'https://ca.slack-edge.com/T023L1WBFLH-U05UAG2LSQ7-f827d5460a13-192', 'https://short.url/2', 'I can all', 'Arstanbekov', 'Mirlan', 'Arstanbekovich', 'Backend dev', 'Osh, Kyrgyzstan'),
    (3, 3, false, 'https://ca.slack-edge.com/T023L1WBFLH-U05UV0F2U21-96aecb313aa4-512', 'https://short.url/3', 'Everything is possible', 'Keldibekova', 'Myrzaiym', 'Keldibekovna', 'Java dev', 'Jalal-Abad, Kyrgyzstan'),
    (4, 4, true, 'https://short.url/4', 'https://short.url/5', 'I am from Talas', 'Mukanov', 'Nurtaazim', 'Universal', 'C# dev', 'Talas, Kyrgyzstan'),
    (5, 5, false, 'https://ca.slack-edge.com/T023L1WBFLH-U05TMGGE84A-b67a66e183b8-512', 'https://short.url/6', 'Happy person', 'Medetov', 'Nurmukhammed', 'Universal', 'C++ dev', 'Naryn, Kyrgyzstan'),
    (6, 6, true, 'https://ca.slack-edge.com/T023L1WBFLH-U05V33UT0SV-8b0148a541d6-512', 'https://short.url/7', 'Happiness with me', 'Kamchiev', 'Nurkamil', 'Universal', 'JS dev', 'Batken, Kyrgyzstan'),
    (7, 7, false, 'https://short.url/8', 'https://short.url/9', 'Study hard', 'Maksat kyzy', 'Aiturgan', 'Universal', 'UX/UI', 'Issyk-Kul, Kyrgyzstan'),
    (8, 8, true, 'https://ca.slack-edge.com/T023L1WBFLH-U05TK7SG3S6-eead6146d502-192', 'https://short.url/10', 'Manas univ', 'Uson kyzy', 'Gulumkan', 'Universal', 'UX/UI', 'Chui, Kyrgyzstan'),
    (9, 9, false, 'https://short.url/11', 'https://short.url/12', 'At school', 'Sadykov', 'Ajybek', 'Universal', 'C++ dev', 'Naryn, Kyrgyzstan'),
    (10, 10, true, 'https://ca.slack-edge.com/T023L1WBFLH-U05TWJYJKCJ-b2595bd01b1c-192', 'https://short.url/13', 'At work', 'Toigonbaev', 'Nurislam', 'Universal', 'Backend dev', 'Bishkek, Kyrgyzstan'),
    (11, 11, false, 'avatar', 'https://short.url/14', 'Code Lover', 'Abdulatipov', 'Akbar', 'Universal', 'Full-stack dev', 'Osh, Kyrgyzstan'),
    (12, 12, true, 'avatar', 'cover', 'Tech Enthusiast', 'Bolotbekov', 'Bakyt', 'Universal', 'Software Engineer', 'Jalal-Abad, Kyrgyzstan'),
    (13, 13, false, 'avatar', 'cover', 'Innovator', 'Cholponbaeva', 'Cholpon', 'Universal', 'Frontend dev', 'Talas, Kyrgyzstan'),
    (14, 14, true, 'avatar', 'cover', 'Dreamer', 'Dastanov', 'Dastan', 'Universal', 'Web Developer', 'Naryn, Kyrgyzstan'),
    (15, 15, false, 'avatar', 'cover', 'Explorer', 'Esenbekov', 'Esen', 'Universal', 'Mobile App Developer', 'Batken, Kyrgyzstan'),
    (16, 16, true, 'avatar', 'cover', 'Problem Solver', 'Joldoshev', 'Joldosh', 'Universal', 'Software Developer', 'Issyk-Kul, Kyrgyzstan'),
    (17, 17, false, 'avatar', 'cover', 'Tech Guru', 'Keneshov', 'Kenesh', 'Universal', 'Systems Analyst', 'Chui, Kyrgyzstan'),
    (18, 18, true, 'avatar', 'cover', 'Innovative Mind', 'Kubatbekov', 'Kubat', 'Universal', 'Database Administrator', 'Bishkek, Kyrgyzstan'),
    (19, 19, false, 'avatar', 'cover', 'Data Scientist', 'Muratov', 'Murat', 'Universal', 'Network Administrator', 'Osh, Kyrgyzstan'),
    (20, 20, true, 'avatar', 'cover', 'AI Enthusiast', 'Nurgaziev', 'Nurgazy', 'Universal', 'Machine Learning Engineer', 'Jalal-Abad, Kyrgyzstan');


insert into chats(id, sender_id, receiver_id)
values (1, 1, 2),
       (2, 1, 3),
       (3, 2, 1),
       (4, 2, 3),
       (5, 3, 1),
       (6, 3, 2),
       (7, 4, 1),
       (8, 5, 2),
       (9, 6, 1),
       (10, 6, 3),
       (11, 7, 2),
       (12, 7, 3),
       (13, 7, 4),
       (14, 11, 1),
       (15, 12, 2),
       (16, 12, 3),
       (17, 12, 4),
       (18, 15, 1),
       (19, 16, 2),
       (20, 16, 3);


INSERT INTO message_content(id, chat_id, content, timestamp, read_or_not_read)
values (1, 1, 'Hello', '2024-04-09', false),
       (2, 1, 'Hello', '2024-04-10', true),
       (3, 2, 'Hello', '2024-04-11', false),
       (4, 2, 'Hello', '2024-04-12', true),
       (5, 3, 'Hello', '2024-04-13', false),
       (6, 3, 'Hello', '2024-04-14', true),
       (7, 4, 'Hello', '2024-04-15', false),
       (8, 4, 'Hello', '2024-04-16', true),
       (9, 5, 'Hello', '2024-04-17', false),
       (10, 5, 'Hi there', '2024-04-18', true),
       (11, 6, 'How are you?', '2024-04-19', false),
       (12, 6, 'Nice to meet you', '2024-04-20', true),
       (13, 7, 'Greetings!', '2024-04-21', false),
       (14, 7, 'Good morning', '2024-04-22', true),
       (15, 8, 'Hey!', '2024-04-23', false),
       (16, 1, 'Welcome!', '2024-04-29', true),
       (17, 2, 'Nice weather today', '2024-04-30', false),
       (18, 3, 'Howdy!', '2024-05-01', true);


INSERT INTO pablic_profiles(id, owner_id, cover, avatar, pablic_name, tematica)
VALUES (1, 1, 'https://st2.depositphotos.com/1105977/9877/i/450/depositphotos_98775856-stock-photo-retro-film-production-accessories-still.jpg', 'https://amc-theatres-res.cloudinary.com/image/upload/f_auto,fl_lossy,h_465,q_auto,w_310/v1717521746/amc-cdn/production/2/movies/67500/67476/PosterDynamic/165402.jpg', 'Felisity', 'MOVIE'),
       (2, 2, 'https://techcrunch.com/wp-content/uploads/2015/04/codecode.jpg?w=1024', 'https://thecode.org/wp-content/uploads/2024/05/cropped-THECODE-LOGO_SYMBOL_2019_HIGH.jpg', 'IT', 'IT'),
       (3, 3, 'https://techcrunch.com/wp-content/uploads/2015/04/codecode.jpg?w=1024', 'https://img.freepik.com/free-photo/person-working-html-computer_23-2150038857.jpg?size=626&ext=jpg&ga=GA1.1.1224184972.1712102400&semt=ais', 'Code your future', 'IT'),
       (4, 4, 'https://techcrunch.com/wp-content/uploads/2015/04/codecode.jpg?w=1024', 'https://static.vecteezy.com/system/resources/previews/019/148/839/non_2x/just-code-it-vector.jpg', 'Vakansy', 'IT'),
       (5, 5, 'https://img.freepik.com/free-photo/programming-background-collage_23-2149901779.jpg', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUpHO0a0y8bTKs4PgICXMM36su0yo568W9GA&s', 'Apartment', 'IT'),
       (6, 6, 'https://st2.depositphotos.com/1105977/9877/i/450/depositphotos_98775856-stock-photo-retro-film-production-accessories-still.jpg', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTPQDYau3Hs4-xw1i8jVSUY4BlF4FLmg8lQqg&s', 'Today', 'MOVIE'),
       (7, 7, 'https://techcrunch.com/wp-content/uploads/2015/04/codecode.jpg?w=1024', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS0rmXrbt5KWXxl9p9aelqM2xIBaMn-b8Zphw&s', 'Java', 'IT'),
       (8, 8, 'https://st2.depositphotos.com/1105977/9877/i/450/depositphotos_98775856-stock-photo-retro-film-production-accessories-still.jpg', 'https://assets-in.bmscdn.com/iedb/movies/images/mobile/thumbnail/xlarge/fighter-et00304730-1704191105.jpg', 'Apartment', 'MOVIE'),
       (9, 9, 'https://techcrunch.com/wp-content/uploads/2015/04/codecode.jpg?w=1024', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSmIQFExWx-Oxxmshk-mmwpD4W8tRq0W2Cf4w&s', 'Networking', 'IT'),
       (10, 10, 'https://techcrunch.com/wp-content/uploads/2015/04/codecode.jpg?w=1024', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQpghv-MjgzU1vNbcG2pTjbjhAp1YHBHs0z9A&s', 'Databases', 'IT'),
       (11, 11, 'cover', 'avatar', 'Web Development', 'IT'),
       (12, 12, 'cover', 'avatar', 'Mobile Apps', 'IT'),
       (13, 13, 'cover', 'avatar', 'Software Engineering', 'IT'),
       (14, 14, 'cover', 'avatar', 'Artificial Intelligence', 'IT'),
       (15, 15, 'cover', 'avatar', 'Cybersecurity', 'IT'),
       (16, 16, 'cover', 'avatar', 'Cloud Computing', 'IT'),
       (17, 17, 'cover', 'avatar', 'Data Science', 'IT'),
       (18, 18, 'cover', 'avatar', 'Blockchain', 'IT'),
       (19, 19, 'cover', 'avatar', 'Machine Learning', 'IT'),
       (20, 20, 'cover', 'avatar', 'Robotics', 'IT');



insert into link_publications(id, link)
values
    (1, 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.rottentomatoes.com%2Fm%2Fchallengers_2024&psig=AOvVaw2a4Vcdafiw-DXne7lmuc6J&ust=1718386504827000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCMCF_NyO2YYDFQAAAAAdAAAAABAR'),
    (2, 'https://youtu.be/1FZ7DbQwVcw'),
    (3, 'https://youtu.be/p4FDs9v_EpY'),
    (4, 'https://sotni.ru/wp-content/uploads/2023/08/estetika-17.webp'),
    (5, 'https://youtu.be/PaKr9gWqwl4'),
    (6, 'https://kartinki.pics/uploads/posts/2022-03/1646316456_1-kartinkin-net-p-kartinki-na-telefon-estetika-1.jpg'),
    (7, 'https://i.pinimg.com/736x/c0/ff/75/c0ff7574dc48649b0b51961fc717605b.jpg'),
    (8, 'https://youtu.be/ZSM3w1v-A_Y'),
    (9, 'https://kartin.papik.pro/uploads/posts/2023-06/1688131226_kartin-papik-pro-p-kartinki-nebo-oblaka-estetika-25.jpg'),
    (10, 'https://www.google.com/imgres?q=%D0%BF%D0%B8%D0%BD%D1%82%D0%B5%D1%80%D0%B5%D1%81%D1%82%20estetik&imgurl=https%3A%2F%2Fkartinki.pics%2Fuploads%2Fposts%2F2022-02%2F1644939472_1-kartinkin-net-p-estetichnie-kartinki-pinterest-1.jpg'),
    (11, 'https://youtu.be/o_1aF54DO60'),
    (12, 'https://www.google.com/imgres?q=%D0%BF%D0%B8%D0%BD%D1%82%D0%B5%D1%80%D0%B5%D1%81%D1%82%20estetik&imgurl=https%3A%2F%2Fi.pinimg.com%2F736x%2F1a%2Fd4%2F0c%2F1ad40ca8904b42e5be34f9cf4676874b.jpg'),
    (13, 'https://www.google.com/imgres?q=%D0%BF%D0%B8%D0%BD%D1%82%D0%B5%D1%80%D0%B5%D1%81%D1%82%20estetik&imgurl=https%3A%2F%2Fsotni.ru%2Fwp-content%2Fuploads%2F2023%2F08%2Fmore-estetika-pinterest-4.webp'),
    (14, 'https://youtu.be/gset79KMmt0'),
    (15, 'https://youtu.be/nyuo9-OjNNg'),
    (16, 'https://flomaster.top/o/uploads/posts/2023-11/1700287142_flomaster-top-p-more-estetika-risunki-vkontakte-2.jpg'),
    (17, 'https://youtu.be/x5Dpz6w_jz4'),
    (18, 'https://media-1.gorbilet.com/75/bd/e8/72/8e/20/thanh-soledas-XGuZ4HlC5qU-unsplash.jpg'),
    (19, 'https://www.youtube.com/watch?v=M4ZoCHID9GI'),
    (20, 'https://darulfikr.ru/wp-content/uploads/2022/10/photo_2022-10-06_10-01-33.jpg')
;

insert into educations(id, profile_id, country, educational_institution)
values (1, 1, 'KAZAKHSTAN', 'KGTU'),
       (2, 1, 'KAZAKHSTAN', 'KGTU'),
       (3, 2, 'KAZAKHSTAN', 'PED'),
       (4, 2, 'KAZAKHSTAN','AUSA'),
       (5, 3, 'KAZAKHSTAN', 'MANAS'),
       (6, 4, 'KAZAKHSTAN', 'USA'),
       (7, 4, 'KAZAKHSTAN', 'OSHGU'),
       (8, 5, 'KAZAKHSTAN', 'OSHGU'),
       (9, 6, 'KAZAKHSTAN', 'KGTU'),
       (10, 7, 'KAZAKHSTAN','MANAS'),
       (11, 7, 'KAZAKHSTAN', 'KGMA'),
       (12, 8, 'KAZAKHSTAN', 'OSHGU'),
       (13, 8, 'KAZAKHSTAN','KSUCTA'),
       (14, 9, 'KAZAKHSTAN','KNU'),
       (15, 9, 'KAZAKHSTAN', 'KRSU'),
       (16, 10, 'KAZAKHSTAN', 'TUIT'),
       (17, 11, 'KAZAKHSTAN', 'KSUCTA'),
       (18, 12, 'KAZAKHSTAN','KSTU'),
       (19, 13, 'KAZAKHSTAN', 'KNURE'),
       (20, 14, 'KAZAKHSTAN','KRSU');


INSERT INTO comments(id, user_id, publication_id, message, created_at)
values (1, 1, 1, 'is the best', '2024-10-10T00:00:00+00:00'),
       (2, 1, 2, 'Great!', '2024-09-05T00:00:00+00:00'),
       (3, 2, 2, 'Good photo', '2024-08-10T00:00:00+00:00'),
       (4, 3, 3, 'Beautiful', '2024-07-12T00:00:00+00:00'),
       (5, 3, 4, 'Cute!', '2024-01-12T00:00:00+00:00'),
       (6, 4, 4, 'Cute!', '2024-01-12T00:00:00+00:00'),
       (7, 4, 5, 'Cute!', '2024-01-12T00:00:00+00:00'),
       (8, 5, 5, 'Cute!', '2024-01-12T00:00:00+00:00'),
       (9, 11, 4, 'Great!', '2024-01-12T00:00:00+00:00'),
       (10, 12, 5, 'Awesome!', '2024-01-12T00:00:00+00:00'),
       (11, 12, 6, 'Superb!', '2024-01-12T00:00:00+00:00'),
       (12, 13, 7, 'Fantastic!', '2024-01-12T00:00:00+00:00'),
       (13, 13, 8, 'Amazing!', '2024-01-12T00:00:00+00:00'),
       (14, 14, 9, 'Impressive!', '2024-01-12T00:00:00+00:00'),
       (15, 14, 10, 'Incredible!', '2024-01-12T00:00:00+00:00'),
       (16, 15, 1, 'Outstanding!', '2024-01-12T00:00:00+00:00'),
       (17, 15, 2, 'Excellent!', '2024-01-12T00:00:00+00:00'),
       (18, 16, 3, 'Wonderful!', '2024-01-12T00:00:00+00:00'),
       (19, 16, 4, 'Marvelous!', '2024-01-12T00:00:00+00:00'),
       (20, 17, 5, 'Splendid!', '2024-01-12T00:00:00+00:00');


insert into chapters(id, user_id, group_name)
values (1, 1, 'Friends'),
       (2, 1, 'Family'),
       (3, 2, 'Colleagues'),
       (4, 2, 'Neighbors'),
       (5, 3, 'Classmates'),
       (6, 3, 'University'),
       (7, 4, 'Workmates'),
       (8, 4, 'Study Group'),
       (9, 5, 'Teammates'),
       (10, 5, 'Project Group');


insert into stories_tag_friends(story_id, tag_friends_id)
values (1, 2),
       (1, 3),
       (1, 4),
       (2, 1),
       (3, 1),
       (3, 3),
       (4, 6),
       (4, 6),
       (5, 7),
       (5, 7),
       (6, 8);


insert into stories_link_publications(link_publications_id, story_id)
values (1, 1),
       (2, 1),
       (3, 2),
       (1, 2),
       (5, 3),
       (4, 3),
       (4, 4),
       (5, 2),
       (5, 4),
       (6, 6),
       (6, 4),
       (7, 5);


insert into publications_link_publications(publication_id, link_publications_id)
values (1, 1),
       (1, 3),
       (2, 1),
       (3, 4),
       (3, 5),
       (4, 2),
       (4, 3),
       (5, 5),
       (5, 6),
       (6, 6);


insert into publications_tag_friends(publication_id, tag_friends_id)
values (1, 1),
       (1, 2),
       (2, 1),
       (3, 4),
       (3, 3),
       (4, 6),
       (4, 3),
       (5, 4),
       (5, 6);


insert into publications_likes(publication_id, likes_id)
values (1, 11),
       (1, 12),
       (1, 13),
       (2, 14),
       (2, 15),
       (3, 16),
       (3, 17),
       (4, 18),
       (4, 19),
       (5, 20);


insert into pablic_profiles_users(users_id, pablic_profile_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (3, 6),
       (3, 7),
       (4, 8),
       (5, 9);


insert into notifications(id, like_id, user_notification_id, sender_user_id, comment_id, publication_id, story_id, seen, notification_message, created_at)
values
    (1, 1, 1, 1, NULL, 5, NULL, false, 'Ваш пост понравился пользователю Aliaskar', '2024-05-22T11:06:23.439327+00:00'),
    (2, 2, 3, 3, NULL, 1, NULL, false, 'Лайкнул на ваш пост', '2024-05-22T17:11:49.125000+00:00'),
    (3, 3, 1, 28, NULL, NULL, 1, false, 'Ваша история понравился пользователю Нуртаазим', '2024-05-22T17:13:34.333000+00:00'),
    (4, 4, 4, 2, 2, NULL, NULL, false, 'Ваш коммент понравился!', '2024-05-22T17:09:56.493000+00:00');

insert into inner_comment(comment_id, inner_comments_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (3, 6),
       (3, 7),
       (4, 8),
       (5, 9),
       (6, 10),
       (6, 11);


insert into chats_link_publications(chat_id, link_publications_id)
values (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 4),
       (3, 1),
       (4, 2),
       (4, 3),
       (5, 5),
       (5, 6);


insert into chapters_friends(chapter_id, friends_id)
values (1, 3),
       (1, 4),
       (2, 5),
       (2, 6),
       (2, 1),
       (3, 2),
       (3, 7),
       (4, 8),
       (4, 9),
       (5, 10);


insert into profile_favorites(profile_id, favorites)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 1),
       (2, 3),
       (3, 4),
       (3, 5),
       (4, 6),
       (5, 6);


insert into user_search_friends_history(search_friends_history, user_id)
values
    (1,1),
    (1,2),
    (1,3),
    (2,1),
    (3,4),
    (4,3),
    (4,5),
    (6,2),
    (7,8),
    (7,9);


insert into comments_likes(comment_id, likes_id)
values
    (1,1),
    (1,2),
    (1,3),
    (2,4),
    (2,5),
    (2,6),
    (3,7),
    (3,8),
    (4,9),
    (4,10);