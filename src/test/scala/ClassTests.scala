import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable
import SetLangDSL.DSL.*

class ClassTests extends AnyFlatSpec with Matchers {
  behavior of "Set operations in setLang"

  // Function to compare values
  def compareValues(actualValue: Any, expectedValue: Any): Boolean = if actualValue == expectedValue then true else false

  //test 1
  it should "Create a class called numbers and execute the parameterized method" in {

    //first define a scope
    val globalScope = Scope{g=>

      g.ClassDef("numbers", c=>{

        //Creating class variables
        c.Assign.Variable(Private, "privateNumber").toValue(1)
        c.Assign.Variable(Protected, "protectedNumber").toValue(2)
        c.Assign.Variable(Public, "publicNumber1").toValue(3)
        c.Assign.Variable("publicNumber2").toValue(4)

        //Class has default constructor

        //Creating class methods
        //getTheNumber returns:
        // classVariable publicNumber1 if parameter whichNumber == 1
        // classVariable publicNumber2 if parameter whichNumber == 2
        c.Method(Public, "getTheNumber", Parameters("whichNumber"), m=>{

          // The advantage of using an hierarchy of methods and classes for the DSL
          // The user is able to use the if then else construct of scala
          if m.Variable("whichNumber").equals(Value(1)) then
            c.Variable("publicNumber1")
          else
            c.Variable("publicNumber2")
        })

      })

      //Creating a class object
      g.Assign.Variable("myObject").toNewObjectOf("numbers")

      //Executing a method of a class object
      // result1 should be assigned to Value(4)
      g.Assign.Variable("result1").Method("getTheNumber", g.Variable("myObject")).withParameters(Value(2)).Execute
    }

    compareValues(globalScope.Variable("result1").getValue, 4) shouldBe true

  }

  //test 2
  it should "Create a private class member" in {
    
    val globalScope = Scope{g=>

      // Create a class
      g.ClassDef("A", {c=>

        // Create a private member
        c.Assign.Variable(Private, "x").toValue(1)

      })

      // Create an object of the class
      g.Assign.Variable("A_obj").toNewObjectOf("A")

      //On trying to access a private field you would get null
      try{
        g.Variable("A_obj").getField("x")
      }
      g.Variable("A_obj").getField("x")

      an [Exception] should be thrownBy g.Variable("A_obj").getField("x")

    }

  }

  //test 3
  it should "Create a protected class member" in {

  }

  //test 4
  it should "Create a class with a parameterized constructor" in {

  }

  //test 5
  it should "Create a private method" in {

  }

  //test 6
  it should "Create a protected method" in {

  }

  //test 7
  it should "Create a "

}