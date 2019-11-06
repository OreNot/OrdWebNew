
create sequence status_sequence start 1 increment 1;
create sequence task_sequence start 1 increment 1;
create sequence urgency_sequence start 1 increment 1;
create sequence user_sequence start 2 increment 1;
create sequence workgroup_sequence start 1 increment 1;
create sequence groupmanager_sequence start 1 increment 1;


create table status (
  id int4 not null,
  name varchar(255),
  primary key (id)
  );

create table task (
  id int4 not null,
  chronos text,
  comment text,
  description varchar(1000) not null,
  exec_date varchar(255),
  reg_date varchar(255),
  task_file_name varchar(255),
  report text,
  user_id int4,
  exec_id int4,
  status_id int4 not null,
  urgency_id int4,
  workgroup_id int4,
  primary key (id)
  );

create table urgency (
  id int4 not null,
  name varchar(255),
  primary key (id)
  );

create table user_role (
  user_id int4 not null,
  roles varchar(255)
  );

create table group_manager (
  id int4 not null,
  user_id int4,
  workgroup_id int4,
  primary key (id)


);

create table usr (
  id int4 not null,
  active boolean not null,
  password varchar(255),
  username varchar(255),
  fio varchar(255),
  email varchar(255),
  primary key (id)
  );

create table work_group (
  id int4 not null,
  name varchar(255),
  primary key (id)
  );

alter table if exists task add constraint task_autor_fk foreign key (user_id) references usr;
alter table if exists task add constraint task_executor_fk foreign key (exec_id) references usr;
alter table if exists task add constraint task_status_fk foreign key (status_id) references status;
alter table if exists task add constraint task_urgency_fk foreign key (urgency_id) references urgency;
alter table if exists task add constraint task_wgroup_fk foreign key (workgroup_id) references work_group;
alter table if exists user_role add constraint role_usr_fk foreign key (user_id) references usr;
alter table if exists group_manager add constraint group_manager_usr_fk foreign key (user_id) references usr;
alter table if exists group_manager add constraint group_manager_wgroup_fk foreign key (workgroup_id) references work_group;

insert into urgency (name, id) values ('Очень важно', 1);
insert into urgency (name, id) values ('Важно', 2);
insert into urgency (name, id) values ('Стандартно', 3);
insert into urgency (name, id) values ('Менее важно', 4);

insert into status (name, id) values ('Зарегистрировано', 1);
insert into status (name, id) values ('Назначена РГ', 2);
insert into status (name, id) values ('Назначен исполнитель', 3);
insert into status (name, id) values ('Возвращено на распределение', 4);
insert into status (name, id) values ('В работе РГ', 5);
insert into status (name, id) values ('В работе у исполнителя', 6);
insert into status (name, id) values ('Выполнено', 7);
insert into status (name, id) values ('В архиве', 8);

insert into work_group (name, id) values ('Группа доверенных сервисов', 1);
insert into work_group (name, id) values ('Группа аудита', 2);
insert into work_group (name, id) values ('Группа эксплуатации СКЗИ', 3);
insert into work_group (name, id) values ('Не назначена', 4);




