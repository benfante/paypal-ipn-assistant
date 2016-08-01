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

import com.benfante.paypal.ipnassistant.IpnData;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
public class IpnDataTest {

    private final Map<String, String> ipnParameters;
    private IpnData instance;
    
    public IpnDataTest() {
        ipnParameters = TestUtils.buildExampleIpnMessage();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new IpnData(ipnParameters);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getParameters method, of class IpnData.
     */
    @Test
    public void testGetParameters() {
        Map<String, String> result = instance.getParameters();
        assertNotNull(result);
    }

    @Test
    public void testParametersOrder() {
        Map<String, String> parameters = instance.getParameters();
        Iterator<String> i1 = parameters.keySet().iterator();
        Iterator<String> i2 = ipnParameters.keySet().iterator();
        while(i1.hasNext()) {
            String s1 = i1.next();
            String s2 = i2.next();
            assertEquals(s2, s1);
        }
        assertTrue(!i2.hasNext());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testDataAreReadonly() {
        Map<String, String> parameters = instance.getParameters();
        parameters.put("something", "anything");
    }
    
    /**
     * Test of getReceiverEmail method, of class IpnData.
     */
    @org.junit.Test
    public void testGetReceiverEmail() {
        String expResult = "gm_1231902686_biz@paypal.com";
        String result = instance.getReceiverEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPaymentStatus method, of class IpnData.
     */
    @org.junit.Test
    public void testGetPaymentStatus() {
        String expResult = "Completed";
        String result = instance.getPaymentStatus();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransactionId method, of class IpnData.
     */
    @org.junit.Test
    public void testGetTransactionId() {
        String expResult = "61E67681CH3238416";
        String result = instance.getTransactionId();
        assertEquals(expResult, result);
    }
    
}
