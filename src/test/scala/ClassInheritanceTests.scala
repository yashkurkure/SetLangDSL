import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable
import SetLangDSL.DSL.*

class ClassInheritanceTests extends AnyFlatSpec with Matchers {
  behavior of "Class inheritance in SetLangDSL"

  // Function to compare values
  def compareValues(actualValue: Any, expectedValue: Any): Boolean = if actualValue == expectedValue then true else false


  // test 1
  it should "Create a simple inheritance relation and test access to fields" in {

    Scope{g=>
      g.ClassDef("Parent", {c=>

        c.AssignVariable(Public, "x").toValue(1)
        c.AssignVariable(Protected, "y").toValue(2)
        c.AssignVariable(Private, "z").toValue(3)

      })

      g.ClassDef("Child", Extends("Parent"), {c=>

        c.Method(Public,"getX",Parameters(),{m=>

          // Accessing a public member of the parent should
          //  be allowed inside the scope of the child class
          m.Variable("x")
        })

        c.Method(Public,"getY",Parameters(),{m=>

          // Accessing a protected member of the parent should
          //  be allowed inside the scope of the child class
          m.Variable("y")
        })

        c.Method(Public, "getZ", Parameters(), {m=>

          // Accessing private member of the parent should
          //  not be allowed inside the scope of the child class
          m.Variable("z")
        })

      })

      // Create an instance of the child class
      g.AssignVariable("obj").toNewObjectOf("Child")

      // Test: Access public field in parent using child's instance
      g.Variable("obj").getField("x").getValue shouldBe 1

      // Test: Access protected field in parent using child's instance
      // this should not be allowed outside the scope of the class
      g.Variable("obj").getField("y") shouldBe null

      // Test: Access private field in parent using child's instance (should not allowed)
      // this should not be inside the child's scope or even outside
      g.Variable("obj").getField("z") shouldBe null

      // Test: Access a public member of the parent inside a method of the child's class
      // this should be allowed
      g.Variable("obj").getMethod("getX").Execute.getValue shouldBe 1

      // Test: Access a protected member of the parent inside a method of the child's class
      // this should be allowed
      g.Variable("obj").getMethod("getY").Execute.getValue shouldBe 2

      // Test: Access private member of the parent inside a method of the child's class
      // this should NOT be allowed 
      g.Variable("obj").getMethod("getZ").Execute shouldBe null


    }

  }

}
