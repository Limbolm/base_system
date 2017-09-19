/!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT /;
/!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS /;
/!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION /;
/!40101 SET NAMES utf8 /;

/*基础用户信息表*/
DROP TABLE IF EXISTS `sys`.`base_user_userInfo`;
CREATE TABLE `sys`.`base_user_userInfo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `group_id` INT NULL DEFAULT 组id,
  `userName` VARCHAR(45) NULL DEFAULT '用户名',
  `passWord` VARCHAR(45) NULL DEFAULT '密码',
  `mobile` VARCHAR(25) NULL DEFAULT '手机',
  `email` VARCHAR(45) NULL DEFAULT '邮箱',
  `crete_time` DATETIME NULL DEFAULT 创建时间,
  `crate_userId` INT NULL DEFAULT 创建人ID,
  `update_time` DATETIME NULL DEFAULT 更新时间,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)
) DEFAULT CHARSET=utf8;

/*基础用户组信息表*/
DROP TABLE IF EXISTS `sys`.`base_user_groupInfo`;
CREATE TABLE `sys`.`base_user_groupInfo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `groupName` VARCHAR(45) NULL DEFAULT '组名称',
  `remark` VARCHAR(100) NULL DEFAULT '备注',
  `crete_time` DATETIME NULL DEFAULT 创建时间,
  `crate_userId` INT NULL DEFAULT 创建人ID,
  `update_time` DATETIME NULL DEFAULT 更新时间,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)
)DEFAULT CHARSET=utf8;

/*基础用户角色信息表*/
DROP TABLE IF EXISTS `sys`.`base_user_roleInfo`;
CREATE TABLE `sys`.`base_user_roleInfo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `roleName` VARCHAR(45) NULL DEFAULT '角色名称',
  `base_user_roleInfocol` VARCHAR(100) NULL DEFAULT '备注',
  `crete_time` DATETIME NULL DEFAULT 创建时间,
  `crate_userId` INT NULL DEFAULT 创建人ID,
  `update_time` DATETIME NULL DEFAULT 更新时间,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)
)DEFAULT CHARSET=utf8;

/*基础用户-角色关系表*/
DROP TABLE IF EXISTS `sys`.`base_user_role-user`;
CREATE TABLE `sys`.`base_user_role-user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `roleId` INT NULL,
  `userId` INT NULL,
  PRIMARY KEY (`id`)
)DEFAULT CHARSET=utf8;

/*资源授权表*/
DROP TABLE IF EXISTS  `sys`.`base_user_authority`;
CREATE TABLE `sys`.`base_user_authority` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `resource_id` INT NULL DEFAULT ' 资源ID',
  `resource_type` INT NULL DEFAULT '资源类型',
  `authority_id` INT NULL DEFAULT 授权对象id,
  `authority_type` INT NULL DEFAULT 授权对象类型,
  PRIMARY KEY (`id`)
)DEFAULT CHARSET=utf8;