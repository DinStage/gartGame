package ru.dubrovin.cartgame.entity;


import ru.dubrovin.cartgame.enums.CartSuit;
import ru.dubrovin.cartgame.enums.NominalCart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CartDeck {
    private List<Cart> cartList;//список карт в коледе
    private Random random = new Random(); // рандомайцер

    /**
     * генерируем колоду карт , перебираем все ENUM и все Номиналы
     */
    public CartDeck() {
        cartList = new ArrayList<>();
        int value = NominalCart.values().length;
        for (NominalCart nominalCart : NominalCart.values()) {
            for (CartSuit cartSuit : CartSuit.values()) {
                Cart cart = new Cart(cartSuit, nominalCart);
                cart.setValue(value);
                cartList.add(cart);

            }
            value = value - 1;
        }

        mixCart();
    }

    /**
     * Выдать указанную карту из колоды (проверить ее налицие в колоде)
     */
    public Cart getCartFromCartDesk(Cart cart) throws Exception {

        for (Cart cartOfCartList : cartList) {
            if (cartOfCartList.equals(cart)) {
                cartList.remove(cartOfCartList);
                return cartOfCartList;
            }

        }
        throw new Exception("Такой карты нет в колоде");

    }

    public List<Cart> getCartList() {
        return cartList;
    }

    /**
     * перемешать карты в колоде
     */
    public void mixCart() {

        cartList.forEach(
                value -> {
                    value.setOrderSorted(random.nextInt(1000000));
                }
        );
        cartList.sort((o1, o2) -> o1.getOrderSorted() - o2.getOrderSorted());
    }

    /**
     * получить случайную карту из колоды
     */
    public Cart randomCart() throws Exception {

        if (cartList.size() == 0)
            throw new Exception("Нет карт в колоде!");
        int randomInt = 0;
        if (cartList.size() > 0)
            randomInt = random.nextInt(cartList.size());

        Cart cart = cartList.get(0);
        cartList.remove(cart);
        return cart;
    }


}
