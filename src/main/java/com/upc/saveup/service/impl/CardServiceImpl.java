package com.upc.saveup.service.impl;

import com.upc.saveup.model.Card;
import com.upc.saveup.repository.CardRepository;
import com.upc.saveup.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card createCard(Card card) {
        return cardRepository.save(card);
    }
    @Override
    public void updateCard(Card card){
        cardRepository.save(card);
    }
    @Override
    public Card getCard(int id){
        return cardRepository.findById(id).get();
    }
    @Override
    public void deleteCard(int id){
        cardRepository.deleteById(id);
    }
    @Override
    public List<Card> getCards(){
        return (List<Card>) cardRepository.findAll();
    }

    @Override
    public boolean isCardExist(int id){
        return cardRepository.existsById(id);
    }
}
