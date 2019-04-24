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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.jcip.annotations.Immutable;

@Entity
@Immutable
@Table(name="concept_relationship")
@Inheritance(strategy=InheritanceType.JOINED)
public class ConceptRelationship extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name="concept_id_1")
	private Concept concept1;
	
	@ManyToOne
	@JoinColumn(name="concept_id_2")
	private Concept concept2;
	
	@Column(name="relationship_id")
	private String relationshipId;
	
	@Column(name="valid_start_date")
	private Date validStartDate;
	
	@Column(name="valid_end_date")
	private Date validEndDate;
	
	@Column(name="invalid_reason")
	private String invalidReason;

	@Override
	public Long getIdAsLong() {
		// TODO Auto-generated method stub
		return null;
	}

}
