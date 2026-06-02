package com.htweb.core.services;

import java.util.List;

public interface EmbedService {
    float[] getEmbedding(String text);

    float[][] getEmbeddings(List<String> texts);
}
