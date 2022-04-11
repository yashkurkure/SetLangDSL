import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable
import SetLangDSL.DSL.*

class ControlStructureTests extends AnyFlatSpec with Matchers {
  behavior of "Interfaces"

  // Function to compare values
  def compareValues(actualValue: Any, expectedValue: Any): Boolean = if actualValue == expectedValue then true else false


  /**
   * Test 1
   *
   * Declaring IF ELSE control structure
   *
   * Testing the true block
   *
   * */
  it should "create a simple if else control structure and run true block" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      g.AssignVariable("set1").Insert(1,2)

      g.AssignVariable("x").toValue(1)

      g.Conditional(
        g.Variable("x"), // Expression
        t=>{
          // if the expression is true, then delete set member 1
          t.Variable("set1").Delete(1)
      },
        f=>{

          //else if the epression is false, then delete set member 2
          f.Variable("set1").Delete(2)
        })

    }// End of global scope

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("set1").getValue.asInstanceOf[mutable.Set[Any]]
    // Test the actual value
    val expectedValue = mutable.Set[Any](2)
    compareValues(actualValue, expectedValue) shouldBe true

  }


  /**
   * Test 2
   *
   * Declaring IF ELSE control structure
   *
   * Testing the false block
   * */
  it should "create a simple if else control structure and run false block" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      g.AssignVariable("set1").Insert(1,2)

      g.AssignVariable("x").toValue(0)

      g.Conditional(
        g.Variable("x"), // Expression
        t=>{
          // if the expression is true, then delete set member 1
          t.Variable("set1").Delete(1)
        },
        f=>{

          //else if the epression is false, then delete set member 2
          f.Variable("set1").Delete(2)
        })

    }// End of global scope

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("set1").getValue.asInstanceOf[mutable.Set[Any]]
    // Test the actual value
    val expectedValue = mutable.Set[Any](1)
    compareValues(actualValue, expectedValue) shouldBe true

  }

}
