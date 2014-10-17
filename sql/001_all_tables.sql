use tui;

CREATE TABLE `users` (
  `id` char(8) NOT NULL,
  `email`  varchar(128) NOT NULL,
  `mobile` varchar(32),
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `reports` (
  `id` char(8) NOT NULL,
  `user_id` char(8) NOT NULL,
  `location` varchar(32) NOT NULL,
  `description` varchar(256) NOT NULL,
  `picture_id` varchar(32) NOT NULL,
  `state` char(16) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(`id`),
  CONSTRAINT `reports_user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `tui`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `spotfixes` (
  `id` char(8) NOT NULL,
  `user_id` char(8) NOT NULL,
  `location` varchar(32) NOT NULL,
  `description` varchar(256) NOT NULL,
  `picture_id` varchar(32) NOT NULL,
  `state` char(16) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `scheduled_on` timestamp NOT NULL,
  `executed_on` timestamp,
  PRIMARY KEY(`id`),
  CONSTRAINT `spotfixes_user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `tui`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `volunteers` (
  `spotfix_id` char(8) NOT NULL,
  `user_id` char(8) NOT NULL,
  CONSTRAINT `volunteers_user_id_fk`
      FOREIGN KEY (`user_id`)
      REFERENCES `tui`.`users` (`id`)
      ON DELETE CASCADE
      ON UPDATE NO ACTION,
  CONSTRAINT `volunteers_spotfix_id_fk`
        FOREIGN KEY (`spotfix_id`)
        REFERENCES `tui`.`spotfixes` (`id`)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `feeds` (
  `item` varchar(1024) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;