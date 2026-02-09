package io.github.enelrith.hermes.product.service;

import io.github.enelrith.hermes.product.dto.AddManufacturerRequest;
import io.github.enelrith.hermes.product.dto.ManufacturerDto;
import io.github.enelrith.hermes.product.exception.ManufacturerAlreadyExistsException;
import io.github.enelrith.hermes.product.exception.ManufacturerDoesNotExistException;
import io.github.enelrith.hermes.product.mapper.ManufacturerMapper;
import io.github.enelrith.hermes.product.repository.ManufacturerRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerMapper manufacturerMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public ManufacturerDto addManufacturer(AddManufacturerRequest request) {
        if (manufacturerRepository.existsByName(request.name())) throw new ManufacturerAlreadyExistsException();

        var manufacturer = manufacturerMapper.toEntity(request);
        manufacturerRepository.save(manufacturer);

        return manufacturerMapper.toManufacturerDto(manufacturer);
    }

    public ManufacturerDto getManufacturerById(Long id) {
        var manufacturer = manufacturerRepository.findById(id).orElseThrow(ManufacturerDoesNotExistException::new);

        return manufacturerMapper.toManufacturerDto(manufacturer);
    }

    public Set<ManufacturerDto> getAllManufacturers() {
        var manufacturers = manufacturerRepository.findAll();

        return manufacturers.stream().map(manufacturerMapper::toManufacturerDto).collect(Collectors.toSet());
    }
}
