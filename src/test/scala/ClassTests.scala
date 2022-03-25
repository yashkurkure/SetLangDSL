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
        c.AssignVariable(Private, "privateNumber").toValue(1)
        c.AssignVariable(Protected, "protectedNumber").toValue(2)
        c.AssignVariable(Public, "publicNumber1").toValue(3)
        c.AssignVariable("publicNumber2").toValue(4)

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
      g.AssignVariable("myObject").toNewObjectOf("numbers")

      //Executing a method of a class object
      // result1 should be assigned to Value(4)
      //TODO: Change method invoking process
      //g.AssignVariable("result1").Method("getTheNumber", g.Variable("myObject")).withParameters(Value(2)).Execute
      g.AssignVariable("result").toValue(g.Variable("myObject").getMethod("getTheNumber").withParameters(2).Execute)

      // Test
      g.Variable("result").getValue shouldBe 4
    }

  }

  //test 2
  it should "Create a private class member" in {
    
    val globalScope = Scope{g=>

      // Create a class
      g.ClassDef("A", {c=>

        // Create a private member
        c.AssignVariable(Private, "x").toValue(1)

      })

      // Create an object of the class
      g.AssignVariable("A_obj").toNewObjectOf("A")

      // Accessing a private/protected or not bound name will return null
      g.Variable("A_obj").getField("x") shouldBe null

    }

  }

  //test 3
  it should "Create a protected class member" in {

  }

  //test 4
  it should "Create a class with a parameterized constructor" in {

    val globalScope = Scope{g=>

      g.ClassDef("A", {c=>
        c.Constructor(Parameters("param1"), c=>{})
      })

      g.AssignVariable("A_obj").toNewObjectOf("A").withParameters(1)

      g.Variable("A_obj").getField("param1").getValue shouldBe 1

    }


  }

  //test 5
  it should "Create a class with a parameterizes constructor (multiple parameters version)" in {

    val globalScope = Scope{g=>

      g.ClassDef("A", {c=>
        c.Constructor(Parameters("param1", "param2", "param3"), c=>{})
      })

      g.AssignVariable("A_obj").toNewObjectOf("A").withParameters(1,2,3)

      g.Variable("A_obj").getField("param1").getValue shouldBe 1
      g.Variable("A_obj").getField("param2").getValue shouldBe 2
      g.Variable("A_obj").getField("param3").getValue shouldBe 3

    }
  }

  //test 6
  it should "Access a private field using a public get method" in {

    val globalScope = Scope{g=>

      // Create a class
      g.ClassDef("A", {c=>

        // Create a private member
        c.AssignVariable(Private, "x").toValue(1)

        c.Method(Public, "getX", Parameters(), {m=>
          m.Variable("x") //or c.Variable("x")
        })

      })

      g.AssignVariable("A_obj").toNewObjectOf("A")
      g.Variable("A_obj").getMethod("getX").Execute.getValue shouldBe 1


    }

  }

  //test 6
  it should "Access a private field using a public set method (set method should NOT change the value)" in {

    val globalScope = Scope{g=>

      // Create a class
      g.ClassDef("A", {c=>

        // Create a private member
        c.AssignVariable(Private, "x").toValue(1)

        c.Method(Public, "getX", Parameters(), {m=>
          m.Variable("x") //or c.Variable("x")
        })

        c.Method(Public, "setX", Parameters("new_x"), {m=>
          m.AssignVariable("x").toValue(m.Variable("new_x"))
          Value(null)
        })

      })

      g.AssignVariable("A_obj").toNewObjectOf("A")
      g.Variable("A_obj").getMethod("setX").withParameters(2).Execute

      // The value of the class should not change as the fields are non mutable
      g.Variable("A_obj").getMethod("getX").Execute.getValue shouldBe 1
      g.Variable("A_obj").getMethod("getX").Execute.getValue should not be 2

    }

  }

  //test
  it should "Create a private method and call it inside another method" in {

    Scope{g=>
      g.ClassDef("A", {c=>

        c.Method(Private, "privateMethod", Parameters(), {m=>
          Value(2)
        })

        c.Method(Public, "publicMethod", Parameters(), {m=>
          c.getMethod("privateMethod").Execute
        })

      })

      g.AssignVariable("A_obj").toNewObjectOf("A")

      //test
      g.Variable("A_obj").getMethod("publicMethod").Execute.getValue shouldBe 2
    }

  }

}