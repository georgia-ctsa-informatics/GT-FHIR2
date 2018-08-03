package edu.gatech.chai.gtfhir2.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import edu.gatech.chai.gtfhir2.mapping.OmopMedication;
import edu.gatech.chai.omopv5.jpa.service.ParameterWrapper;

public class MedicationResourceProvider implements IResourceProvider {

	private WebApplicationContext myAppCtx;
	private String myDbType;
	private OmopMedication myMapper;
	private int preferredPageSize = 30;

	public MedicationResourceProvider() {
		myAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();
		myDbType = myAppCtx.getServletContext().getInitParameter("backendDbType");
		if (myDbType.equalsIgnoreCase("omopv5") == true) {
			myMapper = new OmopMedication(myAppCtx);
		} else {
			myMapper = new OmopMedication(myAppCtx);
		}

		String pageSizeStr = myAppCtx.getServletContext().getInitParameter("preferredPageSize");
		if (pageSizeStr != null && pageSizeStr.isEmpty() == false) {
			int pageSize = Integer.parseInt(pageSizeStr);
			if (pageSize > 0) {
				preferredPageSize = pageSize;
			}
		}
	}

	public static String getType() {
		return "Medication";
	}

	public OmopMedication getMyMapper() {
		return myMapper;
	}

	private Integer getTotalSize(List<ParameterWrapper> paramList) {
		final Long totalSize;
		if (paramList.size() == 0) {
			totalSize = getMyMapper().getSize();
		} else {
			totalSize = getMyMapper().getSize(paramList);
		}

		return totalSize.intValue();
	}

	@Delete()
	public void deleteMedication(@IdParam IdType theId) {
		throw new MethodNotAllowedException("Medication Delete is not Allowed.");
	}

	@Read()
	public Medication readMedication(@IdParam IdType theId) {
		Medication retval = (Medication) myMapper.toFHIR(theId);
		if (retval == null) {
			throw new ResourceNotFoundException(theId);
		}

		return retval;
	}

	@Search()
	public IBundleProvider findMedicationByParams(
			@OptionalParam(name = Medication.SP_RES_ID) TokenParam theMedicationId,
			@OptionalParam(name = Medication.SP_CODE) TokenParam theCode) {
		List<ParameterWrapper> paramList = new ArrayList<ParameterWrapper>();

		if (theMedicationId != null) {
			paramList.addAll(myMapper.mapParameter(Medication.SP_RES_ID, theMedicationId, false));
		}

		if (theCode != null) {
			paramList.addAll(myMapper.mapParameter(Medication.SP_CODE, theCode, false));
		}

		MyBundleProvider myBundleProvider = new MyBundleProvider(paramList);
		myBundleProvider.setTotalSize(getTotalSize(paramList));

		return myBundleProvider;
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Medication.class;
	}

	private void errorProcessing(String msg) {
		OperationOutcome outcome = new OperationOutcome();
		CodeableConcept detailCode = new CodeableConcept();
		detailCode.setText(msg);
		outcome.addIssue().setSeverity(IssueSeverity.FATAL).setDetails(detailCode);
		throw new UnprocessableEntityException(FhirContext.forDstu3(), outcome);
	}

	/**
	 * This method just provides simple business validation for resources we are
	 * storing.
	 * 
	 * @param theMedication
	 *            The medication to validate
	 */
	private void validateResource(Medication theMedication) {
		/*
		 * Our server will have a rule that patients must have a family name or
		 * we will reject them
		 */
		// if (thePatient.getNameFirstRep().getFamily().isEmpty()) {
		// OperationOutcome outcome = new OperationOutcome();
		// CodeableConcept detailCode = new CodeableConcept();
		// detailCode.setText("No family name provided, Patient resources must
		// have at least one family name.");
		// outcome.addIssue().setSeverity(IssueSeverity.FATAL).setDetails(detailCode);
		// throw new UnprocessableEntityException(FhirContext.forDstu3(),
		// outcome);
		// }
	}

	class MyBundleProvider extends OmopFhirBundleProvider implements IBundleProvider {
		public MyBundleProvider(List<ParameterWrapper> paramList) {
			super(paramList);
			setPreferredPageSize(preferredPageSize);
		}

		@Override
		public List<IBaseResource> getResources(int fromIndex, int toIndex) {
			List<IBaseResource> retv = new ArrayList<IBaseResource>();

			// _Include
			List<String> includes = new ArrayList<String>();

			if (paramList.size() == 0) {
				myMapper.searchWithoutParams(fromIndex, toIndex, retv, includes);
			} else {
				myMapper.searchWithParams(fromIndex, toIndex, paramList, retv, includes);
			}

			return retv;
		}

	}

}
