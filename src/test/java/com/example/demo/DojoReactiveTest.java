package com.example.demo;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;
import java.util.stream.Collectors;

public class DojoReactiveTest {

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }

    @Test
    void jugadoresMayoresA35() {
        List<Player> list = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(list);

        observable.filter(jugador -> jugador.getAge() > 35)
                .subscribe(System.out::println);
    }


    @Test
    void jugadoresMayoresA35SegunClub(){
        List<Player> readCsv = CsvUtilFile.getPlayers();
        Flux<Player> observable = Flux.fromIterable(readCsv);

        observable.filter(player -> player.getAge() > 35)
                .distinct()
                .groupBy(Player::getClub)
                .flatMap(groupedFlux -> groupedFlux
                        .collectList()
                        .map(list -> {
                            Map<String, List<Player>> map = new HashMap<>();
                            map.put(groupedFlux.key(), list);
                            return map;
                        }))
                .subscribe(map -> {
                    map.forEach((key, value) -> {
                        System.out.println("\n");
                        System.out.println(key + ": ");
                        value.forEach(System.out::println);
                    });
                });

    }


    @Test
    void mejorJugadorConNacionalidadFrancia() {
        Flux<Player> playersFlux = Flux.fromIterable(CsvUtilFile.getPlayers());
        Mono<Player> mejorJugadorMono = playersFlux
                .filter(player -> player.getNational().equals("Francia"))
                .reduce((player1, player2) ->
                        player1.getWinners() > player2.getWinners() ? player1 : player2
                );

        StepVerifier.create(mejorJugadorMono)
                .expectNextMatches(player -> player.getNational().equals("Francia"))
                .verifyComplete();
    }

    @Test
    void clubsAgrupadosPorNacionalidad() {
        Flux<Player> playersFlux = Flux.fromIterable(CsvUtilFile.getPlayers());
        Mono<Map<String, List<String>>> clubsPorNacionalidadMono = playersFlux
                .collect(Collectors.groupingBy(Player::getNational, Collectors.mapping(Player::getClub, Collectors.toList())));

        StepVerifier.create(clubsPorNacionalidadMono)
                .expectNextMatches(map -> map.containsKey("Francia") && map.containsKey("España"))
                .verifyComplete();
    }

    @Test
    void clubConElMejorJugador() {
        Flux<Player> playersFlux = Flux.fromIterable(CsvUtilFile.getPlayers());
        Mono<String> clubMejorJugadorMono = playersFlux
                .reduce((player1, player2) ->
                        player1.getWinners() > player2.getWinners() ? player1 : player2
                )
                .map(Player::getClub);

        StepVerifier.create(clubMejorJugadorMono)
                .expectNextMatches(club -> !club.isEmpty())
                .verifyComplete();
    }

    @Test
    void clubConElMejorJugador2() {
        Flux<Player> playersFlux = Flux.fromIterable(CsvUtilFile.getPlayers());
        Mono<String> clubMejorJugadorMono = playersFlux
                .collect(Collectors.maxBy(Comparator.comparingInt(Player::getWinners)))
                .map(Optional::get)
                .map(Player::getClub);

        StepVerifier.create(clubMejorJugadorMono)
                .expectNextMatches(club -> !club.isEmpty())
                .verifyComplete();
    }

    @Test
    void elMejorJugador() {
        Flux<Player> playersFlux = Flux.fromIterable(CsvUtilFile.getPlayers());
        Mono<Player> mejorJugadorMono = playersFlux
                .reduce((player1, player2) ->
                        player1.getWinners() > player2.getWinners() ? player1 : player2
                );

        StepVerifier.create(mejorJugadorMono)
                .expectNextMatches(player -> player.getWinners() > 0)
                .verifyComplete();
    }

    @Test
    void elMejorJugador2() {
        Flux<Player> playersFlux = Flux.fromIterable(CsvUtilFile.getPlayers());
        Mono<Player> mejorJugadorMono = playersFlux
                .sort(Comparator.comparingInt(Player::getWinners).reversed())
                .next();

        StepVerifier.create(mejorJugadorMono)
                .expectNextMatches(player -> player.getWinners() > 0)
                .verifyComplete();
    }

    @Test
    void mejorJugadorSegunNacionalidad() {
        Flux<Player> playersFlux = Flux.fromIterable(CsvUtilFile.getPlayers());
        Mono<Player> mejorJugadorSegunNacionalidadMono = playersFlux
                .filter(player -> player.getNational().equals("Francia"))
                .sort(Comparator.comparingInt(Player::getWinners).reversed())
                .next();

        StepVerifier.create(mejorJugadorSegunNacionalidadMono)
                .expectNextMatches(player -> player.getNational().equals("Francia"))
                .verifyComplete();
    }
}
