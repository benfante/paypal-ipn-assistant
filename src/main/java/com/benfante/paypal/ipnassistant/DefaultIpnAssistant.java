/**
 * Copyright 2016 Lucio Benfante <lucio.benfante@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.benfante.paypal.ipnassistant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of an IpnAssistant.
 *
 * First it validates the IPN message.
 *
 * If validation is successful, it checks the proper recipient, and checks that
 * it's not a duplicated message. If all checks are ok, it processes the message.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class DefaultIpnAssistant implements IpnAssistant {

    private final IpnConfiguration ipnConfiguration;
    private final IpnMessageBinder ipnMessageBinder;
    private final IpnVerifier ipnVerifier;
    private final PaymentProcessor paymentProcessor;
    private static final Logger logger = LoggerFactory.getLogger(DefaultIpnAssistant.class);

    public DefaultIpnAssistant(IpnConfiguration ipnConfiguration,
            IpnMessageBinder ipnMessageBinder,
            IpnVerifier ipnVerifier,
            PaymentProcessor paymentProcessor) {
        this.ipnConfiguration = ipnConfiguration;
        this.ipnMessageBinder = ipnMessageBinder;
        this.ipnVerifier = ipnVerifier;
        this.paymentProcessor = paymentProcessor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(IpnData ipnData) {
        logger.info("Started processing IPN message for transaction {}", ipnData.getTransactionId());
        if (messageIsVerified(ipnData)) {
            if (paymentIsCompleted(ipnData)) {
                if (!alreadyProcessed(ipnData)) {
                    ipnMessageBinder.add(ipnData);
                    if (receiverIsCorrect(ipnData)) {
                        paymentProcessor.process(ipnData);
                        logger.info("Completed processing IPN message for transaction {}", ipnData.getTransactionId());
                    } else {
                        logger.warn("Stopped processing transaction {} as the receiver {} is not correct.", ipnData.getTransactionId(), ipnData.getReceiverEmail());
                    }
                } else {
                    logger.warn("Stopped processing transaction {} as already processed (duplicated IPN message).", ipnData.getTransactionId());
                }
            } else {
                logger.warn("Stopped processing transaction {} as the payment is not completed (it's {}).", ipnData.getTransactionId(), ipnData.getPaymentStatus());
            }
        } else {
            logger.warn("Stopped processing transaction {} as the message is not verified.", ipnData.getTransactionId());
        }
    }

    /**
     * Post to Paypal IPN Url for verifying the original message.
     *
     * @param ipnData The data of the IPN Message
     * @return true if the message is verified.
     */
    protected boolean messageIsVerified(IpnData ipnData) {
        return "VERIFIED".equals(verifyMessage(ipnData));
    }

    /**
     * Post to Paypal IPN Url for verifying the original message.
     *
     * @param ipnData The data of the IPN Message
     * @return The PayPal response ("VERIFIED " or "INVALID").
     */
    protected String verifyMessage(IpnData ipnData) {
        return ipnVerifier.verify(ipnData);
    }

    /**
     * Checks if the receiver is the expected one.
     *
     * @param ipnData The data of the IPN message.
     * @return true if the receiver matches.
     */
    protected boolean receiverIsCorrect(IpnData ipnData) {
        return ipnData.getReceiverEmail().equals(ipnConfiguration.getReceiverEmail());
    }

    /**
     * Check if a message has already been processed.
     *
     * @param ipnData The data of the IPN message.
     * @return true if the message has already been processed.
     */
    protected boolean alreadyProcessed(IpnData ipnData) {
        return this.ipnMessageBinder.messageAlreadyExists(ipnData.getTransactionId(), ipnData.getPaymentStatus());
    }

    /**
     * Checks if the payment is completed.
     *
     * @param ipnData The data of the IPN message.
     * @return true if the payment is completed.
     */
    protected boolean paymentIsCompleted(IpnData ipnData) {
        return "Completed".equals(ipnData.getPaymentStatus());
    }

}
