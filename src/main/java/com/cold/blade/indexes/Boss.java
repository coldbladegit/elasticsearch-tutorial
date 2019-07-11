package com.cold.blade.indexes;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 */
@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class Boss implements Serializable {

    private static final long serialVersionUID = -3473141576018108498L;

    private String name;
    private double assets;
    private int age;
}
