package com.upc.saveup.service.impl;

import com.upc.saveup.dto.PayDto;
import com.upc.saveup.exception.ValidationException;
import com.upc.saveup.model.Customer;
import com.upc.saveup.model.Pay;
import com.upc.saveup.repository.CardRepository;
import com.upc.saveup.repository.CustomerRepository;
import com.upc.saveup.repository.PayRepository;
import com.upc.saveup.service.PayService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PayServiceImpl implements PayService {

    private final PayRepository payRepository;
    private final CustomerRepository customerRepository;
    private final CardRepository cardRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public PayServiceImpl(PayRepository payRepository, CustomerRepository customerRepository, CardRepository cardRepository, ModelMapper modelMapper) {
        this.payRepository = payRepository;
        this.customerRepository = customerRepository;
        this.cardRepository = cardRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Pay createPay(PayDto payDto){
        Pay pay = DtoToEntity(payDto);

        Customer customer = customerRepository.findByNameAndLastNameAndPhoneNumber(payDto.getCustomerName(), payDto.getCustomerLastName(), payDto.getPhoneNumber());
        pay.setCustomer(customer);

        return payRepository.save(pay);
    }

    @Override
    public void updatePay(PayDto payDto){
        Pay pay = DtoToEntity(payDto);

        Customer customer = customerRepository.findByNameAndLastNameAndPhoneNumber(payDto.getCustomerName(), payDto.getCustomerLastName(), payDto.getPhoneNumber());

        if(!cardRepository.existsByCardNumberAndCustomerId(payDto.getCardNumber(), customer.getId()))
        {
            throw new ValidationException("La tarjeta no pertenece al cliente");
        }

        pay.setCustomer(customer);

        payRepository.save(pay);
    }

    @Override
    public void updateAmountPay(Pay pay){
        payRepository.save(pay);
    }

    @Override
    public Pay getPay(int id){ return payRepository.findById(id).get(); }

    @Override
    public void deletePay(int id){ payRepository.deleteById(id); }

    @Override
    public List<Pay> getAllPays(){ return (List<Pay>) payRepository.findAll(); }

    @Override
    public boolean isPayExist(int id){ return payRepository.existsById(id); }

    private PayDto EntityToDto(Pay pay) { return modelMapper.map(pay, PayDto.class); }

    private Pay DtoToEntity(PayDto payDto) {
        Pay pay = modelMapper.map(payDto, Pay.class);
        pay.setDate(LocalDate.now());

        return pay;
    }
}
