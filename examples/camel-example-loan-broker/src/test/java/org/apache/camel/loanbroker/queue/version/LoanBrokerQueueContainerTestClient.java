/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.loanbroker.queue.version;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.After;
import org.junit.Before;

public class LoanBrokerQueueContainerTestClient extends LoanBrokerQueueTest {
    
    @Before
    public void startServices() throws Exception {
        deleteDirectory("activemq-data");

        camelContext = new DefaultCamelContext();
       
        // Set up the ActiveMQ JMS Components
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        // Note we can explicitly name the component
        camelContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
       
        camelContext.addRoutes(new RouteBuilder() {
            // using the mock endpoint to check the result
            public void configure() throws Exception {
                from("jms:queue:loanReplyQueue").to("mock:endpoint");
            }
        });
       
        template = camelContext.createProducerTemplate();
        camelContext.start();
    }
    
    @After
    public void stopServices() throws Exception {
        if (camelContext != null) {
            camelContext.stop();
        }
    }

}
