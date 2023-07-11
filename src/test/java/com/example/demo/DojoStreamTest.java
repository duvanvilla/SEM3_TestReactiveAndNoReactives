package com.example.demo;


import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class DojoStreamTest {

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }

    @Test
    void jugadoresMayoresA35(){
        List<Player> list = CsvUtilFile.getPlayers();
        Set<Player> result = list.stream()
                .filter(jugador -> jugador.getAge() > 35)
                .collect(Collectors.toSet());
        result.forEach(System.out::println);
    }

    @Test
    void jugadoresMayoresA35SegunClub(){
        List<Player> list = CsvUtilFile.getPlayers();
        Map<String, List<Player>> result = list.stream()
                .filter(player -> player.getAge() > 35)
                .distinct()
                .collect(Collectors.groupingBy(Player::getClub));

        result.forEach((key, jugadores) -> {
            System.out.println("\n");
            System.out.println(key + ": ");
            jugadores.forEach(System.out::println);
        });

    }

    @Test
    void mejorJugadorConNacionalidadFrancia() {
        List<Player> players = CsvUtilFile.getPlayers();
        Optional<Player> mejorJugador = players.stream()
                .filter(player -> player.getNational().equals("Francia"))
                .max(Comparator.comparingInt(Player::getWinners));

        if (mejorJugador.isPresent()) {
            Player jugador = mejorJugador.get();
            System.out.println("Mejor jugador con nacionalidad Francia: " + jugador);
        }
    }

    @Test
    void clubsAgrupadosPorNacionalidad() {
        List<Player> players = CsvUtilFile.getPlayers();
        Map<String, List<String>> clubsPorNacionalidad = players.stream()
                .collect(Collectors.groupingBy(Player::getNational, Collectors.mapping(Player::getClub, Collectors.toList())));

        System.out.println("Clubs agrupados por nacionalidad:");
        clubsPorNacionalidad.forEach((nacionalidad, clubs) -> System.out.println(nacionalidad + ": " + clubs));
    }

    @Test
    void clubConElMejorJugador() {
        List<Player> players = CsvUtilFile.getPlayers();
        Optional<Player> mejorJugador = players.stream()
                .max(Comparator.comparingInt(Player::getWinners));

        if (mejorJugador.isPresent()) {
            Player jugador = mejorJugador.get();
            System.out.println("Club con el mejor jugador: " + jugador.getClub());
        }
    }

    @Test
    void clubConElMejorJugador2() {
        List<Player> players = CsvUtilFile.getPlayers();
        Optional<Player> mejorJugador = players.stream()
                .max(Comparator.comparingInt(Player::getWinners));

        mejorJugador.ifPresent(jugador -> System.out.println("Club con el mejor jugador: " + jugador.getClub()));
    }

    @Test
    void elMejorJugador() {
        List<Player> players = CsvUtilFile.getPlayers();
        Optional<Player> mejorJugador = players.stream()
                .max(Comparator.comparingInt(Player::getWinners));

        if (mejorJugador.isPresent()) {
            Player jugador = mejorJugador.get();
            System.out.println("El mejor jugador: " + jugador);
        }
    }

    @Test
    void elMejorJugador2() {
        List<Player> players = CsvUtilFile.getPlayers();
        Optional<Player> mejorJugador = players.stream()
                .sorted(Comparator.comparingInt(Player::getWinners).reversed())
                .findFirst();

        mejorJugador.ifPresent(jugador -> System.out.println("El mejor jugador: " + jugador));
    }

    @Test
    void mejorJugadorSegunNacionalidad() {
        List<Player> players = CsvUtilFile.getPlayers();
        Optional<Player> mejorJugador = players.stream()
                .filter(player -> player.getNational().equals("Francia"))
                .max(Comparator.comparingInt(Player::getWinners));

        if (mejorJugador.isPresent()) {
            Player jugador = mejorJugador.get();
            System.out.println("Mejor jugador con nacionalidad Francia: " + jugador);
        }
    }
}
