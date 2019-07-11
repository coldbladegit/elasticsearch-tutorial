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
public class Programmer implements Serializable {

    private static final long serialVersionUID = 6877127867093294019L;

    private String companyName;
    private String name;
    private int age;
    private int level;
    private int salary;
}
