import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable
import SetLangDSL.DSL.*

class ExceptionsTests extends AnyFlatSpec with Matchers{
  behavior of "Exceptions"

  // Function to compare values
  def compareValues(actualValue: Any, expectedValue: Any): Boolean = if actualValue == expectedValue then true else false

  
  // Catch exception in the same scope
  it should "raise and catch an exception in the same scope" in {

    val globalScope = Scope{g=>

      g.ExceptionClassDef("SomeException", f=>{
        f.AssignVariable("reason").toValue("if you see this, you passed the test")
      })

      g.AssignVariable("SomeSet").Insert(1)

      g.ThrowException("SomeException")

      // This statement will not be executed because an exception was thrown
      g.Variable("SomeSet").Insert(2)

      g.Catch("SomeException", f=>{

        g.Variable("SomeSet").Insert(3)
        //println("Test: Exception Caught")

      })

      //After the exception is caught the normal flow of the program resumes
      g.Variable("SomeSet").Insert(4)
    }

    //To access the Variable "SomeSet" from outside the defined scope
    val actualValue = globalScope.Variable("SomeSet").getValue.asInstanceOf[mutable.Set[Any]]
    // Test the actual value
    val expectedValue = mutable.Set[Any](1,3,4)
    compareValues(actualValue, expectedValue) shouldBe true
  }
  
  
  // Exceptions with nested scopes
  it should "Raise an exception and catch it in the parent scope" in {

    val globalScope = Scope{g=>

      g.ExceptionClassDef("SomeException", f=>{
        f.AssignVariable("reason").toValue("if you see this, you passed the test")
      })

      g.AssignVariable("SomeSet").Insert(1)

      g.Scope(f=>{
        f.Variable("SomeSet").Insert(2)

        // Throw exception
        f.ThrowException("SomeException")

        // This should be skipped
        f.Variable("SomeSet").Insert(3)
      })

      // This should be skipped
      g.Variable("SomeSet").Insert(4)

      g.Catch("SomeException", f=>{

        //This will not be skipped
        f.Variable("SomeSet").Insert(5)
      })
    }

    //To access the Variable "SomeSet" from outside the defined scope
    val actualValue = globalScope.Variable("SomeSet").getValue.asInstanceOf[mutable.Set[Any]]
    // Test the actual value
    val expectedValue = mutable.Set[Any](1,2,5)
    compareValues(actualValue, expectedValue) shouldBe true
  }

  // Additional test for testing extra scope nesting with exceptions
  it should "Raise an exception and then catch it in the outermost scope" in {

    val globalScope = Scope{g=>

      g.ExceptionClassDef("SomeException", f=>{
        f.AssignVariable("reason").toValue("if you see this, you passed the test")
      })

      g.AssignVariable("SomeSet").Insert(1)

      g.Scope(f=>{
        f.Variable("SomeSet").Insert(2)

        f.Scope(f1=>{

          f.Variable("SomeSet").Insert(3)

          // Throw exception
          f1.ThrowException("SomeException")

        })

        // This should be skipped
        f.Variable("SomeSet").Insert(4)
      })

      // This should be skipped
      g.Variable("SomeSet").Insert(5)

      g.Catch("SomeException", f=>{

        //This will not be skipped
        f.Variable("SomeSet").Insert(6)
      })
    }

    //To access the Variable "SomeSet" from outside the defined scope
    val actualValue = globalScope.Variable("SomeSet").getValue.asInstanceOf[mutable.Set[Any]]
    // Test the actual value
    val expectedValue = mutable.Set[Any](1,2,3,6)
    compareValues(actualValue, expectedValue) shouldBe true

  }

}
