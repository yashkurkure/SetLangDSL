import SetLangDSL.DSL.Scope
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable

class ScopeTests extends AnyFlatSpec with Matchers{
  behavior of "Scopes and Scoping Rules"

  // Function to compare values
  def compareValues(actualValue: Any, expectedValue: Any): Boolean = if actualValue == expectedValue then true else false


  /**
   * Test 1
   *
   * Declaring Scopes
   *
   * */
  it should "test scope declaration" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      //Assign a variable "x" to a 1
      g.Assign.Variable("x").toValue(1)
    }// End of global scope

    //To access the Variable "set1" from outside the defined scope
    val actualValue = globalScope.Variable("x").getValue
    // Test the actual value
    val expectedValue = 1
    compareValues(actualValue, expectedValue) shouldBe true
  }


  /**
   * Test 2
   *
   * Declaring Variables in child scopes/ scope inside scope
   *
   * */
  it should "create a variable in outer scope and access it in inner scope" in {

    // Create a global scope for the DSL
    val globalScope = Scope{g=>

      //Assign a variable "x" to a 1 on global level
      g.Assign.Variable("x").toValue(1)
      g.Scope(s=>{

        // Test
        s.Variable("x").getValue shouldBe 1

      })
    }// End of global scope
  }


  /**
   * Test 3
   *
   * This test demonstrates the creation of a variable in a child scope
   *  which will shadow any variable with the same name in its parent scopes
   *
   * */
  it should "create a variable in an inner scope that shadows the outer" in {

    // Create a global scope for the DSL
    val globalScope = Scope { g =>

      //Assign a variable "x" to a 1 on global level
      g.Assign.Variable("x").toValue(1)
      g.Scope(s => {

        // Assign "x" to 2 in this scope
        // This will shadow the outer "x"
        s.Assign.Variable("x").toValue(2)


        // Test
        s.Variable("x").getValue shouldBe 2

        // You can still access the outer scope's variable
        //  using the scope instance of the outer variable
        //    For example in this case:
        g.Variable("x").getValue shouldBe 1
      })
    }// End of global scope
  }


  /**
   * Test 4
   *
   * This test demonstrates nested scoping and how they obey shadowing rules
   *
   * */
  it should "create nested scopes and obey variable shadowing" in {

    //Global Level
    Scope{g=>

      g.Assign.Variable("x").toValue(1)
      g.Assign.Variable("y").toValue(100)


      // Level 1
      g.Scope{s1=>

        // This will shadow "x" from the global scope
        s1.Assign.Variable("x").toValue(2)

        // Level 2
        s1.Scope{s2=>

          // Since "x" was never defined in Level 2 scope
          // It will search for it in the parent scopes
          // In this case the nearest existing binding is in Level 1, which maps "x" to 2
          s2.Variable("x").getValue shouldBe 2


          // Similar to "x", "y" is not defined in the level 2 scope
          // It will search for it in the parent scope
          // The nearest existing binding is in the global scope, which maps "y" to 100
          s2.Variable("y").getValue shouldBe 100

          // Level 3
          s2.Scope{s3=>
            s3.Assign.Variable("y").toValue(101)
            s3.Variable("y").getValue shouldBe 101
            s3.Variable("x").getValue shouldBe 2
          }
        }
      }
    }// End of global scope
  }


  /**
   * Test 5
   *
   * This test shows that child scope variables do not
   *  shadow a variables in the parent scopes
   *
   * */
  it should "not access child scope's variable in parent scope" in {

    // Create a global scope for the DSL
    val globalScope = Scope { g =>

      //Assign a variable "x" to a 1 on global level
      g.Assign.Variable("x").toValue(1)
      g.Scope(s => {

        // Assign "x" to 2 in this scope
        // This will shadow the outer "x"
        s.Assign.Variable("x").toValue(2)
      })

        // Test
        g.Variable("x").getValue shouldBe 1

    }// End of global scope
  }


  /**
   * Test 6
   *
   * Declaring Named Scopes
   *
   * */
  it should "declare a named scope and access its values" in{

    Scope{g=>

      g.Scope("SomeScope", s=>{
        s.Assign.Variable("x").toValue(1)
        s.Assign.Variable("y").toValue(2)
      })

      // Test
      g.Scope("SomeScope").Variable("x").getValue shouldBe 1
      g.Scope("SomeScope").Variable("y").getValue shouldBe 2
    }// End of global scope

  }


  /**
   * Test 7
   *
   * Variable shadowing in named scopes
   *
   * */
  it should "shadow parent scope's variables in named scope" in{

    Scope{g=>

      g.Assign.Variable("x").toValue(0)

      g.Scope("SomeScope", s=>{
        s.Assign.Variable("x").toValue(1)
        s.Assign.Variable("y").toValue(2)
      })

      // Test
      g.Scope("SomeScope").Variable("x").getValue shouldBe 1
      g.Scope("SomeScope").Variable("y").getValue shouldBe 2
      g.Variable("x").getValue shouldBe 0
    }// End of global scope
  }

  /**
   * Test 8
   *
   * Nested named scopes
   *
   * */
  it should "Access named scopes in child scopes" in {

    Scope {g=>

      g.Assign.Variable("a").toValue(0)

      g.Scope("NamedScope1", n1=>{

        n1.Assign.Variable("b").toValue(1)
        n1.Assign.Variable("c").toValue(2)

        n1.Scope("NamedScope2", n2=>{

          n2.Assign.Variable("b").toValue(3) //shadowed
          n2.Assign.Variable("d").toValue(4)
        })
      })

      // Tests
      // Test if NamedScope1 can access outer variable "a"
      g.Scope("NamedScope1").Variable("a").getValue shouldBe 0
      // Test is NamedScope2 can access outer variable "a"
      g.Scope("NamedScope1").Scope("NamedScope2").Variable("a").getValue shouldBe 0
      // Test if NamedScope2 can access outer NamedScope1's variables
      g.Scope("NamedScope1").Scope("NamedScope2").Variable("c").getValue shouldBe 2
      // Test if NamedScope2 shadows NamedScope1's variable "b"
      g.Scope("NamedScope1").Scope("NamedScope2").Variable("b").getValue shouldBe 3
      // Test access to inner scope variable
      g.Scope("NamedScope1").Scope("NamedScope2").Variable("d").getValue shouldBe 4
    }// End of global scope

  }




}
