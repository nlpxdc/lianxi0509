SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for lx_role
-- ----------------------------
DROP TABLE IF EXISTS `lx_role`;
CREATE TABLE `lx_role`
(
  `role_id`            int(11)      NOT NULL auto_increment,
  `name`           varchar(255) NOT NULL,
  `description` varchar(255),
  PRIMARY KEY (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  auto_increment = 1;
