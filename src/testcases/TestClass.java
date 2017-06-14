package testcases;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.notification.Failure;
import org.junit.runner.*;

import java.util.List;

public class TestClass {
	public static void main(String[] args) {
		Result rc = new Result();
		rc = org.junit.runner.JUnitCore.runClasses(TestClass.class);
		System.out.println(rc.getRunCount()+" tests were executed, "+rc.getFailureCount()+" of them with failures.");
		if(!rc.wasSuccessful()) {
			List<Failure> fList = rc.getFailures();
			for(Failure f: fList) {
				System.out.println(f.getTestHeader());
				System.out.println(f.getMessage());
			}
		}
	}
}