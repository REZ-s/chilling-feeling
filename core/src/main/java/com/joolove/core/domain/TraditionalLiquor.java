package com.joolove.core.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TL")
@Getter
@Setter
public class TraditionalLiquor extends Item {

}
