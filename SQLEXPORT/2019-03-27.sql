-- --------------------------------------------------------
-- Värd:                         127.0.0.1
-- Serverversion:                5.7.25-log - MySQL Community Server (GPL)
-- Server-OS:                    Win64
-- HeidiSQL Version:             10.1.0.5464
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumpar databasstruktur för bankfuralle
CREATE DATABASE IF NOT EXISTS `bankfuralle` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_swedish_ci */;
USE `bankfuralle`;

-- Dumpar struktur för tabell bankfuralle.accounts
CREATE TABLE IF NOT EXISTS `accounts` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) unsigned NOT NULL,
  `number` varchar(14) COLLATE utf8_swedish_ci NOT NULL,
  `type` enum('CARD','SALARY','SAVING') COLLATE utf8_swedish_ci NOT NULL DEFAULT 'SAVING',
  `name` varchar(20) COLLATE utf8_swedish_ci NOT NULL DEFAULT 'Konto',
  PRIMARY KEY (`id`),
  UNIQUE KEY `number` (`number`),
  KEY `FK_accounts_costumers` (`owner_id`),
  CONSTRAINT `FK_accounts_costumers` FOREIGN KEY (`owner_id`) REFERENCES `costumers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

-- Dumpar data för tabell bankfuralle.accounts: ~12 rows (ungefär)
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` (`id`, `owner_id`, `number`, `type`, `name`) VALUES
	(8, 1, '4747852852852', 'SALARY', 'testetaeeteotwjepojw'),
	(10, 1, '8989123123123', 'SAVING', 'keps'),
	(11, 1, '96589658965896', 'CARD', 'hej'),
	(14, 4, '47475666333111', 'SAVING', 'Konto'),
	(15, 4, '555666', 'SAVING', 'Konto'),
	(16, 4, '555444', 'SAVING', 'Konto'),
	(17, 4, '555', 'SAVING', 'Konto'),
	(18, 4, '5525', 'SAVING', 'Konto'),
	(19, 4, '552225', 'SAVING', 'Konto'),
	(20, 4, '5556661', 'SAVING', 'Konto'),
	(21, 4, '0', 'SAVING', 'Konto'),
	(24, 4, '123654', 'SAVING', 'Konto');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;

-- Dumpar struktur för funktion bankfuralle.account_number_is_free
DELIMITER //
CREATE DEFINER=`test`@`localhost` FUNCTION `account_number_is_free`(`test` VARCHAR(14)) RETURNS tinyint(1)
BEGIN
	DECLARE returnValue BOOLEAN;  
	if EXISTS (SELECT * FROM accounts WHERE `accounts`.`number` = `test`) then 
		SET returnValue = FALSE; 
	ELSE 
		SET returnValue = TRUE; 
	END if; 
	RETURN returnValue;
END//
DELIMITER ;

-- Dumpar struktur för tabell bankfuralle.autogiro
CREATE TABLE IF NOT EXISTS `autogiro` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `from_account` varchar(14) COLLATE utf8_swedish_ci NOT NULL DEFAULT '0',
  `to_account` varchar(14) COLLATE utf8_swedish_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

-- Dumpar data för tabell bankfuralle.autogiro: ~1 rows (ungefär)
/*!40000 ALTER TABLE `autogiro` DISABLE KEYS */;
/*!40000 ALTER TABLE `autogiro` ENABLE KEYS */;

-- Dumpar struktur för procedur bankfuralle.autogiro_payments
DELIMITER //
CREATE DEFINER=`test`@`localhost` PROCEDURE `autogiro_payments`()
BEGIN
	UPDATE autogiro SET to_account = to_account, from_account = from_account; 
END//
DELIMITER ;

-- Dumpar struktur för vy bankfuralle.balance_accounts
-- Skapar temporärtabell för att hantera VIEW-beroendefel
CREATE TABLE `balance_accounts` (
	`id` INT(11) UNSIGNED NOT NULL,
	`owner_id` INT(11) UNSIGNED NOT NULL,
	`number` VARCHAR(14) NOT NULL COLLATE 'utf8_swedish_ci',
	`type` ENUM('CARD','SALARY','SAVING') NOT NULL COLLATE 'utf8_swedish_ci',
	`name` VARCHAR(20) NOT NULL COLLATE 'utf8_swedish_ci',
	`balance` DOUBLE(10,2) NULL
) ENGINE=MyISAM;

-- Dumpar struktur för tabell bankfuralle.cards
CREATE TABLE IF NOT EXISTS `cards` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `owner_id` int(11) unsigned NOT NULL DEFAULT '0',
  `card_number` varchar(16) COLLATE utf8_swedish_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `card_number` (`card_number`),
  KEY `FK_cards_costumers` (`owner_id`),
  CONSTRAINT `FK_cards_costumers` FOREIGN KEY (`owner_id`) REFERENCES `costumers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

-- Dumpar data för tabell bankfuralle.cards: ~2 rows (ungefär)
/*!40000 ALTER TABLE `cards` DISABLE KEYS */;
INSERT INTO `cards` (`id`, `owner_id`, `card_number`) VALUES
	(1, 1, '6547654765476547'),
	(2, 3, '3214321432143214');
/*!40000 ALTER TABLE `cards` ENABLE KEYS */;

-- Dumpar struktur för funktion bankfuralle.card_pay
DELIMITER //
CREATE DEFINER=`test`@`localhost` FUNCTION `card_pay`(
	`in_card_number` VARCHAR(16),
	`in_amount` DOUBLE(10,2),
	`in_message` VARCHAR(15)



) RETURNS int(11)
BEGIN 
	DECLARE `transaction_id` INT(11);
	DECLARE `respons` ENUM('DONE','WAITING','DENIED','PROCESSING');
	DECLARE `card_owner` int(11); 
	DECLARE `account_number` varchar(14); 
	DECLARE `card_limit` double; 
	DECLARE `card_total_transactions` double; 
	if in_amount = 0 then
		SET `respons` = 'DENIED'; 
	else 
		SET `card_owner` = (SELECT `owner_id` FROM `cards` WHERE card_number = in_card_number); 
		SET `card_limit` = (SELECT `max_withdraw` FROM `costumers` WHERE id = `card_owner`);
		SET `card_total_transactions` = (SELECT SUM(amount)*-1 FROM `withdrawals` WHERE `withdrawals`.`owner` = `card_owner` AND `withdrawals`.`status` = 'DONE'  AND `time_of_transaction` > CURRENT_TIMESTAMP - INTERVAL 1 MINUTE);
		SET `account_number` = (SELECT `number` from `balance_accounts` where `owner_id` = `card_owner` AND `type` = 'CARD' AND `balance` >= `in_amount` LIMIT 1);
		if card_total_transactions is null then 
			SET card_total_transactions = 0; 
		end if;
		if `account_number` is not null AND (`card_total_transactions`+`in_amount`) < `card_limit` then
			CALL create_transaction(`account_number`, 0, `in_amount`, `in_message`, '0000-00-00');
			SET transaction_id = (SELECT LAST_INSERT_ID()); 
			do sleep(1);
			SET `respons` = (select `transactions`.`status` from `transactions` where id = transaction_id); 
		else 
			SET `respons` = 'DENIED';
		end if; 
	end if;
	return respons; 
END//
DELIMITER ;

-- Dumpar struktur för procedur bankfuralle.card_payment
DELIMITER //
CREATE DEFINER=`test`@`localhost` PROCEDURE `card_payment`(
	IN `in_card_number` VARCHAR(16)
,
	IN `in_amount` DOUBLE(10,2),
	IN `in_message` VARCHAR(15),
	OUT `out_status` ENUM('DONE','WAITING','DENIED','PROCESSING')
















)
BEGIN 
	DECLARE `transaction_id` INT(11);
	DECLARE `respons` ENUM('DONE','WAITING','DENIED','PROCESSING');
	DECLARE `card_owner` int(11); 
	DECLARE `account_number` varchar(14); 
	DECLARE `card_limit` double; 
	DECLARE `card_total_transactions` double; 
	if in_amount = 0 then
		SET `respons` = 'DENIED'; 
	else 
		SET `card_owner` = (SELECT `owner_id` FROM `cards` WHERE card_number = in_card_number); 
		SET `card_limit` = (SELECT `max_withdraw` FROM `costumers` WHERE id = `card_owner`);
		SET `card_total_transactions` = (SELECT SUM(amount)*-1 FROM `withdrawals` WHERE `withdrawals`.`owner` = `card_owner` AND `withdrawals`.`status` = 'DONE'  AND `time_of_transaction` > CURRENT_TIMESTAMP - INTERVAL 1 MINUTE);
		SET `account_number` = (SELECT `number` from `balance_accounts` where `owner_id` = `card_owner` AND `type` = 'CARD' AND `balance` >= `in_amount` LIMIT 1);
		if card_total_transactions is null then 
			SET card_total_transactions = 0; 
		end if;
		if `account_number` is not null AND (`card_total_transactions`+`in_amount`) < `card_limit` then
			CALL create_transaction(`account_number`, 0, `in_amount`, `in_message`, '0000-00-00');
			SET transaction_id = (SELECT LAST_INSERT_ID()); 
			do sleep(1);
			SET `respons` = (select `transactions`.`status` from `transactions` where id = transaction_id); 
		else 
			SET `respons` = 'DENIED';
		end if; 
	end if;
	select respons as answer; 
END//
DELIMITER ;

-- Dumpar struktur för händelse bankfuralle.check_monthly_transactions
DELIMITER //
CREATE DEFINER=`test`@`localhost` EVENT `check_monthly_transactions` ON SCHEDULE EVERY 1 DAY STARTS '2019-03-22 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN 
	if today_is_the_last_in_month() then
	# in case of day 31 being the transferday we need to perform it earlier every month that has less days
		UPDATE `monthly_transactions` SET `day_of_month` = `day_of_month` WHERE `day_of_month` >= DAY(CURRENT_TIMESTAMP); 
	else 
		UPDATE `monthly_transactions` SET `day_of_month` = `day_of_month` WHERE `day_of_month` = DAY(CURRENT_TIMESTAMP);
	end if;
END//
DELIMITER ;

-- Dumpar struktur för händelse bankfuralle.check_waiting_transactions
DELIMITER //
CREATE DEFINER=`test`@`localhost` EVENT `check_waiting_transactions` ON SCHEDULE EVERY 1 SECOND STARTS '2019-03-21 14:18:44' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
	UPDATE transactions SET `status` = 'PROCESSING' WHERE time_of_transaction <= current_timestamp AND `status` = 'WAITING'; 
END//
DELIMITER ;

-- Dumpar struktur för tabell bankfuralle.costumers
CREATE TABLE IF NOT EXISTS `costumers` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `SSN` varchar(10) COLLATE utf8_swedish_ci NOT NULL,
  `password` varchar(255) COLLATE utf8_swedish_ci NOT NULL,
  `firstname` varchar(50) COLLATE utf8_swedish_ci NOT NULL,
  `lastname` varchar(50) COLLATE utf8_swedish_ci NOT NULL,
  `max_withdraw` double(10,2) NOT NULL DEFAULT '30000.00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `SSN` (`SSN`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

-- Dumpar data för tabell bankfuralle.costumers: ~3 rows (ungefär)
/*!40000 ALTER TABLE `costumers` DISABLE KEYS */;
INSERT INTO `costumers` (`id`, `SSN`, `password`, `firstname`, `lastname`, `max_withdraw`) VALUES
	(1, '0101012020', 'test', 'Kalle', 'Persson', 200000.00),
	(3, '0202020101', 'test', 'Nils', 'Jönsson', 10000.00),
	(4, '0000000000', 'no_access', 'Other', 'Bank_accounts', 0.00);
/*!40000 ALTER TABLE `costumers` ENABLE KEYS */;

-- Dumpar struktur för procedur bankfuralle.create_autogiro
DELIMITER //
CREATE DEFINER=`test`@`localhost` PROCEDURE `create_autogiro`(
	IN `t_from` VARCHAR(14),
	IN `t_to` VARCHAR(14)
	

)
BEGIN
	if (SELECT id FROM `accounts` WHERE `accounts`.`number` = `t_to`) is null then 
		INSERT INTO accounts SET `accounts`.`owner_id` = 4, `accounts`.`number` = `t_to`;
	end if;  
	if not exists(SELECT id from autogiro WHERE to_account = t_to AND from_account = t_from) then
		INSERT INTO autogiro SET to_account = t_to, from_account = t_from; 
	end if;
END//
DELIMITER ;

-- Dumpar struktur för procedur bankfuralle.create_monthly_transaction
DELIMITER //
CREATE DEFINER=`test`@`localhost` PROCEDURE `create_monthly_transaction`(
	IN `t_from` VARCHAR(14),
	IN `t_to` VARCHAR(14),
	IN `t_amount` DOUBLE(10,2),
	IN `t_message` VARCHAR(50),
	IN `transaction_date` VARCHAR(10)
	)
BEGIN
	if (SELECT id FROM `accounts` WHERE `accounts`.`number` = `t_to`) is null then 
		INSERT INTO accounts SET `accounts`.`owner_id` = 4, `accounts`.`number` = `t_to`;
	end if; 
	if t_amount > 0 then
		INSERT INTO monthly_transactions SET message = t_message, to_account = t_to, from_account = t_from, amount = t_amount, day_of_month = EXTRACT(DAY FROM DATE(`transaction_date`)); 
	end if; 
END//
DELIMITER ;

-- Dumpar struktur för procedur bankfuralle.create_transaction
DELIMITER //
CREATE DEFINER=`test`@`localhost` PROCEDURE `create_transaction`(
	IN `t_from` VARCHAR(14),
	IN `t_to` VARCHAR(14),
	IN `t_amount` DOUBLE(10,2),
	IN `t_message` VARCHAR(50)














,
	IN `transaction_date` VARCHAR(10)

)
BEGIN
	DECLARE `from_account_id` INT(11); 
	DECLARE `to_account_id` INT(11); 
	if (SELECT id FROM `accounts` WHERE `accounts`.`number` = `t_to`) is null then 
		INSERT INTO accounts SET `accounts`.`owner_id` = 4, `accounts`.`number` = `t_to`;
	end if;
	
	SET `from_account_id` = (SELECT id FROM `accounts` WHERE `accounts`.`number` = `t_from`); 
	SET `to_account_id` = (SELECT id FROM `accounts` WHERE `accounts`.`number` = `t_to`); 
	if t_amount > 0 then
		INSERT INTO transactions SET time_of_transaction=transaction_date, message = t_message, to_account = to_account_id, from_account = from_account_id, amount = t_amount; 
	end if; 
END//
DELIMITER ;

-- Dumpar struktur för vy bankfuralle.deposits
-- Skapar temporärtabell för att hantera VIEW-beroendefel
CREATE TABLE `deposits` (
	`owner` INT(11) UNSIGNED NULL,
	`account_id` INT(11) UNSIGNED NULL,
	`number` VARCHAR(14) NULL COLLATE 'utf8_swedish_ci',
	`amount` DOUBLE(10,2) UNSIGNED NOT NULL,
	`message` VARCHAR(15) NOT NULL COLLATE 'utf8_swedish_ci',
	`time_of_transaction` DATETIME NOT NULL,
	`status` ENUM('DONE','WAITING','DENIED','PROCESSING') NOT NULL COLLATE 'utf8_swedish_ci'
) ENGINE=MyISAM;

-- Dumpar struktur för funktion bankfuralle.get_balance
DELIMITER //
CREATE DEFINER=`test`@`localhost` FUNCTION `get_balance`(
	`in_account_id` INT
) RETURNS double(10,2)
BEGIN
	DECLARE balance DOUBLE(10,2);
	SET balance = (SELECT SUM(amount) FROM total_transactions WHERE account_id = in_account_id AND `status` = 'DONE');
	if balance IS NULL THEN 
		SET balance = 0;
	END if;
	RETURN balance; 
END//
DELIMITER ;

-- Dumpar struktur för funktion bankfuralle.has_balance
DELIMITER //
CREATE DEFINER=`test`@`localhost` FUNCTION `has_balance`(
	`account_number` VARCHAR(14),
	`transaction_amount` DOUBLE(10,2)
) RETURNS tinyint(1)
BEGIN 
	DECLARE returnValue BOOLEAN; 
	if EXISTS (SELECT * FROM balance_accounts WHERE balance_accounts.`number` = `account_number` AND balance_accounts.balance >= transaction_amount) then 
		SET returnValue = TRUE; 
	ELSE 
		SET returnValue = FALSE; 
	END if; 
	RETURN returnValue; 
END//
DELIMITER ;

-- Dumpar struktur för händelse bankfuralle.hourly_cleanup
DELIMITER //
CREATE DEFINER=`test`@`localhost` EVENT `hourly_cleanup` ON SCHEDULE EVERY 1 HOUR STARTS '2019-03-21 13:33:33' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN 
	DELETE FROM transactions WHERE from_account IS NULL AND to_account IS NULL;
	DELETE FROM monthly_transactions WHERE from_account IS NULL AND to_account IS NULL;
	DELETE FROM transactions WHERE amount = 0;
END//
DELIMITER ;

-- Dumpar struktur för tabell bankfuralle.monthly_transactions
CREATE TABLE IF NOT EXISTS `monthly_transactions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `from_account` varchar(14) COLLATE utf8_swedish_ci DEFAULT NULL,
  `to_account` varchar(14) COLLATE utf8_swedish_ci DEFAULT NULL,
  `amount` double(10,2) unsigned NOT NULL,
  `message` varchar(50) COLLATE utf8_swedish_ci NOT NULL DEFAULT 'Transaction',
  `day_of_month` tinyint(2) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_monthly_transactions_accounts` (`from_account`(4)),
  KEY `FK_monthly_transactions_accounts_2` (`to_account`(4)),
  KEY `FK_monthly_transactions_accounts_from` (`from_account`),
  KEY `FK_monthly_transactions_accounts_to` (`to_account`),
  CONSTRAINT `FK_monthly_transactions_accounts_from` FOREIGN KEY (`from_account`) REFERENCES `accounts` (`number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_monthly_transactions_accounts_to` FOREIGN KEY (`to_account`) REFERENCES `accounts` (`number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

-- Dumpar data för tabell bankfuralle.monthly_transactions: ~3 rows (ungefär)
/*!40000 ALTER TABLE `monthly_transactions` DISABLE KEYS */;
INSERT INTO `monthly_transactions` (`id`, `from_account`, `to_account`, `amount`, `message`, `day_of_month`) VALUES
	(2, '8989123123123', '4747852852852', 200.00, 'mitt test', 22),
	(3, '8989123123123', '96589658965896', 100.00, 'Hej svej', 25),
	(4, '8989123123123', '96589658965896', 200.00, 'Pengar', 25);
/*!40000 ALTER TABLE `monthly_transactions` ENABLE KEYS */;

-- Dumpar struktur för procedur bankfuralle.pay_salary
DELIMITER //
CREATE DEFINER=`test`@`localhost` PROCEDURE `pay_salary`(
	IN `employee_ssn` VARCHAR(10),
	IN `in_amount` DOUBLE(10,2),
	IN `in_message` VARCHAR(15),
	IN `transaction_date` VARCHAR(10),
	OUT `validate` BOOLEAN




)
BEGIN 
	DECLARE costumer_id INT(11);
	DECLARE costumer_salary_account VARCHAR(14);
	SET costumer_id = (SELECT id FROM `costumers` WHERE `SSN` = `employee_ssn`); 
	SET costumer_salary_account = (SELECT `number` FROM `accounts` WHERE `owner_id`=`costumer_id` AND `type`='SALARY' LIMIT 1);
	if costumer_salary_account IS NOT NULL AND in_amount > 0 then
		CALL create_transaction(0, `costumer_salary_account`, `in_amount`, `in_message`,`transaction_date`);
		SET validate = TRUE; 
	ELSE 
		SET validate = FALSE;
	END if; 
	SELECT validate;
END//
DELIMITER ;

-- Dumpar struktur för funktion bankfuralle.today_is_the_last_in_month
DELIMITER //
CREATE DEFINER=`test`@`localhost` FUNCTION `today_is_the_last_in_month`() RETURNS tinyint(1)
BEGIN 
	DECLARE diffingdays INT; 
	DECLARE returnvalue boolean;
	SET diffingdays = (SELECT DATEDIFF(DATE_SUB(CONCAT(EXTRACT(YEAR FROM CURRENT_DATE),'-',EXTRACT(MONTH FROM ADDDATE(CURRENT_DATE, INTERVAL 1 MONTH)), '-01'), INTERVAL 1 DAY), CURRENT_DATE));
	if diffingdays = 0 then 
		SET returnvalue = TRUE; 
	ELSE 
		SET returnvalue = FALSE; 
	END if; 
	RETURN returnvalue; 
END//
DELIMITER ;

-- Dumpar struktur för vy bankfuralle.total_transactions
-- Skapar temporärtabell för att hantera VIEW-beroendefel
CREATE TABLE `total_transactions` (
	`owner` INT(11) UNSIGNED NULL,
	`account_id` INT(11) UNSIGNED NULL,
	`number` VARCHAR(14) NULL COLLATE 'utf8_swedish_ci',
	`amount` DOUBLE NOT NULL,
	`message` VARCHAR(15) NOT NULL COLLATE 'utf8_swedish_ci',
	`time_of_transaction` DATETIME NOT NULL,
	`status` VARCHAR(10) NOT NULL COLLATE 'utf8_swedish_ci'
) ENGINE=MyISAM;

-- Dumpar struktur för tabell bankfuralle.transactions
CREATE TABLE IF NOT EXISTS `transactions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `from_account` int(11) unsigned DEFAULT NULL,
  `to_account` int(11) unsigned DEFAULT NULL,
  `amount` double(10,2) unsigned NOT NULL DEFAULT '0.00',
  `message` varchar(15) COLLATE utf8_swedish_ci NOT NULL DEFAULT 'Transaction',
  `time_of_transaction` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('DONE','WAITING','DENIED','PROCESSING') COLLATE utf8_swedish_ci NOT NULL DEFAULT 'WAITING',
  PRIMARY KEY (`id`),
  KEY `FK_transactions_accounts` (`from_account`),
  KEY `FK_transactions_accounts_2` (`to_account`),
  CONSTRAINT `FK_transactions_accounts` FOREIGN KEY (`from_account`) REFERENCES `accounts` (`id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `FK_transactions_accounts_2` FOREIGN KEY (`to_account`) REFERENCES `accounts` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=302 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

-- Dumpar data för tabell bankfuralle.transactions: ~52 rows (ungefär)
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` (`id`, `from_account`, `to_account`, `amount`, `message`, `time_of_transaction`, `status`) VALUES
	(244, NULL, 8, 26000.00, 'lön', '2019-03-22 20:04:18', 'DONE'),
	(245, NULL, 10, 29500.00, 'lön', '2019-03-22 20:05:36', 'DONE'),
	(246, 8, NULL, 10.00, 'TESTA KORT', '2019-03-22 20:13:15', 'DONE'),
	(247, 8, NULL, 10.00, 'TESTA KORT', '2019-03-22 20:13:37', 'DONE'),
	(248, 8, NULL, 1000.00, 'TESTA KORT', '2019-03-22 20:15:27', 'DONE'),
	(249, 8, NULL, 20000.00, 'TESTA KORT', '2019-03-22 20:15:37', 'DONE'),
	(250, 8, NULL, 200.00, 'TESTA KORT', '2019-03-22 20:18:29', 'DONE'),
	(251, 8, NULL, 200.00, 'TESTA KORT', '2019-03-22 20:18:31', 'DONE'),
	(252, 8, NULL, 200.00, 'TESTA KORT', '2019-03-22 20:18:33', 'DONE'),
	(253, 8, NULL, 1200.00, 'TESTA KORT', '2019-03-22 20:19:41', 'DONE'),
	(254, 8, NULL, 1200.00, 'TESTA KORT', '2019-03-22 20:19:44', 'DONE'),
	(255, 8, NULL, 1200.00, 'TESTA KORT', '2019-03-22 20:19:46', 'DONE'),
	(256, NULL, 10, 229500.00, 'lön', '2019-03-22 20:21:55', 'DONE'),
	(257, 10, NULL, 1200.00, 'TESTA KORT', '2019-03-22 20:25:46', 'DONE'),
	(258, 10, NULL, 3000.00, 'TESTA KORT', '2019-03-22 20:26:01', 'DONE'),
	(259, 10, NULL, 3000.00, 'TESTA KORT', '2019-03-22 20:26:04', 'DONE'),
	(260, 10, NULL, 25000.00, 'TESTA KORT', '2019-03-22 20:26:11', 'DONE'),
	(261, 10, NULL, 25000.00, 'TESTA KORT', '2019-03-22 20:26:16', 'DONE'),
	(262, 10, NULL, 25000.00, 'TESTA KORT', '2019-03-22 20:26:37', 'DONE'),
	(263, 10, NULL, 25000.00, 'TESTA KORT', '2019-03-22 20:26:41', 'DONE'),
	(264, 10, NULL, 25000.00, 'TESTA KORT', '2019-03-22 20:26:45', 'DONE'),
	(265, 10, NULL, 25000.00, 'TESTA KORT', '2019-03-22 20:26:50', 'DONE'),
	(266, 10, NULL, 1000.00, 'TESTA KORT', '2019-03-22 20:29:28', 'DONE'),
	(267, 10, NULL, 1000.00, 'TESTA KORT', '2019-03-22 20:32:51', 'DONE'),
	(268, 10, NULL, 10000.00, 'TESTA KORT', '2019-03-22 20:32:55', 'DONE'),
	(269, 10, NULL, 10000.00, 'TESTA KORT', '2019-03-22 20:33:02', 'DONE'),
	(270, 10, NULL, 2000.00, 'TESTA KORT', '2019-03-22 20:33:57', 'DONE'),
	(271, 10, NULL, 10000.00, 'TESTA KORT', '2019-03-22 20:34:06', 'DONE'),
	(273, 10, NULL, 10.00, 'TESTA KORT', '2019-03-22 20:42:39', 'DONE'),
	(274, 10, NULL, 100.00, 'TESTA KORT', '2019-03-22 20:42:45', 'DONE'),
	(275, 10, NULL, 100.00, 'TESTA KORT', '2019-03-22 20:43:18', 'DONE'),
	(276, NULL, 8, 229500.00, 'lön', '2019-03-22 20:44:16', 'DONE'),
	(277, NULL, 8, 10.00, 'lön', '2019-03-22 20:44:26', 'DONE'),
	(278, 8, 8, 600.00, 'Transaction', '2019-03-25 00:46:21', 'DONE'),
	(279, 8, 10, 200.00, 'högst', '2019-03-23 19:48:34', 'DONE'),
	(280, 8, NULL, 120.00, '123test', '2019-03-25 12:55:53', 'DONE'),
	(281, 10, 11, 100.00, 'Hej svej', '2019-03-25 13:46:43', 'DONE'),
	(282, 8, 14, 129.00, 'Test konto', '2019-03-25 13:51:59', 'DONE'),
	(283, 10, 11, 200.00, 'Pengar', '2019-03-25 19:32:14', 'DONE'),
	(284, 10, 21, 100.00, 'TESTA KORT', '2019-03-25 20:07:56', 'DONE'),
	(285, 10, 21, 100.00, 'TESTA KORT', '2019-03-25 20:08:59', 'DONE'),
	(286, 10, 21, 100.00, 'TESTA KORT', '2019-03-25 20:09:16', 'DONE'),
	(287, 10, 21, 100.00, 'TESTA KORT', '2019-03-25 20:28:55', 'DONE'),
	(288, 10, 21, 100.00, 'TESTA KORT', '2019-03-25 20:38:23', 'DONE'),
	(289, 10, 21, 100.00, 'TESTA KORT', '2019-03-25 20:39:10', 'DONE'),
	(290, 10, 21, 100.00, 'TESTA KORT', '2019-03-25 20:39:42', 'DONE'),
	(291, 10, 21, 100.00, 'TESTA KORT', '2019-03-25 21:08:57', 'DONE'),
	(292, 10, 21, 100.00, 'TESTA 123T', '2019-03-25 21:09:06', 'DONE'),
	(293, 10, 21, 100.00, 'TESTA 123', '2019-03-25 21:13:11', 'DONE'),
	(294, 10, 21, 100.00, 'TESTA 123', '2019-03-25 21:13:30', 'DONE'),
	(295, 10, 21, 100.00, 'TESTA 123', '2019-03-25 21:15:35', 'DONE'),
	(296, 10, 21, 100.00, 'TESTA 123', '2019-03-25 22:18:58', 'DONE'),
	(297, 10, 21, 10.00, 'ICA', '2019-03-25 22:34:49', 'DONE'),
	(298, 10, 21, 100.00, 'COOP', '2019-03-25 22:39:40', 'DONE'),
	(299, 10, 21, 100.00, 'COOP', '2019-03-25 22:40:15', 'DONE'),
	(300, 10, 21, 100.00, 'COOP', '2019-03-25 22:40:37', 'DONE'),
	(301, NULL, 11, 120.00, 'rea', '2019-03-26 12:49:55', 'DONE');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;

-- Dumpar struktur för funktion bankfuralle.validate_transaction
DELIMITER //
CREATE DEFINER=`test`@`localhost` FUNCTION `validate_transaction`(transaction_id INT) RETURNS enum('WAITING','DONE','DENIED','PROCESSING') CHARSET utf8 COLLATE utf8_swedish_ci
BEGIN
	DECLARE `from_account_id` INT(11); 
	DECLARE `transaction_amount` DOUBLE(10,2);
	DECLARE `transaction_status` enum('WAITING','DONE','DENIED','PROCESSING'); 
	SET `transaction_status` = 'DENIED';
	SET `from_account_id` = (SELECT from_account FROM `transactions` WHERE transaction_id = id); 
	SET `transaction_amount` = (SELECT amount FROM `transactions` WHERE transaction_id = id); 
	if from_account_id is null or get_balance(from_account_id) >= transaction_amount THEN 
		SET `transaction_status` = 'DONE'; 
	END if;
	return `transaction_status`;
END//
DELIMITER ;

-- Dumpar struktur för vy bankfuralle.withdrawals
-- Skapar temporärtabell för att hantera VIEW-beroendefel
CREATE TABLE `withdrawals` (
	`owner` INT(11) UNSIGNED NULL,
	`account_id` INT(11) UNSIGNED NULL,
	`number` VARCHAR(14) NULL COLLATE 'utf8_swedish_ci',
	`amount` DOUBLE(19,2) NOT NULL,
	`message` VARCHAR(15) NOT NULL COLLATE 'utf8_swedish_ci',
	`time_of_transaction` DATETIME NOT NULL,
	`status` ENUM('DONE','WAITING','DENIED','PROCESSING') NOT NULL COLLATE 'utf8_swedish_ci'
) ENGINE=MyISAM;

-- Dumpar struktur för trigger bankfuralle.perform_autogiro_payments
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='';
DELIMITER //
CREATE TRIGGER `perform_autogiro_payments` AFTER UPDATE ON `autogiro` FOR EACH ROW BEGIN
	CALL create_transaction(NEW.from_account, NEW.to_account, '299', 'Autogiro', '0000-00-00'); 
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Dumpar struktur för trigger bankfuralle.perform_monthly_transactions
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `perform_monthly_transactions` BEFORE UPDATE ON `monthly_transactions` FOR EACH ROW BEGIN 
	CALL create_transaction(NEW.from_account,NEW.to_account,NEW.amount,NEW.message, '0000-00-00');
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Dumpar struktur för trigger bankfuralle.perform_transactions
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `perform_transactions` BEFORE UPDATE ON `transactions` FOR EACH ROW BEGIN 
	if NEW.`status` = 'PROCESSING' then 
		SET NEW.`status` = validate_transaction(NEW.id);
		SET NEW.time_of_transaction = CURRENT_TIMESTAMP; 
	end if;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Dumpar struktur för vy bankfuralle.balance_accounts
-- Tar bort temporärtabell och skapar slutgiltlig VIEW-struktur
DROP TABLE IF EXISTS `balance_accounts`;
CREATE ALGORITHM=UNDEFINED DEFINER=`test`@`localhost` SQL SECURITY DEFINER VIEW `balance_accounts` AS select `accounts`.`id` AS `id`,`accounts`.`owner_id` AS `owner_id`,`accounts`.`number` AS `number`,`accounts`.`type` AS `type`,`accounts`.`name` AS `name`,`get_balance`(`accounts`.`id`) AS `balance` from `accounts`;

-- Dumpar struktur för vy bankfuralle.deposits
-- Tar bort temporärtabell och skapar slutgiltlig VIEW-struktur
DROP TABLE IF EXISTS `deposits`;
CREATE ALGORITHM=UNDEFINED DEFINER=`test`@`localhost` SQL SECURITY DEFINER VIEW `deposits` AS select `accounts`.`owner_id` AS `owner`,`transactions`.`to_account` AS `account_id`,`accounts`.`number` AS `number`,`transactions`.`amount` AS `amount`,`transactions`.`message` AS `message`,`transactions`.`time_of_transaction` AS `time_of_transaction`,`transactions`.`status` AS `status` from (`transactions` left join `accounts` on((`transactions`.`to_account` = `accounts`.`id`))) where (`accounts`.`owner_id` is not null) order by `transactions`.`time_of_transaction` desc;

-- Dumpar struktur för vy bankfuralle.total_transactions
-- Tar bort temporärtabell och skapar slutgiltlig VIEW-struktur
DROP TABLE IF EXISTS `total_transactions`;
CREATE ALGORITHM=UNDEFINED DEFINER=`test`@`localhost` SQL SECURITY DEFINER VIEW `total_transactions` AS select `withdrawals`.`owner` AS `owner`,`withdrawals`.`account_id` AS `account_id`,`withdrawals`.`number` AS `number`,`withdrawals`.`amount` AS `amount`,`withdrawals`.`message` AS `message`,`withdrawals`.`time_of_transaction` AS `time_of_transaction`,`withdrawals`.`status` AS `status` from `withdrawals` union select `deposits`.`owner` AS `owner`,`deposits`.`account_id` AS `account_id`,`deposits`.`number` AS `number`,`deposits`.`amount` AS `amount`,`deposits`.`message` AS `message`,`deposits`.`time_of_transaction` AS `time_of_transaction`,`deposits`.`status` AS `status` from `deposits`;

-- Dumpar struktur för vy bankfuralle.withdrawals
-- Tar bort temporärtabell och skapar slutgiltlig VIEW-struktur
DROP TABLE IF EXISTS `withdrawals`;
CREATE ALGORITHM=UNDEFINED DEFINER=`test`@`localhost` SQL SECURITY DEFINER VIEW `withdrawals` AS select `accounts`.`owner_id` AS `owner`,`transactions`.`from_account` AS `account_id`,`accounts`.`number` AS `number`,(`transactions`.`amount` * -(1)) AS `amount`,`transactions`.`message` AS `message`,`transactions`.`time_of_transaction` AS `time_of_transaction`,`transactions`.`status` AS `status` from (`transactions` left join `accounts` on((`transactions`.`from_account` = `accounts`.`id`))) where (`accounts`.`owner_id` is not null) order by `transactions`.`time_of_transaction` desc;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
