package io.github.enelrith.hermes.order.service;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import io.github.enelrith.hermes.cart.exception.CartDoesNotExistException;
import io.github.enelrith.hermes.common.enums.OrderStatus;
import io.github.enelrith.hermes.order.entity.Order;
import io.github.enelrith.hermes.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StripeService {
    private final OrderRepository orderRepository;

    @Value("${spring.application.base-url}")
    private String baseUrl;

    @Value("${stripe.webhook-secret-key}")
    private String webhookSecretKey;

    public Session createStripeSession(Order order) throws StripeException {
        var builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(baseUrl + "/order-success?orderNumber=" + order.getNumber())
                .setCancelUrl(baseUrl + "/order-fail?orderNumber=" + order.getNumber())
                .putMetadata("order_id", String.valueOf(order.getId()))
                .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()
                                .putMetadata("order_id", String.valueOf(order.getId()))
                                .build()
                );

        order.getOrderItems().forEach(orderItem -> {
            var lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(Long.valueOf(orderItem.getQuantity()))
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("eur")
                                    .setUnitAmountDecimal(orderItem.getUnitProductGrossPrice()
                                            .multiply(BigDecimal.valueOf(100)))
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(orderItem.getProduct().getName())
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();
            builder.addLineItem(lineItem);
        });

        var shippingLineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmountDecimal(new BigDecimal(300))
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Shipping Fee")
                                                .build()
                                )
                                .build()
                )
                .build();
        builder.addLineItem(shippingLineItem);

        return Session.create(builder.build());
    }

    @Transactional
    public void handleStripeWebhook(String payload, String signature) throws SignatureVerificationException {
        try {
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
            var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);

            switch(event.getType()) {
                case "payment_intent.succeeded" -> {
                    var paymentIntent = (PaymentIntent) stripeObject;

                    if (paymentIntent != null) {
                        var orderId = paymentIntent.getMetadata().get("order_id");
                        var order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow(() -> new CartDoesNotExistException("This order does not exist")); //TODO: Change to actual order exception
                        order.setStatus(OrderStatus.PAID);
                        orderRepository.save(order);
                    }
                }
                case "payment_intent.canceled" -> {
                    var paymentIntent = (PaymentIntent) stripeObject;

                    if (paymentIntent != null) {
                        var orderId = paymentIntent.getMetadata().get("order_id");
                        var order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow(() -> new CartDoesNotExistException("This order does not exist")); //TODO: Change to actual order exception
                        order.setStatus(OrderStatus.FAILED);
                        orderRepository.save(order);
                    }
                }
            }
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
