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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Default implementation of an {@link IpnVerifier}.
 *
 * @author <a href="mailto:lucio.benfante@gmail.com">Lucio Benfante</a>
 */
public class DefaultIpnVerifier implements IpnVerifier {
    private final IpnConfiguration ipnConfiguration;

    public DefaultIpnVerifier(IpnConfiguration ipnConfiguration) {
        this.ipnConfiguration = ipnConfiguration;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String verify(IpnData ipnData) {
        String message = buildNotifyValidateMessage(ipnData);
        PrintWriter pw = null;
        HttpsURLConnection uc;
        try {
            URL u = new URL(ipnConfiguration.getPaypalIpnUrl());
            uc = (HttpsURLConnection) u.openConnection();
            uc.setDoOutput(true);
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            uc.setRequestProperty("Host", "www.paypal.com");
            pw = new PrintWriter(uc.getOutputStream());
            pw.println(message);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
        String response = "INVALID";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()))) {
            response = in.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return response;
    }
    
    /**
     * Build the message for validating the origina IPN message.
     *
     * @param ipnData The data of the PayPal IPN message.
     * @return The Message to post to the PayPal IPN.
     */
    protected String buildNotifyValidateMessage(IpnData ipnData) {
        StringBuilder sb = new StringBuilder("cmd=_notify-validate&");
        sb.append(ipnData.getConcatenatedParameters());
        return sb.toString();
    }
    
}
