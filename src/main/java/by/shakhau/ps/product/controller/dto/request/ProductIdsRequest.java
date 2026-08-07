package by.shakhau.ps.product.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductIdsRequest {

    private List<UUID> ids;
}
