package edu.gatech.chai.gtfhir2.provider;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import edu.gatech.chai.gtfhir2.mapping.OmopMedicationRequest;
import edu.gatech.chai.gtfhir2.utilities.ThrowFHIRExceptions;
import edu.gatech.chai.omopv5.jpa.service.ParameterWrapper;

public class MedicationRequestResourceProvider implements IResourceProvider {

	private WebApplicationContext myAppCtx;
	private String myDbType;
	private OmopMedicationRequest myMapper;
	private int preferredPageSize = 30;

	public MedicationRequestResourceProvider() {
		myAppCtx = ContextLoaderListener.getCurrentWebApplicationContext();
		myDbType = myAppCtx.getServletContext().getInitParameter("backendDbType");
		if (myDbType.equalsIgnoreCase("omopv5") == true) {
			myMapper = new OmopMedicationRequest(myAppCtx);
		} else {
			myMapper = new OmopMedicationRequest(myAppCtx);
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
		return "MedicationRequest";
	}

    public OmopMedicationRequest getMyMapper() {
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

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return MedicationRequest.class;
	}

	/**
	 * The "@Create" annotation indicates that this method implements "create=type", which adds a 
	 * new instance of a resource to the server.
	 */
	@Create()
	public MethodOutcome createMedicationRequest(@ResourceParam MedicationRequest theMedicationRequest) {
		validateResource(theMedicationRequest);
		
		Long id=null;
		try {
			id = myMapper.toDbase(theMedicationRequest, null);
		} catch (FHIRException e) {
			e.printStackTrace();
		}
		
		if (id == null) {
			OperationOutcome outcome = new OperationOutcome();
			CodeableConcept detailCode = new CodeableConcept();
			detailCode.setText("Failed to create entity.");
			outcome.addIssue().setSeverity(IssueSeverity.FATAL).setDetails(detailCode);
			throw new UnprocessableEntityException(FhirContext.forDstu3(), outcome);
		}

		return new MethodOutcome(new IdDt(id));
	}

	@Delete()
	public void deleteMedicationRequest(@IdParam IdType theId) {
		if (myMapper.removeByFhirId(theId) <= 0) {
			throw new ResourceNotFoundException(theId);
		}
	}

	@Update()
	public MethodOutcome updateMedicationRequest(@IdParam IdType theId, @ResourceParam MedicationRequest theMedicationRequest) {
		validateResource(theMedicationRequest);
		
		Long fhirId=null;
		try {
			fhirId = myMapper.toDbase(theMedicationRequest, theId);
		} catch (FHIRException e) {
			e.printStackTrace();
		}

		if (fhirId == null) {
			throw new ResourceNotFoundException(theId);
		}

		return new MethodOutcome();
	}

	@Read()
	public MedicationRequest readMedicationRequest(@IdParam IdType theId) {
		MedicationRequest retval = (MedicationRequest) myMapper.toFHIR(theId);
		if (retval == null) {
			throw new ResourceNotFoundException(theId);
		}
			
		return retval;
	}
	
	@Search()
	public IBundleProvider findMedicationRequetsbyCodes(
			@RequiredParam(name = MedicationRequest.SP_CODE) TokenOrListParam theCodes,
			@OptionalParam(name = MedicationRequest.SP_PATIENT) ReferenceParam thePatient,
			@OptionalParam(name = MedicationRequest.SP_SUBJECT) ReferenceParam theSubject
			) {
		List<ParameterWrapper> paramList = new ArrayList<ParameterWrapper> ();

		ThrowFHIRExceptions.unprocessableEntityException("search by code not implemented yet");
		
		MyBundleProvider myBundleProvider = new MyBundleProvider(paramList);
		return myBundleProvider;
		
	}

	@Search()
	public IBundleProvider findMedicationRequestsByParams(
			@OptionalParam(name = MedicationRequest.SP_RES_ID) TokenParam theMedicationRequestId,
			@OptionalParam(name = MedicationRequest.SP_CODE) TokenParam theCode,
			@OptionalParam(name = MedicationRequest.SP_CONTEXT) ReferenceParam theContext,
			@OptionalParam(name = MedicationRequest.SP_AUTHOREDON) DateParam theDate,
			@OptionalParam(name = MedicationRequest.SP_PATIENT) ReferenceParam thePatient,
			@OptionalParam(name = MedicationRequest.SP_SUBJECT) ReferenceParam theSubject
			) {
		List<ParameterWrapper> paramList = new ArrayList<ParameterWrapper> ();
		
		if (theMedicationRequestId != null) {
			paramList.addAll(myMapper.mapParameter (MedicationRequest.SP_RES_ID, theMedicationRequestId, false));
		}
		
		if (theCode != null) {
			paramList.addAll(myMapper.mapParameter (MedicationRequest.SP_CODE, theCode, false));
		}

		if (theContext != null) {
			paramList.addAll(myMapper.mapParameter (MedicationRequest.SP_CONTEXT, theContext, false));
		}

		if (theDate != null) {
			paramList.addAll(myMapper.mapParameter (MedicationRequest.SP_AUTHOREDON, theDate, false));
		}

		if (thePatient != null || theSubject != null) {
			// We only support Patient for subject so we handle it here.
			if (thePatient != null)
				paramList.addAll(myMapper.mapParameter (MedicationRequest.SP_PATIENT, thePatient, false));
			else
				paramList.addAll(myMapper.mapParameter (MedicationRequest.SP_SUBJECT, theSubject, false));
		}

		// Now finalize the parameter map.
//		final Map<String, List<ParameterWrapper>> finalParamMap = paramMap;
//		final Long totalSize;
//		if (paramMap.size() == 0) {
//			totalSize = myMapper.getSize();
//		} else {
//			totalSize = myMapper.getSize(finalParamMap);
//		}

		MyBundleProvider myBundleProvider = new MyBundleProvider(paramList);
		myBundleProvider.setTotalSize(getTotalSize(paramList));
		
		return myBundleProvider;
		
//		return new IBundleProvider() {
//
//			@Override
//			public IPrimitiveType<Date> getPublished() {
//				return searchTime;
//			}
//
//			@Override
//			public List<IBaseResource> getResources(int fromIndex, int toIndex) {
//				List<IBaseResource> retv = new ArrayList<IBaseResource>();
//
//				// _Include
//				List<String> includes = new ArrayList<String>();
//
//				if (finalParamMap.size() == 0) {
//					myMapper.searchWithoutParams(fromIndex, toIndex, retv, includes);
//				} else {
//					myMapper.searchWithParams(fromIndex, toIndex, finalParamMap, retv, includes);
//				}
//
//				return retv;
//			}
//
//			@Override
//			public String getUuid() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public Integer preferredPageSize() {
//				return preferredPageSize;
//			}
//
//			@Override
//			public Integer size() {
//				return totalSize.intValue();
//			}
//			
//		};
	}
	
//	private void mapParameter(Map<String, List<ParameterWrapper>> paramMap, String FHIRparam, Object value, boolean or) {
//		List<ParameterWrapper> paramList = myMapper.mapParameter(FHIRparam, value, or);
//		if (paramList != null) {
//			paramMap.put(FHIRparam, paramList);
//		}
//	}

	private void validateResource(MedicationRequest theMedication) {
		// TODO: implement validation method
	}
	
	class MyBundleProvider extends OmopFhirBundleProvider implements IBundleProvider {
		public MyBundleProvider(List<ParameterWrapper> paramList) {
			super(paramList);
			setPreferredPageSize (preferredPageSize);
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
