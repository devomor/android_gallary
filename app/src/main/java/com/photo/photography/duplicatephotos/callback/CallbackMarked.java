package com.photo.photography.duplicatephotos.callback;

import java.io.Serializable;

public interface CallbackMarked extends Serializable {
    void photosCleanedExact(int i);

    void photosCleanedSimilar(int i);

    void updateDuplicateFoundExact(int i);

    void updateDuplicateFoundSimilar(int i);

    void updateMarkedExact();

    void updateMarkedSimilar();
}
