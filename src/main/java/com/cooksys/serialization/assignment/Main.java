package com.cooksys.serialization.assignment;

import com.cooksys.serialization.assignment.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    /**
     * Creates a {@link Student} object using the given studentContactFile.
     * The studentContactFile should be an XML file containing the marshaled form of a
     * {@link Contact} object.
     *
     * @param studentContactFile the XML file to use
     * @param jaxb the JAXB context to use
     * @return a {@link Student} object built using the {@link Contact} data in the given file
     */
    public static Student readStudent(File studentContactFile, JAXBContext jaxb) throws JAXBException{
    	
    			Unmarshaller unmarshall = jaxb.createUnmarshaller();
    			Student stu = new Student();
    			Contact info = (Contact)unmarshall.unmarshal( studentContactFile );
    			stu.setContact(info);
				return stu;
    }

    /**
     * Creates a list of {@link Student} objects using the given directory of student contact files.
     *
     * @param studentDirectory the directory of student contact files to use
     * @param jaxb the JAXB context to use
     * @return a list of {@link Student} objects built using the contact files in the given directory
     */
    public static List<Student> readStudents(File studentDirectory, JAXBContext jaxb) throws JAXBException{
    	
    	File[] f = studentDirectory.listFiles();
    	if(f == null)
    	{
    		 System.out.println("error");
    		 
    	}
    	ArrayList<Student> students = new ArrayList<Student>();
    	for(File emp : f)
    	{
    		students.add(readStudent(emp,jaxb));
    	}
    	return students; 
    }

    /**
     * Creates an {@link Instructor} object using the given instructorContactFile.
     * The instructorContactFile should be an XML file containing the marshaled form of a
     * {@link Contact} object.
     *
     * @param instructorContactFile the XML file to use
     * @param jaxb the JAXB context to use
     * @return an {@link Instructor} object built using the {@link Contact} data in the given file
     */
    public static Instructor readInstructor(File instructorContactFile, JAXBContext jaxb) throws JAXBException{
    	Unmarshaller unmarshall = jaxb.createUnmarshaller();
    	Instructor instr =  new Instructor();
    	Contact info =(Contact)unmarshall.unmarshal( instructorContactFile );
    	instr.setContact(info);
		return instr;
        //return null; // TODO
    }

    /**
     * Creates a {@link Session} object using the given rootDirectory. A {@link Session}
     * root directory is named after the location of the {@link Session}, and contains a directory named
     * after the start date of the {@link Session}. The start date directory in turn contains a directory named
     * `students`, which contains contact files for the students in the session. The start date directory
     * also contains an instructor contact file named `instructor.xml`.
     *
     * @param rootDirectory the root directory of the session data, named after the session location
     * @param jaxb the JAXB context to use
     * @return a {@link Session} object built from the data in the given directory
     */
    public static Session readSession(File rootDirectory, JAXBContext jaxb) throws JAXBException{

    	Session sess = new Session();
    	
    	File dateName = new File("input\\memphis\\08-08-2016");
    	File instr = new File("input\\memphis\\08-08-2016\\instructor.xml");
    	File studentdir = new File("input\\memphis\\08-08-2016\\students");
    	
    	sess.setInstructor(readInstructor(instr,jaxb));
    	sess.setStartDate(dateName.getName());
    	sess.setLocation(rootDirectory.getName());
    	sess.setStudents(readStudents(studentdir,jaxb));
    	
    	return sess;
    	
       // return null; // TODO
    }

    /**
     * Writes a given session to a given XML file
     *
     * @param session the session to write to the given file
     * @param sessionFile the file to which the session is to be written
     * @param jaxb the JAXB context to use
     */
    public static void writeSession(Session session, File sessionFile, JAXBContext jaxb) throws JAXBException {
    	Marshaller  marsh= jaxb.createMarshaller();
    	marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    	marsh.marshal(session,sessionFile);
    }

    /**
     * Main Method Execution Steps:
     * 1. Configure JAXB for the classes in the com.cooksys.serialization.assignment.model package
     * 2. Read a session object from the <project-root>/input/memphis/ directory using the methods defined above
     * 3. Write the session object to the <project-root>/output/session.xml file.
     *
     * JAXB Annotations and Configuration:
     * You will have to add JAXB annotations to the classes in the com.cooksys.serialization.assignment.model package
     *
     * Check the XML files in the <project-root>/input/ directory to determine how to configure the {@link Contact}
     *  JAXB annotations
     *
     * The {@link Session} object should marshal to look like the following:
     *      <session location="..." start-date="...">
     *           <instructor>
     *               <contact>...</contact>
     *           </instructor>
     *           <students>
     *               ...
     *               <student>
     *                   <contact>...</contact>
     *               </student>
     *               ...
     *           </students>
     *      </session>
     */
    public static void main(String[] args) {
    	try{
    		JAXBContext compress = JAXBContext.newInstance(Session.class);
        	writeSession(readSession(new File("input/memphis"),compress),new File("output/session.xml"),compress);
    	}
    	catch (JAXBException e)
    	{
    		e.printStackTrace();
    	}
    	
        // TODO
    }
}
