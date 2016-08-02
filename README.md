# PayPal IPN Assistant [![Build Status](https://travis-ci.org/benfante/paypal-ipn-assistant.svg?branch=master)](https://travis-ci.org/benfante/paypal-ipn-assistant)
A small Java Library for managing the IPN messages coming from PayPal.

## How to add to your Maven project

Add the following dependency:

```xml
<dependency>
  <groupId>com.benfante.paypal</groupId>
  <artifactId>ipnAssistant</artifactId>
  <version>1.0</version>
</dependency>
```

## Using it with Spring MVC

(but with few or no modifications you can use it with the DI and Web frameworks you like, there are no dependencies with Spring and Spring MVC)

### Define your buying process

Write an implementation of a `PaymentProcessor` for managing the operations to execute after a payment.

For example:

```java
package com.example;

import com.example.MyItemService;
import com.benfante.paypal.ipnassistant.IpnData;
import com.benfante.paypal.ipnassistant.PaymentProcessor;
import javax.annotation.Resource;

public class MyPaymentProcessor implements PaymentProcessor {

  @Resource
  public MyItemService myItemService;

  @Override
  public void process(IpnData ipnData) {
    int numCartItems = Integer.parseInt(ipnData.getParameter("num_cart_items"));
    if (numCartItems > 0) {
      for (int i = 1; i <= numCartItems; i++) {
        // see https://developer.paypal.com/docs/classic/ipn/integration-guide/IPNandPDTVariables/
        // for the parameter you can receive.
        String item = ipnData.getParameter("item_number"+i);
        String option = ipnData.getParameter("option_selection1_"+i);
        int quantity = Integer.parseInt(ipnData.getParameter("quantity"+i));
        myItemService.buy(item, option, quantity);
      }
    }
  }
}
```

### Configure your application context

```xml
<bean id="paypalIpnConfiguration" class="com.benfante.paypal.ipnassistant.IpnConfiguration">
  <property name="paypalIpnUrl" value="https://www.paypal.com/cgi-bin/webscr"/>
  <property name="receiverEmail" value="paypal-merchant@example.com"/>
</bean>

<!-- Usually you'll need a "real" not only "in-memory" message binder...see below. -->
<bean id="paypalIpnMessageInfoBinder" class="com.benfante.paypal.ipnassistant.InMemoryIpnMessageBinder"/>

<bean id="paypalIpnVerifier" class="com.benfante.paypal.ipnassistant.DefaultIpnVerifier">
  <constructor-arg ref="paypalIpnConfiguration"/>
</bean>

<!-- Your payment processor. -->
<bean id="paypalPaymentProcessor" class="com.example.MyPaymentProcessor"/>
    
<bean id="paypalIpnAssistant" class="com.benfante.paypal.ipnassistant.DefaultIpnAssistant">
  <constructor-arg ref="paypalIpnConfiguration"/>
  <constructor-arg ref="paypalIpnMessageInfoBinder"/>
  <constructor-arg ref="paypalIpnVerifier"/>
  <constructor-arg ref="paypalPaymentProcessor"/>
</bean>
```

## Add your IPN controller

Add the controller that will receive the IPN message (It must accept a POST on a public not-protected URL):

```java
package com.example;

import com.benfante.paypal.ipnassistant.IpnAssistant;
import com.benfante.paypal.ipnassistant.IpnData;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping({"/ipn"})
public class ShoppingIpnController {

    @Resource
    public IpnAssistant ipnAssistant;

    @RequestMapping(value = "/receive", method = {RequestMethod.POST})
    public ResponseEntity<?> receiveIpn(HttpServletRequest req) throws IOException {
        IpnData ipnData = IpnData.buildFromHttpRequestParams(req.getParameterMap());
        ipnAssistant.process(ipnData);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

}
```

Of course in place of a Spring MVC controller you could have a Servlet or anything else your Web framework likes for receiving HTTP requests.

## Use a real not-only-in-memory message binder

The message binder is used for checking the duplicated ipn message that PayPal could send. So in a real system is not a great idea to use the provided (mostly as an example) `com.benfante.paypal.ipnassistant.InMemoryIpnMessageBinder`. You'll need to define a message binder storing the messages into the persistent storage system you prefer for your application.

For example:

```java
package com.example;

import com.example.IpnMessageInfoDao;
import com.example.IpnMessageInfo;
import com.benfante.paypal.ipnassistant.IpnData;
import com.benfante.paypal.ipnassistant.IpnMessageBinder;
import javax.annotation.Resource;

public class IpnMessageInfoBinder implements IpnMessageBinder {

  @Resource
  private IpnMessageInfoDao ipnMessageInfoDao;

  @Override
  public boolean messageAlreadyExists(String transactionId, String paymentStatus) {
    IpnMessageInfo message
      = ipnMessageInfoDao.findByTransactionIdAndPaymentStatus(transactionId, paymentStatus);
    return (message != null);
  }

  @Override
  public void add(IpnData ipnData) {
    IpnMessageInfo message = new IpnMessageInfo();
    message.setTransactionId(ipnData.getTransactionId());
    message.setPaymentStatus(ipnData.getPaymentStatus());
    message.setCustomData(ipnData.getParameter("custom"));
    message.setMcCurrency(ipnData.getParameter("mc_currency"));
    message.setMcGross(ipnData.getParameter("mc_gross"));
    message.setPayerEmail(ipnData.getParameter("payer_email"));
    message.setMessageParameters(ipnData.getConcatenatedParameters());
    ipnMessageInfoDao.store(message);
  }

}
```
