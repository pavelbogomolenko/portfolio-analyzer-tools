package io.github.pavelbogomolenko.stockhistoricalprice;

public interface Buildable<A, B> {
    A build();

    B self();
}
