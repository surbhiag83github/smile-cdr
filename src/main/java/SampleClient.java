import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

public class SampleClient {

    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));

        // Search for Patient resources
        Bundle response = client
                .search()
                .forResource("Patient")       
                //.where(Patient.FAMILY.matches().value("SMITH"))
                .returnBundle(Bundle.class)
                .execute();
        

     // Entries in the return bundle will use the given type 
       ArrayList<PatientOrder> patientlist=new ArrayList<PatientOrder>(); 
       patientlist= getPatientList(response);
       Collections.sort(patientlist,new NameOrdering());
        Iterator<PatientOrder> itr =patientlist.iterator();
        while (itr.hasNext()) {
        	PatientOrder pt = (PatientOrder)itr.next();
        	System.out.println("Patient First Name is "+ pt.getFirstname()+
        			" Patient Last Name is "+pt.getLastname()+" Patient BirthDate is "+pt.getBirthdate());
        }
       
     //END SNIPPET: customTypeClientSearch


    }
    
    public static ArrayList<PatientOrder> getPatientList(Bundle response){
    	 Patient pat =new Patient();
    	 ArrayList<PatientOrder> patientlist=new ArrayList<PatientOrder>(); 
    	 Date birthdate=null;
    	 String firstName="";
    	 String lastName="";
    	 for(int i=0;i<response.getEntry().size()-1;i++) {
        	 pat=(Patient) response.getEntry().get(i).getResource();
        	 
        	 if(pat.getBirthDate()!=null) {
            // System.out.println("Patient Birth Date is :"+pat.getBirthDate());
             birthdate=pat.getBirthDate();
        	 }
             
        	 if(pat.getName()!=null && pat.getName().size()>0) {
        		 firstName= pat.getName().get(0).getGivenAsSingleString();
        		 lastName= pat.getName().get(0).getFamily();      		 
        		        		             
             }
        	 patientlist.add(new PatientOrder(firstName,lastName, birthdate));
		} 
    	 return patientlist;
    }
   

}
