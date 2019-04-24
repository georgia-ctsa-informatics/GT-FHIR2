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
package edu.gatech.chai.omopv5.model.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import net.jcip.annotations.Immutable;

@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "concept_id")
public class Concept extends BaseEntity {
	public static String tableName = "concept";

	@JsonProperty("concept_id")
	private Long id;
	
	@JsonProperty("concept_name")
	private String name;
	
	@JsonProperty("domain_id")
	private String domain;

	@JsonProperty("concept_class_id")
	private String conceptClass;
	
	@JsonProperty("standard_concept")
	private Character standardConcept;
	
	@JsonProperty("vocabulary_id")
	private Vocabulary vocabulary;
	
	@JsonProperty("concept_code")
	private String conceptCode;
	
	@JsonProperty("valid_start_date")
	@JsonFormat(
		      shape = JsonFormat.Shape.STRING,
		      pattern = "yyyy-MM-ddThh:mm:ss+zz:zz")
	private Date validStartDate;
	
	@JsonProperty("valid_end_date")
	@JsonFormat(
		      shape = JsonFormat.Shape.STRING,
		      pattern = "yyyy-MM-ddThh:mm:ss+zz:zz")
	private Date validEndDate;
	
	@JsonProperty("invalid_reason")
	private String invalidReason;

	public Concept() {
		super();
	}
	
	public Concept(Long id) {
		super();
		this.id = id;
	}
	
	public Concept(Long id, String name){
		super();
		this.id = id;
		this.name = name;
	}

	public Concept(Long id, String name, String domain, String conceptClass, Character standardConcept,
			Vocabulary vocabulary, String conceptCode, Date validStartDate,
			Date validEndDate, String invalidReason) {
		super();
		this.id = id;
		this.name = name;
		this.domain = domain;
		this.conceptClass = conceptClass;
		this.standardConcept = standardConcept;
		this.vocabulary = vocabulary;
		this.conceptCode = conceptCode;
		this.validStartDate = validStartDate;
		this.validEndDate = validEndDate;
		this.invalidReason = invalidReason;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getConceptClass() {
		return conceptClass;
	}

	public void setConceptClass(String conceptClass) {
		this.conceptClass = conceptClass;
	}

	public Character getStandardConcept() {
		return standardConcept;
	}
	
	public void setStandardConcept(Character standardConcept) {
		this.standardConcept = standardConcept;
	}
	
	public Vocabulary getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(Vocabulary vocabulary) {
		this.vocabulary = vocabulary;
	}

	public String getConceptCode() {
		return conceptCode;
	}

	public void setConceptCode(String conceptCode) {
		this.conceptCode = conceptCode;
	}

	public Date getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

	public Date getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	public String getInvalidReason() {
		return invalidReason;
	}

	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}

	@Override
	public String toString() {
		//Since this is an omop v.4 based model, all the information below is expected to be not null.
		return this.getId() + ", "
				+ this.getName() + ", "
				+ this.getDomain() + ", "
				+ this.getConceptClass() + ", "
				+ this.getStandardConcept() + ", "
				+ this.getVocabulary() + ", "
				+ this.getConceptCode() + ", "
				+ this.getValidStartDate() + ", "
				+ this.getValidEndDate();
	}

	@Override
	public Long getIdAsLong() {
		return getId();
	}

}
