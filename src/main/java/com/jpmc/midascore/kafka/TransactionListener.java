package com.jpmc.midascore.kafka;

import java.math.BigDecimal;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;

@Component
public class TransactionListener {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final RestTemplate restTemplate;

    public TransactionListener(
            UserRepository userRepository,
            TransactionRecordRepository transactionRecordRepository,
            RestTemplate restTemplate
    ) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(
        topics = "${general.kafka-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(Transaction transaction) {

        // 1. Fetch users
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (sender == null || recipient == null) {
            return;
        }

        float amount = transaction.getAmount();

        // 2. Validate balance
        if (sender.getBalance() < amount) {
            return;
        }

        // 3. Call Incentive API
        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                transaction,
                Incentive.class
        );

        float incentiveAmount = incentive.getAmount();

        // 4. Update balances
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);

        // 5. Save transaction record (with incentive)
        TransactionRecord record = new TransactionRecord(
                String.valueOf(transaction.getSenderId()),
                String.valueOf(transaction.getRecipientId()),
                BigDecimal.valueOf(amount),
                incentiveAmount
        );

        transactionRecordRepository.save(record);

        // TEMP: find Wilbur balance
        if ("wilbur".equals(sender.getName())) {
            System.out.println("WILBUR BALANCE = " + sender.getBalance());
        }
        if ("wilbur".equals(recipient.getName())) {
            System.out.println("WILBUR BALANCE = " + recipient.getBalance());
        }
    }
}
