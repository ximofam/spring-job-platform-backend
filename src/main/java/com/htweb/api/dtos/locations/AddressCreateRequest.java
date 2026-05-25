package com.htweb.api.dtos.locations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressCreateRequest {
    @NotBlank(message = "Địa chỉ đường/số nhà không được để trống")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String streetAddress;

    @NotNull(message = "Thành phố/Tỉnh không được để trống")
    private Long cityId;

    @NotNull(message = "Quận/Huyện không được để trống")
    private Long districtId;
}