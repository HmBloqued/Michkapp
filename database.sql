CREATE TABLE `address` ( 
  `street_name` VARCHAR(250) NOT NULL,
  `zip_code` CHAR(20) NOT NULL,
  `street_number` CHAR(20) NOT NULL,
  `city` VARCHAR(250) NOT NULL,
  `id` INT AUTO_INCREMENT NOT NULL,
  `floor` INT NULL,
  `apartment_number` INT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (`id`)
);
CREATE TABLE `furniture` ( 
  `id` BIGINT AUTO_INCREMENT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `room_id` INT NOT NULL,
  `position` ENUM NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (`id`)
);
CREATE TABLE `furniture_state_inventory` ( 
  `inventory_id` INT NOT NULL,
  `furniture_id` BIGINT NOT NULL,
  `datetime` DATETIME NOT NULL,
  `furniture_state` ENUM NOT NULL,
  `picture` BLOB NULL,
  `picture_date` DATETIME NULL,
  `comment` VARCHAR(250) NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (`inventory_id`, `furniture_id`)
);
CREATE TABLE `inventory` ( 
  `id` INT AUTO_INCREMENT NOT NULL,
  `property_id` INT NOT NULL,
  `agent_id` INT NOT NULL,
  `occupant_id` INT NOT NULL,
  `start_date` DATETIME NOT NULL,
  `is_occupant_present` TINYINT NOT NULL,
  `is_owner_present` TINYINT NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (`id`)
);
CREATE TABLE `property` ( 
  `id` INT AUTO_INCREMENT NOT NULL,
  `address_id` INT NOT NULL,
  `owner_id` INT NOT NULL,
  `last_inventory_date` DATE NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (`id`)
);
CREATE TABLE `room` ( 
  `id` INT AUTO_INCREMENT NOT NULL,
  `property_id` INT NOT NULL,
  `room_type` ENUM NOT NULL,
  `name` VARCHAR(250) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (`id`)
);
CREATE TABLE `user` ( 
  `id` INT AUTO_INCREMENT NOT NULL,
  `firstname` VARCHAR(250) NOT NULL,
  `lastname` VARCHAR(250) NOT NULL,
  `role` ENUM NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (`id`)
);
ALTER TABLE `furniture` ADD CONSTRAINT `furniture_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `furniture_state_inventory` ADD CONSTRAINT `furniture_state_furniture` FOREIGN KEY (`furniture_id`) REFERENCES `furniture` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `furniture_state_inventory` ADD CONSTRAINT `furniture_state_inventory` FOREIGN KEY (`inventory_id`) REFERENCES `inventory` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `inventory` ADD CONSTRAINT `inventory_agent` FOREIGN KEY (`agent_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `inventory` ADD CONSTRAINT `inventory_occupant` FOREIGN KEY (`occupant_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `inventory` ADD CONSTRAINT `inventory_property` FOREIGN KEY (`property_id`) REFERENCES `property` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `property` ADD CONSTRAINT `property_adress` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `property` ADD CONSTRAINT `property_owner` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `room` ADD CONSTRAINT `room_property` FOREIGN KEY (`property_id`) REFERENCES `property` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
