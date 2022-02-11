import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import collection.mutable.Set
import SetLangDSL.SetLang.construct.*
import SetLangDSL.SetLang.setOperation.*

/**
 * This test suite tests the set operations of setLang
 * */
class TestSetOperations extends AnyFlatSpec with Matchers{
  behavior of "Set operations in setLang"

  def compareValues(actualValue: Any, expectedValue: Any): Boolean = if actualValue == expectedValue then true else false

  it should "return as set" in{
    //Insert can handle 1 to 22 arguments, of which all will be inserted into a set
    val actualValue = Insert(1,2,3,4,5,6,7,8,9).eval().getValue()
    val expectedValue = Set(1,2,3,4,5,6,7,8,9)

    //compare the values
    compareValues(actualValue, expectedValue) shouldBe true
  }

  it should "Delete a value from a set. [Case: Value is present in set]" in{

    //Create a set and bind it to a name
    Assign(Variable("testSet"), Insert(1,2,3,4,5,6,7,8,9)).eval()
    
    //Delete the value 1 from the set
    Assign(Variable("testSet"), Delete(Value(1))).eval()

    val actualValue = Variable("testSet").eval().getValue()
    val expectedValue = Set(2,3,4,5,6,7,8,9)

    compareValues(actualValue, expectedValue) shouldBe true
  }

  it should "Delete a value from a set. [Case: Value is not present in set]" in{

  }

  it should "return the union of two sets" in{
    //first let us create a set and assign it to a variable
    val set1 = Insert(1,2,3,4,5).eval() //no need to call getValue(), since Union accepts (Value(Set), Value(Set))
    val set2 = Insert(6,7,8,9,10).eval()

    val actualValue = Union(set1, set2).eval().getValue()
    val expectedValue = Set(1,2,3,4,5,6,7,8,9,10)

    //compare the values
    compareValues(actualValue, expectedValue) shouldBe true
  }

  it should "return the intersection of two sets" in{

  }


  it should "return the difference of two sets" in{

  }

  it should "return the symmetric difference of two sets" in{

  }

  it should "return the cartesian product of two sets" in{

  }

}
