import java.util.Comparator;

public class NameOrdering implements Comparator<PatientOrder> {

	@Override
	public int compare(PatientOrder o1, PatientOrder o2) {
		// TODO Auto-generated method stub
			  
		return o1.getFirstname().compareTo(o2.getFirstname());  

	}

}
