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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Some utils for tests.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class TestUtils {

    public static Map<String, String> buildExampleIpnMessage() {
        final Map<String, String> ipnParameters = new LinkedHashMap<>();
        // data from the PayPal example at https://developer.paypal.com/docs/classic/ipn/integration-guide/IPNIntro/
        ipnParameters.put("payment_status", "Completed");
        ipnParameters.put("receiver_email", "gm_1231902686_biz@paypal.com");
        ipnParameters.put("receiver_id", "S8XGHLYDW9T3S");
        ipnParameters.put("residence_country", "US");
        ipnParameters.put("test_ipn", "1");
        ipnParameters.put("transaction_subject", "");
        ipnParameters.put("txn_id", "61E67681CH3238416");
        ipnParameters.put("txn_type", "express_checkout");
        ipnParameters.put("payer_email", "gm_1231902590_per@paypal.com");
        ipnParameters.put("payer_id", "LPLWNMTBWMFAY");
        ipnParameters.put("payer_status", "verified");
        ipnParameters.put("first_name", "Test");
        ipnParameters.put("last_name", "User");
        ipnParameters.put("address_city", "San Jose");
        ipnParameters.put("address_country", "United States");
        ipnParameters.put("address_state", "CA");
        ipnParameters.put("address_status", "confirmed");
        ipnParameters.put("address_country_code", "US");
        ipnParameters.put("address_name", "Test User");
        ipnParameters.put("address_street", "1 Main St");
        ipnParameters.put("address_zip", "95131");
        ipnParameters.put("custom", "Your custom field");
        ipnParameters.put("handling_amount", "0.00");
        ipnParameters.put("item_name", "");
        ipnParameters.put("item_number", "");
        ipnParameters.put("mc_currency", "USD");
        ipnParameters.put("mc_fee", "0.88");
        ipnParameters.put("mc_gross", "19.95");
        ipnParameters.put("payment_date", "20:12:59 Jan 13, 2009 PST");
        ipnParameters.put("payment_fee", "0.88");
        ipnParameters.put("payment_gross", "19.95");
        ipnParameters.put("payment_status", "Completed");
        ipnParameters.put("payment_type", "instant");
        ipnParameters.put("protection_eligibility", "Eligible");
        ipnParameters.put("quantity", "1");
        ipnParameters.put("shipping", "0.00");
        ipnParameters.put("tax", "0.00");
        ipnParameters.put("notify_version", "2.6");
        ipnParameters.put("charset", "windows-1252");
        ipnParameters.put("verify_sign", "AtkOfCXbDm2hu0ZELryHFjY-Vb7PAUvS6nMXgysbElEn9v-1XcmSoGtf");
        return ipnParameters;
    }

}
