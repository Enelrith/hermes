package io.github.enelrith.hermes.order.controller;

import com.stripe.exception.SignatureVerificationException;
import io.github.enelrith.hermes.order.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe-webhook")
@RequiredArgsConstructor
public class StripeController {
    private final StripeService stripeService;

    @PostMapping
    public ResponseEntity<Void> handleWebhook(@RequestHeader("Stripe-Signature") String signature, @RequestBody String payload) {
        try {
            stripeService.handleStripeWebhook(payload, signature);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }
}
