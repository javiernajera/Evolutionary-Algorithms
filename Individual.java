
public class Individual {
    // this string holds the variable assignments
    private String assignments = "";            
    
    // constructor with no argument makes 
    // individual obj with empty assignment String
    public Individual() {                      
        assignments = "";                      
    }
    
    // Constructor with assignments arg
    public Individual(String assignments) {
        this.assignments = assignments;
    }
    
    // Constructor that makes an Individual obj
    // with random assignments given # of variables
    public Individual(int numVariables) {       
                                                
        for (int i = 0; i < numVariables; ++i) {
            if (Math.random() < 0.5) {
                assignments += "1";
            }
            else {
                assignments += "0";
            }
        }
    }
    
    // Getter that returns an Individual's assignment string
    public String GetAssignments() {
        return assignments;
    }
    
    // Getter that returns the assingment of a specific variable/index.
    public char GetVariable(int index) {
        return assignments.charAt(index);
    }
    
    // Setter that sets the Individual's assignments string to the string given
    // in the parameter
    public void SetAssignments(String assignments) {
        this.assignments = assignments;
    }

}
