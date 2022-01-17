package com.ibuttimer.springecom.dao;

import com.ibuttimer.springecom.entity.Subdivision;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "SubdivisionNameSubdiv", types = {Subdivision.class})
public interface SubdivisionNameSubdiv {

    @Value("#{target.name}")
    String getName();

    @Value("#{target.subdiv}")
    String getSubdiv();
}
