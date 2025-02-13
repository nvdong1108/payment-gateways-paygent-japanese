# Guide to Integrating Visa, MasterCard, JCB Payments via Paygent

### Introduction

Paygent is a popular payment gateway in Japan that allows businesses to integrate online payments with cards such as Visa, MasterCard, JCB, and more. Paygent provides a robust API to support secure and fast payment transactions.
 
- [Website Paygent](https://www.paygent.co.jp/)
- [Admin Sandbox Paygent](https://sandbox.paygent.co.jp/n/o/login.html)
## System Requirements

A registered and activated Paygent account.

A programming environment that supports HTTP Requests (Python, Java, PHP, etc.).

SSL certificate if using secure transactions.

## Integration Guide

To integrate payment with Paygent, follow these steps:

### Step 1: Register a Paygent Account

- Visit Paygent's official website and register a business account.

- Complete the verification steps required by Paygent.

- Obtain the Merchant ID and API Key for integration.

### Step 2: Setup and Configuration

- Download the SDK or use Paygent's API as per the official documentation.

- Configure your system with the Merchant ID and API Key.

- Test the connection with Paygent's server using sample requests.

### Step 3: Send Payment Request

- Prepare the transaction data according to Paygent's required format.

- Send the payment request via Paygent's API.

- Receive the response from Paygent and handle the transaction result.

### Step 4: Verification and Validation
- Verify the order details and payment status.

- Log transaction details for future support and troubleshooting.

- Ensure the system is functioning correctly before deploying to production.
## Sample API Payment Request

Below is an example of a payment request using Paygent's API:
```json
{
"merchant_id": "123456",
"order_id": "ORD001",
"amount": 10000,
"currency": "JPY",
"card_number": "4111111111111111",
"card_expiry": "1225",
"cvv": "123"
}
```


## Important Notes

- Ensure testing in a sandbox environment before integrating into the live system.

- Encrypt payment data to secure customer information.

- Check for possible errors and handle them appropriately.

## Description

[![Watch the video](https://www.youtube.com/shorts/BYO-8dUqwxk)](https://www.youtube.com/shorts/BYO-8dUqwxk)
