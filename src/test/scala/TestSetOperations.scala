import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import SetLangDSL.SetLang.construct.*
import SetLangDSL.SetLang.setOperation.*
import scala.collection.mutable

/*
 *  Copyright (c) 2022. Yash Kurkure. All rights reserved.
 */
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
    //Create a set and bind it to a name
    Assign(Variable("testSet"), Insert(1,2,3,4,5,6,7,8,9)).eval()

    //Delete the value 11 from the set
    Assign(Variable("testSet"), Delete(Value(11))).eval()

    val actualValue = Variable("testSet").eval().getValue()
    val expectedValue = Set(1,2,3,4,5,6,7,8,9)

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
    //first let us create a set and assign it to a variable
    val set1 = Insert(1,2,3,4,5,11).eval() //no need to call getValue(), since Union accepts (Value(Set), Value(Set))
    val set2 = Insert(6,7,8,9,10,11).eval()

    val actualValue = Intersection(set1, set2).eval().getValue()
    val expectedValue = Set(11)

    //compare the values
    compareValues(actualValue, expectedValue) shouldBe true

  }


  it should "return the difference of two sets" in{

    //first let us create a set and assign it to a variable
    val set1 = Insert(1,2,3,4,5).eval() //no need to call getValue(), since Union accepts (Value(Set), Value(Set))
    val set2 = Insert(4,5).eval()

    val actualValue = Difference(set1, set2).eval().getValue()
    val expectedValue = Set(1,2,3)

    //compare the values
    compareValues(actualValue, expectedValue) shouldBe true

  }

  it should "return the symmetric difference of two sets" in{

    //first let us create a set and assign it to a variable
    val set1 = Insert(1,2,3,4,5).eval() //no need to call getValue(), since Union accepts (Value(Set), Value(Set))
    val set2 = Insert(2,4,6).eval()

    val actualValue = SymmetricDifference(set1, set2).eval().getValue()
    val expectedValue = Set(1,3,5,6)

    //compare the values
    compareValues(actualValue, expectedValue) shouldBe true

  }

  it should "return the cartesian product of two sets" in{
    //first let us create a set and assign it to a variable
    val set1 = Insert(1,2,3).eval() //no need to call getValue(), since Union accepts (Value(Set), Value(Set))
    val set2 = Insert(2,4,6).eval()

    val actualValue = CartesianProduct(set1, set2).eval().getValue()
    val expectedValue = Set((1,2),(1,4),(1,6),(2,2),(2,4),(2,6),(3,2),(3,4),(3,6))

    //compare the values
    compareValues(actualValue, expectedValue) shouldBe true
  }

}
