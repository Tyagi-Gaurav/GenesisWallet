package com.gw.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gw.common.annotations.GenerateBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize
@GenerateBuilder
@Document
public record ExternalUser2(String userName,
                            String externalSystem) { }
