package com.github.maxos.shitSalary.paid;

public record PaidEntity(
        double minPay,
        double maxPay,
        PaidType paidType
) {}
