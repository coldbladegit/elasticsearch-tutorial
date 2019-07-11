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
public class Company implements Serializable {

    private static final long serialVersionUID = 7146470170957709891L;

    private String name;
    private String address;
    private Boss owner;
}
