-- phpMyAdmin SQL Dump
-- version 4.6.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 11, 2016 at 03:51 PM
-- Server version: 5.7.13-log
-- PHP Version: 5.6.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sat`
--

-- --------------------------------------------------------

--
-- Table structure for table `chat`
--

CREATE TABLE `chat` (
  `id_sender` varchar(30) NOT NULL,
  `id_receiver` varchar(30) NOT NULL,
  `chat_message` varchar(255) NOT NULL,
  `datetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `group_chat`
--

CREATE TABLE `group_chat` (
  `id_sender` varchar(30) NOT NULL,
  `id_group` varchar(30) NOT NULL,
  `chat_message` varchar(255) NOT NULL,
  `datetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `group_sat`
--

CREATE TABLE `group_sat` (
  `id` varchar(255) NOT NULL,
  `Name` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `server`
--

CREATE TABLE `server` (
  `id` varchar(255) NOT NULL,
  `ip_address` varchar(15) NOT NULL,
  `port` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `server`
--

INSERT INTO `server` (`id`, `ip_address`, `port`) VALUES
('testingServer', '127.0.0.1', 2013),
('testingServer1', '127.0.0.1', 2012),
('testingServer2', '192.168.173.1', 2013);

-- --------------------------------------------------------

--
-- Table structure for table `user_group`
--

CREATE TABLE `user_group` (
  `id_group` varchar(255) NOT NULL,
  `id_member` varchar(255) NOT NULL,
  `public_key` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user_sat`
--

CREATE TABLE `user_sat` (
  `id` varchar(30) NOT NULL,
  `id_server` varchar(255) NOT NULL,
  `ip_address_port` varchar(25) DEFAULT NULL,
  `passwd` varchar(255) DEFAULT NULL,
  `prof_name` varchar(50) DEFAULT NULL,
  `public_key` varchar(255) DEFAULT NULL,
  `current_status` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_sat`
--

INSERT INTO `user_sat` (`id`, `id_server`, `ip_address_port`, `passwd`, `prof_name`, `public_key`, `current_status`) VALUES
('arcgrant', 'testingServer', '127.0.0.1:50391', 'testing', 'Arcgrant', '', 'offline'),
('broadcast', 'testingServer', '127.0.0.1:27015', 'slansdjlsndfasjkdbvasdjabksdhbfeawyiu', 'broadcast', 'fasbdfkashvasjgdvfasjdfgaisyufbasdjhvasdfaj', 'offline'),
('cete', 'testingServer1', '127.0.0.1:50388', 'testing', 'Christian Thomas', 'testing', 'offline'),
('egaprianto', 'testingServer', '127.0.0.1:57562', 'testing', 'Ega Prianto', 'testing', 'offline'),
('garlzend', 'testingServer', '127.0.0.1:54392', 'testing', 'Garlz', '', 'offline'),
('testing', 'testingServer', '127.0.0.1:54392', 'testing', 'testing', 'testing', 'offline'),
('wych', 'testingServer1', '127.0.0.1:57560', '12345678', 'nougat', '', 'offline');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chat`
--
ALTER TABLE `chat`
  ADD KEY `id_sender` (`id_sender`),
  ADD KEY `id_receiver` (`id_receiver`);

--
-- Indexes for table `group_chat`
--
ALTER TABLE `group_chat`
  ADD KEY `id_sender` (`id_sender`),
  ADD KEY `id_group` (`id_group`);

--
-- Indexes for table `group_sat`
--
ALTER TABLE `group_sat`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `server`
--
ALTER TABLE `server`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_group`
--
ALTER TABLE `user_group`
  ADD KEY `id_group` (`id_group`),
  ADD KEY `id_user` (`id_member`);

--
-- Indexes for table `user_sat`
--
ALTER TABLE `user_sat`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_server` (`id_server`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chat`
--
ALTER TABLE `chat`
  ADD CONSTRAINT `chat_ibfk_1` FOREIGN KEY (`id_sender`) REFERENCES `user_sat` (`id`),
  ADD CONSTRAINT `chat_ibfk_2` FOREIGN KEY (`id_receiver`) REFERENCES `user_sat` (`id`);

--
-- Constraints for table `group_chat`
--
ALTER TABLE `group_chat`
  ADD CONSTRAINT `group_chat_ibfk_1` FOREIGN KEY (`id_sender`) REFERENCES `user_sat` (`id`),
  ADD CONSTRAINT `group_chat_ibfk_2` FOREIGN KEY (`id_group`) REFERENCES `group_sat` (`id`);

--
-- Constraints for table `user_group`
--
ALTER TABLE `user_group`
  ADD CONSTRAINT `user_group_ibfk_1` FOREIGN KEY (`id_group`) REFERENCES `group_sat` (`id`),
  ADD CONSTRAINT `user_group_ibfk_2` FOREIGN KEY (`id_member`) REFERENCES `user_sat` (`id`);

--
-- Constraints for table `user_sat`
--
ALTER TABLE `user_sat`
  ADD CONSTRAINT `user_sat_ibfk_1` FOREIGN KEY (`id_server`) REFERENCES `server` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
