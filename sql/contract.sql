SELECT concat('DROP TABLE IF EXISTS ', table_name, ';')
FROM information_schema.tables
WHERE table_schema = 'yuri';

DROP TABLE IF EXISTS contract_log;
DROP TABLE IF EXISTS contract_process;
DROP TABLE IF EXISTS contract_state;
DROP TABLE IF EXISTS contract_attachment;
DROP TABLE IF EXISTS behavior;
DROP TABLE IF EXISTS act;
DROP TABLE IF EXISTS `function`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS contract;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS customer;

CREATE TABLE user
(
    name     varchar(40) primary key,
    password varchar(20) not null
);
INSERT INTO user
VALUES ('system', '123');
INSERT INTO user
VALUES ('test', 'test');
INSERT INTO user
VALUES ('zzy', 'zzy');
INSERT INTO user
VALUES ('ckf', 'ckf');
INSERT INTO user
VALUES ('czl', 'czl');
INSERT INTO user
VALUES ('ly', 'ly');
INSERT INTO user
VALUES ('jxl', 'jxl');

CREATE TABLE role
(
    id          int primary key auto_increment,
    name        varchar(40),
    description varchar(100)
);
INSERT INTO role (name, description)
VALUES ('管理员', '对系统进行管理，拥有管理最高权限，用来维护系统的正常运行。');
INSERT INTO role (name, description)
VALUES ('操作员', '负责合同操作流程的管理，并可以跟踪合同的不同阶段。');

CREATE TABLE `function`
(
    num         varchar(10) primary key,
    name        varchar(40) unique not null,
    description varchar(100)
);
INSERT INTO `function`
values ('0', '起草合同', '');
INSERT INTO `function`
values ('1', '定稿合同', '');
INSERT INTO `function`
values ('2', '查询合同', '');
INSERT INTO `function`
values ('3', '删除合同', '');
INSERT INTO `function`
values ('4', '会签合同', '');
INSERT INTO `function`
values ('5', '审批合同', '');
INSERT INTO `function`
values ('6', '签订合同', '');
INSERT INTO `function`
values ('7', '分配会签', '');
INSERT INTO `function`
values ('8', '分配审批', '');
INSERT INTO `function`
values ('9', '分配签订', '');
INSERT INTO `function`
values ('10', '流程查询', '');
INSERT INTO `function`
values ('11', '新增用户', '');
INSERT INTO `function`
values ('12', '编辑用户', '');
INSERT INTO `function`
values ('13', '查询用户', '');
INSERT INTO `function`
values ('14', '删除用户', '');
INSERT INTO `function`
values ('15', '新增角色', '');
INSERT INTO `function`
values ('16', '编辑角色', '');
INSERT INTO `function`
values ('17', '查询角色', '');
INSERT INTO `function`
values ('18', '删除角色', '');
# INSERT INTO `function` values ('19', '编辑功能', '');
# INSERT INTO `function` values ('20', '查询功能', '');
# INSERT INTO `function` values ('21', '删除功能', '');
INSERT INTO `function`
values ('22', '配置权限', '');
INSERT INTO `function`
values ('23', '新增客户', '');
INSERT INTO `function`
values ('24', '编辑客户', '');
INSERT INTO `function`
values ('25', '查询客户', '');
INSERT INTO `function`
values ('26', '删除客户', '');

CREATE TABLE act
(
    username    varchar(40),
    roleId      int,
    description varchar(100),
    primary key (username, roleId),
    foreign key (username) REFERENCES `user` (name) ON DELETE CASCADE,
    foreign key (roleId) REFERENCES `role` (id) ON DELETE CASCADE
);
INSERT INTO act
VALUES ('system', 1, '');
INSERT INTO act
VALUES ('zzy', 1, '');
INSERT INTO act
VALUES ('ckf', 1, '');
INSERT INTO act
VALUES ('czl', 1, '');
INSERT INTO act
VALUES ('ly', 1, '');
INSERT INTO act
VALUES ('jxl', 1, '');
INSERT INTO act
VALUES ('test', 2, '');

CREATE TABLE behavior
(
    roleId int,
    num    varchar(10),
    primary key (roleId, num),
    foreign key (roleId) REFERENCES `role` (id) ON DELETE CASCADE,
    foreign key (num) REFERENCES `function` (num) ON DELETE CASCADE
);
INSERT INTO behavior
values (1, '0');
INSERT INTO behavior
values (1, '1');
INSERT INTO behavior
values (1, '2');
INSERT INTO behavior
values (1, '3');
INSERT INTO behavior
values (1, '4');
INSERT INTO behavior
values (1, '5');
INSERT INTO behavior
values (1, '6');
INSERT INTO behavior
values (1, '7');
INSERT INTO behavior
values (1, '8');
INSERT INTO behavior
values (1, '9');
INSERT INTO behavior
values (1, '10');
INSERT INTO behavior
values (1, '11');
INSERT INTO behavior
values (1, '12');
INSERT INTO behavior
values (1, '13');
INSERT INTO behavior
values (1, '14');
INSERT INTO behavior
values (1, '15');
INSERT INTO behavior
values (1, '16');
INSERT INTO behavior
values (1, '17');
INSERT INTO behavior
values (1, '18');
#INSERT INTO behavior values (1, '19');
#INSERT INTO behavior values (1, '20');
#INSERT INTO behavior values (1, '21');
INSERT INTO behavior
values (1, '22');
INSERT INTO behavior
values (1, '23');
INSERT INTO behavior
values (1, '24');
INSERT INTO behavior
values (1, '25');
INSERT INTO behavior
values (1, '26');

CREATE TABLE customer
(
    num     int primary key auto_increment,
    name    varchar(40) not null,
    address varchar(100),
    tel     varchar(20),
    fax     varchar(20),
    code    varchar(10),
    bank    varchar(50),
    account varchar(50),
    other   varchar(100)
);
INSERT INTO customer (name, address, tel)
values ('c1', '长沙', '153******18');
INSERT INTO customer (name, address, tel)
values ('c2', '北京', '153******19');
INSERT INTO customer (name, address, tel)
values ('c3', 'CS', '135********20');

CREATE TABLE contract
(
    num         varchar(20) primary key,
    name        varchar(40) not null,
    customerNum int,
    begin       date        not null,
    end         date        not null,
    content     text        not null,
    userName    varchar(40),
    foreign key (customerNum) REFERENCES customer (num) ON DELETE CASCADE,
    foreign key (userName) REFERENCES user (name) ON DELETE NO ACTION
);

CREATE TABLE contract_process
(
    contractNum varchar(20),
    type        integer not null,
    state       integer not null,
    userName    varchar(40),
    content     text,
    time        date,
    primary key (contractNum, type, userName),
    foreign key (contractNum) REFERENCES contract (num) ON DELETE CASCADE,
    foreign key (contractNum) REFERENCES user (name) ON DELETE CASCADE
);

CREATE TABLE contract_state
(
    contractNum varchar(20),
    type        integer not null,
    time        date,
    primary key (contractNum),
    foreign key (contractNum) REFERENCES contract (num) ON DELETE CASCADE
);

CREATE TABLE contract_log
(
    id       int primary key auto_increment,
    userName varchar(40),
    content  text,
    time     datetime,
    foreign key (userName) REFERENCES user (name) ON DELETE NO ACTION
);

CREATE TABLE contract_attachment
(
    contractNum varchar(20),
    fileName    varchar(100) not null,
    path        varchar(100) not null,
    type        varchar(20)  not null,
    uploadTime  datetime,
    primary key (contractNum),
    foreign key (contractNum) REFERENCES contract (num) ON DELETE CASCADE
);

DROP TABLE IF EXISTS login;

SET @@GLOBAL.FOREIGN_KEY_CHECKS = 1;