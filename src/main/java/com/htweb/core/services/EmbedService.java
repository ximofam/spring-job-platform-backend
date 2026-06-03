package com.htweb.core.services;

import com.htweb.core.helpers.dtos.VectorDto;

import java.util.List;

public interface EmbedService {
    VectorDto getEmbeddingCached(String text);

    float[] getEmbedding(String text);

    float[][] getEmbeddings(List<String> texts);
}
