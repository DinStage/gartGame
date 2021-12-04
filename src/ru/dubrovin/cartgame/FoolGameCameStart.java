package ru.dubrovin.cartgame;

import ru.dubrovin.cartgame.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class FoolGameCameStart {
    public static void main(String[] args) throws Exception {

        List<Player> players  = List.of(new Player("Женя"), new Player("Лена"), new Player("Ваня") , new Player("Оля") , new Player("Гарри") );

        FoolCameCart foolCameCart = new FoolCameCart(players);
        foolCameCart.active();
    }
}
