import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable
import SetLangDSL.DSL._

class TestSetOperations extends AnyFlatSpec with Matchers {
  behavior of "Set operations in setLang"

  // Function to compare values
  def compareValues(actualValue: Any, expectedValue: Any): Boolean = if actualValue == expectedValue then true else false

  //test 1
  it should "Insert 1 element in a set" in {

    //first define a scope
    val globalScope = Scope{g=>

      //Assign a variable "set1" to a Set("a")
      g.Assign.Variable("set1").Insert("a")
    }

    val expectedValue = mutable.Set[Any]("a")

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("set1").getValue.asInstanceOf[mutable.Set[Any]]

    compareValues(actualValue, expectedValue) shouldBe true
  }

  //test 2
  it should "Insert 2 or more elements in a set" in {

    //first define a scope
    val globalScope = Scope{g=>

      //Assign a variable "set1" to a Set("a")
      g.Assign.Variable("set1").Insert("a","b","c")
    }

    val expectedValue = mutable.Set[Any]("a","b","c")

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("set1").getValue.asInstanceOf[mutable.Set[Any]]

    compareValues(actualValue, expectedValue) shouldBe true
  }

  //test 3
  it should "Union of 2 sets" in {

    //first define a scope
    val globalScope = Scope{g=>

      //Assign a variable "set1" to a Set("a")
      g.Assign.Variable("set1").Insert("a","b","c")
      g.Assign.Variable("set2").Insert("b","c","d","e","f")
      g.Assign.Variable("result").Union(g.Variable("set1"), g.Variable("set2"))
    }

    val expectedValue = mutable.Set[Any]("a","b","c","d","e","f")

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("result").getValue.asInstanceOf[mutable.Set[Any]]

    compareValues(actualValue, expectedValue) shouldBe true
  }

  //test 4
  it should "Intersection of 2 sets" in {

    //first define a scope
    val globalScope = Scope{g=>

      //Assign a variable "set1" to a Set("a")
      g.Assign.Variable("set1").Insert("a","b","c")
      g.Assign.Variable("set2").Insert("b","c","d")
      g.Assign.Variable("result").Intersection(g.Variable("set1"), g.Variable("set2"))
    }

    val expectedValue = mutable.Set[Any]("b","c")

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("result").getValue.asInstanceOf[mutable.Set[Any]]

    compareValues(actualValue, expectedValue) shouldBe true
  }

  //test 5
  it should "Difference of 2 sets" in {

    //first define a scope
    val globalScope = Scope{g=>

      //Assign a variable "set1" to a Set("a")
      g.Assign.Variable("set1").Insert(1,2,3,4,5)
      g.Assign.Variable("set2").Insert(4,5)
        g.Assign.Variable("result").Difference(g.Variable("set1"), g.Variable("set2"))
    }

    val expectedValue = mutable.Set[Any](1,2,3)

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("result").getValue.asInstanceOf[mutable.Set[Any]]

    compareValues(actualValue, expectedValue) shouldBe true
  }


  //test 6
  it should "SymmetricDifference difference of 2 sets" in {

    //first define a scope
    val globalScope = Scope{g=>

      //Assign a variable "set1" to a Set("a")
      g.Assign.Variable("set1").Insert(1,2,3,4,5)
      g.Assign.Variable("set2").Insert(2,4,6)
      g.Assign.Variable("result").SymmetricDifference(g.Variable("set1"), g.Variable("set2"))
    }

    val expectedValue = mutable.Set[Any](1,3,5,6)

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("result").getValue.asInstanceOf[mutable.Set[Any]]

    compareValues(actualValue, expectedValue) shouldBe true
  }

  //test 7
  it should "CartesianProduct of 2 sets" in {

    //first define a scope
    val globalScope = Scope{g=>

      //Assign a variable "set1" to a Set("a")
      g.Assign.Variable("set1").Insert(1,2,3)
      g.Assign.Variable("set2").Insert(2,4,6)
      g.Assign.Variable("result").CartesianProduct(g.Variable("set1"), g.Variable("set2"))
    }

    val expectedValue = mutable.Set[Any]((1,2),(1,4),(1,6),(2,2),(2,4),(2,6),(3,2),(3,4),(3,6))

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("result").getValue.asInstanceOf[mutable.Set[Any]]

    compareValues(actualValue, expectedValue) shouldBe true
  }

  //test 8
  it should "Delete an element from a set" in {

    //first define a scope
    val globalScope = Scope{g=>

      //Assign a variable "set1" to a Set("a")
      g.Assign.Variable("set1").Insert(1,2,3,4,5,6)
      g.Variable("set1").Delete(2)
      g.Variable("set1").Delete(4)
      g.Variable("set1").Delete(6)
      g.Variable("set1").Delete(7) //this wont change the value as 7 is not in the set
    }

    val expectedValue = mutable.Set[Any](1,3,5)

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("set1").getValue.asInstanceOf[mutable.Set[Any]]

    compareValues(actualValue, expectedValue) shouldBe true
  }

  //test 9
  it should "Insert values represented by variables inside set" in {

    //first define a scope
    val globalScope = Scope{g=>
      g.Assign.Variable("x").toValue(3)
      g.Assign.Variable("set1").Insert(1,2,g.Variable("x"))
    }

    val expectedValue = mutable.Set[Any](1,2,3)

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("set1").getValue.asInstanceOf[mutable.Set[Any]]

    compareValues(actualValue, expectedValue) shouldBe true
  }

  //test 10
  it should "Delete a value represented by a variable from a set" in {

    //first define a scope
    val globalScope = Scope{g=>
      g.Assign.Variable("x").toValue(3)
      g.Assign.Variable("set1").Insert(1,2,3,4)
      g.Variable("set1").Delete(g.Variable("x"))
    }

    val expectedValue = mutable.Set[Any](1,2,4)

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("set1").getValue.asInstanceOf[mutable.Set[Any]]

    compareValues(actualValue, expectedValue) shouldBe true
  }

}
