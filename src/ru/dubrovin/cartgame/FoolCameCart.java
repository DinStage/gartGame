package ru.dubrovin.cartgame;

import ru.dubrovin.cartgame.entity.*;
import ru.dubrovin.cartgame.enums.CartSuit;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;


/**
 * Карточная игра ДУРАК КЛАСИЧЕСКИЙ НЕПЕРВОДНОЙ
 */
public class FoolCameCart {
    private CartDeck cartDeck;      // колода карт
    private List<Player> players;   // список игроков
    private int startCartCount = 6; // Количество карт для игрока
    private Cart privilegiesCart;   // Привилигерованная карта
    private List<Player> currentAttakingPlayers = new ArrayList<>(); // Текущие атакующие игроки ( те кто ходит)
    private Player currentDefenderPlayer; // Текущий защищающийся игрок (под кого ходят)
    private List<Cart> cartTable = new ArrayList<>(); // Текущий карточня стол

    public FoolCameCart(List<Player> players) throws Exception {
        this.cartDeck = new CartDeck();
        privilegiesCart = cartDeck.randomCart();
        this.players = players;
        //Раздаем карты
        dealCardsPlayers();

    }

    /**
     * Раздаем карты игрокам
     */
    public void dealCardsPlayers() {
        players.forEach(player -> {
            try {
                player.setPlayerCartList(this.cartDeck, startCartCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Определение с каой каррты будет заходить  - примитивная стратегия заходим всегда с наименьшей карты
     */
    public Cart startCart(Player player) throws Exception {
        List<Cart> playerCartList = player.getPlayerCartList();
        playerCartList.sort((o1, o2) -> o1.getValue() - o2.getValue());
        if (playerCartList.size() > 0)
            return playerCartList.get(0);

        throw new Exception("У игрока отсутствуют карты");
    }


    /**
     * Определение более сильной карты карточной игры ДУРАК
     */
    public int compositionCart(Cart cartOne, Cart cartTwo, CartSuit privelegionSuit) {
        if ((cartOne.getCartSuit() == cartTwo.getCartSuit()) && (cartOne.getValue() > cartTwo.getValue()))
            return 1;
        if ((cartTwo.getCartSuit() != privelegionSuit && cartOne.getCartSuit() == privelegionSuit))
            return 1;


        return -1;
    }

    /**
     * Выбор ЗАЩИТНОЙ карты , карты с которой отбиваются от противника
     **/
    public Cart getStrongCart(Cart cartAttack, Player player) {
        return player.getPlayerCartList().stream().filter(cartPlayer -> compositionCart( cartPlayer , cartAttack, privilegiesCart.getCartSuit()) > 0)
                .findFirst().orElse(null);

    }

    /**
     * Выдача карты на стол
     */
    public void addCartTable(Cart cart, Player player) {
        player.takeCart(cart);
        cartTable.add(cart);
    }

    /**
     * Передать карты с карточного стола указанному игроку
     */
    public void takeCartTableForPlayer(Player player) {
        player.addCarts(cartTable);
        cartTable.clear();
    }

    public Player getNextPlayer(Player player) {
        int indexNextPlayer = players.indexOf(player) + 1;
        if (indexNextPlayer >= players.size())
            indexNextPlayer = 0;

        return players.get(indexNextPlayer);


    }


    /**
     * ПолучитьТекущуКозырнуюКарту
     */
    public Cart getPrivilegiesCart() {
        return privilegiesCart;
    }

    /**
     * Определяем игрока с которого начинаем игру (минимальная козырная карта, или  минимальную карту из разданных )
     */
    public Player getStartPlayer() {
        // Получим минимальную козынуую карту с разданных игрокам
        Cart minCart;
        List<Cart> distribution = new ArrayList<>();
        players.forEach(player -> distribution.addAll(player.getPlayerCartList()));

        //** ищим игрока с минимальной козырной картой
        List<Cart> distributionPrivilegions = distribution.stream().filter(cart -> cart.getCartSuit() == privilegiesCart.getCartSuit()).collect(Collectors.toList());
        if (distributionPrivilegions.size() > 0) {
            distributionPrivilegions.sort((o1, o2) -> o1.getValue() - o2.getValue());
            minCart = distributionPrivilegions.get(0);

        } else {
            distribution.sort((o1, o2) -> o1.getValue() - o2.getValue());
            minCart = distribution.get(0);
        }
        System.out.println("Минимальная карта" + minCart.toString());

        return players.stream().filter(player -> player.getPlayerCartList().contains(minCart))
                .findFirst().orElse(null);


    }

    /**
     * установим текущий атакующих игроков и игрока который Защищается
     */
    public void setCurrentAttakingAndDefenderPlayers(Player attackingPlayer) {


        currentAttakingPlayers.clear();

        currentAttakingPlayers.add(attackingPlayer);
        currentDefenderPlayer = getNextPlayer(attackingPlayer);
        currentAttakingPlayers.add(getNextPlayer(currentDefenderPlayer));

    }

    /**
     * Очистка стола
     */
    public void clearCartTable() {
        cartTable.clear();
    }

    /**
     * Запуск игры с указанными игроками по разработанной стратегии
     */

    public void printPlayer() {
      /***
        System.out.println("-----------------------------------------------");

        players.forEach(player -> {

            System.out.println("Игрок : " + player.getName());
            System.out.println("Его карты " + player.getPlayerCartList());
            System.out.println("-----------------------------------------------");

        });
       */
        System.out.println("Атакующие игроки :" + currentAttakingPlayers);
        System.out.println("Защищающиеся игроки :" + currentDefenderPlayer);
        System.out.println("Количество карт в колоде " + cartDeck.getCartList().size());

        players.forEach(player -> {
            System.out.println("У игрока " + player.getName() + " карт : " + player.getPlayerCartList().size());
        });


    }

    public void active() throws Exception {


// Игроки начали играть

        System.out.println("Козырная карта:" + getPrivilegiesCart());
        Player player = getStartPlayer();
        setCurrentAttakingAndDefenderPlayers(player);


        int i = 0;
        boolean gameEnd = false;


        while (gameEnd == false) {

            i++;

            System.out.println("Шаг игры  №:" + i);

            Cart cartAttack = startCart(currentAttakingPlayers.get(0));
            addCartTable(cartAttack, player);
            System.out.println("Игрок  " + currentAttakingPlayers.get(0).getName() + " пошел картой " + cartAttack);

            Cart cartDefender = getStrongCart(cartAttack, currentDefenderPlayer);

            if (cartDefender == null) {
                System.out.println("Игроку " + currentDefenderPlayer.getName() + " нечем защититься!!! его карты " + currentDefenderPlayer.getPlayerCartList()+ " он забрал карты ");
                takeCartTableForPlayer(currentDefenderPlayer);
                setCurrentAttakingAndDefenderPlayers(getNextPlayer(currentDefenderPlayer)); // пределяем игроков от неотбившегося игрока

            } else {
                System.out.println("Игрок  " + currentDefenderPlayer.getName() + " защитился  картой " + cartDefender);
                currentDefenderPlayer.takeCart(cartDefender);
                currentAttakingPlayers.get(0).takeCart(cartAttack);
                // Очистим стол карты уходят в биту
                clearCartTable();

                setCurrentAttakingAndDefenderPlayers(currentDefenderPlayer); /// определяем игроков от неотбившегося игрока

            }


           // System.out.println("Дораздаем карты ");

            /*Раздаем карты игрокам*/
            dealCardsPlayers();


            /**
             * Примитивно если у игрока 0 карт то он победил!!!
             */

            ListIterator<Player> playerIterator = players.listIterator();
            while (playerIterator.hasNext()) {
                Player currentPlayer = playerIterator.next();
                if (currentPlayer.getPlayerCartList().size() == 0) {
                    System.out.println("Игрок победил  : " + currentPlayer.getName());
                    gameEnd = true;
                }
            }

            if (gameEnd)
                break;

           // printPlayer();
        }

    }

}
