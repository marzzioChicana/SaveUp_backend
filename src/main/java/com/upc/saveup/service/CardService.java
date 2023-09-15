package com.upc.saveup.service;

import com.upc.saveup.model.Card;

import java.util.List;

public interface CardService {
    public abstract Card createCard(Card card);
    public abstract void updateCard(Card card);
    public abstract void deleteCard(int id);
    public abstract Card getCard(int id);
    public abstract List<Card> getCards();
    public abstract boolean isCardExist(int id);
}
