package com.example.fundservice.application.port.in;

import com.example.fundservice.domain.model.Fund;
import java.util.List;

/**
 * A page of funds together with the total count.
 *
 * @param total total number of matching funds
 * @param items the funds in this page
 */
public record PagedFunds(long total, List<Fund> items) {
}
