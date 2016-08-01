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

import com.benfante.paypal.ipnassistant.PaymentProcessor;
import com.benfante.paypal.ipnassistant.IpnData;

/**
 * A payment processor for tests. Doing nothing.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class FakePaymentProcessor implements PaymentProcessor {

    @Override
    public void process(IpnData ipnData) {
    }
    
}
