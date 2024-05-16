create table if not exists public.link_publications
(
    id   bigint not null
    primary key,
    link varchar(255)
    );

alter table public.link_publications
    owner to postgres;

create table if not exists public.users
(
    id                bigint not null
    primary key,
    block_account     boolean,
    confirmation_code varchar(255),
    created_at        timestamp(6) with time zone,
                                       email             varchar(255),
    is_block          boolean,
    password          varchar(255),
    role              varchar(255)
    constraint users_role_check
    check ((role)::text = ANY ((ARRAY ['ADMIN'::character varying, 'USER'::character varying])::text[])),
    user_name         varchar(255)
    );

alter table public.users
    owner to postgres;

create table if not exists public.chapters
(
    id         bigint not null
    primary key,
    group_name varchar(255),
    user_id    bigint
    constraint fk2k1taa8k2k6k7rfhc0vu8o5e8
    references public.users
    );

alter table public.chapters
    owner to postgres;

create table if not exists public.chapters_friends
(
    chapter_id bigint not null
    constraint fk6tnymxprkqtx2nm6gujd4ofpa
    references public.chapters,
    friends_id bigint not null
    constraint uk_si06asjmbgt5euoe954f5ryv4
    unique
    constraint fkjdemac2heurftopd9o3ot25gu
    references public.users
);

alter table public.chapters_friends
    owner to postgres;

create table if not exists public.chats
(
    id          bigint not null
    primary key,
    receiver_id bigint
    constraint fk6dbye15iemw6gjqt0q4q06nf1
    references public.users,
    sender_id   bigint
    constraint fkla7peq6fislsxok7a4wxv5p36
    references public.users
);

alter table public.chats
    owner to postgres;

create table if not exists public.chats_link_publications
(
    chat_id              bigint not null
    constraint fkkpv7s08j4dyh3s8v65sjydr3k
    references public.chats,
    link_publications_id bigint not null
    constraint fkg51cis0xhuo64knjp38sxdk7n
    references public.link_publications
);

alter table public.chats_link_publications
    owner to postgres;

create table if not exists public.likes
(
    id      bigint not null
    primary key,
    user_id bigint
    constraint uk_3yfb9vqbvu7vae6u28wpe73ux
    unique
    constraint fknvx9seeqqyy71bij291pwiwrg
    references public.users
);

alter table public.likes
    owner to postgres;

create table if not exists public.message_content
(
    id               bigint  not null
    primary key,
    content          varchar(255),
    read_or_not_read boolean not null,
    timestamp        timestamp(6),
    chat_id          bigint
    constraint fk66qml0imffo6y2v3jhogboh55
    references public.chats
    );

alter table public.message_content
    owner to postgres;

create table if not exists public.pablic_profiles
(
    id                 bigint not null
    primary key,
    avatar             varchar(255),
    cover              varchar(255),
    description_public varchar(255),
    pablic_name        varchar(255),
    tematica           varchar(255)
    constraint pablic_profiles_tematica_check
    check ((tematica)::text = ANY
((ARRAY ['MOVIE'::character varying, 'IT'::character varying, 'MULTFILM'::character varying, 'ANIME'::character varying, 'HORROR'::character varying, 'MUSIC'::character varying, 'SPORT'::character varying, 'MEDICINE'::character varying, 'TRANSPORT'::character varying, 'CONSTRUCTION'::character varying, 'FINANCE'::character varying, 'TOURISM'::character varying])::text[])),
    user_id            bigint
    constraint uk_7eyp5h1wcf9w2pwj3ve7i5p4k
    unique
    constraint fknp28n08ku2mkssoi6dawxphck
    references public.users
    );

alter table public.pablic_profiles
    owner to postgres;

create table if not exists public.pablic_profiles_users
(
    pablic_profile_id bigint not null
    constraint fk78fhjm3ak5m8d1jbcuhkdtyo8
    references public.pablic_profiles,
    users_id          bigint not null
    constraint fk2ycnpc6lck9v0hxc7gpb3sfa5
    references public.users
);

alter table public.pablic_profiles_users
    owner to postgres;

create table if not exists public.profiles
(
    id              bigint  not null
    primary key,
    about_your_self varchar(255),
    avatar          varchar(255),
    cover           varchar(255),
    first_name      varchar(255),
    last_name       varchar(255),
    patronymic_name varchar(255),
    phone_number    varchar(255),
    profession      varchar(255),
    work_or_not     boolean not null,
    user_id         bigint
    constraint uk_4ixsj6aqve5pxrbw2u0oyk8bb
    unique
    constraint fk410q61iev7klncmpqfuo85ivh
    references public.users
    );

alter table public.profiles
    owner to postgres;

create table if not exists public.educations
(
    id                      bigint not null
    primary key,
    country                 varchar(255)
    constraint educations_country_check
    check ((country)::text = ANY
((ARRAY ['AFGHANISTAN'::character varying, 'ALBANIA'::character varying, 'ALGERIA'::character varying, 'ANDORRA'::character varying, 'ANGOLA'::character varying, 'ANTIGUA_AND_BARBUDA'::character varying, 'ARGENTINA'::character varying, 'ARMENIA'::character varying, 'AUSTRALIA'::character varying, 'AUSTRIA'::character varying, 'AZERBAIJAN'::character varying, 'BAHAMAS'::character varying, 'BAHRAIN'::character varying, 'BANGLADESH'::character varying, 'BARBADOS'::character varying, 'BELARUS'::character varying, 'BELGIUM'::character varying, 'BELIZE'::character varying, 'BENIN'::character varying, 'BHUTAN'::character varying, 'BOLIVIA'::character varying, 'BOSNIA_AND_HERZEGOVINA'::character varying, 'BOTSWANA'::character varying, 'BRAZIL'::character varying, 'BRUNEI'::character varying, 'BULGARIA'::character varying, 'BURKINA_FASO'::character varying, 'BURUNDI'::character varying, 'CABO_VERDE'::character varying, 'CAMBODIA'::character varying, 'CAMEROON'::character varying, 'CANADA'::character varying, 'CENTRAL_AFRICAN_REPUBLIC'::character varying, 'CHAD'::character varying, 'CHILE'::character varying, 'CHINA'::character varying, 'COLOMBIA'::character varying, 'COMOROS'::character varying, 'CONGO'::character varying, 'COSTA_RICA'::character varying, 'CROATIA'::character varying, 'CUBA'::character varying, 'CYPRUS'::character varying, 'CZECH_REPUBLIC'::character varying, 'DENMARK'::character varying, 'DJIBOUTI'::character varying, 'DOMINICA'::character varying, 'DOMINICAN_REPUBLIC'::character varying, 'ECUADOR'::character varying, 'EGYPT'::character varying, 'EL_SALVADOR'::character varying, 'EQUATORIAL_GUINEA'::character varying, 'ERITREA'::character varying, 'ESTONIA'::character varying, 'ESWATINI'::character varying, 'ETHIOPIA'::character varying, 'FIJI'::character varying, 'FINLAND'::character varying, 'FRANCE'::character varying, 'GABON'::character varying, 'GAMBIA'::character varying, 'GEORGIA'::character varying, 'GERMANY'::character varying, 'GHANA'::character varying, 'GREECE'::character varying, 'GRENADA'::character varying, 'GUATEMALA'::character varying, 'GUINEA'::character varying, 'GUINEA_BISSAU'::character varying, 'GUYANA'::character varying, 'HAITI'::character varying, 'HONDURAS'::character varying, 'HUNGARY'::character varying, 'ICELAND'::character varying, 'INDIA'::character varying, 'INDONESIA'::character varying, 'IRAN'::character varying, 'IRAQ'::character varying, 'IRELAND'::character varying, 'ISRAEL'::character varying, 'ITALY'::character varying, 'JAMAICA'::character varying, 'JAPAN'::character varying, 'JORDAN'::character varying, 'KAZAKHSTAN'::character varying, 'KENYA'::character varying, 'KIRIBATI'::character varying, 'KOSOVO'::character varying, 'KUWAIT'::character varying, 'KYRGYZSTAN'::character varying, 'LAOS'::character varying, 'LATVIA'::character varying, 'LEBANON'::character varying, 'LESOTHO'::character varying, 'LIBERIA'::character varying, 'LIBYA'::character varying, 'LIECHTENSTEIN'::character varying, 'LITHUANIA'::character varying, 'LUXEMBOURG'::character varying, 'MADAGASCAR'::character varying, 'MALAWI'::character varying, 'MALAYSIA'::character varying, 'MALDIVES'::character varying, 'MALI'::character varying, 'MALTA'::character varying, 'MARSHALL_ISLANDS'::character varying, 'MAURITANIA'::character varying, 'MAURITIUS'::character varying, 'MEXICO'::character varying, 'MICRONESIA'::character varying, 'MOLDOVA'::character varying, 'MONACO'::character varying, 'MONGOLIA'::character varying, 'MONTENEGRO'::character varying, 'MOROCCO'::character varying, 'MOZAMBIQUE'::character varying, 'MYANMAR'::character varying, 'NAMIBIA'::character varying, 'NAURU'::character varying, 'NEPAL'::character varying, 'NETHERLANDS'::character varying, 'NEW_ZEALAND'::character varying, 'NICARAGUA'::character varying, 'NIGER'::character varying, 'NIGERIA'::character varying, 'NORTH_KOREA'::character varying, 'NORTH_MACEDONIA'::character varying, 'NORWAY'::character varying, 'OMAN'::character varying, 'PAKISTAN'::character varying, 'PALAU'::character varying, 'PANAMA'::character varying, 'PAPUA_NEW_GUINEA'::character varying, 'PARAGUAY'::character varying, 'PERU'::character varying, 'PHILIPPINES'::character varying, 'POLAND'::character varying, 'PORTUGAL'::character varying, 'QATAR'::character varying, 'ROMANIA'::character varying, 'RUSSIA'::character varying, 'RWANDA'::character varying, 'SAN_MARINO'::character varying])::text[])),
    educational_institution varchar(255),
    location                varchar(255),
    profile_id              bigint
    constraint fkbufn19514tqheek69srnucok2
    references public.profiles
    );

alter table public.educations
    owner to postgres;

create table if not exists public.profile_favorites
(
    profile_id bigint not null
    constraint fk6v1vuda47ahbo4vjnbxfp6m4j
    references public.profiles,
    favorites  bigint
);

alter table public.profile_favorites
    owner to postgres;

create table if not exists public.publications
(
    id                bigint  not null
    primary key,
    created_at        timestamp(6) with time zone,
                                       description       varchar(255),
    is_block_comment  boolean not null,
    location          varchar(255),
    updated_at        timestamp(6) with time zone,
                                       owner_id          bigint
                                       constraint fk2k3jm9lcut1jnx6bii0gaam
                                       references public.users,
                                       pablic_profile_id bigint
                                       constraint fkpjj9u1srbc0tjlknfkcctc0i9
                                       references public.pablic_profiles
                                       );

alter table public.publications
    owner to postgres;

create table if not exists public.comments
(
    id             bigint not null
    primary key,
    created_at     timestamp(6) with time zone,
                                    message        varchar(255),
    publication_id bigint
    constraint fklpjwqhvr0p7e7x1u9efvaq6xe
    references public.publications,
    user_id        bigint
    constraint fk8omq0tc18jd43bu5tjh6jvraq
    references public.users
    );

alter table public.comments
    owner to postgres;

create table if not exists public.comments_likes
(
    comment_id bigint not null
    constraint fkogmkq8clqlxqis53e9tlu4w96
    references public.comments,
    likes_id   bigint not null
    constraint fk3kwimcp8ca9xtw9ekp0qyis7d
    references public.likes
);

alter table public.comments_likes
    owner to postgres;

create table if not exists public.inner_comment
(
    comment_id        bigint not null
    constraint fkns7vwspv8rra96kdt4coi2uni
    references public.comments,
    inner_comments_id bigint not null
    constraint uk_748fejeg2x53gsdrdkr8if4sk
    unique
    constraint fkcum59qpfnpjsjpbu0qomxybnq
    references public.comments
);

alter table public.inner_comment
    owner to postgres;

create table if not exists public.publication_complains
(
    publication_id bigint not null
    constraint fk20oi0n1j6qeslhb3jqqbn46lu
    references public.publications,
    complains      varchar(255),
    complains_key  bigint not null,
    primary key (publication_id, complains_key)
    );

alter table public.publication_complains
    owner to postgres;

create table if not exists public.publications_likes
(
    publication_id bigint not null
    constraint fk3q0l0hr84aain1lqw3jh0md7d
    references public.publications,
    likes_id       bigint not null
    constraint fk82v5pioqtx097g49urx30ofry
    references public.likes
);

alter table public.publications_likes
    owner to postgres;

create table if not exists public.publications_link_publications
(
    publication_id       bigint not null
    constraint fkchw8j91qbs1985ol5spevixnk
    references public.publications,
    link_publications_id bigint not null
    constraint fkrrnfxb4agjmbfdysjqj7xnidv
    references public.link_publications
);

alter table public.publications_link_publications
    owner to postgres;

create table if not exists public.publications_tag_friends
(
    publication_id bigint not null
    constraint fk3tco713li1x8bv36b5px5mf89
    references public.publications,
    tag_friends_id bigint not null
    constraint fkgjfs1592eblgg0ur3niwn5rbb
    references public.users
);

alter table public.publications_tag_friends
    owner to postgres;

create table if not exists public.stories
(
    id         bigint not null
    primary key,
    created_at timestamp(6) with time zone,
                                text       varchar(255),
    owner_id   bigint
    constraint fkms01wbaok97nx72e69l78v6h8
    references public.users
    );

alter table public.stories
    owner to postgres;

create table if not exists public.notifications
(
    id                   bigint  not null
    primary key,
    created_at           timestamp(6) with time zone,
                                          notification_message varchar(255),
    seen                 boolean not null,
    sender_user_id       bigint,
    comment_id           bigint
    constraint fkl7p8sj183bxuwg2sq2ltx3cpv
    references public.comments,
    like_id              bigint
    constraint uk_o3g9xp49pct7b6kdlxats4vlo
    unique
    constraint fk53rlxse09xt1t5pjoxejtynwx
    references public.likes,
    publication_id       bigint
    constraint fktr0wc4o9auvjpmkudn6c9jdd1
    references public.publications,
    story_id             bigint
    constraint fk92dcff9rffogt61h3ysxib9jb
    references public.stories,
    user_notification_id bigint
    constraint fka2umo4k5q8nwp58l9hq9w7eof
    references public.users
    );

alter table public.notifications
    owner to postgres;

create table if not exists public.comments_notifications
(
    comment_id       bigint not null
    constraint fk19rvicl2ofon9yougwy473ap8
    references public.comments,
    notifications_id bigint not null
    constraint uk_e2xjuopo3fncggsw2h97uy3vx
    unique
    constraint fk4f9m0l8vf3rawgrth21m00vv7
    references public.notifications
);

alter table public.comments_notifications
    owner to postgres;

create table if not exists public.stories_likes
(
    story_id bigint not null
    constraint fkacm2weurh9srm8e1unh6hi5rs
    references public.stories,
    likes_id bigint not null
    constraint fk637gfru2r3bovhlhcrquhwrjn
    references public.likes
);

alter table public.stories_likes
    owner to postgres;

create table if not exists public.stories_link_publications
(
    story_id             bigint not null
    constraint fkfdkd5ict5c0c8ypa9o3vp7pis
    references public.stories,
    link_publications_id bigint not null
    constraint fkpi6gg011b3asrrgo5yw1yb8ls
    references public.link_publications
);

alter table public.stories_link_publications
    owner to postgres;

create table if not exists public.stories_tag_friends
(
    story_id       bigint not null
    constraint fkr673xypdhhj489xt6dq597jtx
    references public.stories,
    tag_friends_id bigint not null
    constraint fk4r558r787w8vbaq5n7d40onyk
    references public.users
);

alter table public.stories_tag_friends
    owner to postgres;

create table if not exists public.user_block_accounts
(
    user_id        bigint not null
    constraint fkjm47xeyx89g125q1mmtgt2de7
    references public.users,
    block_accounts bigint
);

alter table public.user_block_accounts
    owner to postgres;

create table if not exists public.user_search_friends_history
(
    user_id                bigint not null
    constraint fkr8u1grj650ognhodmvuaktdqd
    references public.users,
    search_friends_history bigint
);

alter table public.user_search_friends_history
    owner to postgres;