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

import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class DefaultIpnAssistantTest {

    private final Map<String, String> ipnParameters;
    private IpnData ipnData;
    private final DefaultIpnAssistant instance;

    public DefaultIpnAssistantTest() {
        ipnParameters = TestUtils.buildExampleIpnMessage();

        IpnConfiguration ipnConfiguration = new IpnConfiguration();
        ipnConfiguration.setPaypalIpnUrl("https://www.sandbox.paypal.com/cgi-bin/webscr");
        ipnConfiguration.setReceiverEmail("gm_1231902686_biz@paypal.com");
        
        inMemoryIpnMessageBinder = new InMemoryIpnMessageBinder();

        instance = new DefaultIpnAssistant(ipnConfiguration, inMemoryIpnMessageBinder,
                new FakeIpnVerifier(),
                new FakePaymentProcessor());
    }
    private final InMemoryIpnMessageBinder inMemoryIpnMessageBinder;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        ipnData = new IpnData(ipnParameters);
        inMemoryIpnMessageBinder.clear();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of process method, of class DefaultIpnAssistant.
     */
    @Test
    public void testProcess() {
        instance.process(ipnData);
    }

    /**
     * Test of messageIsVerified method, of class DefaultIpnAssistant.
     */
    @Test
    public void testMessageIsVerified() {
        boolean expResult = true;
        boolean result = instance.messageIsVerified(ipnData);
        assertEquals(expResult, result);
    }

    /**
     * Test of verifyMessage method, of class DefaultIpnAssistant.
     */
    @Test
    public void testVerifyMessage() {
        String expResult = "VERIFIED";
        String result = instance.verifyMessage(ipnData);
        assertEquals(expResult, result);
    }

    /**
     * Test of receiverIsCorrect method, of class DefaultIpnAssistant.
     */
    @Test
    public void testReceiverIsCorrect() {
        boolean expResult = true;
        boolean result = instance.receiverIsCorrect(ipnData);
        assertEquals(expResult, result);
    }

    /**
     * Test of alreadyProcessed method, of class DefaultIpnAssistant.
     */
    @Test
    public void testNotAlreadyProcessed() {
        boolean expResult = false;
        boolean result = instance.alreadyProcessed(ipnData);
        assertEquals(expResult, result);
    }

    @Test
    public void testProcessingTwiceTheSameMessage() {
        instance.process(ipnData);
        instance.process(ipnData);
        boolean expResult = true;
        boolean result = instance.alreadyProcessed(ipnData);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of paymentIsCompleted method, of class DefaultIpnAssistant.
     */
    @Test
    public void testPaymentIsCompleted() {
        boolean expResult = true;
        boolean result = instance.paymentIsCompleted(ipnData);
        assertEquals(expResult, result);
    }

}
