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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data of an IPN Message.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class IpnData {

    private final Map<String, String> params;

    public IpnData(Map<String, String> params) {
        this.params = Collections.unmodifiableMap(params);
    }
    
    public static final IpnData buildFromHttpRequestParams(Map<String, String[]> httpParams) {
        Map<String, String> result = new LinkedHashMap<>();
        httpParams.entrySet().stream().forEach((entry) -> {
            result.put(entry.getKey(), entry.getValue()[0]);
        });
        return new IpnData(result);
    }
    
    /**
     * Returns an unmodifiable map of the IPN parameters.
     *
     * @return An unmodifiable map of IPN parameters.
     */
    public Map<String, String> getParameters() {
        return params;
    }

    public String getReceiverEmail() {
        return params.get("receiver_email");
    }

    public String getPaymentStatus() {
        return params.get("payment_status");
    }

    public String getTransactionId() {
        return params.get("txn_id");
    }

    public String getParameter(String name) {
        return params.get(name);
    }

    public String getConcatenatedParameters() {
        String encoding = params.get("charset");
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                if (first) {
                    first = false;
                } else {
                    sb.append('&');
                }
                sb.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), encoding));
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }
        return sb.toString();
    }
}
