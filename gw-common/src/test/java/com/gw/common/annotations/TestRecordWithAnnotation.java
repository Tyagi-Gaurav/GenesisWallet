package com.gw.common.annotations;

@GenerateBuilder
public record TestRecordWithAnnotation(
        String abc,
        int def,
        float xyz) {}