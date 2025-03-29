package com.example.worker.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String SOLICITACAO_QUEUE = "solicitacao.queue";
    public static final String RESPOSTA_QUEUE = "resposta.queue";
    public static final String EXCHANGE = "solicitacao.exchange";
    
    @Bean
    public Queue solicitacaoQueue() {
        return new Queue(SOLICITACAO_QUEUE);
    }
    
    @Bean
    public Queue respostaQueue() {
        return new Queue(RESPOSTA_QUEUE);
    }
    
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }
    
    @Bean
    public Binding bindingSolicitacao() {
        return BindingBuilder.bind(solicitacaoQueue())
                .to(exchange())
                .with("solicitacao.routingkey");
    }
    
    @Bean
    public Binding bindingResposta() {
        return BindingBuilder.bind(respostaQueue())
                .to(exchange())
                .with("resposta.routingkey");
    }
    
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}