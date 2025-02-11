package com.example.receipt_processor.controller;

import com.example.receipt_processor.entity.Receipt;
import com.example.receipt_processor.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/receipts/process")
    public Map<String, String> processReceipt(@RequestBody Receipt receipt) {
        String id = receiptService.saveReceipt(receipt);
        Map<String, String> response = new HashMap<>();
        response.put("id", id);
        return response;
    }

    @GetMapping("/receipts/{id}/points")
    public Map<String, Integer> getPoints(@PathVariable String id) {
        int points = receiptService.calculatePoints(id);
        Map<String, Integer> response = new HashMap<>();
        response.put("points", points);
        return response;
    }
}