package com.htweb.dtos;

import lombok.Data;

public class CategoryDto {
    private CategoryDto() {
    }

    @Data
    public static class DetailResponse {
        private Integer id;
        private String name;
        private String description;
    }

    @Data
    public static class ItemResponse {
        private Integer id;
        private String name;
    }

    @Data
    public static class CreateRequest {
        private String name;
        private String description;
    }
}
