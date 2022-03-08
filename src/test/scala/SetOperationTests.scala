import SetLangDSL.DSL.Scope
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable

class SetOperationTests extends AnyFlatSpec with Matchers {
  behavior of "Set operations with Variables and Assign"

  // Function to compare values
  def compareValues(actualValue: Any, expectedValue: Any): Boolean = if actualValue == expectedValue then true else false

  /**
   * Test 1
   *
   * Insert 1 element into a set
   * - Also shows how to use Assign
   *
   * */
  it should "Insert 1 element in a set" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      //Assign a variable "set1" to a Set("a")
      g.Assign.Variable("set1").Insert("a")
    }

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("set1").getValue.asInstanceOf[mutable.Set[Any]]
    // Test the actual value
    val expectedValue = mutable.Set[Any]("a")
    compareValues(actualValue, expectedValue) shouldBe true
  }

  /**
   * Test 2
   *
   * Insert 2 or more elements into a set
   *
   * */
  it should "Insert 2 or more elements in a set" in {

    //Create a global scope for the DSL
    val globalScope = Scope{g=>

      //Assign a variable "set1" to a Set("a", "b", "c")
      g.Assign.Variable("set1").Insert("a","b","c")
    }

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("set1").getValue.asInstanceOf[mutable.Set[Any]]
    println(actualValue)
    // Test the actual value
    val expectedValue = mutable.Set[Any]("a","b","c")
    compareValues(actualValue, expectedValue) shouldBe true
  }

  /**
   * Test 3
   *
   * Union of 2 sets
   *
   * */
  it should "Union of 2 sets" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      // Assign a variable "set1" to a Set("a", "b", "c")
      g.Assign.Variable("set1").Insert("a","b","c")
      // Assign a variable "set2" to Set("b", "c", "d", "e", "f")
      g.Assign.Variable("set2").Insert("b","c","d","e","f")
      // Assign a variable "result" to the Union of "set1" and "set2"
      g.Assign.Variable("result").Union(g.Variable("set1"), g.Variable("set2"))
    }

    // To access the Variable "result" from outside the defined scope
    //val actualValue = globalScope.Variable("result").getValue.asInstanceOf[mutable.Set[Any]]
    print(globalScope.Variable("result").getValue)
    // Test the actual value
    val expectedValue = mutable.Set[Any]("a","b","c","d","e","f")
    //compareValues(actualValue, expectedValue) shouldBe true
  }

  /**
   * Test 4
   *
   * Intersection of 2 sets
   *
   * */
  it should "Intersection of 2 sets" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      // Assign a variable "set1" to a Set("a", "b", "c")
      g.Assign.Variable("set1").Insert("a","b","c")
      // Assign a variable "set1" to a Set("b", "c", "d")
      g.Assign.Variable("set2").Insert("b","c","d")
      // Assign a variable "result" to the Intersection of "set1" and "set2"
      g.Assign.Variable("result").Intersection(g.Variable("set1"), g.Variable("set2"))
    }

    // To access the Variable "result" from outside the defined scope
    val actualValue = globalScope.Variable("result").getValue.asInstanceOf[mutable.Set[Any]]
    // Test the actual value
    val expectedValue = mutable.Set[Any]("b","c")
    compareValues(actualValue, expectedValue) shouldBe true
  }

  /**
   * Test 5
   *
   * Difference of 2 sets
   *
   * */
  it should "Difference of 2 sets" in {

    val globalScope = Scope{g=>

      // Assign "set1" with "(1,2,3,4,5)"
      g.Assign.Variable("set1").Insert(1,2,3,4,5)
      // Assign "set2" with "(4,5)"
      g.Assign.Variable("set2").Insert(4,5)
      // Assign "result" to difference of "set1" and "set2"
      g.Assign.Variable("result").Difference(g.Variable("set1"), g.Variable("set2"))
    }

    // To access the Variable "result" from outside the defined scope
    val actualValue = globalScope.Variable("result").getValue.asInstanceOf[mutable.Set[Any]]
    val expectedValue = mutable.Set[Any](1,2,3)
    compareValues(actualValue, expectedValue) shouldBe true
  }


  /**
   * Test 6
   *
   * Symmetric Difference of 2 sets
   *
   * */
  it should "SymmetricDifference difference of 2 sets" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      // Assign "set1" to (1,2,3,4,5)
      g.Assign.Variable("set1").Insert(1,2,3,4,5)
      // Assign "set2" to (2,4,6)
      g.Assign.Variable("set2").Insert(2,4,6)
      // Assign "result" to the symmetric difference of "set1" and "set2"
      g.Assign.Variable("result").SymmetricDifference(g.Variable("set1"), g.Variable("set2"))
    }

    // To access the Variable "result" from outside the defined scope
    val actualValue = globalScope.Variable("result").getValue.asInstanceOf[mutable.Set[Any]]
    val expectedValue = mutable.Set[Any](1,3,5,6)
    compareValues(actualValue, expectedValue) shouldBe true
  }

  /**
   * Test 7
   *
   * Cartesian Product of 2 Sets
   *
   * */
  it should "CartesianProduct of 2 sets" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      // Assign "set1" to (1,2,3)
      g.Assign.Variable("set1").Insert(1,2,3)
      // Assign "set2" to (2,4,6)
      g.Assign.Variable("set2").Insert(2,4,6)
      // Assign "result" to the Cartesian Product of "set1" and "set2"
      g.Assign.Variable("result").CartesianProduct(g.Variable("set1"), g.Variable("set2"))
    }

    // To access the Variable "result" from outside the defined scope
    val actualValue = globalScope.Variable("result").getValue.asInstanceOf[mutable.Set[Any]]
    val expectedValue = mutable.Set[Any]((1,2),(1,4),(1,6),(2,2),(2,4),(2,6),(3,2),(3,4),(3,6))
    compareValues(actualValue, expectedValue) shouldBe true
  }

  /**
   * Test 8
   *
   * Delete element from a set
   *
   * */
  it should "Delete an element from a set" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      // Assign "set1" to (1,2,3,4,5,6)
      g.Assign.Variable("set1").Insert(1,2,3,4,5,6)
      // Delete 2 from "set1"
      g.Variable("set1").Delete(2)
      // Delete 4 from "set1"
      g.Variable("set1").Delete(4)
      // Delete 6 from "set1"
      g.Variable("set1").Delete(6)
      // Delete 7 from "set1", this wont change anything as 7 is not in the set
      g.Variable("set1").Delete(7)
    }

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("set1").getValue.asInstanceOf[mutable.Set[Any]]
    val expectedValue = mutable.Set[Any](1,3,5)
    compareValues(actualValue, expectedValue) shouldBe true
  }

  /**
   * Test 9
   *
   * Insert using values represented by defined variables
   *
   * */
  it should "Insert values represented by variables inside set" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      // Assign "x" to 3
      g.Assign.Variable("x").toValue(3)
      // Assign "set1" to a set with (1,2, x) where x will be resolved to its value
      g.Assign.Variable("set1").Insert(1,2,g.Variable("x"))
    }

    // To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("set1").getValue.asInstanceOf[mutable.Set[Any]]
    val expectedValue = mutable.Set[Any](1,2,3)
    compareValues(actualValue, expectedValue) shouldBe true
  }

  /**
   * Test 10
   *
   * Delete value from set, using a value of defined variables
   *
   * */
  it should "Delete a value represented by a variable from a set" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      // Assign "x" to 3
      g.Assign.Variable("x").toValue(3)
      // Assign "set1" to (1,2,3,4)
      g.Assign.Variable("set1").Insert(1,2,3,4)
      // Delete value of "x" from "set1"
      g.Variable("set1").Delete(g.Variable("x"))
    }

    // To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("set1").getValue.asInstanceOf[mutable.Set[Any]]
    val expectedValue = mutable.Set[Any](1,2,4)
    compareValues(actualValue, expectedValue) shouldBe true
  }

  /**
   * Test 11
   *
   * Test the Check operation on sets
   * Check would return Value(true) if a element is present in the set else Value(false)
   *
   * */
  it should "Check if a set contains a value or not" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      g.Assign.Variable("x").toValue(3)
      // Assign "set1" to (1,2,3,4)
      g.Assign.Variable("set1").Insert(1,2,3,4)

      // Test Check() operation
      g.Variable("set1").Check(2).getValue shouldBe true
      g.Variable("set1").Check(g.Variable("x")).getValue shouldBe true
      g.Variable("set1").Check(10).getValue shouldBe false
    }
  }

}
