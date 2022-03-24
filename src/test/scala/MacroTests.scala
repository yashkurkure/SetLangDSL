import SetLangDSL.DSL.Scope
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable

class MacroTests extends AnyFlatSpec with Matchers{
  behavior of "Macros across Scopes"

  // Function to compare values
  def compareValues(actualValue: Any, expectedValue: Any): Boolean = if actualValue == expectedValue then true else false

  /**
   * Test 1
   *
   * Declaring Macros
   *
   * */
  it should "Declare a macro" in{

    val globalScope = Scope{g=>

      g.Assign.Variable("mySet").Insert(1,2,3,4,5)

      g.Assign.Variable("DeleteMacro").toMacro{m=>
        m.Delete(1)
        m.Insert(100,200)
      }

      g.ExecuteMacro("DeleteMacro", g.Variable("mySet"))

      g.Variable("mySet")

    }

    // To access the Variable "result" from outside the defined scope
    val actualValue = globalScope.Variable("mySet").getValue.asInstanceOf[mutable.Set[Any]]
    // Test the actual value
    val expectedValue = mutable.Set[Any](2,3,4,5,100,200)
    compareValues(actualValue, expectedValue) shouldBe true

  }




}