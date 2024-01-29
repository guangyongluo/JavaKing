SELECT VERSION();

SHOW master status;

SHOW Variables like 'log_bin';

SHOW VARIABLES;

DROP USER IF EXISTS 'canal'@'%';
CREATE USER 'canal'@'%' IDENTIFIED BY 'canal';
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ;
FLUSH PRIVILEGES;

SELECT * FROM mysql.user;

SHOW VARIABLES LIKE 'validate_password%';



SET GLOBAL validate_password.length = 5;
SET GLOBAL validate_password.mixed_case_count = 0;
SET GLOBAL validate_password.number_count = 0;
SET GLOBAL validate_password.special_char_count = 0;
SET GLOBAL validate_password.policy=LOW;

CREATE TABLE `t_user` (

  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,

  `userName` VARCHAR(100) NOT NULL,

  PRIMARY KEY (`id`)

) ENGINE=INNODB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4


CREATE TABLE `t_customer` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `cname` varchar(50) NOT NULL,
  `age` int(10) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `sex` tinyint(4) NOT NULL,
  `birth` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_cname` (`cname`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4
