package com.ibuttimer.springecom.entity;

import java.util.List;

public interface IEntity {

    public Long getId();

    /**
     * Check object equality
     * @param o         object to check
     * @param excludes  list of fields to exclude
     * @return
     */
    boolean equals(Object o, List<Enum<?>> excludes);

    /**
     * Get object field value
     * @param field     field to get
     * @return
     */
    Object get(Enum<?> field);
}
