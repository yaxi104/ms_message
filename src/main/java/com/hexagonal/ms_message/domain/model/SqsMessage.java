package com.hexagonal.ms_message.domain.model;

public class SqsMessage<T> {
    private final T body;
    private final String receiptHandle;

    public SqsMessage(T body, String receiptHandle) {
        this.body = body;
        this.receiptHandle = receiptHandle;
    }

    public T getBody() {
        return body;
    }

    public String getReceiptHandle() {
        return receiptHandle;
    }
}
