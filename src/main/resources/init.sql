CREATE TABLE IF NOT EXISTS `user` (
    `id` VARCHAR(255) NOT NULL,
    `username` VARCHAR(255),
    `realname` VARCHAR(255),
    `password` VARCHAR(255),
    `email` VARCHAR(255),
    `phone` VARCHAR(255),
    `status` INT,
    `create_time` DATETIME,
    `update_time` DATETIME,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
  