# SetLangDSL

Version: Homework 3

By Yash Kurkure

NetID: ykurku2

Note to grader:
My implementation for eveything is differnt from what I did in HW2
The advantages of the new style are:
1. calling eval() everytime is unecessary
2. scala if then else can be used inside the DSL scopes and methods
3. scala println and print methods can also be used inside scopes and methods
4. number of imports went down from 3 to 2
5. Multiline Scopes and Macros

Things that are not working:
1. Extends (Class Inheritance)

# What to import?
You must import three things:
1. import import SetLangDSL.DSL.* => this imports DSL object, which acts as an entry point into the DSL
2. import import SetLangDSL._ => this imports required constructs like Value, accessSpecifiers etc
   

# Usage:

## Value 
value is a class that can hold type Any
In this implementation the setOperations like Insert() and Delete() are to be done on the value (the type checking would ensure it only works on a set)
It was designed this way because Insert() and Delete() is a function of a set
```
Class Value(value: Any)
```
All values in the DSL are represented by Value(). Any expression that evaluates something will return Value(value)
To get the "actual value" you can use the getValue() method.
##### Usage
```
Value(1).getValue //returns 1
Value("a").getValue //returns "a"
Value(HashSet("a","b","c")).getValue //returns HashSet("a","b","c")
```

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
  //In this case Vairable would reuturn an intermediate class in the binding process called IncompleteScopeBinding()
  g.Assign.Variable("x").toValue(1) //assigns x to 1
  
  //In this case the return type of Variable() is Value()
  println(g.Variable("x").getValue) //prints value of x
}

```
## Assign

Assign returns the Binding Instance of the scope named ScopeBindings
The ScopeBinding has method Variable() to create binding of variables Variables

```
Assign: ScopeBindings
```
Assign is used to assign values to variables.

##### Some examples of usage
```
Scope{g=>
  g.Assign.Variable("x").toValue(1) //assigns x to 1
}

```

### Delete
```
Delete(value:Value): Value
```
Delete cannot be used by itself. It must be used on a set value like shown below

##### Usage examples:
```
Scope{g=>
  g.Variable("someSet").Delete(Value("a"))
  or
  g.Variable("someSet").Delete("a")
}
```


### Scope
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

### Macro

Intended use is:
```
Scope { g =>
  g.Assign.Variable("mySet").Insert("a", "b", "c")
  g.Assign.Variable("myMacro").toMacro(v => {
        v.Delete("b")
      })
  g.ExecuteMacro("myMacro", g.Variable("mySet"))
    }
```

### Usage of the set operations
All the set operations work with the Assign() statement.
The package supports:
Insert(), Union(), Intersection(), Difference(), SymmetricDifference(), CartesianProduct()

Assume the general format is SetOperation(set1: Value, set2: Value), where Value represents the sets (type checking will be done inside the method)
except for Insert, which would be Insert(<values>)
```
//Usage
val globalScope = Scope{g=>

      //Assign a variable "set1" to a Set("a")
      g.Assign.Variable("set1").Insert("a","b","c")
      g.Assign.Variable("set2").Insert("b","c","d","e","f")
      g.Assign.Variable("result").SetOperation(g.Variable("set1"), g.Variable("set2"))
    }
```
Classes and Methods
 
Example Use:
```
Scope{g=>

      g.ClassDef("numbers", c=>{

        //Creating class variables
        c.Assign.Variable(Private, "privateNumber").toValue(1)
        c.Assign.Variable(Protected, "protectedNumber").toValue(2)
        c.Assign.Variable(Public, "publicNumber1").toValue(3)
        c.Assign.Variable("publicNumber2").toValue(4)

        //Class has default constructor

        //Creating class methods
        //getTheNumber returns:
        // classVariable publicNumber1 if parameter whichNumber == 1
        // classVariable publicNumber2 if parameter whichNumber == 2
        c.Method(Public, "getTheNumber", Parameters("whichNumber"), m=>{

          // The advantage of using an hierarchy of methods and classes for the DSL
          // The user is able to use the if then else construct of scala
          if m.Variable("whichNumber").equals(Value(1)) then
            c.Variable("publicNumber1")
          else
            c.Variable("publicNumber2")
        })

      })

      //Creating a class object
      g.Assign.Variable("myObject").NewObject("numbers")

      //Executing a method of a class object
      // result1 should be assigned to Value(4)
      g.Assign.Variable("result1").Method("getTheNumber", g.Variable("myObject")).withParameters(Value(2)).Execute
    }
```
  

