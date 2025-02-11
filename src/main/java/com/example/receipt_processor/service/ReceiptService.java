package com.example.receipt_processor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import com.example.receipt_processor.entity.Receipt;
import com.example.receipt_processor.entity.Item;
import com.example.receipt_processor.repository.ReceiptRepository;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    public String saveReceipt(Receipt receipt) {
        String id = UUID.randomUUID().toString();
        receipt.setId(id);
        receiptRepository.save(receipt);
        return id;
    }

    public int calculatePoints(String id) {
        Optional<Receipt> receiptOpt = receiptRepository.findById(id);
        if (receiptOpt.isEmpty()) {
            throw new IllegalArgumentException("Receipt not found");
        }

        Receipt receipt = receiptOpt.get();
        int points = 0;

        points += receipt.getRetailer().chars().filter(Character::isLetterOrDigit).count();
        System.out.println("Points after retailer name: " + points);

        if (Math.floor(receipt.getTotal()) == receipt.getTotal()) {
            points += 50;
        }
        System.out.println("Points after checking round total: " + points);

        if (receipt.getTotal() % 0.25 == 0) {
            points += 25;
        }
        System.out.println("Points after checking multiple of 0.25: " + points);

        if (receipt.getItems() != null && !receipt.getItems().isEmpty()) {
            points += (receipt.getItems().size() / 2) * 5;
        }
        System.out.println("Points after checking number of items: " + points);

        for (Item item : receipt.getItems()) {
            String trimmedDesc = item.getShortDescription().trim();
            if (trimmedDesc.length() % 3 == 0) {
                points += (int) Math.ceil(item.getPrice() * 0.2);
            }
        }
        System.out.println("Points after checking item descriptions: " + points);

        if (receipt.getTotal() > 10.00) {
            points += 5;
        }
        System.out.println("Points after checking total > 10.00: " + points);

        if (receipt.getPurchaseDate().getDayOfMonth() % 2 != 0) {
            points += 6;
        }
        System.out.println("Points after checking odd day: " + points);

        int hour = receipt.getPurchaseTime().getHour();
        if (hour >= 14 && hour < 16) {
            points += 10;
        }
        System.out.println("Final Points: " + points);

        return points;
    }
}
