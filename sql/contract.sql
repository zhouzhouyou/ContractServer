DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    name     varchar(40) primary key,
    password varchar(20) not null
);
INSERT INTO user VALUES ('system', '123');
INSERT INTO user VALUES ('test', 'test');
INSERT INTO user VALUES ('zzy', 'zzy');
INSERT INTO user VALUES ('ckf', 'ckf');
INSERT INTO user VALUES ('czl', 'czl');
INSERT INTO user VALUES ('ly', 'ly');
INSERT INTO user VALUES ('jxl', 'jxl');

DROP TABLE IF EXISTS role;
CREATE TABLE role
(
    name        varchar(40) primary key,
    description varchar(100)
);
INSERT INTO role VALUES ('管理员', '对系统进行管理，拥有管理最高权限，用来维护系统的正常运行。');
INSERT INTO role VALUES ('操作员', '负责合同操作流程的管理，并可以跟踪合同的不同阶段。');

DROP TABLE IF EXISTS `function`;
CREATE TABLE `function`
(
    num         varchar(10) primary key,
    name        varchar(40) not null,
    description varchar(100)
);
INSERT INTO `function` values ('0', '起草合同', '');
INSERT INTO `function` values ('1', '定稿合同', '');
INSERT INTO `function` values ('2', '查询合同', '');
INSERT INTO `function` values ('3', '删除合同', '');
INSERT INTO `function` values ('4', '会签合同', '');
INSERT INTO `function` values ('5', '审批合同', '');
INSERT INTO `function` values ('6', '签订合同', '');
INSERT INTO `function` values ('7', '分配会签', '');
INSERT INTO `function` values ('8', '分配审批', '');
INSERT INTO `function` values ('9', '分配签订', '');
INSERT INTO `function` values ('10', '流程查询', '');
INSERT INTO `function` values ('11', '新增用户', '');
INSERT INTO `function` values ('12', '编辑用户', '');
INSERT INTO `function` values ('13', '查询用户', '');
INSERT INTO `function` values ('14', '删除用户', '');
INSERT INTO `function` values ('15', '新增角色', '');
INSERT INTO `function` values ('16', '编辑角色', '');
INSERT INTO `function` values ('17', '查询角色', '');
INSERT INTO `function` values ('18', '新增功能', '');
INSERT INTO `function` values ('19', '编辑功能', '');
INSERT INTO `function` values ('20', '查询功能', '');
INSERT INTO `function` values ('21', '删除功能', '');
INSERT INTO `function` values ('22', '配置权限', '');
INSERT INTO `function` values ('23', '新增客户', '');
INSERT INTO `function` values ('24', '编辑客户', '');
INSERT INTO `function` values ('25', '查询客户', '');
INSERT INTO `function` values ('26', '删除客户', '');

DROP TABLE IF EXISTS act;
CREATE TABLE act
(
    username    varchar(40) REFERENCES `user` (name) ON DELETE CASCADE,
    roleName    varchar(40) REFERENCES `role` (name) ON DELETE CASCADE,
    description varchar(100),
    primary key (username, roleName)
);
INSERT INTO act VALUES ('system', '管理员', '');
INSERT INTO act VALUES ('zzy', '管理员', '');
INSERT INTO act VALUES ('ckf', '管理员', '');
INSERT INTO act VALUES ('czl', '管理员', '');
INSERT INTO act VALUES ('ly', '管理员', '');
INSERT INTO act VALUES ('jxl', '管理员', '');
INSERT INTO act VALUES ('test', '操作员', '');

DROP TABLE IF EXISTS behavior;
CREATE TABLE behavior
(
    roleName varchar(40) REFERENCES `role` (name) ON DELETE CASCADE,
    num      varchar(10) REFERENCES `function` (num) ON DELETE CASCADE,
    primary key (roleName, num)
);
INSERT INTO behavior values ('管理员', '0');
INSERT INTO behavior values ('管理员', '1');
INSERT INTO behavior values ('管理员', '2');
INSERT INTO behavior values ('管理员', '3');
INSERT INTO behavior values ('管理员', '4');
INSERT INTO behavior values ('管理员', '5');
INSERT INTO behavior values ('管理员', '6');
INSERT INTO behavior values ('管理员', '7');
INSERT INTO behavior values ('管理员', '8');
INSERT INTO behavior values ('管理员', '9');
INSERT INTO behavior values ('管理员', '10');
INSERT INTO behavior values ('管理员', '11');
INSERT INTO behavior values ('管理员', '12');
INSERT INTO behavior values ('管理员', '13');
INSERT INTO behavior values ('管理员', '14');
INSERT INTO behavior values ('管理员', '15');
INSERT INTO behavior values ('管理员', '16');
INSERT INTO behavior values ('管理员', '17');
INSERT INTO behavior values ('管理员', '18');
INSERT INTO behavior values ('管理员', '19');
INSERT INTO behavior values ('管理员', '20');
INSERT INTO behavior values ('管理员', '21');
INSERT INTO behavior values ('管理员', '22');
INSERT INTO behavior values ('管理员', '23');
INSERT INTO behavior values ('管理员', '24');
INSERT INTO behavior values ('管理员', '25');
INSERT INTO behavior values ('管理员', '26');

DROP TABLE IF EXISTS customer;
CREATE TABLE customer
(
    num     varchar(20) primary key,
    name    varchar(40) not null,
    address varchar(100),
    tel     varchar(20),
    fax     varchar(20),
    code    varchar(10),
    bank    varchar(50),
    account varchar(50)
);

DROP TABLE IF EXISTS contract;
CREATE TABLE contract
(
    num         varchar(20) primary key,
    name        varchar(40) not null,
    customerNum varchar(20) REFERENCES customer (num),
    begin       date        not null,
    end         date        not null,
    content     text        not null,
    userName    varchar(40) REFERENCES user (name)
);

DROP TABLE IF EXISTS contract_process;
CREATE TABLE contract_process
(
    contractNum varchar(20) REFERENCES contract (num),
    type        integer not null,
    state       integer not null,
    userName    varchar(40) REFERENCES user (name),
    content     text,
    time        date,
    primary key (contractNum, type, userName)
);

DROP TABLE IF EXISTS contract_state;
CREATE TABLE contract_state
(
    contractNum varchar(20) REFERENCES contract (num),
    type        integer not null,
    time        date,
    primary key (contractNum)
);

DROP TABLE IF EXISTS contract_log;
CREATE TABLE contract_log
(
    userName varchar(40) REFERENCES user (name),
    content text,
    time date,
    primary key (userName, time)
);

DROP TABLE IF EXISTS contract_attachment;
CREATE TABLE contract_attachment
(
    contractNum varchar(20) REFERENCES contract (num),
    fileName varchar(100) not null,
    path varchar(100) not null,
    type varchar(20) not null,
    uploadTime date,
    primary key (contractNum)
);