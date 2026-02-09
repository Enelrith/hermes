package io.github.enelrith.hermes.product.controller;

import io.github.enelrith.hermes.product.dto.AddManufacturerRequest;
import io.github.enelrith.hermes.product.dto.ManufacturerDto;
import io.github.enelrith.hermes.product.service.ManufacturerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/manufacturers")
@RequiredArgsConstructor
@Validated
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @PostMapping
    public ResponseEntity<ManufacturerDto> addManufacturer(@Valid @RequestBody AddManufacturerRequest request) {
        var manufacturer = manufacturerService.addManufacturer(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(manufacturer.id())
                .toUri();
        return ResponseEntity.created(location).body(manufacturer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerDto> getManufacturerById(@PathVariable @Positive Long id) {
        var manufacturer = manufacturerService.getManufacturerById(id);

        return ResponseEntity.ok(manufacturer);
    }

    @GetMapping
    public ResponseEntity<Set<ManufacturerDto>> getAllManufacturers() {
        var manufacturers = manufacturerService.getAllManufacturers();

        return ResponseEntity.ok(manufacturers);
    }
}
