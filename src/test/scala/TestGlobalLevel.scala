import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import collection.mutable.Set
import SetLangDSL.SetLang.construct.*
import SetLangDSL.SetLang.setOperation.*

class TestGlobalLevel extends AnyFlatSpec with Matchers{
  behavior of "SetLang in a global SetLangDSL.scope"
  //This suite does not test for 2 or more levels deep nested scopes
  
  
  it should "create a set with values a,b,c,d" in {
    //first assign the variable x to a set
    Assign(Variable("x"), Insert("a", "b", "c", "d")).eval()
    //then get the value of the set by using the Variable name "x"
    //Variable() returns a Value(), so call getValue() to get the actual set instance
    val actualValue = Variable("x").eval().getValue()
    println()
    val expectedValue = Set("a","b","c","d")
    //now test the value
    val result = if actualValue == expectedValue then true else false
    result shouldBe true
  }

}
