-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3307
-- Tiempo de generación: 03-12-2023 a las 19:13:43
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `yritik`
--
create database if not exists yritik;
use yritik;
drop table if exists artistas;
drop table if exists canciones;
drop table if exists favs;
drop table if exists usuarios;
-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `artistas`
--

CREATE TABLE `artistas` (
  `codigo` bigint(20) UNSIGNED NOT NULL,
  `nombre` varchar(20) DEFAULT NULL,
  `oyentes` varchar(20) DEFAULT NULL,
  `canciones` int(11) DEFAULT NULL,
  `albums` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `artistas`
--

INSERT INTO `artistas` (`codigo`, `nombre`, `oyentes`, `canciones`, `albums`) VALUES
(1, 'Quevedo', '30M', 29, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `canciones`
--

CREATE TABLE `canciones` (
  `codigo` int(11) NOT NULL,
  `nombre` varchar(20) DEFAULT NULL,
  `artista` varchar(20) DEFAULT NULL,
  `duracion` varchar(20) DEFAULT NULL,
  `link` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `canciones`
--

INSERT INTO `canciones` (`codigo`, `nombre`, `artista`, `duracion`, `link`) VALUES
(1, 'Playa Del Inglés', 'Quevedo', '03:57', 'Canciones/playadelingles.mp3'),
(2, 'Piel de Cordero', 'Quevedo', '03:39', 'Canciones/pielCordero.mp3'),
(3, 'Velocidad Crucero', 'Feid', '02:15', 'Canciones/velocidadCrucero.mp3'),
(4, 'Despacito', 'Luis Fonsi', '04:41', 'Canciones/Despacito.mp3'),
(5, 'Emrata', 'Myke Towers', '03:45', 'Canciones/Emrata.mp3'),
(6, 'Mi Casa', 'Cruz Cafuné', '02:46', 'Canciones/MiCasa.mp3');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `favs`
--

CREATE TABLE `favs` (
  `codigoFav` bigint(20) UNSIGNED NOT NULL,
  `codigoUsuario` int(11) NOT NULL,
  `codigoCancion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `favs`
--

INSERT INTO `favs` (`codigoFav`, `codigoUsuario`, `codigoCancion`) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 1, 6),
(4, 2, 4),
(5, 1, 3),
(6, 3, 6),
(9, 3, 1),
(10, 3, 3),
(11, 4, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `codigo` int(11) NOT NULL,
  `nombreCompleto` varchar(20) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `contra` varchar(20) DEFAULT NULL,
  `tlf1` int(9) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`codigo`, `nombreCompleto`, `username`, `contra`, `tlf1`, `email`) VALUES
(1, 'Ritik Punjabi', 'ritikthadani', '1234', 649011184, 'ritikthadani@gmail.com'),
(2, 'Néstor NoPareja', 'nestornopareja', '1234', 656775785, 'nestorcasipareja@gmail.com'),
(3, 'Admin', 'admin', '1234', 111111111, 'admin@gmail.com'),
(4, 'lauchi', 'lau', '1234', 213421421, 'lau@gmail.com');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `artistas`
--
ALTER TABLE `artistas`
  ADD PRIMARY KEY (`codigo`);

--
-- Indices de la tabla `canciones`
--
ALTER TABLE `canciones`
  ADD PRIMARY KEY (`codigo`);

--
-- Indices de la tabla `favs`
--
ALTER TABLE `favs`
  ADD PRIMARY KEY (`codigoFav`),
  ADD KEY `codigoCancion` (`codigoCancion`),
  ADD KEY `codigoUsuario` (`codigoUsuario`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`codigo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `artistas`
--
ALTER TABLE `artistas`
  MODIFY `codigo` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `canciones`
--
ALTER TABLE `canciones`
  MODIFY `codigo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `favs`
--
ALTER TABLE `favs`
  MODIFY `codigoFav` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `codigo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `favs`
--
ALTER TABLE `favs`
  ADD CONSTRAINT `favs_ibfk_1` FOREIGN KEY (`codigoCancion`) REFERENCES `canciones` (`codigo`),
  ADD CONSTRAINT `favs_ibfk_2` FOREIGN KEY (`codigoUsuario`) REFERENCES `usuarios` (`codigo`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
