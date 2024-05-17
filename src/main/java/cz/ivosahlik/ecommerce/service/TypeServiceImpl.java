package cz.ivosahlik.ecommerce.service;

import cz.ivosahlik.ecommerce.entity.Type;
import cz.ivosahlik.ecommerce.model.TypeResponse;
import cz.ivosahlik.ecommerce.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class TypeServiceImpl implements TypeService {
    private final TypeRepository typeRepository;

    @Override
    public List<TypeResponse> getAllTypes() {
        log.info("Fetching All Types!!!");
        return typeRepository.findAll().stream()
                .map(this::convertToTypeResponse)
                .toList();
    }

    private TypeResponse convertToTypeResponse(Type type) {
        return TypeResponse.builder()
                .id(type.getId())
                .name(type.getName())
                .build();
    }
}
