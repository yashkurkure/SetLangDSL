import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import collection.mutable.Set
import SetLangDSL.SetLang.construct.*
import SetLangDSL.SetLang.setOperation.*

class TestGlobalLevel extends AnyFlatSpec with Matchers{
  behavior of "SetLang in a global SetLangDSL.scope"
  //This suite does not test for 2 or more levels deep nested scopes

  def compareValues(actualValue: Any, expectedValue: Any): Boolean = if actualValue == expectedValue then true else false


  it should "assign a set to a variable (test the binding mechanism)" in {
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

  it should "return true if a value exists in a set, else return false. [Using Value()]" in{

    //first assign the variable x to a set
    Assign(Variable("x"), Insert("a", "b", "c", "d")).eval()

    //then check if the the following values exist in the set "x"
    val result1 = Check("x", Value("a")).eval().getValue()// should be true
    val result2 = Check("x", Value("s")).eval().getValue()// should be false

    if(result1 == true && result2 == false) then true else false shouldBe true
  }

  it should "return true if a value referenced by a variable exits in a set, else false [Using Variable()]" in {

    //first assign the variable x to a set
    Assign(Variable("x"), Insert("a", "b", "c", "d")).eval()
    Assign(Variable("y"), Value("a")).eval()
    Assign(Variable("z"), Value("x")).eval()

    //then check if the the following values exist in the set "x"
    val result1 = Check("x", Variable("y")).eval().getValue()// should be true
    val result2 = Check("x", Variable("z")).eval().getValue()// should be false

    if(result1 == true && result2 == false) then true else false shouldBe true
  }

  it should "add a value to a set from inside a scope" in {

    // x is in the global scope
    Assign(Variable("x"), Insert("a", "b", "c", "d")).eval()

    // declare a scope with the addition to set operation
    Scope("myScope", Assign(Variable("x"), Add(Value("e")))).eval()

    //execute the scope
    Scope("myScope").eval()

    //check if the set was updated with the additional value "e"
    Variable("x").eval().getValue() shouldBe Set("a","b","c","d","e")


  }

}
