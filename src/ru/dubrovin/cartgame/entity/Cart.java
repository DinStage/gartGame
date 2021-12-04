package ru.dubrovin.cartgame.entity;


import ru.dubrovin.cartgame.enums.CartSuit;
import ru.dubrovin.cartgame.enums.NominalCart;

import java.util.Objects;

public class Cart {

    private int value; // "боевая" сила карты
    private CartSuit cartSuit; // масть
    private NominalCart nominal; // номинал
    private int orderSorted; // порядок сортировки для рандомной сортировки

    public int getValue() {
        return value;
    }

    public int getOrderSorted() {
        return orderSorted;
    }

    public void setOrderSorted(int orderSorted) {
        this.orderSorted = orderSorted;
    }

    public CartSuit getCartSuit() {
        return cartSuit;
    }

    public Cart(CartSuit cartSuit, NominalCart nominal) {

        this.cartSuit = cartSuit;
        this.nominal = nominal;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return cartSuit == cart.cartSuit && nominal == cart.nominal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, cartSuit, nominal);
    }

    @Override
    public String toString() {
        return "{" + cartSuit + " - " +  nominal +"}";
    }
}