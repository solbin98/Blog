-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema blog
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema blog
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `blog` DEFAULT CHARACTER SET utf8 ;
USE `blog` ;

-- -----------------------------------------------------
-- Table `blog`.`Category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `blog`.`Category` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `parent` INT NULL,
  `order_num` INT NULL,
  PRIMARY KEY (`category_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `blog`.`Board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `blog`.`Board` (
  `board_id` INT NOT NULL AUTO_INCREMENT,
  `category_id` INT NOT NULL,
  `title` VARCHAR(45) NULL,
  `content` TEXT NULL,
  `date` DATETIME NULL,
  `view` INT NULL,
  PRIMARY KEY (`board_id`),
  INDEX `fk_Board_Category1_idx` (`category_id` ASC) VISIBLE,
  CONSTRAINT `fk_Board_Category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `blog`.`Category` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `blog`.`Comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `blog`.`Comment` (
  `comment_id` INT NOT NULL AUTO_INCREMENT,
  `board_id` INT NOT NULL,
  `inner_num` INT NULL,
  `depth` INT NULL,
  `parent` INT NULL,
  `writer` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `content` TEXT NULL,
  `date` DATETIME NULL,
  PRIMARY KEY (`comment_id`),
  INDEX `fk_comment_Board_idx` (`board_id` ASC) VISIBLE,
  CONSTRAINT `fk_comment_Board`
    FOREIGN KEY (`board_id`)
    REFERENCES `blog`.`Board` (`board_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `blog`.`Tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `blog`.`Tag` (
  `tag_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`tag_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `blog`.`BoardTag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `blog`.`BoardTag` (
  `tag_id` INT NOT NULL,
  `board_id` INT NULL,
  INDEX `fk_Board_Tag_Tag1_idx` (`tag_id` ASC) VISIBLE,
  INDEX `fk_Board_Tag_Board1_idx` (`board_id` ASC) VISIBLE,
  CONSTRAINT `fk_Board_Tag_Tag1`
    FOREIGN KEY (`tag_id`)
    REFERENCES `blog`.`Tag` (`tag_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Board_Tag_Board1`
    FOREIGN KEY (`board_id`)
    REFERENCES `blog`.`Board` (`board_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `blog`.`File`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `blog`.`File` (
  `file_id` INT NOT NULL AUTO_INCREMENT,
  `board_id` INT NOT NULL,
  `path` VARCHAR(100) NULL,
  `name` VARCHAR(100) NULL,
  `type` VARCHAR(45) NULL,
  `size` VARCHAR(45) NULL,
  PRIMARY KEY (`file_id`),
  INDEX `fk_File_Board1_idx` (`board_id` ASC) VISIBLE,
  CONSTRAINT `fk_File_Board1`
    FOREIGN KEY (`board_id`)
    REFERENCES `blog`.`Board` (`board_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
