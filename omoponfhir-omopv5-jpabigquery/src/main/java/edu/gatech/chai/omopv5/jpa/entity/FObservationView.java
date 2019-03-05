/*******************************************************************************
 * Copyright (c) 2019 Georgia Tech Research Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *******************************************************************************/
package edu.gatech.chai.omopv5.jpa.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.jcip.annotations.Immutable;

@Entity
@Immutable
@Table(name="f_observation_view")
public class FObservationView extends BaseEntity {
	@Id
	@Column(name="observation_id")
	@Access(AccessType.PROPERTY)
	private Long id;

	@ManyToOne(cascade={CascadeType.MERGE})
	@JoinColumn(name = "person_id", nullable = false)
	private FPerson fPerson;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "observation_concept_id", nullable = false)
	private Concept observationConcept;

	@Column(name = "observation_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "observation_time")
	// @Temporal(TemporalType.TIME)
	private String time;

	@Column(name = "value_as_string")
	private String valueAsString;

	@Column(name = "value_as_number")
	private Double valueAsNumber;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "value_as_concept_id")
	private Concept valueAsConcept;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "observation_type_concept_id", nullable = false)
	private Concept typeConcept;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "provider_id")
	private Provider provider;

	@ManyToOne(cascade = { CascadeType.MERGE }, fetch=FetchType.LAZY)
	@JoinColumn(name = "visit_occurrence_id")
	private VisitOccurrence visitOccurrence;

	@Column(name = "source_value")
	private String sourceValue;

	@ManyToOne(cascade = { CascadeType.MERGE }, fetch=FetchType.LAZY)
	@JoinColumn(name = "source_concept_id")
	private Concept sourceConcept;

	@ManyToOne(cascade = { CascadeType.MERGE }, fetch=FetchType.LAZY)
	@JoinColumn(name = "qualifier_concept_id")
	private Concept qualifierConcept;

	@Column(name = "qualifier_source_value")
	private String qualifierSourceValue;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "unit_concept_id")
	private Concept unitConcept;

	@Column(name = "unit_source_value")
	private String unitSourceValue;

	@Column(name = "range_low")
	private BigDecimal rangeLow;

	@Column(name = "range_high")
	private BigDecimal rangeHigh;
	
	@Column(name = "value_source_value")
	private String valueSourceValue;

	@ManyToOne(cascade = { CascadeType.MERGE }, fetch=FetchType.LAZY)
	@JoinColumn(name = "observation_operator_concept_id")
	private Concept operatorConcept;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public FPerson getFPerson() {
		return fPerson;
	}

	public void setFPerson(FPerson person) {
		this.fPerson = person;
	}

	public Concept getObservationConcept() {
		return observationConcept;
	}

	public void setObservationConcept(Concept observationConcept) {
		this.observationConcept = observationConcept;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getValueAsString() {
		return valueAsString;
	}

	public void setValueAsString(String valueAsString) {
		this.valueAsString = valueAsString;
	}

	public Double getValueAsNumber() {
		return valueAsNumber;
	}

	public void setValueAsNumber(Double valueAsNumber) {
		this.valueAsNumber = valueAsNumber;
	}

	public Concept getValueAsConcept() {
		return valueAsConcept;
	}

	public void setValueAsConcept(Concept valueAsConcept) {
		this.valueAsConcept = valueAsConcept;
	}

	public Concept getTypeConcept() {
		return typeConcept;
	}

	public void setTypeConcept(Concept typeConcept) {
		this.typeConcept = typeConcept;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public VisitOccurrence getVisitOccurrence() {
		return visitOccurrence;
	}

	public void setVisitOccurrence(VisitOccurrence visitOccurrence) {
		this.visitOccurrence = visitOccurrence;
	}

	public Concept getQualifierConcept () {
		return qualifierConcept;
	}
	
	public void setQualifierConcept (Concept qualifierConcept) {
		this.qualifierConcept = qualifierConcept;
	}
	
	public String getQualifierSourceValue () {
		return qualifierSourceValue;
	}
	
	public void setQualifierSourceValue (String qualifierSourceValue) {
		this.qualifierSourceValue = qualifierSourceValue;
	}
	
	public String getSourceValue() {
		return sourceValue;
	}

	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}

	public Concept getSourceConcept() {
		return sourceConcept;
	}
	
	public void setSourceConcept(Concept sourceConcept) {
		this.sourceConcept = sourceConcept;
	}
	
	public Concept getUnitConcept() {
		return unitConcept;
	}

	public void setUnitConcept(Concept unitConcept) {
		this.unitConcept = unitConcept;
	}

	public String getUnitSourceValue() {
		return unitSourceValue;
	}

	public void setUnitSourceValue(String unitSourceValue) {
		this.unitSourceValue = unitSourceValue;
	}
	
	public BigDecimal getRangeLow() {
		return rangeLow;
	}

	public void setRangeLow(BigDecimal rangeLow) {
		this.rangeLow = rangeLow;
	}

	public BigDecimal getRangeHigh() {
		return rangeHigh;
	}

	public void setRangeHigh(BigDecimal rangeHigh) {
		this.rangeHigh = rangeHigh;
	}
	
	public String getValueSourceValue() {
		return valueSourceValue;
	}
	
	public void setValueSourceValue(String valueSourceValue) {
		this.valueSourceValue = valueSourceValue;
	}

	public Concept getOperatorConcept() {
		return operatorConcept;
	}

	public void setOperatorConcept(Concept operatorConcept) {
		this.operatorConcept = operatorConcept;
	}

	@Override
	public Long getIdAsLong() {
		return getId();
	}


}
