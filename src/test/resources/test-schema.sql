create table if not exists posts
(
    id         bigserial primary key,
    title      varchar(255) not null,
    content    text         not null,
    image      bytea,
    created timestamp default CURRENT_TIMESTAMP,
    updated timestamp default CURRENT_TIMESTAMP
    );

create index idx_post_created on posts (created);

create table if not exists comments
(
    id         bigserial primary key,
    post_id    integer not null references posts on delete cascade,
    content    text    not null,
    created timestamp default CURRENT_TIMESTAMP,
    updated timestamp default CURRENT_TIMESTAMP
);

create index idx_comment_created on comments (created);

create table if not exists likes
(
    id         bigserial primary key,
    post_id    integer not null references posts on delete cascade,
    user_id    integer not null,
    created timestamp default CURRENT_TIMESTAMP
);

create index idx_like_created on likes (created);

create table tags
(
    id   bigserial primary key,
    name varchar(50) not null unique
);

create index idx_tag_name on tags (name);

create table posts_tags
(
    post_id integer not null references posts on delete cascade,
    tag_id  integer not null references tags on delete cascade,
    primary key (post_id, tag_id)
);
