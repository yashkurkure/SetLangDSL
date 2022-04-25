******# SetLangDSL

*Version: Homework 5
By Yash Kurkure  
NetID: ykurku2*

# What to import?
You must import three things:
1. import import SetLangDSL.DSL.* => this imports DSL object, which acts as an entry point into the DSL
2. import import SetLangDSL._ => this imports required constructs like Value, accessSpecifiers etc

# Quick Links
- Homework 1
	- [Value](https://github.com/yashkurkure/SetLangDSL#value)
	- [Variable](https://github.com/yashkurkure/SetLangDSL#variable)
	- [AssignVariable](https://github.com/yashkurkure/SetLangDSL#assignvariable)
	- [Scope](https://github.com/yashkurkure/SetLangDSL#scope)
	- [Macro](https://github.com/yashkurkure/SetLangDSL#macro)
	- [Set Operations](https://github.com/yashkurkure/SetLangDSL#set-operations)
	- 
- Homework 2
	- [Classes and Methods](https://github.com/yashkurkure/SetLangDSL#classes-and-methods)
	- [Class Inheritance]()
- Homework 3
	- [Interfaces](https://github.com/yashkurkure/SetLangDSL#interfaces)
	- [Abstact Classes]()
- Homework 4
	-[Condtional Control Flow](https://github.com/yashkurkure/SetLangDSL#conditional-control-flow)
	-[Throwing and Catching Exceptions](https://github.com/yashkurkure/SetLangDSL#throwing-and-catching-exceptions)
- Homework 5
	- [Partial Evaluation of Scopes](https://github.com/yashkurkure/SetLangDSL/tree/Homework5#partial-evaluation-scopes)
	- [Map](https://github.com/yashkurkure/SetLangDSL/tree/Homework5#using-map)



# How this DSL works?

You start of with a global scope, using the calls Scope{f: ScopeDefiniton => Unit} : ScopeDefinition

The function Scope takes a function as an argument that has ScopeDefinition as the argument.

All the DSL statements go inside this function f. 

ScopeDefinition consists of the bindings and statements that will be evaluated at runtime itself.
The function f is called inside Scope{} which leads to the lazy evaluation of the statements written in f.

## Value
Value is a class defined in SetLangDSL.DSL

The class's constructor takes one argument of type **Any**
```  
Class Value(value: Any)
```  

Value is the fundamental type of the DSL that acts as a container to hold any type used in Scala.
For example:
```
// Hold an integer
Value(1)

//Hold a string
Value("some string")

//Hold a set
Value(mutable.Set[Any](1,2,3,4))
```

Most of the operations in the DSL will return an instance of Value

One can access the actual scala type value by using asInstanceOf on the return value of getValue:
```
//For example if we have a set
val setAsValue = Value(mutable.Set[Any](1,2,3,4))

//To get the mutable.Set instance
val set = setAsValue.getValue.asInstanceOf[mutable.Set[Any]]
```

In this implementation the setOperations like Insert() and Delete() are to be done on the value (the type checking would ensure it only works on a set)  
It was designed this way because Insert() and Delete() is a function of a set

## Variable
variable is defined in multiple classes, and depending on the usage the return value changes
```  
Variable(name: String) => Value //to get the value of a variable in a certain Context  
```  
Any variable that exists in the binding table of a context can be accessed using Variable().  
If you call Variable() on a name that is not bound to a value, it would return VariableNotFound(name: String).

Note: For variables in scopes, check out Scope()

##### Usage
```  
Scope{g=>  
 //In this case Vairable would reuturn an intermediate class in the binding process called IncompleteScopeBinding() g.Assign.Variable("x").toValue(1) //assigns x to 1   //In this case the return type of Variable() is Value()  
 println(g.Variable("x").getValue) //prints value of x}  
  
```  
## AssignVariable

AssignVairable returns the Binding Instance of the scope named ScopeBindings  
The ScopeBinding has method Variable() to create binding of variables Variables

```  
AssignVariable: ScopeBindings  
```  
AssignVariable is used to assign values to variables.

##### Some examples of usage
```  
Scope{g=>  
 g.Assign.Variable("x").toValue(1) //assigns x to 1}  
  
```  

## Scope
```  
Scope(name:String, body: construct) => construct  
Scope(name: String) => construct  
```  
There are two parts to using scopes.

First you need to create one, this will create a binding of the scope "name" to the scope object. The scope object will store the bindings of the interiors of the scope and a reference to the parent scope.
```  
// Creating a scope  
Scope("myScope", Assign(Variable("myVar"), Value(1)).eval()  
//at this point you have created a binding for the scope  
```  

Now, the second part is to execute the scope. You can execute the scope we created above as follows:
```  
Scope("myScope")  
this will return the value of Assign(..) inside the scope body  
```  
Once the above scope is executed it will bind the variable "myVar" to 1 in the LOCAL scope.  
"myVar" cannot be accessed from outside the scope.

Some rules on variable shadowing. Take the following code snippet as an example:
```  
Assign(Variable("v"), Value(1)).eval()  
Scope("myScope", Variable("v")).eval()  
Scope("myScope").eval() => would return value 1  
  
//lets say we have another scope  
Scope("myScope2", Assign(Variable("v"), Value("a"))).eval() //declare scope  
Scope("myScope").eval() //execute scope  
//In the above case, the value of v would be "a"  
  
//Here is a way to print the value of a variable from a certain scope  
//first get the scope instance  
val scopeInstance = Variable("myScope2").eval().getValue().asInstanceOf[scope]  
Variable("v").evalInScope(scopeInstance).getValue() //would return "a"  
```  
The simple rule is that when a variable is searched for in a scope, the program will first check the local scope, if it is not found it will look up the scope tree till it reaches the global scope.  
The nearest scope with an active binding of the variable would be accessed.

## Macro

Intended use is:
```  
Scope { g =>  
 g.Assign.Variable("mySet").Insert("a", "b", "c") g.Assign.Variable("myMacro").toMacro(v => { v.Delete("b") }) g.ExecuteMacro("myMacro", g.Variable("mySet")) }
 ```  

## Set operations
All the set operations work with the Assign() statement.  
The package supports:  
Delete(), Insert(), Union(), Intersection(), Difference(), SymmetricDifference(), CartesianProduct()

Assume the general format is SetOperation(set1: Value, set2: Value), where Value represents the sets (type checking will be done inside the method)  
except for Insert, which would be Insert(<values>)
```
//Usage  
val globalScope = Scope{g=>  
  
 //Assign a variable "set1" to a Set("a") g.Assign.Variable("set1").Insert("a","b","c") g.Assign.Variable("set2").Insert("b","c","d","e","f") g.Assign.Variable("result").SetOperation(g.Variable("set1"), g.Variable("set2"))
```

## Classes and Methods

Example Use:
```  
Scope{g=>  
  
 g.ClassDef("numbers", c=>{  
 //Creating class variables c.Assign.Variable(Private, "privateNumber").toValue(1) c.Assign.Variable(Protected, "protectedNumber").toValue(2) c.Assign.Variable(Public, "publicNumber1").toValue(3) c.Assign.Variable("publicNumber2").toValue(4)  
 //Class has default constructor  
 //Creating class methods //getTheNumber returns: // classVariable publicNumber1 if parameter whichNumber == 1 // classVariable publicNumber2 if parameter whichNumber == 2 c.Method(Public, "getTheNumber", Parameters("whichNumber"), m=>{  
 // The advantage of using an hierarchy of methods and classes for the DSL // The user is able to use the if then else construct of scala if m.Variable("whichNumber").equals(Value(1)) then c.Variable("publicNumber1") else c.Variable("publicNumber2") })  
 })  
 //Creating a class object g.Assign.Variable("myObject").NewObject("numbers")  
 //Executing a method of a class object // result1 should be assigned to Value(4) g.Assign.Variable("result1").Method("getTheNumber", g.Variable("myObject")).withParameters(Value(2)).Execute }
 ```

## Interfaces

Classes and Interfaces can implement interfaces. Implementation of only 1 interface is allowed per class or interface.

Example Use: Class Implements Interface
This can be found in InterfaceTests.scala
```
Scope{g=>  
  
  g.InterfaceDef("interface", {i=>  
  
    i.AssignVariable("x")  
  
    i.Method(Public, "implementMe", Parameters())  
  
  })  
  
  g.ClassDef("class", Implements("interface"), {c=>  
  
    // You can try commenting out any of the below  
 // If something is not implement it will throw an exception  c.AssignVariable("x").toValue(1)  
    c.Method(Public, "implementMe", Parameters(), {m=>  
      Value(1)  
    })  
  })

	// Create an instance of the class  
	g.AssignVariable("obj").toNewObjectOf("class")  
	
	//Tests
	//g.Variable("obj").getField("x").getValue shouldBe 1  
	//g.Variable("obj").getMethod("implementMe").Execute.getValue shouldBe 1  
}
```
Example Use: Interface2 implements Interface1 then some class implements Interface2
```
Scope{g=>  
  
  g.InterfaceDef("interface1", {i=>  
  
    i.AssignVariable("x")  
  
    i.Method(Public, "implementMe", Parameters())  
  
  })  
  
  g.InterfaceDef("interface2", Implements("interface1"), i=>{  
  
    i.AssignVariable("y")  
  
    i.Method(Public, "implementMe2", Parameters())  
  
  })  
  
  g.ClassDef("implemented", Implements("interface2"), c=>{  
  
    // These were a part of interface 1 that interface 2 inherited  
  c.AssignVariable("x").toValue(1)  
    c.Method(Public, "implementMe", Parameters(), {m=>  
      Value(1)  
    })  
  
    // These were the additional ones defined in interface 2, after it implemented interface1  
  c.AssignVariable("y").toValue(2)  
    c.Method(Public, "implementMe2", Parameters(), {m=>  
      Value(2)  
    })  
  
  
  })  
  
  // Create an instance of the class  
  g.AssignVariable("obj").toNewObjectOf("implemented")  
  
  //Tests
  //g.Variable("obj").getField("x").getValue shouldBe 1  
  //g.Variable("obj").getMethod("implementMe").Execute.getValue shouldBe 1  
  //g.Variable("obj").getField("y").getValue shouldBe 2  
  //g.Variable("obj").getMethod("implementMe2").Execute.getValue shouldBe 2  
}
```

## Conditional Control Flow

The DSL allows a Conditional statement that behaves like the if else construct

The below example can be found in ControlStructureTests.scala

Example Usage:

```
// Create a global scope for the DSL  
val globalScope = Scope{g=>  
  
  g.AssignVariable("set1").Insert(1,2)  
  
  g.AssignVariable("x").toValue(1)  
  
  g.Conditional(  
    g.Variable("x"), // Expression  
  t=>{  
      // if the expression is true, then delete set member 1  
  t.Variable("set1").Delete(1)  
  },  
  f=>{  
  
      //else if the epression is false, then delete set member 2  
  f.Variable("set1").Delete(2)  
    })  
  
}// End of global scope
```
## Throwing and Catching Exceptions

Exceptions can be thrown using Throw(className: String) and caught using Catch(className: String, f: ScopeDefinition=>Unit)

But before that you will have to declare an exception class using ExceptionClass(className: String, f: ClassDefinition=>Unit)

```
val globalScope = Scope{g=>  
  
  // Declare the exception class
  g.ExceptionClassDef("SomeException", f=>{  
    f.AssignVariable("reason").toValue("if you see this, you passed the test")  
  })  
  
  g.AssignVariable("SomeSet").Insert(1)  
  
  // Throw the exception using the exception class's name
  g.ThrowException("SomeException")  
  
  // This statement will not be executed because an exception was thrown  
  g.Variable("SomeSet").Insert(2)  
  
  //Catch block
  // This catch block will catch the excpetion of the class "SomeException"  
  g.Catch("SomeException", f=>{  
  
    g.Variable("SomeSet").Insert(3)  
  
  })  
  
  //After the exception is caught the normal flow of the program resumes  
  g.Variable("SomeSet").Insert(4)  
}
```

Tests can be found in ExceptionTests.scala


## Partial Evaluation Scopes

The partial evaluation scope extends the definition of a normal scope.
The added functionality is the partial evaluation of the statements that depend on undefined variables.
The scope can be evaluated to get a single value just like a function, only in the case all the variables are later decalred by the programmer using the evaluate() method.


A simple tutorial on using Partially Evaluted Scopes

Step 1: Create a PartialScope <br>

Notice that the variables y and b are never decalred in the PartialScope
```

val f = g.PartialScope{p=>
        p.AssignVariable("x").Insert(1,2,3)
        p.AssignVariable("z").Union(p.Variable("x"), p.Variable("y"))
        p.AssignVariable("a").Union(p.Variable("z"), p.Variable("b"))
      }

```

Step 2; Define the variables y and b

This can be done in 2 ways

Way 1: Both at the same time and then evalaute
```

f.evaluate{p=>  
    p.AssignVariable("y").Insert(4,5,6)
    p.AssignVariable("b").Insert(7,8,9)
}

```
This will evaluate the partial scope with the given values of y and b inside the arguement of evaluate.

Way 2: Define y and b separately
```
    g.AssignVariable("result1").toValue(f.evaluate{p=>
        p.AssignVariable("y").Insert(4,5,6)
      })

      g.AssignVariable("result2").toValue(f.evaluate{p=>
        p.AssignVariable("b").Insert(7,8,9)
      })
```

You can directly assign the value that evalute spits out to some variable. But notice that we first decalred y and the declared b.

In this case for the variable result1, the scope is still only partially evaluted. Thus, the value of result1 will be null.

But in the case of varaiable result2, the scope get completly evaluated. The complete evaluation only occurs when all the undefinedd variables get mapped to some value in the scope. The value of reuslt 2 will be of type DSL.Value

## Using Map

The primary type that the DSL deals with is Value. The Value class can wrap any Scala type inside it, to be used inside the DSL.

Map is defined to take a function Value=>Value. Map can only be called on Sets, so the function Map will check if the Value on which it is being called on is a type of mutable.Set[_] 

Example:
```
Scope{g=>
      g.AssignVariable("set1").Insert(1,2,3,4)

      g.AssignVariable("set2").toValue(g.Variable("set1").Map{v=>Value(v.getValue.asInstanceOf[Int]+1)})
     }
```

The argument to the Map function is: v=>Value(v.getValue.asInstanceOf[Int] + 1)

A disadvantage of this DSL is that the user has to convert and remember the types of their variables. This can be seen in the situation above where we had to use asInstanceOf[Int] to be able to use the '+' operator. Since it does not work on the type Any.


