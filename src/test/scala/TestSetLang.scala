import SetLangDSL.DSL.Scope
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable
class TestSetLang extends AnyFlatSpec with Matchers {
  
  behavior of "Assign, Scope, Macro"

  // Function to compare values
  def compareValues(actualValue: Any, expectedValue: Any): Boolean = if actualValue == expectedValue then true else false

  //test 1
  it should "Execute a macro on a variable" in {

    //first define a scope
    val globalScope = Scope { g =>
      g.Assign.Variable("mySet").Insert("a", "b", "c")
      g.Assign.Variable("myMacro").toMacro(v => {
        v.Delete("b")
      })
      g.ExecuteMacro("myMacro", g.Variable("mySet"))
    }

    val expectedValue = mutable.Set[Any]("a","c")

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("mySet").getValue.asInstanceOf[mutable.Set[Any]]

    compareValues(actualValue, expectedValue) shouldBe true
  }
  

}
