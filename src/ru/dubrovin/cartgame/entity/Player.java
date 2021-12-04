package ru.dubrovin.cartgame.entity;


import ru.dubrovin.cartgame.enums.CartSuit;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;//Имя игрока
    private List<Cart> playerCartList = new ArrayList<>(); // Карты игрока

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Раздать игроку карты
     */
    public void setPlayerCartList(CartDeck cartDeck, int countCartGame) throws Exception {

        for (int i = playerCartList.size(); i < countCartGame; i++) {
            if (cartDeck.getCartList().size() > 0)
                playerCartList.add(cartDeck.randomCart());
        }

    }

    /**
     * Изъять карту из разданных карт игрока
     */
    public List<Cart> takeCart(Cart cart) {
        playerCartList.remove(cart);
        return playerCartList;
    }

    /**
     * Добавить картЫ игроку
     */
    public List<Cart> addCarts(List<Cart> cartList) {
        playerCartList.addAll(cartList);
        return playerCartList;
    }


    /**
     * Получение всех карт игрока
     */
    public List<Cart> getPlayerCartList() {
        return playerCartList;

    }
}
